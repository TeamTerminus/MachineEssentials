package net.teamterminus.machineessentials.network;

import net.modificationstation.stationapi.api.util.math.Direction;

public class NetworkPath {

	public final NetworkComponentTile target;
	public final Direction targetDirection;
	public final int distance;
	public final NetworkComponentTile[] path;

	public NetworkPath(Direction destDirection, NetworkComponentTile[] path, int distance) {
		this.target = path[path.length - 1];
		this.targetDirection = destDirection;
		this.distance = distance;
		this.path = path;
	}

	public int getDistance() {
		return distance;
	}
}
