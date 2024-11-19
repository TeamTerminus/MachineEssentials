package net.teamterminus.machineessentials.fluid.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;

import java.util.ArrayList;
import java.util.List;

public abstract class FluidScreenHandler extends ScreenHandler {

    public List<FluidStack> trackedFluidStacks = new ArrayList<>();
    public List<FluidSlot> fluidSlots = new ArrayList<>();

    public FluidInventory fInv;

    protected void addFluidSlot(FluidSlot slot){
        slot.slotIndex = this.fluidSlots.size();
        this.fluidSlots.add(slot);
        this.trackedFluidStacks.add(null);
    }

    public FluidSlot getFluidSlot(int idx) { return this.fluidSlots.get(idx); }
    public void putFluidInSlot(int idx, FluidStack fluid) { this.getFluidSlot(idx).putStack(fluid);}

    public FluidStack onFluidSlotClick(int index, int button, boolean shift, PlayerEntity player) {
        if(index == -999){
            return null;
        }
        FluidSlot slot = fluidSlots.get(index);
        PlayerInventory playerInventory = player.inventory;
        if(slot != null){
            if(playerInventory.getCursorStack() != null && playerInventory.getCursorStack().getItem() instanceof BucketItem bucket && bucket.fluidBlockId > 0) {
                emptyBucket(playerInventory,slot,index,bucket);
            } else if (playerInventory.getCursorStack() != null && playerInventory.getCursorStack().getItem() instanceof BucketItem bucket && bucket.fluidBlockId == 0) {
                if (slot.getFluidStack() != null && slot.getFluidStack().amount >= 1000) {
                    return fillBucket(playerInventory,slot,index,bucket);
                }
            }
            //TODO: custom fluid container items
        }
        return null;
    }

    protected FluidStack fillBucket(PlayerInventory playerInventory, FluidSlot slot, int index, BucketItem bucket) {
        if(FluidContainerRegistry.findEmptyContainers(slot.getFluidStack().fluid).contains(bucket)){
            Item item = FluidContainerRegistry.findFilledContainersWithContainer(slot.getFluidStack().fluid,bucket).get(0);
            if (item != null) {
                ItemStack stack = new ItemStack(item,1);
                if(playerInventory.getCursorStack().count > 1){
                    boolean isInvFull = true;
                    for (int i = 0; i < playerInventory.size(); ++i) {
                        if (playerInventory.getStack(i) == null){
                            isInvFull = false;
                            break;
                        }
                    }
                    if(isInvFull){
                        return fluidSlots.get(index).getFluidStack();
                    }
                    playerInventory.addStack(stack);
                    playerInventory.getCursorStack().count--;
                } else {
                    playerInventory.setCursorStack(stack);
                }
                fInv.getFluidInSlot(index).amount -= 1000;
                slot.onPickupFromSlot(slot.getFluidStack());
                slot.onSlotChanged();
                return fluidSlots.get(index).getFluidStack();
            }
        }
        return null;
    }

    protected void emptyBucket(PlayerInventory playerInventory, FluidSlot slot, int index, BucketItem bucket) {
        List<FluidType> fluids = FluidContainerRegistry.findFluidsWithFilledContainer(bucket);
        if(!fluids.isEmpty()){
            FluidType fluid = fluids.get(0);
            if (slot.getFluidStack() == null) {
                if(fInv.getAllowedFluidsForSlot(index).isEmpty() || fInv.getAllowedFluidsForSlot(index).contains(fluid)){
                    if(slot.isFluidValid(fluid)){
                        playerInventory.setCursorStack(new ItemStack(bucket.getCraftingReturnItem(), 1));
                        slot.putStack(new FluidStack(fluid, fluid.unitsPerBucket()));
                        slot.onSlotChanged();
                    }
                }
            } else if (slot.getFluidStack() != null && slot.getFluidStack().isFluidEqual(fluid)) {
                if (slot.getFluidStack().amount + fluid.unitsPerBucket() <= fInv.getFluidCapacityForSlot(slot.id)) {
                    if(fInv.getAllowedFluidsForSlot(index).isEmpty() || fInv.getAllowedFluidsForSlot(index).contains(fluid)){
                        if(slot.isFluidValid(fluid)){
                            playerInventory.setCursorStack(new ItemStack(bucket.getCraftingReturnItem(), 1));
                            slot.getFluidStack().amount += fluid.unitsPerBucket();
                            slot.onSlotChanged();
                        }
                    }
                }
            }
        }
    }
}
