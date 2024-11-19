package net.teamterminus.machineessentials.fluid.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Identifier;

public class FluidStack {

    public FluidType fluid;
    public int amount;

    public FluidStack(FluidType type, int amount) {
        this.fluid = type;
        this.amount = amount;
    }

    public FluidStack(FluidType type) {
        this(type, 1000);
    }

    public FluidStack(Identifier id, int amount) {
        this(FluidTypeRegistry.get(id), amount);
    }

    public FluidStack(Identifier id) {
        this(id, 1000);
    }

    public FluidStack(NbtCompound nbt){
        readFromNBT(nbt);
    }

    public FluidStack splitStack(int amount){
        this.amount -= amount;
        return new FluidStack(this.fluid, amount);
    }

    public boolean isFluidEqual(FluidStack stack){
        if(stack == null) return false;
        return stack.fluid == fluid;
    }

    public boolean isFluidEqual(FluidType type){
        return fluid == type;
    }

    public boolean isStackEqual(FluidStack stack){
        if(stack == null) return false;
        return stack.fluid == fluid && stack.amount == amount;
    }

    public static boolean areStacksEqual(FluidStack fluidStack, FluidStack fluidStack1) {
        if (fluidStack == null && fluidStack1 == null) {
            return true;
        } else {
            return fluidStack != null && fluidStack1 != null && fluidStack.isStackEqual(fluidStack1);
        }
    }

    public static boolean areFluidsEqual(FluidStack fluidStack, FluidStack fluidStack1) {
        if (fluidStack == null && fluidStack1 == null) {
            return true;
        } else {
            return fluidStack != null && fluidStack1 != null && fluidStack.isFluidEqual(fluidStack1);
        }
    }

    public NbtCompound writeToNBT(NbtCompound nbt) {
        if(fluid != null){
            nbt.putString("type", fluid.id().toString());
            nbt.putInt("amount",amount);
        }
        return nbt;
    }

    public void readFromNBT(NbtCompound nbt){
        if(nbt.contains("type")){
            this.fluid = FluidTypeRegistry.get(Identifier.of(nbt.getString("type")));
            this.amount = nbt.getInt("amount");
        }
    }

    public String getFluidName(){
        return fluid.getTranslatedName();
    }

    public String toString(){
        return amount+"mB "+fluid.getTranslatedName();
    }

    public FluidStack copy(){
        return new FluidStack(fluid, amount);
    }

    public ItemStack toItemStack(){
        return new ItemStack(fluid.flowing(), amount);
    }
}
