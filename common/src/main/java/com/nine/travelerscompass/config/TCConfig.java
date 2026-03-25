package com.nine.travelerscompass.config;

import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.filter.FilterType;
import com.nine.travelerscompass.config.option.*;

import java.util.List;

public class TCConfig {

    // Search modes
    public static final ConfigValue<Boolean> ENABLE_BLOCKS_SEARCH = ConfigImpl.register(
            "enable_blocks_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate specific blocks in the world."));

    public static final ConfigValue<Boolean> ENABLE_FLUIDS_SEARCH = ConfigImpl.register(
            "enable_fluids_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate fluid blocks (water, lava, etc.)."));

    public static final ConfigValue<Boolean> ENABLE_CONTAINERS_SEARCH = ConfigImpl.register(
            "enable_containers_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate all container types based on their contents."));

    public static final ConfigValue<Boolean> ENABLE_VILLAGERS_SEARCH = ConfigImpl.register(
            "enable_villagers_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows searching for villagers based on their trades."));

    public static final ConfigValue<Boolean> ENABLE_SPAWNERS_SEARCH = ConfigImpl.register(
            "enable_spawners_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate spawners by their mob type."));

    public static final ConfigValue<Boolean> ENABLE_MOBS_SEARCH = ConfigImpl.register(
            "enable_mobs_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate specific mobs by their spawn eggs."));

    public static final ConfigValue<Boolean> ENABLE_INVENTORIES_SEARCH = ConfigImpl.register(
            "enable_inventories_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate Entities based on items in their inventory."));

    public static final ConfigValue<Boolean> ENABLE_PLAYERS_INVENTORIES_SEARCH = ConfigImpl.register(
            "enable_players_inventories_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate players based on items in their inventory."));

    public static final ConfigValue<Boolean> ENABLE_ITEM_ENTITIES_SEARCH = ConfigImpl.register(
            "enable_item_entities_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate dropped items on the ground."));

    public static final ConfigValue<Boolean> ENABLE_DROPS_SEARCH = ConfigImpl.register(
            "enable_drops_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to locate mobs based on their potential loot drops."));

    public static final ConfigValue<Boolean> ENABLE_BLOCK_CONTAINERS_SEARCH = ConfigImpl.register(
            "enable_block_containers_search", true,
            ConfigSection.GENERAL, ConfigFlag.SYNC,
            ConfigComment.of("Allows the compass to search inside block containers (chests, furnaces, barrels, etc.).")
                    .line("Requires 'enable_containers_search' to be enabled."));

    // Search parameters
    public static final ConfigValue<Integer> BLOCKS_CHUNK_SEARCH_RANGE = ConfigImpl.register(
            "chunk_search_range", 10,
            ConfigSection.BEHAVIOR, ConfigFlag.SYNC,
            new ConfigRange<>(0, Integer.MAX_VALUE),
            ConfigComment.of("Maximum search radius in chunks from the player.")
                    .line("Applies to any block scanning search mode"));

    public static final ConfigValue<Integer> ENTITIES_SEARCH_RANGE = ConfigImpl.register(
            "entities_search_range", 200,
            ConfigSection.BEHAVIOR, ConfigFlag.SYNC,
            new ConfigRange<>(0, Integer.MAX_VALUE),
            ConfigComment.of("Maximum search radius in blocks from the player.")
                    .line("Applies to any entity"));

    public static final ConfigValue<Integer> WIDE_SEARCH_RANGE = ConfigImpl.register(
            "wide_search_range", 16,
            ConfigSection.BEHAVIOR, ConfigFlag.SYNC,
            new ConfigRange<>(0, Integer.MAX_VALUE),
            ConfigComment.of("Maximum search radius in chunks from the player.")
                    .line("Applies to any block"));

    public static final ConfigValue<Integer> SEARCH_INTERVAL = ConfigImpl.register(
            "search_interval", 40,
            ConfigSection.BEHAVIOR,
            new ConfigRange<>(1, Integer.MAX_VALUE),
            ConfigComment.of("Delay in ticks between search updates (20 ticks = 1 second)."));

    public static final ConfigValue<Integer> MAX_CHUNK_SCANS_PER_TICK_ON_SERVER = ConfigImpl.register(
            "chunks_scans_per_tick_on_server", 8,
            ConfigSection.BEHAVIOR,
            new ConfigRange<>(0, 199),
            ConfigComment.of("Maximum number of chunk scan operations per tick, shared across all players on the server (or just one in singleplayer).")
                    .line("This value has a direct impact on server performance."));

    public static final ConfigValue<Integer> MAX_CHUNK_SCANS_PER_COMPASS = ConfigImpl.register(
            "chunks_scans_per_compass", 3,
            ConfigSection.BEHAVIOR,
            new ConfigRange<>(0, 199),
            ConfigComment.of("Maximum chunk scans per compass per search cycle.")
                    .line("Limits individual compass performance impact."));

    public static final ConfigValue<Integer> MAX_FORCE_CHUNK_GENERATION_PER_TICK = ConfigImpl.register(
            "force_chunk_generation_limit", 2,
            ConfigSection.BEHAVIOR,
            new ConfigRange<>(0, 199),
            ConfigComment.of("Maximum new chunks that can be generated or loaded per tick during search.")
                    .line("Set to 0 to disable searching in ungenerated chunks.")
                    .line("High values may cause lag spikes."));

    public static final ConfigValue<Integer> MAX_CACHED_LOCATIONS = ConfigImpl.register(
            "max_cached_locations", 180,
            ConfigSection.BEHAVIOR,
            new ConfigRange<>(0, Integer.MAX_VALUE),
            ConfigComment.of("Limits how many matching locations per compass will be stored after scanning.")
                    .line("Only the closest or most relevant targets will be cached, based on priority and distance."));

    // Balance
    public static final ConfigValue<Boolean> SEARCH_REQUIRES_XP = ConfigImpl.register(
            "search_xp_cost", false,
            ConfigSection.SEARCH_COST, ConfigFlag.SYNC,
            ConfigComment.of("When enabled, adding items to the compass inventory costs experience levels."));

    public static final ConfigValue<List<String>> ITEM_COSTS = ConfigImpl.register(
            "items_search_cost", List.of(),
            ConfigSection.SEARCH_COST, ConfigFlag.SYNC,
            ConfigComment.of("XP costs for searching specific items.")
                    .line("Format: \"modid:item=consumed_levels/required_levels\"")
                    .line(" - consumed_levels: XP levels consumed when adding the item")
                    .line(" - required_levels: minimum player level required to add the item")
                    .line("Example: [\"minecraft:diamond_ore=4/20\", \"minecraft:ancient_debris=20/40\"]"));

    public static final ConfigValue<List<String>> ITEM_TAGS_COSTS = ConfigImpl.register(
            "tags_search_cost", List.of(),
            ConfigSection.SEARCH_COST, ConfigFlag.SYNC,
            ConfigComment.of("Defines XP costs for adding items or blocks based on tags.")
                    .line("Works for both item tags and block tags.")
                    .line("If an item has a cost defined both in 'items_search_cost' and here, the 'items_search_cost' value takes priority.")
                    .line("Example: [\"minecraft:planks=1/3\"]"));

    // Filters
    public static final ConfigValue<FilterType> MODS_FILTER_TYPE = ConfigImpl.register(
            "mods_filter_type", FilterType.BLACKLIST,
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("Filter mode for mod namespaces."));

    public static final ConfigValue<List<String>> MODS_FILTER = ConfigImpl.register(
            "mods_filter", List.of(),
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("List of mod namespaces to filter.")
                    .line("Example: [\"minecraft\", \"ae2\", \"alexscaves\"]"));

    public static final ConfigValue<FilterType> ITEMS_FILTER_TYPE = ConfigImpl.register(
            "items_filter_type", FilterType.BLACKLIST,
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("Filter mode for specific items."));

    public static final ConfigValue<List<String>> ITEMS_FILTER = ConfigImpl.register(
            "items_filter", List.of(),
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("List of item IDs to filter.")
                    .line("Example: [\"minecraft:carrot\", \"minecraft:diamond_ore\"]"));

    public static final ConfigValue<FilterType> ITEM_TAGS_FILTER_TYPE = ConfigImpl.register(
            "item_tags_filter_type", FilterType.BLACKLIST,
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("Filter mode for item tags."));

    public static final ConfigValue<List<String>> ITEM_TAGS_FILTER = ConfigImpl.register(
            "item_tags_filter", List.of(),
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("List of item tags to filter.")
                    .line("Example: [\"minecraft:logs\", \"c:ores\"]"));

    public static final ConfigValue<FilterType> ENTITIES_FILTER_TYPE = ConfigImpl.register(
            "entities_filter_type", FilterType.BLACKLIST,
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("Filter mode for entities."));

    public static final ConfigValue<List<String>> ENTITIES_FILTER = ConfigImpl.register(
            "entities_filter", List.of(),
            ConfigSection.FILTERS, ConfigFlag.SYNC,
            ConfigComment.of("List of entity IDs to filter.")
                    .line("Example: [\"minecraft:enderman\", \"aether:valkyrie\"]"));

    // HUD
    public static final ConfigValue<Boolean> ENABLE_HUD = ConfigImpl.register(
            "enable_hud", true,
            ConfigSection.HUD, ConfigFlag.SYNC,
            ConfigComment.of("Enables the compass HUD overlay."));

    public static final ConfigValue<Boolean> SHOW_COORDS = ConfigImpl.register(
            "hud_show_coords", true,
            ConfigSection.HUD, ConfigFlag.SYNC,
            ConfigComment.of("Shows target coordinates in the HUD."));

    public static final ConfigValue<Integer> DEFAULT_HUD_X_POSITION = ConfigImpl.register(
            "default_x_hud_offset", 0,
            ConfigSection.HUD,
            ConfigComment.of("Base x hud offset in px"));

    public static final ConfigValue<Integer> DEFAULT_HUD_Y_POSITION = ConfigImpl.register(
            "default_y_hud_offset", 0,
            ConfigSection.HUD,
            ConfigComment.of("Base y hud offset in px"));

    public static final ConfigValue<Double> DEFAULT_HUD_SCALE = ConfigImpl.register(
            "default_hud_scale", 1.0D,
            ConfigSection.HUD,
            ConfigComment.of("Default hud scale"));

    // Compat
    public static final ConfigValue<Boolean> ENABLE_LOOTR_SEARCH = ConfigImpl.register(
            "enable_lootr_search", true,
            ConfigSection.COMPAT, ConfigFlag.SYNC,
            ConfigComment.of("Enables Lootr containers searching when Lootr mod is installed."));

    public static final ConfigValue<LootrSearchMode.ConfigMode> LOOTR_SEARCH = ConfigImpl.register(
            "lootr_search_mode", LootrSearchMode.ConfigMode.ALL,
            ConfigSection.COMPAT, ConfigFlag.SYNC,
            ConfigComment.of("Lootr search behavior"));

    public static final ConfigValue<Boolean> JEI_COMPATIBILITY = ConfigImpl.register(
            "jei_compatibility", true,
            ConfigSection.COMPAT, ConfigFlag.SYNC,
            ConfigComment.of("Enable dragging items from the JEI panel into Compass inventory"));

    public static final ConfigValue<Boolean> REI_COMPATIBILITY = ConfigImpl.register(
            "rei_compatibility", true,
            ConfigSection.COMPAT, ConfigFlag.SYNC,
            ConfigComment.of("Enable dragging items from the REI panel into Compass inventory"));

    public static final ConfigValue<Boolean> EMI_COMPATIBILITY = ConfigImpl.register(
            "emi_compatibility", true,
            ConfigSection.COMPAT, ConfigFlag.SYNC,
            ConfigComment.of("Enable dragging items from the EMI panel into Compass inventory"));

    public static final ConfigValue<Boolean> JADE_COMPATIBILITY = ConfigImpl.register(
            "jade_compatibility", true,
            ConfigSection.COMPAT,
            ConfigComment.of("Enable displaying Block/Entity search info in the Jade info panel"));

    public static final ConfigValue<Boolean> THE_ONE_PROBE_COMPATIBILITY = ConfigImpl.register(
            "the_one_probe_compatibility", true,
            ConfigSection.COMPAT,
            ConfigLoaderTarget.of(LoaderTarget.NEOFORGE),
            ConfigComment.of("Enable displaying Block/Entity search info in the TOP info panel"));

    public static void init() {
    }
}
