package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.utils.ButtonTextures;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.GuiGraphics;

public class ToggleButton extends BaseButton implements Toggleable {

    protected final ButtonTextures buttonTextures;

    protected boolean toggled;

    public ToggleButton(int x, int y, int width, int height, ButtonTextures buttonTextures) {
        super(x, y, width, height);
        this.buttonTextures = buttonTextures;
    }

    @Override
    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    @Override
    public boolean isToggled(){
        return toggled;
    }

    protected TextureData getBaseLayer(){
        return buttonTextures.get(toggled, isHovered);
    }

    @Override
    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        TextureData baseLayer = getBaseLayer();
        ClientUtils.renderTexture(graphics, baseLayer, this.getX(), this.getY());
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

}
