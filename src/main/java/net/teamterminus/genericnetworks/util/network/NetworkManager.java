package net.teamterminus.genericnetworks.util.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.util.math.Vec3i;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkManager {
	public static final Vec3i[] OFFSETS = new Vec3i[] {
		new Vec3i(0, -1, 0),
		new Vec3i(0, 1, 0),
		new Vec3i(0, 0, -1),
		new Vec3i(0, 0, 1),
		new Vec3i(-1, 0, 0),
		new Vec3i(1, 0, 0)
	};
	
	private static final Map<Dimension, Set<Network>> NETS = Maps.newHashMap();
	private static final AtomicInteger ID_PROVIDER = new AtomicInteger(0);
	
	public static int getNetID(World world, int x, int y, int z) {
		Network net = getNet(world, x, y, z);
		return net == null ? -1 : net.hashCode();
	}
	
	public static void addBlock(World world, int x, int y, int z) {
		Set<Network> nets = NETS.computeIfAbsent(world.dimension, i -> Sets.newHashSet());
		
		Set<Network> sideNets = Sets.newHashSet();
		for (Network net: nets) {
			for (Vec3i offset: OFFSETS) {
				int px = x + offset.getX();
				int py = y + offset.getY();
				int pz = z + offset.getZ();
				if (net.existsOnPos(px, py, pz)) {
					sideNets.add(net);
				}
			}
		}
		
		Network net = null;
		int size = sideNets.size();
		if (size == 0) {
			net = new Network(world);
			net.addBlock(x, y, z);
			for (Vec3i offset: OFFSETS) {
				int px = x + offset.getX();
				int py = y + offset.getY();
				int pz = z + offset.getZ();
				if (canBeNet(world, px, py, pz)) {
					net.addBlock(px, py, pz);
				}
			}
			if (net.getSize() > 1) {
				nets.add(net);
			}
		}
		else if (size == 1) {
			net = sideNets.stream().findAny().get();
			net.addBlock(x, y, z);
		}
		else {
			Network[] netsArray = sideNets.toArray(new Network[size]);
			Network main = netsArray[0];
			main.addBlock(x, y, z);
			for (byte i = 1; i < netsArray.length; i++) {
				main.mergeNetwork(netsArray[i]);
				nets.remove(netsArray[i]);
			}
			net = main;
		}
		
		if (net != null) {
			for (Vec3i offset: OFFSETS) {
				int px = x + offset.getX();
				int py = y + offset.getY();
				int pz = z + offset.getZ();
				if (canBeNet(world, px, py, pz) && getNet(world, px, py, pz) == null) {
					net.addBlock(px, py, pz);
				}
			}
		}
	}
	
	public static void removeBlock(World world, int x, int y, int z) {
		Set<Network> nets = NETS.get(world.dimension);
		
		if (nets == null) {
			return;
		}
		
		Network target = null;
		for (Network net: nets) {
			if (net.existsOnPos(x, y, z)) {
				target = net;
				break;
			}
		}
		
		if (target != null) {
			List<? extends Network> sideNets = target.removeBlock(x, y, z);
			if (sideNets != null) {
				nets.remove(target);
				nets.addAll(sideNets);
			}
			else if (target.getSize() < 2) {
				nets.remove(target);
			}
		}
	}
	
	public static int getUID() {
		return ID_PROVIDER.getAndIncrement();
	}
	
	public static void netsToTag(World world, NbtCompound root) {
		Set<Network> nets = NETS.get(world.dimension);
		NbtCompound dimTag = new NbtCompound();
		root.put("dim" + world.dimension.id, dimTag);
		
		if (nets == null) {
			return;
		}
		
		NbtList netsList = new NbtList();
		dimTag.put("nets", netsList);
		nets.forEach(network -> {
			netsList.add(network.toTag());
		});
	}
	
	public static void netsFromTag(World world, NbtCompound root) {
		Set<Network> nets = Sets.newHashSet();
		NETS.put(world.dimension, nets);
		
		NbtCompound dimTag = root.getCompound("dim" + world.dimension.id);
		if (dimTag == null) {
			return;
		}
		
		NbtList netsList = dimTag.getList("nets");
		final int size = netsList.size();
		for (int i = 0; i < size; i++) {
			Network net = Network.fromTag(world, (NbtCompound) netsList.get(i));
			if (net.getSize() > 1) {
				nets.add(net);
			}
		}
	}
	
	public static boolean canBeNet(World world, int x, int y, int z) {
		Block block = Block.BLOCKS[world.getBlockId(x, y, z)];
		return canBeNet(block);
	}
	
	public static boolean canBeNet(BlockView world, int x, int y, int z) {
		Block block = Block.BLOCKS[world.getBlockId(x, y, z)];
		return canBeNet(block);
	}
	
	public static boolean canBeNet(Block block) {
		return true;//block instanceof CableBlock || block instanceof BlockWithEnergy;
	}
	
	private static Network getNet(World world, int x, int y, int z) {
		Set<Network> nets = NETS.get(world.dimension);
		if (nets != null) {
			for (Network net: nets) {
				if (net.existsOnPos(x, y, z)) {
					return net;
				}
			}
		}
		return null;
	}
}
