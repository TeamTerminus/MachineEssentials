package net.teamterminus.metest.energy.electric.event;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.util.Identifier;
import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.machineessentials.energy.electric.api.WireProperties;
import net.teamterminus.metest.energy.electric.block.TestGeneratorBlock;
import net.teamterminus.metest.energy.electric.block.TestMachineBlock;
import net.teamterminus.metest.energy.electric.block.TestWireBlock;
import net.teamterminus.metest.energy.electric.util.TestWireMaterials;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static net.teamterminus.metest.METest.NAMESPACE;

public class ElectricBlocks {

    public static TestWireBlock wire;
    public static TestGeneratorBlock generator;
    public static TestMachineBlock machine;

    @EventListener
    public static void registerBlocks(BlockRegistryEvent event) {
        wire = (TestWireBlock) new TestWireBlock(Identifier.of(NAMESPACE, "wire"), Material.METAL, new WireProperties(1, false, true, TestWireMaterials.test))
                .setTranslationKey(NAMESPACE, "wire")
                .setHardness(1)
                .setResistance(5);

        generator = (TestGeneratorBlock) new TestGeneratorBlock(Identifier.of(NAMESPACE, "generator"), Material.METAL, VoltageTier.LV)
                .setTranslationKey(NAMESPACE, "generator")
                .setHardness(1)
                .setResistance(5);

        machine = (TestMachineBlock) new TestMachineBlock(Identifier.of(NAMESPACE, "machine"), Material.METAL, VoltageTier.LV)
                .setTranslationKey(NAMESPACE, "machine")
                .setHardness(1)
                .setResistance(5);
    }
}
