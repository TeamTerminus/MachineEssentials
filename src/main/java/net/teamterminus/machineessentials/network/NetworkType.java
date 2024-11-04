package net.teamterminus.machineessentials.network;

import org.jetbrains.annotations.NotNull;

public class NetworkType {

	/*public static final NetworkType FLUID = new NetworkType("fluid");
	public static final NetworkType SIGNALUM = new NetworkType("signalum");
	public static final NetworkType ITEM = new NetworkType("item");
	public static final NetworkType RES_NETWORK = new NetworkType("retrostorage_network");
	public static final NetworkType CATALYST_ENERGY = new NetworkType("catalyst_energy");*/
	public static final NetworkType ENERGY = new NetworkType("energy");
	public static final NetworkType ELECTRIC = new NetworkType("electric");

	public final @NotNull String type;

	public NetworkType(@NotNull String type) {
		this.type = type;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NetworkType)) return false;

		NetworkType that = (NetworkType) o;
		return type.equals(that.type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}
}
