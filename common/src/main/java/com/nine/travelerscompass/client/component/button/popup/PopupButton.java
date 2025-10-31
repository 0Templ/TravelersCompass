package com.nine.travelerscompass.client.component.button.popup;

import com.nine.travelerscompass.client.component.button.base.BaseButton;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconLayer;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.Rect2i;

import java.util.ArrayList;
import java.util.List;

public abstract class PopupButton extends BaseIconButton {
	
	protected final IconLayer popupLayer;
	protected final IconLayer connectorLayer;
	
	protected final Rect2i popupRect;
	
	private boolean popupVisible = false;
	
	public List<BaseButton> popupButtons = new ArrayList<>();
	
	private static final int POPUP_PADDING = 1;
	
	protected PopupButton(ButtonGenericSettings settings, IconLayer popupLayer, IconLayer connectorLayer) {
		super(settings);
		this.popupLayer = popupLayer;
		this.connectorLayer = connectorLayer;
		this.popupRect = calculatePopupRect(this.popupLayer);
	}
	
	protected Rect2i calculatePopupRect(IconLayer popupTexture) {
		TextureData textureData = popupTexture.textureData();
		
		int x = (int) (getX() + popupTexture.xOffset() - POPUP_PADDING);
		int y = (int) (getY() + popupTexture.yOffset() - POPUP_PADDING);
		
		int width = textureData.width() + POPUP_PADDING;
		int height = textureData.height() + POPUP_PADDING;
		
		return new Rect2i(x, y, width, height);
	}
	
	public boolean isPopupVisible() {
		return popupVisible;
	}
	
	public void onPopupClosed() {
	}
	
	public void onPopupOpened() {
	}
	
	public void popupMouseClicked(MouseButtonEvent event, boolean bl) {
		if (isPopupVisible()) {
			if (popupRect.contains((int) event.x(), (int) event.y())) {
				for (BaseButton popupElement : popupButtons) {
					popupElement.mouseClicked(event, bl);
				}
			}
		}
	}
	
	public void updatePopupButtons() {
		for (var b : popupButtons) {
			b.updateState();
		}
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		int mX = (int) mouseX;
		int mY = (int) mouseY;
		if (isPopupVisible()) {
			if (popupRect.contains(mX, mY)) {
				for (BaseButton popupElement : popupButtons) {
					popupElement.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
				}
			}
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	public void afterPopupClick() {
	}
	
	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.renderWidget(graphics, mouseX, mouseY, partialTicks);
		if (isHovered) {
			if (!popupVisible) {
				onPopupOpened();
			}
			popupVisible = true;
		} else if (popupVisible && !popupRect.contains(mouseX, mouseY)) {
			onPopupClosed();
			popupVisible = false;
		}
		if (isPopupVisible()) {
			renderPopup(graphics, mouseX, mouseY, partialTicks);
			for (BaseButton button : popupButtons) {
				button.render(graphics, mouseX, mouseY, partialTicks);
			}
		}
	}
	
	protected void renderPopup(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		ClientUtils.renderIconLayer(graphics, popupLayer, getX(), getY());
		ClientUtils.renderIconLayer(graphics, connectorLayer, getX(), getY());
	}
	
	
}
