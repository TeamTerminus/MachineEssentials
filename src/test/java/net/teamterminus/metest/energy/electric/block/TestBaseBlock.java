package net.teamterminus.metest.energy.electric.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.energy.electric.api.HasVoltageTier;
import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.machineessentials.energy.electric.template.ElectricBlock;
import net.teamterminus.machineessentials.energy.electric.template.ElectricBlockEntity;
import net.teamterminus.machineessentials.network.NetworkType;

public abstract class TestBaseBlock extends ElectricBlock implements HasVoltageTier {

    public final VoltageTier tier;

    public TestBaseBlock(Identifier identifier, Material material, VoltageTier tier) {
        super(identifier, material);
        this.tier = tier;
    }

    @Override
    public VoltageTier getTier() {
        return tier;
    }

    @Override
    public NetworkType getType() {
        return NetworkType.ELECTRIC;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        ElectricBlockEntity tile = (ElectricBlockEntity) world.getBlockEntity(x, y, z);
        player.sendMessage("---------------");
        player.sendMessage(String.format("T: %s", tile.getTier().name()));
        player.sendMessage(String.format("V IN/OUT: %d/%d", tile.getMaxInputVoltage(), tile.getMaxOutputVoltage()));
        player.sendMessage(String.format("A IN/OUT: %d/%d", tile.getMaxInputAmperage(), tile.getMaxOutputAmperage()));
        player.sendMessage(String.format("A: %d", tile.getAmpsCurrentlyUsed()));
        player.sendMessage(String.format("E: %d/%d", tile.getEnergy(), tile.getEnergyCapacity()));
        player.sendMessage(String.format("N: %s", tile.energyNet));
        player.sendMessage("---------------");
        return super.onUse(world, x, y, z, player);
    }
}
