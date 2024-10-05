package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.client.ClientEvents;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.HUDButtonPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HUDButton extends BaseButton {

    public HUDButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public boolean mouseClicked(double xMousePos, double yMousePos, int p_93643_) {
        if (!(post || initial)) {
            return false;
        }
        return super.mouseClicked(xMousePos, yMousePos, p_93643_);
    }

    @Override
    public void onClick(double xMousePos, double yMousePos) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !TCConfig.enableHud.get()) {
            return;
        }
        int mouseX = (int) xMousePos;
        int mouseY = (int) yMousePos;
        Rect2i hudButton = new Rect2i(this.getX() + 35, this.getY(), 14, 14);
        Rect2i xButton = new Rect2i(this.getX() + 13, this.getY() + 2, 10, 10);
        Rect2i yButton = new Rect2i(this.getX() + 24, this.getY() + 2, 10, 10);
        Rect2i rButton = new Rect2i(this.getX() + 37, this.getY() + 15, 10, 10);
        Rect2i cButton = new Rect2i(this.getX() + 24, this.getY() + 15, 10, 10);
        Rect2i pButton = new Rect2i(this.getX() + 13, this.getY() + 15, 10, 10);
        Rect2i tButton = new Rect2i(this.getX() + 2, this.getY() + 2, 10, 10);

        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof TravelersCompassItem) {
            if (hudButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(1, controlPressed(), shiftPressed()));
            }
            if (xButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(2, controlPressed(), shiftPressed()));
            }
            if (yButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(3, controlPressed(), shiftPressed()));
            }
            if (cButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(4, controlPressed(), shiftPressed()));
            }
            if (rButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(5, controlPressed(), shiftPressed()));
            }
            if (pButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(6, controlPressed(), shiftPressed()));
            }
            if (tButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new HUDButtonPacket(7, controlPressed(), shiftPressed()));
            }
        }
    }


    public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float pPartialTick) {
        Player player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        if (player != null) {

            ItemStack stack = player.getMainHandItem();
            int xPosMain = 0;
            int yPosMain = 0;

            int xPosReset = 226;
            int yPosReset = 216;

            int xPosX = 236;
            int yPosX = 216;

            int xPosY = 246;
            int yPosY = 216;

            int xPosChat = 196;
            int yPosChat = 216;

            int xPosPreset;
            int yPosPreset = 216;

            int xPosType;
            int yPosType = 216;

            Rect2i hudButton = new Rect2i(this.getX() + 35, this.getY(), 14, 14);
            Rect2i xButton = new Rect2i(this.getX() + 13, this.getY() + 2, 10, 10);
            Rect2i yButton = new Rect2i(this.getX() + 24, this.getY() + 2, 10, 10);
            Rect2i rButton = new Rect2i(this.getX() + 37, this.getY() + 15, 10, 10);
            Rect2i cButton = new Rect2i(this.getX() + 24, this.getY() + 15, 10, 10);
            Rect2i pButton = new Rect2i(this.getX() + 13, this.getY() + 15, 10, 10);
            Rect2i tButton = new Rect2i(this.getX() + 2, this.getY() + 2, 10, 10);
            Rect2i allowArea = new Rect2i(this.getX() + 35, this.getY(), 15, 15);

            Rect2i area1 = new Rect2i(this.getX() + 11, this.getY(), 25, 14);
            Rect2i area2 = new Rect2i(this.getX() + 35, this.getY() + 14, 14, 13);
            Rect2i area3 = new Rect2i(this.getX() + 24, this.getY() + 14, 14, 13);
            Rect2i area4 = new Rect2i(this.getX() + 11, this.getY() + 14, 14, 13);
            Rect2i area5 = new Rect2i(this.getX() - 2, this.getY(), 14, 13);

            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                yPosMain = 56;
                xPosMain = 126;

                boolean inAllowedZone = area1.contains(mouseX, mouseY) || area2.contains(mouseX, mouseY) || area3.contains(mouseX, mouseY) || area4.contains(mouseX, mouseY) || area5.contains(mouseX, mouseY);
                this.initial = allowArea.contains(mouseX, mouseY);
                if (initial && (inAllowedZone)) {
                    post = true;
                } else if (!inAllowedZone) {
                    post = false;
                }
                yPosMain += hudButton.contains(mouseX, mouseY) ? 14 : 0;
                Component shiftToDecreaseTooTip = Component.translatable("options.travelerscompass.tooltip.offset.shift").withStyle(ChatFormatting.GRAY);
                Component ctrlTooTip = Component.translatable("options.travelerscompass.tooltip.offset.ctrl").withStyle(ChatFormatting.GRAY);
                Component resetTooTip = Component.translatable("options.travelerscompass.tooltip.reset.2").withStyle(ChatFormatting.GRAY);
                Component chatTooTip = Component.translatable("options.travelerscompass.tooltip.chat.2").withStyle(ChatFormatting.GRAY);
                Component alignToolTip = Component.translatable("options.travelerscompass.tooltip.align").withStyle(ChatFormatting.GRAY);
                Component typeToolTip = Component.translatable("options.travelerscompass.tooltip.type").withStyle(ChatFormatting.GRAY);
                Component hoodToolTip0 = Component.translatable("options.travelerscompass.hud.settings.0").withStyle(ChatFormatting.GRAY);
                Component hoodToolTip1 = Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);
                Component hoodToolTip2 = Component.translatable("options.travelerscompass.hud.settings.1").withStyle(ChatFormatting.GRAY);
                Component hoodToolTip3 = Component.translatable("options.travelerscompass.hud.settings.2").withStyle(ChatFormatting.GRAY);
                if (hudButton.contains(mouseX, mouseY)) {
                    String toolTip = Component.translatable("options.travelerscompass.hud.settings").getString();
                    this.setTooltip(Tooltip.create(Component.literal(toolTip).append(hoodToolTip0).append(
                            compassStack.hudMode(stack) ? hoodToolTip3 : (compassStack.hudModeRequiresHeld(stack) ? hoodToolTip2 : hoodToolTip1)
                    )));
                }
                if ((post || initial) && (compassStack.hudMode(stack) || compassStack.hudModeRequiresHeld(stack))) {
                    ClientEvents.renderHud(pGuiGraphics, player, Minecraft.getInstance().font, stack, compassStack);
                    int offsetAmount = (controlPressed() ? 5 : 1) * (shiftPressed() ? -1 : 1);
                    if (xButton.contains(mouseX, mouseY)) {
                        String toolTip = Component.translatable("options.travelerscompass.tooltip.offset.x").getString();
                        this.setTooltip(Tooltip.create(Component.literal(toolTip).append(shiftToDecreaseTooTip).append(ctrlTooTip)));
                        yPosX = controlPressed() ? 226 : 216;
                        xPosX = shiftPressed() ? 206 : 216;
                        if (compassStack.getXHudPos(stack) + offsetAmount < -ClientEvents.DEFAULT_X_OFFSET || compassStack.getXHudPos(stack) + offsetAmount > mc.getWindow().getGuiScaledWidth() + ClientEvents.DEFAULT_Y_OFFSET) {
                            yPosX += 20;
                        }
                    } else if (yButton.contains(mouseX, mouseY)) {
                        String toolTip = Component.translatable("options.travelerscompass.tooltip.offset.y").getString();
                        this.setTooltip(Tooltip.create(Component.literal(toolTip).append(shiftToDecreaseTooTip).append(ctrlTooTip)));
                        yPosY = controlPressed() ? 226 : 216;
                        xPosY = shiftPressed() ? 206 : 216;
                        if (compassStack.getYHudPos(stack) + offsetAmount < -ClientEvents.DEFAULT_Y_OFFSET || compassStack.getYHudPos(stack) + offsetAmount > mc.getWindow().getGuiScaledHeight() + ClientEvents.DEFAULT_Y_OFFSET) {
                            yPosY += 20;
                        }
                    } else if (rButton.contains(mouseX, mouseY)) {
                        String toolTip = Component.translatable("options.travelerscompass.tooltip.reset.1").getString();
                        this.setTooltip(Tooltip.create(Component.literal(toolTip).append(resetTooTip)));
                        yPosReset += 10;
                    } else if (cButton.contains(mouseX, mouseY)) {
                        String toolTip = Component.translatable("options.travelerscompass.tooltip.chat.1").getString();
                        this.setTooltip(Tooltip.create(Component.literal(toolTip).append(chatTooTip)));
                        yPosChat += 10;
                    } else if (pButton.contains(mouseX, mouseY)) {
                        String toolTip = compassStack.getHudAlign(stack) == 0 ?
                                Component.translatable("options.travelerscompass.tooltip.align.right").getString() :
                                (compassStack.getHudAlign(stack) == 1 ? Component.translatable("options.travelerscompass.tooltip.align.left").getString()
                                        : Component.translatable("options.travelerscompass.tooltip.align.center").getString());
                        this.setTooltip(Tooltip.create(Component.literal(toolTip).append(alignToolTip)));
                        yPosPreset += 10;
                    } else if (tButton.contains(mouseX, mouseY)) {
                        String toolTip = compassStack.getHudType(stack) == 0 ?
                                Component.translatable("options.travelerscompass.tooltip.type.compact").getString() :
                                (compassStack.getHudType(stack) == 1 ? Component.translatable("options.travelerscompass.tooltip.type.extended").getString()
                                        : Component.translatable("options.travelerscompass.tooltip.type.else").getString());
                        this.setTooltip(Tooltip.create(Component.literal(typeToolTip.getString()).append(Component.literal(toolTip).withStyle(ChatFormatting.GRAY))));
                        yPosType += 10;
                    } else if (!hudButton.contains(mouseX, mouseY)) {
                        String toolTip = Component.translatable("").getString();
                        this.setTooltip(Tooltip.create(Component.literal(toolTip)));
                    }
                    if (!compassStack.hudWithChatMode(stack)) {
                        xPosChat -= 10;
                    }
                    xPosPreset = switch (compassStack.getHudAlign(stack)) {
                        case 1 -> 166;
                        case 2 -> 156;
                        default -> 176;
                    };
                    xPosType = switch (compassStack.getHudType(stack)) {
                        case 1, 2 -> 146;
                        default -> 136;
                    };
                    pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, 176, 49, 27);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 13, this.getY() + 2, xPosX, yPosX, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 24, this.getY() + 2, xPosY, yPosY, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 37, this.getY() + 15, xPosReset, yPosReset, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 24, this.getY() + 15, xPosChat, yPosChat, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 13, this.getY() + 15, xPosPreset, yPosPreset, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 2, this.getY() + 2, xPosType, yPosType, 10, 10);
                } else if (!hudButton.contains(mouseX, mouseY)) {
                    String toolTip = Component.translatable("").getString();
                    this.setTooltip(Tooltip.create(Component.literal(toolTip)));
                }
                if (compassStack.hudModeRequiresHeld(stack)) {
                    xPosMain += 28;
                }
                if (compassStack.hudMode(stack)) {
                    xPosMain += 14;
                }
                if (hudButton.contains(mouseX, mouseY)) {
                    yPosMain += 14;
                }
            }
            if (!TCConfig.enableHud.get()) {
                xPosMain = 42;
                yPosMain = 56;
                if (hudButton.contains(mouseX, mouseY)) {
                    this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
                }
            }
            pGuiGraphics.blit(TEXTURE, this.getX() + 35, this.getY(), xPosMain, yPosMain, 14, 14);
        }
    }

}