package com.nine.travelerscompass.client.screen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.component.button.*;
import com.nine.travelerscompass.client.component.button.base.BaseButton;
import com.nine.travelerscompass.client.component.button.hud.HudButton;
import com.nine.travelerscompass.client.component.button.popup.PopupButton;
import com.nine.travelerscompass.client.component.button.range.IntRangeButton;
import com.nine.travelerscompass.client.component.button.search.*;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.ui.constant.TCComponents;
import com.nine.travelerscompass.client.ui.constant.TCIcons;
import com.nine.travelerscompass.client.ui.constant.TCTextures;
import com.nine.travelerscompass.client.utils.ButtonGrid;
import com.nine.travelerscompass.client.utils.GhostStack;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.TabPage;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.cost.SearchCost;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class CompassScreen extends AbstractContainerScreen<CompassMenu> {
	
	private static final Identifier SEARCH_SCREEN_LOCATION = Identifier.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/container/compass_screen_1.png");
	private static final Identifier SETTINGS_SCREEN_LOCATION = Identifier.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/container/compass_screen_2.png");
	public static final Identifier PRIORITY_SLOT = Identifier.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/priority_slot.png");

	private static final List<SearchModeSpec> SEARCH_MODE_SPECS = List.of(
			new SearchModeSpec(0, 0, CompassComponents.BLOCKS, TCConfig.ENABLE_BLOCKS_SEARCH::get,
					TCIcons.SearchMode.BLOCKS_ACTIVE, TCIcons.SearchMode.BLOCKS_INACTIVE,
					SearchModeButton::new, List.of()),
			new SearchModeSpec(1, 0, CompassComponents.MOBS, TCConfig.ENABLE_MOBS_SEARCH::get,
					TCIcons.SearchMode.MOBS_ACTIVE, TCIcons.SearchMode.MOBS_INACTIVE,
					SearchModeButton::new, List.of(
							Component.translatable("tooltip.travelerscompass.search_mode.mobs.desc").withStyle(ChatFormatting.GRAY),
							Component.translatable("tooltip.travelerscompass.search_mode.mobs_shift_click.desc",
									Component.translatable("tooltip.travelerscompass.settings.modification.shift_click")).withStyle(ChatFormatting.GRAY)
					)),
			new SearchModeSpec(2, 1, CompassComponents.FLUIDS, TCConfig.ENABLE_FLUIDS_SEARCH::get,
					TCIcons.SearchMode.FLUIDS_ACTIVE, TCIcons.SearchMode.FLUIDS_INACTIVE,
					FluidSearchModeButton::new, List.of()),
			new SearchModeSpec(1, 1, CompassComponents.SPAWNERS, TCConfig.ENABLE_SPAWNERS_SEARCH::get,
					TCIcons.SearchMode.SPAWNERS_ACTIVE, TCIcons.SearchMode.SPAWNERS_INACTIVE,
					SearchModeButton::new, List.of()),
			new SearchModeSpec(2, 2, CompassComponents.DROP, TCConfig.ENABLE_DROPS_SEARCH::get,
					TCIcons.SearchMode.DROP_ACTIVE, TCIcons.SearchMode.DROP_INACTIVE,
					SearchModeButton::new, List.of()),
			new SearchModeSpec(1, 2, CompassComponents.ITEM_ENTITIES, TCConfig.ENABLE_ITEM_ENTITIES_SEARCH::get,
					TCIcons.SearchMode.ITEM_ENTITIES_ACTIVE, TCIcons.SearchMode.ITEM_ENTITIES_INACTIVE,
					SearchModeButton::new, List.of()),
			new SearchModeSpec(2, 0, CompassComponents.CONTAINERS, TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH::get,
					TCIcons.SearchMode.CONTAINERS_ACTIVE, TCIcons.SearchMode.CONTAINERS_INACTIVE,
					ContainersButton::new, List.of()),
			new SearchModeSpec(0, 1, CompassComponents.VILLAGERS, TCConfig.ENABLE_VILLAGERS_SEARCH::get,
					TCIcons.SearchMode.VILLAGERS_ACTIVE, TCIcons.SearchMode.VILLAGERS_INACTIVE,
					VillagersButton::new, List.of()),
			new SearchModeSpec(0, 2, CompassComponents.INVENTORIES, TCConfig.ENABLE_INVENTORIES_SEARCH::get,
					TCIcons.SearchMode.INVENTORIES_ACTIVE, TCIcons.SearchMode.INVENTORIES_INACTIVE,
					InventoriesButton::new, List.of())
	);

	private static final List<UtilityButtonSpec> SEARCH_UTILITY_SPECS = List.of(
			new UtilityButtonSpec(2, 3, InfoButton::new),
			new UtilityButtonSpec(1, 3, WideSearchButton::new),
			new UtilityButtonSpec(0, 3, PauseButton::new)
	);

	private static final List<ToggleSettingSpec> BOOLEAN_SETTING_SPECS = List.of(
			new ToggleSettingSpec(2, 2, CompassComponents.HEIGHT_MARKER,
					toggleIcons(
							TCIcons.Settings.HEIGHT_MARKER_ACTIVE, TCIcons.Settings.HEIGHT_MARKER_ACTIVE_HOVERED,
							TCIcons.Settings.HEIGHT_MARKER_INACTIVE, TCIcons.Settings.HEIGHT_MARKER_INACTIVE_HOVERED
					)),
			new ToggleSettingSpec(1, 3, CompassComponents.TARGET_VALIDATION,
					toggleIcons(
							TCIcons.Settings.TARGET_VALIDATION_ACTIVE, TCIcons.Settings.TARGET_VALIDATION_ACTIVE_HOVERED,
							TCIcons.Settings.TARGET_VALIDATION_INACTIVE, TCIcons.Settings.TARGET_VALIDATION_INACTIVE_HOVERED
					)),
			new ToggleSettingSpec(0, 2, CompassComponents.SOUND_PING,
					toggleIcons(
							TCIcons.Settings.SOUND_PING_ACTIVE, TCIcons.Settings.SOUND_PING_ACTIVE_HOVERED,
							TCIcons.Settings.SOUND_PING_INACTIVE, TCIcons.Settings.SOUND_PING_INACTIVE_HOVERED
					)),
			new ToggleSettingSpec(2, 1, CompassComponents.FORCE_CHUNKS_LOAD,
					toggleIcons(
							TCIcons.Settings.FORCE_LOAD_ACTIVE, TCIcons.Settings.FORCE_LOAD_ACTIVE_HOVERED,
							TCIcons.Settings.FORCE_LOAD_INACTIVE, TCIcons.Settings.FORCE_LOAD_INACTIVE_HOVERED
					))
	);

	private static final List<RangeSettingSpec> RANGE_SETTING_SPECS = List.of(
			new RangeSettingSpec(1, 0, TCIcons.Settings.CHUNKS_RANGE, TCConfig.BLOCKS_CHUNK_SEARCH_RANGE::get, CompassComponents.BLOCK_SEARCH_CHUNK_RANGE),
			new RangeSettingSpec(2, 0, TCIcons.Settings.ENTITIES_RANGE, TCConfig.ENTITIES_SEARCH_RANGE::get, CompassComponents.ENTITIES_SEARCH_RANGE),
			new RangeSettingSpec(1, 1, TCIcons.Settings.WIDE_SEARCH_RANGE, TCConfig.WIDE_SEARCH_RANGE::get, CompassComponents.WIDE_SEARCH_RANGE)
	);

	private static final List<Consumer<ItemStack>> RESET_ACTIONS = List.of(
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.WIDE_SEARCH_RANGE),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.ENTITIES_SEARCH_RANGE),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.BLOCK_SEARCH_CHUNK_RANGE),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.FORCE_CHUNKS_LOAD),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.SOUND_PING),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.PRIORITY_MODE),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.HEIGHT_MARKER),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.HUD_RENDER_MODE),
			stack -> CompassComponents.putDefaultToServer(stack, CompassComponents.TARGET_VALIDATION)
	);
	
	private enum ButtonCategory {
		SEARCH,
		SETTINGS,
		STATIC;
	}

	@FunctionalInterface
	private interface SearchButtonFactory {
		BaseButton create(ButtonSearchModeSettings settings);
	}

	@FunctionalInterface
	private interface GenericButtonFactory {
		BaseButton create(ButtonGenericSettings settings);
	}

	private record SearchModeSpec(
			int column,
			int row,
			DataStorage<Boolean> data,
			BooleanSupplier configEnabled,
			Icon activeIcon,
			Icon inactiveIcon,
			SearchButtonFactory factory,
			List<Component> descriptions) {

		private SearchModeSpec {
			descriptions = List.copyOf(descriptions);
		}

	}

	private record UtilityButtonSpec(int column, int row, GenericButtonFactory factory) {
	}

	private record ToggleSettingSpec(
			int column,
			int row,
			DataStorage<Boolean> data,
			BiFunction<Boolean, Boolean, Icon> iconProvider) {
	}

	private record RangeSettingSpec(
			int column,
			int row,
			Icon icon,
			IntSupplier maxValue,
			DataStorage<Integer> data) {
	}
	
	private static class ButtonEntry {
		
		public final BaseButton button;
		public final ButtonCategory category;
		public boolean active;
		
		private ButtonEntry(BaseButton button, ButtonCategory category) {
			this.button = button;
			this.category = category;
			this.active = false;
		}
		
		public void setActive(boolean active) {
			this.active = active;
		}
		
	}
	
	public GhostStack ghostStack = GhostStack.EMPTY;
	
	private final List<ButtonEntry> buttonEntries = new ArrayList<>();
	
	private Player player;
	private ItemStack stack;
	private TabPage tabPage;
	private UUID uuid;
	
	private ButtonGrid grid;
	
	private TabButton searchTabButton;
	private TabButton settingsTabButton;
	
	public CompassScreen(CompassMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle, 177, 169);
		this.tabPage = TabPage.SEARCH;
	}
	
	@Override
	protected void init() {
		super.init();
		player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		ItemStack stack = player.getMainHandItem();
		this.stack = stack;
		this.grid = new ButtonGrid(leftPos + 7, topPos + 7, 14, 14, 2, 2);
		this.uuid = CompassComponents.get(stack, CompassComponents.COMPASS_UUID);
		this.tabPage = CompassComponents.get(stack, CompassComponents.TAB_PAGE);
		initButtons();
		revealButtons(ButtonCategory.STATIC);
		revealButtons(contentCategory(tabPage));
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		
		for (var entry : buttonEntries) {
			var button = entry.button;
			if (entry.active) {
				if (button.isMouseOver(mouseX, mouseY)) button.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
				if (button instanceof PopupButton popupButton) {
					if (popupButton.isPopupVisible()) {
						popupButton.popupButtons.forEach(b -> {
							if (b.isMouseOver(mouseX, mouseY)) b.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
						});
					}
				}
			}
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean bl) {
		for (var entry : buttonEntries) {
			if (entry.button instanceof PopupButton popupButton && entry.active) {
				popupButton.popupMouseClicked(event, bl);
				popupButton.afterPopupClick();
			}
		}
		return super.mouseClicked(event, bl);
	}
	
	@Override
	protected void containerTick() {
		for (var entry : buttonEntries) {
			if (entry.active) {
				entry.button.tick();
			}
		}
	}
	
	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
	}
	
	private void initSearchModeButtons() {
		var settings = createSearchModeSettings();
		for (SearchModeSpec spec : SEARCH_MODE_SPECS) {
			addSearchModeButton(settings, spec);
		}
	}
	
	private void initUtilButtons() {
		var settings = createCommonButtonSettings();
		for (UtilityButtonSpec spec : SEARCH_UTILITY_SPECS) {
			addButton(
					spec.factory().create(settings.copy().position(grid.x(spec.column()), grid.y(spec.row())).build()),
					ButtonCategory.SEARCH
			);
		}
	}
	
	private void initSettingsButtons() {
		var settings = createCommonButtonSettings();
		addBooleanSettingsButtons(settings);
		addButton(
				new SettingsButton<>(settings.copy().position(grid.x(1), grid.y(2)).build(),
						CompassComponents.PRIORITY_MODE,
						(cached, hovered) -> switch (cached) {
							case OFF ->
									hovered ? TCIcons.Settings.PRIORITY_OFF_HOVERED : TCIcons.Settings.PRIORITY_OFF;
							case NORMAL ->
									hovered ? TCIcons.Settings.PRIORITY_NORMAL_HOVERED : TCIcons.Settings.PRIORITY_NORMAL;
							case INVERTED ->
									hovered ? TCIcons.Settings.PRIORITY_INVERT_HOVERED : TCIcons.Settings.PRIORITY_INVERT;
						},
						(cached -> "tooltip.travelerscompass.settings.priority_mode." + cached.name().toLowerCase()),
						(cached) -> switch (cached) {
							case OFF -> TCComponents.DISABLED;
							case NORMAL -> TCComponents.ENABLED;
							case INVERTED -> TCComponents.INVERTED;
						}),
				ButtonCategory.SETTINGS);
		
		addButton(
				new ResetButton(settings.copy().position(grid.x(2), grid.y(3))
						.afterLeftClick((button ->
						{
							HudButton.resetHudSettings(stack);
							resetSettings(stack);
							updateButtons(ButtonCategory.SETTINGS);
							ClientCache.updateAllHudSettings(stack, true);
						}))
						.build()),
				ButtonCategory.SETTINGS);
		
		addButton(
				new HudButton(settings.copy().position(grid.x(0), grid.y(3)).build()),
				ButtonCategory.SETTINGS);
		
		addRangeSettingsButtons(createRangeSettings(settings));
	}
	
	
	private void initTabButtons() {
		ButtonGenericSettings.Builder<?> settings = ButtonGenericSettings.builder()
				.size(26, 24)
				.stackSup(() -> stack);
		
		this.searchTabButton = addButton(new TabButton(
				settings.position(leftPos + 130, topPos + 8)
						.afterLeftClick(b -> switchTab(TabPage.SEARCH))
						.build(),
				tabPage == TabPage.SEARCH, Icon.of(TCTextures.Common.SEARCH, 6, 4), Component.translatable("tooltip.travelerscompass.search")), ButtonCategory.STATIC);
		
		this.settingsTabButton = addButton(new TabButton(
				settings.position(leftPos + 130, topPos + 42)
						.afterLeftClick(b -> switchTab(TabPage.SETTINGS))
						.build(),
				tabPage == TabPage.SETTINGS, Icon.of(TCTextures.Common.SETTINGS, 6, 4), Component.translatable("tooltip.travelerscompass.settings")), ButtonCategory.STATIC);
		
		
	}
	
	private void initButtons() {
		buttonEntries.clear();
		initTabButtons();
		initSearchModeButtons();
		initUtilButtons();
		initSettingsButtons();
		
	}
	
	public static void resetSettings(ItemStack stack) {
		for (Consumer<ItemStack> action : RESET_ACTIONS) {
			action.accept(stack);
		}
	}

	private static ButtonCategory contentCategory(TabPage page) {
		return page == TabPage.SETTINGS ? ButtonCategory.SETTINGS : ButtonCategory.SEARCH;
	}

	private static BiFunction<Boolean, Boolean, Icon> toggleIcons(Icon active, Icon activeHovered, Icon inactive, Icon inactiveHovered) {
		return (cached, hovered) -> cached
				? (hovered ? activeHovered : active)
				: (hovered ? inactiveHovered : inactive);
	}

	private ButtonSearchModeSettings.Builder createSearchModeSettings() {
		return ButtonSearchModeSettings.builder()
				.size(14, 14)
				.stackSup(() -> stack)
				.uuid(uuid)
				.mainLayerSet(TCTextures.Buttons.TOGGLE_BUTTON)
				.lockIcon(TCIcons.Common.LOCK);
	}

	private ButtonGenericSettings.Builder<?> createCommonButtonSettings() {
		return ButtonGenericSettings.builder()
				.uuid(uuid)
				.size(14, 14)
				.mainLayerSet(TCTextures.Buttons.TOGGLE_BUTTON)
				.stackSup(() -> stack);
	}

	private ButtonRangeSettings.Builder createRangeSettings(ButtonGenericSettings.Builder<?> settings) {
		return ButtonRangeSettings.builder(settings)
				.minusIcons(TCIcons.Common.MINUS, TCIcons.Common.MINUS_HOVERED, TCIcons.Common.MINUS_INACTIVE)
				.plusIcons(TCIcons.Common.PLUS, TCIcons.Common.PLUS_HOVERED, TCIcons.Common.PLUS_INACTIVE);
	}

	private void addSearchModeButton(ButtonSearchModeSettings.Builder settings, SearchModeSpec spec) {
		var builder = settings.copy()
				.position(grid.x(spec.column()), grid.y(spec.row()))
				.data(spec.data())
				.configEnabled(spec.configEnabled().getAsBoolean())
				.icons(spec.activeIcon(), spec.inactiveIcon());
		if (!spec.descriptions().isEmpty()) {
			builder.descriptions(spec.descriptions().toArray(Component[]::new));
		}
		addButton(spec.factory().create(builder.build()), ButtonCategory.SEARCH);
	}

	private void addBooleanSettingsButtons(ButtonGenericSettings.Builder<?> settings) {
		for (ToggleSettingSpec spec : BOOLEAN_SETTING_SPECS) {
			addButton(
					SettingsButton.create(
							settings.copy().position(grid.x(spec.column()), grid.y(spec.row())).build(),
							spec.data(),
							spec.iconProvider()
					),
					ButtonCategory.SETTINGS
			);
		}
	}

	private void addRangeSettingsButtons(ButtonRangeSettings.Builder rangeSettings) {
		for (RangeSettingSpec spec : RANGE_SETTING_SPECS) {
			addButton(
					new IntRangeButton(
							rangeSettings.copy().position(grid.x(spec.column()), grid.y(spec.row()))
									.icon(spec.icon())
									.build(),
							1,
							spec.maxValue().getAsInt(),
							1,
							5,
							spec.data()
					),
					ButtonCategory.SETTINGS
			);
		}
	}

	private void switchTab(TabPage targetPage) {
		if (tabPage == targetPage) {
			return;
		}
		searchTabButton.selected = targetPage == TabPage.SEARCH;
		settingsTabButton.selected = targetPage == TabPage.SETTINGS;
		CompassComponents.TAB_PAGE.syncToServer(stack, targetPage);
		tabPage = targetPage;
		revealButtons(contentCategory(targetPage));
		hideButtons(contentCategory(targetPage == TabPage.SEARCH ? TabPage.SETTINGS : TabPage.SEARCH));
	}
	
	private void updateButtons(ButtonCategory category) {
		for (var entry : buttonEntries) {
			if (category.equals(entry.category)) {
				var button = entry.button;
				button.updateState();
				if (button instanceof PopupButton popupButton) {
					popupButton.updatePopupButtons();
				}
			}
		}
	}
	
	private void revealButtons(ButtonCategory category) {
		for (var entry : buttonEntries) {
			if (category.equals(entry.category)) {
				entry.setActive(true);
				addRenderableWidget(entry.button);
			}
		}
	}
	
	private void hideButtons(ButtonCategory category) {
		for (var entry : buttonEntries) {
			if (category.equals(entry.category)) {
				entry.setActive(false);
				removeWidget(entry.button);
			}
		}
	}
	
	private <T extends BaseButton> T addButton(T button, ButtonCategory category) {
		buttonEntries.add(new ButtonEntry(button, category));
		return button;
	}
	
	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		extractTooltip(guiGraphics, mouseX, mouseY);
		if (minecraft != null && minecraft.player != null) {
			if (minecraft.player.getMainHandItem().getItem() instanceof TravelersCompassItem) {
				this.stack = minecraft.player.getMainHandItem();
			}
		}
	}
	
	@Override
	protected void extractTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		super.extractTooltip(guiGraphics, mouseX, mouseY);
		Slot slot = hoveredSlot;
		if (slot != null && slot.container instanceof CompassContainer) {
			ItemStack carriedStack = getMenu().getCarried();
			if (carriedStack.isEmpty()) {
				if (ghostStack.valid()) {
					if (!ghostStack.allowed()) {
						guiGraphics.setTooltipForNextFrame(this.font,
								Component.translatable("nei.travelerscompass.dragging_disabled",
												Component.translatable("nei.travelerscompass." + ghostStack.nei().name().toLowerCase()))
										.withStyle(ChatFormatting.RED), mouseX, mouseY, carriedStack.get(DataComponents.TOOLTIP_STYLE));
						return;
					}
					carriedStack = ghostStack.stack();
				}
			}
			if (carriedStack.isEmpty() || !TCConfig.SEARCH_REQUIRES_XP.get()) {
				return;
			}
			Item carried = carriedStack.getItem();
			SearchCost sc = SearchCostHelper.getCost(carried);
			FilterReason filterReason = FilterManager.getFilterReason(carried);
			if (filterReason.isAllowed()) {
				if (!sc.isFree() && !player.isCreative()) {
					List<Component> extra = new ArrayList<>();
					MutableComponent req = sc.reqAsComponent()
							.withStyle(sc.meetsRequirement(player.experienceLevel) ? ChatFormatting.GRAY : ChatFormatting.RED);
					MutableComponent cost = sc.costAsComponent().withStyle(ChatFormatting.GRAY);
					extra.add(req);
					extra.add(cost);
					guiGraphics.setComponentTooltipForNextFrame(this.font, extra, mouseX, mouseY, carriedStack.get(DataComponents.TOOLTIP_STYLE));
				}
			} else {
				guiGraphics.setTooltipForNextFrame(this.font, ClientCache.formatFilterReason(filterReason), mouseX, mouseY, carriedStack.get(DataComponents.TOOLTIP_STYLE));
			}
		}
		
	}
	
	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		Player player = Minecraft.getInstance().player;
		if (player != null && stack != null) {
			int w = (this.width - this.imageWidth) / 2;
			int h = (this.height - this.imageHeight) / 2;
			graphics.blit(RenderPipelines.GUI_TEXTURED, tabPage.equals(TabPage.SEARCH) ? SEARCH_SCREEN_LOCATION : SETTINGS_SCREEN_LOCATION, w, h, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
			for (int i : CompassComponents.get(stack, CompassComponents.PRIORITY_SLOTS)) {
				int j = i > 2 ? 1 : 0;
				j = i > 5 ? 2 : j;
				int c = i > 2 ? i - 3 : i;
				c = i > 5 ? i - 6 : c;
				graphics.blit(RenderPipelines.GUI_TEXTURED, PRIORITY_SLOT, leftPos + 72 + c * 18, topPos + 10 + j * 18, 0, 0, 18, 18, 18, 18);
			}
		}
	}
	
	
	public int leftPos() {
		return leftPos;
	}
	
	public int topPos() {
		return topPos;
	}
	
}
