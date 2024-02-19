package net.teamterminus.genericnetworks.mixin;


import net.minecraft.class_62;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionData;
import net.teamterminus.genericnetworks.util.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


@Mixin(World.class)
public class WorldMixin {
	@Final
	@Shadow
	protected DimensionData dimensionData;
	
	@Inject(method = "<init>(Lnet/minecraft/world/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V", at = @At("TAIL"))
	private void gnapi_onLevelInit(DimensionData dimensionData, String name, Dimension dimension, long seed, CallbackInfo ci) {
		File file = dimensionData.method_1736("ia_networks");
		if (file.exists()) {
			try {
				NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));
				NetworkManager.netsFromTag(World.class.cast(this), tag);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Inject(method = "method_195", at = @At("HEAD"))
	public void gnapi_onLevelSave(boolean bool, class_62 progress, CallbackInfo ci) {
		try {
			File file = dimensionData.method_1736("ia_networks");
			NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));
			NetworkManager.netsToTag(World.class.cast(this), tag);
			NbtIo.writeCompressed(tag, new FileOutputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
