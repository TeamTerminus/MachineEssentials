package net.teamterminus.genericnetworks.util.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import net.modificationstation.stationapi.api.util.math.Vec3i;

import java.util.*;


public class Network {
	private final Map<Vec3i, NetworkComponent> networkBlocks = Maps.newHashMap();
	private final Map<Vec3i, BlockEntry> blocks = Maps.newHashMap();
	private final World world;
	private final int id;
	private NetworkData networkData;
	
	public Network(World world) {
		this(world, NetworkManager.getUID());
	}
	
	private Network(World world, int id) {
		this.world = world;
		this.id = id;
	}
	
	public int getSize() {
		return blocks.size();
	}
	
	public boolean existsOnPos(int x, int y, int z) {
		Vec3i pos = new Vec3i(x, y, z);
		return blocks.containsKey(pos);
	}
	
	public void addBlock(int x, int y, int z) {
		Block block = Block.BLOCKS[world.getBlockId(x, y, z)];
		byte meta = (byte) world.getBlockMeta(x, y, z);
		
		Vec3i pos = new Vec3i(x, y, z);
		blocks.put(pos, new BlockEntry(block, meta));
		if (block instanceof NetworkComponent) {
			networkBlocks.put(pos, (NetworkComponent) block);
			update();
		}
	}
	
	public List<Network> removeBlock(int x, int y, int z) {
		Vec3i pos = new Vec3i(x, y, z);
		networkBlocks.remove(pos);
		blocks.remove(pos);
		update();
		
		List<Vec3i> sideNets = new ArrayList<>(6);
		for (byte i = 0; i < 6; i++) {
			Vec3i offset = NetworkManager.OFFSETS[i];
			Vec3i side = new Vec3i(x + offset.getX(), y + offset.getY(), z + offset.getZ());
			if (blocks.containsKey(side)) {
				sideNets.add(side);
			}
		}
		
		List<Set<Vec3i>> preNets = Lists.newArrayList();
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
            Network sideNet = new Network(world);

            preNet.forEach(blockPos -> {
                sideNet.blocks.put(blockPos, blocks.get(blockPos));
                NetworkComponent energyBlock = networkBlocks.get(blockPos);
                if (energyBlock != null) {
                    sideNet.networkBlocks.put(blockPos, energyBlock);
                }
            });

            if (sideNet.getSize() > 1) {
                result.add(sideNet);
                sideNet.update();
            }
        }
		
		return result;
	}
	
	public void mergeNetwork(Network net) {
		blocks.putAll(net.blocks);
		networkBlocks.putAll(net.networkBlocks);
		networkBlocks.forEach((pos, networkComponent) -> {
			BlockEntry block = blocks.get(pos);
			networkComponent.update(pos.getX(),pos.getY(),pos.getZ(),block.meta,networkData);
		});
	}
	
	public NbtCompound toTag() {
		NbtCompound net = new NbtCompound();
		NbtList positions = new NbtList();
		net.put("blocks", positions);
		net.put("data", networkData.toTag());
		net.putInt("id", id);
		
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
        Network net = new Network(world, id);
        net.networkData = new NetworkData(root.getCompound("data"));
		
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
			}
		}
		
		return net;
	}
	
	private Set<Vec3i> floodFill(Vec3i start) {
		List<Set<Vec3i>> edges = Lists.newArrayList();
		Set<Vec3i> result = Sets.newHashSet();
		edges.add(Sets.newHashSet(start));
		edges.add(Sets.newHashSet());
		
		byte n = 0;
		boolean added = true;
		while (added) {
			Set<Vec3i> oldEdge = edges.get(n & 1);
			Set<Vec3i> newEdge = edges.get((n + 1) & 1);
			n = (byte) ((n + 1) & 1);
			oldEdge.forEach(pos -> {
				for (byte i = 0; i < 6; i++) {
					Vec3i offset = NetworkManager.OFFSETS[i];
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
	
	private void update() {
		networkBlocks.forEach((pos, networkComponent) -> {
			BlockEntry block = blocks.get(pos);
			networkComponent.update(pos.getX(),pos.getY(),pos.getZ(),block.meta,networkData);
		});
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
		return String.format("[S:%d, NBS: %d, D: %s]", getSize(), networkBlocks.size(), networkData);
	}
	
	private static class BlockEntry {
		Block block;
		byte meta;
		
		private BlockEntry(Block block, byte meta) {
			this.block = block;
			this.meta = meta;
		}
	}
}
