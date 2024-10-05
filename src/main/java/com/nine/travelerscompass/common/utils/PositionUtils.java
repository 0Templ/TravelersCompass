package com.nine.travelerscompass.common.utils;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.data.SpecialChestInventory;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PositionUtils {

    @Nullable
    public static LocationData getNearestBlockFromList(Level level, Entity entity, List<Item> searchBlocks, ItemStack stack, TravelersCompassItem travelersCompassItem, boolean wideSearch) {
        if (!(entity instanceof Player player) || searchBlocks == null) {
            return null;
        }
        BlockPos userPos = player.getOnPos();
        double px = player.getX();
        double py = player.getY();
        double pz = player.getZ();
        ArrayList<Fluid> fluids = new ArrayList<>();
        ArrayList<Fluid> fluidsFavorite = new ArrayList<>();
        ArrayList<EntityType<?>> entityTypes = new ArrayList<>();
        ArrayList<EntityType<?>> entityTypesFavorite = new ArrayList<>();
        CompassContainer container = CompassContainer.container(stack);
        List<Item> favoriteList = container.getFavoriteList(travelersCompassItem, stack);
        List<Item> blocks_list = new ArrayList<>();
        List<EntityType<?>> spawners_list = new ArrayList<>();
        List<Fluid> fluids_list = new ArrayList<>();
        for (Item item : searchBlocks) {
            if (item instanceof BucketItem bucketItem && travelersCompassItem.isSearchingFluids(stack)) {
                fluids.add(bucketItem.getFluid());
                if (favoriteList.contains(bucketItem)) {
                    fluidsFavorite.add(bucketItem.getFluid());
                }
            }
            if (item instanceof SpawnEggItem spawnEggItem && travelersCompassItem.isSearchingSpawners(stack)) {
                entityTypes.add(spawnEggItem.getType(null));
                if (favoriteList.contains(spawnEggItem)) {
                    entityTypesFavorite.add(spawnEggItem.getType(null));
                }
            }
        }
        AABB aabb = new AABB(px, py, pz, px + 1, py + 1, pz + 1).inflate(wideSearch ? travelersCompassItem.wideSearchRadius(stack) : travelersCompassItem.blockSearchRadius(stack));
        boolean hasAnyFavorite;
        if (!favoriteList.isEmpty() && travelersCompassItem.priorityMode(stack)) {
            if (TCConfig.enableBlockSearch.get() && travelersCompassItem.isSearchingBlocks(stack)) {
                blocks_list = BlockPos.betweenClosedStream(aabb)
                        .map(blockPos -> {
                            BlockState blockState = level.getBlockState(blockPos);
                            Block block = blockState.getBlock();
                            return block.asItem();
                        })
                        .toList();
            }
            if (travelersCompassItem.isSearchingSpawners(stack) && TCConfig.enableSpawnerSearch.get()) {
                spawners_list = BlockPos.betweenClosedStream(aabb)
                        .map(blockPos -> {
                            BlockState blockState = level.getBlockState(blockPos);
                            Block block = blockState.getBlock();
                            if (block instanceof SpawnerBlock) {
                                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                                if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                                    SpawnData spawndata = spawnerBlockEntity.getSpawner().nextSpawnData;
                                    if (spawndata != null) {
                                        CompoundTag compoundtag = spawndata.getEntityToSpawn();
                                        Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                                        if (optional.isPresent()) {
                                            return optional.get();
                                        }
                                    }
                                }
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
            if (TCConfig.enableSpawnerSearch.get() && travelersCompassItem.isSearchingFluids(stack)) {
                fluids_list = BlockPos.betweenClosedStream(aabb)
                        .map(blockPos -> {
                            BlockState blockState = level.getBlockState(blockPos);
                            Block block = blockState.getBlock();
                            if (block instanceof LiquidBlock liquidBlock) {
                                return liquidBlock.getFluid().getSource();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .toList();
            }
            hasAnyFavorite = blocks_list.stream().anyMatch(favoriteList::contains) || fluids_list.stream().anyMatch(fluidsFavorite::contains) || spawners_list.stream().anyMatch(entityTypesFavorite::contains);
        } else {
            hasAnyFavorite = false;
        }
        BlockPos blockPos = BlockPos.betweenClosedStream(aabb)
                .map(BlockPos::immutable)
                .filter(foundPos -> {
                    BlockState blockState = level.getBlockState(foundPos);
                    Block block = blockState.getBlock();
                    if (block instanceof AirBlock) {
                        return false;
                    }
                    if (!entityTypes.isEmpty() && block instanceof SpawnerBlock && travelersCompassItem.isSearchingSpawners(stack) && TCConfig.enableSpawnerSearch.get()) {
                        BlockEntity blockEntity = level.getBlockEntity(foundPos);
                        if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                            SpawnData spawndata = spawnerBlockEntity.getSpawner().nextSpawnData;
                            if (spawndata != null) {
                                CompoundTag compoundtag = spawndata.getEntityToSpawn();
                                Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                                if (optional.isPresent()) {
                                    if (hasAnyFavorite && !entityTypesFavorite.contains(optional.get())) {
                                        return false;
                                    }
                                    return entityTypes.contains(optional.get());
                                }
                            }
                        }
                    }
                    if (!fluids.isEmpty() && block instanceof LiquidBlock liquidBlock && travelersCompassItem.isSearchingFluids(stack)) {
                        if (hasAnyFavorite && !fluidsFavorite.contains(liquidBlock.getFluid().getSource())) {
                            return false;
                        }
                        return fluids.contains(liquidBlock.getFluid().getSource());
                    }
                    if (hasAnyFavorite && !favoriteList.contains(block.asItem())) {
                        return false;
                    }
                    return searchBlocks.contains(block.asItem()) && TCConfig.enableBlockSearch.get() && travelersCompassItem.isSearchingBlocks(stack);
                })
                .min(Comparator.comparingDouble(foundPos -> foundPos.distSqr(userPos)))
                .orElse(null);
        if (blockPos != null) {
            return new LocationData(blockPos, hasAnyFavorite, level.getBlockState(blockPos).getBlock().getName().getString());
        }
        return null;
    }

/*    public static List<ItemStack> getAllPossibleBlockLoot(Block block, ServerLevel level, BlockPos pos) {
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
    }*/

    @Nullable
    public static LocationData getNearestContainerFromList(Level level, Entity entity, List<Item> searchItems, ItemStack stack, TravelersCompassItem travelersCompassItem, boolean wideSearch) {
        if (!(entity instanceof Player player) || searchItems == null) {
            return null;
        }
        BlockPos userPos = player.blockPosition();
        double px = player.getX();
        double py = player.getY();
        double pz = player.getZ();
        AABB aabb = new AABB(px, py, pz, (px + 1), (py + 1), (pz + 1)).inflate(wideSearch ? travelersCompassItem.wideSearchRadius(stack) : travelersCompassItem.containerSearchRadius(stack));
        boolean hasAnyFavorite;
        CompassContainer container = CompassContainer.container(stack);
        List<Item> favoriteList = container.getFavoriteList(travelersCompassItem, stack);
        if (!favoriteList.isEmpty() && travelersCompassItem.priorityMode(stack)) {
            List<Item> containersItemList = BlockPos.betweenClosedStream(aabb)
                    .flatMap(blockPos -> {
                        BlockEntity blockEntity = level.getBlockEntity(blockPos);
                        if (blockEntity != null) {
                            if (ConfigUtils.hasLootr()) {
                                if (blockEntity instanceof ILootBlockEntity lootEntity && player instanceof ServerPlayer serverPlayer) {
                                    if ((lootEntity.getOpeners().contains(player.getUUID()) && !TCConfig.LootrCompatibility.get() || TCConfig.LootrCompatibility.get())) {
                                        SpecialChestInventory inventory = DataStorage.getInventory(level, lootEntity.getTileId(), lootEntity.getPosition(), serverPlayer, (RandomizableContainerBlockEntity) lootEntity, lootEntity::unpackLootTable);
                                        if (inventory != null) {
                                            return IntStream.range(0, inventory.getContainerSize())
                                                    .mapToObj(index -> {
                                                        ItemStack stackInSlot = inventory.getItem(index);
                                                        if (searchItems.contains(stackInSlot.getItem()) && favoriteList.contains(stackInSlot.getItem())) {
                                                            return stackInSlot.getItem();
                                                        }
                                                        return null;
                                                    })
                                                    .filter(Objects::nonNull);
                                        }
                                    }
                                }
                            }
                            return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                                    .map(itemHandler -> IntStream.range(0, itemHandler.getSlots())
                                            .mapToObj(itemHandler::getStackInSlot)
                                            .map(ItemStack::getItem))
                                    .orElse(Stream.empty());
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();
            hasAnyFavorite = containersItemList.stream().anyMatch(favoriteList::contains);
        } else {
            hasAnyFavorite = false;
        }
        BlockPos blockPos = BlockPos.betweenClosedStream(aabb)
                .map(BlockPos::immutable)
                .filter(foundPos -> {
                    BlockEntity blockEntity = level.getBlockEntity(foundPos);
                    if (blockEntity != null) {
                        if (ConfigUtils.hasLootr()) {
                            if (blockEntity instanceof ILootBlockEntity lootEntity && player instanceof ServerPlayer serverPlayer) {
                                if (ConfigUtils.hasLootr()) {
                                    if ((lootEntity.getOpeners().contains(player.getUUID()) && !TCConfig.LootrCompatibility.get() || TCConfig.LootrCompatibility.get())) {
                                        SpecialChestInventory inventory = DataStorage.getInventory(level, lootEntity.getTileId(), lootEntity.getPosition(), serverPlayer, (RandomizableContainerBlockEntity) lootEntity, lootEntity::unpackLootTable);
                                        if (inventory != null) {
                                            for (int index = 0; index < inventory.getContainerSize(); index++) {
                                                ItemStack stackInSlot = inventory.getItem(index);
                                                if (searchItems.contains(stackInSlot.getItem()) && !(hasAnyFavorite && !favoriteList.contains(stackInSlot.getItem()))) {
                                                    return searchItems.contains(stackInSlot.getItem());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                                .map(itemHandler -> {
                                    for (int index = 0; index < itemHandler.getSlots(); index++) {
                                        ItemStack stackInSlot = itemHandler.getStackInSlot(index);
                                        if (searchItems.contains(stackInSlot.getItem()) && !(hasAnyFavorite && !favoriteList.contains(stackInSlot.getItem()))) {
                                            return searchItems.contains(stackInSlot.getItem());
                                        }
                                    }
                                    return false;
                                })
                                .orElse(false);
                    }
                    return false;
                })
                .min(Comparator.comparingDouble(foundPos -> foundPos.distSqr(userPos)))
                .orElse(null);
        if (blockPos != null) {
            return new LocationData(blockPos, hasAnyFavorite, level.getBlockState(blockPos).getBlock().getName().getString());
        }
        return null;
    }

    @Nullable
    public static LocationData getNearestEntity(Level level, Entity entity, List<Item> compassItems, ItemStack stack, TravelersCompassItem travelersCompassItem, boolean wideSearch) {
        if (entity instanceof Player player) {
            BlockPos userPos = player.getOnPos();
            double px = player.getX();
            double py = player.getY();
            double pz = player.getZ();

            AABB aabb = new AABB(px, py, pz, (px + 1), (py + 1), (pz + 1)).inflate(wideSearch ? travelersCompassItem.wideSearchRadius(stack) : travelersCompassItem.entitySearchRadius(stack));
            List<Entity> list = entity.level().getEntitiesOfClass(Entity.class, aabb);
            List<Entity> good_list = new ArrayList<>();

            CompassContainer container = CompassContainer.container(stack);
            List<Item> favoriteList = container.getFavoriteList(travelersCompassItem, stack);
            List<Item> allItems = new ArrayList<>();
            boolean vG = !travelersCompassItem.isSearchingVillagersGoods(stack);
            boolean vC = !travelersCompassItem.isSearchingVillagersCost(stack);
            boolean hasAnyFavorite;
            for (Entity entityJ : list) {
                if (travelersCompassItem.isSearchingEntitiesInv(stack)) {
                    if (entityJ instanceof LivingEntity living && !living.is(player) && ((living instanceof Player && !travelersCompassItem.isSearchingPlayersInv(stack) || (!(living instanceof Player) && !travelersCompassItem.isSearchingMobsInv(stack))))) {
                        List<Item> entityInventory = living.getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                                .map(itemHandler -> IntStream.range(0, itemHandler.getSlots())
                                        .mapToObj(itemHandler::getStackInSlot)
                                        .flatMap(itemStack -> {
                                            if (!itemStack.isEmpty()) {
                                                return Stream.of(itemStack.getItem());
                                            } else {
                                                return Stream.empty();
                                            }
                                        })
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList());
                        allItems.addAll(entityInventory);
                    }
                    if (entityJ instanceof Container container_ && !travelersCompassItem.isSearchingMinecartsInv(stack)) {
                        for (int index = 0; index < container_.getContainerSize(); index++) {
                            Item item = container_.getItem(index).getItem();
                            allItems.add(item);
                        }
                        if (ConfigUtils.hasLootr()) {
                            if (container_ instanceof LootrChestMinecartEntity minecartEntity && player instanceof ServerPlayer serverPlayer) {
                                if ((minecartEntity.getOpeners().contains(player.getUUID()) && !TCConfig.LootrCompatibility.get() || TCConfig.LootrCompatibility.get())) {
                                    SpecialChestInventory inventory = DataStorage.getInventory(level, minecartEntity, serverPlayer, minecartEntity::addLoot);
                                    if (inventory != null) {
                                        for (int index = 0; index < inventory.getContainerSize(); index++) {
                                            allItems.add(inventory.getItem(index).getItem());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (entityJ instanceof ItemEntity itemEntity && travelersCompassItem.isSearchingItemEntities(stack)) {
                    if (compassItems.contains(itemEntity.getItem().getItem())) {
                        if (favoriteList.contains(itemEntity.getItem().getItem())) {
                            allItems.add(itemEntity.getItem().getItem());
                        }
                    }
                }
                if (entityJ instanceof LivingEntity living && travelersCompassItem.isSearchingDrops(stack)) {
                    if (living.level().getServer() != null && !(entityJ instanceof Player)) {
                        LootTable loottable = Objects.requireNonNull(living.level().getServer()).getLootData().getLootTable(living.getLootTable());
                        List<Item> dropList = LootUtils.getItemsFromLootTable(loottable);
                        allItems.addAll(dropList);
                    }
                }
                if (entityJ instanceof Villager villager && travelersCompassItem.isSearchingVillagers(stack)) {
                    for (MerchantOffer merchantOffer : villager.getOffers()) {
                        if (vG) {
                            if (compassItems.contains(merchantOffer.getResult().getItem())) {
                                if (favoriteList.contains(merchantOffer.getResult().getItem())) {
                                    allItems.add(merchantOffer.getResult().getItem());
                                }
                            }
                        }
                        if (vC) {
                            if (compassItems.contains(merchantOffer.getCostA().getItem()) || compassItems.contains(merchantOffer.getCostB().getItem())) {
                                if (favoriteList.contains(merchantOffer.getCostA().getItem())) {
                                    allItems.add(merchantOffer.getCostA().getItem());
                                }
                                if (favoriteList.contains(merchantOffer.getCostB().getItem())) {
                                    allItems.add(merchantOffer.getCostB().getItem());
                                }
                            }
                        }
                    }
                }
            }
            hasAnyFavorite = allItems.stream().anyMatch(favoriteList::contains);
            hasAnyFavorite = hasAnyFavorite && travelersCompassItem.priorityMode(stack);
            for (Entity entityJ : list) {
                if (entityJ instanceof LivingEntity living) {
                    if (living.level().getServer() != null && !(entityJ instanceof Player) && ConfigUtils.isAllowedToSearch(living) && travelersCompassItem.isSearchingDrops(stack)) {
                        LootTable loottable = Objects.requireNonNull(living.level().getServer()).getLootData().getLootTable(living.getLootTable());
                        List<Item> dropList = LootUtils.getItemsFromLootTable(loottable);
                        for (Item item : dropList) {
                            if (hasAnyFavorite && favoriteList.contains(item) && compassItems.contains(item)) {
                                good_list.add(living);
                            }
                            if (!hasAnyFavorite && compassItems.contains(item)) {
                                good_list.add(living);
                            }
                        }
                    }
                }
                if (travelersCompassItem.isSearchingEntitiesInv(stack)) {
                    if (entityJ instanceof LivingEntity living && !living.is(player) && ((living instanceof Player && !travelersCompassItem.isSearchingPlayersInv(stack) || (!(living instanceof Player) && !travelersCompassItem.isSearchingMobsInv(stack))))) {
                        List<Item> entityInventory = living.getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                                .map(itemHandler -> IntStream.range(0, itemHandler.getSlots())
                                        .mapToObj(itemHandler::getStackInSlot)
                                        .flatMap(itemStack -> {
                                            if (!itemStack.isEmpty()) {
                                                return Stream.of(itemStack.getItem());
                                            } else {
                                                return Stream.empty();
                                            }
                                        })
                                        .collect(Collectors.toList()))
                                .orElse(Collections.emptyList());
                        for (Item item : entityInventory) {
                            if (hasAnyFavorite && favoriteList.contains(item) && compassItems.contains(item)) {
                                good_list.add(entityJ);
                            }
                            if (!hasAnyFavorite && compassItems.contains(item)) {
                                good_list.add(entityJ);
                            }
                        }
                    }
                    if (entityJ instanceof Container c && !travelersCompassItem.isSearchingMinecartsInv(stack)) {
                        for (int index = 0; index < c.getContainerSize(); index++) {
                            Item item = c.getItem(index).getItem();
                            if (hasAnyFavorite && favoriteList.contains(item) && compassItems.contains(item)) {
                                good_list.add(entityJ);
                            }
                            if (!hasAnyFavorite && compassItems.contains(item)) {
                                good_list.add(entityJ);
                            }
                        }
                    }
                    if (ConfigUtils.hasLootr()) {
                        if (entityJ instanceof Container container_ && container_ instanceof LootrChestMinecartEntity minecartEntity && player instanceof ServerPlayer serverPlayer && !travelersCompassItem.isSearchingMinecartsInv(stack)) {
                            if ((minecartEntity.getOpeners().contains(player.getUUID()) && !TCConfig.LootrCompatibility.get() || TCConfig.LootrCompatibility.get())) {
                                SpecialChestInventory inventory = DataStorage.getInventory(level, minecartEntity, serverPlayer, minecartEntity::addLoot);
                                if (inventory != null) {
                                    for (int index = 0; index < inventory.getContainerSize(); index++) {
                                        Item item = inventory.getItem(index).getItem();
                                        if (hasAnyFavorite && favoriteList.contains(item) && compassItems.contains(item)) {
                                            good_list.add(entityJ);
                                        }
                                        if (!hasAnyFavorite && compassItems.contains(item)) {
                                            good_list.add(entityJ);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (entityJ instanceof ItemEntity itemEntity && travelersCompassItem.isSearchingItemEntities(stack)) {
                    if (compassItems.contains(itemEntity.getItem().getItem())) {
                        if (hasAnyFavorite && favoriteList.contains(itemEntity.getItem().getItem())) {
                            good_list.add(itemEntity);
                        }
                        if (!hasAnyFavorite) {
                            good_list.add(itemEntity);
                        } else if (favoriteList.contains(itemEntity.getItem().getItem())) {
                            good_list.add(itemEntity);
                        }
                    }
                }
                if (entityJ instanceof Villager villager && travelersCompassItem.isSearchingVillagers(stack)) {
                    for (MerchantOffer merchantOffer : villager.getOffers()) {
                        if (vG) {
                            if (compassItems.contains(merchantOffer.getResult().getItem())) {
                                if (!hasAnyFavorite) {
                                    good_list.add(villager);
                                } else if (favoriteList.contains(merchantOffer.getResult().getItem())) {
                                    good_list.add(villager);
                                }
                            }
                        }
                        if (vC) {
                            if (compassItems.contains(merchantOffer.getCostA().getItem()) || compassItems.contains(merchantOffer.getCostB().getItem())) {
                                if (!hasAnyFavorite) {
                                    good_list.add(villager);
                                } else if (favoriteList.contains(merchantOffer.getCostA().getItem()) || favoriteList.contains(merchantOffer.getCostB().getItem())) {
                                    good_list.add(villager);
                                }
                            }
                        }
                    }
                }
            }
            BlockPos blockPos = good_list.stream()
                    .map(Entity::getOnPos)
                    .min(Comparator.comparingDouble(ent -> ent.distSqr(userPos)))
                    .orElse(null);
            String name = null;
            if (blockPos != null) {
                name = String.valueOf(good_list.stream()
                        .filter(ent -> ent.getOnPos().equals(blockPos))
                        .map(Entity::getName)
                        .map(Component::getString)
                        .findFirst()
                        .orElse(null));
            }
            if (blockPos != null) {
                return new LocationData(blockPos, hasAnyFavorite, name);
            }
        }
        return null;
    }

    public static @Nullable LocationData getNearestMobFromEggItem(Entity entity, List<Item> spawnItems, ItemStack stack, TravelersCompassItem travelersCompassItem, boolean wideSearch) {
        if (entity instanceof Player player) {
            BlockPos userPos = player.getOnPos();

            double px = player.getX();
            double py = player.getY();
            double pz = player.getZ();
            CompassContainer container = CompassContainer.container(stack);

            List<Item> favoriteList = container.getFavoriteList(travelersCompassItem, stack);
            boolean hasAnyFavorite;
            AABB aabb = new AABB(px, py, pz, (px + 1), (py + 1), (pz + 1)).inflate(wideSearch ? travelersCompassItem.wideSearchRadius(stack) : travelersCompassItem.entitySearchRadius(stack));
            List<LivingEntity> list = entity.level().getEntitiesOfClass(LivingEntity.class, aabb);
            List<Item> favoriteEList = new ArrayList<>();
            List<LivingEntity> good_list = new ArrayList<>();
            for (LivingEntity entityJ : list) {
                if (!(entityJ instanceof Player) && ConfigUtils.isAllowedToSearch(entityJ) && travelersCompassItem.isSearchingMobs(stack)) {
                    for (Item spawnItem : spawnItems) {
                        if (spawnItem instanceof SpawnEggItem spawnEggItem) {
                            EntityType<?> entityType = spawnEggItem.getType(null);
                            if (entityJ.getType().equals(entityType) && favoriteList.contains(spawnItem)) {
                                favoriteEList.add(spawnItem);
                            }
                        }
                        if (spawnItem instanceof MobBucketItem && entityJ instanceof Bucketable bucketable) {
                            ItemStack bucketItemStack = bucketable.getBucketItemStack();
                            if (bucketItemStack.getItem().equals(spawnItem) && favoriteList.contains(spawnItem)) {
                                favoriteEList.add(spawnItem);
                            }
                        }
                    }
                }
            }
            hasAnyFavorite = !favoriteEList.isEmpty() && travelersCompassItem.priorityMode(stack);
            for (LivingEntity entityJ : list) {
                if (!(entityJ instanceof Player) && ConfigUtils.isAllowedToSearch(entityJ) && travelersCompassItem.isSearchingMobs(stack)) {
                    for (Item spawnItem : spawnItems) {
                        if (spawnItem instanceof SpawnEggItem spawnEggItem) {
                            EntityType<?> entityType = spawnEggItem.getType(null);
                            if (entityJ.getType().equals(entityType)) {
                                if (!hasAnyFavorite) {
                                    good_list.add(entityJ);
                                }
                                if (favoriteList.contains(spawnEggItem) && hasAnyFavorite) {
                                    good_list.add(entityJ);
                                }
                            }
                        }
                        if (spawnItem instanceof MobBucketItem bucketItem && entityJ instanceof Bucketable bucketable) {
                            ItemStack bucketItemStack = bucketable.getBucketItemStack();
                            if (bucketItemStack.getItem().equals(bucketItem)) {
                                if (!hasAnyFavorite) {
                                    good_list.add(entityJ);
                                }
                                if (favoriteList.contains(bucketItem) && hasAnyFavorite) {
                                    good_list.add(entityJ);
                                }
                            }
                        }
                    }
                }
            }
            BlockPos blockPos = good_list.stream()
                    .map(LivingEntity::getOnPos)
                    .min(Comparator.comparingDouble(ent -> ent.distSqr(userPos)))
                    .orElse(null);
            String name = null;
            if (blockPos != null) {
                name = String.valueOf(good_list.stream()
                        .filter(ent -> ent.getOnPos().equals(blockPos))
                        .map(LivingEntity::getName)
                        .map(Component::getString)
                        .findFirst()
                        .orElse(null));
            }
            if (blockPos != null) {
                return new LocationData(blockPos, hasAnyFavorite, name);
            }
        }
        return null;
    }

    public static LocationData getClosestLocationFromList(List<LocationData> positions, Entity entity, ItemStack stack, TravelersCompassItem travelersCompassItem) {
        BlockPos playerPos = entity.blockPosition();
        LocationData closestPos = null;
        double closestDistanceSq = Double.MAX_VALUE;
        boolean hasTrueValue = false;
        for (LocationData location : positions) {
            if (location != null && location.hasAnyFavorite()) {
                hasTrueValue = true;
            }
        }

        if (hasTrueValue) {
            for (LocationData location : positions) {
                if (location != null && location.hasAnyFavorite()) {
                    BlockPos pos = location.getBlockPos();
                    double distanceSq = pos.distSqr(playerPos);
                    if (distanceSq < closestDistanceSq) {
                        closestDistanceSq = distanceSq;
                        closestPos = location;
                    }
                }
            }
        } else {
            for (LocationData location : positions) {
                if (location != null) {
                    BlockPos pos = location.getBlockPos();
                    double distanceSq = pos.distSqr(playerPos);
                    if (distanceSq < closestDistanceSq) {
                        closestDistanceSq = distanceSq;
                        closestPos = location;
                    }
                }
            }
        }
        travelersCompassItem.markFavoriteItem(stack, hasTrueValue);
        return closestPos;
    }

    public static LocationData getNearestLocation(Level level, Entity entity, TravelersCompassItem travelersCompassItem, CompassContainer compassContainer, ItemStack stack, boolean wideSearch) {
        if (travelersCompassItem.isPaused(stack) && !wideSearch) {
            return null;
        }
        List<LocationData> blockPosList = new ArrayList<>();
        boolean searchingForMobs = (travelersCompassItem.isSearchingMobs(stack) && TCConfig.enableMobSearch.get())
                || (travelersCompassItem.isSearchingEntitiesInv(stack) && TCConfig.enableMobsInventorySearch.get())
                || (travelersCompassItem.isSearchingVillagers(stack) && TCConfig.enableVillagersSearch.get())
                || (travelersCompassItem.isSearchingItemEntities(stack) && TCConfig.enableItemEntitiesSearch.get())
                || (travelersCompassItem.isSearchingDrops(stack) && TCConfig.enableDropSearch.get());
        boolean searchingBlocks = (travelersCompassItem.isSearchingBlocks(stack) && TCConfig.enableBlockSearch.get())
                || (travelersCompassItem.isSearchingFluids(stack) && TCConfig.enableFluidSearch.get())
                || (travelersCompassItem.isSearchingSpawners(stack) && TCConfig.enableSpawnerSearch.get());
        boolean searchingContainers = (travelersCompassItem.isSearchingContainers(stack) && TCConfig.enableContainerSearch.get());
        if (searchingForMobs) {
            blockPosList.add(PositionUtils.getNearestMobFromEggItem(entity, compassContainer.getList(), stack, travelersCompassItem, wideSearch));
            blockPosList.add(PositionUtils.getNearestEntity(level, entity, compassContainer.getList(), stack, travelersCompassItem, wideSearch));
        }
        if (searchingBlocks) {
            blockPosList.add(PositionUtils.getNearestBlockFromList(level, entity, compassContainer.getList(), stack, travelersCompassItem, wideSearch));
        }
        if (searchingContainers) {
            blockPosList.add(PositionUtils.getNearestContainerFromList(level, entity, compassContainer.getList(), stack, travelersCompassItem, wideSearch));
        }
        return PositionUtils.getClosestLocationFromList(blockPosList, entity, stack, travelersCompassItem);
    }

    public static class LocationData {
        private final BlockPos blockPos;
        private final boolean hasAnyFavorite;
        private final String name;

        public LocationData(BlockPos blockPos, boolean hasAnyFavorite, String additionalData) {
            this.blockPos = blockPos;
            this.hasAnyFavorite = hasAnyFavorite;
            this.name = additionalData;
        }

        public BlockPos getBlockPos() {
            return blockPos;
        }

        public boolean hasAnyFavorite() {
            return hasAnyFavorite;
        }

        public String getName() {
            return name;
        }
    }
}
