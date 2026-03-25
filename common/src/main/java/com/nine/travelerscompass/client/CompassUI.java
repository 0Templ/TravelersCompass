package com.nine.travelerscompass.client;

import com.nine.travelerscompass.client.ui.constant.TCColors;
import com.nine.travelerscompass.client.ui.constant.TCComponents;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.client.utils.ButtonTexturesSet;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.network.chat.Component;

/**
 * Facade kept for compatibility. Prefer importing from the TC* classes directly:
 * {@link TCColors}, {@link TCComponents}, {@link TCTextures}, {@link TCIcons}
 */
public final class CompassUI {

	// -------------------------------------------------------------------------
	// Components (static text/labels)
	// -------------------------------------------------------------------------
	public static final Component SHORT_INDENT      = TCComponents.SHORT_INDENT;
	public static final Component DESC_ARROW        = TCComponents.DESC_ARROW;
	public static final Component CONFIG_DISABLED   = TCComponents.CONFIG_DISABLED;
	public static final Component DISABLED          = TCComponents.DISABLED;
	public static final Component ENABLED           = TCComponents.ENABLED;
	public static final Component INVERTED          = TCComponents.INVERTED;
	public static final Component INACTIVE          = TCComponents.INACTIVE;
	public static final Component HUD_ENABLED       = TCComponents.HUD_ENABLED;
	public static final Component HUD_REQUIRES_HAND = TCComponents.HUD_REQUIRES_HAND;
	public static final Component RBM               = TCComponents.RBM;
	public static final Component FILTER_BY_ITEM_ID = TCComponents.FILTER_BY_ITEM_ID;

	// -------------------------------------------------------------------------
	// Colors
	// -------------------------------------------------------------------------
	public static final class Colors {
		public static final int SOFT_GRAY  = TCColors.SOFT_GRAY;
		public static final int GRAY       = TCColors.GRAY;
		public static final int HONEY      = TCColors.HONEY;
		public static final int SOFT_HONEY = TCColors.SOFT_HONEY;
		public static final int SOFT_GREEN = TCColors.SOFT_GREEN;
		public static final int SOFT_RED   = TCColors.SOFT_RED;
		public static final float MAX_SCALE = TCColors.MAX_SCALE;
		public static final float MIN_SCALE = TCColors.MIN_SCALE;
		private Colors() {}
	}

	// -------------------------------------------------------------------------
	// Texture / Icon facades
	// -------------------------------------------------------------------------
	public static final class CommonTextures {
		public static final TextureData LOCK              = TCTextures.Common.LOCK;
		public static final TextureData SMALL_LOCK        = TCTextures.Common.SMALL_LOCK;
		public static final TextureData INFO              = TCTextures.Common.INFO;
		public static final TextureData INFO_HOVERED      = TCTextures.Common.INFO_HOVERED;
		public static final TextureData PAUSE             = TCTextures.Common.PAUSE;
		public static final TextureData PAUSE_HOVERED     = TCTextures.Common.PAUSE_HOVERED;
		public static final TextureData RESUME            = TCTextures.Common.RESUME;
		public static final TextureData RESUME_HOVERED    = TCTextures.Common.RESUME_HOVERED;
		public static final TextureData PLUS              = TCTextures.Common.PLUS;
		public static final TextureData PLUS_HOVERED      = TCTextures.Common.PLUS_HOVERED;
		public static final TextureData PLUS_INACTIVE     = TCTextures.Common.PLUS_INACTIVE;
		public static final TextureData MINUS             = TCTextures.Common.MINUS;
		public static final TextureData MINUS_HOVERED     = TCTextures.Common.MINUS_HOVERED;
		public static final TextureData MINUS_INACTIVE    = TCTextures.Common.MINUS_INACTIVE;
		public static final TextureData PLUS_SMALL           = TCTextures.Common.PLUS_SMALL;
		public static final TextureData PLUS_SMALL_HOVERED   = TCTextures.Common.PLUS_SMALL_HOVERED;
		public static final TextureData PLUS_SMALL_INACTIVE  = TCTextures.Common.PLUS_SMALL_INACTIVE;
		public static final TextureData MINUS_SMALL          = TCTextures.Common.MINUS_SMALL;
		public static final TextureData MINUS_SMALL_HOVERED  = TCTextures.Common.MINUS_SMALL_HOVERED;
		public static final TextureData MINUS_SMALL_INACTIVE = TCTextures.Common.MINUS_SMALL_INACTIVE;
		public static final TextureData SEARCH            = TCTextures.Common.SEARCH;
		public static final TextureData SETTINGS          = TCTextures.Common.SETTINGS;
		public static final TextureData WIDE_SEARCH         = TCTextures.Common.WIDE_SEARCH;
		public static final TextureData WIDE_SEARCH_HOVERED = TCTextures.Common.WIDE_SEARCH_HOVERED;

		public static final Icon LOCK_ICON              = TCIcons.Common.LOCK;
		public static final Icon SMALL_LOCK_ICON        = TCIcons.Common.SMALL_LOCK;
		public static final Icon INFO_ICON              = TCIcons.Common.INFO;
		public static final Icon INFO_HOVERED_ICON      = TCIcons.Common.INFO_HOVERED;
		public static final Icon PAUSE_ICON             = TCIcons.Common.PAUSE;
		public static final Icon PAUSE_HOVERED_ICON     = TCIcons.Common.PAUSE_HOVERED;
		public static final Icon RESUME_ICON            = TCIcons.Common.RESUME;
		public static final Icon RESUME_HOVERED_ICON    = TCIcons.Common.RESUME_HOVERED;
		public static final Icon PLUS_ICON              = TCIcons.Common.PLUS;
		public static final Icon PLUS_HOVERED_ICON      = TCIcons.Common.PLUS_HOVERED;
		public static final Icon PLUS_INACTIVE_ICON     = TCIcons.Common.PLUS_INACTIVE;
		public static final Icon MINUS_ICON             = TCIcons.Common.MINUS;
		public static final Icon MINUS_HOVERED_ICON     = TCIcons.Common.MINUS_HOVERED;
		public static final Icon MINUS_INACTIVE_ICON    = TCIcons.Common.MINUS_INACTIVE;
		public static final Icon PLUS_SMALL_ICON          = TCIcons.Common.PLUS_SMALL;
		public static final Icon PLUS_SMALL_HOVERED_ICON  = TCIcons.Common.PLUS_SMALL_HOVERED;
		public static final Icon PLUS_SMALL_INACTIVE_ICON = TCIcons.Common.PLUS_SMALL_INACTIVE;
		public static final Icon MINUS_SMALL_ICON          = TCIcons.Common.MINUS_SMALL;
		public static final Icon MINUS_SMALL_HOVERED_ICON  = TCIcons.Common.MINUS_SMALL_HOVERED;
		public static final Icon MINUS_SMALL_INACTIVE_ICON = TCIcons.Common.MINUS_SMALL_INACTIVE;
		public static final Icon SEARCH_ICON            = TCIcons.Common.SEARCH;
		public static final Icon SETTINGS_ICON          = TCIcons.Common.SETTINGS;
		public static final Icon WIDE_SEARCH_ICON         = TCIcons.Common.WIDE_SEARCH;
		public static final Icon WIDE_SEARCH_HOVERED_ICON = TCIcons.Common.WIDE_SEARCH_HOVERED;
		private CommonTextures() {}
	}

	public static final class SettingsTextures {
		public static final TextureData WARNING_SIGN                    = TCTextures.Settings.WARNING_SIGN;
		public static final TextureData RESET                          = TCTextures.Settings.RESET;
		public static final TextureData RESET_HOVERED                  = TCTextures.Settings.RESET_HOVERED;
		public static final TextureData SOUND_PING_ACTIVE              = TCTextures.Settings.SOUND_PING_ACTIVE;
		public static final TextureData SOUND_PING_ACTIVE_HOVERED      = TCTextures.Settings.SOUND_PING_ACTIVE_HOVERED;
		public static final TextureData SOUND_PING_INACTIVE            = TCTextures.Settings.SOUND_PING_INACTIVE;
		public static final TextureData SOUND_PING_INACTIVE_HOVERED    = TCTextures.Settings.SOUND_PING_INACTIVE_HOVERED;
		public static final TextureData HEIGHT_MARKER_ACTIVE           = TCTextures.Settings.HEIGHT_MARKER_ACTIVE;
		public static final TextureData HEIGHT_MARKER_ACTIVE_HOVERED   = TCTextures.Settings.HEIGHT_MARKER_ACTIVE_HOVERED;
		public static final TextureData HEIGHT_MARKER_INACTIVE         = TCTextures.Settings.HEIGHT_MARKER_INACTIVE;
		public static final TextureData HEIGHT_MARKER_INACTIVE_HOVERED = TCTextures.Settings.HEIGHT_MARKER_INACTIVE_HOVERED;
		public static final TextureData CHUNKS_RANGE                   = TCTextures.Settings.CHUNKS_RANGE;
		public static final TextureData ENTITIES_RANGE                 = TCTextures.Settings.ENTITIES_RANGE;
		public static final TextureData WIDE_SEARCH_RANGE              = TCTextures.Settings.WIDE_SEARCH_RANGE;
		public static final TextureData PRIORITY_NORMAL                = TCTextures.Settings.PRIORITY_NORMAL;
		public static final TextureData PRIORITY_NORMAL_HOVERED        = TCTextures.Settings.PRIORITY_NORMAL_HOVERED;
		public static final TextureData PRIORITY_OFF                   = TCTextures.Settings.PRIORITY_OFF;
		public static final TextureData PRIORITY_OFF_HOVERED           = TCTextures.Settings.PRIORITY_OFF_HOVERED;
		public static final TextureData PRIORITY_INVERT                = TCTextures.Settings.PRIORITY_INVERT;
		public static final TextureData PRIORITY_INVERT_HOVERED        = TCTextures.Settings.PRIORITY_INVERT_HOVERED;
		public static final TextureData FORCE_LOAD_ACTIVE              = TCTextures.Settings.FORCE_LOAD_ACTIVE;
		public static final TextureData FORCE_LOAD_ACTIVE_HOVERED      = TCTextures.Settings.FORCE_LOAD_ACTIVE_HOVERED;
		public static final TextureData FORCE_LOAD_INACTIVE            = TCTextures.Settings.FORCE_LOAD_INACTIVE;
		public static final TextureData FORCE_LOAD_INACTIVE_HOVERED    = TCTextures.Settings.FORCE_LOAD_INACTIVE_HOVERED;
		public static final TextureData TARGET_VALIDATION_ACTIVE           = TCTextures.Settings.TARGET_VALIDATION_ACTIVE;
		public static final TextureData TARGET_VALIDATION_ACTIVE_HOVERED   = TCTextures.Settings.TARGET_VALIDATION_ACTIVE_HOVERED;
		public static final TextureData TARGET_VALIDATION_INACTIVE         = TCTextures.Settings.TARGET_VALIDATION_INACTIVE;
		public static final TextureData TARGET_VALIDATION_INACTIVE_HOVERED = TCTextures.Settings.TARGET_VALIDATION_INACTIVE_HOVERED;
		public static final TextureData HUD_HAND                       = TCTextures.Settings.HUD_HAND;
		public static final TextureData HUD_HAND_HOVERED               = TCTextures.Settings.HUD_HAND_HOVERED;
		public static final TextureData HUD_ALWAYS                     = TCTextures.Settings.HUD_ALWAYS;
		public static final TextureData HUD_ALWAYS_HOVERED             = TCTextures.Settings.HUD_ALWAYS_HOVERED;
		public static final TextureData HUD_OFF                        = TCTextures.Settings.HUD_OFF;
		public static final TextureData HUD_OFF_HOVERED                = TCTextures.Settings.HUD_OFF_HOVERED;
		public static final TextureData HUD_X_POS                      = TCTextures.Settings.HUD_X_POS;
		public static final TextureData HUD_Y_POS                      = TCTextures.Settings.HUD_Y_POS;
		public static final TextureData HUD_WIDTH                      = TCTextures.Settings.HUD_WIDTH;
		public static final TextureData HUD_HEIGHT                     = TCTextures.Settings.HUD_HEIGHT;
		public static final TextureData HUD_SCALE                      = TCTextures.Settings.HUD_SCALE;
		public static final TextureData HUD_ALIGNMENT_CENTER           = TCTextures.Settings.HUD_ALIGNMENT_CENTER;
		public static final TextureData HUD_ALIGNMENT_CENTER_HOVERED   = TCTextures.Settings.HUD_ALIGNMENT_CENTER_HOVERED;
		public static final TextureData HUD_ALIGNMENT_LEFT             = TCTextures.Settings.HUD_ALIGNMENT_LEFT;
		public static final TextureData HUD_ALIGNMENT_LEFT_HOVERED     = TCTextures.Settings.HUD_ALIGNMENT_LEFT_HOVERED;
		public static final TextureData HUD_ALIGNMENT_RIGHT            = TCTextures.Settings.HUD_ALIGNMENT_RIGHT;
		public static final TextureData HUD_ALIGNMENT_RIGHT_HOVERED    = TCTextures.Settings.HUD_ALIGNMENT_RIGHT_HOVERED;
		public static final TextureData HUD_TYPE_COMPACT               = TCTextures.Settings.HUD_TYPE_COMPACT;
		public static final TextureData HUD_TYPE_COMPACT_HOVERED       = TCTextures.Settings.HUD_TYPE_COMPACT_HOVERED;
		public static final TextureData HUD_TYPE_EXTENDED              = TCTextures.Settings.HUD_TYPE_EXTENDED;
		public static final TextureData HUD_TYPE_EXTENDED_HOVERED      = TCTextures.Settings.HUD_TYPE_EXTENDED_HOVERED;
		public static final TextureData HUD_CHAT_ACTIVE                = TCTextures.Settings.HUD_CHAT_ACTIVE;
		public static final TextureData HUD_CHAT_ACTIVE_HOVERED        = TCTextures.Settings.HUD_CHAT_ACTIVE_HOVERED;
		public static final TextureData HUD_CHAT_INACTIVE              = TCTextures.Settings.HUD_CHAT_INACTIVE;
		public static final TextureData HUD_CHAT_INACTIVE_HOVERED      = TCTextures.Settings.HUD_CHAT_INACTIVE_HOVERED;
		public static final TextureData HUD_RESET                      = TCTextures.Settings.HUD_RESET;
		public static final TextureData HUD_RESET_HOVERED              = TCTextures.Settings.HUD_RESET_HOVERED;

		public static final Icon WARNING_SIGN_ICON                     = TCIcons.Settings.WARNING_SIGN;
		public static final Icon RESET_ICON                           = TCIcons.Settings.RESET;
		public static final Icon RESET_HOVERED_ICON                   = TCIcons.Settings.RESET_HOVERED;
		public static final Icon SOUND_PING_ACTIVE_ICON               = TCIcons.Settings.SOUND_PING_ACTIVE;
		public static final Icon SOUND_PING_ACTIVE_HOVERED_ICON       = TCIcons.Settings.SOUND_PING_ACTIVE_HOVERED;
		public static final Icon SOUND_PING_INACTIVE_ICON             = TCIcons.Settings.SOUND_PING_INACTIVE;
		public static final Icon SOUND_PING_INACTIVE_HOVERED_ICON     = TCIcons.Settings.SOUND_PING_INACTIVE_HOVERED;
		public static final Icon HEIGHT_MARKER_ACTIVE_ICON            = TCIcons.Settings.HEIGHT_MARKER_ACTIVE;
		public static final Icon HEIGHT_MARKER_ACTIVE_HOVERED_ICON    = TCIcons.Settings.HEIGHT_MARKER_ACTIVE_HOVERED;
		public static final Icon HEIGHT_MARKER_INACTIVE_ICON          = TCIcons.Settings.HEIGHT_MARKER_INACTIVE;
		public static final Icon HEIGHT_MARKER_INACTIVE_HOVERED_ICON  = TCIcons.Settings.HEIGHT_MARKER_INACTIVE_HOVERED;
		public static final Icon CHUNKS_RANGE_ICON                    = TCIcons.Settings.CHUNKS_RANGE;
		public static final Icon ENTITIES_RANGE_ICON                  = TCIcons.Settings.ENTITIES_RANGE;
		public static final Icon WIDE_SEARCH_RANGE_ICON               = TCIcons.Settings.WIDE_SEARCH_RANGE;
		public static final Icon PRIORITY_NORMAL_ICON                 = TCIcons.Settings.PRIORITY_NORMAL;
		public static final Icon PRIORITY_NORMAL_HOVERED_ICON         = TCIcons.Settings.PRIORITY_NORMAL_HOVERED;
		public static final Icon PRIORITY_OFF_ICON                    = TCIcons.Settings.PRIORITY_OFF;
		public static final Icon PRIORITY_OFF_HOVERED_ICON            = TCIcons.Settings.PRIORITY_OFF_HOVERED;
		public static final Icon PRIORITY_INVERT_ICON                 = TCIcons.Settings.PRIORITY_INVERT;
		public static final Icon PRIORITY_INVERT_HOVERED_ICON         = TCIcons.Settings.PRIORITY_INVERT_HOVERED;
		public static final Icon FORCE_LOAD_ACTIVE_ICON               = TCIcons.Settings.FORCE_LOAD_ACTIVE;
		public static final Icon FORCE_LOAD_ACTIVE_HOVERED_ICON       = TCIcons.Settings.FORCE_LOAD_ACTIVE_HOVERED;
		public static final Icon FORCE_LOAD_INACTIVE_ICON             = TCIcons.Settings.FORCE_LOAD_INACTIVE;
		public static final Icon FORCE_LOAD_INACTIVE_HOVERED_ICON     = TCIcons.Settings.FORCE_LOAD_INACTIVE_HOVERED;
		public static final Icon TARGET_VALIDATION_ACTIVE_ICON            = TCIcons.Settings.TARGET_VALIDATION_ACTIVE;
		public static final Icon TARGET_VALIDATION_ACTIVE_HOVERED_ICON    = TCIcons.Settings.TARGET_VALIDATION_ACTIVE_HOVERED;
		public static final Icon TARGET_VALIDATION_INACTIVE_ICON          = TCIcons.Settings.TARGET_VALIDATION_INACTIVE;
		public static final Icon TARGET_VALIDATION_INACTIVE_HOVERED_ICON  = TCIcons.Settings.TARGET_VALIDATION_INACTIVE_HOVERED;
		public static final Icon HUD_HAND_ICON                        = TCIcons.Settings.HUD_HAND;
		public static final Icon HUD_HAND_HOVERED_ICON                = TCIcons.Settings.HUD_HAND_HOVERED;
		public static final Icon HUD_ALWAYS_ICON                      = TCIcons.Settings.HUD_ALWAYS;
		public static final Icon HUD_ALWAYS_HOVERED_ICON              = TCIcons.Settings.HUD_ALWAYS_HOVERED;
		public static final Icon HUD_OFF_ICON                         = TCIcons.Settings.HUD_OFF;
		public static final Icon HUD_OFF_HOVERED_ICON                 = TCIcons.Settings.HUD_OFF_HOVERED;
		public static final Icon HUD_X_POS_ICON                       = TCIcons.Settings.HUD_X_POS;
		public static final Icon HUD_Y_POS_ICON                       = TCIcons.Settings.HUD_Y_POS;
		public static final Icon HUD_WIDTH_ICON                       = TCIcons.Settings.HUD_WIDTH;
		public static final Icon HUD_HEIGHT_ICON                      = TCIcons.Settings.HUD_HEIGHT;
		public static final Icon HUD_SCALE_ICON                       = TCIcons.Settings.HUD_SCALE;
		public static final Icon HUD_ALIGNMENT_CENTER_ICON            = TCIcons.Settings.HUD_ALIGNMENT_CENTER;
		public static final Icon HUD_ALIGNMENT_CENTER_HOVERED_ICON    = TCIcons.Settings.HUD_ALIGNMENT_CENTER_HOVERED;
		public static final Icon HUD_ALIGNMENT_LEFT_ICON              = TCIcons.Settings.HUD_ALIGNMENT_LEFT;
		public static final Icon HUD_ALIGNMENT_LEFT_HOVERED_ICON      = TCIcons.Settings.HUD_ALIGNMENT_LEFT_HOVERED;
		public static final Icon HUD_ALIGNMENT_RIGHT_ICON             = TCIcons.Settings.HUD_ALIGNMENT_RIGHT;
		public static final Icon HUD_ALIGNMENT_RIGHT_HOVERED_ICON     = TCIcons.Settings.HUD_ALIGNMENT_RIGHT_HOVERED;
		public static final Icon HUD_TYPE_COMPACT_ICON                = TCIcons.Settings.HUD_TYPE_COMPACT;
		public static final Icon HUD_TYPE_COMPACT_HOVERED_ICON        = TCIcons.Settings.HUD_TYPE_COMPACT_HOVERED;
		public static final Icon HUD_TYPE_EXTENDED_ICON               = TCIcons.Settings.HUD_TYPE_EXTENDED;
		public static final Icon HUD_TYPE_EXTENDED_HOVERED_ICON       = TCIcons.Settings.HUD_TYPE_EXTENDED_HOVERED;
		public static final Icon HUD_CHAT_ACTIVE_ICON                 = TCIcons.Settings.HUD_CHAT_ACTIVE;
		public static final Icon HUD_CHAT_ACTIVE_HOVERED_ICON         = TCIcons.Settings.HUD_CHAT_ACTIVE_HOVERED;
		public static final Icon HUD_CHAT_INACTIVE_ICON               = TCIcons.Settings.HUD_CHAT_INACTIVE;
		public static final Icon HUD_CHAT_INACTIVE_HOVERED_ICON       = TCIcons.Settings.HUD_CHAT_INACTIVE_HOVERED;
		public static final Icon HUD_RESET_ICON                       = TCIcons.Settings.HUD_RESET;
		public static final Icon HUD_RESET_HOVERED_ICON               = TCIcons.Settings.HUD_RESET_HOVERED;
		private SettingsTextures() {}
	}

	public static final class ButtonTextures {
		public static final TextureData BUTTON               = TCTextures.Buttons.BUTTON;
		public static final TextureData BUTTON_HOVERED       = TCTextures.Buttons.BUTTON_HOVERED;
		public static final TextureData BUTTON_ACTIVE        = TCTextures.Buttons.BUTTON_ACTIVE;
		public static final TextureData BUTTON_ACTIVE_HOVERED = TCTextures.Buttons.BUTTON_ACTIVE_HOVERED;
		public static final TextureData BUTTON_DISABLED      = TCTextures.Buttons.BUTTON_DISABLED;
		public static final TextureData SMALL_BUTTON               = TCTextures.Buttons.SMALL_BUTTON;
		public static final TextureData SMALL_BUTTON_HOVERED       = TCTextures.Buttons.SMALL_BUTTON_HOVERED;
		public static final TextureData SMALL_BUTTON_ACTIVE        = TCTextures.Buttons.SMALL_BUTTON_ACTIVE;
		public static final TextureData SMALL_BUTTON_ACTIVE_HOVERED = TCTextures.Buttons.SMALL_BUTTON_ACTIVE_HOVERED;
		public static final TextureData SMALL_BUTTON_DISABLED      = TCTextures.Buttons.SMALL_BUTTON_DISABLED;
		public static final ButtonTexturesSet TOGGLE_BUTTON       = TCTextures.Buttons.TOGGLE_BUTTON;
		public static final ButtonTexturesSet SMALL_TOGGLE_BUTTON = TCTextures.Buttons.SMALL_TOGGLE_BUTTON;
		private ButtonTextures() {}
	}

	public static final class SearchModeTextures {
		public static final TextureData BLOCKS_ACTIVE   = TCTextures.SearchMode.BLOCKS_ACTIVE;
		public static final TextureData BLOCKS_INACTIVE = TCTextures.SearchMode.BLOCKS_INACTIVE;
		public static final TextureData MOBS_ACTIVE     = TCTextures.SearchMode.MOBS_ACTIVE;
		public static final TextureData MOBS_INACTIVE   = TCTextures.SearchMode.MOBS_INACTIVE;
		public static final TextureData FLUIDS_ACTIVE   = TCTextures.SearchMode.FLUIDS_ACTIVE;
		public static final TextureData FLUIDS_INACTIVE = TCTextures.SearchMode.FLUIDS_INACTIVE;
		public static final TextureData SPAWNERS_ACTIVE   = TCTextures.SearchMode.SPAWNERS_ACTIVE;
		public static final TextureData SPAWNERS_INACTIVE = TCTextures.SearchMode.SPAWNERS_INACTIVE;
		public static final TextureData ITEM_ENTITIES_ACTIVE   = TCTextures.SearchMode.ITEM_ENTITIES_ACTIVE;
		public static final TextureData ITEM_ENTITIES_INACTIVE = TCTextures.SearchMode.ITEM_ENTITIES_INACTIVE;
		public static final TextureData DROP_ACTIVE   = TCTextures.SearchMode.DROP_ACTIVE;
		public static final TextureData DROP_INACTIVE = TCTextures.SearchMode.DROP_INACTIVE;
		public static final TextureData VILLAGERS_ACTIVE        = TCTextures.SearchMode.VILLAGERS_ACTIVE;
		public static final TextureData VILLAGERS_INACTIVE      = TCTextures.SearchMode.VILLAGERS_INACTIVE;
		public static final TextureData VILLAGERS_BUYS_ACTIVE   = TCTextures.SearchMode.VILLAGERS_BUYS_ACTIVE;
		public static final TextureData VILLAGERS_BUYS_INACTIVE = TCTextures.SearchMode.VILLAGERS_BUYS_INACTIVE;
		public static final TextureData VILLAGERS_SELLS_ACTIVE   = TCTextures.SearchMode.VILLAGERS_SELLS_ACTIVE;
		public static final TextureData VILLAGERS_SELLS_INACTIVE = TCTextures.SearchMode.VILLAGERS_SELLS_INACTIVE;
		public static final TextureData INVENTORIES_ACTIVE          = TCTextures.SearchMode.INVENTORIES_ACTIVE;
		public static final TextureData INVENTORIES_INACTIVE        = TCTextures.SearchMode.INVENTORIES_INACTIVE;
		public static final TextureData INVENTORIES_PLAYERS_ACTIVE   = TCTextures.SearchMode.INVENTORIES_PLAYERS_ACTIVE;
		public static final TextureData INVENTORIES_PLAYERS_INACTIVE = TCTextures.SearchMode.INVENTORIES_PLAYERS_INACTIVE;
		public static final TextureData INVENTORIES_MOBS_ACTIVE   = TCTextures.SearchMode.INVENTORIES_MOBS_ACTIVE;
		public static final TextureData INVENTORIES_MOBS_INACTIVE = TCTextures.SearchMode.INVENTORIES_MOBS_INACTIVE;
		public static final TextureData CONTAINERS_ACTIVE          = TCTextures.SearchMode.CONTAINERS_ACTIVE;
		public static final TextureData CONTAINERS_INACTIVE        = TCTextures.SearchMode.CONTAINERS_INACTIVE;
		public static final TextureData BLOCK_CONTAINERS_ACTIVE    = TCTextures.SearchMode.BLOCK_CONTAINERS_ACTIVE;
		public static final TextureData BLOCK_CONTAINERS_INACTIVE  = TCTextures.SearchMode.BLOCK_CONTAINERS_INACTIVE;
		public static final TextureData ENTITY_CONTAINERS_ACTIVE   = TCTextures.SearchMode.ENTITY_CONTAINERS_ACTIVE;
		public static final TextureData ENTITY_CONTAINERS_INACTIVE = TCTextures.SearchMode.ENTITY_CONTAINERS_INACTIVE;
		public static final TextureData LOOTR_ALL      = TCTextures.SearchMode.LOOTR_ALL;
		public static final TextureData LOOTR_CLOSED   = TCTextures.SearchMode.LOOTR_CLOSED;
		public static final TextureData LOOTR_INACTIVE = TCTextures.SearchMode.LOOTR_INACTIVE;
		public static final TextureData LOOTR_OPENED   = TCTextures.SearchMode.LOOTR_OPENED;

		public static final Icon BLOCKS_ACTIVE_ICON   = TCIcons.SearchMode.BLOCKS_ACTIVE;
		public static final Icon BLOCKS_INACTIVE_ICON = TCIcons.SearchMode.BLOCKS_INACTIVE;
		public static final Icon MOBS_ACTIVE_ICON     = TCIcons.SearchMode.MOBS_ACTIVE;
		public static final Icon MOBS_INACTIVE_ICON   = TCIcons.SearchMode.MOBS_INACTIVE;
		public static final Icon FLUIDS_ACTIVE_ICON   = TCIcons.SearchMode.FLUIDS_ACTIVE;
		public static final Icon FLUIDS_INACTIVE_ICON = TCIcons.SearchMode.FLUIDS_INACTIVE;
		public static final Icon SPAWNERS_ACTIVE_ICON   = TCIcons.SearchMode.SPAWNERS_ACTIVE;
		public static final Icon SPAWNERS_INACTIVE_ICON = TCIcons.SearchMode.SPAWNERS_INACTIVE;
		public static final Icon ITEM_ENTITIES_ACTIVE_ICON   = TCIcons.SearchMode.ITEM_ENTITIES_ACTIVE;
		public static final Icon ITEM_ENTITIES_INACTIVE_ICON = TCIcons.SearchMode.ITEM_ENTITIES_INACTIVE;
		public static final Icon DROP_ACTIVE_ICON   = TCIcons.SearchMode.DROP_ACTIVE;
		public static final Icon DROP_INACTIVE_ICON = TCIcons.SearchMode.DROP_INACTIVE;
		public static final Icon VILLAGERS_ACTIVE_ICON        = TCIcons.SearchMode.VILLAGERS_ACTIVE;
		public static final Icon VILLAGERS_INACTIVE_ICON      = TCIcons.SearchMode.VILLAGERS_INACTIVE;
		public static final Icon VILLAGERS_BUYS_ACTIVE_ICON   = TCIcons.SearchMode.VILLAGERS_BUYS_ACTIVE;
		public static final Icon VILLAGERS_BUYS_INACTIVE_ICON = TCIcons.SearchMode.VILLAGERS_BUYS_INACTIVE;
		public static final Icon VILLAGERS_SELLS_ACTIVE_ICON   = TCIcons.SearchMode.VILLAGERS_SELLS_ACTIVE;
		public static final Icon VILLAGERS_SELLS_INACTIVE_ICON = TCIcons.SearchMode.VILLAGERS_SELLS_INACTIVE;
		public static final Icon INVENTORIES_ACTIVE_ICON          = TCIcons.SearchMode.INVENTORIES_ACTIVE;
		public static final Icon INVENTORIES_INACTIVE_ICON        = TCIcons.SearchMode.INVENTORIES_INACTIVE;
		public static final Icon INVENTORIES_PLAYERS_ACTIVE_ICON   = TCIcons.SearchMode.INVENTORIES_PLAYERS_ACTIVE;
		public static final Icon INVENTORIES_PLAYERS_INACTIVE_ICON = TCIcons.SearchMode.INVENTORIES_PLAYERS_INACTIVE;
		public static final Icon INVENTORIES_MOBS_ACTIVE_ICON   = TCIcons.SearchMode.INVENTORIES_MOBS_ACTIVE;
		public static final Icon INVENTORIES_MOBS_INACTIVE_ICON = TCIcons.SearchMode.INVENTORIES_MOBS_INACTIVE;
		public static final Icon CONTAINERS_ACTIVE_ICON          = TCIcons.SearchMode.CONTAINERS_ACTIVE;
		public static final Icon CONTAINERS_INACTIVE_ICON        = TCIcons.SearchMode.CONTAINERS_INACTIVE;
		public static final Icon BLOCK_CONTAINERS_ACTIVE_ICON    = TCIcons.SearchMode.BLOCK_CONTAINERS_ACTIVE;
		public static final Icon BLOCK_CONTAINERS_INACTIVE_ICON  = TCIcons.SearchMode.BLOCK_CONTAINERS_INACTIVE;
		public static final Icon ENTITY_CONTAINERS_ACTIVE_ICON   = TCIcons.SearchMode.ENTITY_CONTAINERS_ACTIVE;
		public static final Icon ENTITY_CONTAINERS_INACTIVE_ICON = TCIcons.SearchMode.ENTITY_CONTAINERS_INACTIVE;
		public static final Icon LOOTR_ALL_ICON      = TCIcons.SearchMode.LOOTR_ALL;
		public static final Icon LOOTR_CLOSED_ICON   = TCIcons.SearchMode.LOOTR_CLOSED;
		public static final Icon LOOTR_INACTIVE_ICON = TCIcons.SearchMode.LOOTR_INACTIVE;
		public static final Icon LOOTR_OPENED_ICON   = TCIcons.SearchMode.LOOTR_OPENED;
		private SearchModeTextures() {}
	}

	public static final class PopupTextures {
		public static final TextureData POPUP_1X2            = TCTextures.Popup.POPUP_1X2;
		public static final TextureData POPUP_2X1            = TCTextures.Popup.POPUP_2X1;
		public static final TextureData POPUP_3X1            = TCTextures.Popup.POPUP_3X1;
		public static final TextureData POPUP_3X3            = TCTextures.Popup.POPUP_3X3;
		public static final TextureData CONNECTOR_HORIZONTAL = TCTextures.Popup.CONNECTOR_HORIZONTAL;
		public static final TextureData CONNECTOR_VERTICAL   = TCTextures.Popup.CONNECTOR_VERTICAL;
		private PopupTextures() {}
	}

	public static final class TabTextures {
		public static final TextureData PAGE_TAB_ACTIVE   = TCTextures.Tabs.PAGE_TAB_ACTIVE;
		public static final TextureData PAGE_TAB_INACTIVE = TCTextures.Tabs.PAGE_TAB_INACTIVE;
		public static final TextureData WARNING_TAB       = TCTextures.Tabs.WARNING_TAB;
		private TabTextures() {}
	}

	private CompassUI() {}

}
