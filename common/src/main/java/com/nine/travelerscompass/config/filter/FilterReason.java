package com.nine.travelerscompass.config.filter;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public sealed interface FilterReason {

    record Allowed() implements FilterReason {
    }

    record ByModId(String modId) implements FilterReason {
    }

    record ByItemId(ResourceLocation itemId) implements FilterReason {
    }

    record ByTag(TagKey<Item> tag) implements FilterReason {
    }

    record ByEntityType(EntityType<?> type) implements FilterReason {
    }

}
