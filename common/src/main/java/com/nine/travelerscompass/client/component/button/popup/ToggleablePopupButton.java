package com.nine.travelerscompass.client.component.button.popup;

import com.nine.travelerscompass.client.component.button.Toggleable;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.IconLayer;

public abstract class ToggleablePopupButton extends PopupButton implements Toggleable {
	
	protected boolean toggled;
	
	public ToggleablePopupButton(ButtonGenericSettings settings, IconLayer popupLayer, IconLayer connectorLayer) {
		super(settings, popupLayer, connectorLayer);
	}
	
	@Override
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	@Override
	public boolean isToggled() {
		return toggled;
	}
	
	@Override
	public boolean isPopupVisible() {
		return super.isPopupVisible() && isToggled();
	}
	
}
