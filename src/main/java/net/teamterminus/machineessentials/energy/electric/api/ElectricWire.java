package net.teamterminus.machineessentials.energy.electric.api;

import net.teamterminus.machineessentials.network.NetworkWire;

public interface ElectricWire extends NetworkWire {
    /**
     * @return The maximum voltage rating of this wire
     */
    long getVoltageRating();

    /**
     * @return The maximum amperage rating of this wire
     */
    long getAmpRating();

    /**
     * Triggered when the current flowing through this wire is higher than it can handle.
     */
    void onOvercurrent(long amps);

    /**
     * Triggered when the wire is exposed to higher voltage than it can handle.
     */
    void onOvervoltage(long voltage);

    /**
     * @return The wire properties of this wire
     */
    WireProperties getProperties();

    void incrementAmperage(long amperage);
}
