package com.nine.travelerscompass.client.render.item;

import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.mixin.client.accessor.CreativeModeInventoryScreenAccessor;
import com.nine.travelerscompass.mixin.client.accessor.GuiGraphicsExtractorAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2f;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CompassRenderContext {

	private static final RenderContext INACTIVE = new RenderContext(false, false, 0.0F);
	private static final ThreadLocal<Deque<RenderContext>> CONTEXTS = new ThreadLocal<>();
	private static final ThreadLocal<Boolean> CREATIVE_SLOT = new ThreadLocal<>();
	
	private static final StackWalker NEI_WALKER = StackWalker.getInstance();

	private CompassRenderContext() {
	}

	public static void begin(GuiGraphicsExtractor graphics, ItemStack stack, int x, int y) {
		if (!(stack.getItem() instanceof TravelersCompassItem)) {
			return;
		}
		
		RenderContext context = INACTIVE;
		if (isCatalogueRender()) {
			context = createContext(graphics, x, y);
		}
		Deque<RenderContext> contexts = CONTEXTS.get();
		if (contexts == null) {
			contexts = new ArrayDeque<>();
			CONTEXTS.set(contexts);
		}
		contexts.push(context);
	}

	public static void beginSlot(Object screen, Slot slot) {
		if (screen instanceof CreativeModeInventoryScreen creativeScreen) {
			boolean catalogueSlot = !creativeScreen.isInventoryOpen()
					&& ((CreativeModeInventoryScreenAccessor) creativeScreen )
					.travelerscompass$isCreativeSlot(slot);
			CREATIVE_SLOT.set(catalogueSlot);
		}
	}

	public static void end(ItemStack stack) {
		if (!(stack.getItem() instanceof TravelersCompassItem)) {
			return;
		}
		Deque<RenderContext> contexts = CONTEXTS.get();
		if (contexts != null && !contexts.isEmpty()) {
			contexts.pop();
		}
		if (contexts != null && contexts.isEmpty()) {
			CONTEXTS.remove();
		}
	}

	public static void endSlot(Object screen) {
		if (screen instanceof CreativeModeInventoryScreen) {
			CREATIVE_SLOT.remove();
		}
	}

	public static boolean isActive() {
		return current().active();
	}

	public static boolean isHovered() {
		return current().hovered();
	}

	public static float angle() {
		return current().angle();
	}

	private static RenderContext current() {
		Deque<RenderContext> contexts = CONTEXTS.get();
		return contexts == null || contexts.isEmpty() ? INACTIVE : contexts.peek();
	}

	private static RenderContext createContext(GuiGraphicsExtractor graphics, int x, int y) {
		int slotSize = 16;
		Vector2f topLeft = graphics.pose().transformPosition(x, y, new Vector2f());
		Vector2f topRight = graphics.pose().transformPosition(x + slotSize, y, new Vector2f());
		Vector2f bottomLeft = graphics.pose().transformPosition(x, y + slotSize, new Vector2f());
		Vector2f bottomRight = graphics.pose().transformPosition(x + slotSize, y + 16.0F, new Vector2f());
		Vector2f center = graphics.pose().transformPosition(x + 8.0F, y + 8.0F, new Vector2f());

		float minX = Math.min(Math.min(topLeft.x, topRight.x), Math.min(bottomLeft.x, bottomRight.x));
		float maxX = Math.max(Math.max(topLeft.x, topRight.x), Math.max(bottomLeft.x, bottomRight.x));
		float minY = Math.min(Math.min(topLeft.y, topRight.y), Math.min(bottomLeft.y, bottomRight.y));
		float maxY = Math.max(Math.max(topLeft.y, topRight.y), Math.max(bottomLeft.y, bottomRight.y));

		GuiGraphicsExtractorAccessor accessor = (GuiGraphicsExtractorAccessor) graphics;
		float mouseX = accessor.travelerscompass$getMouseX();
		float mouseY = accessor.travelerscompass$getMouseY();
		boolean hovered = mouseX >= minX && mouseX < maxX && mouseY >= minY && mouseY < maxY;

		float angle = (float) (Math.atan2(mouseX - center.x, center.y - mouseY) / (Math.PI * 2.0));
		return new RenderContext(true, hovered, Mth.positiveModulo(angle, 1.0F));
	}

	private static boolean isCatalogueRender() {
		if (Boolean.TRUE.equals(CREATIVE_SLOT.get())) {
			return true;
		}

		return NEI_WALKER.walk(frames -> frames
				.limit(48)
				.map(StackWalker.StackFrame::getClassName)
				.anyMatch(CompassRenderContext::isNeiClass));
	}

	private static boolean isNeiClass(String className) {
		return className.startsWith("mezz.jei.")
				|| className.startsWith("me.shedaniel.rei.")
				|| className.startsWith("dev.emi.emi.");
	}

	private record RenderContext(boolean active, boolean hovered, float angle) {
	}
}
