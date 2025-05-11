package net.teamterminus.metest.energy.electric.block.entity;

import net.minecraft.block.Block;
import net.teamterminus.machineessentials.energy.electric.template.ElectricGeneratorBlockEntity;
import net.teamterminus.metest.energy.electric.block.TestBaseBlock;

public class TestGeneratorBlockEntity extends ElectricGeneratorBlockEntity {

    @Override
    public void init(Block block) {
        super.init(block);
        maxAmpsOut = 1;
        maxAmpsIn = 0;
        maxVoltageIn = 0;
        maxVoltageOut = getTier((TestBaseBlock) block).maxVoltage;
        capacity = getTier((TestBaseBlock) block).maxVoltage * 64L;
    }

    @Override
    public void tick() {
        addEnergy(maxVoltageOut * maxAmpsOut);
    }

    @Override
    public void onOvervoltage(long voltage) {

    }
}
