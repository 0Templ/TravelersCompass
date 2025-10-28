package com.nine.travelerscompass.client.utils;

public record ButtonGrid(int x, int y, int buttonWidth, int buttonHeight, int gapX, int gapY) {
	
	public int x(int column) {
		return x + column * (buttonWidth + gapX);
	}
	
	public int y(int row) {
		return y + row * (buttonHeight + gapY);
	}
	
}