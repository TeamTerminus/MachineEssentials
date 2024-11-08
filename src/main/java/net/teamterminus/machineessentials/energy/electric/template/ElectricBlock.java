package net.teamterminus.machineessentials.energy.electric.template;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.util.BlockEntityInit;

public abstract class ElectricBlock extends TemplateBlockWithEntity {

    public ElectricBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        super.onPlaced(world, x, y, z);
        ((BlockEntityInit) world.getBlockEntity(x, y, z)).init();
    }
}
