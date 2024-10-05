package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;


public class TabButton extends Button {

    private final int type;

    private static final ResourceLocation TEXTURE = new ResourceLocation(TravelersCompass.MODID, "textures/gui/component/gui_components.png");

    public TabButton(int x, int y, int width, int height, int type, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.type = type;
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float pPartialTick) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            int j = 112;
            int k;
            int d = this.type;
            k = (d == 0) ? 0 : 52;
            ItemStack stack = player.getMainHandItem();
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                List<Integer> modes = compassStack.selectedModes(stack);
                List<Integer> elementsToExclude = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
                modes.retainAll(elementsToExclude);
                boolean nothingSelected = modes.isEmpty();
                if (compassStack.configMode(stack) && d != 1) {
                    k += 26;
                }
                if (!compassStack.configMode(stack) && d == 1) {
                    k += 26;
                }
                if (d == 3) {
                    Component statusToolTip = Component.empty();
                    if (compassStack.positionRelativeToTarget(stack) == 5) {
                        statusToolTip = Component.translatable("options.travelerscompass.tooltip.not_enough_exp").append(Component.translatable("options.travelerscompass.tooltip.not_enough_exp_more").withStyle(ChatFormatting.GRAY));
                    } else if (compassContainer.isEmpty()) {
                        statusToolTip = Component.translatable("options.travelerscompass.tooltip.empty").append(Component.translatable("options.travelerscompass.tooltip.empty_more").withStyle(ChatFormatting.GRAY));
                    } else if (nothingSelected) {
                        statusToolTip = Component.translatable("options.travelerscompass.tooltip.not_selected_1").append(Component.translatable("options.travelerscompass.tooltip.not_selected_2").withStyle(ChatFormatting.GRAY));
                    } else if (compassStack.isPaused(stack)) {
                        statusToolTip = Component.translatable("options.travelerscompass.tooltip.paused_1").append(Component.translatable("options.travelerscompass.tooltip.paused_2").withStyle(ChatFormatting.GRAY));
                    } else if (TravelersCompassItem.getFoundPosition(stack) != null && TCConfig.xpDrain.get() && !player.getAbilities().instabuild) {
                        statusToolTip = Component.translatable("options.travelerscompass.tooltip.enough_exp").append(Component.translatable("options.travelerscompass.tooltip.enough_exp_more", TCConfig.xpCost.get(), TCConfig.xpDrainRate.get() / 20).withStyle(ChatFormatting.GRAY));
                    }
                    Rect2i area1 = new Rect2i(this.getX(), this.getY(), 15, 11);
                    Rect2i area2 = new Rect2i(this.getX(), this.getY() + 10, 17, 11);
                    if (area1.contains(mouseX, mouseY) || area2.contains(mouseX, mouseY)) {
                        this.setTooltip(Tooltip.create((statusToolTip)));
                    } else {
                        this.setTooltip(Tooltip.create(Component.literal("")));
                    }
                    j = 136;
                    if (compassStack.positionRelativeToTarget(stack) == 5 || compassStack.isPaused(stack) || compassContainer.isEmpty() || nothingSelected || (TravelersCompassItem.getFoundPosition(stack) != null && TCConfig.xpDrain.get() && !player.getAbilities().instabuild)) {
                        k = 0;
                    } else {
                        return;
                    }
                }
            }
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), k, j, this.width, this.height);
        }
    }
}