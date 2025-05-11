package net.teamterminus.machineessentials.network;

import org.jetbrains.annotations.NotNull;

/**
 * Record representing network types, Blocks in networks with differing types won't connect to each other and their networks won't merge.
 *
 * @param id The type of the network
 */
public record NetworkType(@NotNull String id) {

    /**
     * Type for a generic electric (Gregtech-style) energy network.
     */
    public static final NetworkType ELECTRIC = new NetworkType("electric");

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetworkType that)) return false;

        return id.equals(that.id);
    }

}
