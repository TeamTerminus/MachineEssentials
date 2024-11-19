package net.teamterminus.machineessentials.fluid.core;


import net.minecraft.item.Item;

import java.util.List;

public class FluidContainerRegistryEntry {

    public Item container;
    public Item containerEmpty;
    public List<FluidType> fluid;

    public FluidContainerRegistryEntry(Item container, Item containerEmpty, List<FluidType> fluid) {
        this.container = container;
        this.containerEmpty = containerEmpty;
        this.fluid = fluid;
    }
}
