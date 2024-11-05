package net.teamterminus.machineessentials.energy.electric.base;

import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.NotNull;

public abstract class ElectricStorageBlockEntity extends ElectricDeviceBlockEntity {

    @Override
    public boolean canProvide(@NotNull Direction dir) {
        return true;
    }

}
