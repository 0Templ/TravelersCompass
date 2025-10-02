package com.nine.travelerscompass.compat.lootr;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.ISearchCriterion;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.LootrContainerLocationObject;
import com.nine.travelerscompass.common.search.location.LootrMinecartLocationObject;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.zestyblaze.lootr.api.IHasOpeners;
import net.zestyblaze.lootr.api.blockentity.ILootBlockEntity;
import net.zestyblaze.lootr.data.DataStorage;
import net.zestyblaze.lootr.data.SpecialChestInventory;
import net.zestyblaze.lootr.entity.LootrChestMinecartEntity;

import java.util.ArrayList;
import java.util.List;

public class FabricLootrHelper {

    public static List<ILocationObject> minecartMatch(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
        List<ILocationObject> ret = new ArrayList<>();
        if (entity instanceof LootrChestMinecartEntity minecart) {
            if (criteria.itemCriteria.isEmpty()) {
                return List.of();
            }
            if (!shouldCheckLootrContainer(minecart, options)) return ret;
            SpecialChestInventory inventory = DataStorage.getInventory(level, minecart, options.player(), minecart::addLoot);
            if (inventory != null) {
                String id = entity.getType().getDescriptionId();
                for (ItemStack stack : inventory.getInventoryContents()){
                    Item item = stack.getItem();
                    if (criteria.itemCriteriaMap.containsKey(item)){
                        ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
                        ret.add(new LootrMinecartLocationObject(
                                pos.immutable(),
                                criterion.slot(),
                                criterion.priority(),
                                id,
                                item.getDescriptionId(),
                                entity.getUUID()
                        ));
                    }
                }
            }
        }
        return ret;
    }

    public static List<ILocationObject> matchLootrContainer(TypedCriteria criteria, SearchOptions options, BlockPos pos, BlockState state, BlockEntity be) {
        List<ILocationObject> ret = new ArrayList<>();
        if (be instanceof ILootBlockEntity lootEntity) {
            if (criteria.itemCriteria.isEmpty()) {
                return List.of();
            }
            if (!shouldCheckLootrContainer(lootEntity, options)) return ret;
            SpecialChestInventory inventory = DataStorage.getInventory(
                    options.player().level(),
                    lootEntity.getTileId(),
                    lootEntity.getPosition(),
                    options.player(),
                    (RandomizableContainerBlockEntity) lootEntity,
                    lootEntity::unpackLootTable);
            if (inventory != null) {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
                for (ItemStack stack : inventory.getInventoryContents()){
                    Item item = stack.getItem();
                    if (criteria.itemCriteriaMap.containsKey(item)){
                        ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
                        ret.add(new LootrContainerLocationObject(pos.immutable(),
                                criterion.slot(),
                                criterion.priority(),
                                0,
                                state.getBlock().getDescriptionId(),
                                item.getDescriptionId(),
                                id
                        ));
                    }
                }
            }
        }
        return ret;
    }

    public static boolean shouldCheckLootrContainer(IHasOpeners iHasOpeners, SearchOptions options){
        LootrSearchMode searchMode = options.get(CompassProperties.LOOTR_MODE);
        boolean opened = iHasOpeners.getOpeners().contains(options.getPlayerUUID());
        return switch (searchMode) {
            case ALL -> true;
            case OPENED -> opened;
            case CLOSED -> !opened;
            default -> false;
        };
    }


}
