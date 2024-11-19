package net.teamterminus.machineessentials.fluid.core.api;

import net.minecraft.item.ItemStack;
import net.teamterminus.machineessentials.fluid.core.FluidStack;

public interface FluidInventoryItem {

    int getCapacity(ItemStack stack);
    int getRemainingCapacity(ItemStack stack);
    boolean canFill(ItemStack stack);
    boolean canDrain(ItemStack stack);
    FluidStack getCurrentFluid(ItemStack stack);
    void setCurrentFluid(FluidStack fluidStack, ItemStack stack);

    ItemStack fill(FluidStack fluidStack, ItemStack stack, FluidInventory tile);
    void drain(ItemStack stack, int slot, FluidInventory tile);

}
