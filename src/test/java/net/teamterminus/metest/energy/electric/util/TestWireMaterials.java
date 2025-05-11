package net.teamterminus.metest.energy.electric.util;

import net.teamterminus.machineessentials.energy.electric.api.VoltageTier;
import net.teamterminus.machineessentials.energy.electric.api.WireMaterial;

public class TestWireMaterials {

    public static WireMaterial test;

    static {
        test = new WireMaterial("material.metest.wire.test", 0x555555, 1, VoltageTier.LV, 0, 9001);
    }

}
