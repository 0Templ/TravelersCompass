package com.nine.travelerscompass.client.ui.constant;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

public final class TCComponents {

	private static final FontDescription SPACE_FONT = new FontDescription.Resource(
			Identifier.fromNamespaceAndPath(TCCommon.MODID, "space_helper"));

	public static final Component SHORT_INDENT = Component.literal("\uF820")
			.withStyle(Style.EMPTY.withFont(SPACE_FONT));

	public static final Component DESC_ARROW = Component.literal("▶").append(SHORT_INDENT)
			.withColor(TCColors.SOFT_GRAY);

	public static final Component CONFIG_DISABLED = Component.translatable("tooltip.travelerscompass.disabled_config")
			.withStyle(ChatFormatting.GRAY);
	public static final Component DISABLED = Component.translatable("tooltip.travelerscompass.disabled")
			.withStyle(ChatFormatting.GRAY);
	public static final Component ENABLED = Component.translatable("tooltip.travelerscompass.enabled")
			.withStyle(ChatFormatting.GRAY);
	public static final Component INVERTED = Component.translatable("tooltip.travelerscompass.inverted")
			.withStyle(ChatFormatting.GRAY);
	public static final Component INACTIVE = Component.translatable("tooltip.travelerscompass.inactive")
			.withStyle(ChatFormatting.GRAY);
	public static final Component HUD_ENABLED = Component.translatable("tooltip.travelerscompass.settings.hud.enabled")
			.withStyle(ChatFormatting.GRAY);
	public static final Component HUD_REQUIRES_HAND = Component.translatable("tooltip.travelerscompass.settings.hud.requires_hand")
			.withStyle(ChatFormatting.GRAY);

	public static final Component RBM = Component.translatable("tooltip.travelerscompass.settings.modification.rbm")
			.withColor(TCColors.SOFT_GRAY);

	public static final Component FILTER_BY_ITEM_ID = Component.translatable("tooltip.travelerscompass.config.filter_reason.item")
			.withStyle(ChatFormatting.RED);

	private TCComponents() {}

}
