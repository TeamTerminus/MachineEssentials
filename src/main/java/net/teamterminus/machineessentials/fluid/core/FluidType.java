package net.teamterminus.machineessentials.fluid.core;

import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.util.Identifier;

public record FluidType(Identifier id, Block flowing, Block still, int unitsPerBucket, boolean canBeSolidified) {
    public String getTranslationKey() {
        return "fluid." + id.namespace + "." + id.path + ".name";
    }

    public String getTranslatedName() {
        return I18n.getTranslation(getTranslationKey());
    }
}
