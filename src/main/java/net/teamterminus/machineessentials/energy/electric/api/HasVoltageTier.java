package net.teamterminus.machineessentials.energy.electric.api;

public interface HasVoltageTier {

    VoltageTier getTier();

    default VoltageTier getTier(HasVoltageTier block) {
        return block.getTier();
    }
}
