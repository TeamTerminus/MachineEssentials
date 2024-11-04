package net.teamterminus.machineessentials.network;

import net.modificationstation.stationapi.api.util.math.Direction;
import net.teamterminus.machineessentials.util.Vec3i;

public interface NetworkComponentTile extends NetworkComponent {

	Vec3i getPosition();

	boolean isConnected(Direction direction);

	void networkChanged(Network network);

	void removedFromNetwork(Network network);
}
