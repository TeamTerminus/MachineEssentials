package net.teamterminus.machineessentials.fluid.core;

import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FluidContainerRegistry {
    private static final HashMap<Identifier, FluidContainerRegistryEntry> fluidContainers = new HashMap<>();

    public static void register(Identifier id, FluidContainerRegistryEntry type) {
        if(fluidContainers.containsKey(id)) return;
        fluidContainers.put(id, type);
    }

    public static FluidContainerRegistryEntry unregister(Identifier id) {
        return fluidContainers.remove(id);
    }

    public static FluidContainerRegistryEntry get(Identifier identifier) {
        return fluidContainers.get(identifier);
    }

    public static Collection<FluidContainerRegistryEntry> values(){
        return Collections.unmodifiableCollection(fluidContainers.values());
    }

    public static Set<Identifier> keys(){
        return Collections.unmodifiableSet(fluidContainers.keySet());
    }

    public static List<FluidType> findFluidsWithFilledContainer(Item container){
        List<FluidType> fluids = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : values()) {
            if(fluidContainerRegistryEntry.container == container){
                List<FluidType> fluid = fluidContainerRegistryEntry.fluid;
                fluids.addAll(fluid);
            }
        }
        return fluids;
    }

    public static List<FluidType> findFluidsWithEmptyContainer(Item container){
        List<FluidType> fluids = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : values()) {
            if(fluidContainerRegistryEntry.containerEmpty == container){
                List<FluidType> fluid = fluidContainerRegistryEntry.fluid;
                fluids.addAll(fluid);
            }
        }
        return fluids;
    }

    public static List<Item> findContainers(FluidType fluid){
        List<Item> items = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : values()) {
            if(fluidContainerRegistryEntry.fluid.contains(fluid)){
                Item item = fluidContainerRegistryEntry.container;
                items.add(item);
            }
        }
        return items;
    }

    public static List<Item> findEmptyContainers(FluidType fluid){
        List<Item> items = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : values()) {
            if(fluidContainerRegistryEntry.fluid.contains(fluid)) {
                Item item = fluidContainerRegistryEntry.containerEmpty;
                items.add(item);
            }
        }
        return items;
    }

    public static List<Item> findFilledContainersWithContainer(FluidType fluid, Item container){
        List<Item> items = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : fluidContainers.values()) {
            if(fluidContainerRegistryEntry.fluid.contains(fluid) && fluidContainerRegistryEntry.containerEmpty == container){
                Item item = fluidContainerRegistryEntry.container;
                items.add(item);
            }
        }
        return items;
    }

    public static List<Item> findEmptyContainersWithContainer(FluidType fluid, Item container){
        List<Item> items = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : fluidContainers.values()) {
            if(fluidContainerRegistryEntry.fluid.contains(fluid) && fluidContainerRegistryEntry.container == container) {
                Item item = fluidContainerRegistryEntry.containerEmpty;
                items.add(item);
            }
        }
        return items;
    }

    public static List<FluidType> getAllFluids(){
        List<FluidType> fluids = new ArrayList<>();
        for (FluidContainerRegistryEntry fluidContainerRegistryEntry : fluidContainers.values()) {
            List<FluidType> fluid = fluidContainerRegistryEntry.fluid;
            fluids.addAll(fluid);
        }
        return fluids;
    }
}
