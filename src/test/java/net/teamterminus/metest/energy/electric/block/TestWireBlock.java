package net.teamterminus.metest.energy.electric.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.machineessentials.energy.electric.api.WireMaterial;
import net.teamterminus.machineessentials.energy.electric.api.WireProperties;
import net.teamterminus.machineessentials.energy.electric.template.ElectricWireBlock;
import net.teamterminus.metest.energy.electric.block.entity.TestWireBlockEntity;

public class TestWireBlock extends ElectricWireBlock implements CustomTooltipProvider {

    public TestWireBlock(Identifier identifier, Material material, WireProperties properties) {
        super(identifier, material, properties);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new TestWireBlockEntity();
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        TestWireBlockEntity wire = (TestWireBlockEntity) world.getBlockEntity(x, y, z);
        player.sendMessage("---------------");
        player.sendMessage(String.format("A: %f", wire.getAverageAmpLoad()));
        player.sendMessage(String.format("WP: %s", wire.getProperties()));
        player.sendMessage(String.format("N: %s", wire.energyNet));
        player.sendMessage("---------------");
        return super.onUse(world, x, y, z, player);
    }

    @Override
    public String[] getTooltip(ItemStack itemStack, String s) {
        WireMaterial mat = properties.material();
        VoltageTier voltage = mat.maxVoltage();
        String superconductor = properties.superconductor() ? Formatting.LIGHT_PURPLE + voltage.name() + " Superconductor\n" : "";
        String str = s + "\n" + superconductor + String.format("%sMax Voltage: %s%dV %s(%s%s%s)\n%sMax Current: %s%dA\n%sVoltage Drop: %s%dV%s/block",
                Formatting.GRAY,
                Formatting.GREEN, voltage.maxVoltage, Formatting.GRAY, voltage.textColor, voltage.name(), Formatting.GRAY,
                Formatting.GRAY,
                Formatting.GOLD, properties.size() * mat.defaultAmps(),
                Formatting.GRAY,
                Formatting.RED, mat.lossPerBlock(), Formatting.GRAY
        );
        return str.split("\n");
    }
}
