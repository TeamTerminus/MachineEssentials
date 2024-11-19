package net.teamterminus.machineessentials.fluid.test;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.fluid.core.FluidType;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;

import java.util.ArrayList;

public class FluidContainerBlockEntity extends BlockEntity implements FluidInventory {

    public FluidStack[] fluidContents = new FluidStack[1];
    public int[] fluidCapacity = new int[1];
    public ArrayList<ArrayList<FluidType>> acceptedFluids = new ArrayList<>(fluidContents.length);

    public FluidContainerBlockEntity() {
        fluidCapacity = new int[]{4000};
        acceptedFluids.add(new ArrayList<>());
    }

    @Override
    public boolean canInsertFluid(int slot, FluidStack fluidStack) {
        if(getFluidInSlot(slot) != null) if(!getFluidInSlot(slot).isFluidEqual(fluidStack)) return false;
        return Math.min(fluidStack.amount,getRemainingCapacity(slot)) > 0;
    }

    @Override
    public FluidStack getFluidInSlot(int slot) {
        if(this.fluidContents.length == 0) return null;
        if(this.fluidContents[slot] == null || this.fluidContents[slot].fluid == null || this.fluidContents[slot].amount == 0){
            this.fluidContents[slot] = null;
        }
        return fluidContents[slot];
    }

    @Override
    public int getFluidCapacityForSlot(int slot) {
        return fluidCapacity[slot];
    }

    @Override
    public ArrayList<FluidType> getAllowedFluidsForSlot(int slot) {
        return acceptedFluids.get(slot);
    }

    @Override
    public void setFluidInSlot(int slot, FluidStack fluid) {
        if(fluid == null || fluid.amount == 0 || fluid.fluid == null){
            this.fluidContents[slot] = null;
            this.onFluidInventoryChanged();
            return;
        }
        if(acceptedFluids.get(slot).contains(fluid.fluid) || acceptedFluids.get(slot).isEmpty()){
            this.fluidContents[slot] = fluid;
            this.onFluidInventoryChanged();
        }
    }

    @Override
    public FluidStack insertFluid(int slot, FluidStack fluidStack) {
        FluidStack stack = fluidContents[slot];
        FluidStack split = fluidStack.splitStack(Math.min(fluidStack.amount,getRemainingCapacity(slot)));
        if(stack != null && split.amount > 0){
            fluidContents[slot].amount += split.amount;
        } else {
            fluidContents[slot] = split;
        }
        return fluidStack;
    }

    @Override
    public int getRemainingCapacity(int slot) {
        if(fluidContents[slot] == null){
            return fluidCapacity[slot];
        }
        return fluidCapacity[slot]-fluidContents[slot].amount;
    }

    @Override
    public int getFluidInventorySize() {
        return fluidContents.length;
    }

    @Override
    public void onFluidInventoryChanged() {

    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        NbtList nbtTagList = tag.getList("Fluids");
        this.fluidContents = new FluidStack[this.getFluidInventorySize()];

        for (int i3 = 0; i3 < nbtTagList.size(); ++i3) {
            NbtCompound compound = ( NbtCompound) nbtTagList.get(i3);
            int i5 =  compound.getByte("Slot") & 255;
            if (i5 < this.fluidContents.length) {
                this.fluidContents[i5] = new FluidStack(compound);
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtList nbtTagList = new NbtList();
        for (int i3 = 0; i3 < this.fluidContents.length; ++i3) {
            if (this.fluidContents[i3] != null && this.fluidContents[i3].fluid != null) {
                NbtCompound compound = new NbtCompound();
                compound.putByte("Slot", (byte) i3);
                this.fluidContents[i3].writeToNBT(compound);
                nbtTagList.add(compound);
            }
        }
        tag.put("Fluids", nbtTagList);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ItemStack getStack(int slot) {
        return null;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return null;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {

    }

    @Override
    public String getName() {
        return "FluidContainer";
    }

    @Override
    public int getMaxCountPerStack() {
        return 0;
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.world.getBlockEntity(this.x, this.y, this.z) == this && player.getSquaredDistance((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;

    }
}
