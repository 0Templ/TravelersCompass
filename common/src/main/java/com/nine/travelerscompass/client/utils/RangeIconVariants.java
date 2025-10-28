package com.nine.travelerscompass.client.utils;

public record RangeIconVariants(Icon normal, Icon ctrl, Icon inactive) {
	
	public Icon get(boolean ctrl, boolean active) {
		return active ? (ctrl ? this.ctrl : this.normal) : this.inactive;
	}
	
}
