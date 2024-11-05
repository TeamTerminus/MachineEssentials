package net.teamterminus.machineessentials.energy.electric.base;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.energy.electric.api.IElectric;
import net.teamterminus.machineessentials.energy.electric.api.IVoltageTiered;
import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.machineessentials.network.INetworkComponentTile;
import net.teamterminus.machineessentials.network.Network;
import net.teamterminus.machineessentials.network.NetworkType;
import net.teamterminus.machineessentials.util.AveragingCounter;
import net.teamterminus.machineessentials.util.IBlockEntityInit;

public abstract class ElectricBlockEntity extends BlockEntity implements IBlockEntityInit, INetworkComponentTile, IVoltageTiered, IElectric {

    /**
     * Prefer using getEnergy() instead when possible, this field might not always represent the real energy level!
     */
    protected long energy = 0;
    /**
     * Prefer using getCapacity() instead when possible, this field might not always represent the real capacity!
     */
    protected long capacity = 0;

    protected long maxVoltageIn = 0;
    protected long maxAmpsIn = 0;

    protected long maxVoltageOut = 0;
    protected long maxAmpsOut = 0;

    protected AveragingCounter averageAmpLoad = new AveragingCounter();
    protected AveragingCounter averageEnergyTransfer = new AveragingCounter();
    protected long ampsUsing = 0;

    @Override
    public VoltageTier getTier() {
        IVoltageTiered block = (IVoltageTiered) getBlock();
        return block.getTier();
    }

    //IEnergyContainer
    @Override
    public long getEnergy() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long getMaxInputVoltage() {
        return maxVoltageIn;
    }

    @Override
    public long getMaxInputAmperage() {
        return maxAmpsIn;
    }

    @Override
    public long getMaxOutputVoltage() {
        return maxVoltageOut;
    }

    @Override
    public long getMaxOutputAmperage() {
        return maxAmpsOut;
    }

    @Override
    public long internalChangeEnergy(long difference) {
        averageEnergyTransfer.increment(world,difference);
        energy += difference;
        return difference;
    }

    @Override
    public double getAverageEnergyTransfer() {
        return averageEnergyTransfer.getAverage(world);
    }

    @Override
    public long getAmpsCurrentlyUsed() {
        return ampsUsing;
    }

    @Override
    public void addAmpsToUse(long amperage) {
        averageAmpLoad.increment(world,amperage);
        ampsUsing += amperage;
    }

    @Override
    public double getAverageAmpLoad() {
        return averageAmpLoad.getAverage(world);
    }

    //NetworkComponent
    public Network energyNet;

    @Override
    public NetworkType getType() {
        return NetworkType.ELECTRIC;
    }

    @Override
    public Vec3i getPosition() {
        return new Vec3i(x,y,z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return MachineEssentials.getBlockEntity(direction,world,this) instanceof IElectric;
    }

    @Override
    public void networkChanged(Network network) {
        this.energyNet = network;
    }

    @Override
    public void removedFromNetwork(Network network) {
        this.energyNet = null;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        energy = tag.getLong("Energy");
        super.readNbt(tag);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putLong("Energy",energy);
        super.writeNbt(tag);
    }
}
