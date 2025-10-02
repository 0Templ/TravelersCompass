package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.utils.ButtonTextures;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;

public class TogglePopupButton extends PopupButton implements Toggleable {

    protected final ButtonTextures buttonTextures;

    protected boolean toggled;
    protected boolean activated;

    protected TogglePopupButton(int x, int y, int width, int height, ButtonTextures buttonTextures, TextureData popupConnector, TextureData popupTexture, Rect2i mainRect, Rect2i popupRect) {
        super(x, y, width, height, popupConnector, popupTexture, mainRect, popupRect);
        this.buttonTextures = buttonTextures;
    }

    @Override
    public void setToggled(boolean toggled){
        this.toggled = toggled;
    }

    @Override
    public boolean isToggled(){
        return toggled;
    }

    public void setActivated(boolean activated){
        this.activated = activated;
    }

    public boolean isActivated(){
        return activated;
    }

    @Override
    protected boolean isPopupVisible(){
        return super.isPopupVisible() && isToggled();
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData baseLayer = buttonTextures.get(toggled, isHovered);
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
    }

}
