package net.teamterminus.machineessentials.energy.electric.api;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.util.Formatting;

/**
 * Voltage tiers from ULV (8V) to MAX (65535V), with their colors and names.
 */
public enum VoltageTier {
    ULV("voltage.ulv.name", 1, 8, Formatting.GRAY, 0x555555),
    LV("voltage.lv.name", 9, 32, Formatting.RED, 0xFF5555),
    MV("voltage.mv.name", 33, 128, Formatting.GOLD, 0xFFAA00),
    HV("voltage.hv.name", 129, 512, Formatting.YELLOW, 0xFFFF55),
    EV("voltage.ev.name", 513, 2048, Formatting.GREEN, 0x55FF55),
    UV("voltage.uv.name", 2049, 8192, Formatting.AQUA, 0x8C0000),
    OV("voltage.ov.name", 8193, 32768, Formatting.DARK_PURPLE, 0x8C0000),
    MAX("voltage.max.name", 32769, 65535, Formatting.LIGHT_PURPLE, 0xFF55FF);

    public final String translationKey;
    public final int minVoltage;
    public final int maxVoltage;
    public final Formatting textColor;
    public final int color;

    // cope mine diver
    private static final Int2ObjectOpenHashMap<VoltageTier> cache = new Int2ObjectOpenHashMap<>();

    VoltageTier(String translationKey, int minVoltage, int maxVoltage, Formatting textColor, int color) {
        this.translationKey = translationKey;
        this.minVoltage = minVoltage;
        this.maxVoltage = maxVoltage;
        this.textColor = textColor;
        this.color = color;
    }

    public String getName() {
        return I18n.getTranslation(translationKey);
    }

    public static VoltageTier get(int voltage) {
        return cache.computeIfAbsent(voltage, VoltageTier::internalGet);
    }

    private static VoltageTier internalGet(int voltage) {
        for (VoltageTier tier : VoltageTier.values()) {
            if (voltage >= tier.minVoltage && voltage <= tier.maxVoltage) {
                return tier;
            }
        }
        return null;
    }
}
