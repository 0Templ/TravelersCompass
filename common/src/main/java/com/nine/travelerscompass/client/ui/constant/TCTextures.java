package com.nine.travelerscompass.client.ui.constant;

import com.nine.travelerscompass.client.utils.ButtonTexturesSet;
import com.nine.travelerscompass.client.utils.TextureData;

public final class TCTextures {

	// -------------------------------------------------------------------------
	// Common icons atlas  (128x128)
	// -------------------------------------------------------------------------
	public static final class Common {

		private static final TextureData ATLAS = new TextureData.Builder()
				.location("icon/common_icons").sourceSize(128, 128).build();

		public static final TextureData LOCK              = ATLAS.toBuilder().uv(32,   0).size( 8, 10).build();
		public static final TextureData SMALL_LOCK        = ATLAS.toBuilder().uv(48,   0).size( 6,  8).build();

		public static final TextureData INFO              = ATLAS.toBuilder().uv(64,   0).size( 6,  8).build();
		public static final TextureData INFO_HOVERED      = ATLAS.toBuilder().uv(80,   0).size( 6,  8).build();

		public static final TextureData PAUSE             = ATLAS.toBuilder().uv(96,   0).size( 9).build();
		public static final TextureData PAUSE_HOVERED     = ATLAS.toBuilder().uv(112,  0).size( 9).build();

		public static final TextureData RESUME            = ATLAS.toBuilder().uv(96,  16).size( 4,  8).build();
		public static final TextureData RESUME_HOVERED    = ATLAS.toBuilder().uv(112, 16).size( 4,  8).build();

		public static final TextureData PLUS              = ATLAS.toBuilder().uv( 0,   0).size(10).build();
		public static final TextureData PLUS_HOVERED      = ATLAS.toBuilder().uv( 0,  16).size(10).build();
		public static final TextureData PLUS_INACTIVE     = ATLAS.toBuilder().uv( 0,  32).size(10).build();

		public static final TextureData MINUS             = ATLAS.toBuilder().uv(16,   0).size(10).build();
		public static final TextureData MINUS_HOVERED     = ATLAS.toBuilder().uv(16,  16).size(10).build();
		public static final TextureData MINUS_INACTIVE    = ATLAS.toBuilder().uv(16,  32).size(10).build();

		public static final TextureData PLUS_SMALL         = ATLAS.toBuilder().uv( 0,  48).size(6).build();
		public static final TextureData PLUS_SMALL_HOVERED  = ATLAS.toBuilder().uv( 0,  64).size(6).build();
		public static final TextureData PLUS_SMALL_INACTIVE = ATLAS.toBuilder().uv( 0,  80).size(6).build();

		public static final TextureData MINUS_SMALL         = ATLAS.toBuilder().uv(16,  48).size(6).build();
		public static final TextureData MINUS_SMALL_HOVERED  = ATLAS.toBuilder().uv(16,  64).size(6).build();
		public static final TextureData MINUS_SMALL_INACTIVE = ATLAS.toBuilder().uv(16,  80).size(6).build();

		public static final TextureData SEARCH            = ATLAS.toBuilder().uv(32,  16).size(14).build();
		public static final TextureData SETTINGS          = ATLAS.toBuilder().uv(32,  32).size(15).build();

		public static final TextureData WIDE_SEARCH         = ATLAS.toBuilder().uv(64,  16).size(10, 11).build();
		public static final TextureData WIDE_SEARCH_HOVERED = ATLAS.toBuilder().uv(80,  16).size(10, 11).build();

		private Common() {}
	}

	// -------------------------------------------------------------------------
	// Settings icons atlas  (256x256)
	// -------------------------------------------------------------------------
	public static final class Settings {

		private static final TextureData ATLAS = new TextureData.Builder()
				.location("icon/settings_icons").sourceSize(256, 256).build();

		public static final TextureData WARNING_SIGN = ATLAS.toBuilder().uv(112,  0).size(12).build();

		public static final TextureData RESET         = ATLAS.toBuilder().uv( 0,  96).size( 8).build();
		public static final TextureData RESET_HOVERED = ATLAS.toBuilder().uv(16,  96).size( 8).build();

		public static final TextureData SOUND_PING_ACTIVE           = ATLAS.toBuilder().uv( 0,  64).size(8).build();
		public static final TextureData SOUND_PING_ACTIVE_HOVERED   = ATLAS.toBuilder().uv(16,  64).size(8).build();
		public static final TextureData SOUND_PING_INACTIVE         = ATLAS.toBuilder().uv( 0,  80).size(8, 6).build();
		public static final TextureData SOUND_PING_INACTIVE_HOVERED = ATLAS.toBuilder().uv(16,  80).size(8, 6).build();

		public static final TextureData HEIGHT_MARKER_ACTIVE          = ATLAS.toBuilder().uv(64,  16).size(10).build();
		public static final TextureData HEIGHT_MARKER_ACTIVE_HOVERED  = HEIGHT_MARKER_ACTIVE.toBuilder().uv(80,  16).build();
		public static final TextureData HEIGHT_MARKER_INACTIVE        = HEIGHT_MARKER_ACTIVE.toBuilder().uv(64,  32).build();
		public static final TextureData HEIGHT_MARKER_INACTIVE_HOVERED = HEIGHT_MARKER_ACTIVE.toBuilder().uv(80, 32).build();

		public static final TextureData CHUNKS_RANGE      = ATLAS.toBuilder().uv( 0,  0).size(10).build();
		public static final TextureData ENTITIES_RANGE    = ATLAS.toBuilder().uv(16,  0).size(10).build();
		public static final TextureData WIDE_SEARCH_RANGE = ATLAS.toBuilder().uv(32,  0).size(10).build();

		public static final TextureData PRIORITY_NORMAL          = ATLAS.toBuilder().uv( 0,  16).size(10).build();
		public static final TextureData PRIORITY_NORMAL_HOVERED  = ATLAS.toBuilder().uv(16,  16).size(10).build();
		public static final TextureData PRIORITY_OFF             = ATLAS.toBuilder().uv( 0,  32).size(10).build();
		public static final TextureData PRIORITY_OFF_HOVERED     = ATLAS.toBuilder().uv(16,  32).size(10).build();
		public static final TextureData PRIORITY_INVERT          = ATLAS.toBuilder().uv( 0,  48).size(10).build();
		public static final TextureData PRIORITY_INVERT_HOVERED  = ATLAS.toBuilder().uv(16,  48).size(10).build();

		public static final TextureData FORCE_LOAD_ACTIVE          = ATLAS.toBuilder().uv(48,  0).size(11).build();
		public static final TextureData FORCE_LOAD_ACTIVE_HOVERED  = ATLAS.toBuilder().uv(64,  0).size(11).build();
		public static final TextureData FORCE_LOAD_INACTIVE        = ATLAS.toBuilder().uv(80,  0).size(11).build();
		public static final TextureData FORCE_LOAD_INACTIVE_HOVERED = ATLAS.toBuilder().uv(96, 0).size(11).build();

		public static final TextureData TARGET_VALIDATION_ACTIVE          = ATLAS.toBuilder().uv( 0, 144).size(16, 20).build();
		public static final TextureData TARGET_VALIDATION_ACTIVE_HOVERED  = ATLAS.toBuilder().uv(32, 144).size(16, 20).build();
		public static final TextureData TARGET_VALIDATION_INACTIVE        = ATLAS.toBuilder().uv( 0, 176).size(16, 20).build();
		public static final TextureData TARGET_VALIDATION_INACTIVE_HOVERED = ATLAS.toBuilder().uv(32, 176).size(16, 20).build();

		public static final TextureData HUD_HAND         = ATLAS.toBuilder().uv(32,  16).size(10).build();
		public static final TextureData HUD_HAND_HOVERED = ATLAS.toBuilder().uv(48,  16).size(10).build();

		public static final TextureData HUD_ALWAYS         = ATLAS.toBuilder().uv(32,  32).size(10).build();
		public static final TextureData HUD_ALWAYS_HOVERED = ATLAS.toBuilder().uv(48,  32).size(10).build();

		public static final TextureData HUD_OFF         = ATLAS.toBuilder().uv(32,  48).size(10).build();
		public static final TextureData HUD_OFF_HOVERED = ATLAS.toBuilder().uv(48,  48).size(10).build();

		public static final TextureData HUD_X_POS  = ATLAS.toBuilder().uv( 0, 112).size(6, 7).build();
		public static final TextureData HUD_Y_POS  = ATLAS.toBuilder().uv(16, 112).size(6, 7).build();
		public static final TextureData HUD_WIDTH  = ATLAS.toBuilder().uv(32, 112).size(6, 7).build();
		public static final TextureData HUD_HEIGHT = ATLAS.toBuilder().uv(48, 112).size(5, 7).build();
		public static final TextureData HUD_SCALE  = ATLAS.toBuilder().uv(64, 112).size(7).build();

		public static final TextureData HUD_ALIGNMENT_CENTER         = ATLAS.toBuilder().uv(64,  64).size(4).build();
		public static final TextureData HUD_ALIGNMENT_CENTER_HOVERED = ATLAS.toBuilder().uv(80,  64).size(4).build();
		public static final TextureData HUD_ALIGNMENT_LEFT           = ATLAS.toBuilder().uv(64,  96).size(4).build();
		public static final TextureData HUD_ALIGNMENT_LEFT_HOVERED   = ATLAS.toBuilder().uv(80,  96).size(4).build();
		public static final TextureData HUD_ALIGNMENT_RIGHT          = ATLAS.toBuilder().uv(64,  80).size(4).build();
		public static final TextureData HUD_ALIGNMENT_RIGHT_HOVERED  = ATLAS.toBuilder().uv(80,  80).size(4).build();

		public static final TextureData HUD_TYPE_COMPACT          = ATLAS.toBuilder().uv( 0, 128).size(5, 4).build();
		public static final TextureData HUD_TYPE_COMPACT_HOVERED  = ATLAS.toBuilder().uv(16, 128).size(5, 4).build();
		public static final TextureData HUD_TYPE_EXTENDED         = ATLAS.toBuilder().uv(32, 128).size(5, 6).build();
		public static final TextureData HUD_TYPE_EXTENDED_HOVERED = ATLAS.toBuilder().uv(48, 128).size(5, 6).build();

		public static final TextureData HUD_CHAT_ACTIVE           = ATLAS.toBuilder().uv(32,  80).size(6, 7).build();
		public static final TextureData HUD_CHAT_ACTIVE_HOVERED   = ATLAS.toBuilder().uv(48,  80).size(6, 7).build();
		public static final TextureData HUD_CHAT_INACTIVE         = ATLAS.toBuilder().uv(32,  64).size(8).build();
		public static final TextureData HUD_CHAT_INACTIVE_HOVERED = ATLAS.toBuilder().uv(48,  64).size(8).build();

		public static final TextureData HUD_RESET         = ATLAS.toBuilder().uv(32,  96).size(5, 7).build();
		public static final TextureData HUD_RESET_HOVERED = ATLAS.toBuilder().uv(48,  96).size(5, 7).build();

		private Settings() {}
	}

	// -------------------------------------------------------------------------
	// Button backgrounds
	// -------------------------------------------------------------------------
	public static final class Buttons {

		public static final TextureData BUTTON               = new TextureData.Builder().location("button/button").size(14, 14).uv( 0,  0).sourceSize(14, 70).build();
		public static final TextureData BUTTON_HOVERED       = new TextureData.Builder().location("button/button").size(14, 14).uv( 0, 14).sourceSize(14, 70).build();
		public static final TextureData BUTTON_ACTIVE        = new TextureData.Builder().location("button/button").size(14, 14).uv( 0, 28).sourceSize(14, 70).build();
		public static final TextureData BUTTON_ACTIVE_HOVERED = new TextureData.Builder().location("button/button").uv( 0, 42).size(14, 14).sourceSize(14, 70).build();
		public static final TextureData BUTTON_DISABLED      = new TextureData.Builder().location("button/button").size(14, 14).uv( 0, 56).sourceSize(14, 70).build();

		public static final TextureData SMALL_BUTTON               = new TextureData.Builder().location("button/small_button").size(10, 10).uv( 0,  0).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_HOVERED       = new TextureData.Builder().location("button/small_button").size(10, 10).uv( 0, 10).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_ACTIVE        = new TextureData.Builder().location("button/small_button").size(10, 10).uv( 0, 20).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_ACTIVE_HOVERED = new TextureData.Builder().location("button/small_button").size(10, 10).uv( 0, 30).sourceSize(10, 50).build();
		public static final TextureData SMALL_BUTTON_DISABLED      = new TextureData.Builder().location("button/small_button").size(10, 10).uv( 0, 40).sourceSize(10, 50).build();

		public static final ButtonTexturesSet TOGGLE_BUTTON = new ButtonTexturesSet(
				BUTTON, BUTTON_HOVERED, BUTTON_ACTIVE, BUTTON_ACTIVE_HOVERED, BUTTON_DISABLED);

		public static final ButtonTexturesSet SMALL_TOGGLE_BUTTON = new ButtonTexturesSet(
				SMALL_BUTTON, SMALL_BUTTON_HOVERED, SMALL_BUTTON_ACTIVE, SMALL_BUTTON_ACTIVE_HOVERED, SMALL_BUTTON_DISABLED);

		private Buttons() {}
	}

	// -------------------------------------------------------------------------
	// Search mode icons atlas  (128x128)
	// -------------------------------------------------------------------------
	public static final class SearchMode {

		private static final TextureData ATLAS = new TextureData.Builder()
				.location("icon/search_mode_icons").sourceSize(128, 128).build();

		public static final TextureData BLOCKS_ACTIVE   = ATLAS.toBuilder().uv(16,  0).size(8).build();
		public static final TextureData BLOCKS_INACTIVE = ATLAS.toBuilder().uv( 0,  0).size(8).build();

		public static final TextureData MOBS_ACTIVE   = ATLAS.toBuilder().uv(16,  16).size(8).build();
		public static final TextureData MOBS_INACTIVE = ATLAS.toBuilder().uv( 0,  16).size(8).build();

		public static final TextureData FLUIDS_ACTIVE   = new TextureData.Builder().location("icon/fluids_search_active").size(8).sourceSize(8, 248).build();
		public static final TextureData FLUIDS_INACTIVE = new TextureData.Builder().location("icon/fluids_search_inactive").size(8).sourceSize(8, 248).build();

		public static final TextureData SPAWNERS_ACTIVE   = ATLAS.toBuilder().uv(48,  16).size(10).build();
		public static final TextureData SPAWNERS_INACTIVE = ATLAS.toBuilder().uv(32,  16).size(10).build();

		public static final TextureData ITEM_ENTITIES_ACTIVE   = ATLAS.toBuilder().uv(80,  0).size(11).build();
		public static final TextureData ITEM_ENTITIES_INACTIVE = ATLAS.toBuilder().uv(64,  0).size(11).build();

		public static final TextureData DROP_ACTIVE   = ATLAS.toBuilder().uv(48,  0).size(11).build();
		public static final TextureData DROP_INACTIVE = ATLAS.toBuilder().uv(32,  0).size(11).build();

		public static final TextureData VILLAGERS_ACTIVE   = ATLAS.toBuilder().uv(80,  16).size(12).build();
		public static final TextureData VILLAGERS_INACTIVE = ATLAS.toBuilder().uv(64,  16).size(12).build();

		public static final TextureData VILLAGERS_BUYS_ACTIVE   = ATLAS.toBuilder().uv(16,  48).size(8, 9).build();
		public static final TextureData VILLAGERS_BUYS_INACTIVE = ATLAS.toBuilder().uv( 0,  48).size(8, 9).build();

		public static final TextureData VILLAGERS_SELLS_ACTIVE   = ATLAS.toBuilder().uv(48,  48).size(10).build();
		public static final TextureData VILLAGERS_SELLS_INACTIVE = ATLAS.toBuilder().uv(32,  48).size(10).build();

		public static final TextureData INVENTORIES_ACTIVE   = ATLAS.toBuilder().uv(112,  0).size(9, 11).build();
		public static final TextureData INVENTORIES_INACTIVE = ATLAS.toBuilder().uv( 96,  0).size(9, 11).build();

		public static final TextureData INVENTORIES_PLAYERS_ACTIVE   = ATLAS.toBuilder().uv(112,  48).size(8).build();
		public static final TextureData INVENTORIES_PLAYERS_INACTIVE = ATLAS.toBuilder().uv( 96,  48).size(8).build();

		public static final TextureData INVENTORIES_MOBS_ACTIVE   = ATLAS.toBuilder().uv(80,  48).size(8).build();
		public static final TextureData INVENTORIES_MOBS_INACTIVE = ATLAS.toBuilder().uv(64,  48).size(8).build();

		public static final TextureData CONTAINERS_ACTIVE   = ATLAS.toBuilder().uv(112,  16).size(8).build();
		public static final TextureData CONTAINERS_INACTIVE = ATLAS.toBuilder().uv( 96,  16).size(8).build();

		public static final TextureData BLOCK_CONTAINERS_ACTIVE   = ATLAS.toBuilder().uv(16,  64).size(8).build();
		public static final TextureData BLOCK_CONTAINERS_INACTIVE = ATLAS.toBuilder().uv( 0,  64).size(8).build();

		public static final TextureData ENTITY_CONTAINERS_ACTIVE   = ATLAS.toBuilder().uv(48,  64).size(10).build();
		public static final TextureData ENTITY_CONTAINERS_INACTIVE = ATLAS.toBuilder().uv(32,  64).size(10).build();

		public static final TextureData LOOTR_ALL      = ATLAS.toBuilder().uv( 64,  64).size(8).build();
		public static final TextureData LOOTR_CLOSED   = ATLAS.toBuilder().uv( 80,  64).size(8).build();
		public static final TextureData LOOTR_INACTIVE = ATLAS.toBuilder().uv( 96,  64).size(8).build();
		public static final TextureData LOOTR_OPENED   = ATLAS.toBuilder().uv(112,  64).size(8).build();

		private SearchMode() {}
	}

	// -------------------------------------------------------------------------
	// Popup backgrounds
	// -------------------------------------------------------------------------
	public static final class Popup {

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

		private Popup() {}
	}

	// -------------------------------------------------------------------------
	// Tab textures
	// -------------------------------------------------------------------------
	public static final class Tabs {

		public static final TextureData PAGE_TAB_ACTIVE = new TextureData.Builder()
				.location("tab/page_tab").sourceSize(26, 48).size(26, 24).build();

		public static final TextureData PAGE_TAB_INACTIVE = new TextureData.Builder()
				.location("tab/page_tab").uv(0, 24).sourceSize(26, 48).size(26, 24).build();

		public static final TextureData WARNING_TAB = new TextureData.Builder()
				.location("tab/warning_tab").size(22).build();

		private Tabs() {}
	}

	private TCTextures() {}

}
