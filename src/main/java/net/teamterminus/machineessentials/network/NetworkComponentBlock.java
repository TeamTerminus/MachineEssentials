package net.teamterminus.machineessentials.network;

/**
 * Marks a block as being able to be an active component of a network.
 */
public interface NetworkComponentBlock {
    NetworkType getType();
}
