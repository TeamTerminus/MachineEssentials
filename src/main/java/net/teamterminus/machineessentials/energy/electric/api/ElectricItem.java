package net.teamterminus.machineessentials.energy.electric.api;

import net.minecraft.item.ItemStack;

public interface ElectricItem {

    /**
     * @return Amount of energy currently available in item
     */
    long getEnergy(ItemStack stack);

    /**
     * @return Maximum energy capacity of the item
     */
    long getCapacity(ItemStack stack);

    /**
     * @return Amount of unused capacity left
     */
    default long getRemainingCapacity(ItemStack stack) {
        return getCapacity(stack) - getEnergy(stack);
    }

    /**
     * @return Maximum voltage this item can handle
     */
    long getMaxVoltage(ItemStack stack);

    /**
     * @return Maximum amount of amps this item can use
     */
    long getMaxInputAmperage(ItemStack stack);


    /**
     * @return Maximum amperage this item can deliver
     */
    long getMaxOutputAmperage(ItemStack stack);


    /**
     * Charges the item.
     *
     * @param energy The amount of energy to charge the item by
     * @return Amount of energy actually charged
     */
    long addEnergy(ItemStack stack, long energy);

    /**
     * Discharges the item.
     *
     * @param energy The amount of energy to discharge
     * @return Amount of energy actually discharged
     */
    long removeEnergy(ItemStack stack, long energy);

}
