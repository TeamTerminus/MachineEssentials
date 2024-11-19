package net.teamterminus.machineessentials.fluid.core;

import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;

import java.util.List;

public class FluidSlot {
    public final FluidInventory fluidInventory;
    public int id;
    public int slotIndex;
    public int x;
    public int y;

    public FluidSlot(FluidInventory FluidInventory, int idx, int x, int y) {
        fluidInventory = FluidInventory;
        id = idx;
        slotIndex = idx;
        this.x = x;
        this.y = y;
    }

    public void onSlotChanged(){
        this.fluidInventory.onFluidInventoryChanged();
    }

    public void onPickupFromSlot(FluidStack stack) {
        this.onSlotChanged();
    }

    public boolean isFluidValid(FluidType stack) {
        return true;
    }

	public boolean isAnyFluidValid(List<FluidType> stack) {
		return true;
	}

	public boolean areAllFluidValid(List<FluidType> stack) {
		return true;
	}

    public FluidStack getFluidStack() {
        return fluidInventory.getFluidInSlot(this.slotIndex);
    }

    public boolean hasStack() {
        return this.getFluidStack() != null;
    }

    public void putStack(FluidStack stack) {
        if(stack == null){
            this.fluidInventory.setFluidInSlot(this.slotIndex,null);
            this.onSlotChanged();
        }
        else if(fluidInventory.getAllowedFluidsForSlot(slotIndex).isEmpty() || fluidInventory.getAllowedFluidsForSlot(slotIndex).contains(stack.fluid)){
            this.fluidInventory.setFluidInSlot(this.slotIndex, stack);
            this.onSlotChanged();
        }
    }
}
