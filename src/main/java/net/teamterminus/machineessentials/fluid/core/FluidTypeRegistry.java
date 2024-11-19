package net.teamterminus.machineessentials.fluid.core;

import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class FluidTypeRegistry {
    private static final HashMap<Identifier, FluidType> fluidTypes = new HashMap<>();

    public static void register(Identifier id, FluidType type) {
        if(fluidTypes.containsKey(id)) return;
        fluidTypes.put(id, type);
    }

    public static FluidType unregister(Identifier id) {
        return fluidTypes.remove(id);
    }

    public static FluidType get(Identifier identifier) {
        return fluidTypes.get(identifier);
    }

}
