package net.teamterminus.machineessentials.network;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import lombok.Getter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.teamterminus.machineessentials.MachineEssentials;

import java.util.*;

/**
 * Travels across a network recording possible paths between its components.
 * <p>
 * This class uses <code>NetworkWire</code> to differentiate between the medium (wires) and endpoints (devices) of a network to build paths.
 * @param <T> Any type that extends <code>NetworkComponent</code>
 */
public class NetworkWalker<T extends NetworkComponent> {

    protected NetworkWalker<T> root;
    protected final World world;
    protected Set<T> walkedConduits;
    protected final List<Direction> nextConduitDirections = new ArrayList<>(Direction.values().length-1);
    protected final List<T> nextConduits = new ArrayList<>(Direction.values().length-1);
    protected List<NetworkWalker<T>> walkers;
    protected Vec3i currentPos;
    protected T currentConduit;
    protected Direction from = null;
    @Getter
    protected int walkedBlocks;
    protected boolean used;
    protected boolean running;
    @Getter
    protected boolean failed = false;

    protected T[] conduits;
    protected final List<NetworkPath> routes;

    public NetworkWalker(World world, Vec3i source, int walkedBlocks, List<NetworkPath> routes) {
        this.world = world;
        this.walkedBlocks = walkedBlocks;
        this.currentPos = source;
        this.root = this;
        this.routes = routes;
    }

    public static <T extends NetworkComponent> List<NetworkPath> createNetworkPaths(World world, Vec3i source) {
        if (world.getBlockEntity(source.getX(), source.getY(), source.getZ()) instanceof NetworkComponent) {
            NetworkWalker<T> walker = new NetworkWalker<>(world, source, 1, new ArrayList<>());
            walker.traverse();
            return walker.isFailed() ? null : walker.routes;
        }
        return null;
    }

    protected NetworkWalker<T> createSubWalker(World world, Direction nextDir, Vec3i nextPos, int walkedBlocks) {
        NetworkWalker<T> subWalker = new NetworkWalker<>(world, nextPos, walkedBlocks, routes);
        subWalker.conduits = conduits;
        return subWalker;
    }

    /**
     * By default, will travel at most 32768 blocks.
     */
    public void traverse() {
        traverse(32768);
    }

    /**
     * Traverse the network until <code>max</code> is reached or there aren't any more valid paths.
     * @param max The maximum amount of blocks to traverse
     */
    public void traverse(int max) {
        if (used) {
            throw new IllegalStateException("Walker already used!");
        }
        root = this;
        walkedConduits = new HashSet<>();
        int i = 0;
        running = true;
        //runs purely on side effects
        //noinspection StatementWithEmptyBody
        while (running && !walk() && i++ < max);
        running = false;
        walkedConduits = null;
        if (i >= max) {
            MachineEssentials.LOGGER.error("Walker reached maximum amount of walks: {}", i);
        }
        used = true;
    }

    protected boolean walk() {
        if (walkers == null) {
            if (!checkPos()) {
                this.root.failed = true;
                return true;
            }

            if (nextConduitDirections.isEmpty()) {
                return true;
            }
            if (nextConduitDirections.size() == 1) {
                currentPos = nextConduits.get(0).getPosition();
                currentConduit = nextConduits.get(0);
                from = nextConduitDirections.get(0).getOpposite();
                walkedBlocks++;
                return isNotRunning();
            }

            walkers = new ArrayList<>();
            for (int i = 0; i < nextConduitDirections.size(); i++) {
                Direction direction = nextConduitDirections.get(i);
                NetworkWalker<T> walker = createSubWalker(world, direction, currentPos.add(new Vec3i(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ())), walkedBlocks + 1);
                walker.root = root;
                walker.currentConduit = nextConduits.get(i);
                walker.from = direction.getOpposite();
                walkers.add(walker);
            }
        }
        Iterator<NetworkWalker<T>> iterator = walkers.iterator();
        while (iterator.hasNext()) {
            NetworkWalker<T> next = iterator.next();
            if (next.walk()) {
                onRemoveSubWalker(next);
                iterator.remove();
            }
        }

        return isNotRunning() || walkers.isEmpty();
    }


    protected boolean checkPos() {
        nextConduitDirections.clear();
        nextConduits.clear();
        if (currentConduit == null) {
            BlockEntity thisConduit = world.getBlockEntity(currentPos.getX(), currentPos.getY(), currentPos.getZ());
            if (!(thisConduit instanceof NetworkWire)) {
                return false;
            }
            currentConduit = (T) thisConduit;
        }
        checkConduit(currentConduit, currentPos);
        root.walkedConduits.add(currentConduit);

        for (Direction direction : getAllowedDirections()) {
            if (direction == from || !currentConduit.isConnected(direction)) {
                continue;
            }

            BlockEntity blockEntity = MachineEssentials.getBlockEntity(direction, world, (BlockEntity) currentConduit);
            if (blockEntity instanceof NetworkWire) {
                T otherConduit = (T) blockEntity;
                if (!otherConduit.isConnected(direction.getOpposite()) || isWalked(otherConduit)) {
                    continue;
                }
                if (isValid(currentConduit, otherConduit, currentPos, direction)) {
                    nextConduitDirections.add(direction);
                    nextConduits.add(otherConduit);
                    continue;
                }
            }
            checkNeighbour(currentConduit, currentPos, direction, blockEntity);
        }
        return true;
    }

    protected void checkNeighbour(T conduit, Vec3i pos, Direction dirToNeighbour, BlockEntity neighbour) {
        if (conduit != conduits[conduits.length -1]) throw new IllegalStateException("Current conduit is not the last one added, you dun goofed.");
        if (!(neighbour instanceof NetworkWire) && neighbour.getBlock() instanceof NetworkComponentBlock) {
            if (neighbour instanceof NetworkComponent networkComponent && networkComponent.getType() == conduit.getType()) {
                NetworkComponent[] path = new NetworkComponent[conduits.length+1];
                System.arraycopy(conduits, 0, path, 0, conduits.length);
                path[path.length-1] = networkComponent;
                routes.add(new NetworkPath(dirToNeighbour, path, getWalkedBlocks()));
            }
        }
    }

    protected boolean isValid(T conduit, T neighbourConduit, Vec3i pos, Direction dirToNeighbour) {
        return conduit.getType() == neighbourConduit.getType();
    }

    protected Direction[] getAllowedDirections() {
        return Direction.values();
    }

    private void checkConduit(T currentConduit, Vec3i currentPos) {
        conduits = ArrayUtils.add(conduits, currentConduit);
    }

    protected void onRemoveSubWalker(NetworkWalker<T> subWalker) {}

    protected boolean isWalked(T conduit) {
        return root.walkedConduits.contains(conduit);
    }

    public void stop() {
        root.running = false;
    }

    public boolean isRoot() {
        return this.root == this;
    }

    public boolean isNotRunning() {
        return !root.running;
    }

}
