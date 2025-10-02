package com.nine.travelerscompass.common.search.matcher;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.*;
import com.nine.travelerscompass.common.search.location.*;
import com.nine.travelerscompass.common.utils.LootUtils;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.mixin.accessor.BaseSpawnerAccessor;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.*;

public class PositionMatchers {

    public static final BlockEntityMatcher CONTAINER_MATCHER = Platform.PLATFORM_MATCHERS.containerMatcher();

    public static final BlockEntityMatcher LOOTR_CONTAINER_MATCHER = Platform.PLATFORM_MATCHERS.lootrContainerMatcher();

    public static final EntityMatcher LOOTR_MINECART_MATCHER = Platform.PLATFORM_MATCHERS.lootrMinecartMatcher();

    public static final BlockMatcher BLOCK_MATCHER = new BlockMatcher() {

        @Override
        public ILocationObject match(TypedCriteria criteria, BlockPos pos, BlockState state) {
            Block block = state.getBlock();
            for (BlockCriterion criterion : criteria.blockCriteria) {
                if (criterion.check(block)) {
                    ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                    return (new BlockLocationObject(pos, criterion.slot(), criterion.priority(), block.getName().getString(), id));
                }
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return options.get(CompassProperties.BLOCKS) && TCConfig.ENABLE_BLOCKS_SEARCH.get();
        }
    };

    public static final BlockMatcher FLUID_MATCHER = new BlockMatcher() {

        @Override
        public ILocationObject match(TypedCriteria criteria, BlockPos pos, BlockState state) {
            Block block = state.getBlock();
            if (block instanceof LiquidBlock liquidBlock){
                Fluid fluid = liquidBlock.getFluidState(state).getType();
                for (FluidCriterion criterion : criteria.fluidCriteria){
                    if (criterion.check(fluid)){
                        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                        return new BlockLocationObject(
                                pos.immutable(),
                                criterion.slot(),
                                criterion.priority(),
                                state.getBlock().getDescriptionId(),
                                id
                        );
                    }
                }
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return options.get(CompassProperties.FLUIDS) && TCConfig.ENABLE_FLUIDS_SEARCH.get();
        }
    };

    public static final BlockEntityMatcher SPAWNER_MATCHER = new BlockEntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, BlockPos pos, BlockState state, BlockEntity be) {
            Block block = state.getBlock();
            if (be instanceof SpawnerBlockEntity spawnerBlockEntity) {
                SpawnData spawndata = ((BaseSpawnerAccessor)spawnerBlockEntity.getSpawner()).travelerscompass$nextSpawnData();
                if (spawndata != null) {
                    CompoundTag compoundtag = spawndata.getEntityToSpawn();
                    Optional<EntityType<?>> optional = EntityType.by(compoundtag);
                    if (optional.isPresent()) {
                        EntityType<?> entityType = optional.get();
                        for (EntityCriterion criterion : criteria.entityCriteria){
                            if (criterion.check(entityType)){
                                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
                                return List.of(new SpawnerLocationObject(
                                        pos.immutable(),
                                        criterion.slot(),
                                        criterion.priority(),
                                        state.getBlock().getDescriptionId(),
                                        entityType.getDescriptionId(),
                                        id
                                        ));
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return options.get(CompassProperties.SPAWNERS) && TCConfig.ENABLE_SPAWNERS_SEARCH.get();
        }
    };


    public static final EntityMatcher MINECART_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            List<ILocationObject> ret = new ArrayList<>();
            if (entity instanceof AbstractMinecartContainer minecart) {
                if (criteria.itemCriteria.isEmpty()) {
                    return List.of();
                }
                UUID uuid = minecart.getUUID();
                String descriptionId = entity.getType().getDescriptionId();
                for (ItemStack stack : minecart.getItemStacks()){
                    Item item = stack.getItem();
                    if (criteria.itemCriteriaMap.containsKey(item)){
                        ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
                        ret.add(new ContainerEntityLocationObject(pos.immutable(),
                                criterion.slot(),
                                criterion.priority(),
                                descriptionId,
                                item.getDescriptionId(),
                                uuid
                        ));
                    }
                }
            }
            return ret;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            if (!options.get(CompassProperties.CONTAINERS) || !TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get()) {
                return false;
            }
            return options.get(CompassProperties.MINECARTS);
        }
    };


    public static final EntityMatcher EGG_MOB_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            EntityType<?> type = entity.getType();
            for (EntityCriterion criterion : criteria.entityCriteria){
                if (criterion.check(type)){
                    return List.of(new EntityLocationObject(
                            entity.blockPosition(),
                            criterion.slot(),
                            criterion.priority(),
                            entity.getName().getString(),
                            entity.getUUID()));
                }
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return options.get(CompassProperties.MOBS) && TCConfig.ENABLE_MOBS_SEARCH.get();
        }
    };

    public static final EntityMatcher VILLAGER_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            List<ILocationObject> ret = new ArrayList<>();
            if (entity instanceof Villager villager) {
                MerchantOffers offers = villager.getOffers();
                boolean buys = options.get(CompassProperties.VILLAGERS_BUYS);
                boolean sells = options.get(CompassProperties.VILLAGERS_SELLS);
                UUID uuid = villager.getUUID();
                String descriptionId = entity.getType().getDescriptionId();
                for (MerchantOffer offer : offers) {
                    Item resultItem = offer.getResult().getItem();
                    Item costAItem = offer.getCostA().getItem();
                    Item costBItem = offer.getCostB().getItem();
                    for (ItemCriterion criterion : criteria.itemCriteria) {
                        if (sells && criterion.check(resultItem)) {
                            ret.add(new ContainerEntityLocationObject(pos, criterion.slot(), criterion.priority(), descriptionId, resultItem.getDescriptionId(), uuid));
                        }
                        if (buys) {
                            if (criterion.check(costAItem)) {
                                ret.add(new ContainerEntityLocationObject(pos, criterion.slot(), criterion.priority(), descriptionId, costAItem.getDescriptionId(), uuid));
                            }
                            if (criterion.check(costBItem)){
                                ret.add(new ContainerEntityLocationObject(pos, criterion.slot(), criterion.priority(), descriptionId, costBItem.getDescriptionId(), uuid));
                            }
                        }
                    }
                }
                return ret;
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return (options.get(CompassProperties.VILLAGERS) &&
                    (options.get(CompassProperties.VILLAGERS_BUYS) || options.get(CompassProperties.VILLAGERS_SELLS)))
                    && TCConfig.ENABLE_VILLAGERS_SEARCH.get();
        }
    };

    public static final EntityMatcher INVENTORY_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            List<ILocationObject> ret = new ArrayList<>();
            if (entity instanceof LivingEntity living) {
                Set<Item> invItems = new HashSet<>();
                boolean players = options.get(CompassProperties.INVENTORIES_PLAYERS);
                boolean mobs = options.get(CompassProperties.INVENTORIES_MOBS);
                String descriptionId = entity.getType().getDescriptionId();
                UUID uuid = entity.getUUID();
                if (uuid.equals(options.getPlayerUUID())) {
                    return null;
                }
                if (living instanceof Player player) {
                    if (players) {
                        invItems.addAll(player.getInventory().items.stream().map(ItemStack::getItem).toList());
                        invItems.addAll(player.getInventory().armor.stream().map(ItemStack::getItem).toList());
                        invItems.addAll(player.getInventory().offhand.stream().map(ItemStack::getItem).toList());
                        descriptionId = player.getDisplayName().getString();
                    }
                }
                else if (mobs){
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        invItems.add(living.getItemBySlot(slot).getItem());
                    }
                }
                else {
                    return null;
                }
                for (var criterion : criteria.itemCriteria){
                    if (invItems.contains(criterion.item())) {
                        ret.add(new ContainerEntityLocationObject(
                                pos, criterion.slot(),
                                criterion.priority(),
                                descriptionId,
                                criterion.item().getDescriptionId(),
                                uuid));
                    }
                }
                return ret;
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return (options.get(CompassProperties.INVENTORIES) &&
                    (options.get(CompassProperties.INVENTORIES_MOBS) || options.get(CompassProperties.INVENTORIES_PLAYERS)));
        }
    };

    public static final EntityMatcher DROP_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            List<ILocationObject> ret = new ArrayList<>();
            if (entity instanceof LivingEntity living) {
                MinecraftServer server = level.getServer();
                if (server != null){
                    Set<Item> drops = LootUtils.getItemsFromLootTable(living.getLootTable(), level);
                    if (drops != null){
                        for (var criterion : criteria.itemCriteria){
                            if (drops.contains(criterion.item())) {
                                ret.add(new ContainerEntityLocationObject(
                                        pos, criterion.slot(),
                                        criterion.priority(),
                                        entity.getType().getDescriptionId(),
                                        criterion.item().getDescriptionId(),
                                        entity.getUUID()));
                            }
                        }
                    }
                }
                return ret;
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return options.get(CompassProperties.DROP);
        }
    };

    public static final EntityMatcher ITEM_ENTITY_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            List<ILocationObject> ret = new ArrayList<>();
            if (entity instanceof ItemEntity itemEntity) {
                if (criteria.itemCriteria.isEmpty()){
                    return ret;
                }
                Item item = itemEntity.getItem().getItem();
                if (criteria.itemCriteriaMap.containsKey(item)){
                    ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
                    ret.add(new EntityLocationObject(pos.immutable(),
                            criterion.slot(),
                            criterion.priority(),
                            item.getDescriptionId(),
                            itemEntity.getUUID()
                    ));
                }
                return ret;
            }
            return null;
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            return options.get(CompassProperties.ITEM_ENTITIES);
        }
    };


    public static final List<BlockMatcher> BLOCK_MATCHERS = List.of(
            BLOCK_MATCHER,
            FLUID_MATCHER
        );

    public static final List<BlockEntityMatcher> BLOCK_ENTITY_MATCHERS = List.of(
            CONTAINER_MATCHER,
            SPAWNER_MATCHER,
            LOOTR_CONTAINER_MATCHER
    );

    public static final List<EntityMatcher> ENTITY_MATCHERS = List.of(
            EGG_MOB_MATCHER,
            INVENTORY_MATCHER,
            MINECART_MATCHER,
            VILLAGER_MATCHER,
            DROP_MATCHER,
            ITEM_ENTITY_MATCHER,
            LOOTR_MINECART_MATCHER
    );

}
