package net.teamterminus.machineessentials.network;

import org.jetbrains.annotations.NotNull;

public class NetworkType {

	public static final NetworkType ENERGY = new NetworkType("energy");
	public static final NetworkType ELECTRIC = new NetworkType("electric");

	public final @NotNull String type;

	public NetworkType(@NotNull String type) {
		this.type = type;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NetworkType that)) return false;

        return type.equals(that.type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}
}