package net.teamterminus.machineessentials.energy.electric.template;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.network.NetworkComponentBlock;

public abstract class ElectricBlock extends TemplateBlockWithEntity implements NetworkComponentBlock {

    public ElectricBlock(Identifier identifier, Material material) {
        super(identifier, material);
    }

}
