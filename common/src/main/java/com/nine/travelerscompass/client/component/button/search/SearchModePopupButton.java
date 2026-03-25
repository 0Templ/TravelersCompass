package com.nine.travelerscompass.client.component.button.search;

import com.nine.travelerscompass.client.ui.constant.TCComponents;

import com.nine.travelerscompass.client.component.button.Toggleable;
import com.nine.travelerscompass.client.component.button.popup.ToggleablePopupButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.IconLayer;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public abstract class SearchModePopupButton extends ToggleablePopupButton implements Toggleable {
	
	private boolean relevant = true;
	protected final boolean configEnabled;
	private final DataStorage<Boolean> data;
	public final Icon iconActive;
	public final Icon iconInactive;
	public final Icon iconLock;
	
	public SearchModePopupButton(ButtonSearchModeSettings settings, IconLayer popupLayer, IconLayer connectorLayer) {
		super(settings, popupLayer, connectorLayer);
		this.configEnabled = settings.configEnabled;
		this.data = settings.data;
		this.iconActive = settings.iconActive;
		this.iconInactive = settings.iconInactive;
		this.iconLock = settings.iconLock;
		
		setToggled(data.get(stack()));
		checkRelevancy();
		refreshTooltip();
	}
	
	@Override
	protected Icon getIcon() {
		if (configEnabled) {
			if (isRelevant() && isToggled()) {
				return iconActive;
			}
			return iconInactive;
		}
		return iconLock;
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		CompassComponents.toggleToServer(stack(), data, true);
		setToggled(data.get(stack()));
		checkRelevancy();
		refreshTooltip();
		return true;
	}
	
	@Override
	public void afterPopupClick() {
		checkRelevancy();
		refreshTooltip();
	}
	
	@Override
	public void refreshTooltip() {
		String key = "tooltip.travelerscompass.search_mode." + data.id();
		var desc = Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY);
		this.setTooltip(TooltipBuilder.builder()
				.title(key)
				.descIf(desc, configEnabled && shiftPressed)
				.state(configEnabled ? (isToggled() ? (isRelevant() ? TCComponents.ENABLED : TCComponents.INACTIVE) : TCComponents.DISABLED) : TCComponents.CONFIG_DISABLED)
				.buildAsTooltip());
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return buttonTexturesSet().get(isToggled(), isHovered, configEnabled);
	}
	
	public boolean isRelevant() {
		return relevant;
	}
	
	public void setRelevant(boolean relevant) {
		this.relevant = relevant;
	}
	
	public void checkRelevancy() {
	}
	
}
