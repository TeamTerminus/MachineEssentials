package net.teamterminus.machineessentials.energy.electric.api;

/**
 * Properties of a material that an electric wire can be made out of.
 *
 * @param translationKey
 * @param color
 * @param defaultAmps
 * @param maxVoltage
 * @param lossPerBlock
 * @param meltingTemperature
 */
public record WireMaterial(
        String translationKey,
        int color,
        int defaultAmps,
        VoltageTier maxVoltage,
        int lossPerBlock,
        int meltingTemperature
) {
}
