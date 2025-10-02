package com.nine.travelerscompass.client.component.button;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface Toggleable {

    Component CONFIG_DISABLED = Component.translatable("tooltip.travelerscompass.disabled_config").withStyle(ChatFormatting.GRAY);
    Component DISABLED = Component.translatable("tooltip.travelerscompass.disabled").withStyle(ChatFormatting.GRAY);
    Component ENABLED = Component.translatable("tooltip.travelerscompass.enabled").withStyle(ChatFormatting.GRAY);
    Component INVERTED = Component.translatable("tooltip.travelerscompass.inverted").withStyle(ChatFormatting.GRAY);
    Component INACTIVE = Component.translatable("tooltip.travelerscompass.inactive").withStyle(ChatFormatting.GRAY);

    void setToggled(boolean toggled);

    boolean isToggled();

}
