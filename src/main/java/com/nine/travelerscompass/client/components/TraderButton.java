package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.ConfigButtonPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class TraderButton extends BaseButton {

    public TraderButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public boolean mouseClicked(double xMousePos, double yMousePos, int button) {
        if (!(post || initial) || !TCConfig.enableVillagersSearch.get()) {
            return false;
        }
        return super.mouseClicked(xMousePos, yMousePos, button);
    }

    @Override
    public void onClick(double xMousePos, double yMousePos) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        int mouseX = (int) xMousePos;
        int mouseY = (int) yMousePos;
        Rect2i mainButton = new Rect2i(this.getX(), this.getY() + 24, 14, 13);
        Rect2i area2 = new Rect2i(this.getX(), this.getY() + 2, 10, 10);
        Rect2i area3 = new Rect2i(this.getX(), this.getY() + 13, 10, 10);
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof TravelersCompassItem) {
            if (mainButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(4));
            }
            if (area3.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(401));
            }
            if (area2.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(402));
            }
        }
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float pPartialTick) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {

            ItemStack stack = player.getMainHandItem();
            int xPosMain = 56;
            int yPosMain = 0;
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                boolean isSearchingVillagers = compassStack.isSearchingVillagers(stack);

                if (compassStack.isSearchingVillagersGoods(stack) && compassStack.isSearchingVillagersCost(stack)) {
                    xPosMain = isSearchingVillagers ? 242 : 56;
                    yPosMain = isSearchingVillagers ? 56 : 0;
                } else if (isSearchingVillagers) {
                    yPosMain += 28;
                }
                Rect2i mainButton = new Rect2i(this.getX(), this.getY() + 24, 14, 13);
                Rect2i area1 = new Rect2i(this.getX(), this.getY(), 14, 38);
                Rect2i area2 = new Rect2i(this.getX(), this.getY() + 2, 10, 10);
                Rect2i area3 = new Rect2i(this.getX(), this.getY() + 13, 10, 10);
                int cPosX = 116;
                int cPosY = 216;

                int gPosX = 126;
                int gPosY = 216;
                boolean inAllowedZone = area1.contains(mouseX, mouseY);
                this.initial = mainButton.contains(mouseX, mouseY);
                if (initial && (inAllowedZone)) {
                    post = true;
                } else if (!inAllowedZone) {
                    post = false;
                }
                yPosMain += mainButton.contains(mouseX, mouseY) ? 14 : 0;
                Component toolTipMainState = compassStack.isSearchingVillagers(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);

                Component toolTipGoodsState = !compassStack.isSearchingVillagersGoods(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);
                Component toolTipGoodsMore = Component.translatable("options.travelerscompass.tooltip.villager_button.goods_info").withStyle(ChatFormatting.GRAY);

                Component toolTipCostState = !compassStack.isSearchingVillagersCost(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);
                Component toolTipCostMore = Component.translatable("options.travelerscompass.tooltip.villager_button.cost_info").withStyle(ChatFormatting.GRAY);
                if (compassStack.isSearchingVillagersCost(stack)) {
                    cPosY += 20;
                }
                if (compassStack.isSearchingVillagersGoods(stack)) {
                    gPosY += 20;
                }
                if (mainButton.contains(mouseX, mouseY)) {
                    String villagerTooltip = Component.translatable("options.travelerscompass.tooltip.villager_button").getString();
                    if (shiftPressed()) {
                        villagerTooltip += (Component.translatable("options.travelerscompass.tooltip.villagers.info").getString());
                    }
                    this.setTooltip(Tooltip.create(Component.literal(villagerTooltip).append(toolTipMainState)));
                    if (!TCConfig.enableVillagersSearch.get()) {
                        this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
                    }
                }
                if ((post || initial) && isSearchingVillagers) {
                    if (area3.contains(mouseX, mouseY)) {
                        if (!shiftPressed()) {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.villager_button.goods")
                                    .append(toolTipGoodsState)));
                        } else {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.villager_button.goods")
                                    .append(toolTipGoodsMore)
                                    .append(toolTipGoodsState)));
                        }
                        gPosY += 10;
                    }
                    if (area2.contains(mouseX, mouseY)) {
                        if (!shiftPressed()) {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.villager_button.cost")
                                    .append(toolTipCostState)));
                        } else {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.villager_button.cost")
                                    .append(toolTipCostMore)
                                    .append(toolTipCostState)));
                        }
                        cPosY += 10;
                    }

                    pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, 204, 14, 38);

                    pGuiGraphics.blit(TEXTURE, this.getX() + 2, this.getY() + 2, cPosX, cPosY, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 2, this.getY() + 13, gPosX, gPosY, 10, 10);
                } else if (!mainButton.contains(mouseX, mouseY)) {
                    String toolTip = Component.translatable("").getString();
                    this.setTooltip(Tooltip.create(Component.literal(toolTip)));
                }


            }
            if (!TCConfig.enableVillagersSearch.get()) {
                xPosMain = 42;
                yPosMain = 56;
            }
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY() + 24, xPosMain, yPosMain, 14, 14);
        }
    }


}