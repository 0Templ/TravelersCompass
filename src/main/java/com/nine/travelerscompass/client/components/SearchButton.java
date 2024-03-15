package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.item.CompassMode;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.SearchButtonPacket;
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

public class SearchButton extends AbstractButton {
    private int type;
    public SearchButton(int x, int y, int width, int height, int type) {
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
        if (player == null || (!TCConfig.enableMobSearch.get() && this.type == 1) || (!TCConfig.enableContainerSearch.get() && this.type == 2) || (!TCConfig.enableBlockSearch.get() && this.type == 3)){
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() instanceof TravelersCompassItem){
            NetworkHandler.CHANNEL.sendToServer(new SearchButtonPacket(this.type));
            setMode(stack,this.type);
        }
    }
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Player player =  Minecraft.getInstance().player;
        if (player != null) {

            int j = 0;
            int k = 0;
            int kM = type == 1 ? 0 : this.type - 1;

            ItemStack stack = player.getMainHandItem();

            if (!TCConfig.enableMobSearch.get() && this.type == 1){
                k = 54;
            }
            if (!TCConfig.enableContainerSearch.get() && this.type == 2){
                k = 54;
            }
            if (!TCConfig.enableBlockSearch.get() && this.type == 3){
                k = 54;
            }

            k = k == 54 ? 54 : 18*kM;
            if(stack.getItem() instanceof TravelersCompassItem compassStack && this.type == 1 && compassStack.isSearchingMobs(stack) && TCConfig.enableMobSearch.get()){
                j += 36;
            }
            if(stack.getItem() instanceof TravelersCompassItem compassStack && this.type == 2 && compassStack.isSearchingContainers(stack) && TCConfig.enableContainerSearch.get()){
                j += 36;
            }
            if(stack.getItem() instanceof TravelersCompassItem compassStack && this.type == 3 && compassStack.isSearchingBlocks(stack) && TCConfig.enableBlockSearch.get()){
                j += 36;
            }

            if (this.isHovered()) {
                j += 18;
            }
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), k, j, this.width, this.height);

        }
    }
    private static final ResourceLocation TEXTURE = new ResourceLocation(TravelersCompass.MODID, "textures/gui/component/gui_components.png");

    private void setMode(ItemStack stack,int ID) {
        if (stack.getItem() instanceof TravelersCompassItem travelersCompassItem) {
            switch (ID) {
                case (1):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_MOBS);
                    break;
                case (2):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_CONTAINERS);
                    break;
                case (3):
                    travelersCompassItem.writeCompassData(stack, CompassMode.SEARCHING_BLOCKS);
                    break;
            }
        }
    }
}