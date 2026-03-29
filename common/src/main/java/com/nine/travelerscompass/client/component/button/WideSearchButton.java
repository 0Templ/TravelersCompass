package com.nine.travelerscompass.client.component.button;

import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.component.button.base.BaseIconButton;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.ui.constant.TCColors;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.client.utils.TooltipBuilder;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.network.packet.c2s.PausePacket;
import com.nine.travelerscompass.network.packet.c2s.WideSearchPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class WideSearchButton extends BaseIconButton {

	private static final float PROGRESS_SMOOTHING = 0.35F;

	private float displayedProgress = 0.0F;
	
	public WideSearchButton(ButtonGenericSettings settings) {
		super(settings);
		refreshTooltip();
	}
	
	@Override
	public boolean onLeftClick(double mouseX, double mouseY) {
		if (CompassComponents.SEARCH_STATE.get(stack()) == SearchState.WIDE_SEARCHING) {
			if (shiftPressed) {
				ClientCache.PROGRESS_DATA_CACHE.put(uuid, new SearchProgress(0, 0));
				ClientCache.PROGRESS_DATA_CACHE.remove(uuid);
				Platform.PLATFORM_NETWORK.sendToServer(new PausePacket(uuid));
				CompassComponents.SEARCH_STATE.set(stack(), SearchState.IDLE);
			}
		} else {
			Platform.PLATFORM_NETWORK.sendToServer(new WideSearchPacket(uuid));
		}
		return true;
	}
	
	@Override
	protected TextureData getMainLayerTexture() {
		return TCTextures.Buttons.TOGGLE_BUTTON.get(false, isHovered);
	}
	
	@Override
	protected Icon getIcon() {
		return isHovered ? TCIcons.Common.WIDE_SEARCH_HOVERED : TCIcons.Common.WIDE_SEARCH;
	}
	
	@Override
	public void tick() {
		var state = CompassComponents.SEARCH_STATE.get(stack());
		if (state == SearchState.WIDE_SEARCHING) {
			SearchProgress searchProgress = ClientCache.PROGRESS_DATA_CACHE.get(uuid);
			float targetProgress = searchProgress == null ? 0.0F : searchProgress.fraction();
			if (targetProgress < displayedProgress) {
				displayedProgress = targetProgress;
			} else {
				displayedProgress = Mth.lerp(PROGRESS_SMOOTHING, displayedProgress, targetProgress);
				if ((targetProgress - displayedProgress) < 0.01F) {
					displayedProgress = targetProgress;
				}
			}
		} else {
			displayedProgress = 0.0F;
		}
		refreshTooltip();
	}
	
	
	@Override
	public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractContents(graphics, mouseX, mouseY, partialTicks);
		SearchProgress searchProgress = ClientCache.PROGRESS_DATA_CACHE.get(uuid);
		if (searchProgress != null && searchProgress.isValid()) {
			if (CompassComponents.SEARCH_STATE.get(stack()) == SearchState.WIDE_SEARCHING) {
				drawProgress(graphics, this.getX() + 1, this.getY() + 12, 13, 1, displayedProgress,
						isHovered ? 0xFFFFFFFF : 0xA2FFFFFF);
			}
		}
	}
	
	private void drawProgress(GuiGraphicsExtractor g, int x, int y, int w, int h, float p, int fg) {
		p = Mth.clamp(p, 0.0F, 1.0F);
		float wf = p * w;
		int full = Mth.floor(wf);
		float partial = wf - full;
		if (full > 0) {
			g.fill(x, y, x + full, y + h, fg);
		}
		if (partial > 0.0F && full < w) {
			int alpha = fg >>> 24;
			int partialAlpha = Mth.clamp(Mth.ceil(alpha * partial), 0, 255);
			if (partialAlpha > 0) {
				int partialColor = (fg & 0x00FFFFFF) | (partialAlpha << 24);
				g.fill(x + full, y, x + full + 1, y + h, partialColor);
			}
		}
	}
	
	@Override
	public void refreshTooltip() {
		var builder = TooltipBuilder.builder();
		String key = "tooltip.travelerscompass.wide_search";
		builder.title(key);
		builder.descIf(key, shiftPressed);
		if (CompassComponents.SEARCH_STATE.get(stack()) == SearchState.WIDE_SEARCHING) {
			SearchProgress searchProgress = ClientCache.PROGRESS_DATA_CACHE.get(uuid);
			int percent = 0;
			if (searchProgress != null) {
				percent = searchProgress.percent();
			}
			Component progress = Component.literal(percent + "%").withColor(TCColors.SOFT_GRAY);
			Component shiftRBM = Component.translatable("tooltip.travelerscompass.settings.modification.shift_click").withColor(TCColors.SOFT_GRAY);
			
			builder.line(Component.translatable("tooltip.travelerscompass.settings.info.status.progress", progress).withStyle(ChatFormatting.GRAY));
			builder.line(Component.translatable("tooltip.travelerscompass.wide_search.cancel", shiftRBM).withStyle(ChatFormatting.GRAY));
		}
		
		this.setTooltip(builder.buildAsTooltip());
	}
	
}
