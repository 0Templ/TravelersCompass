package com.nine.travelerscompass;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TravelersCompass.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TCConfig {

    public static final ForgeConfigSpec COMMON;


    public static ForgeConfigSpec.IntValue entitySearchRadius;
    public static ForgeConfigSpec.IntValue blockSearchRadius;
    public static ForgeConfigSpec.IntValue containersSearchRadius;
    public static ForgeConfigSpec.IntValue searchRate;
    public static ForgeConfigSpec.IntValue xpCost;
    public static ForgeConfigSpec.DoubleValue xpDrainDistanceMultiplier;
    public static ForgeConfigSpec.IntValue xpDrainRate;
    public static ForgeConfigSpec.BooleanValue xpDrain;
    public static ForgeConfigSpec.BooleanValue enableMobSearch;
    public static ForgeConfigSpec.BooleanValue enableContainerSearch;
    public static ForgeConfigSpec.BooleanValue enableBlockSearch;
    public static ForgeConfigSpec.BooleanValue enableVillagersSearch;
    public static ForgeConfigSpec.BooleanValue enableDropSearch;
    public static ForgeConfigSpec.BooleanValue enableItemEntitiesSearch;
    public static ForgeConfigSpec.BooleanValue enableFluidSearch;
    public static ForgeConfigSpec.BooleanValue enableSpawnerSearch;
    public static ForgeConfigSpec.BooleanValue enableMobsInventorySearch;
    public static ForgeConfigSpec.BooleanValue blackListFilter;
    public static ForgeConfigSpec.ConfigValue<List<String>> filteredItemList;
    public static ForgeConfigSpec.ConfigValue<List<String>> filteredTagItemList;
    public static ForgeConfigSpec.ConfigValue<List<String>> filteredModList;
    public static ForgeConfigSpec.ConfigValue<List<String>> filteredEntityList;
    public static ForgeConfigSpec.ConfigValue<List<String>> filteredEntityByModIDList;
    public static ForgeConfigSpec.BooleanValue REICompatibility;
    public static ForgeConfigSpec.BooleanValue JEICompatibility;
    public static ForgeConfigSpec.BooleanValue LootrCompatibility;
    public static ForgeConfigSpec.BooleanValue TheOneProbeCompatibility;
    public static ForgeConfigSpec.BooleanValue JadeCompatibility;
    static {
        final var builder = new ForgeConfigSpec.Builder();
        builder.push("compass-behavior");
        blockSearchRadius = builder.
                comment("Block search radius (in blocks). Large values may result in lag issues.").
                defineInRange("blocks_search_radius", 40, 1, Integer.MAX_VALUE);
        containersSearchRadius = builder.
                comment("Containers search radius (in blocks). Large values may result in lag issues.").
                defineInRange("containers_search_radius", 40, 1, Integer.MAX_VALUE);
        entitySearchRadius = builder.
                comment("Mobs search radius (in blocks).").
                defineInRange("entities_search_radius", 120, 1, Integer.MAX_VALUE);
        searchRate = builder.
                comment("How often the compass search target will be updated. Low values can cause SERIOUS performance problems.").
                defineInRange("search_rate", 50, 1, Integer.MAX_VALUE);
        xpDrain = builder.
                comment("Will experience be drained for using the compass?").
                define("xp_drain", false);
        xpCost = builder.
                comment("How much XP will be drained from player when Compass is active?").
                defineInRange("xp_cost", 2, 0, Integer.MAX_VALUE);
        xpDrainDistanceMultiplier = builder.
                comment("The amount of experience spent will depend on the distance to the found object.").
                defineInRange("xp_drain_from_distance_multiplier", 0.01D, 0, Double.MAX_VALUE);
        xpDrainRate = builder.
                comment("How often experience will be spent for using the compass?").
                defineInRange("xp_drain_rate", 300, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("compass-functions");
        enableBlockSearch = builder.
                comment("Prevents players from searching for blocks.").
                define("block_search", true);
        enableContainerSearch = builder.
                comment("Prevents players from searching containers (chests, furnaces, etc.) based on their contents.").
                define("container_search", true);
        enableMobSearch = builder.
                comment("Prevents players from searching mobs based on their Spawn Eggs.").
                define("mob_search", true);
        enableDropSearch = builder.
                comment("Prevents players from searching mobs based on their Spawn Eggs.").
                define("drop_search", true);
        enableVillagersSearch = builder.
                comment("Prevents players from searching Villagers based on their trade items.").
                define("villager_search", true);
        enableItemEntitiesSearch = builder.
                comment("Prevents players from searching items lying on the ground.").
                define("item_entity_search", true);
        enableFluidSearch = builder.
                comment("Prevents players from searching fluids based on their buckets.").
                define("mob_fluid_search", true);
        enableSpawnerSearch = builder.
                comment("Prevents players from searching spawners.").
                define("mob_spawner_search", true);
        enableMobsInventorySearch = builder.
                comment("Prevents players from searching entities based on their inventories (Minecarts).").
                define("mob_inventory_search", true);
        blackListFilter = builder.
                comment("Setting the value to true will make the compass search for all objects except those specified in the list.\nSetting it to false will make the compass search only for objects listed in the list.").
                define("black_list_filter_type", true);
        filteredItemList = builder.
                comment("Items and blocks in this list will be filtered based on the filtering mode set above.\nExample: [\"minecraft.diamond_block\", \"minecraft.carrot\", \"twilightforest.cicada\"]").
                define("filter_by_names",  new ArrayList<>());
        filteredTagItemList = builder.
                comment("Items and blocks in this list will be filtered based on their tags.\nExample: [\"forge:stone\"]").
                define("filter_by_tags",  new ArrayList<>());
        filteredModList = builder.
                comment("Items and blocks in this list will be filtered based on their mod Name.\nExample: [\"alexscaves\", \"ae2\", \"minecraft\"]").
                define("filter_by_mods",  new ArrayList<>());
        filteredEntityList = builder.
                comment("Entities in this list will be filtered based on their names.\nExample: [\"entity.minecraft.trader_llama\", \"entity.mowziesmobs.foliaath\"]").
                define("filter_entity",  new ArrayList<>());
        filteredEntityByModIDList = builder.
                comment("Entities in this list will be filtered based on what mod they are from.\nExample: [\"creeperoverhaul\", \"deeperdarker\", \"minecraft\"]").
                define("filter_entity_by_mods",  new ArrayList<>());
        builder.pop();
        builder.push("compass-compatibility");
        builder.comment("JEI/REI interaction -> drag and drop items from the JEI/REI panel to the compass inventory");
        REICompatibility = builder.
                comment("Enable interaction with the REI panel").
                define("rei_compatibility", true);
        JEICompatibility = builder.
                comment("Enable interaction with the JEI panel").
                define("jei_compatibility", true);
        LootrCompatibility = builder.
                comment("Enable the search for the contents of chests and minecarts from the Lootr mod that have not yet been opened by the player").
                define("lootr_compatibility", true);
        JadeCompatibility = builder.
                comment("Allows to show Jade tooltips regarding blocks/entities that you can/can't search for").
                define("jade_compatibility", true);
        TheOneProbeCompatibility = builder.
                comment("Allows to show TOP tooltips regarding blocks/entities that you can/can't search for").
                define("top_compatibility", true);
        COMMON = builder.build();
    }

}
