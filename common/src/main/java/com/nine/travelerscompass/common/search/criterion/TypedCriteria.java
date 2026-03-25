package com.nine.travelerscompass.common.search.criterion;

import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.mixin.accessor.BucketItemAccessor;
import com.nine.travelerscompass.platform.Platform;
import com.nine.travelerscompass.platform.accessor.MobBucketItemAccessor;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
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

	// For chunk palette check
	public final Set<Block> placeableBlocks;

	// Extractor registry — add platform/compat extractors via Platform.PLATFORM_MATCHERS
	public static final List<CriterionExtractor> EXTRACTORS = new ArrayList<>(List.of(
			(stack, priority, slot) -> List.of(new ItemCriterion(stack.getItem(), priority, slot)),
			(stack, priority, slot) -> {
				if (stack.getItem() instanceof BlockItem blockItem) {
					return List.of(new BlockCriterion(blockItem.getBlock(), priority, slot));
				}
				return List.of();
			},
			(stack, priority, slot) -> {
				if (stack.getItem() instanceof BucketItem bucketItem) {
					Fluid fluid = ((BucketItemAccessor) bucketItem).travelerscompass$content();
					if (fluid != null) {
						return List.of(new FluidCriterion(fluid, priority, slot));
					}
				}
				return List.of();
			},
			(stack, priority, slot) -> {
				if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
					return List.of(new EntityCriterion(spawnEggItem.getType(stack), priority, slot));
				}
				return List.of();
			},
			(stack, priority, slot) -> {
				if (stack.getItem() instanceof MobBucketItem mobBucketItem) {
					return List.of(new EntityCriterion(
							((MobBucketItemAccessor) mobBucketItem).travelerscompass$getType(), priority, slot));
				}
				return List.of();
			}
	));

	static {
		EXTRACTORS.addAll(Platform.PLATFORM_MATCHERS.criterionExtractors());
	}

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

	private static Map<Item, ISearchCriterion> criteriaToMap(List<? extends ISearchCriterion> list) {
		Map<Item, ISearchCriterion> searchItemsMap = new HashMap<>();
		for (var criterion : list) {
			if (criterion instanceof ItemCriterion itemCriterion) {
				searchItemsMap.put(itemCriterion.item(), criterion);
			}
		}
		return searchItemsMap;
	}

	public static List<ISearchCriterion> extractCriteria(CompassContainer container, ItemStack stack, Level level) {
		List<Integer> array = CompassComponents.get(stack, CompassComponents.PRIORITY_SLOTS);
		Set<Integer> priorSlots = new HashSet<>(array);
		List<ISearchCriterion> ret = new ArrayList<>();

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack cStack = container.getItem(i);
			if (cStack.getItem() instanceof AirItem) continue;
			boolean prior = priorSlots.contains(i);
			for (CriterionExtractor extractor : EXTRACTORS) {
				ret.addAll(extractor.extract(cStack, prior, i));
			}
		}
		return ret;
	}
}
