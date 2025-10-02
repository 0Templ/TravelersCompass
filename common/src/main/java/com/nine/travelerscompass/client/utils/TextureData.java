package com.nine.travelerscompass.client.utils;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.resources.ResourceLocation;

public class TextureData {

    private final int u;
    private final int v;

    private final int width;
    private final int height;

    private final int textureWidth;
    private final int textureHeight;


    private final int renderWidth;
    private final int renderHeight;

    private final ResourceLocation location;

    public TextureData(ResourceLocation location, int u, int v, int width, int height, int renderWidth, int renderHeight, int textureWidth, int textureHeight) {
        this.location = location;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.renderWidth = renderWidth;
        this.renderHeight = renderHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public int getV() {
        return v;
    }

    public int getU() {
        return u;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public IconTexture icon(int x, int y){
        return new IconTexture(this, x, y);
    }

    public static class Builder {

        private ResourceLocation location;

        private int u = 0;
        private int v = 0;

        private int textureWidth = 0;
        private int textureHeight = 0;

        private int width;
        private int height;

        private int renderWidth;
        private int renderHeight;


        private static final String GUI_TEXTURES_PATH = "textures/gui/";

        public Builder location(String path){
            this.location = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, GUI_TEXTURES_PATH + path + ".png");
            return this;
        }

        public Builder uv(int u, int v){
            this.u = u;
            this.v = v;
            return this;
        }

        public Builder renderSize(int size){
            return renderSize(size, size);
        }

        public Builder renderSize(int width, int height){
            this.renderWidth = width;
            this.renderHeight = height;
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

        public Builder sourceSize(int width, int height){
            this.textureWidth = width;
            this.textureHeight = height;
            return this;
        }

        public Builder from(TextureData data){
            this.location = data.location;
            this.u = data.u;
            this.v = data.v;
            this.width = data.width;
            this.height = data.height;
            this.renderWidth = data.renderWidth;
            this.renderHeight = data.renderHeight;
            this.textureWidth = data.textureWidth;
            this.textureHeight = data.textureHeight;
            return this;
        }

        public TextureData build() {
            if (renderWidth == 0) renderWidth = width;
            if (renderHeight == 0) renderHeight = height;
            if (textureWidth == 0) textureWidth = width;
            if (textureHeight == 0) textureHeight = height;
            return new TextureData(location, u, v, width, height, renderWidth, renderHeight, textureWidth, textureHeight);
        }
    }
}
