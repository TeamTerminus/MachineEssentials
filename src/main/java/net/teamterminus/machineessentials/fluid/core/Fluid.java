package net.teamterminus.machineessentials.fluid.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Collections;

public class Fluid {

    /*public static final long UNIT = 3628800;

    public static final long BUCKET = UNIT;
    public static final long BLOCK = UNIT;
    public static final long INGOT = UNIT/9;
    public static final long NUGGET = UNIT/81;*/

    public static final FluidType WATER = new FluidType(Identifier.of("minecraft:water"), Block.FLOWING_WATER, Block.WATER,1000,false);
    public static final FluidType LAVA = new FluidType(Identifier.of("minecraft:lava"), Block.FLOWING_LAVA, Block.LAVA,1000,false);

    public static void init(){
        FluidTypeRegistry.register(Identifier.of("minecraft:water"), WATER);
        FluidTypeRegistry.register(Identifier.of("minecraft:lava"), LAVA);

        FluidContainerRegistry.register(Identifier.of("minecraft:water_bucket"),new FluidContainerRegistryEntry(Item.WATER_BUCKET,Item.BUCKET, Collections.singletonList(WATER)));
        FluidContainerRegistry.register(Identifier.of("minecraft:lava_bucket"),new FluidContainerRegistryEntry(Item.LAVA_BUCKET,Item.BUCKET, Collections.singletonList(LAVA)));
    }
}
