package com.nine.travelerscompass.client.hud;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.hud.type.IHudTypeRenderer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.utils.HudRenderMode;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HudRenderer {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/gui_components.png");
    public static final ResourceLocation EDGES_TEXTURE = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/hud_edges.png");

    private static final int EDGE_PADDING = 7;
    private static final int HEIGHT_PADDING = 2;
    private static final int WIDTH_PADDING = 2;

    static final Minecraft minecraft = Minecraft.getInstance();

    private static int renderTicksCount = 0;

    public static void renderTick(GuiGraphics guiGraphics, DeltaTracker deltaTracker){
        renderTick(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false));
    }

    public static void renderTick(
            GuiGraphics graphics,
            float partialTicks
    ) {
        Player player = minecraft.player;
        if (player != null) {
            if (player.tickCount % 100 == 0) {
                Set<UUID> validUUIDs = new HashSet<>();
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(ItemRegistry.TRAVELERS_COMPASS.get())) {
                        UUID uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
                        validUUIDs.add(uuid);
                    }
                }
                ClientData.HUD_DATA_CACHE.keySet().removeIf(uuid -> !validUUIDs.contains(uuid));
            }
            if (!shouldShow()) {
                return;
            }
            Font font = minecraft.font;
            for (HudData hudData : ClientData.HUD_DATA_CACHE.values()) {
                HudSettings settings = hudData.getSettings();
                if (settings.renderMode() == HudRenderMode.OFF) continue;
                if (settings.renderMode() == HudRenderMode.HAND_ONLY && !hudData.isSelected()) continue;
                if (minecraft.screen instanceof ChatScreen && !settings.hudWithChat()) continue;
                if (minecraft.screen == null || minecraft.screen instanceof ChatScreen) {
                    IHudTypeRenderer renderer = settings.hudType().renderer;
                    graphics.pose().pushPose();
                    graphics.pose().scale(settings.scale(), settings.scale(), 1.0F);
                    renderer.render(graphics, player, font, hudData);
                    graphics.pose().popPose();
                }
            }
        }
        if (player != null && renderTicksCount % 3000 == 0) {
            renderTicksCount = 0;
        }
    }

    public static void forceRender(GuiGraphics graphics, float partialTicks, UUID uuid) {
        if (ClientData.HUD_DATA_CACHE.containsKey(uuid)) {
            HudData hudData = ClientData.HUD_DATA_CACHE.get(uuid);
            HudSettings settings = hudData.getSettings();
            IHudTypeRenderer renderer = settings.hudType().renderer;
            graphics.pose().pushPose();
            graphics.pose().scale(settings.scale(), settings.scale(), 1.0F);
            renderer.render(graphics, minecraft.player, minecraft.font, hudData);
            drawEdges(graphics, settings.xPos(), settings.yPos(), settings.width(), settings.height(), partialTicks);
            graphics.pose().popPose();
        }
    }

    private static boolean shouldShow() {
        return TCConfig.ENABLE_HUD.get() && minecraft.level != null
                && !minecraft.options.hideGui;
    }

    protected static void drawEdges(GuiGraphics graphics, int xPos, int yPos, int width, int height, float partialTicks) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        float pulse = 0.8F + 0.2F * Mth.sin((partialTicks + player.tickCount) * 0.1F);
        final int padding = EDGE_PADDING;
        graphics.setColor(1, 1, 1, pulse);
        graphics.blit(EDGES_TEXTURE, xPos, yPos,
                0, 0,
                padding, padding, 14, 14
        );
        graphics.blit(EDGES_TEXTURE, xPos + width - padding + WIDTH_PADDING * 2, yPos,
                7, 0,
                padding, padding, 14, 14
        );
        graphics.blit(EDGES_TEXTURE, xPos, yPos + height - padding + HEIGHT_PADDING,
                0, 7,
                padding, padding, 14, 14
        );
        graphics.blit(EDGES_TEXTURE, xPos + width - padding + WIDTH_PADDING * 2, yPos + height - padding + HEIGHT_PADDING,
                7, 7,
                padding, padding, 14, 14
        );
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
