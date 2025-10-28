package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class TabButton extends BaseIconButton {
	
	public boolean selected;
	private final Icon iconTexture;
	
	public TabButton(ButtonGenericSettings settings, boolean selected, Icon iconTexture, Component tooltip) {
		super(settings);
		this.selected = selected;
		this.iconTexture = iconTexture;
		this.setTooltip(Tooltip.create(tooltip));
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return selected ? CompassUI.TabTextures.PAGE_TAB_ACTIVE : CompassUI.TabTextures.PAGE_TAB_INACTIVE;
	}
	
	@Override
	protected Icon getIcon() {
		return iconTexture;
	}
	
}
