package com.nine.travelerscompass.config.filter;

import com.nine.travelerscompass.client.ui.constant.TCComponents;

import com.nine.travelerscompass.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public sealed interface FilterReason {
	
	boolean isAllowed();
	
	Component toComponent();
	
	record Allowed() implements FilterReason {
		
		@Override
		public boolean isAllowed() {
			return true;
		}
		
		@Override
		public Component toComponent() {
			return Component.empty();
		}
		
	}
	
	record ByModId(String modId) implements FilterReason {
		
		@Override
		public boolean isAllowed() {
			return false;
		}
		
		@Override
		public Component toComponent() {
			return Component.translatable("tooltip.travelerscompass.config.filter_reason.mod",
					Platform.PLATFORM.getModName(modId)).withStyle(ChatFormatting.RED);
		}
		
	}
	
	record ByItemId(Identifier id) implements FilterReason {
		
		@Override
		public boolean isAllowed() {
			return false;
		}
		
		@Override
		public Component toComponent() {
			return TCComponents.FILTER_BY_ITEM_ID;
		}
		
	}
	
	record ByTag(TagKey<Item> tag) implements FilterReason {
		
		@Override
		public boolean isAllowed() {
			return false;
		}
		
		@Override
		public Component toComponent() {
			return Component.translatable("tooltip.travelerscompass.config.filter_reason.tag", tag.location().toString())
					.withStyle(ChatFormatting.RED);
		}
		
	}
	
	record ByEntityType(EntityType<?> type) implements FilterReason {
		
		@Override
		public boolean isAllowed() {
			return false;
		}
		
		@Override
		public Component toComponent() {
			Component entityName = type.getDescription();
			return Component.translatable("tooltip.travelerscompass.config.filter_reason.entity", entityName)
					.withStyle(ChatFormatting.RED);
		}
		
	}
	
}
