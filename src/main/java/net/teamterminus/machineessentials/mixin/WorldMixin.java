package net.teamterminus.machineessentials.mixin;

import net.minecraft.client.gui.screen.LoadingDisplay;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import net.teamterminus.machineessentials.network.NetworkManager;
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
	protected WorldStorage dimensionData;
	
	@Inject(method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V", at = @At("TAIL"))
	private void me_onLevelInit(WorldStorage dimensionData, String name, Dimension dimension, long seed, CallbackInfo ci) {
		File file = dimensionData.getWorldPropertiesFile("networks");
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
	
	@Inject(method = "saveWithLoadingDisplay", at = @At("HEAD"))
	public void me_onLevelSave(boolean bool, LoadingDisplay progress, CallbackInfo ci) {
		try {
			File file = dimensionData.getWorldPropertiesFile("networks");
			NbtCompound tag = NbtIo.readCompressed(new FileInputStream(file));
			NetworkManager.netsToTag(World.class.cast(this), tag);
			NbtIo.writeCompressed(tag, new FileOutputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
