package net.teamterminus.metest.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.TooltipHelper;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipBuildEvent;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(TooltipHelper.class)
public class TooltipHelperMixin {

    /**
     * @author sunsetsatellite
     * @reason yes
     */
    @Overwrite
    public static ArrayList<String> getTooltipForItemStack(String originalTooltip, ItemStack itemStack, PlayerInventory playerInventory, HandledScreen container) {
        ArrayList<String> newTooltip;
        CustomTooltipProvider provider = null;

        if (itemStack.getItem() instanceof CustomTooltipProvider itemProvider) {
            provider = itemProvider;
        } else if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CustomTooltipProvider blockProvider) {
            provider = blockProvider;
        }

        if (provider != null) {
            newTooltip = new ArrayList<>(Arrays.asList(provider.getTooltip(itemStack, originalTooltip)));
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(originalTooltip);
            newTooltip = list;
        }

        StationAPI.EVENT_BUS.post(TooltipBuildEvent.builder()
                .tooltip(newTooltip)
                .inventory(playerInventory)
                .container(container)
                .itemStack(itemStack)
                .build()
        );

        return newTooltip;
    }

}
