package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class TabButton extends BaseButton {

    public boolean enabled;
    private final TextureData iconTexture;
    private final int imXOffset, imYOffset;

    public TabButton(int x, int y, int width, int height, int imXOffset, int imYOffset, boolean enabled, TextureData iconTexture, OnPress onPress, Component tooltip) {
        super(x, y, width, height, onPress);
        this.enabled = enabled;
        this.iconTexture = iconTexture;
        this.imXOffset = imXOffset;
        this.imYOffset = imYOffset;
        this.setTooltip(Tooltip.create(tooltip));
    }

    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        ClientUtils.renderTexture(graphics, enabled ? ClientData.TAB_PAGE_ACTIVE : ClientData.TAB_PAGE_INACTIVE, this.getX(), this.getY());
        ClientUtils.renderTexture(graphics, iconTexture, this.getX() + imXOffset, this.getY() + imYOffset);
    }
}
