package com.nine.travelerscompass.client.hud;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.hud.anchor.HudAnchorX;
import com.nine.travelerscompass.client.hud.anchor.HudAnchorY;
import com.nine.travelerscompass.client.hud.type.IHudTypeRenderer;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.init.ItemRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HudRenderer {
	
	public static final Identifier EDGES_TEXTURE = Identifier.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/hud_edges.png");
	
	private static final int EDGE_PADDING = 7;
	private static final int HEIGHT_PADDING = 2;
	private static final int WIDTH_PADDING = 2;
	
	static final Minecraft minecraft = Minecraft.getInstance();
	
	private static int renderTicksCount = 0;
	
	public static void renderTick(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		renderTick(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false));
	}
	
	public static void renderTick(
			GuiGraphicsExtractor graphics,
			float partialTicks
	) {
		Player player = minecraft.player;
		if (player != null) {
			if (player.tickCount % 100 == 0) {
				Set<UUID> validUUIDs = new HashSet<>();
				for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
					if (stack.is(ItemRegistry.TRAVELERS_COMPASS.get())) {
						UUID uuid = CompassComponents.get(stack, CompassComponents.COMPASS_UUID);
						validUUIDs.add(uuid);
					}
				}
				ClientCache.HUD_DATA_CACHE.keySet().removeIf(uuid -> !validUUIDs.contains(uuid));
			}
			if (!shouldShow()) {
				return;
			}
			for (HudData hudData : ClientCache.HUD_DATA_CACHE.values()) {
				HudSettings settings = hudData.getSettings();
				if (settings.renderMode() == HudRenderMode.OFF) continue;
				if (settings.renderMode() == HudRenderMode.HAND_ONLY && !hudData.isSelected()) continue;
				if (minecraft.screen instanceof ChatScreen && !settings.hudWithChat()) continue;
				if (minecraft.screen == null || minecraft.screen instanceof ChatScreen) {
					renderHud(graphics, hudData, false, partialTicks);
				}
			}
		}
		if (player != null && renderTicksCount % 3000 == 0) {
			renderTicksCount = 0;
		}
	}
	
	public static void forceRender(GuiGraphicsExtractor graphics, float partialTicks, UUID uuid) {
		if (ClientCache.HUD_DATA_CACHE.containsKey(uuid)) {
			renderHud(graphics, ClientCache.HUD_DATA_CACHE.get(uuid), true, partialTicks);
		}
	}
	
	private static void renderHud(GuiGraphicsExtractor graphics, HudData hudData, boolean drawEdges, float partialTicks) {
		HudSettings settings = hudData.getSettings();
		IHudTypeRenderer renderer = settings.hudType().renderer;
		
		int scaledScreenWidth = minecraft.getWindow().getGuiScaledWidth();
		int scaledScreenHeight = minecraft.getWindow().getGuiScaledHeight();
		
		int x = Math.min(settings.xPos(), scaledScreenWidth);
		int y = Math.min(settings.yPos(), scaledScreenHeight);
		int width = Math.min(scaledScreenWidth, settings.width());
		int height = Math.min(scaledScreenHeight, settings.height());
		
		graphics.pose().pushMatrix();
		if (settings.anchorX() == HudAnchorX.RIGHT) {
			x = scaledScreenWidth - width - x;
			var off = (width) - (width * settings.scale());
			graphics.pose().translate(off, 0);
		}
		if (settings.anchorY() == HudAnchorY.BOTTOM) {
			y = scaledScreenHeight - height - y;
			var off = (height) - (height * settings.scale());
			graphics.pose().translate(0, off);
		}
		graphics.pose().translate((x), (y));
		graphics.pose().scale(settings.scale());
		
		renderer.render(graphics, minecraft.player, minecraft.font, hudData,
				WIDTH_PADDING, HEIGHT_PADDING,
				width - WIDTH_PADDING, height - HEIGHT_PADDING
		);
		
		if (drawEdges) {
			drawEdges(graphics,
					0, 0,
					width, height,
					partialTicks);
		}
		graphics.pose().popMatrix();
	}
	
	private static boolean shouldShow() {
		return TCConfig.ENABLE_HUD.get() && minecraft.level != null
				&& !minecraft.options.hideGui;
	}
	
	protected static void drawEdges(GuiGraphicsExtractor graphics,
									int xPos, int yPos,
									int width, int height,
									float partialTicks) {
		
		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		float pulse = 0.8F + 0.2F * Mth.sin((partialTicks + player.tickCount) * 0.1F);
		final int padding = EDGE_PADDING;
		int opacity = ARGB.white(pulse);
		graphics.blit(RenderPipelines.GUI_TEXTURED, EDGES_TEXTURE, xPos, yPos,
				0, 0,
				padding, padding, 14, 14, opacity
		);
		graphics.blit(RenderPipelines.GUI_TEXTURED, EDGES_TEXTURE, xPos + width - padding, yPos,
				7, 0,
				padding, padding, 14, 14, opacity
		);
		graphics.blit(RenderPipelines.GUI_TEXTURED, EDGES_TEXTURE, xPos, yPos + height - padding,
				0, 7,
				padding, padding, 14, 14, opacity
		);
		graphics.blit(RenderPipelines.GUI_TEXTURED, EDGES_TEXTURE, xPos + width - padding, yPos + height - padding,
				7, 7,
				padding, padding, 14, 14, opacity
		);
	}
	
}
