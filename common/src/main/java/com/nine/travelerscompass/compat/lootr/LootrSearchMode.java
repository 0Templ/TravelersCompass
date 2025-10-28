package com.nine.travelerscompass.compat.lootr;

public enum LootrSearchMode {
	
	ALL("both"),
	CLOSED("closed"),
	OPENED("opened"),
	OFF("off");
	
	private final String key;
	
	LootrSearchMode(String key) {
		this.key = key;
	}
	
	public String key() {
		return key;
	}
	
	public enum ConfigMode {
		CLOSED,
		OPENED,
		ALL
	}
	
}
