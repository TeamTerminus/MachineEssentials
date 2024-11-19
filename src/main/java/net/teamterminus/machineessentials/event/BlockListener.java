package net.teamterminus.machineessentials.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.PressurePlateActivationRule;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.template.block.*;
import net.modificationstation.stationapi.api.util.Null;
import net.teamterminus.machineessentials.fluid.test.FluidContainerBlock;

public class BlockListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    public void registerBlocks(BlockRegistryEvent event) {
        fluidContainer = (TemplateBlockWithEntity) new FluidContainerBlock(Identifier.of(MOD_ID, "fluid_container"),Material.GLASS).setTranslationKey(MOD_ID, "fluid_container");

    }
    public static TemplateBlockWithEntity fluidContainer;

}