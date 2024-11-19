package net.teamterminus.machineessentials.fluid.test;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.gui.screen.container.GuiHelper;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.event.ScreenHandlerListener;

public class FluidContainerBlock extends TemplateBlockWithEntity {
    public FluidContainerBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    public boolean onUse(World world, int i, int j, int k, PlayerEntity entityPlayer)
    {
        ScreenHandlerListener.x = i;
        ScreenHandlerListener.y = j;
        ScreenHandlerListener.z = k;
        FluidContainerBlockEntity be = (FluidContainerBlockEntity) world.getBlockEntity(i, j, k);
        Minecraft.INSTANCE.setScreen(new FluidContainerScreen(entityPlayer.inventory, be));
        //GuiHelper.openGUI(entityPlayer, MachineEssentials.NAMESPACE.id("fluid_container"), be, new FluidContainerScreenHandler(entityPlayer.inventory, be));
        return true;
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new FluidContainerBlockEntity();
    }
}
