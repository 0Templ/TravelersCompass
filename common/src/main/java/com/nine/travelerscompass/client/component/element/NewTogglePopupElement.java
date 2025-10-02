package com.nine.travelerscompass.client.component.element;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.component.button.Toggleable;
import com.nine.travelerscompass.client.utils.ButtonTextures;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class NewTogglePopupElement extends NewPopupElement implements Toggleable {

    protected static final Component DISABLED = Component.translatable("tooltip.travelerscompass.toggle_element.disabled").withStyle(ChatFormatting.GRAY);
    protected static final Component ENABLED = Component.translatable("tooltip.travelerscompass.toggle_element.enabled").withStyle(ChatFormatting.GRAY);

    protected boolean toggled;
    protected final ButtonTextures buttonTextures;
    protected final IconTexture iconActive;
    protected final IconTexture iconInactive;

    public NewTogglePopupElement(int width, int height, ButtonTextures buttonTextures, IconTexture iconActive, IconTexture iconInactive) {
        super(width, height);
        this.buttonTextures = buttonTextures;
        this.iconActive = iconActive;
        this.iconInactive = iconInactive;
    }

    public NewTogglePopupElement(int width, int height, IconTexture iconActive, IconTexture iconInactive){
        this(width, height, ClientData.SMALL_TOGGLE_BUTTON, iconActive, iconInactive);
    }

    @Override
    public boolean isToggled(){
        return toggled;
    }

    @Override
    public void setToggled(boolean toggled){
        this.toggled = toggled;
    }

    protected IconTexture getIconTexture() {
        return isToggled() ? iconActive : iconInactive;
    }

    protected TextureData getBaseLayer() {
        return buttonTextures.get(toggled, isHovered);
    }

    public void renderIcon(GuiGraphics graphics, int xPos, int yPos) {
        IconTexture icon = getIconTexture();
        ClientUtils.renderTexture(graphics, icon, xPos, yPos);
    }

    @Override
    public void renderElement(GuiGraphics graphics, int xPos, int yPos) {
        TextureData baseLayer = getBaseLayer();
        ClientUtils.renderTexture(graphics, baseLayer, xPos, yPos);
        renderIcon(graphics, xPos, yPos);
    }

}
