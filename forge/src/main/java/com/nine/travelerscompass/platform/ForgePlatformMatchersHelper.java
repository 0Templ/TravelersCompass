package com.nine.travelerscompass.platform;

import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchOptions;
import com.nine.travelerscompass.common.search.criterion.ISearchCriterion;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ContainerLocationObject;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.BlockMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;
import com.nine.travelerscompass.config.TCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ForgePlatformMatchersHelper implements IPlatformMatchersHelper {

    @Override
    public List<BlockEntityMatcher> blockEntityMatchers(){
        return List.of(CONTAINER_MATCHER);
    }

    @Override
    public List<BlockMatcher> blockMatchers(){
        return List.of();

    }

    @Override
    public List<EntityMatcher> entityMatchers(){
        return List.of();
    }

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

}
