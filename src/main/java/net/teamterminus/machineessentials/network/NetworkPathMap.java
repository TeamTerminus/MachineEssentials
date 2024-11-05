package net.teamterminus.machineessentials.network;

import net.modificationstation.stationapi.api.util.math.Vec3i;

import java.util.HashMap;
import java.util.List;

/**
 * Maps points to valid paths in a network that could be traversed if started from those points.
 */
public class NetworkPathMap extends HashMap<Vec3i, List<NetworkPath>> {
}
