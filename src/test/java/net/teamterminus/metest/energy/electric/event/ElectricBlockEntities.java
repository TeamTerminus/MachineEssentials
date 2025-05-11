package net.teamterminus.metest.energy.electric.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.metest.energy.electric.block.entity.TestGeneratorBlockEntity;
import net.teamterminus.metest.energy.electric.block.entity.TestMachineBlockEntity;
import net.teamterminus.metest.energy.electric.block.entity.TestWireBlockEntity;

import static net.teamterminus.metest.METest.NAMESPACE;

public class ElectricBlockEntities {

    @EventListener
    public static void registerBlockEntities(BlockEntityRegisterEvent event) {
        event.register(TestWireBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "wire")));
        event.register(TestGeneratorBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "generator")));
        event.register(TestMachineBlockEntity.class, String.valueOf(Identifier.of(NAMESPACE, "machine")));
    }

}
