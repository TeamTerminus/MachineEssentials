package net.teamterminus.machineessentials.network;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.event.world.BlockSetEvent;
import net.modificationstation.stationapi.api.event.world.WorldEvent;
import net.modificationstation.stationapi.api.event.world.WorldPropertiesEvent;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import net.teamterminus.machineessentials.util.BlockChangeInfo;
import net.teamterminus.machineessentials.util.Vec3i;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static net.teamterminus.machineessentials.network.Network.OFFSETS;

public class NetworkManager {

	private static final Map<Integer, Set<Network>> NETS = new HashMap<>();
	private static final AtomicInteger ID_PROVIDER = new AtomicInteger(0);
	private static final NetworkManager INSTANCE = new NetworkManager();

	private NetworkManager() {}

	public static int getNetID(World world, int x, int y, int z) {
		Network net = getNet(world, x, y, z);
		return net == null ? -1 : net.hashCode();
	}

	public static NetworkManager getInstance() {
		return INSTANCE;
	}

	@EventListener
	public void blockChanged(BlockSetEvent event) {
		if(event.blockState == States.AIR.get()){
			removeBlock(new BlockChangeInfo(event.world,new Vec3i(event.x, event.y, event.z),event.blockState,event.blockMeta));
		} else {
			addBlock(new BlockChangeInfo(event.world,new Vec3i(event.x, event.y, event.z),event.blockState,event.blockMeta));
		}
	}

	@EventListener
	public void initNetsEvent(WorldEvent.Init event) {
		File file = event.world.getDimensionData().getWorldPropertiesFile("networks");
		if (file.exists()) {
			try {
				NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));
				NetworkManager.netsFromTag(World.class.cast(this), tag);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@EventListener
	public void saveNetsEvent(WorldEvent.Save event) {
		try {
			File file = event.world.getDimensionData().getWorldPropertiesFile("networks");
			NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));
			NetworkManager.netsToTag(World.class.cast(this), tag);
			NbtIo.writeCompressed(tag, new FileOutputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void addBlock(BlockChangeInfo blockChanged) {
		int x = blockChanged.pos.x;
		int y = blockChanged.pos.y;
		int z = blockChanged.pos.z;
		World world = blockChanged.world;

		if(!canBeNet(blockChanged.state.getBlock())) {
			return;
		}

		NetworkComponent component = (NetworkComponent) blockChanged.state.getBlock();

		Set<Network> nets = NETS.computeIfAbsent(world.dimension.id, i -> new HashSet<>());

		Set<Network> sideNets = new HashSet<>();
		for (Network net: nets) {
			for (Vec3i offset: OFFSETS) {
				int px = x + offset.x;
				int py = y + offset.y;
				int pz = z + offset.z;
				if (net.existsOnPos(px, py, pz)) {
					sideNets.add(net);
				}
			}
		}

		Network net = null;
		int size = sideNets.size();
		//no nets around, create one
		if (size == 0) {
			net = new Network(world,component.getType());
			net.addBlock(x, y, z);
			for (Vec3i offset: OFFSETS) {
				int px = x + offset.x;
				int py = y + offset.y;
				int pz = z + offset.z;
				if (canBeNet(world, px, py, pz)) {
					net.addBlock(px, py, pz);
				}
			}
			if (net.getSize() > 1) {
				nets.add(net);
			}
		}
		else if (size == 1) {
			Network potentialNet = sideNets.stream().findAny().get();
			if(potentialNet.isOfSameType(component)){
				potentialNet.addBlock(x, y, z);
				net = potentialNet;
			}
		}
		else { //multiple nets around
			Network[] netsArray = sideNets.toArray(new Network[size]);
			Network main = null;
			for (Network network : netsArray) {
				if(network.isOfSameType(component)){
					main = network;
					main.addBlock(x, y, z);
					for (Network otherNet : netsArray) {
						if(otherNet == main){
							continue;
						}
						if(otherNet.isOfSameType(main)){
							main.mergeNetwork(otherNet);
							nets.remove(otherNet);
						}
					}
					net = main;
					break;
				}
			}
		}

		if(net == null && getNet(world, x, y, z) == null) {
			net = new Network(world,component.getType());
			net.addBlock(x, y, z);
			for (Vec3i offset: OFFSETS) {
				int px = x + offset.x;
				int py = y + offset.y;
				int pz = z + offset.z;
				if (canBeNet(world, px, py, pz)) {
					net.addBlock(px, py, pz);
				}
			}
			if (net.getSize() > 1) {
				nets.add(net);
			}
		}

		//add surrounding blocks to net if type matches
		for (Vec3i offset : OFFSETS) {
			int px = x + offset.x;
			int py = y + offset.y;
			int pz = z + offset.z;
			if (canBeNet(world, px, py, pz) && getNet(world, px, py, pz) == null && net != null) {
				NetworkComponent sideComponent = (NetworkComponent) world.getBlockState(px, py, pz).getBlock();
				if(net.isOfSameType(sideComponent)){
					net.addBlock(px, py, pz);
				}
			}
		}
	}

	public static void removeBlock(BlockChangeInfo blockChanged) {
		int x = blockChanged.pos.x;
		int y = blockChanged.pos.y;
		int z = blockChanged.pos.z;
		World world = blockChanged.world;
		Set<Network> nets = NETS.get(world.dimension.id);

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
		Set<Network> nets = NETS.get(world.dimension.id);
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

	public static void netsToTag(int dim, NbtCompound root) {
		Set<Network> nets = NETS.get(dim);
		NbtCompound dimTag = new NbtCompound();
		root.put("dim" + dim, dimTag);

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
		Set<Network> nets = new HashSet<>();
		NETS.put(world.dimension.id, nets);

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

	public static boolean canBeNet(StationFlatteningWorld world, int x, int y, int z) {
		Block block = world.getBlockState(x,y,z).getBlock();
		return canBeNet(block);
	}

	public static boolean canBeNet(Block block) {
		return block instanceof NetworkComponent;
	}

	private static Network getNet(World world, int x, int y, int z) {
		Set<Network> nets = NETS.get(world.dimension.id);
		if (nets != null) {
			for (Network net: nets) {
				if (net.existsOnPos(x, y, z)) {
					return net;
				}
			}
		}
		return null;
	}

	public static void updateAllNets(){
		NETS.forEach((dimId, nets)->{
			for (Network net : nets) {
				net.update();
			}
		});
	}
}
