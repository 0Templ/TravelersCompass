package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.utils.*;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.network.packet.c2s.PausePacket;
import com.nine.travelerscompass.network.packet.c2s.WideSearchPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class WideSearchButton extends BaseIconButton {
	
	public WideSearchButton(ButtonGenericSettings settings) {
		super(settings);
		refreshTooltip();
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		if (CompassProperties.SEARCH_STATE.get(stack()) == SearchState.WIDE_SEARCHING) {
			if (shiftPressed) {
				ClientCache.PROGRESS_DATA_CACHE.put(uuid, new SearchProgress(0, 0));
				ClientCache.PROGRESS_DATA_CACHE.remove(uuid);
				Platform.PLATFORM_NETWORK.sendToServer(new PausePacket(uuid));
				CompassProperties.SEARCH_STATE.set(stack(), SearchState.IDLE);
			}
		} else {
			Platform.PLATFORM_NETWORK.sendToServer(new WideSearchPacket(uuid));
		}
		return true;
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return CompassUI.ButtonTextures.TOGGLE_BUTTON.get(false, isHovered);
	}
	
	@Override
	protected Icon getIcon() {
		return isHovered ? CompassUI.CommonTextures.WIDE_SEARCH_HOVERED_ICON : CompassUI.CommonTextures.WIDE_SEARCH_ICON;
	}
	
	@Override
	public void tick() {
		refreshTooltip();
	}
	
	
	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.renderWidget(graphics, mouseX, mouseY, partialTicks);
		SearchProgress searchProgress = ClientCache.PROGRESS_DATA_CACHE.get(uuid);
		if (searchProgress != null) {
			if (CompassProperties.SEARCH_STATE.get(stack()) == SearchState.WIDE_SEARCHING) {
				float progress = (float) searchProgress.progress / searchProgress.total;
				drawProgress(graphics, this.getX() + 1, this.getY() + 12, 13, 1, progress,
						isHovered ? 0xFFFFFFFF : 0xA2FFFFFF);
			}
		}
	}
	
	private void drawProgress(GuiGraphics g, int x, int y, int w, int h, float p, int fg) {
		float wf = p * w;
		int full = Mth.floor(wf);
		if (full > 0) {
			g.fill(x, y, x + full, y + h, fg);
		}
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.wide_search";
		builder.title(key);
		builder.descIf(key, shiftPressed);
		if (CompassProperties.SEARCH_STATE.get(stack()) == SearchState.WIDE_SEARCHING) {
			SearchProgress searchProgress = ClientCache.PROGRESS_DATA_CACHE.get(uuid);
			int percent = 0;
			if (searchProgress != null) {
				percent = (searchProgress.progress * 100) / searchProgress.total;
			}
			Component progress = ClientUtils.coloredComponent(Component.literal(percent + "%"), CompassUI.Colors.SOFT_GRAY);
			Component shiftRBM = ClientUtils.coloredComponent(
					Component.translatable("tooltip.travelerscompass.settings.modification.shift_click"), CompassUI.Colors.SOFT_GRAY);
			
			builder.line(Component.translatable("tooltip.travelerscompass.settings.info.status.progress", progress).withStyle(ChatFormatting.GRAY));
			builder.line(Component.translatable("tooltip.travelerscompass.wide_search.cancel", shiftRBM).withStyle(ChatFormatting.GRAY));
		}
		
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
