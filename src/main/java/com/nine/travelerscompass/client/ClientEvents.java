package com.nine.travelerscompass.client;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = TravelersCompass.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public void onRenderTick(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        if (shouldShow() && player != null) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            Font font = Minecraft.getInstance().font;
            ItemStack stack = player.getMainHandItem();
            ItemStack inventoryStack = null;
            if (stack.getItem() instanceof TravelersCompassItem compassStack && compassStack.hudModeRequiresHeld(stack) && (mc.screen == null || (compassStack.hudWithChatMode(stack) && mc.screen instanceof ChatScreen))) {
                renderHud(guiGraphics, player, font, stack, compassStack);
            } else {
                for (ItemStack item : player.getInventory().items) {
                    if (item.getItem() instanceof TravelersCompassItem) {
                        inventoryStack = item;
                        break;
                    }
                }
                if (inventoryStack != null && inventoryStack.getItem() instanceof TravelersCompassItem compassStack && compassStack.hudMode(inventoryStack)
                        && (mc.screen == null || (compassStack.hudWithChatMode(inventoryStack) && mc.screen instanceof ChatScreen))) {
                    renderHud(guiGraphics, player, font, inventoryStack, compassStack);
                }
            }
        }
    }

    public static int DEFAULT_X_OFFSET = 5;
    public static int DEFAULT_Y_OFFSET = 5;

    public static int getXOffset(int preset, Component s, int xOffset, Font font) {
        Minecraft mc = Minecraft.getInstance();
        return switch (preset) {
            case (0) -> xOffset;
            case (1) -> mc.getWindow().getGuiScaledWidth() - xOffset - font.width(s);
            case (2) -> mc.getWindow().getGuiScaledWidth() / 2 - xOffset - font.width(s) / 2;
            default -> 0;
        };
    }

    public static void renderHud(GuiGraphics guiGraphics, Player player, Font font, ItemStack stack, TravelersCompassItem compassItem) {
        BlockPos targetPos = TravelersCompassItem.getFoundPosition(stack);
        BlockPos userPos = player.getOnPos();
        String targetName = compassItem.foundTarget(stack);
        int preset = compassItem.getHudAlign(stack);
        int xOffset = compassItem.getXHudPos(stack) + DEFAULT_X_OFFSET;
        int yOffset = compassItem.getYHudPos(stack) + DEFAULT_Y_OFFSET;

        Component compact_state = Component.translatable("options.travelerscompass.hud.compact_state");
        Component state = Component.translatable("options.travelerscompass.hud.state");
        Component empty = Component.translatable("options.travelerscompass.hud.empty").withStyle(ChatFormatting.GRAY);
        Component not_selected = Component.translatable("options.travelerscompass.hud.not_selected").withStyle(ChatFormatting.GRAY);
        Component paused = Component.translatable("options.travelerscompass.hud.paused").withStyle(ChatFormatting.GRAY);
        Component not_enough_exp = Component.translatable("options.travelerscompass.hud.not_enough_exp").withStyle(ChatFormatting.GRAY);
        Component found = Component.translatable("options.travelerscompass.hud.found").withStyle(ChatFormatting.GRAY);
        Component searching = Component.translatable("options.travelerscompass.hud.searching").withStyle(ChatFormatting.GRAY);
        Component target = Component.translatable("options.travelerscompass.hud.target");
        Component last_target = Component.translatable("options.travelerscompass.hud.last_target");
        Component position = Component.translatable("options.travelerscompass.hud.position");
        Component distance = Component.translatable("options.travelerscompass.hud.distance");

        List<Integer> modes = compassItem.selectedModes(stack);
        List<Integer> elementsToExclude = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        modes.retainAll(elementsToExclude);
        boolean nothingSelected = modes.isEmpty();
        int type = compassItem.getHudType(stack);
        if (type == 1) {
            guiGraphics.drawString(font, state, getXOffset(preset, state, xOffset, font), yOffset, 0xFFFFFF);
            if (TCConfig.xpDrain.get() && !TravelersCompassItem.canSearch(player)) {
                guiGraphics.drawString(font, not_enough_exp, getXOffset(preset, empty, xOffset, font), yOffset + 10, 0xAAAAAA);
            } else if (TravelersCompassItem.getFoundPosition(stack) != null) {
                guiGraphics.drawString(font, found, getXOffset(preset, found, xOffset, font), yOffset + 10, 0xAAAAAA);
            } else if (CompassContainer.container(stack).isEmpty()) {
                guiGraphics.drawString(font, empty, getXOffset(preset, empty, xOffset, font), yOffset + 10, 0xAAAAAA);
            } else if (nothingSelected) {
                guiGraphics.drawString(font, not_selected, getXOffset(preset, not_selected, xOffset, font), yOffset + 10, 0xAAAAAA);
            } else if (compassItem.isPaused(stack)) {
                guiGraphics.drawString(font, paused, getXOffset(preset, paused, xOffset, font), yOffset + 10, 0xAAAAAA);
            } else if (TravelersCompassItem.getFoundPosition(stack) == null) {
                guiGraphics.drawString(font, searching, getXOffset(preset, searching, xOffset, font), yOffset + 10, 0xAAAAAA);
            }
            if (targetPos != null) {
                guiGraphics.drawString(font, target, getXOffset(preset, target, xOffset, font), yOffset + 25, 0xFFFFFF);
                guiGraphics.drawString(font, targetName, getXOffset(preset, Component.literal(targetName), xOffset, font), yOffset + 35, compassItem.hasFavoriteItem(stack) ? 0xd4b16a : 0xAAAAAA);
                String cords = targetPos.getX() +
                        ", " + targetPos.getY() +
                        ", " + targetPos.getZ();
                guiGraphics.drawString(font, position, getXOffset(preset, position, xOffset, font), yOffset + 50, 0xFFFFFF);
                guiGraphics.drawString(font, cords, getXOffset(preset, Component.literal(cords), xOffset, font), yOffset + 60, 0xAAAAAA);

                guiGraphics.drawString(font, distance, getXOffset(preset, distance, xOffset, font), yOffset + 75, 0xFFFFFF);
                guiGraphics.drawString(font, String.valueOf((int) Math.sqrt(userPos.distSqr(targetPos))), getXOffset(preset, Component.literal(String.valueOf((int) Math.sqrt(userPos.distSqr(targetPos)))), xOffset, font), yOffset + 85, 0xAAAAAA);
            }
        } else if (type == 0) {
            xOffset -= 2;
            yOffset -= 2;
            Component state1 = Component.literal("").withStyle(ChatFormatting.GRAY);
            boolean lackOfExp = TCConfig.xpDrain.get() && !TravelersCompassItem.canSearch(player);
            if (lackOfExp) {
                state1 = not_enough_exp;
            } else if (TravelersCompassItem.getFoundPosition(stack) != null) {
                state1 = found;
            } else if (CompassContainer.container(stack).isEmpty()) {
                state1 = empty;
            } else if (nothingSelected) {
                state1 = not_selected;
            } else if (compassItem.isPaused(stack)) {
                state1 = paused;
            } else if (TravelersCompassItem.getFoundPosition(stack) == null) {
                state1 = searching;
            }
            Component state0 = Component.literal(compact_state.getString() + ": ").append(state1);
            guiGraphics.drawString(font, state0, getXOffset(preset, state0, xOffset, font), yOffset, 0xFFFFFF);
            if (targetPos != null) {
                String cords = targetPos.getX() +
                        ", " + targetPos.getY() +
                        ", " + targetPos.getZ();
                MutableComponent coloredTargetName = Component.literal(targetName).withStyle(Component.literal(targetName).getStyle().withColor(compassItem.hasFavoriteItem(stack) ? 0xd4b16a : 11184810));
                Component target0 = lackOfExp ? Component.literal(last_target.getString() + ": ").append(coloredTargetName) : Component.literal(target.getString() + ": ").append(coloredTargetName);
                guiGraphics.drawString(font, target0, getXOffset(preset, target0, xOffset, font), yOffset + 10, 0xFFFFFF);

                Component position0 = Component.literal(position.getString() + ": ").append(Component.literal(cords).withStyle(ChatFormatting.GRAY));
                guiGraphics.drawString(font, position0, getXOffset(preset, position0, xOffset, font), yOffset + 20, 0xFFFFFF);

                Component distance0 = Component.literal(distance.getString() + ": ").append(Component.literal(String.valueOf((int) Math.sqrt(userPos.distSqr(targetPos)))).withStyle(ChatFormatting.GRAY));
                guiGraphics.drawString(font, distance0, getXOffset(preset, distance0, xOffset, font), yOffset + 30, 0xFFFFFF);

            }
        }

    }

    private boolean shouldShow() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null
                && !mc.options.hideGui
                && !mc.options.renderDebug
                /* && TCConfig.enableHUDClient.get()*/;
    }

}
