package com.nine.travelerscompass.client.component.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewPopupElement {

    private final Rect2i area;

    protected boolean shiftPressed;
    protected boolean ctrlPressed;
    protected boolean isHovered;
    protected boolean active = true;

    private int xOffSet;
    private int yOffSet;

    private final List<Component> tooltip = new ArrayList<>();

    public NewPopupElement(int width, int height) {
        this.area = new Rect2i(0, 0, width, height);

    }

    public void updateState() {
    }

    private void updateAreaPos(int xPos, int yPos){
        area.setPosition(xPos, yPos);
    }

    public boolean hoveredAt(int mouseX, int mouseY){
        return area.contains(mouseX, mouseY);
    }

    protected void setTooltip(List<Component> components){
        tooltip.clear();
        tooltip.addAll(components);
    }

    public void init(int x, int y){
        this.xOffSet = x;
        this.yOffSet = y;
        refreshTooltip();
    }

    public int getXOffset(){
        return xOffSet;
    }

    public int getYOffset(){
        return yOffSet;
    }

    public void refreshTooltip(){
    }

    public void onEnter(){
    }

    public void onExit(){
    }

    protected void onModifierChange() {
    }

    protected void renderElement(GuiGraphics graphics, int xPos, int yPos){
    }

    public void handleClick(int mouseX, int mouseY, int button){
        if (active) {
            if (hoveredAt(mouseX, mouseY)){
                {
                    onClick(button);
                }
                refreshTooltip();
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            }
        }
    }

    public void handleMouseScroll(int mouseX, int mouseY, double delta){
        if (active) {
            if (hoveredAt(mouseX, mouseY)) {
                {
                    onMouseScroll(delta);
                }
                refreshTooltip();
                Minecraft.getInstance().getSoundManager()
                        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            }
        }
    }


    public void onClick(int button){

    }

    public void onMouseScroll(double delta){

    }


    public void render(GuiGraphics graphics, int mouseX, int mouseY, int xPos, int yPos){
        updateAreaPos(xPos, yPos);
        boolean shiftDown = Screen.hasShiftDown();
        boolean ctrlDown = Screen.hasControlDown();
        if (shiftDown != shiftPressed || ctrlDown != ctrlPressed) {
            shiftPressed = shiftDown;
            ctrlPressed = ctrlDown;
            refreshTooltip();
            onModifierChange();
        }
        if (hoveredAt(mouseX, mouseY)){
            if (!isHovered){
                isHovered = true;
                onEnter();
            }
            isHovered = true;
            graphics.renderTooltip(Minecraft.getInstance().font,
                    tooltip, Optional.empty(), mouseX, mouseY);
        }
        else {
            if (isHovered){
                isHovered = false;
                onExit();
            }
            isHovered = false;
        }
        renderElement(graphics, xPos, yPos);
    }


}
