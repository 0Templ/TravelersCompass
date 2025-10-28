package com.nine.travelerscompass.client.utils;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.resources.ResourceLocation;

public record TextureData(
		ResourceLocation location,
		int u, int v,
		int width, int height,
		int sourceWidth, int sourceHeight) {
	
	public Builder toBuilder() {
		return new Builder().from(this);
	}
	
	public IconLayer layer(int xOffset, int yOffset) {
		return new IconLayer(this, xOffset, yOffset);
	}
	
	public IconLayer layer() {
		return new IconLayer(this);
	}
	
	public Icon icon(int xOffset, int yOffset) {
		return Icon.of(this, xOffset, yOffset);
	}
	
	public Icon icon() {
		return Icon.of(this);
	}
	
	public static class Builder {
		
		private ResourceLocation location;
		
		private int u = 0;
		private int v = 0;
		
		private int width, height;
		private int sourceWidth, sourceHeight;
		
		private static final String GUI_TEXTURES_PATH = "textures/gui/";
		
		public Builder location(String path) {
			this.location = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, GUI_TEXTURES_PATH + path + ".png");
			return this;
		}
		
		public Builder uv(int uv) {
			return uv(uv, uv);
		}
		
		public Builder uv(int u, int v) {
			this.u = u;
			this.v = v;
			return this;
		}
		
		public Builder sourceSize(int width, int height) {
			this.sourceWidth = width;
			this.sourceHeight = height;
			return this;
		}
		
		public Builder size(int size) {
			return size(size, size);
		}
		
		public Builder size(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}
		
		
		public Builder from(TextureData data) {
			this.location = data.location;
			this.u = data.u;
			this.v = data.v;
			this.width = data.width;
			this.height = data.height;
			this.sourceWidth = data.sourceWidth;
			this.sourceHeight = data.sourceHeight;
			return this;
		}
		
		public TextureData build() {
			if (this.sourceWidth == 0) this.sourceWidth = this.width;
			if (this.sourceHeight == 0) this.sourceHeight = this.height;
			return new TextureData(location, u, v, width, height, sourceWidth, sourceHeight);
		}
		
	}
	
}
