package net.teamterminus.machineessentials.energy.electric.template;

import lombok.Getter;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.energy.electric.api.WireProperties;
import net.teamterminus.machineessentials.network.NetworkType;

public abstract class ElectricWireBlock extends ElectricBlock {

    @Getter
    protected WireProperties properties;

    public ElectricWireBlock(Identifier identifier, Material material, WireProperties properties) {
        super(identifier, material);
        this.properties = properties;
    }

    @Override
    public NetworkType getType() {
        return NetworkType.ELECTRIC;
    }
}
