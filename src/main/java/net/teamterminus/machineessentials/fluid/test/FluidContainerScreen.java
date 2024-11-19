package net.teamterminus.machineessentials.fluid.test;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.world.World;
import net.teamterminus.machineessentials.fluid.core.FluidScreen;
import net.teamterminus.machineessentials.fluid.core.api.FluidInventory;
import org.lwjgl.opengl.GL11;

public class FluidContainerScreen extends FluidScreen
{

    public FluidContainerScreen(PlayerInventory playerInventory, FluidInventory fluidInventory)
    {
        super(new FluidContainerScreenHandler(playerInventory, fluidInventory));
    }

    public void removed()
    {
        super.removed();
        container.onClosed(minecraft.player);
    }

    protected void drawForeground()
    {
        textRenderer.draw("Fluid Container", 8, 6, 0x404040);
        textRenderer.draw("Inventory", 8, (backgroundHeight - 96) + 2, 0x404040);
    }

    protected void drawBackground(float f)
    {
        int i = minecraft.textureManager.getTextureId("/assets/machineessentials/stationapi/gui/tank_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.textureManager.bindTexture(i);
        int j = (width - backgroundWidth) / 2;
        int k = (height - backgroundHeight) / 2;
        drawTexture(j, k, 0, 0, backgroundWidth, backgroundHeight);
    }
}