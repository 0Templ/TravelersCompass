package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.component.element.NewPopupElement;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;

import java.util.ArrayList;
import java.util.List;

public class PopupButton extends BaseButton {

    protected final TextureData popupConnector;
    protected final TextureData popup;

    protected final Rect2i mainRect;
    protected final Rect2i popupRect;

    private boolean popupVisible = false;

    protected List<NewPopupElement> popupElements = new ArrayList<>();

    protected PopupButton(int x, int y, int width, int height,
                          TextureData popupConnector,
                          TextureData popupTexture,
                          Rect2i mainRect,
                          Rect2i popupRect) {
        super(x, y, width, height);
        this.mainRect = mainRect;
        this.popupRect = popupRect;
        this.popupConnector = popupConnector;
        this.popup = popupTexture;
    }

    protected int getPopupXOffset() {
        return 0;
    }

    protected int getPopupYOffset() {
        return 0;
    }

    protected int getConnectorXOffset() {
        return 0;
    }

    protected int getConnectorYOffset() {
        return 0;
    }

    protected boolean isPopupVisible(){
        return popupVisible && isEnabled();
    }

    protected boolean isEnabled(){
        return true;
    }

    private void updateRectsOffset() {
        this.mainRect.setPosition(getX(), getY());
        this.popupRect.setPosition(getX() + getPopupXOffset(), getY() + getPopupYOffset());
    }

    public void onPopupExit(){}

    public void onPopupEnter(){}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        if (isEnabled()) {
            if (popupRect.contains(mX, mY) && isPopupVisible()) {
                for (NewPopupElement popupElement : popupElements) {
                    popupElement.handleClick(mX, mY, button);
                }
            } else if (mainRect.contains(mX, mY)) {
                onMainButtonClick(button);
            }
            afterClick(button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        if (isEnabled()) {
            if (popupRect.contains(mX, mY) && isPopupVisible()) {
                for (NewPopupElement popupElement : popupElements) {
                    popupElement.handleMouseScroll(mX, mY, delta);
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    protected void onMainButtonClick(int button){
    }

    protected void afterClick(int button){
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        updateRectsOffset();
        if (mainRect.contains(mouseX, mouseY)){
            if (!popupVisible){
                onPopupEnter();
            }
            popupVisible = true;
        }
        else if (popupVisible && !popupRect.contains(mouseX, mouseY)){
            onPopupExit();
            popupVisible = false;
        }
        renderIcon(graphics, mouseX, mouseY, partialTicks);
        if (isPopupVisible()){
            renderPopup(graphics, mouseX, mouseY, partialTicks);
            for (NewPopupElement popupElement : popupElements){
                popupElement.render(graphics, mouseX, mouseY,
                        getX() + popupElement.getXOffset(),
                        getY() + popupElement.getYOffset());
            }
        }
    }

    protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    protected void renderPopup(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(popupConnector.getLocation(),
                this.getX() + getConnectorXOffset(),
                this.getY() + getConnectorYOffset(),
                0, 0,
                popupConnector.getRenderWidth(), popupConnector.getTextureHeight(), popupConnector.getRenderWidth(), popupConnector.getTextureHeight()
        );
        graphics.blit(popup.getLocation(),
                this.getX() + getPopupXOffset(),
                this.getY() + getPopupYOffset(),
                0, 0,
                popup.getRenderWidth(), popup.getTextureHeight(), popup.getRenderWidth(), popup.getTextureHeight()
        );
    }
}
