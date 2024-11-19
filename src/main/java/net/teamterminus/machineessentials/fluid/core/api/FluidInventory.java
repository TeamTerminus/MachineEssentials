package net.teamterminus.machineessentials.fluid.core.api;

import net.minecraft.inventory.Inventory;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;

import java.util.ArrayList;

public interface FluidInventory extends Inventory {
    boolean canInsertFluid(int slot, FluidStack fluidStack);
    FluidStack getFluidInSlot(int slot);
    int getFluidCapacityForSlot(int slot);
    ArrayList<FluidType> getAllowedFluidsForSlot(int slot);
    void setFluidInSlot(int slot, FluidStack fluid);
    FluidStack insertFluid(int slot, FluidStack fluidStack);
    int getRemainingCapacity(int slot);
    int getFluidInventorySize();
    void onFluidInventoryChanged();
}
