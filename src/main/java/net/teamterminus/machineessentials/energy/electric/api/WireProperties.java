package net.teamterminus.machineessentials.energy.electric.api;

/**
 * Properties of a specific type of wire made out of <code>material</code>.
 *
 * @param size
 * @param insulated
 * @param superconductor
 * @param material
 */
public record WireProperties(
        int size,
        boolean insulated,
        boolean superconductor,
        WireMaterial material
) {
}
