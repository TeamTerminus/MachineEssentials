package net.teamterminus.machineessentials.network;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.teamterminus.machineessentials.MachineEssentials;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A network of blocks.
 */
public class Network {
    
    public static final Vec3i[] OFFSETS = new Vec3i[]{
            new Vec3i(0, 1, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, 1),
    };

    protected final Map<Vec3i, NetworkComponentBlock> networkBlocks = Maps.newHashMap();
    protected final Map<Vec3i, BlockEntry> blocks = Maps.newHashMap();
    protected final World world;
    protected final int id;
    protected final NetworkPathMap NET_PATH_DATA = new NetworkPathMap();
    protected final Random random;
    public final @NotNull NetworkType type;

    public Network(World world, @NotNull NetworkType type) {
        this(world, NetworkManager.getUID(), type);
    }

    private Network(World world, int id, @NotNull NetworkType type) {
        this.world = world;
        this.id = id;
        this.type = type;
        this.random = new Random(id);
    }

    /**
     * Returns a list of paths possible in this network from the current position.
     * @param pos The position to start from
     * @return List of possible <code>NetworkPath</code>s sorted according to their distance from <code>pos</code>
     */
    public List<NetworkPath> getPathData(Vec3i pos){
        List<NetworkPath> routes = NET_PATH_DATA.get(pos);
        if (routes == null){
            routes = NetworkWalker.createNetworkPaths(world, pos);
            if (routes == null){
                return Collections.emptyList();
            }
            routes.sort(Comparator.comparingInt(NetworkPath::getDistance));
            NET_PATH_DATA.put(pos, routes);
        }
        return routes;
    }

    public int getSize() {
        return blocks.size();
    }

    public boolean existsOnPos(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        return blocks.containsKey(pos);
    }

    public void addBlock(int x, int y, int z) {
        Block block = world.getBlockState(x, y, z).getBlock();
        byte meta = (byte) world.getBlockMeta(x, y, z);

        Vec3i pos = new Vec3i(x, y, z);
        blocks.put(pos, new BlockEntry(block, meta));
        if (block instanceof NetworkComponentBlock) {
            networkBlocks.put(pos, (NetworkComponentBlock) block);
            if (world.getBlockEntity(x, y, z) instanceof NetworkComponent){
                ((NetworkComponent) world.getBlockEntity(x, y, z)).networkChanged(this);
            }
            update();
        }
        NET_PATH_DATA.clear();
    }

    public List<Network> removeBlock(int x, int y, int z) {
        Vec3i pos = new Vec3i(x, y, z);
        NetworkComponentBlock component = networkBlocks.get(pos);
        if (component != null) {
            if (world.getBlockEntity(x, y, z) instanceof NetworkComponent){
                ((NetworkComponent) world.getBlockEntity(x, y, z)).removedFromNetwork(this);
            }
        }
        networkBlocks.remove(pos);
        blocks.remove(pos);
        update();

        List<Vec3i> sideNets = new ArrayList<>(6);
        for (byte i = 0; i < 6; i++) {
            Vec3i offset = OFFSETS[i];
            Vec3i side = new Vec3i(x + offset.getX(), y + offset.getY(), z + offset.getZ());
            if (blocks.containsKey(side)) {
                sideNets.add(side);
            }
        }

        List<Set<Vec3i>> preNets = new ArrayList<>();
        boolean[] ignore = new boolean[sideNets.size()];
        for (byte i = 0; i < ignore.length; i++) {
            if (ignore[i]) {
                continue;
            }
            Vec3i startBlock = sideNets.get(i);
            Set<Vec3i> netBlocks = floodFill(startBlock);
            preNets.add(netBlocks);
            if (i < ignore.length - 1) {
                for (byte j = (byte) (i + 1); j < ignore.length; j++) {
                    if (netBlocks.contains(sideNets.get(j))) {
                        ignore[j] = true;
                    }
                }
            }
        }

        final int size = preNets.size();
        if (size < 2) {
            return null;
        }

        List<Network> result = new ArrayList<>(size);
        for (Set<Vec3i> preNet : preNets) {
            Network sideNet = new Network(world, type);

            preNet.forEach(blockPos -> {
                sideNet.blocks.put(blockPos, blocks.get(blockPos));
                NetworkComponentBlock netBlock = networkBlocks.get(blockPos);
                if (netBlock != null) {
                    sideNet.networkBlocks.put(blockPos, netBlock);
                    BlockEntity blockEntity = world.getBlockEntity(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    if (blockEntity instanceof NetworkComponent){
                        ((NetworkComponent) blockEntity).networkChanged(sideNet);
                    }
                }
            });

            if (sideNet.getSize() > 1) {
                result.add(sideNet);
                sideNet.update();
            }
        }
        NET_PATH_DATA.clear();
        return result;
    }

    public void mergeNetwork(Network net) {
        if (net.isOfSameType(net)) {
            blocks.putAll(net.blocks);
            networkBlocks.putAll(net.networkBlocks);
        }
        networkBlocks.forEach((pos, networkComponent) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos.getX(), pos.getY(), pos.getZ());
            if (blockEntity instanceof NetworkComponent){
                ((NetworkComponent) blockEntity).networkChanged(net);
            }
        });
        NET_PATH_DATA.clear();
    }

    public NbtCompound toTag() {
        NbtCompound net = new NbtCompound();
        NbtList positions = new NbtList();
        net.put("blocks", positions);
        net.putInt("id", id);
        net.putString("type", type.id());

        blocks.forEach((pos, entry) -> {
            NbtCompound tag = new NbtCompound();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            tag.putInt("id", entry.block.id);
            tag.putInt("meta", entry.meta);
            positions.add(tag);
        });

        return net;
    }

    public static Network fromTag(World world, NbtCompound root) {
        int id = root.getInt("id");
        NbtList positions = root.getList("blocks");
        NetworkType networkType = new NetworkType(root.getString("type"));
        Network net = new Network(world, id, networkType);

        final int size = positions.size();
        for (int i = 0; i < size; i++) {
            NbtCompound tag = (NbtCompound) positions.get(i);
            Block block = Block.BLOCKS[tag.getInt("id")];
            if (block != null) {
                int x = tag.getInt("x");
                int y = tag.getInt("y");
                int z = tag.getInt("z");
                byte meta = tag.getByte("meta");
                net.blocks.put(new Vec3i(x, y, z), new BlockEntry(block, meta));
                if (NetworkManager.canBeNet(block)){
                    net.networkBlocks.put(new Vec3i(x, y, z), (NetworkComponentBlock) block);
                }
            }
        }

        net.update();

        return net;
    }

    private Set<Vec3i> floodFill(Vec3i start) {
        List<Set<Vec3i>> edges = new ArrayList<>();
        Set<Vec3i> result = new HashSet<>();
        edges.add(MachineEssentials.setOf(start));
        edges.add(new HashSet<>());

        byte n = 0;
        boolean added = true;
        while (added) {
            Set<Vec3i> oldEdge = edges.get(n & 1);
            Set<Vec3i> newEdge = edges.get((n + 1) & 1);
            n = (byte) ((n + 1) & 1);
            oldEdge.forEach(pos -> {
                for (byte i = 0; i < 6; i++) {
                    Vec3i offset = OFFSETS[i];
                    Vec3i side = new Vec3i(pos.getX() + offset.getX(), pos.getY() + offset.getY(), pos.getZ() + offset.getZ());
                    if (blocks.containsKey(side) && !result.contains(side)) {
                        newEdge.add(side);
                    }
                }
            });
            added = !oldEdge.isEmpty();
            result.addAll(oldEdge);
            oldEdge.clear();
        }

        return result;
    }

    public void update() {
        networkBlocks.forEach((pos, networkComponent) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos.getX(), pos.getY(), pos.getZ());
            if (blockEntity instanceof NetworkComponent){
                ((NetworkComponent) blockEntity).networkChanged(this);
            }
        });
    }

    public boolean isOfSameType(NetworkComponentBlock component){
        return component.getType().equals(type);
    }

    public boolean isOfSameType(Network net){
        return net.type.equals(type);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Network net) {
            Optional<Vec3i> optional = net.blocks.keySet().stream().findAny();
            if (optional.isPresent()) {
                Vec3i pos = optional.get();
                return blocks.containsKey(pos);
            }
        }
        return false;
    }

    public String toString() {
        return String.format("[ID: %d, Size: %d]", id, networkBlocks.size());
    }

    protected static class BlockEntry {
        Block block;
        byte meta;

        private BlockEntry(Block block, byte meta) {
            this.block = block;
            this.meta = meta;
        }
    }

}
