package net.teamterminus.metest.energy.electric.block.entity;

import net.minecraft.block.Block;
import net.teamterminus.machineessentials.energy.electric.template.ElectricWireBlockEntity;

public class TestWireBlockEntity extends ElectricWireBlockEntity {

    @Override
    public void onOvercurrent(long amps) {

    }

    @Override
    public void onOvervoltage(long voltage) {

    }

    @Override
    public void init(Block block) {
        super.init(block);
    }
}
