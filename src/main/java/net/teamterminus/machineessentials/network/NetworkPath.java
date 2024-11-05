package net.teamterminus.machineessentials.network;

import lombok.Getter;
import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * A path from one component to another in a network.
 */
public class NetworkPath {

	public final INetworkComponent target;
	public final Direction targetDirection;
	@Getter
    public final int distance;
	public final INetworkComponent[] path;

	public NetworkPath(Direction destDirection, INetworkComponent[] path, int distance) {
		this.target = path[path.length - 1];
		this.targetDirection = destDirection;
		this.distance = distance;
		this.path = path;
	}

}
