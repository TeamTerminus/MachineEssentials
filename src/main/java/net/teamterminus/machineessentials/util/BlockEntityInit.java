package net.teamterminus.machineessentials.util;

import net.minecraft.block.Block;

// i don't want to do block entity code in a block class method, sorry calm
// tile entity constructors don't have access to world or position data, and that is why this interface is necessary
public interface BlockEntityInit {
    void init(Block block);
}
