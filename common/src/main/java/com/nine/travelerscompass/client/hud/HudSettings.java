package com.nine.travelerscompass.client.hud;

import com.nine.travelerscompass.common.utils.Alignment;
import com.nine.travelerscompass.common.utils.HudRenderMode;
import com.nine.travelerscompass.common.utils.HudType;

public record HudSettings (
        HudRenderMode renderMode,
        HudType hudType,
        Alignment alignment,
        boolean hudWithChat,
        float scale,
        int width,
        int height,
        int xPos,
        int yPos){

    public static final int EDGE_PADDING = 7;
    public static final int HEIGHT_PADDING = 2;
    public static final int WIDTH_PADDING = 2;

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder {

        private HudRenderMode renderMode;
        private HudType hudType;
        private Alignment alignment;
        private boolean hudWithChat;
        private float scale;
        private int width;
        private int height;
        private int xPos;
        private int yPos;

        public Builder(HudSettings settings) {
            this.renderMode = settings.renderMode;
            this.hudType = settings.hudType;
            this.alignment = settings.alignment;
            this.hudWithChat = settings.hudWithChat;
            this.scale = settings.scale;
            this.width = settings.width;
            this.height = settings.height;
            this.xPos = settings.xPos;
            this.yPos = settings.yPos;
        }

        public HudSettings build() {
            return new HudSettings(
                    renderMode,
                    hudType,
                    alignment,
                    hudWithChat,
                    scale,
                    width,
                    height,
                    xPos,
                    yPos
            );
        }

        public Builder renderMode(HudRenderMode value) {
            this.renderMode = value;
            return this;
        }

        public Builder hudType(HudType value) {
            this.hudType = value;
            return this;
        }

        public Builder alignment(Alignment value) {
            this.alignment = value;
            return this;
        }

        public Builder hudWithChat(boolean value) {
            this.hudWithChat = value;
            return this;
        }

        public Builder scale(float value) {
            this.scale = value;
            return this;
        }

        public Builder width(int value) {
            this.width = value;
            return this;
        }

        public Builder height(int value) {
            this.height = value;
            return this;
        }

        public Builder xPos(int value) {
            this.xPos = value;
            return this;
        }

        public Builder yPos(int value) {
            this.yPos = value;
            return this;
        }
    }
}
