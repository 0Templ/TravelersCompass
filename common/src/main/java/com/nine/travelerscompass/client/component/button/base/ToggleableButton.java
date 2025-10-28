package com.nine.travelerscompass.client.component.button.base;

import com.nine.travelerscompass.client.component.button.Toggleable;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;

public abstract class ToggleableButton extends BaseIconButton implements Toggleable {
	
	protected boolean toggled;
	
	public ToggleableButton(ButtonGenericSettings settings) {
		super(settings);
	}
	
	@Override
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	@Override
	public boolean isToggled() {
		return toggled;
	}
	
}
