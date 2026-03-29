package com.nine.travelerscompass.client.utils;

import com.nine.travelerscompass.client.ui.constant.TCColors;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.WithContent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ClientUtils {
	
	public static Component getDistance(BlockPos playerPos, BlockPos targetPos) {
		return Component.literal(String.valueOf((int) Math.sqrt(playerPos.distSqr(targetPos)))).withStyle(ChatFormatting.GRAY);
	}
	
	public static Component getLocationObjectTargetName(ILocationObject object) {
		boolean priority = object.priority();
		final int defaultColor = priority ? TCColors.HONEY : TCColors.GRAY;
		final int softColor = priority ? TCColors.SOFT_HONEY : TCColors.SOFT_GRAY;
		if (object instanceof WithContent withContent) {
			MutableComponent ret = Component.empty();
			ret.append(Component.translatable(object.descriptionId()).withColor(defaultColor));
			ret.append(Component.literal(" [").withColor(defaultColor));
			ret.append(Component.translatable(withContent.contentId()).withColor(softColor));
			ret.append(Component.literal("]").withColor(defaultColor));
			return ret;
		}
		return Component.translatable(object.descriptionId()).withColor(defaultColor);
	}
	
	public static Component cutComponent(Font font, Component component, int availableWidth) {
		MutableComponent ret = Component.empty();
		MutableComponent dots = Component.literal("...");
		int dotsWidth = font.width(dots);
		if (availableWidth <= font.width(dots)) {
			return dots;
		}
		int componentWidth = font.width(component);
		
		if (componentWidth <= availableWidth) {
			return component;
		}
		int widthUsed = 0;
		for (Component content : component.toFlatList()) {
			int contentWidth = font.width(content);
			if (widthUsed + contentWidth + dotsWidth <= availableWidth) {
				ret.append(content);
				widthUsed += contentWidth;
			} else {
				String s = content.getString();
				if (widthUsed + dotsWidth >= availableWidth) {
					ret.append(dots.withStyle(content.getStyle()));
				} else {
					Component formated = Component.literal(
									font.plainSubstrByWidth(s, availableWidth - widthUsed - dotsWidth))
							.withStyle(content.getStyle()).append(dots.withStyle(content.getStyle()));
					ret.append(formated);
				}
				break;
			}
		}
		
		return ret;
	}
	
	public static String compressNumber(int val) {
		int abs = Math.abs(val);
		if (abs >= 1_000_000) return (val / 1_000_000)
				+ Component.translatable("hud.travelerscompass.number.mega").getString();
		if (abs >= 1_000) return (val / 1_000)
				+ Component.translatable("hud.travelerscompass.number.kilo").getString();
		return String.valueOf(val);
	}
	
	public static void renderIcon(GuiGraphicsExtractor graphics, Icon icon, int x, int y) {
		for (var layer : icon.layers()) {
			renderIconLayer(graphics, layer, x, y);
		}
	}
	
	public static void renderIconLayer(GuiGraphicsExtractor graphics, IconLayer layer, int x, int y) {
		graphics.pose().pushMatrix();
		float scale = layer.scale();
		graphics.pose().translate((x + layer.xOffset()), (y + layer.yOffset()));
		graphics.pose().scale(scale);
		renderTexture(graphics, layer.textureData(), 0, 0);
		graphics.pose().popMatrix();
	}
	
	public static void renderTexture(GuiGraphicsExtractor graphics, TextureData data, int x, int y) {
		graphics.blit(
				RenderPipelines.GUI_TEXTURED,
				data.location(),
				x,
				y,
				data.u(),
				data.v(),
				data.width(),
				data.height(),
				data.width(),
				data.height(),
				data.sourceWidth(),
				data.sourceHeight()
		);
	}
	
	
}
