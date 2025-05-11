package net.teamterminus.machineessentials.energy.electric.template;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;
import net.teamterminus.machineessentials.MachineEssentials;
import net.teamterminus.machineessentials.energy.electric.api.Electric;
import net.teamterminus.machineessentials.energy.electric.api.HasVoltageTier;
import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.machineessentials.network.Network;
import net.teamterminus.machineessentials.network.NetworkComponent;
import net.teamterminus.machineessentials.network.NetworkManager;
import net.teamterminus.machineessentials.network.NetworkType;
import net.teamterminus.machineessentials.util.AveragingCounter;
import net.teamterminus.machineessentials.util.BlockEntityInit;

public abstract class ElectricBlockEntity extends BlockEntity implements NetworkComponent, HasVoltageTier, Electric, BlockEntityInit {

    /**
     * Only use this directly if you know what you're doing.
     *
     * @see #getEnergy()
     */
    protected long energy = 0;
    /**
     * @see #getEnergyCapacity()
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
        HasVoltageTier block = (HasVoltageTier) getBlock();
        return block.getTier();
    }

    @Override
    public void init(Block block) {
        networkChanged(NetworkManager.getNet(world, x, y, z));
    }

    //IEnergyContainer
    @Override
    public long getEnergy() {
        return energy;
    }

    @Override
    public long getEnergyCapacity() {
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
    public long adjustEnergy(long difference) {
        averageEnergyTransfer.increment(world, difference);
        energy += difference;
        return difference;
    }

    @Override
    public void setEnergy(long energy) {
        this.energy = energy;
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
        averageAmpLoad.increment(world, amperage);
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
        return new Vec3i(x, y, z);
    }

    @Override
    public boolean isConnected(Direction direction) {
        return MachineEssentials.getBlockEntity(direction, world, this) instanceof Electric;
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
        tag.putLong("Energy", energy);
        super.writeNbt(tag);
    }
}
