package net.teamterminus.machineessentials.fluid.core;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.gui.screen.container.TooltipRenderEvent;
import net.teamterminus.machineessentials.util.FluidSlotInteraction;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public abstract class FluidScreen extends HandledScreen {

    public FluidScreenHandler container;

    public FluidScreen(FluidScreenHandler container) {
        super(container);
        this.container = container;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
        PlayerInventory playerInventory = this.minecraft.player.inventory;
        FluidSlot hoveredSlot = null;
        int w = (this.width - this.backgroundWidth) / 2;
        int h = (this.height - this.backgroundHeight) / 2;
        for (int i = 0; i < this.container.fluidSlots.size(); i++) {
            FluidSlot slot = this.container.fluidSlots.get(i);
            if(slot == null) continue;
            if(slot.hasStack()) {
                this.drawFluidSlot(slot);
            }
            if (this.isPointOverFluidSlot(slot, mouseX, mouseY)) {
                hoveredSlot = slot;
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                int x = hoveredSlot.x;
                int y = hoveredSlot.y;
                this.fillGradient(w+x, h+y, w+x + 16, h+y + 16, -2130706433, -2130706433);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }

        if(playerInventory.getCursorStack() == null && hoveredSlot != null && hoveredSlot.hasStack()){
            String s = (TranslationStorage.getInstance().getClientTranslation(hoveredSlot.getFluidStack().toItemStack().getTranslationKey())).trim();
            if (!s.isEmpty()) {
                int x = mouseX + 12;
                int y = mouseY - 12;
                int sWidth = this.textRenderer.getWidth(s);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                this.fillGradient(x - 3, y - 3, x + sWidth + 3, y + 8 + 3, 0xc0000000, 0xc0000000);
                this.textRenderer.drawWithShadow(s, x, y, 0xFFFFFFFF);
                GL11.glEnable(2896);
                GL11.glEnable(2929);
            }
        }
    }

    private void drawFluidSlot(FluidSlot slot) {
        int w = (this.width - this.backgroundWidth) / 2;
        int h = (this.height - this.backgroundHeight) / 2;
        int x = slot.x;
        int y = slot.y;
        ItemStack stack = slot.getFluidStack().toItemStack();

        itemRenderer.renderGuiItem(this.textRenderer, this.minecraft.textureManager, stack, w+x, h+y);
        itemRenderer.renderGuiItemDecoration(this.textRenderer, this.minecraft.textureManager, stack, w+x, h+y);
    }


    protected boolean isPointOverFluidSlot(FluidSlot slot1, int x, int y) {
        int w = (this.width - this.backgroundWidth) / 2;
        int h = (this.height - this.backgroundHeight) / 2;
        x -= w;
        y -= h;
        return x >= slot1.x - 1 && x < slot1.x + 16 + 1 && y >= slot1.y - 1 && y < slot1.y + 16 + 1;
    }

    protected FluidSlot getFluidSlotAtPosition(int x, int y) {
        for(int i3 = 0; i3 < container.fluidSlots.size(); ++i3) {
            FluidSlot slot4 = container.fluidSlots.get(i3);
            if(this.isPointOverFluidSlot(slot4, x, y)) {
                return slot4;
            }
        }

        return null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button == 0 || button == 1) {
            FluidSlot slot = this.getFluidSlotAtPosition(mouseX, mouseY);
            int w = (this.width - this.backgroundWidth) / 2;
            int h = (this.height - this.backgroundHeight) / 2;
            boolean isOutside = mouseX < w || mouseY < h || mouseX >= w + this.backgroundWidth || mouseY >= h + this.backgroundHeight;
            int slotId = -1;
            if (slot != null) {
                slotId = slot.id;
            }

            if (isOutside) {
                slotId = -999;
            }

            if (slotId != -1) {
                boolean shift = slotId != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                ((FluidSlotInteraction) this.minecraft.interactionManager).machineEssentials$fluidSlotClick(this.container.syncId, slotId, button, shift, this.minecraft.player);
            }
        }

    }
}
