package com.nine.travelerscompass.config;

import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.filter.FilterType;

import java.util.List;

import static com.nine.travelerscompass.platform.Platform.PLATFORM_CONFIG;

public class TCConfig {

    //Search modes
    public static final ConfigValue<Boolean> ENABLE_BLOCKS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_blocks_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate specific blocks in the world.");


    public static final ConfigValue<Boolean> ENABLE_FLUIDS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_fluids_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate fluid blocks (water, lava, etc.).");

    public static final ConfigValue<Boolean> ENABLE_CONTAINERS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_containers_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate all container types based on their contents.");


    public static final ConfigValue<Boolean> ENABLE_VILLAGERS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_villagers_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows searching for villagers based on their trades.");

    public static final ConfigValue<Boolean> ENABLE_SPAWNERS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_spawners_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate spawners by their mob type.");

    public static final ConfigValue<Boolean> ENABLE_MOBS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_mobs_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate specific mobs by their spawn eggs.");

    public static final ConfigValue<Boolean> ENABLE_INVENTORIES_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_inventories_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate Entities based on items in their inventory.");

    public static final ConfigValue<Boolean> ENABLE_PLAYERS_INVENTORIES_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_players_inventories_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate players based on items in their inventory.");

    public static final ConfigValue<Boolean> ENABLE_ITEM_ENTITIES_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_item_entities_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate dropped items on the ground.");

    public static final ConfigValue<Boolean> ENABLE_DROPS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_drops_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to locate mobs based on their potential loot drops.");


    public static final ConfigValue<Boolean> ENABLE_BLOCK_CONTAINERS_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_block_containers_search",
                    true, ConfigValue.Section.GENERAL,
                    ConfigType.COMMON, true,
                    "Allows the compass to search inside block containers (chests, furnaces, barrels, etc.).\n" +
                            "Requires 'enable_containers_search' to be enabled.");

    // Search Parameters
    public static final ConfigValue<Integer> BLOCKS_CHUNK_SEARCH_RANGE =
            PLATFORM_CONFIG.registerInt(
                    "chunk_search_range",
                    10, 0, Integer.MAX_VALUE, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, true,
                    "Maximum search radius in chunks from the player.\n" +
                            "Applies to any block scanning search mode");

    public static final ConfigValue<Integer> ENTITIES_SEARCH_RANGE =
            PLATFORM_CONFIG.registerInt(
                    "entities_search_range",
                    200, 0, Integer.MAX_VALUE, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, true,
                    "Maximum search radius in blocks from the player.\n" +
                            "Applies to any entity");

    public static final ConfigValue<Integer> WIDE_SEARCH_RANGE =
            PLATFORM_CONFIG.registerInt(
                    "wide_search_range",
                    16, 0, Integer.MAX_VALUE, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, true,
                    "Maximum search radius in chunks from the player.\n" +
                            "Applies to any block");

    public static final ConfigValue<Integer> SEARCH_INTERVAL =
            PLATFORM_CONFIG.registerInt(
                    "search_interval",
                    40, 1, Integer.MAX_VALUE, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, false,
                    "Delay in ticks between search updates (20 ticks = 1 second).");

    public static final ConfigValue<Integer> MAX_CHUNK_SCANS_PER_TICK_ON_SERVER =
            PLATFORM_CONFIG.registerInt(
                    "chunks_scans_per_tick_on_server",
                    8, 0, 199, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, false,
                    "Maximum number of chunk scan operations per tick, shared across all players on the server (or just one in singleplayer).\n" +
                            "This value has a direct impact on server performance.");

    public static final ConfigValue<Integer> MAX_CHUNK_SCANS_PER_COMPASS =
            PLATFORM_CONFIG.registerInt(
                    "chunks_scans_per_compass",
                    3, 0, 199, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, false,
                    "Maximum chunk scans per compass per search cycle.\n" +
                            "Limits individual compass performance impact.");


    public static final ConfigValue<Integer> MAX_FORCE_CHUNK_GENERATION_PER_TICK =
            PLATFORM_CONFIG.registerInt(
                    "force_chunk_generation_limit",
                    2, 0, 199, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, false,
                    "Maximum new chunks that can be generated or loaded per tick during search.\n" +
                            "Set to 0 to disable searching in ungenerated chunks.\n" +
                            "High values may cause lag spikes.");

    public static final ConfigValue<Integer> MAX_CACHED_LOCATIONS =
            PLATFORM_CONFIG.registerInt(
                    "max_cached_locations",
                    180, 0, Integer.MAX_VALUE, ConfigValue.Section.BEHAVIOR,
                    ConfigType.COMMON, false,
                    "Limits how many matching locations per compass will be stored after scanning.\n" +
                            "Only the closest or most relevant targets will be cached, based on priority and distance.");

    //Balance
    public static final ConfigValue<Boolean> SEARCH_REQUIRES_XP =
            PLATFORM_CONFIG.register(
                    "search_xp_cost",
                    false,
                    ConfigValue.Section.SEARCH_COST,
                    ConfigType.COST, true,
                    "When enabled, adding items to the compass inventory costs experience levels.");

    public static final ConfigValue<List<String>> ITEM_COSTS =
            PLATFORM_CONFIG.register(
                    "items_search_cost",
                    List.of(),
                    ConfigValue.Section.SEARCH_COST,
                    ConfigType.COST, true,
                    "XP costs for searching specific items.\n" +
                            "Format: \"modid:item=consumed_levels/required_levels\"\n" +
                            " - consumed_levels: XP levels consumed when adding the item\n" +
                            " - required_levels: minimum player level required to add the item\n" +
                            "Example: [\"minecraft:diamond_ore=4/20\", \"minecraft:ancient_debris=20/40\"]"
            );

    public static final ConfigValue<List<String>> ITEM_TAGS_COSTS =
            PLATFORM_CONFIG.register(
                    "tags_search_cost",
                    List.of(),
                    ConfigValue.Section.SEARCH_COST,
                    ConfigType.COST, true,
                    "Defines XP costs for adding items or blocks based on tags.\n" +
                            "Works for both item tags and block tags.\n" +
                            "If an item has a cost defined both in 'items_search_cost' and here, the 'items_search_cost' value takes priority.\n" +
                            "Example: [\"minecraft:planks=1/3\"]"
            );

    //Filters
    public static final ConfigValue<FilterType> MODS_FILTER_TYPE =
            PLATFORM_CONFIG.register(
                    "mods_filter_type",
                    FilterType.BLACKLIST,
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "Filter mode for mod namespaces.");

    public static final ConfigValue<List<String>> MODS_FILTER =
            PLATFORM_CONFIG.register(
                    "mods_filter",
                    List.of(),
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "List of mod namespaces to filter.\n" +
                            "Example: [\"minecraft\", \"ae2\", \"alexscaves\"]");

    public static final ConfigValue<FilterType> ITEMS_FILTER_TYPE =
            PLATFORM_CONFIG.register(
                    "items_filter_type",
                    FilterType.BLACKLIST,
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "Filter mode for specific items.");

    public static final ConfigValue<List<String>> ITEMS_FILTER =
            PLATFORM_CONFIG.register(
                    "items_filter",
                    List.of(),
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "List of item IDs to filter.\n" +
                            "Example: [\"minecraft:carrot\", \"minecraft:diamond_ore\"]");

    public static final ConfigValue<FilterType> ITEM_TAGS_FILTER_TYPE =
            PLATFORM_CONFIG.register(
                    "item_tags_filter_type",
                    FilterType.BLACKLIST,
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "Filter mode for item tags.");

    public static final ConfigValue<List<String>> ITEM_TAGS_FILTER =
            PLATFORM_CONFIG.register(
                    "item_tags_filter",
                    List.of(),
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "List of item tags to filter.\n" +
                            "Example: [\"minecraft:logs\", \"c:ores\"]");

    public static final ConfigValue<FilterType> ENTITIES_FILTER_TYPE =
            PLATFORM_CONFIG.register(
                    "entities_filter_type",
                    FilterType.BLACKLIST,
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "Filter mode for entities.");

    public static final ConfigValue<List<String>> ENTITIES_FILTER =
            PLATFORM_CONFIG.register(
                    "entities_filter",
                    List.of(),
                    ConfigValue.Section.FILTERS,
                    ConfigType.COMMON, true,
                    "List of entity IDs to filter.\n" +
                            "Example: [\"minecraft:enderman\", \"aether:valkyrie\"]");

    //HUD
    public static final ConfigValue<Boolean> ENABLE_HUD =
            PLATFORM_CONFIG.register(
                    "enable_hud",
                    true, ConfigValue.Section.HUD,
                    ConfigType.COMMON, true,
                    "Enables the compass HUD overlay.");

    public static final ConfigValue<Boolean> SHOW_COORDS =
            PLATFORM_CONFIG.register(
                    "hud_show_coords",
                    true, ConfigValue.Section.HUD,
                    ConfigType.COMMON, true,
                    "Shows target coordinates in the HUD.");

    public static final ConfigValue<Integer> DEFAULT_HUD_X_POSITION =
            PLATFORM_CONFIG.register(
                    "default_x_hud_offset",
                    0, ConfigValue.Section.HUD,
                    ConfigType.COMMON, false,
                    "Base x hud offset in px");

    public static final ConfigValue<Integer> DEFAULT_HUD_Y_POSITION =
            PLATFORM_CONFIG.register(
                    "default_y_hud_offset",
                    0, ConfigValue.Section.HUD,
                    ConfigType.COMMON, false,
                    "Base x hud offset in px");

    public static final ConfigValue<Double> DEFAULT_HUD_SCALE =
            PLATFORM_CONFIG.register(
                    "default_hud_scale",
                    1.0D, ConfigValue.Section.HUD,
                    ConfigType.COMMON, false,
                    "Default hud scale");

    //Compat
    public static final ConfigValue<Boolean> ENABLE_LOOTR_SEARCH =
            PLATFORM_CONFIG.register(
                    "enable_lootr_search",
                    true, ConfigValue.Section.COMPAT,
                    ConfigType.COMMON, true,
                    "Enables Lootr containers searching when Lootr mod is installed.");

    public static final ConfigValue<LootrSearchMode.ConfigMode> LOOTR_SEARCH =
            PLATFORM_CONFIG.register(
                    "lootr_search_mode",
                    LootrSearchMode.ConfigMode.ALL, ConfigValue.Section.COMPAT,
                    ConfigType.COMMON, true,
                    "Lootr search behavior");

    public static final ConfigValue<Boolean> JEI_COMPATIBILITY =
            PLATFORM_CONFIG.register(
                    "jei_compatibility",
                    true, ConfigValue.Section.COMPAT,
                    ConfigType.COMMON, true,
                    "Enable dragging items from the JEI panel into Compass inventory");

    public static final ConfigValue<Boolean> REI_COMPATIBILITY =
            PLATFORM_CONFIG.register(
                    "rei_compatibility",
                    true, ConfigValue.Section.COMPAT,
                    ConfigType.COMMON, true,
                    "Enable dragging items from the REI panel into Compass inventory");

    public static final ConfigValue<Boolean> EMI_COMPATIBILITY =
            PLATFORM_CONFIG.register(
                    "emi_compatibility",
                    true, ConfigValue.Section.COMPAT,
                    ConfigType.COMMON, true,
                    "Enable dragging items from the EMI panel into Compass inventory");

    public static final ConfigValue<Boolean> JADE_COMPATIBILITY =
            PLATFORM_CONFIG.register(
                    "jade_compatibility",
                    true, ConfigValue.Section.COMPAT,
                    ConfigType.COMMON,
                    "Enable displaying Block/Entity search info in the Jade info panel");


    public static void init(){}

}
