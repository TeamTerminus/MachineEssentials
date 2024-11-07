package net.teamterminus.machineessentials.energy.electric.template;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public abstract class ElectricGeneratorBlockEntity extends ElectricBlockEntity {

    @Override
    public void tick() {
        super.tick();
        ampsUsing = 0;
    }

    @Override
    public boolean canReceive(@NotNull Direction dir) {
        return false;
    }

    @Override
    public boolean canProvide(@NotNull Direction dir) {
        return true;
    }

    @Override
    public long receiveEnergy(@NotNull Direction dir, long amperage) {
        return 0;
    }
}
