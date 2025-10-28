package com.nine.travelerscompass.common.utils;

public enum HeightAttitude {
	
	UP("up"),
	DOWN("down"),
	SAME("same"),
	NONE("none");
	
	private final String key;
	
	HeightAttitude(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
}
