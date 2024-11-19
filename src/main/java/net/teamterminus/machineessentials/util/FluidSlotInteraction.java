package net.teamterminus.machineessentials.util;

import net.minecraft.entity.player.PlayerEntity;
import net.teamterminus.machineessentials.fluid.core.FluidStack;

public interface FluidSlotInteraction {
    FluidStack machineEssentials$fluidSlotClick(int i, int j, int k, boolean flag, PlayerEntity player);
}
