package com.nine.travelerscompass.client.component.button.settings;

import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.RangeIconVariants;

public class ButtonRangeSettings extends ButtonGenericSettings {
	
	public final RangeIconVariants plusIcons;
	public final RangeIconVariants minusIcons;
	
	public final Icon icon;
	
	public ButtonRangeSettings(Builder builder) {
		super(builder);
		this.plusIcons = builder.plusIcons;
		this.minusIcons = builder.minusIcons;
		this.icon = builder.icon;
	}
	
	public static Builder builder(ButtonGenericSettings.Builder<?> builder) {
		var ret = new Builder();
		builder.copyParentTo(ret);
		return ret;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends ButtonGenericSettings.Builder<Builder> {
		
		private RangeIconVariants plusIcons;
		private RangeIconVariants minusIcons;
		private Icon icon;
		
		public Builder plusIcons(Icon normal, Icon ctrl, Icon inactive) {
			this.plusIcons = new RangeIconVariants(normal, ctrl, inactive);
			return this;
		}
		
		public Builder minusIcons(Icon normal, Icon ctrl, Icon inactive) {
			this.minusIcons = new RangeIconVariants(normal, ctrl, inactive);
			return this;
		}
		
		public Builder icon(Icon icon) {
			this.icon = icon;
			return this;
		}
		
		@Override
		public Builder copy() {
			Builder copy = new Builder();
			copyParentTo(copy);
			copy.plusIcons = this.plusIcons;
			copy.minusIcons = this.minusIcons;
			copy.icon = this.icon;
			return copy;
		}
		
		@Override
		public ButtonRangeSettings build() {
			return new ButtonRangeSettings(this);
		}
		
	}
	
}
