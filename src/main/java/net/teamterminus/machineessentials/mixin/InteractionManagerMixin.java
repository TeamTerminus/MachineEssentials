package net.teamterminus.machineessentials.mixin;

import net.minecraft.client.InteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.teamterminus.machineessentials.fluid.core.FluidScreenHandler;
import net.teamterminus.machineessentials.fluid.core.FluidStack;
import net.teamterminus.machineessentials.util.FluidSlotInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(InteractionManager.class)
public class InteractionManagerMixin implements FluidSlotInteraction {

    @Unique
    @Override
    public FluidStack machineEssentials$fluidSlotClick(int i, int j, int k, boolean flag, PlayerEntity player) {
        if(player.currentScreenHandler instanceof FluidScreenHandler fluidScreenHandler){
            return fluidScreenHandler.onFluidSlotClick(j, k, flag, player);
        }
        return null;
    }

}
