package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.ISearchCriterion;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ContainerLocationObject;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.BlockMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;

import java.util.ArrayList;
import java.util.List;

public class NeoForgePlatformMatchersHelper implements IPlatformMatchersHelper {
	
	@Override
	public List<BlockEntityMatcher> blockEntityMatchers() {
		return List.of(CONTAINER_MATCHER/*, LOOTR_CONTAINER_MATCHER*/);
	}
	
	@Override
	public List<BlockMatcher> blockMatchers() {
		return List.of();
		
	}
	
	@Override
	public List<EntityMatcher> entityMatchers() {
		return List.of(/*LOOTR_MINECART_MATCHER*/);
	}
	
	
	public static final EntityMatcher LOOTR_MINECART_MATCHER = new EntityMatcher() {
		
		@Override
		public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
			return null;
//			return NeoForgeLootrHelper.minecartMatch(criteria, options, entity, level, pos);
		}
		
		@Override
		public boolean isAllowed(SearchOptions options) {
			return Platform.PLATFORM.isModLoaded("lootr") && options.get(CompassComponents.LOOTR_MODE) != LootrSearchMode.OFF;
		}
	};
	
	
	public static final BlockEntityMatcher LOOTR_CONTAINER_MATCHER = new BlockEntityMatcher() {
		
		@Override
		public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, BlockPos pos, BlockState state, BlockEntity be) {
			return null;
//			return NeoForgeLootrHelper.matchLootrContainer(criteria, options, pos, state, be);
		}
		
		@Override
		public boolean isAllowed(SearchOptions options) {
			if (!options.get(CompassComponents.CONTAINERS) || !TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get()) {
				return false;
			}
			return Platform.PLATFORM.isModLoaded("lootr") && options.get(CompassComponents.LOOTR_MODE) != LootrSearchMode.OFF;
		}
	};
	
	public static final BlockEntityMatcher CONTAINER_MATCHER = new BlockEntityMatcher() {
		
		@Override
		public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, BlockPos pos, BlockState state, BlockEntity be) {
			List<ILocationObject> ret = new ArrayList<>();
			if (criteria.itemCriteria.isEmpty()) {
				return List.of();
			}
			Level level = be.getLevel();
			if (level != null) {
				var blockCap = be.getLevel().getCapability(Capabilities.Item.BLOCK, pos, state, be, null);
				if (blockCap != null) {
					for (int i = 0; i < blockCap.size(); i++) {
						Item item = blockCap.getResource(i).getItem();
						if (criteria.itemCriteriaMap.containsKey(item)) {
							ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
							ret.add(new ContainerLocationObject(pos.immutable(),
									criterion.slot(),
									criterion.priority(),
									i,
									state.getBlock().getDescriptionId(),
									item.getDescriptionId(),
									BuiltInRegistries.BLOCK.getKey(state.getBlock())
							));
						}
					}
				}
			}
			return ret;
		}
		
		@Override
		public boolean isAllowed(SearchOptions options) {
			if (!options.get(CompassComponents.CONTAINERS) || !TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get()) {
				return false;
			}
			return options.get(CompassComponents.BLOCK_CONTAINERS);
		}
	};
	
}
