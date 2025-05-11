package net.teamterminus.machineessentials.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import net.teamterminus.machineessentials.util.BlockEntityInit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin {

    @Shadow
    public abstract int getBlockId(int x, int y, int z);

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockEntity(IIILnet/minecraft/block/entity/BlockEntity;)V", shift = At.Shift.AFTER))
    public void setBlockEntity(int x, int y, int z, BlockEntity blockEntity, CallbackInfo ci) {
        if (blockEntity instanceof BlockEntityInit tileInit) {
            tileInit.init(Block.BLOCKS[getBlockId(x, y, z)]);
        }
    }


}
