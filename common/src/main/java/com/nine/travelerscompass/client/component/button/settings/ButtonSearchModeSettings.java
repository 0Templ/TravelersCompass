package com.nine.travelerscompass.client.component.button.settings;

import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.common.data.DataStorage;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ButtonSearchModeSettings extends ButtonGenericSettings {
	
	public final DataStorage<Boolean> data;
	public final Icon iconActive;
	public final Icon iconInactive;
	public final Icon iconLock;
	public final boolean configEnabled;
	public final List<Component> descriptions;
	
	public ButtonSearchModeSettings(Builder builder) {
		super(builder);
		this.data = builder.data;
		this.iconActive = builder.iconActive;
		this.iconInactive = builder.iconInactive;
		this.iconLock = builder.iconLock;
		this.configEnabled = builder.configEnabled;
		this.descriptions = builder.descriptions;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends ButtonGenericSettings.Builder<Builder> {
		
		private DataStorage<Boolean> data;
		private Icon iconActive;
		private Icon iconInactive;
		private Icon iconLock;
		private boolean configEnabled;
		private List<Component> descriptions;
		
		public Builder data(DataStorage<Boolean> data) {
			this.data = data;
			return this;
		}
		
		public Builder icons(Icon active, Icon inactive) {
			this.iconActive = active;
			this.iconInactive = inactive;
			return this;
		}
		
		public Builder lockIcon(Icon lock) {
			this.iconLock = lock;
			return this;
		}
		
		public Builder descriptions(Component... descriptions) {
			this.descriptions = List.of(descriptions);
			return this;
		}
		
		public Builder configEnabled(boolean configEnabled) {
			this.configEnabled = configEnabled;
			return this;
		}
		
		@Override
		public Builder copy() {
			Builder copy = new Builder();
			copyParentTo(copy);
			copy.data = this.data;
			copy.iconActive = this.iconActive;
			copy.iconInactive = this.iconInactive;
			copy.iconLock = this.iconLock;
			copy.configEnabled = this.configEnabled;
			copy.descriptions = this.descriptions;
			
			return copy;
		}
		
		@Override
		public ButtonSearchModeSettings build() {
			return new ButtonSearchModeSettings(this);
		}
		
	}
	
}
