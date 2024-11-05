package net.teamterminus.machineessentials.network;

import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;

/**
 * Marks a block entity as being able to be an active component of a network.
 * <p>
 * Block entities whose blocks implement <code>INetworkComponent</code> should also implement this.
 */
public interface INetworkComponentTile extends INetworkComponent {

	Vec3i getPosition();

	boolean isntConnected(Direction direction);

	void networkChanged(Network network);

	void removedFromNetwork(Network network);
}
