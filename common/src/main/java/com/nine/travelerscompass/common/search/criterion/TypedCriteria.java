package com.nine.travelerscompass.common.search.criterion;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.mixin.accessor.BucketItemAccessor;
import com.nine.travelerscompass.platform.accessor.MobBucketItemAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.stream.Collectors;

public class TypedCriteria {

    public final Set<EntityCriterion> entityCriteria;

    public final Set<ItemCriterion> itemCriteria;

    public final Set<FluidCriterion> fluidCriteria;

    public final Set<BlockCriterion> blockCriteria;

    public final Map<Item, ISearchCriterion> itemCriteriaMap;

    //For chunk palette check
    public final Set<Block> placeableBlocks;

    public TypedCriteria(List<ISearchCriterion> criteria) {

        this.entityCriteria = quickFill(criteria, EntityCriterion.class);
        this.itemCriteria = quickFill(criteria, ItemCriterion.class);
        this.fluidCriteria = quickFill(criteria, FluidCriterion.class);
        this.blockCriteria = quickFill(criteria, BlockCriterion.class);

        this.itemCriteriaMap = criteriaToMap(criteria);

        this.placeableBlocks = criteria.stream()
                .filter(c -> c instanceof IPlaceableCriterion)
                .map(c -> ((IPlaceableCriterion) c).asBlock())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

    }

    private static <T> Set<T> quickFill(List<ISearchCriterion> list, Class<T> type) {
        return list.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toSet());
    }

    private static Map<Item, ISearchCriterion> criteriaToMap(List<? extends ISearchCriterion> list){
        Map<Item, ISearchCriterion> searchItemsMap = new HashMap<>();
        for (var criterion : list) {
            if (criterion instanceof ItemCriterion itemCriterion) {
                searchItemsMap.put(itemCriterion.item(), criterion);
            }
        }
        return searchItemsMap;
    }

    public static List<ISearchCriterion> extractCriteria(CompassContainer container, ItemStack stack){
        int[] array = CompassProperties.get(stack, CompassProperties.PRIORITY_SLOTS);
        var ret = new ArrayList<ISearchCriterion>();
        Set<Integer> priorSlots = new HashSet<>();
        for (int slot : array) {
            priorSlots.add(slot);
        }
        for (int i = 0; i < container.getContainerSize(); i++){
            ItemStack cStack = container.getItem(i);
            Item item = cStack.getItem();
            if (item instanceof AirItem) continue;
            boolean prior = priorSlots.contains(i);

            ret.add(new ItemCriterion(item, prior, i));
            if (item instanceof BlockItem blockItem){
                ret.add(new BlockCriterion(blockItem.getBlock(), prior, i));
            }
            if (item instanceof BucketItem bucketItem){
                Fluid fluid = ((BucketItemAccessor)bucketItem).travelerscompass$content();
                if (fluid != null){
                    ret.add(new FluidCriterion(fluid, prior, i));
                }
            }
            if (item instanceof SpawnEggItem spawnEggItem){
                ret.add(new EntityCriterion(spawnEggItem.getType(null), prior, i));
            }
            if (item instanceof MobBucketItem mobBucketItem){
                EntityType<?> type = ((MobBucketItemAccessor) mobBucketItem).travelerscompass$getType();
                ret.add(new EntityCriterion(type, prior, i));
            }
        }
        return ret;
    }
}
