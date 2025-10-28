package com.nine.travelerscompass.client.component.button.settings;

import com.nine.travelerscompass.client.component.button.base.BaseButton;
import com.nine.travelerscompass.client.utils.ButtonTexturesSet;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ButtonGenericSettings {
	
	public final int x, y, width, height;
	public final Supplier<ItemStack> supplier;
	public final Consumer<BaseButton> afterLeftClick;
	public final Consumer<BaseButton> afterRightClick;
	public final Consumer<BaseButton> afterInteraction;
	public final ButtonTexturesSet buttonTexturesSet;
	
	public final UUID uuid;
	
	public ButtonGenericSettings(Builder<?> builder) {
		this.x = builder.x;
		this.y = builder.y;
		this.width = builder.width;
		this.height = builder.height;
		this.supplier = builder.supplier;
		this.afterLeftClick = builder.afterLeftClick;
		this.afterRightClick = builder.afterRightClick;
		this.afterInteraction = builder.afterInteraction;
		this.uuid = builder.uuid;
		this.buttonTexturesSet = builder.buttonTexturesSet;
	}
	
	public static Builder<?> builder() {
		return new Builder<>();
	}
	
	public static class Builder<T extends Builder<T>> {
		
		protected int x, y;
		protected int width, height;
		protected Supplier<ItemStack> supplier;
		protected Consumer<BaseButton> afterLeftClick;
		protected Consumer<BaseButton> afterRightClick;
		protected Consumer<BaseButton> afterInteraction;
		protected UUID uuid;
		protected ButtonTexturesSet buttonTexturesSet;
		
		@SuppressWarnings("unchecked")
		protected T self() {
			return (T) this;
		}
		
		public T afterLeftClick(Consumer<BaseButton> afterLeftClick) {
			this.afterLeftClick = afterLeftClick;
			return self();
		}
		
		public T afterRightClick(Consumer<BaseButton> afterRightClick) {
			this.afterRightClick = afterRightClick;
			return self();
		}
		
		public T afterInteraction(Consumer<BaseButton> afterAnyClick) {
			this.afterInteraction = afterAnyClick;
			return self();
		}
		
		public T mainLayerSet(ButtonTexturesSet set) {
			this.buttonTexturesSet = set;
			return self();
		}
		
		public T position(int x, int y) {
			this.x = x;
			this.y = y;
			return self();
		}
		
		public T moved(int x, int y) {
			this.x += x;
			this.y += y;
			return self();
		}
		
		public T size(int width, int height) {
			this.width = width;
			this.height = height;
			return self();
		}
		
		public T stackSup(Supplier<ItemStack> supplier) {
			this.supplier = supplier;
			return self();
		}
		
		public T uuid(UUID uuid) {
			this.uuid = uuid;
			return self();
		}
		
		public void copyParentTo(Builder<?> target) {
			target.x = this.x;
			target.y = this.y;
			target.width = this.width;
			target.height = this.height;
			target.supplier = this.supplier;
			target.afterLeftClick = this.afterLeftClick;
			target.afterRightClick = this.afterRightClick;
			target.afterInteraction = this.afterInteraction;
			target.uuid = this.uuid;
			target.buttonTexturesSet = this.buttonTexturesSet;
		}
		
		@SuppressWarnings("unchecked")
		public T copy() {
			T copy = (T) new ButtonGenericSettings.Builder<>();
			copyParentTo(copy);
			return copy;
		}
		
		public ButtonGenericSettings build() {
			return new ButtonGenericSettings(self());
		}
		
	}
	
}
