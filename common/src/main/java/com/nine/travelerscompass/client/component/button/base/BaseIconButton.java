package com.nine.travelerscompass.client.component.button.base;

import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.ButtonTexturesSet;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.GuiGraphics;

public abstract class BaseIconButton extends BaseButton {
	
	protected final ButtonTexturesSet buttonTexturesSet;
	
	public BaseIconButton(ButtonGenericSettings settings) {
		super(settings);
		this.buttonTexturesSet = settings.buttonTexturesSet;
	}
	
	protected ButtonTexturesSet buttonTexturesSet() {
		return buttonTexturesSet;
	}
	
	protected abstract TextureData getMainLayerTexture();
	
	protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		ClientUtils.renderTexture(graphics, getMainLayerTexture(), this.getX(), this.getY());
	}
	
	protected abstract Icon getIcon();
	
	protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		ClientUtils.renderIcon(graphics, getIcon(), this.getX(), this.getY());
	}
	
	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.renderWidget(graphics, mouseX, mouseY, partialTicks);
		renderMainLayer(graphics, mouseX, mouseY, partialTicks);
		renderIcon(graphics, mouseX, mouseY, partialTicks);
	}
	
}
