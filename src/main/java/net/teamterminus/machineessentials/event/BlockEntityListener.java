package net.teamterminus.machineessentials.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.teamterminus.machineessentials.fluid.test.FluidContainerBlockEntity;

public class BlockEntityListener {

    @Entrypoint.Namespace
    public static final Namespace MOD_ID = Null.get();

    @EventListener
    private static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(FluidContainerBlockEntity.class, String.valueOf(Identifier.of(MOD_ID, "FluidContainer")));
    }
}