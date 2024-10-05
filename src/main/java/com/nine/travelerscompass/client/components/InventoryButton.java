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

public class InventoryButton extends BaseButton {

    public InventoryButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public boolean mouseClicked(double xMousePos, double yMousePos, int p_93643_) {
        if (!(post || initial) || !TCConfig.enableMobsInventorySearch.get()) {
            return false;
        }
        return super.mouseClicked(xMousePos, yMousePos, p_93643_);
    }

    @Override
    public void onClick(double xMousePos, double yMousePos) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        int mouseX = (int) xMousePos;
        int mouseY = (int) yMousePos;
        Rect2i mainButton = new Rect2i(this.getX() + 35, this.getY(), 13, 14);
        Rect2i playerArea = new Rect2i(this.getX() + 2, this.getY() + 2, 10, 10);
        Rect2i mobArea = new Rect2i(this.getX() + 13, this.getY() + 2, 10, 10);
        Rect2i minecartArea = new Rect2i(this.getX() + 24, this.getY() + 2, 10, 10);
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof TravelersCompassItem) {
            if (mainButton.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(6));
            }
            if (playerArea.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(601));
            }
            if (mobArea.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(602));
            }
            if (minecartArea.contains(mouseX, mouseY)) {
                NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(603));
            }
        }
    }


    public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float pPartialTick) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {

            ItemStack stack = player.getMainHandItem();
            int xPosMain = 84;
            int yPosMain = 0;
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                boolean isSearchingInvs = compassStack.isSearchingEntitiesInv(stack);

                if (compassStack.isSearchingMinecartsInv(stack) && compassStack.isSearchingPlayersInv(stack) && compassStack.isSearchingMobsInv(stack)) {
                    xPosMain = isSearchingInvs ? 242 : 84;
                    yPosMain = isSearchingInvs ? 28 : 0;
                } else if (isSearchingInvs) {
                    yPosMain += 28;
                }
                Rect2i allArea = new Rect2i(this.getX(), this.getY(), 49, 14);
                Rect2i mainButton = new Rect2i(this.getX() + 35, this.getY(), 13, 14);
                Rect2i playerArea = new Rect2i(this.getX() + 2, this.getY() + 2, 10, 10);
                Rect2i mobArea = new Rect2i(this.getX() + 13, this.getY() + 2, 10, 10);
                Rect2i minecartArea = new Rect2i(this.getX() + 24, this.getY() + 2, 10, 10);
                int mPosX = 106;
                int mPosY = 216;

                int pPosX = 96;
                int pPosY = 216;

                int mbPosX = 86;
                int mbPosY = 216;
                boolean inAllowedZone = allArea.contains(mouseX, mouseY);
                this.initial = mainButton.contains(mouseX, mouseY);
                if (initial && (inAllowedZone)) {
                    post = true;
                } else if (!inAllowedZone) {
                    post = false;
                }
                yPosMain += mainButton.contains(mouseX, mouseY) ? 14 : 0;
                Component toolTipMainState = compassStack.isSearchingEntitiesInv(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);

                Component toolTipMinecartsState = !compassStack.isSearchingMinecartsInv(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);
                Component toolTipMinecartsMore = Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.minecarts_info").withStyle(ChatFormatting.GRAY);

                Component toolTipPlayersState = !compassStack.isSearchingPlayersInv(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);
                Component toolTipPlayersMore = Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.players_info").withStyle(ChatFormatting.GRAY);

                Component toolTipMobsState = !compassStack.isSearchingMobsInv(stack) ?
                        Component.translatable("options.travelerscompass.tooltip.enabled").withStyle(ChatFormatting.GRAY) :
                        Component.translatable("options.travelerscompass.tooltip.disabled").withStyle(ChatFormatting.GRAY);
                Component toolTipMobsMore = Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.mobs_info").withStyle(ChatFormatting.GRAY);

                if (compassStack.isSearchingMobsInv(stack)) {
                    mbPosY += 20;
                }
                if (compassStack.isSearchingPlayersInv(stack)) {
                    pPosY += 20;
                }
                if (compassStack.isSearchingMinecartsInv(stack)) {
                    mPosY += 20;
                }
                if (mainButton.contains(mouseX, mouseY)) {
                    String invsTooltip = Component.translatable("options.travelerscompass.tooltip.mobs_inv_button").getString();
                    if (shiftPressed()) {
                        invsTooltip += (Component.translatable("options.travelerscompass.tooltip.mobs_inv.info").getString());
                    }
                    this.setTooltip(Tooltip.create(Component.literal(invsTooltip).append(toolTipMainState)));
                    if (!TCConfig.enableMobsInventorySearch.get()) {
                        this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
                    }
                }
                if ((post || initial) && isSearchingInvs) {
                    if (playerArea.contains(mouseX, mouseY)) {
                        if (!shiftPressed()) {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.players")
                                    .append(toolTipPlayersState)));
                        } else {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.players")
                                    .append(toolTipPlayersMore)
                                    .append(toolTipPlayersState)));
                        }
                        pPosY += 10;
                    }
                    if (mobArea.contains(mouseX, mouseY)) {
                        if (!shiftPressed()) {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.mobs")
                                    .append(toolTipMobsState)));
                        } else {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.mobs")
                                    .append(toolTipMobsMore)
                                    .append(toolTipMobsState)));
                        }
                        mbPosY += 10;
                    }
                    if (minecartArea.contains(mouseX, mouseY)) {
                        if (!shiftPressed()) {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.minecarts")
                                    .append(toolTipMinecartsState)));
                        } else {
                            this.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.mobs_inv_button.minecarts")
                                    .append(toolTipMinecartsMore)
                                    .append(toolTipMinecartsState)));
                        }
                        mPosY += 10;
                    }
                    pGuiGraphics.blit(TEXTURE, this.getX() + 2, this.getY() + 2, pPosX, pPosY, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 13, this.getY() + 2, mbPosX, mbPosY, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX() + 24, this.getY() + 2, mPosX, mPosY, 10, 10);
                    pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, 242, 49, 14);
                } else if (!mainButton.contains(mouseX, mouseY)) {
                    String toolTip = Component.translatable("").getString();
                    this.setTooltip(Tooltip.create(Component.literal(toolTip)));
                }
            }
            if (!TCConfig.enableMobsInventorySearch.get()) {
                xPosMain = 42;
                yPosMain = 56;
            }
            pGuiGraphics.blit(TEXTURE, this.getX() + 35, this.getY(), xPosMain, yPosMain, 14, 14);
        }
    }
}