package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.item.CompassMode;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.ConfigButtonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ConfigButton extends AbstractButton {
    private int type;
    public ConfigButton(int x, int y, int width, int height, int type) {
        super(x, y, width, height, Component.empty());
        this.type = type;
    }
    @Override
    public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }

    @Override
    public void renderString(GuiGraphics guiGraphics, Font font, int color) {
        guiGraphics.drawCenteredString(font, Language.getInstance().getVisualOrder(getMessage()), getX() + width / 2, getY() + (height - 8) / 2, color);
    }
    @Override
    public void onPress() {
        Player player =  Minecraft.getInstance().player;
        if ((!TCConfig.enableVillagersSearch.get() && this.type == 1) 
                || (!TCConfig.enableItemEntitiesSearch.get() && this.type == 2)
                || (!TCConfig.enableFluidSearch.get() && this.type == 3) 
                || (!TCConfig.enableSpawnerSearch.get() && this.type == 4)
                || (!TCConfig.enableMobsInventorySearch.get() && this.type == 5)
                || (!TCConfig.enableDropSearch.get() && this.type == 6)){
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() instanceof TravelersCompassItem){
            NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(this.type));
            setMode(stack,this.type);
        }
    }
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Player player =  Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            int k = 0;
            int j = 96;

            if (this.type == 3 || this.type == 4){
                k += 14;
            }
            if (this.type == 5 || this.type == 6){
                k += 28;
            }
            if (this.type == 6 || this.type == 7){
                k += 42;
            }
            int kM = type == 1 ? 0 : this.type - 1;
            if ((!TCConfig.enableVillagersSearch.get() && this.type == 1)
                    || (!TCConfig.enableItemEntitiesSearch.get() && this.type == 2)
                    || (!TCConfig.enableFluidSearch.get() && this.type == 3)
                    || (!TCConfig.enableSpawnerSearch.get() && this.type == 4)
                    || (!TCConfig.enableMobsInventorySearch.get() && this.type == 5)
                    || (!TCConfig.enableDropSearch.get() && this.type == 6)){
                k = 112;
            }
            k = k == 112 ? 112 : 14 * kM;
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                if (this.type == 1 && compassStack.isSearchingVillagers(stack)) {
                    j += 28;
                }
                if (this.type == 2 && compassStack.isSearchingItemEntities(stack)) {
                    j += 28;
                }
                if (this.type == 3 && compassStack.isSearchingFluids(stack)) {
                    j += 28;
                }
                if (this.type == 4 && compassStack.isSearchingSpawners(stack)) {
                    j += 28;
                }
                if (this.type == 5 && compassStack.isSearchingMobsInv(stack)) {
                    j += 28;
                }
                if (this.type == 6 && compassStack.isSearchingDrops(stack)) {
                    j += 28;
                }
                if (this.type == 7 && compassStack.isPaused(stack)) {
                    j += 28;
                }
            }
            if (this.isHovered()) {
                j += 14;
            }
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), k, j, this.width, this.height);
        }
    }

    public boolean isActive() {
        return this.active;
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(TravelersCompass.MODID, "textures/gui/component/gui_components.png");

    private void setMode(ItemStack stack, int ID) {
        if (stack.getItem() instanceof TravelersCompassItem travelersCompassItem) {
            switch (ID) {
                case (1):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_VILLAGERS);
                    break;
                case (2):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_DROPPED_ITEMS);
                    break;
                case (3):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_FLUIDS);
                    break;
                case (4):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_SPAWNERS);
                    break;
                case (5):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_MOBS_INV);
                    break;
                case (6):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_MOB_DROP);
                    break;
                case (7):
                    travelersCompassItem.writeCompassData(stack, CompassMode.PAUSED);
                    break;
            }
        }
    }
}