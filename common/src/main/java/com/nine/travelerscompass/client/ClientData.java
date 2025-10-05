package com.nine.travelerscompass.client;

import com.nine.travelerscompass.client.hud.HudData;
import com.nine.travelerscompass.client.utils.ButtonTextures;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.client.utils.TextureData;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientData {

    public static final Map<UUID, HudData> HUD_DATA_CACHE = new HashMap<>();
    public static final Map<UUID, SearchProgress> PROGRESS_DATA_CACHE = new HashMap<>();

    public static final TextureData BUTTON = new TextureData.Builder().location("button/button").size(14, 14).uv(0, 0).sourceSize(14, 70).build();
    public static final TextureData BUTTON_HOVERED = new TextureData.Builder().location("button/button").size(14, 14).uv(0, 14).sourceSize(14, 70).build();
    public static final TextureData BUTTON_ACTIVE = new TextureData.Builder().location("button/button").size(14, 14).uv(0, 28).sourceSize(14, 70).build();
    public static final TextureData BUTTON_ACTIVE_HOVERED = new TextureData.Builder().location("button/button").uv(0, 42).size(14, 14).sourceSize(14, 70).build();
    public static final TextureData BUTTON_DISABLED = new TextureData.Builder().location("button/button").size(14, 14).uv(0, 56).sourceSize(14, 70).build();

    public static final TextureData SMALL_BUTTON = new TextureData.Builder().location("button/small_button").size(10, 10).uv(0, 0).sourceSize(10, 50).build();
    public static final TextureData SMALL_BUTTON_HOVERED = new TextureData.Builder().location("button/small_button").size(10, 10).uv(0, 10).sourceSize(10, 50).build();
    public static final TextureData SMALL_BUTTON_ACTIVE = new TextureData.Builder().location("button/small_button").size(10, 10).uv(0, 20).sourceSize(10, 50).build();
    public static final TextureData SMALL_BUTTON_ACTIVE_HOVERED = new TextureData.Builder().location("button/small_button").size(10, 10).uv(0, 30).sourceSize(10, 50).build();
    public static final TextureData SMALL_BUTTON_DISABLED = new TextureData.Builder().location("button/small_button").size(10, 10).uv(0, 40).sourceSize(10, 50).build();

    public static final TextureData POPUP_1X2 = new TextureData.Builder().location("popup/background_1x2").size(18, 29).build();
    public static final TextureData POPUP_2X1 = new TextureData.Builder().location("popup/background_2x1").size(29, 18).build();
    public static final TextureData POPUP_3X1 = new TextureData.Builder().location("popup/background_3x1").size(40, 18).build();
    public static final TextureData POPUP_3X3 = new TextureData.Builder().location("popup/background_3x3").size(40, 40).build();
    public static final TextureData CONNECTOR_HORIZONTAL = new TextureData.Builder().location("popup/connector_horizontal").size(12, 1).build();
    public static final TextureData CONNECTOR_VERTICAL = new TextureData.Builder().location("popup/connector_vertical").size(1, 12).build();

    public static final TextureData LOCK = new TextureData.Builder().location("icon/lock").size(8, 10).build();
    public static final TextureData LOCK_SMALL = new TextureData.Builder().location("icon/lock_small").size(6, 8).build();

    public static final TextureData MOBS_ACTIVE = new TextureData.Builder().location("icon/search_mode/mobs_search").renderSize(10).size(8).sourceSize(8, 16).build();
    public static final TextureData MOBS_INACTIVE = new TextureData.Builder().location("icon/search_mode/mobs_search").renderSize(10).size(8).uv(0, 8).sourceSize(8, 16).build();

    public static final TextureData BLOCKS_ACTIVE = new TextureData.Builder().location("icon/search_mode/blocks_search").renderSize(10).size(8).sourceSize(8, 16).build();
    public static final TextureData BLOCKS_INACTIVE = new TextureData.Builder().location("icon/search_mode/blocks_search").renderSize(10).size(8).uv(0, 8).sourceSize(8, 16).build();

    public static final TextureData FLUIDS_ACTIVE = new TextureData.Builder().location("icon/search_mode/fluids_search_active").renderSize(10).size(8).sourceSize(8, 248).build();
    public static final TextureData FLUIDS_INACTIVE = new TextureData.Builder().location("icon/search_mode/fluids_search_inactive").renderSize(10).size(8).sourceSize(8, 248).build();

    public static final TextureData ITEM_ENTITIES_ACTIVE = new TextureData.Builder().location("icon/search_mode/item_entities_search").size(11).sourceSize(11, 22).build();
    public static final TextureData ITEM_ENTITIES_INACTIVE = new TextureData.Builder().location("icon/search_mode/item_entities_search").size(11).uv(0, 11).sourceSize(11, 22).build();

    public static final TextureData SPAWNERS_ACTIVE = new TextureData.Builder().location("icon/search_mode/spawners_search").renderSize(10).size(10).sourceSize(10, 20).build();
    public static final TextureData SPAWNERS_INACTIVE = new TextureData.Builder().location("icon/search_mode/spawners_search").renderSize(10).size(10).uv(0, 10).sourceSize(10, 20).build();

    public static final TextureData VILLAGERS_ACTIVE = new TextureData.Builder().location("icon/search_mode/villagers/villagers_search").size(12).sourceSize(12, 24).build();
    public static final TextureData VILLAGERS_INACTIVE = VILLAGERS_ACTIVE.toBuilder().uv(0, 12).build();

    public static final TextureData DROPS_ACTIVE = new TextureData.Builder().location("icon/search_mode/drops_search").size(11).sourceSize(11, 22).build();
    public static final TextureData DROPS_INACTIVE = new TextureData.Builder().location("icon/search_mode/drops_search").size(11).uv(0, 11).sourceSize(11, 22).build();

    public static final TextureData INVENTORIES_ACTIVE = new TextureData.Builder().location("icon/search_mode/inventories/inventories_search").size(10, 12).sourceSize(10, 24).build();
    public static final TextureData INVENTORIES_INACTIVE = new TextureData.Builder().location("icon/search_mode/inventories/inventories_search").size(10, 12).uv(0, 12).sourceSize(10, 24).build();


    public static final TextureData CONTAINERS_ACTIVE = new TextureData.Builder().location("icon/search_mode/containers/containers_search").renderSize(10).size(8).sourceSize(8, 16).build();
    public static final TextureData CONTAINERS_INACTIVE = new TextureData.Builder().location("icon/search_mode/containers/containers_search").renderSize(10).size(8).uv(0, 8).sourceSize(8, 16).build();

    public static final TextureData BLOCK_CONTAINERS_ACTIVE = new TextureData.Builder().location("icon/search_mode/containers/block_containers_search").size(8).sourceSize(8, 16).build();
    public static final TextureData BLOCK_CONTAINERS_INACTIVE = new TextureData.Builder().location("icon/search_mode/containers/block_containers_search").size(8).uv(0, 8).sourceSize(8, 16).build();

    public static final TextureData MINECARTS_ACTIVE = new TextureData.Builder().location("icon/search_mode/containers/minecarts_search").size(10).sourceSize(10, 20).build();
    public static final TextureData MINECARTS_INACTIVE = new TextureData.Builder().location("icon/search_mode/containers/minecarts_search").size(10).uv(0, 10).sourceSize(10, 20).build();

    public static final TextureData LOOTR_ALL = new TextureData.Builder().location("icon/search_mode/containers/lootr_containers_search").size(8).uv(0, 0).sourceSize(8, 32).build();
    public static final TextureData LOOTR_CLOSED = new TextureData.Builder().location("icon/search_mode/containers/lootr_containers_search").size(8).uv(0, 8).sourceSize(8, 32).build();
    public static final TextureData LOOTR_INACTIVE = new TextureData.Builder().location("icon/search_mode/containers/lootr_containers_search").size(8).uv(0, 16).sourceSize(8, 32).build();
    public static final TextureData LOOTR_OPENED = new TextureData.Builder().location("icon/search_mode/containers/lootr_containers_search").size(8).uv(0, 24).sourceSize(8, 32).build();

    public static final TextureData WIDE_SEARCH_ICON = new TextureData.Builder().location("icon/wide_search/wide_search").size(10, 11).sourceSize(10, 22).build();
    public static final TextureData WIDE_SEARCH_HOVERED = new TextureData.Builder().location("icon/wide_search/wide_search").size(10, 11).uv(0, 11).sourceSize(10, 22).build();

    public static final TextureData VILLAGERS_BUYS_ACTIVE = new TextureData.Builder().location("icon/search_mode/villagers/villagers_buys_search").size(8, 9).sourceSize(8, 18).build();
    public static final TextureData VILLAGERS_BUYS_INACTIVE = VILLAGERS_BUYS_ACTIVE.toBuilder().uv(0, 9).build();
    public static final TextureData VILLAGERS_SELLS_ACTIVE = new TextureData.Builder().location("icon/search_mode/villagers/villagers_sells_search").size(10).sourceSize(10, 20).build();
    public static final TextureData VILLAGERS_SELLS_INACTIVE = VILLAGERS_SELLS_ACTIVE.toBuilder().uv(0, 10).build();

    public static final TextureData PLAYERS_INV_ACTIVE = new TextureData.Builder().location("icon/search_mode/inventories/players_inv_search").size(8).sourceSize(8, 16).build();
    public static final TextureData PLAYERS_INV_INACTIVE = new TextureData.Builder().location("icon/search_mode/inventories/players_inv_search").size(8).uv(0, 8).sourceSize(8, 16).build();
    public static final TextureData MOBS_INV_ACTIVE = new TextureData.Builder().location("icon/search_mode/inventories/mobs_inv_search").size(8).sourceSize(8, 16).build();
    public static final TextureData MOBS_INV_INACTIVE = new TextureData.Builder().location("icon/search_mode/inventories/mobs_inv_search").size(8).uv(0, 8).sourceSize(8, 16).build();

    public static final TextureData SOUND_PING_ACTIVE = new TextureData.Builder().location("icon/sound_ping").size(8).sourceSize(8, 32).build();
    public static final TextureData SOUND_PING_ACTIVE_HOVERED = SOUND_PING_ACTIVE.toBuilder().uv(0, 8).build();
    public static final TextureData SOUND_PING_INACTIVE  = SOUND_PING_ACTIVE.toBuilder().uv(0, 16).build();
    public static final TextureData SOUND_PING_INACTIVE_HOVERED  = SOUND_PING_ACTIVE.toBuilder().uv(0, 24).build();

    public static final TextureData ATTITUDE_MARKER_ACTIVE = new TextureData.Builder().location("icon/height_marker").size(10).sourceSize(10, 40).build();
    public static final TextureData ATTITUDE_MARKER_ACTIVE_HOVERED = ATTITUDE_MARKER_ACTIVE.toBuilder().uv(0, 10).build();
    public static final TextureData ATTITUDE_MARKER_INACTIVE = ATTITUDE_MARKER_ACTIVE.toBuilder().uv(0, 20).build();
    public static final TextureData ATTITUDE_MARKER_INACTIVE_HOVERED = ATTITUDE_MARKER_ACTIVE.toBuilder().uv(0, 30).build();

    public static final TextureData PLUS = new TextureData.Builder().location("icon/plus_minus").size(10).sourceSize(32, 30).build();
    public static final TextureData PLUS_HOVERED = PLUS.toBuilder().uv(0, 10).build();
    public static final TextureData PLUS_INACTIVE = PLUS.toBuilder().uv(0, 20).build();
    public static final TextureData MINUS = PLUS.toBuilder().uv(10, 0).build();
    public static final TextureData MINUS_HOVERED = PLUS.toBuilder().uv(10, 10).build();
    public static final TextureData MINUS_INACTIVE = PLUS.toBuilder().uv(10, 20).build();

    public static final TextureData PLUS_SMALL = new TextureData.Builder().location("icon/plus_minus").uv(20, 0).size(6).sourceSize(32, 30).build();
    public static final TextureData PLUS_SMALL_HOVERED = PLUS_SMALL.toBuilder().uv(20, 6).build();
    public static final TextureData PLUS_SMALL_INACTIVE = PLUS_SMALL.toBuilder().uv(20, 12).build();
    public static final TextureData MINUS_SMALL = PLUS_SMALL.toBuilder().uv(26, 0).build();
    public static final TextureData MINUS_SMALL_HOVERED = PLUS_SMALL.toBuilder().uv(26, 6).build();
    public static final TextureData MINUS_SMALL_INACTIVE = PLUS_SMALL.toBuilder().uv(26, 12).build();

    public static final TextureData TAB_PAGE_ACTIVE = new TextureData.Builder().location("page_tab").size(26, 24).sourceSize(26, 48).build();
    public static final TextureData TAB_PAGE_INACTIVE = TAB_PAGE_ACTIVE.toBuilder().uv(0, 24).build();
    public static final TextureData SEARCH = new TextureData.Builder().location("icon/search").size(16).build();
    public static final TextureData SETTINGS = new TextureData.Builder().location("icon/settings").size(16).build();

    public static final TextureData INFO = new TextureData.Builder().location("icon/info").size(6, 8).sourceSize(6, 16).build();
    public static final TextureData INFO_HOVERED = INFO.toBuilder().uv(0, 8).build();

    public static final TextureData PAUSE = new TextureData.Builder().location("icon/pause").size(10).sourceSize(10, 20).build();
    public static final TextureData PAUSE_HOVERED = PAUSE.toBuilder().uv(0, 10).build();
    public static final TextureData RESUME = new TextureData.Builder().location("icon/resume").size(10).sourceSize(10, 20).build();
    public static final TextureData RESUME_HOVERED = RESUME.toBuilder().uv(0, 10).build();


    //HUD ICONS
    public static final TextureData HUD_ICONS = new TextureData.Builder().location("icon/hud_icons").sourceSize(64, 64).build();

    public static final TextureData HUD_HAND = HUD_ICONS.toBuilder().uv(0, 0).size(10).build();
    public static final TextureData HUD_HAND_HOVERED = HUD_ICONS.toBuilder().uv(10, 0).size(10).build();
    public static final TextureData HUD_ALWAYS = HUD_ICONS.toBuilder().uv(0, 10).size(10).build();
    public static final TextureData HUD_ALWAYS_HOVERED = HUD_ICONS.toBuilder().uv(10, 10).size(10).build();
    public static final TextureData HUD_OFF = HUD_ICONS.toBuilder().uv(0, 20).size(10).build();
    public static final TextureData HUD_OFF_HOVERED = HUD_ICONS.toBuilder().uv(10, 20).size(10).build();
    public static final TextureData HUD_WIDTH = HUD_ICONS.toBuilder().uv(48, 8).size(6, 7).build();
    public static final TextureData HUD_HEIGHT = HUD_ICONS.toBuilder().uv(56, 8).size(5, 7).build();
    public static final TextureData HUD_SCALE = HUD_ICONS.toBuilder().uv(48, 16).size(7, 7).build();
    public static final TextureData HUD_X_POS = HUD_ICONS.toBuilder().uv(48, 0).size(6, 7).build();
    public static final TextureData HUD_Y_POS = HUD_ICONS.toBuilder().uv(56, 0).size(6, 7).build();
    public static final TextureData HUD_RESET = HUD_ICONS.toBuilder().uv(16, 48).size(5, 7).build();
    public static final TextureData HUD_RESET_HOVERED = HUD_ICONS.toBuilder().uv(16, 55).size(5, 7).build();
    public static final TextureData HUD_ALIGNMENT_CENTER = HUD_ICONS.toBuilder().uv(0, 32).size(4).build();
    public static final TextureData HUD_ALIGNMENT_CENTER_HOVERED = HUD_ICONS.toBuilder().uv(0, 36).size(4).build();
    public static final TextureData HUD_ALIGNMENT_LEFT = HUD_ICONS.toBuilder().uv(8, 32).size(4).build();
    public static final TextureData HUD_ALIGNMENT_LEFT_HOVERED = HUD_ICONS.toBuilder().uv(8, 36).size(4).build();
    public static final TextureData HUD_ALIGNMENT_RIGHT = HUD_ICONS.toBuilder().uv(4, 32).size(4).build();
    public static final TextureData HUD_ALIGNMENT_RIGHT_HOVERED = HUD_ICONS.toBuilder().uv(4, 36).size(4).build();
    public static final TextureData HUD_TYPE_EXTENDED = HUD_ICONS.toBuilder().uv(5, 48).size(5, 6).build();
    public static final TextureData HUD_TYPE_EXTENDED_HOVERED = HUD_ICONS.toBuilder().uv(5, 54).size(5, 6).build();
    public static final TextureData HUD_TYPE_COMPACT = HUD_ICONS.toBuilder().uv(0, 48).size(5, 4).build();
    public static final TextureData HUD_TYPE_COMPACT_HOVERED = HUD_ICONS.toBuilder().uv(0, 52).size(5, 4).build();
    public static final TextureData HUD_CHAT_ACTIVE = HUD_ICONS.toBuilder().uv(32, 16).size(8).build();
    public static final TextureData HUD_CHAT_ACTIVE_HOVERED = HUD_ICONS.toBuilder().uv(32, 24).size(8).build();
    public static final TextureData HUD_CHAT_INACTIVE= HUD_ICONS.toBuilder().uv(32, 0).size(8).build();
    public static final TextureData HUD_CHAT_INACTIVE_HOVERED = HUD_ICONS.toBuilder().uv(32, 8).size(8).build();


    //SETTINGS
    public static final TextureData SETTINGS_ICONS = new TextureData.Builder().location("icon/settings_icons").sourceSize(128, 128).build();

    public static final TextureData RESET = SETTINGS_ICONS.toBuilder().uv(16, 0).renderSize(9).size(8).build();
    public static final TextureData RESET_HOVERED = RESET.toBuilder().uv(16, 8).build();

    public static final TextureData CHUNKS_RANGE = SETTINGS_ICONS.toBuilder().size(10).build();
    public static final TextureData ENTITIES_RANGE = SETTINGS_ICONS.toBuilder().uv(0, 10).size(10).build();
    public static final TextureData WIDE_SEARCH_RANGE = SETTINGS_ICONS.toBuilder().uv(0, 20).size(10).build();

    public static final TextureData PRIORITY_NORMAL = SETTINGS_ICONS.toBuilder().uv(0, 30).size(10).build();
    public static final TextureData PRIORITY_NORMAL_HOVERED = SETTINGS_ICONS.toBuilder().uv(10, 30).size(10).build();
    public static final TextureData PRIORITY_OFF = SETTINGS_ICONS.toBuilder().uv(0, 40).size(10).build();
    public static final TextureData PRIORITY_OFF_HOVERED = SETTINGS_ICONS.toBuilder().uv(10, 40).size(10).build();
    public static final TextureData PRIORITY_INVERT = SETTINGS_ICONS.toBuilder().uv(0, 50).size(10).build();
    public static final TextureData PRIORITY_INVERT_HOVERED = SETTINGS_ICONS.toBuilder().uv(10, 50).size(10).build();

    public static final TextureData FORCE_LOAD_ACTIVE = SETTINGS_ICONS.toBuilder().uv(32, 0).renderSize(10).size(11).build();
    public static final TextureData FORCE_LOAD_ACTIVE_HOVERED = SETTINGS_ICONS.toBuilder().uv(43, 0).renderSize(10).size(11).build();
    public static final TextureData FORCE_LOAD_INACTIVE = SETTINGS_ICONS.toBuilder().uv(32, 11).renderSize(10).size(11).build();
    public static final TextureData FORCE_LOAD_INACTIVE_HOVERED = SETTINGS_ICONS.toBuilder().uv(43, 11).renderSize(10).size(11).build();

    public static final TextureData TARGET_VALIDATION_ACTIVE = SETTINGS_ICONS.toBuilder().uv(0, 60).renderSize(8, 10).size(16, 20).build();
    public static final TextureData TARGET_VALIDATION_ACTIVE_HOVERED = SETTINGS_ICONS.toBuilder().uv(16, 60).renderSize(8, 10).size(16, 20).build();
    public static final TextureData TARGET_VALIDATION_INACTIVE = SETTINGS_ICONS.toBuilder().uv(0, 80).renderSize(8, 10).size(16, 20).build();
    public static final TextureData TARGET_VALIDATION_INACTIVE_HOVERED = SETTINGS_ICONS.toBuilder().uv(16, 80).renderSize(8, 10).size(16, 20).build();

    public static final TextureData WARNING_SIGN = SETTINGS_ICONS.toBuilder().uv(0, 100).size(22, 22).build();




    public static final ButtonTextures TOGGLE_BUTTON = new ButtonTextures(BUTTON, BUTTON_HOVERED, BUTTON_ACTIVE, BUTTON_ACTIVE_HOVERED, BUTTON_DISABLED);
    public static final ButtonTextures SMALL_TOGGLE_BUTTON = new ButtonTextures(SMALL_BUTTON, SMALL_BUTTON_HOVERED, SMALL_BUTTON_ACTIVE, SMALL_BUTTON_ACTIVE_HOVERED, SMALL_BUTTON_DISABLED);


    //todo: move icons here
    public static final IconTexture PRIORITY_NORMAL_ICON = new IconTexture(PRIORITY_NORMAL, 2, 2);
    public static final IconTexture PRIORITY_NORMAL_HOVERED_ICON = new IconTexture(PRIORITY_NORMAL_HOVERED, 2, 2);
    public static final IconTexture PRIORITY_OFF_ICON = new IconTexture(PRIORITY_OFF, 2, 2);
    public static final IconTexture PRIORITY_OFF_HOVERED_ICON = new IconTexture(PRIORITY_OFF_HOVERED, 2, 2);
    public static final IconTexture PRIORITY_INVERT_ICON = new IconTexture(PRIORITY_INVERT, 2, 2);
    public static final IconTexture PRIORITY_INVERT_HOVERED_ICON = new IconTexture(PRIORITY_INVERT_HOVERED, 2, 2);

}
