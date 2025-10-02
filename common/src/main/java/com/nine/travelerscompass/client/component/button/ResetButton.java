package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.utils.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ResetButton extends BaseButton {

    public ResetButton(int x, int y, OnPress onPress) {
        super(x, y, 14, 14, onPress);
    }

    protected void renderMainLayer(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        ClientUtils.renderTexture(graphics, ClientData.TOGGLE_BUTTON.get(false, isHovered()), this.getX(), this.getY());
        renderIcon(graphics, mouseX, mouseY, partialTicks);
    }

    protected void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        ClientUtils.renderTexture(graphics, isHovered() ? ClientData.RESET_HOVERED : ClientData.RESET, this.getX() + 3, this.getY() + 3);
    }

    @Override
    public void refreshTooltip(){
        Component title = Component.translatable("tooltip.travelerscompass.settings.reset");
        List<Component> components = new ArrayList<>();
        components.add(title);
        if (shiftPressed){
            Component desc = ClientUtils.coloredComponent(Component.literal("▶"), ClientUtils.SOFT_GRAY)
                    .append((Component.translatable("tooltip.travelerscompass.settings.reset.desc").withStyle(ChatFormatting.GRAY)));
            components.add(desc);
        }
        this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(components)));
    }
}
