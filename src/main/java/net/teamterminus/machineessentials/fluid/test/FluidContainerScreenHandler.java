package net.teamterminus.machineessentials.fluid.test;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;
import net.teamterminus.machineessentials.fluid.core.FluidScreenHandler;
import net.teamterminus.machineessentials.fluid.core.FluidSlot;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;

public class FluidContainerScreenHandler extends FluidScreenHandler {

    public FluidContainerScreenHandler(PlayerInventory playerInventory, FluidInventory fluidInventory) {

        fInv = fluidInventory;

        addFluidSlot(new FluidSlot(fluidInventory, 0, 62 + 18,17 + 18));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return fInv.canPlayerUse(player);
    }
}
