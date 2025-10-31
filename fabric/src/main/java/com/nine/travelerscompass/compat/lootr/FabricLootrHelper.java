package com.nine.travelerscompass.compat.lootr;

import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.ISearchCriterion;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.LootrContainerLocationObject;
import com.nine.travelerscompass.common.search.location.LootrMinecartLocationObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.common.api.data.DefaultLootFiller;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity;
import noobanidus.mods.lootr.common.data.DataStorage;
import noobanidus.mods.lootr.common.entity.LootrChestMinecartEntity;

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
			var inventory = DataStorage.getInventory(minecart, options.player(), DefaultLootFiller.getInstance());
			if (inventory != null) {
				String id = entity.getType().getDescriptionId();
				for (ItemStack stack : inventory.getInventoryContents()) {
					Item item = stack.getItem();
					if (criteria.itemCriteriaMap.containsKey(item)) {
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
		if (be instanceof ILootrBlockEntity lootEntity) {
			if (criteria.itemCriteria.isEmpty()) {
				return List.of();
			}
			if (!shouldCheckLootrContainer(lootEntity, options)) return ret;
			var inventory = DataStorage.getInventory(lootEntity, options.player(), DefaultLootFiller.getInstance());
			if (inventory != null) {
				ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
				for (ItemStack stack : inventory.getInventoryContents()) {
					Item item = stack.getItem();
					if (criteria.itemCriteriaMap.containsKey(item)) {
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
	
	public static boolean shouldCheckLootrContainer(ILootrInfoProvider iHasOpeners, SearchOptions options) {
		LootrSearchMode searchMode = options.get(CompassComponents.LOOTR_MODE);
		if (iHasOpeners.getActualOpeners() == null) return false;
		boolean opened = iHasOpeners.getActualOpeners().contains(options.getPlayerUUID());
		return switch (searchMode) {
			case ALL -> true;
			case OPENED -> opened;
			case CLOSED -> !opened;
			default -> false;
		};
	}
	
}
