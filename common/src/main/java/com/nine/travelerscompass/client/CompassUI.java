package com.nine.travelerscompass.client;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.utils.ButtonTexturesSet;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;


public final class CompassUI {
	
	//COMPONENTS
	//Indent a bit shorten then a regular one
	//Locates in assets/travelerscompass/font
	private static final FontDescription NEW_FONT = new FontDescription.Resource(ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "space_helper"));
	
	public static final Component SHORT_INDENT = Component.literal("\uF820")
			.withStyle(Style.EMPTY.withFont(NEW_FONT));
	public static final Component DESC_ARROW = ClientUtils.coloredComponent(Component.literal("▶").append(SHORT_INDENT), Colors.SOFT_GRAY);
	
	public static final Component CONFIG_DISABLED = Component.translatable("tooltip.travelerscompass.disabled_config").withStyle(ChatFormatting.GRAY);
	public static final Component DISABLED = Component.translatable("tooltip.travelerscompass.disabled").withStyle(ChatFormatting.GRAY);
	public static final Component ENABLED = Component.translatable("tooltip.travelerscompass.enabled").withStyle(ChatFormatting.GRAY);
	public static final Component INVERTED = Component.translatable("tooltip.travelerscompass.inverted").withStyle(ChatFormatting.GRAY);
	public static final Component INACTIVE = Component.translatable("tooltip.travelerscompass.inactive").withStyle(ChatFormatting.GRAY);
	public static final Component HUD_ENABLED = Component.translatable("tooltip.travelerscompass.settings.hud.enabled").withStyle(ChatFormatting.GRAY);
	public static final Component HUD_REQUIRES_HAND = Component.translatable("tooltip.travelerscompass.settings.hud.requires_hand").withStyle(ChatFormatting.GRAY);
	
	public static final Component RBM = Component.translatable("tooltip.travelerscompass.settings.modification.rbm").withColor(Colors.SOFT_GRAY);
	
	public static final Component FILTER_BY_ITEM_ID = Component.translatable("tooltip.travelerscompass.config.filter_reason.item").withStyle(ChatFormatting.RED);
	
	//COLORS
	public static final class Colors {
		
		public static final int SOFT_GRAY = 0xc8c8c8;
		public static final int GRAY = 11184810;
		
		public static final int HONEY = 0xd4b16a;
		public static final int SOFT_HONEY = 0xf6d491;
		
		public static final int SOFT_GREEN = 0xaee67f;
		public static final int SOFT_RED = 0xb84949;
		
		public static final float MAX_SCALE = 3F;
		public static final float MIN_SCALE = 0.3F;
		
	}
	
	
	//TEXTURES
	
	public static final class CommonTextures {
		
		private static final TextureData ATLAS = new TextureData.Builder()
				.location("icon/common_icons").sourceSize(128, 128).build();
		
		//Texture Data
		public static final TextureData LOCK = ATLAS.toBuilder().uv(32, 0).size(8, 10).build();
		
		public static final TextureData SMALL_LOCK = ATLAS.toBuilder().uv(48, 0).size(6, 8).build();
		
		public static final TextureData INFO = ATLAS.toBuilder().uv(64, 0).size(6, 8).build();
		public static final TextureData INFO_HOVERED = ATLAS.toBuilder().uv(80, 0).size(6, 8).build();
		
		public static final TextureData PAUSE = ATLAS.toBuilder().uv(96, 0).size(9).build();
		public static final TextureData PAUSE_HOVERED = ATLAS.toBuilder().uv(112, 0).size(9).build();
		
		public static final TextureData RESUME = ATLAS.toBuilder().uv(96, 16).size(4, 8).build();
		public static final TextureData RESUME_HOVERED = ATLAS.toBuilder().uv(112, 16).size(4, 8).build();
		
		public static final TextureData PLUS = ATLAS.toBuilder().uv(0).size(10).build();
		public static final TextureData PLUS_HOVERED = ATLAS.toBuilder().uv(0, 16).size(10).build();
		public static final TextureData PLUS_INACTIVE = ATLAS.toBuilder().uv(0, 32).size(10).build();
		
		public static final TextureData MINUS = ATLAS.toBuilder().uv(16, 0).size(10).build();
		public static final TextureData MINUS_HOVERED = ATLAS.toBuilder().uv(16, 16).size(10).build();
		public static final TextureData MINUS_INACTIVE = ATLAS.toBuilder().uv(16, 32).size(10).build();
		
		public static final TextureData PLUS_SMALL = ATLAS.toBuilder().uv(0, 48).size(6).build();
		public static final TextureData PLUS_SMALL_HOVERED = ATLAS.toBuilder().uv(0, 64).size(6).build();
		public static final TextureData PLUS_SMALL_INACTIVE = ATLAS.toBuilder().uv(0, 80).size(6).build();
		
		public static final TextureData MINUS_SMALL = ATLAS.toBuilder().uv(16, 48).size(6).build();
		public static final TextureData MINUS_SMALL_HOVERED = ATLAS.toBuilder().uv(16, 64).size(6).build();
		public static final TextureData MINUS_SMALL_INACTIVE = ATLAS.toBuilder().uv(16, 80).size(6).build();
		
		public static final TextureData SEARCH = ATLAS.toBuilder().uv(32, 16).size(14).build();
		public static final TextureData SETTINGS = ATLAS.toBuilder().uv(32, 32).size(15).build();
		
		public static final TextureData WIDE_SEARCH = ATLAS.toBuilder().uv(64, 16).size(10, 11).build();
		public static final TextureData WIDE_SEARCH_HOVERED = ATLAS.toBuilder().uv(80, 16).size(10, 11).build();
		
		//Icons
		public static final Icon LOCK_ICON = Icon.of(LOCK, 3, 2);
		public static final Icon SMALL_LOCK_ICON = Icon.of(SMALL_LOCK, 2, 1);
		
		public static final Icon INFO_ICON = Icon.of(INFO, 4, 3);
		public static final Icon INFO_HOVERED_ICON = Icon.of(INFO_HOVERED, 4, 3);
		
		public static final Icon PAUSE_ICON = Icon.of(PAUSE, 3, 3);
		public static final Icon PAUSE_HOVERED_ICON = Icon.of(PAUSE_HOVERED, 3, 3);
		
		public static final Icon RESUME_ICON = Icon.of(RESUME, 5, 2.5F, 1.25f);
		public static final Icon RESUME_HOVERED_ICON = Icon.of(RESUME_HOVERED, 5, 2.5F, 1.25f);
		
		public static final Icon PLUS_ICON = Icon.of(PLUS, 3, 3);
		public static final Icon PLUS_HOVERED_ICON = Icon.of(PLUS_HOVERED, 3, 3);
		public static final Icon PLUS_INACTIVE_ICON = Icon.of(PLUS_INACTIVE, 3, 3);
		
		public static final Icon MINUS_ICON = Icon.of(MINUS, 3, 6);
		public static final Icon MINUS_HOVERED_ICON = Icon.of(MINUS_HOVERED, 3, 6);
		public static final Icon MINUS_INACTIVE_ICON = Icon.of(MINUS_INACTIVE, 3, 6);
		
		public static final Icon PLUS_SMALL_ICON = Icon.of(PLUS_SMALL, 2F, 2F, 1.15f);
		public static final Icon PLUS_SMALL_HOVERED_ICON = Icon.of(PLUS_SMALL_HOVERED, 2F, 2F, 1.15f);
		public static final Icon PLUS_SMALL_INACTIVE_ICON = Icon.of(PLUS_SMALL_INACTIVE, 2F, 2F, 1.15f);
		
		public static final Icon MINUS_SMALL_ICON = Icon.of(MINUS_SMALL, 2F, 2F, 1.15f);
		public static final Icon MINUS_SMALL_HOVERED_ICON = Icon.of(MINUS_SMALL_HOVERED, 2F, 2F, 1.15f);
		public static final Icon MINUS_SMALL_INACTIVE_ICON = Icon.of(MINUS_SMALL_INACTIVE, 2F, 2F, 1.15f);
		
		public static final Icon SEARCH_ICON = Icon.of(SEARCH, 0, 0);
		public static final Icon SETTINGS_ICON = Icon.of(SETTINGS, 0, 0);
		
		public static final Icon WIDE_SEARCH_ICON = Icon.of(WIDE_SEARCH, 2, 2);
		public static final Icon WIDE_SEARCH_HOVERED_ICON = Icon.of(WIDE_SEARCH_HOVERED, 2, 2);
		
	}
	
	public static final class SettingsTextures {
		
		private static final TextureData ATLAS = new TextureData.Builder()
				.location("icon/settings_icons").sourceSize(256, 256).build();
		
		public static final TextureData WARNING_SIGN = ATLAS.toBuilder().uv(112, 0).size(12).build();
		public static final Icon WARNING_SIGN_ICON = Icon.of(WARNING_SIGN, 3, 3);
		
		public static final TextureData RESET = ATLAS.toBuilder().uv(0, 96).size(8).build();
		public static final TextureData RESET_HOVERED = ATLAS.toBuilder().uv(16, 96).size(8).build();
		
		public static final Icon RESET_ICON = Icon.of(RESET, 3.5F, 3.5F);
		public static final Icon RESET_HOVERED_ICON = Icon.of(RESET_HOVERED, 3.5F, 3.5F);
		
		public static final TextureData SOUND_PING_ACTIVE = ATLAS.toBuilder().uv(0, 64).size(8).build();
		public static final TextureData SOUND_PING_ACTIVE_HOVERED = ATLAS.toBuilder().uv(16, 64).size(8).build();
		public static final TextureData SOUND_PING_INACTIVE = ATLAS.toBuilder().uv(0, 80).size(8, 6).build();
		public static final TextureData SOUND_PING_INACTIVE_HOVERED = ATLAS.toBuilder().uv(16, 80).size(8, 6).build();
		
		public static final Icon SOUND_PING_ACTIVE_ICON = Icon.of(SOUND_PING_ACTIVE, 3, 3.5F);
		public static final Icon SOUND_PING_ACTIVE_HOVERED_ICON = Icon.of(SOUND_PING_ACTIVE_HOVERED, 3, 3.5F);
		public static final Icon SOUND_PING_INACTIVE_ICON = Icon.of(SOUND_PING_INACTIVE, 3, 4.5F);
		public static final Icon SOUND_PING_INACTIVE_HOVERED_ICON = Icon.of(SOUND_PING_INACTIVE_HOVERED, 3, 4.5F);
		
		public static final TextureData HEIGHT_MARKER_ACTIVE = ATLAS.toBuilder().uv(64, 16).size(10).build();
		public static final TextureData HEIGHT_MARKER_ACTIVE_HOVERED = HEIGHT_MARKER_ACTIVE.toBuilder().uv(80, 16).build();
		public static final TextureData HEIGHT_MARKER_INACTIVE = HEIGHT_MARKER_ACTIVE.toBuilder().uv(64, 32).build();
		public static final TextureData HEIGHT_MARKER_INACTIVE_HOVERED = HEIGHT_MARKER_ACTIVE.toBuilder().uv(80, 32).build();
		
		public static final Icon HEIGHT_MARKER_ACTIVE_ICON = Icon.of(HEIGHT_MARKER_ACTIVE, 2, 2);
		public static final Icon HEIGHT_MARKER_ACTIVE_HOVERED_ICON = Icon.of(HEIGHT_MARKER_ACTIVE_HOVERED, 2, 2);
		public static final Icon HEIGHT_MARKER_INACTIVE_ICON = Icon.of(HEIGHT_MARKER_INACTIVE, 2, 2);
		public static final Icon HEIGHT_MARKER_INACTIVE_HOVERED_ICON = Icon.of(HEIGHT_MARKER_INACTIVE_HOVERED, 2, 2);
		
		public static final TextureData CHUNKS_RANGE = ATLAS.toBuilder().uv(0).size(10).build();
		public static final TextureData ENTITIES_RANGE = ATLAS.toBuilder().uv(16, 0).size(10).build();
		public static final TextureData WIDE_SEARCH_RANGE = ATLAS.toBuilder().uv(32, 0).size(10).build();
		
		public static final Icon CHUNKS_RANGE_ICON = Icon.of(CHUNKS_RANGE, 2, 2);
		public static final Icon ENTITIES_RANGE_ICON = Icon.of(ENTITIES_RANGE, 2, 2);
		public static final Icon WIDE_SEARCH_RANGE_ICON = Icon.of(WIDE_SEARCH_RANGE, 2, 2);
		
		public static final TextureData PRIORITY_NORMAL = ATLAS.toBuilder().uv(0, 16).size(10).build();
		public static final TextureData PRIORITY_NORMAL_HOVERED = ATLAS.toBuilder().uv(16, 16).size(10).build();
		public static final TextureData PRIORITY_OFF = ATLAS.toBuilder().uv(0, 32).size(10).build();
		public static final TextureData PRIORITY_OFF_HOVERED = ATLAS.toBuilder().uv(16, 32).size(10).build();
		public static final TextureData PRIORITY_INVERT = ATLAS.toBuilder().uv(0, 48).size(10).build();
		public static final TextureData PRIORITY_INVERT_HOVERED = ATLAS.toBuilder().uv(16, 48).size(10).build();
		
		public static final Icon PRIORITY_NORMAL_ICON = Icon.of(PRIORITY_NORMAL, 2, 2);
		public static final Icon PRIORITY_NORMAL_HOVERED_ICON = Icon.of(PRIORITY_NORMAL_HOVERED, 2, 2);
		public static final Icon PRIORITY_OFF_ICON = Icon.of(PRIORITY_OFF, 2, 2);
		public static final Icon PRIORITY_OFF_HOVERED_ICON = Icon.of(PRIORITY_OFF_HOVERED, 2, 2);
		public static final Icon PRIORITY_INVERT_ICON = Icon.of(PRIORITY_INVERT, 2, 2);
		public static final Icon PRIORITY_INVERT_HOVERED_ICON = Icon.of(PRIORITY_INVERT_HOVERED, 2, 2);
		
		public static final TextureData FORCE_LOAD_ACTIVE = ATLAS.toBuilder().uv(48, 0).size(11).build();
		public static final TextureData FORCE_LOAD_ACTIVE_HOVERED = ATLAS.toBuilder().uv(64, 0).size(11).build();
		public static final TextureData FORCE_LOAD_INACTIVE = ATLAS.toBuilder().uv(80, 0).size(11).build();
		public static final TextureData FORCE_LOAD_INACTIVE_HOVERED = ATLAS.toBuilder().uv(96, 0).size(11).build();
		
		public static final Icon FORCE_LOAD_ACTIVE_ICON = Icon.of(FORCE_LOAD_ACTIVE, 2, 2, 0.9F);
		public static final Icon FORCE_LOAD_ACTIVE_HOVERED_ICON = Icon.of(FORCE_LOAD_ACTIVE_HOVERED, 2, 2, 0.9F);
		public static final Icon FORCE_LOAD_INACTIVE_ICON = Icon.of(FORCE_LOAD_INACTIVE, 2, 2, 0.9F);
		public static final Icon FORCE_LOAD_INACTIVE_HOVERED_ICON = Icon.of(FORCE_LOAD_INACTIVE_HOVERED, 2, 2, 0.9F);
		
		public static final TextureData TARGET_VALIDATION_ACTIVE = ATLAS.toBuilder().uv(0, 144).size(16, 20).build();
		public static final TextureData TARGET_VALIDATION_ACTIVE_HOVERED = ATLAS.toBuilder().uv(32, 144).size(16, 20).build();
		public static final TextureData TARGET_VALIDATION_INACTIVE = ATLAS.toBuilder().uv(0, 176).size(16, 20).build();
		public static final TextureData TARGET_VALIDATION_INACTIVE_HOVERED = ATLAS.toBuilder().uv(32, 176).size(16, 20).build();
		
		public static final Icon TARGET_VALIDATION_ACTIVE_ICON = Icon.of(TARGET_VALIDATION_ACTIVE, 3, 2, 0.5f);
		public static final Icon TARGET_VALIDATION_ACTIVE_HOVERED_ICON = Icon.of(TARGET_VALIDATION_ACTIVE_HOVERED, 3, 2, 0.5f);
		public static final Icon TARGET_VALIDATION_INACTIVE_ICON = Icon.of(TARGET_VALIDATION_INACTIVE, 3, 2, 0.5f);
		public static final Icon TARGET_VALIDATION_INACTIVE_HOVERED_ICON = Icon.of(TARGET_VALIDATION_INACTIVE_HOVERED, 3, 2, 0.5f);
		
		public static final TextureData HUD_HAND = ATLAS.toBuilder().uv(32, 16).size(10).build();
		public static final TextureData HUD_HAND_HOVERED = ATLAS.toBuilder().uv(48, 16).size(10).build();
		
		public static final Icon HUD_HAND_ICON = Icon.of(HUD_HAND, 2, 2);
		public static final Icon HUD_HAND_HOVERED_ICON = Icon.of(HUD_HAND_HOVERED, 2, 2);
		
		public static final TextureData HUD_ALWAYS = ATLAS.toBuilder().uv(32, 32).size(10).build();
		public static final TextureData HUD_ALWAYS_HOVERED = ATLAS.toBuilder().uv(48, 32).size(10).build();
		
		public static final Icon HUD_ALWAYS_ICON = Icon.of(HUD_ALWAYS, 2, 2);
		public static final Icon HUD_ALWAYS_HOVERED_ICON = Icon.of(HUD_ALWAYS_HOVERED, 2, 2);
		
		public static final TextureData HUD_OFF = ATLAS.toBuilder().uv(32, 48).size(10).build();
		public static final TextureData HUD_OFF_HOVERED = ATLAS.toBuilder().uv(48, 48).size(10).build();
		
		public static final Icon HUD_OFF_ICON = Icon.of(HUD_OFF, 2, 2);
		public static final Icon HUD_OFF_HOVERED_ICON = Icon.of(HUD_OFF_HOVERED, 2, 2);
		
		public static final TextureData HUD_X_POS = ATLAS.toBuilder().uv(0, 112).size(6, 7).build();
		public static final TextureData HUD_Y_POS = ATLAS.toBuilder().uv(16, 112).size(6, 7).build();
		public static final TextureData HUD_WIDTH = ATLAS.toBuilder().uv(32, 112).size(6, 7).build();
		public static final TextureData HUD_HEIGHT = ATLAS.toBuilder().uv(48, 112).size(5, 7).build();
		public static final TextureData HUD_SCALE = ATLAS.toBuilder().uv(64, 112).size(7).build();
		
		public static final Icon HUD_X_POS_ICON = Icon.of(HUD_X_POS, 2, 2);
		public static final Icon HUD_Y_POS_ICON = Icon.of(HUD_Y_POS, 2, 2);
		public static final Icon HUD_WIDTH_ICON = Icon.of(HUD_WIDTH, 2, 2);
		public static final Icon HUD_HEIGHT_ICON = Icon.of(HUD_HEIGHT, 3, 2);
		public static final Icon HUD_SCALE_ICON = Icon.of(HUD_SCALE, 2, 2);
		
		public static final TextureData HUD_ALIGNMENT_CENTER = ATLAS.toBuilder().uv(64).size(4).build();
		public static final TextureData HUD_ALIGNMENT_CENTER_HOVERED = ATLAS.toBuilder().uv(80, 64).size(4).build();
		public static final TextureData HUD_ALIGNMENT_LEFT = ATLAS.toBuilder().uv(64, 96).size(4).build();
		public static final TextureData HUD_ALIGNMENT_LEFT_HOVERED = ATLAS.toBuilder().uv(80, 96).size(4).build();
		public static final TextureData HUD_ALIGNMENT_RIGHT = ATLAS.toBuilder().uv(64, 80).size(4).build();
		public static final TextureData HUD_ALIGNMENT_RIGHT_HOVERED = ATLAS.toBuilder().uv(80).size(4).build();
		
		public static final Icon HUD_ALIGNMENT_CENTER_ICON = Icon.of(HUD_ALIGNMENT_CENTER, 3, 1);
		public static final Icon HUD_ALIGNMENT_CENTER_HOVERED_ICON = Icon.of(HUD_ALIGNMENT_CENTER_HOVERED, 3, 1);
		public static final Icon HUD_ALIGNMENT_LEFT_ICON = Icon.of(HUD_ALIGNMENT_LEFT, 1, 1);
		public static final Icon HUD_ALIGNMENT_LEFT_HOVERED_ICON = Icon.of(HUD_ALIGNMENT_LEFT_HOVERED, 1, 1);
		public static final Icon HUD_ALIGNMENT_RIGHT_ICON = Icon.of(HUD_ALIGNMENT_RIGHT, 5, 1);
		public static final Icon HUD_ALIGNMENT_RIGHT_HOVERED_ICON = Icon.of(HUD_ALIGNMENT_RIGHT_HOVERED, 5, 1);
		
		public static final TextureData HUD_TYPE_COMPACT = ATLAS.toBuilder().uv(0, 128).size(5, 4).build();
		public static final TextureData HUD_TYPE_COMPACT_HOVERED = ATLAS.toBuilder().uv(16, 128).size(5, 4).build();
		public static final TextureData HUD_TYPE_EXTENDED = ATLAS.toBuilder().uv(32, 128).size(5, 6).build();
		public static final TextureData HUD_TYPE_EXTENDED_HOVERED = ATLAS.toBuilder().uv(48, 128).size(5, 6).build();
		
		public static final Icon HUD_TYPE_COMPACT_ICON = Icon.of(HUD_TYPE_COMPACT, 2, 2);
		public static final Icon HUD_TYPE_COMPACT_HOVERED_ICON = Icon.of(HUD_TYPE_COMPACT_HOVERED, 2, 2);
		public static final Icon HUD_TYPE_EXTENDED_ICON = Icon.of(HUD_TYPE_EXTENDED, 2, 2);
		public static final Icon HUD_TYPE_EXTENDED_HOVERED_ICON = Icon.of(HUD_TYPE_EXTENDED_HOVERED, 2, 2);
		
		public static final TextureData HUD_CHAT_ACTIVE = ATLAS.toBuilder().uv(32, 80).size(6, 7).build();
		public static final TextureData HUD_CHAT_ACTIVE_HOVERED = ATLAS.toBuilder().uv(48, 80).size(6, 7).build();
		public static final TextureData HUD_CHAT_INACTIVE = ATLAS.toBuilder().uv(32, 64).size(8).build();
		public static final TextureData HUD_CHAT_INACTIVE_HOVERED = ATLAS.toBuilder().uv(48, 64).size(8).build();
		
		public static final Icon HUD_CHAT_ACTIVE_ICON = Icon.of(HUD_CHAT_ACTIVE, 2, 2);
		public static final Icon HUD_CHAT_ACTIVE_HOVERED_ICON = Icon.of(HUD_CHAT_ACTIVE_HOVERED, 2, 2);
		public static final Icon HUD_CHAT_INACTIVE_ICON = Icon.of(HUD_CHAT_INACTIVE, 1, 1);
		public static final Icon HUD_CHAT_INACTIVE_HOVERED_ICON = Icon.of(HUD_CHAT_INACTIVE_HOVERED, 1, 1);
		
		public static final TextureData HUD_RESET = ATLAS.toBuilder().uv(32, 96).size(5, 7).build();
		public static final TextureData HUD_RESET_HOVERED = ATLAS.toBuilder().uv(48, 96).size(5, 7).build();
		
		public static final Icon HUD_RESET_ICON = Icon.of(HUD_RESET, 3, 2);
		public static final Icon HUD_RESET_HOVERED_ICON = Icon.of(HUD_RESET_HOVERED, 3, 2);
		
	}
	
	public static final class ButtonTextures {
		
		public static final TextureData BUTTON = new TextureData.Builder()
				.location("button/button").size(14, 14).uv(0, 0).sourceSize(14, 70).build();
		public static final TextureData BUTTON_HOVERED = new TextureData.Builder()
				.location("button/button").size(14, 14).uv(0, 14).sourceSize(14, 70).build();
		public static final TextureData BUTTON_ACTIVE = new TextureData.Builder()
				.location("button/button").size(14, 14).uv(0, 28).sourceSize(14, 70).build();
		public static final TextureData BUTTON_ACTIVE_HOVERED = new TextureData.Builder()
				.location("button/button").uv(0, 42).size(14, 14).sourceSize(14, 70).build();
		public static final TextureData BUTTON_DISABLED = new TextureData.Builder()
				.location("button/button").size(14, 14).uv(0, 56).sourceSize(14, 70).build();
		
		public static final TextureData SMALL_BUTTON = new TextureData.Builder()
				.location("button/small_button").size(10, 10).uv(0, 0).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_HOVERED = new TextureData.Builder()
				.location("button/small_button").size(10, 10).uv(0, 10).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_ACTIVE = new TextureData.Builder()
				.location("button/small_button").size(10, 10).uv(0, 20).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_ACTIVE_HOVERED = new TextureData.Builder()
				.location("button/small_button").size(10, 10).uv(0, 30).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_DISABLED = new TextureData.Builder()
				.location("button/small_button").size(10, 10).uv(0, 40).sourceSize(10, 50).build();
		
		public static final ButtonTexturesSet TOGGLE_BUTTON = new ButtonTexturesSet(
				BUTTON, BUTTON_HOVERED, BUTTON_ACTIVE, BUTTON_ACTIVE_HOVERED, BUTTON_DISABLED
		);
		
		public static final ButtonTexturesSet SMALL_TOGGLE_BUTTON = new ButtonTexturesSet(
				SMALL_BUTTON, SMALL_BUTTON_HOVERED, SMALL_BUTTON_ACTIVE, SMALL_BUTTON_ACTIVE_HOVERED, SMALL_BUTTON_DISABLED
		);
		
	}
	
	public static final class SearchModeTextures {
		
		private static final TextureData ATLAS = new TextureData.Builder()
				.location("icon/search_mode_icons")
				.sourceSize(128, 128)
				.build();
		
		// Blocks
		public static final TextureData BLOCKS_ACTIVE = ATLAS.toBuilder().uv(16, 0).size(8).build();
		public static final TextureData BLOCKS_INACTIVE = ATLAS.toBuilder().uv(0, 0).size(8).build();
		
		public static final Icon BLOCKS_ACTIVE_ICON = Icon.of(BLOCKS_ACTIVE, 2, 2, 1.25F);
		public static final Icon BLOCKS_INACTIVE_ICON = Icon.of(BLOCKS_INACTIVE, 2, 2, 1.25F);
		
		// Mobs
		public static final TextureData MOBS_ACTIVE = ATLAS.toBuilder().uv(16, 16).size(8).build();
		public static final TextureData MOBS_INACTIVE = ATLAS.toBuilder().uv(0, 16).size(8).build();
		
		public static final Icon MOBS_ACTIVE_ICON = Icon.of(MOBS_ACTIVE, 2, 2, 1.25F);
		public static final Icon MOBS_INACTIVE_ICON = Icon.of(MOBS_INACTIVE, 2, 2, 1.25F);
		
		// Fluids
		public static final TextureData FLUIDS_ACTIVE = new TextureData.Builder().location("icon/fluids_search_active").size(8).sourceSize(8, 248).build();
		public static final TextureData FLUIDS_INACTIVE = new TextureData.Builder().location("icon/fluids_search_inactive").size(8).sourceSize(8, 248).build();
		
		public static final Icon FLUIDS_ACTIVE_ICON = Icon.of(FLUIDS_ACTIVE, 2, 2, 1.25F);
		public static final Icon FLUIDS_INACTIVE_ICON = Icon.of(FLUIDS_INACTIVE, 2, 2, 1.25F);
		
		// Spawners
		public static final TextureData SPAWNERS_ACTIVE = ATLAS.toBuilder().uv(48, 16).size(10).build();
		public static final TextureData SPAWNERS_INACTIVE = ATLAS.toBuilder().uv(32, 16).size(10).build();
		
		public static final Icon SPAWNERS_ACTIVE_ICON = Icon.of(SPAWNERS_ACTIVE, 2, 2);
		public static final Icon SPAWNERS_INACTIVE_ICON = Icon.of(SPAWNERS_INACTIVE, 2, 2);
		
		// ItemEntities
		public static final TextureData ITEM_ENTITIES_ACTIVE = ATLAS.toBuilder().uv(80, 0).size(11).build();
		public static final TextureData ITEM_ENTITIES_INACTIVE = ATLAS.toBuilder().uv(64, 0).size(11).build();
		
		public static final Icon ITEM_ENTITIES_ACTIVE_ICON = Icon.of(ITEM_ENTITIES_ACTIVE, 2, 2);
		public static final Icon ITEM_ENTITIES_INACTIVE_ICON = Icon.of(ITEM_ENTITIES_INACTIVE, 2, 2);
		
		// Drops
		public static final TextureData DROP_ACTIVE = ATLAS.toBuilder().uv(48, 0).size(11).build();
		public static final TextureData DROP_INACTIVE = ATLAS.toBuilder().uv(32, 0).size(11).build();
		
		public static final Icon DROP_ACTIVE_ICON = Icon.of(DROP_ACTIVE, 1, 2);
		public static final Icon DROP_INACTIVE_ICON = Icon.of(DROP_INACTIVE, 1, 2);
		
		// Villagers
		public static final TextureData VILLAGERS_ACTIVE = ATLAS.toBuilder().uv(80, 16).size(12).build();
		public static final TextureData VILLAGERS_INACTIVE = ATLAS.toBuilder().uv(64, 16).size(12).build();
		
		public static final Icon VILLAGERS_ACTIVE_ICON = Icon.of(VILLAGERS_ACTIVE, 1, 1);
		public static final Icon VILLAGERS_INACTIVE_ICON = Icon.of(VILLAGERS_INACTIVE, 1, 1);
		
		public static final TextureData VILLAGERS_BUYS_ACTIVE = ATLAS.toBuilder().uv(16, 48).size(8, 9).build();
		public static final TextureData VILLAGERS_BUYS_INACTIVE = ATLAS.toBuilder().uv(0, 48).size(8, 9).build();
		
		public static final Icon VILLAGERS_BUYS_ACTIVE_ICON = Icon.of(VILLAGERS_BUYS_ACTIVE, 1, 0);
		public static final Icon VILLAGERS_BUYS_INACTIVE_ICON = Icon.of(VILLAGERS_BUYS_INACTIVE, 1, 0);
		
		public static final TextureData VILLAGERS_SELLS_ACTIVE = ATLAS.toBuilder().uv(48, 48).size(10).build();
		public static final TextureData VILLAGERS_SELLS_INACTIVE = ATLAS.toBuilder().uv(32, 48).size(10).build();
		
		public static final Icon VILLAGERS_SELLS_ACTIVE_ICON = Icon.of(VILLAGERS_SELLS_ACTIVE);
		public static final Icon VILLAGERS_SELLS_INACTIVE_ICON = Icon.of(VILLAGERS_SELLS_INACTIVE);
		
		// Inventories
		public static final TextureData INVENTORIES_ACTIVE = ATLAS.toBuilder().uv(112, 0).size(9, 11).build();
		public static final TextureData INVENTORIES_INACTIVE = ATLAS.toBuilder().uv(96, 0).size(9, 11).build();
		
		public static final Icon INVENTORIES_ACTIVE_ICON = Icon.of(INVENTORIES_ACTIVE, 2.05F, 1F, 1.10f);
		public static final Icon INVENTORIES_INACTIVE_ICON = Icon.of(INVENTORIES_INACTIVE, 2.05F, 1F, 1.10f);
		
		public static final TextureData INVENTORIES_PLAYERS_ACTIVE = ATLAS.toBuilder().uv(112, 48).size(8).build();
		public static final TextureData INVENTORIES_PLAYERS_INACTIVE = ATLAS.toBuilder().uv(96, 48).size(8).build();
		
		public static final Icon INVENTORIES_PLAYERS_ACTIVE_ICON = Icon.of(INVENTORIES_PLAYERS_ACTIVE, 1, 1);
		public static final Icon INVENTORIES_PLAYERS_INACTIVE_ICON = Icon.of(INVENTORIES_PLAYERS_INACTIVE, 1, 1);
		
		public static final TextureData INVENTORIES_MOBS_ACTIVE = ATLAS.toBuilder().uv(80, 48).size(8).build();
		public static final TextureData INVENTORIES_MOBS_INACTIVE = ATLAS.toBuilder().uv(64, 48).size(8).build();
		
		public static final Icon INVENTORIES_MOBS_ACTIVE_ICON = Icon.of(INVENTORIES_MOBS_ACTIVE, 1, 1);
		public static final Icon INVENTORIES_MOBS_INACTIVE_ICON = Icon.of(INVENTORIES_MOBS_INACTIVE, 1, 1);
		
		// Containers
		public static final TextureData CONTAINERS_ACTIVE = ATLAS.toBuilder().uv(112, 16).size(8).build();
		public static final TextureData CONTAINERS_INACTIVE = ATLAS.toBuilder().uv(96, 16).size(8).build();
		
		public static final Icon CONTAINERS_ACTIVE_ICON = Icon.of(CONTAINERS_ACTIVE, 2, 2, 1.25F);
		public static final Icon CONTAINERS_INACTIVE_ICON = Icon.of(CONTAINERS_INACTIVE, 2, 2, 1.25F);
		
		public static final TextureData BLOCK_CONTAINERS_ACTIVE = ATLAS.toBuilder().uv(16, 64).size(8).build();
		public static final TextureData BLOCK_CONTAINERS_INACTIVE = ATLAS.toBuilder().uv(0, 64).size(8).build();
		
		public static final Icon BLOCK_CONTAINERS_ACTIVE_ICON = Icon.of(BLOCK_CONTAINERS_ACTIVE, 1, 1);
		public static final Icon BLOCK_CONTAINERS_INACTIVE_ICON = Icon.of(BLOCK_CONTAINERS_INACTIVE, 1, 1);
		
		public static final TextureData ENTITY_CONTAINERS_ACTIVE = ATLAS.toBuilder().uv(48, 64).size(10).build();
		public static final TextureData ENTITY_CONTAINERS_INACTIVE = ATLAS.toBuilder().uv(32, 64).size(10).build();
		
		public static final Icon ENTITY_CONTAINERS_ACTIVE_ICON = Icon.of(ENTITY_CONTAINERS_ACTIVE);
		public static final Icon ENTITY_CONTAINERS_INACTIVE_ICON = Icon.of(ENTITY_CONTAINERS_INACTIVE);
		
		// Lootr
		public static final TextureData LOOTR_ALL = ATLAS.toBuilder().uv(64).size(8).build();
		public static final TextureData LOOTR_CLOSED = ATLAS.toBuilder().uv(80, 64).size(8).build();
		public static final TextureData LOOTR_INACTIVE = ATLAS.toBuilder().uv(96, 64).size(8).build();
		public static final TextureData LOOTR_OPENED = ATLAS.toBuilder().uv(112, 64).size(8).build();
		
		public static final Icon LOOTR_ALL_ICON = Icon.of(LOOTR_ALL, 1, 1);
		public static final Icon LOOTR_CLOSED_ICON = Icon.of(LOOTR_CLOSED, 1, 1);
		public static final Icon LOOTR_INACTIVE_ICON = Icon.of(LOOTR_INACTIVE, 1, 1);
		public static final Icon LOOTR_OPENED_ICON = Icon.of(LOOTR_OPENED, 1, 1);
		
	}
	
	public static final class PopupTextures {
		
		public static final TextureData POPUP_1X2 = new TextureData.Builder()
				.location("popup/background_1x2").size(18, 29).build();
		public static final TextureData POPUP_2X1 = new TextureData.Builder()
				.location("popup/background_2x1").size(29, 18).build();
		public static final TextureData POPUP_3X1 = new TextureData.Builder()
				.location("popup/background_3x1").size(40, 18).build();
		public static final TextureData POPUP_3X3 = new TextureData.Builder()
				.location("popup/background_3x3").size(40, 40).build();
		
		public static final TextureData CONNECTOR_HORIZONTAL = new TextureData.Builder()
				.location("popup/connector_horizontal").size(12, 1).build();
		public static final TextureData CONNECTOR_VERTICAL = new TextureData.Builder()
				.location("popup/connector_vertical").size(1, 12).build();
		
	}
	
	public static final class TabTextures {
		
		public static final TextureData PAGE_TAB_ACTIVE = new TextureData.Builder()
				.location("tab/page_tab").sourceSize(26, 48).size(26, 24).build();
		
		public static final TextureData PAGE_TAB_INACTIVE = new TextureData.Builder()
				.location("tab/page_tab").uv(0, 24).sourceSize(26, 48).size(26, 24).build();
		
		public static final TextureData WARNING_TAB = new TextureData.Builder()
				.location("tab/warning_tab").size(22).build();
		
	}
	
}