package net.teamterminus.metest.energy.electric.block.entity;

import net.minecraft.block.Block;
import net.teamterminus.machineessentials.energy.electric.template.ElectricDeviceBlockEntity;
import net.teamterminus.metest.energy.electric.block.TestBaseBlock;

public class TestMachineBlockEntity extends ElectricDeviceBlockEntity {

    @Override
    public void init(Block block) {
        super.init(block);
        maxAmpsOut = 0;
        maxAmpsIn = 1;
        maxVoltageIn = getTier((TestBaseBlock) block).maxVoltage;
        maxVoltageOut = 0;
        capacity = getTier((TestBaseBlock) block).maxVoltage * 64L;
    }

    @Override
    public void tick() {
        super.tick();
        removeEnergy(16);
    }

    @Override
    public void onOvervoltage(long voltage) {

    }

}
