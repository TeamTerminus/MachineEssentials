package net.teamterminus.metest.energy.electric.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.util.Formatting;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.metest.energy.electric.block.entity.TestGeneratorBlockEntity;

public class TestGeneratorBlock extends TestBaseBlock implements CustomTooltipProvider {

    public TestGeneratorBlock(Identifier identifier, Material material, VoltageTier tier) {
        super(identifier, material, tier);
    }

    @Override
    protected BlockEntity createBlockEntity() {
        return new TestGeneratorBlockEntity();
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip) {
        String s = String.format("%s\n%sMax Voltage %sOUT%s: %s%dV %s(%s%s%s)\n%sMax Current Generated: %s%dA\n%sEnergy Capacity: %s%dJ", originalTooltip,
                Formatting.GRAY, Formatting.RED, Formatting.GRAY,
                Formatting.GREEN, tier.maxVoltage, Formatting.GRAY, tier.textColor, tier.name(), Formatting.GRAY,
                Formatting.GRAY, Formatting.GOLD, 1,
                Formatting.GRAY, Formatting.YELLOW, tier.maxVoltage * 64
        );
        return s.split("\n");
    }
}
