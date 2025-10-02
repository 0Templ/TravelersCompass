package com.nine.travelerscompass.client.component.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BaseButton extends Button {

    protected boolean shiftPressed = false;
    protected boolean ctrlPressed = false;
    protected boolean hoverFlag = false;

    public BaseButton(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty(), (button) -> {}, DEFAULT_NARRATION);
    }

    protected BaseButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
    }

    public void refreshTooltip(){
    }

    protected void onKeyModifierChanged(){
        refreshTooltip();
    }

    protected void onEnter(){

    }

    protected void onExit(){

    }

    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
    }

    protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
    }

    public void tick(){
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderMainLayer(graphics, mouseX, mouseY, partialTicks);
        boolean shiftDown = Screen.hasShiftDown();
        boolean ctrlDown = Screen.hasControlDown();
        if (shiftDown != shiftPressed || ctrlDown != ctrlPressed) {
            ctrlPressed = ctrlDown;
            shiftPressed = shiftDown;
            onKeyModifierChanged();
        }
        if (isHovered()){
            if (!hoverFlag){
                hoverFlag = true;
                refreshTooltip();
                onEnter();
            }
        }
        else {
            if (hoverFlag){
                hoverFlag = false;
                onExit();
            }
        }
    }

}
