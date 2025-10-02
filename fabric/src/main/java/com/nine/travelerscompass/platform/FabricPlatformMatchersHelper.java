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
import com.nine.travelerscompass.compat.lootr.FabricLootrHelper;
import com.nine.travelerscompass.compat.lootr.LootrSearchMode;
import com.nine.travelerscompass.config.TCConfig;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class FabricPlatformMatchersHelper implements IPlatformMatchersHelper {


    public static final EntityMatcher LOOTR_MINECART_MATCHER = new EntityMatcher() {

        @Override
        public List<ILocationObject> match(TypedCriteria criteria, SearchOptions options, Entity entity, Level level, BlockPos pos) {
            return FabricLootrHelper.minecartMatch(criteria, options, entity, level, pos);
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
            return FabricLootrHelper.matchLootrContainer(criteria, options, pos, state, be);
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
                Storage<ItemVariant> storage = ItemStorage.SIDED.find(be.getLevel(), pos, state, be, null);
                if (storage == null){
                    return ret;
                }
                int index = -1;
                for (StorageView<ItemVariant> variantStorageView : storage) {
                    index++;
                    if (variantStorageView.isResourceBlank()){
                        continue;
                    }
                    Item item = variantStorageView.getResource().getItem();
                    if (criteria.itemCriteriaMap.containsKey(item)){
                        ISearchCriterion criterion = criteria.itemCriteriaMap.get(item);
                        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
                        ret.add(new ContainerLocationObject(pos.immutable(),
                                criterion.slot(),
                                criterion.priority(),
                                index,
                                state.getBlock().getDescriptionId(),
                                item.getDescriptionId(),
                                id
                        ));
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
    public BlockEntityMatcher containerMatcher() {
        return CONTAINER_MATCHER;
    }

    @Override
    public BlockEntityMatcher lootrContainerMatcher() {
        return LOOTR_CONTAINER_MATCHER;
    }

    @Override
    public EntityMatcher lootrMinecartMatcher() {
        return LOOTR_MINECART_MATCHER;
    }
}
