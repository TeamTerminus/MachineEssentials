package net.teamterminus.machineessentials.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.teamterminus.machineessentials.fluid.test.FluidContainerBlockEntity;
import net.teamterminus.machineessentials.fluid.test.FluidContainerScreen;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class ScreenHandlerListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerScreenHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        registry.registerValueNoMessage(Identifier.of(MOD_ID, "openFluidContainer"), BiTuple.of(this::openFluidContainer, FluidContainerBlockEntity::new));
    }

    public static int x;
    public static int y;
    public static int z;

    @Environment(EnvType.CLIENT)
    public Screen openFluidContainer(PlayerEntity player, Inventory inventoryBase) {
        return new FluidContainerScreen(player.inventory, (FluidContainerBlockEntity) inventoryBase);
    }
}