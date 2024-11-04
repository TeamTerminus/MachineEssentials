package net.teamterminus.machineessentials.util;


import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class BlockChangeInfo {

	public BlockState state;
	public int meta;
	public World world;
	public Vec3i pos;

	public BlockChangeInfo(World world, Vec3i pos, BlockState state, int meta) {
		this.state = state;
		this.meta = meta;
		this.world = world;
		this.pos = pos;
	}
}
