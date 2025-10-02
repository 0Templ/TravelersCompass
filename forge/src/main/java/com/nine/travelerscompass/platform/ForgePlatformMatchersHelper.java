package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.ISearchCriterion;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ContainerLocationObject;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;
import com.nine.travelerscompass.compat.CompatibilityHelper;
import com.nine.travelerscompass.compat.lootr.ForgeLootrHelper;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ForgePlatformMatchersHelper implements IPlatformMatchersHelper {

    public static final EntityMatcher LOOTR_MINECART_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            return ForgeLootrHelper.minecartMatch(criteria, options, entity, level, pos);
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            if (!options.get(CompassProperties.MINECARTS) || !TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get()) {
                return false;
            }
            return CompatibilityHelper.LOOTR_LOADED && options.get(CompassProperties.LOOTR_MODE) != LootrSearchMode.OFF;
        }
    };


    public static final BlockEntityMatcher LOOTR_CONTAINER_MATCHER = new BlockEntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, BlockPos pos, BlockState state, BlockEntity be) {
            return ForgeLootrHelper.matchLootrContainer(criteria, options, pos, state, be);
        }

        @Override
        public boolean isAllowed(SearchOptions options) {
            if (!options.get(CompassProperties.CONTAINERS) || !TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get()) {
                return false;
            }
            return CompatibilityHelper.LOOTR_LOADED && options.get(CompassProperties.LOOTR_MODE) != LootrSearchMode.OFF;
        }
    };

    public static final BlockEntityMatcher CONTAINER_MATCHER = new BlockEntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, BlockPos pos, BlockState state, BlockEntity be) {
            List<ILocationObject> ret = new ArrayList<>();
            if (criteria.itemCriteria.isEmpty()) {
                return List.of();
            }
            LazyOptional<IItemHandler> cap = be.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
            if (cap.isPresent()) {
                var resolved = cap.resolve();
                if (resolved.isPresent()){
                    IItemHandler itemHandler = resolved.get();
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        Item item = itemHandler.getStackInSlot(i).getItem();
                        if (criteria.itemCriteriaMap.containsKey(item)) {
                            ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
                            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(state.getBlock());
                            ret.add(new ContainerLocationObject(pos.immutable(),
                                    criterion.slot(),
                                    criterion.priority(),
                                    i,
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

        @Override
        public boolean isAllowed(SearchOptions options) {
            if (!options.get(CompassProperties.CONTAINERS) || !TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get()) {
                return false;
            }
            return options.get(CompassProperties.CONTAINERS_CHESTS);
        }
    };



    @Override
    public BlockEntityMatcher lootrContainerMatcher() {
        return LOOTR_CONTAINER_MATCHER;
    }

    @Override
    public EntityMatcher lootrMinecartMatcher() {
        return LOOTR_MINECART_MATCHER;
    }

    @Override
    public BlockEntityMatcher containerMatcher() {
        return CONTAINER_MATCHER;
    }

}
