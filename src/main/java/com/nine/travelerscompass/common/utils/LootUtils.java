package com.nine.travelerscompass.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LootUtils {
    public static List<LootPool> getPools(LootTable lootTable) {
        return lootTable.pools;
    }

    public static List<LootPoolEntryContainer> getEntries(LootPool lootPool) {
        return List.of(lootPool.entries);
    }

    public static List<LootItem> getLootItems(LootTable lootTable) {
        List<LootItem> dropList = new ArrayList<>();
        getPools(lootTable).forEach(
                lootPool -> {
                    getEntries(lootPool).stream()
                            .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                            .forEach(dropList::add);
                }
        );
        dropList.removeIf(Objects::isNull);
        return dropList;
    }

    public static List<Item> getItemsFromLootTable(LootTable lootTable) {
        List<Item> dropStackList = new ArrayList<>();

        getLootItems(lootTable).stream()
                .map(lootItem -> lootItem.item)
                .forEach(dropStackList::add);

        return dropStackList;
    }

    public static List<ItemStack> getAllPossibleLootFromBlock(Block block, ServerLevel level, BlockPos pos) {
        ResourceLocation lootTableLocation = block.getLootTable();
        LootTable lootTable = level.getServer().getLootData().getLootTable(lootTableLocation);

        LootParams.Builder paramsBuilder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, null)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos));
        LootParams lootParams = paramsBuilder.create(LootContextParamSets.BLOCK);

        List<ItemStack> allPossibleLoot = new ArrayList<>();
        lootTable.getRandomItems(lootParams, item -> {
            if (!allPossibleLoot.contains(item)) {
                allPossibleLoot.add(item);
            }
        });

        return allPossibleLoot;
    }

}
