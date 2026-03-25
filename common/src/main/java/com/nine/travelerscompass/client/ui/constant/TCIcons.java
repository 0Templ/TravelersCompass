package com.nine.travelerscompass.client.ui.constant;

import com.nine.travelerscompass.client.utils.Icon;

public final class TCIcons {

	// -------------------------------------------------------------------------
	// Common
	// -------------------------------------------------------------------------
	public static final class Common {

		public static final Icon LOCK       = Icon.of(TCTextures.Common.LOCK,       3, 2);
		public static final Icon SMALL_LOCK = Icon.of(TCTextures.Common.SMALL_LOCK, 2, 1);

		public static final Icon INFO         = Icon.of(TCTextures.Common.INFO,         4, 3);
		public static final Icon INFO_HOVERED = Icon.of(TCTextures.Common.INFO_HOVERED, 4, 3);

		public static final Icon PAUSE         = Icon.of(TCTextures.Common.PAUSE,         3,    3);
		public static final Icon PAUSE_HOVERED = Icon.of(TCTextures.Common.PAUSE_HOVERED, 3,    3);
		public static final Icon RESUME         = Icon.of(TCTextures.Common.RESUME,         5, 2.5F, 1.25f);
		public static final Icon RESUME_HOVERED = Icon.of(TCTextures.Common.RESUME_HOVERED, 5, 2.5F, 1.25f);

		public static final Icon PLUS          = Icon.of(TCTextures.Common.PLUS,         3, 3);
		public static final Icon PLUS_HOVERED  = Icon.of(TCTextures.Common.PLUS_HOVERED, 3, 3);
		public static final Icon PLUS_INACTIVE = Icon.of(TCTextures.Common.PLUS_INACTIVE,3, 3);

		public static final Icon MINUS          = Icon.of(TCTextures.Common.MINUS,          3, 6);
		public static final Icon MINUS_HOVERED  = Icon.of(TCTextures.Common.MINUS_HOVERED,  3, 6);
		public static final Icon MINUS_INACTIVE = Icon.of(TCTextures.Common.MINUS_INACTIVE, 3, 6);

		public static final Icon PLUS_SMALL          = Icon.of(TCTextures.Common.PLUS_SMALL,          2F, 2F, 1.15f);
		public static final Icon PLUS_SMALL_HOVERED  = Icon.of(TCTextures.Common.PLUS_SMALL_HOVERED,  2F, 2F, 1.15f);
		public static final Icon PLUS_SMALL_INACTIVE = Icon.of(TCTextures.Common.PLUS_SMALL_INACTIVE, 2F, 2F, 1.15f);

		public static final Icon MINUS_SMALL          = Icon.of(TCTextures.Common.MINUS_SMALL,          2F, 2F, 1.15f);
		public static final Icon MINUS_SMALL_HOVERED  = Icon.of(TCTextures.Common.MINUS_SMALL_HOVERED,  2F, 2F, 1.15f);
		public static final Icon MINUS_SMALL_INACTIVE = Icon.of(TCTextures.Common.MINUS_SMALL_INACTIVE, 2F, 2F, 1.15f);

		public static final Icon SEARCH   = Icon.of(TCTextures.Common.SEARCH);
		public static final Icon SETTINGS = Icon.of(TCTextures.Common.SETTINGS);

		public static final Icon WIDE_SEARCH         = Icon.of(TCTextures.Common.WIDE_SEARCH,         2, 2);
		public static final Icon WIDE_SEARCH_HOVERED = Icon.of(TCTextures.Common.WIDE_SEARCH_HOVERED, 2, 2);

		private Common() {}
	}

	// -------------------------------------------------------------------------
	// Settings
	// -------------------------------------------------------------------------
	public static final class Settings {

		public static final Icon WARNING_SIGN = Icon.of(TCTextures.Settings.WARNING_SIGN, 3, 3);

		public static final Icon RESET         = Icon.of(TCTextures.Settings.RESET,         3.5F, 3.5F);
		public static final Icon RESET_HOVERED = Icon.of(TCTextures.Settings.RESET_HOVERED, 3.5F, 3.5F);

		public static final Icon SOUND_PING_ACTIVE           = Icon.of(TCTextures.Settings.SOUND_PING_ACTIVE,           3, 3.5F);
		public static final Icon SOUND_PING_ACTIVE_HOVERED   = Icon.of(TCTextures.Settings.SOUND_PING_ACTIVE_HOVERED,   3, 3.5F);
		public static final Icon SOUND_PING_INACTIVE         = Icon.of(TCTextures.Settings.SOUND_PING_INACTIVE,         3, 4.5F);
		public static final Icon SOUND_PING_INACTIVE_HOVERED = Icon.of(TCTextures.Settings.SOUND_PING_INACTIVE_HOVERED, 3, 4.5F);

		public static final Icon HEIGHT_MARKER_ACTIVE           = Icon.of(TCTextures.Settings.HEIGHT_MARKER_ACTIVE,           2, 2);
		public static final Icon HEIGHT_MARKER_ACTIVE_HOVERED   = Icon.of(TCTextures.Settings.HEIGHT_MARKER_ACTIVE_HOVERED,   2, 2);
		public static final Icon HEIGHT_MARKER_INACTIVE         = Icon.of(TCTextures.Settings.HEIGHT_MARKER_INACTIVE,         2, 2);
		public static final Icon HEIGHT_MARKER_INACTIVE_HOVERED = Icon.of(TCTextures.Settings.HEIGHT_MARKER_INACTIVE_HOVERED, 2, 2);

		public static final Icon CHUNKS_RANGE      = Icon.of(TCTextures.Settings.CHUNKS_RANGE,      2, 2);
		public static final Icon ENTITIES_RANGE    = Icon.of(TCTextures.Settings.ENTITIES_RANGE,    2, 2);
		public static final Icon WIDE_SEARCH_RANGE = Icon.of(TCTextures.Settings.WIDE_SEARCH_RANGE, 2, 2);

		public static final Icon PRIORITY_NORMAL          = Icon.of(TCTextures.Settings.PRIORITY_NORMAL,          2, 2);
		public static final Icon PRIORITY_NORMAL_HOVERED  = Icon.of(TCTextures.Settings.PRIORITY_NORMAL_HOVERED,  2, 2);
		public static final Icon PRIORITY_OFF             = Icon.of(TCTextures.Settings.PRIORITY_OFF,             2, 2);
		public static final Icon PRIORITY_OFF_HOVERED     = Icon.of(TCTextures.Settings.PRIORITY_OFF_HOVERED,     2, 2);
		public static final Icon PRIORITY_INVERT         = Icon.of(TCTextures.Settings.PRIORITY_INVERT,           2, 2);
		public static final Icon PRIORITY_INVERT_HOVERED = Icon.of(TCTextures.Settings.PRIORITY_INVERT_HOVERED,   2, 2);

		public static final Icon FORCE_LOAD_ACTIVE           = Icon.of(TCTextures.Settings.FORCE_LOAD_ACTIVE,           2, 2, 0.9F);
		public static final Icon FORCE_LOAD_ACTIVE_HOVERED   = Icon.of(TCTextures.Settings.FORCE_LOAD_ACTIVE_HOVERED,   2, 2, 0.9F);
		public static final Icon FORCE_LOAD_INACTIVE         = Icon.of(TCTextures.Settings.FORCE_LOAD_INACTIVE,         2, 2, 0.9F);
		public static final Icon FORCE_LOAD_INACTIVE_HOVERED = Icon.of(TCTextures.Settings.FORCE_LOAD_INACTIVE_HOVERED, 2, 2, 0.9F);

		public static final Icon TARGET_VALIDATION_ACTIVE           = Icon.of(TCTextures.Settings.TARGET_VALIDATION_ACTIVE,           3, 2, 0.5f);
		public static final Icon TARGET_VALIDATION_ACTIVE_HOVERED   = Icon.of(TCTextures.Settings.TARGET_VALIDATION_ACTIVE_HOVERED,   3, 2, 0.5f);
		public static final Icon TARGET_VALIDATION_INACTIVE         = Icon.of(TCTextures.Settings.TARGET_VALIDATION_INACTIVE,         3, 2, 0.5f);
		public static final Icon TARGET_VALIDATION_INACTIVE_HOVERED = Icon.of(TCTextures.Settings.TARGET_VALIDATION_INACTIVE_HOVERED, 3, 2, 0.5f);

		public static final Icon HUD_HAND         = Icon.of(TCTextures.Settings.HUD_HAND,         2, 2);
		public static final Icon HUD_HAND_HOVERED = Icon.of(TCTextures.Settings.HUD_HAND_HOVERED, 2, 2);

		public static final Icon HUD_ALWAYS         = Icon.of(TCTextures.Settings.HUD_ALWAYS,         2, 2);
		public static final Icon HUD_ALWAYS_HOVERED = Icon.of(TCTextures.Settings.HUD_ALWAYS_HOVERED, 2, 2);

		public static final Icon HUD_OFF         = Icon.of(TCTextures.Settings.HUD_OFF,         2, 2);
		public static final Icon HUD_OFF_HOVERED = Icon.of(TCTextures.Settings.HUD_OFF_HOVERED, 2, 2);

		public static final Icon HUD_X_POS  = Icon.of(TCTextures.Settings.HUD_X_POS,  2, 2);
		public static final Icon HUD_Y_POS  = Icon.of(TCTextures.Settings.HUD_Y_POS,  2, 2);
		public static final Icon HUD_WIDTH  = Icon.of(TCTextures.Settings.HUD_WIDTH,  2, 2);
		public static final Icon HUD_HEIGHT = Icon.of(TCTextures.Settings.HUD_HEIGHT, 3, 2);
		public static final Icon HUD_SCALE  = Icon.of(TCTextures.Settings.HUD_SCALE,  2, 2);

		public static final Icon HUD_ALIGNMENT_CENTER         = Icon.of(TCTextures.Settings.HUD_ALIGNMENT_CENTER,         3, 1);
		public static final Icon HUD_ALIGNMENT_CENTER_HOVERED = Icon.of(TCTextures.Settings.HUD_ALIGNMENT_CENTER_HOVERED, 3, 1);
		public static final Icon HUD_ALIGNMENT_LEFT           = Icon.of(TCTextures.Settings.HUD_ALIGNMENT_LEFT,           1, 1);
		public static final Icon HUD_ALIGNMENT_LEFT_HOVERED   = Icon.of(TCTextures.Settings.HUD_ALIGNMENT_LEFT_HOVERED,   1, 1);
		public static final Icon HUD_ALIGNMENT_RIGHT          = Icon.of(TCTextures.Settings.HUD_ALIGNMENT_RIGHT,          5, 1);
		public static final Icon HUD_ALIGNMENT_RIGHT_HOVERED  = Icon.of(TCTextures.Settings.HUD_ALIGNMENT_RIGHT_HOVERED,  5, 1);

		public static final Icon HUD_TYPE_COMPACT          = Icon.of(TCTextures.Settings.HUD_TYPE_COMPACT,          2, 2);
		public static final Icon HUD_TYPE_COMPACT_HOVERED  = Icon.of(TCTextures.Settings.HUD_TYPE_COMPACT_HOVERED,  2, 2);
		public static final Icon HUD_TYPE_EXTENDED         = Icon.of(TCTextures.Settings.HUD_TYPE_EXTENDED,         2, 2);
		public static final Icon HUD_TYPE_EXTENDED_HOVERED = Icon.of(TCTextures.Settings.HUD_TYPE_EXTENDED_HOVERED, 2, 2);

		public static final Icon HUD_CHAT_ACTIVE           = Icon.of(TCTextures.Settings.HUD_CHAT_ACTIVE,           2, 2);
		public static final Icon HUD_CHAT_ACTIVE_HOVERED   = Icon.of(TCTextures.Settings.HUD_CHAT_ACTIVE_HOVERED,   2, 2);
		public static final Icon HUD_CHAT_INACTIVE         = Icon.of(TCTextures.Settings.HUD_CHAT_INACTIVE,         1, 1);
		public static final Icon HUD_CHAT_INACTIVE_HOVERED = Icon.of(TCTextures.Settings.HUD_CHAT_INACTIVE_HOVERED, 1, 1);

		public static final Icon HUD_RESET         = Icon.of(TCTextures.Settings.HUD_RESET,         3, 2);
		public static final Icon HUD_RESET_HOVERED = Icon.of(TCTextures.Settings.HUD_RESET_HOVERED, 3, 2);

		private Settings() {}
	}

	// -------------------------------------------------------------------------
	// Search mode
	// -------------------------------------------------------------------------
	public static final class SearchMode {

		public static final Icon BLOCKS_ACTIVE   = Icon.of(TCTextures.SearchMode.BLOCKS_ACTIVE,   2, 2, 1.25F);
		public static final Icon BLOCKS_INACTIVE = Icon.of(TCTextures.SearchMode.BLOCKS_INACTIVE, 2, 2, 1.25F);

		public static final Icon MOBS_ACTIVE   = Icon.of(TCTextures.SearchMode.MOBS_ACTIVE,   2, 2, 1.25F);
		public static final Icon MOBS_INACTIVE = Icon.of(TCTextures.SearchMode.MOBS_INACTIVE, 2, 2, 1.25F);

		public static final Icon FLUIDS_ACTIVE   = Icon.of(TCTextures.SearchMode.FLUIDS_ACTIVE,   2, 2, 1.25F);
		public static final Icon FLUIDS_INACTIVE = Icon.of(TCTextures.SearchMode.FLUIDS_INACTIVE, 2, 2, 1.25F);

		public static final Icon SPAWNERS_ACTIVE   = Icon.of(TCTextures.SearchMode.SPAWNERS_ACTIVE,   2, 2);
		public static final Icon SPAWNERS_INACTIVE = Icon.of(TCTextures.SearchMode.SPAWNERS_INACTIVE, 2, 2);

		public static final Icon ITEM_ENTITIES_ACTIVE   = Icon.of(TCTextures.SearchMode.ITEM_ENTITIES_ACTIVE,   2, 2);
		public static final Icon ITEM_ENTITIES_INACTIVE = Icon.of(TCTextures.SearchMode.ITEM_ENTITIES_INACTIVE, 2, 2);

		public static final Icon DROP_ACTIVE   = Icon.of(TCTextures.SearchMode.DROP_ACTIVE,   1, 2);
		public static final Icon DROP_INACTIVE = Icon.of(TCTextures.SearchMode.DROP_INACTIVE, 1, 2);

		public static final Icon VILLAGERS_ACTIVE   = Icon.of(TCTextures.SearchMode.VILLAGERS_ACTIVE,   1, 1);
		public static final Icon VILLAGERS_INACTIVE = Icon.of(TCTextures.SearchMode.VILLAGERS_INACTIVE, 1, 1);

		public static final Icon VILLAGERS_BUYS_ACTIVE   = Icon.of(TCTextures.SearchMode.VILLAGERS_BUYS_ACTIVE,   1, 0);
		public static final Icon VILLAGERS_BUYS_INACTIVE = Icon.of(TCTextures.SearchMode.VILLAGERS_BUYS_INACTIVE, 1, 0);

		public static final Icon VILLAGERS_SELLS_ACTIVE   = Icon.of(TCTextures.SearchMode.VILLAGERS_SELLS_ACTIVE);
		public static final Icon VILLAGERS_SELLS_INACTIVE = Icon.of(TCTextures.SearchMode.VILLAGERS_SELLS_INACTIVE);

		public static final Icon INVENTORIES_ACTIVE   = Icon.of(TCTextures.SearchMode.INVENTORIES_ACTIVE,   2.05F, 1F, 1.10f);
		public static final Icon INVENTORIES_INACTIVE = Icon.of(TCTextures.SearchMode.INVENTORIES_INACTIVE, 2.05F, 1F, 1.10f);

		public static final Icon INVENTORIES_PLAYERS_ACTIVE   = Icon.of(TCTextures.SearchMode.INVENTORIES_PLAYERS_ACTIVE,   1, 1);
		public static final Icon INVENTORIES_PLAYERS_INACTIVE = Icon.of(TCTextures.SearchMode.INVENTORIES_PLAYERS_INACTIVE, 1, 1);

		public static final Icon INVENTORIES_MOBS_ACTIVE   = Icon.of(TCTextures.SearchMode.INVENTORIES_MOBS_ACTIVE,   1, 1);
		public static final Icon INVENTORIES_MOBS_INACTIVE = Icon.of(TCTextures.SearchMode.INVENTORIES_MOBS_INACTIVE, 1, 1);

		public static final Icon CONTAINERS_ACTIVE   = Icon.of(TCTextures.SearchMode.CONTAINERS_ACTIVE,   2, 2, 1.25F);
		public static final Icon CONTAINERS_INACTIVE = Icon.of(TCTextures.SearchMode.CONTAINERS_INACTIVE, 2, 2, 1.25F);

		public static final Icon BLOCK_CONTAINERS_ACTIVE   = Icon.of(TCTextures.SearchMode.BLOCK_CONTAINERS_ACTIVE,   1, 1);
		public static final Icon BLOCK_CONTAINERS_INACTIVE = Icon.of(TCTextures.SearchMode.BLOCK_CONTAINERS_INACTIVE, 1, 1);

		public static final Icon ENTITY_CONTAINERS_ACTIVE   = Icon.of(TCTextures.SearchMode.ENTITY_CONTAINERS_ACTIVE);
		public static final Icon ENTITY_CONTAINERS_INACTIVE = Icon.of(TCTextures.SearchMode.ENTITY_CONTAINERS_INACTIVE);

		public static final Icon LOOTR_ALL      = Icon.of(TCTextures.SearchMode.LOOTR_ALL,      1, 1);
		public static final Icon LOOTR_CLOSED   = Icon.of(TCTextures.SearchMode.LOOTR_CLOSED,   1, 1);
		public static final Icon LOOTR_INACTIVE = Icon.of(TCTextures.SearchMode.LOOTR_INACTIVE, 1, 1);
		public static final Icon LOOTR_OPENED   = Icon.of(TCTextures.SearchMode.LOOTR_OPENED,   1, 1);

		private SearchMode() {}
	}

	private TCIcons() {}

}
