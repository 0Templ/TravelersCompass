package com.nine.travelerscompass.client.screen;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.CompassUI;
import com.nine.travelerscompass.client.component.button.*;
import com.nine.travelerscompass.client.component.button.base.BaseButton;
import com.nine.travelerscompass.client.component.button.hud.HudButton;
import com.nine.travelerscompass.client.component.button.popup.PopupButton;
import com.nine.travelerscompass.client.component.button.range.IntRangeButton;
import com.nine.travelerscompass.client.component.button.search.*;
import com.nine.travelerscompass.client.component.button.settings.ButtonGenericSettings;
import com.nine.travelerscompass.client.component.button.settings.ButtonRangeSettings;
import com.nine.travelerscompass.client.component.button.settings.ButtonSearchModeSettings;
import com.nine.travelerscompass.client.utils.ButtonGrid;
import com.nine.travelerscompass.client.utils.Icon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.TabPage;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.cost.SearchCost;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CompassScreen extends AbstractContainerScreen<CompassMenu> {
	
	private static final ResourceLocation SEARCH_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/container/compass_screen_1.png");
	private static final ResourceLocation SETTINGS_SCREEN_LOCATION = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/container/compass_screen_2.png");
	public static final ResourceLocation PRIORITY_SLOT = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/priority_slot.png");
	
	private enum ButtonCategory {
		SEARCH,
		SETTINGS,
		STATIC;
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
	
	public ItemStack ghostStack = ItemStack.EMPTY;
	
	private final List<ButtonEntry> buttonEntries = new ArrayList<>();
	
	private Player player;
	private ItemStack stack;
	private TabPage tabPage;
	private UUID uuid;
	
	private ButtonGrid grid;
	
	private TabButton searchTabButton;
	private TabButton settingsTabButton;
	
	private WarningButton warningButton;
	
	private SearchModeButton blocksSearchButton;
	private SearchModeButton mobsSearchButton;
	private ContainersButton containersSearchButton;
	private FluidSearchModeButton fluidsSearchButton;
	private SearchModeButton spawnersSearchButton;
	private SearchModeButton dropSearchButton;
	private SearchModeButton itemEntitiesSearchButton;
	private InventoriesButton inventoriesSearchButton;
	//
	private VillagersButton villagersSearchButton;
	
	private PauseButton pauseButton;
	private WideSearchButton wideSearchButton;
	private InfoButton infoButton;
	
	//Settings
	private HudButton hudButton;
	private SettingsButton forceChunksLoadButton;
	private SettingsButton targetValidationButton;
	private SettingsButton soundPingButton;
	private SettingsButton heightMarkerButton;
	private SettingsButton priorityButton;
	private IntRangeButton chunksSearchRangeButton;
	private IntRangeButton entitiesSearchRangeButton;
	private IntRangeButton wideSearchRangeButton;
	
	public CompassScreen(CompassMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		imageWidth = 177;
		imageHeight = 169;
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
		this.uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
		this.tabPage = CompassProperties.get(stack, CompassProperties.TAB_PAGE);
		initButtons();
		revealButtons(ButtonCategory.STATIC);
		revealButtons(tabPage == TabPage.SETTINGS ? ButtonCategory.SETTINGS : ButtonCategory.SEARCH);
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
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (var entry : buttonEntries) {
			if (entry.button instanceof PopupButton popupButton && entry.active) {
				popupButton.popupMouseClicked(mouseX, mouseY, button);
				popupButton.afterPopupClick();
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
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
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}
	
	private void initSearchModeButtons() {
		ButtonSearchModeSettings.Builder settings = ButtonSearchModeSettings.builder()
				.size(14, 14)
				.stackSup(() -> stack)
				.uuid(uuid)
				.mainLayerSet(CompassUI.ButtonTextures.TOGGLE_BUTTON)
				.lockIcon(CompassUI.CommonTextures.LOCK_ICON);
		
		this.blocksSearchButton = addButton(
				new SearchModeButton(settings.copy().position(grid.x(0), grid.y(0))
						.data(CompassProperties.BLOCKS)
						.configEnabled(TCConfig.ENABLE_BLOCKS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.BLOCKS_ACTIVE_ICON, CompassUI.SearchModeTextures.BLOCKS_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.blocksSearchButton = addButton(
				new SearchModeButton(settings.copy().position(grid.x(1), grid.y(0))
						.descriptions(
								Component.translatable("tooltip.travelerscompass.search_mode.mobs.desc").withStyle(ChatFormatting.GRAY),
								Component.translatable("tooltip.travelerscompass.search_mode.mobs_shift_click.desc",
										Component.translatable("tooltip.travelerscompass.settings.modification.shift_click")).withStyle(ChatFormatting.GRAY)
						)
						.data(CompassProperties.MOBS)
						.configEnabled(TCConfig.ENABLE_MOBS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.MOBS_ACTIVE_ICON, CompassUI.SearchModeTextures.MOBS_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.fluidsSearchButton = addButton(
				new FluidSearchModeButton(settings.copy().position(grid.x(2), grid.y(1))
						.data(CompassProperties.FLUIDS)
						.configEnabled(TCConfig.ENABLE_FLUIDS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.FLUIDS_ACTIVE_ICON, CompassUI.SearchModeTextures.FLUIDS_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.spawnersSearchButton = addButton(
				new SearchModeButton(settings.copy().position(grid.x(1), grid.y(1))
						.data(CompassProperties.SPAWNERS)
						.configEnabled(TCConfig.ENABLE_SPAWNERS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.SPAWNERS_ACTIVE_ICON, CompassUI.SearchModeTextures.SPAWNERS_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		
		this.dropSearchButton = addButton(
				new SearchModeButton(settings.copy().position(grid.x(2), grid.y(2))
						.data(CompassProperties.DROP)
						.configEnabled(TCConfig.ENABLE_DROPS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.DROP_ACTIVE_ICON, CompassUI.SearchModeTextures.DROP_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.itemEntitiesSearchButton = addButton(
				new SearchModeButton(settings.copy().position(grid.x(1), grid.y(2))
						.data(CompassProperties.ITEM_ENTITIES)
						.configEnabled(TCConfig.ENABLE_ITEM_ENTITIES_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.ITEM_ENTITIES_ACTIVE_ICON, CompassUI.SearchModeTextures.ITEM_ENTITIES_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.containersSearchButton = addButton(
				new ContainersButton(settings.copy().position(grid.x(2), grid.y(0))
						.data(CompassProperties.CONTAINERS)
						.configEnabled(TCConfig.ENABLE_BLOCK_CONTAINERS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.CONTAINERS_ACTIVE_ICON, CompassUI.SearchModeTextures.CONTAINERS_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.villagersSearchButton = addButton(
				new VillagersButton(settings.copy().position(grid.x(0), grid.y(1))
						.data(CompassProperties.VILLAGERS)
						.configEnabled(TCConfig.ENABLE_VILLAGERS_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.VILLAGERS_ACTIVE_ICON, CompassUI.SearchModeTextures.VILLAGERS_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
		this.inventoriesSearchButton = addButton(
				new InventoriesButton(settings.copy().position(grid.x(0), grid.y(2))
						.data(CompassProperties.INVENTORIES)
						.configEnabled(TCConfig.ENABLE_INVENTORIES_SEARCH.get())
						.icons(CompassUI.SearchModeTextures.INVENTORIES_ACTIVE_ICON, CompassUI.SearchModeTextures.INVENTORIES_INACTIVE_ICON)
						.build()), ButtonCategory.SEARCH);
		
	}
	
	private void initUtilButtons() {
		
		ButtonGenericSettings.Builder<?> settings = ButtonGenericSettings.builder()
				.uuid(uuid)
				.size(14, 14)
				.mainLayerSet(CompassUI.ButtonTextures.TOGGLE_BUTTON)
				.stackSup(() -> stack);
		
		
		this.infoButton = addButton(new InfoButton(settings.copy().position(grid.x(2), grid.y(3)).build()), ButtonCategory.SEARCH);
		this.wideSearchButton = addButton(new WideSearchButton(settings.copy().position(grid.x(1), grid.y(3)).build()), ButtonCategory.SEARCH);
		
		this.pauseButton = addButton(new PauseButton(settings.copy().position(grid.x(0), grid.y(3)).build()), ButtonCategory.SEARCH);
		
	}
	
	private void initSettingsButtons() {
		ButtonGenericSettings.Builder<?> settings = ButtonGenericSettings.builder()
				.uuid(uuid)
				.size(14, 14)
				.mainLayerSet(CompassUI.ButtonTextures.TOGGLE_BUTTON)
				.stackSup(() -> stack);
		
		this.heightMarkerButton = addButton(
				SettingsButton.create(settings.copy().position(grid.x(2), grid.y(2)).build(),
						CompassProperties.HEIGHT_MARKER,
						(cached, hovered) -> {
							if (cached) {
								return hovered ? CompassUI.SettingsTextures.HEIGHT_MARKER_ACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.HEIGHT_MARKER_ACTIVE_ICON;
							}
							return hovered ? CompassUI.SettingsTextures.HEIGHT_MARKER_INACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.HEIGHT_MARKER_INACTIVE_ICON;
						}),
				ButtonCategory.SETTINGS);
		
		this.targetValidationButton = addButton(
				SettingsButton.create(settings.copy().position(grid.x(1), grid.y(3)).build(),
						CompassProperties.TARGET_VALIDATION,
						(cached, hovered) -> {
							if (cached) {
								return hovered ? CompassUI.SettingsTextures.TARGET_VALIDATION_ACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.TARGET_VALIDATION_ACTIVE_ICON;
							}
							return hovered ? CompassUI.SettingsTextures.TARGET_VALIDATION_INACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.TARGET_VALIDATION_INACTIVE_ICON;
						}),
				ButtonCategory.SETTINGS);
		
		this.soundPingButton = addButton(
				SettingsButton.create(settings.copy().position(grid.x(0), grid.y(2)).build(),
						CompassProperties.SOUND_PING,
						(cached, hovered) -> {
							if (cached) {
								return hovered ? CompassUI.SettingsTextures.SOUND_PING_ACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.SOUND_PING_ACTIVE_ICON;
							}
							return hovered ? CompassUI.SettingsTextures.SOUND_PING_INACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.SOUND_PING_INACTIVE_ICON;
						}),
				ButtonCategory.SETTINGS);
		
		this.forceChunksLoadButton = addButton(
				SettingsButton.create(settings.copy().position(grid.x(2), grid.y(1)).build(),
						CompassProperties.FORCE_CHUNKS_LOAD,
						(cached, hovered) -> {
							if (cached) {
								return hovered ? CompassUI.SettingsTextures.FORCE_LOAD_ACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.FORCE_LOAD_ACTIVE_ICON;
							}
							return hovered ? CompassUI.SettingsTextures.FORCE_LOAD_INACTIVE_HOVERED_ICON : CompassUI.SettingsTextures.FORCE_LOAD_INACTIVE_ICON;
						}),
				ButtonCategory.SETTINGS);
		
		this.priorityButton = addButton(
				new SettingsButton<>(settings.copy().position(grid.x(1), grid.y(2)).build(),
						CompassProperties.PRIORITY_MODE,
						(cached, hovered) -> switch (cached) {
							case OFF ->
									hovered ? CompassUI.SettingsTextures.PRIORITY_OFF_HOVERED_ICON : CompassUI.SettingsTextures.PRIORITY_OFF_ICON;
							case NORMAL ->
									hovered ? CompassUI.SettingsTextures.PRIORITY_NORMAL_HOVERED_ICON : CompassUI.SettingsTextures.PRIORITY_NORMAL_ICON;
							case INVERTED ->
									hovered ? CompassUI.SettingsTextures.PRIORITY_INVERT_HOVERED_ICON : CompassUI.SettingsTextures.PRIORITY_INVERT_ICON;
						},
						(cached -> "tooltip.travelerscompass.settings.priority_mode." + cached.name().toLowerCase()),
						(cached) -> switch (cached) {
							case OFF -> CompassUI.DISABLED;
							case NORMAL -> CompassUI.ENABLED;
							case INVERTED -> CompassUI.INVERTED;
						}),
				ButtonCategory.SETTINGS);
		
		ResetButton resetButton = addButton(
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
		
		this.hudButton = addButton(
				new HudButton(settings.copy().position(grid.x(0), grid.y(3)).build()),
				ButtonCategory.SETTINGS);
		
		ButtonRangeSettings.Builder rangeSettings = ButtonRangeSettings
				.builder(settings)
				.minusIcons(CompassUI.CommonTextures.MINUS_ICON, CompassUI.CommonTextures.MINUS_HOVERED_ICON, CompassUI.CommonTextures.MINUS_INACTIVE_ICON)
				.plusIcons(CompassUI.CommonTextures.PLUS_ICON, CompassUI.CommonTextures.PLUS_HOVERED_ICON, CompassUI.CommonTextures.PLUS_INACTIVE_ICON);
		
		
		this.chunksSearchRangeButton = addButton(
				new IntRangeButton(rangeSettings.copy().position(grid.x(1), grid.y(0))
						.icon(CompassUI.SettingsTextures.CHUNKS_RANGE_ICON).build(),
						1, TCConfig.BLOCKS_CHUNK_SEARCH_RANGE.get(),
						1, 5,
						CompassProperties.BLOCK_SEARCH_CHUNK_RANGE),
				ButtonCategory.SETTINGS);
		
		this.entitiesSearchRangeButton = addButton(
				new IntRangeButton(rangeSettings.copy().position(grid.x(2), grid.y(0))
						.icon(CompassUI.SettingsTextures.ENTITIES_RANGE_ICON).build(),
						1, TCConfig.ENTITIES_SEARCH_RANGE.get(),
						1, 5,
						CompassProperties.ENTITIES_SEARCH_RANGE),
				ButtonCategory.SETTINGS);
		
		this.entitiesSearchRangeButton = addButton(
				new IntRangeButton(rangeSettings.copy().position(grid.x(1), grid.y(1))
						.icon(CompassUI.SettingsTextures.WIDE_SEARCH_RANGE_ICON).build(),
						1, TCConfig.WIDE_SEARCH_RANGE.get(),
						1, 5,
						CompassProperties.WIDE_SEARCH_RANGE),
				ButtonCategory.SETTINGS);
		
		
	}
	
	
	private void initTabButtons() {
		ButtonGenericSettings.Builder<?> settings = ButtonGenericSettings.builder()
				.size(26, 24)
				.stackSup(() -> stack);
		
		this.searchTabButton = addButton(new TabButton(
				settings.position(leftPos + 130, topPos + 8)
						.afterLeftClick(b -> {
							if (tabPage == TabPage.SETTINGS) {
								searchTabButton.selected = true;
								settingsTabButton.selected = false;
								CompassProperties.TAB_PAGE.syncToServer(stack, TabPage.SEARCH);
								this.tabPage = TabPage.SEARCH;
								revealButtons(ButtonCategory.SEARCH);
								hideButtons(ButtonCategory.SETTINGS);
							}
						})
						.build(),
				tabPage == TabPage.SEARCH, Icon.of(CompassUI.CommonTextures.SEARCH, 6, 4), Component.translatable("tooltip.travelerscompass.search")), ButtonCategory.STATIC);
		
		this.settingsTabButton = addButton(new TabButton(
				settings.position(leftPos + 130, topPos + 42)
						.afterLeftClick(b -> {
							if (tabPage == TabPage.SEARCH) {
								searchTabButton.selected = false;
								settingsTabButton.selected = true;
								CompassProperties.TAB_PAGE.syncToServer(stack, TabPage.SETTINGS);
								this.tabPage = TabPage.SETTINGS;
								revealButtons(ButtonCategory.SETTINGS);
								hideButtons(ButtonCategory.SEARCH);
							}
						})
						.build(),
				tabPage == TabPage.SETTINGS, Icon.of(CompassUI.CommonTextures.SETTINGS, 6, 4), Component.translatable("tooltip.travelerscompass.settings")), ButtonCategory.STATIC);
		
		
	}
	
	private void initButtons() {
		buttonEntries.clear();
		initTabButtons();
		initSearchModeButtons();
		initUtilButtons();
		initSettingsButtons();
		
	}
	
	public static void resetSettings(ItemStack stack) {
		CompassProperties.putDefaultToServer(stack, CompassProperties.WIDE_SEARCH_RANGE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.ENTITIES_SEARCH_RANGE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.BLOCK_SEARCH_CHUNK_RANGE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.FORCE_CHUNKS_LOAD);
		CompassProperties.putDefaultToServer(stack, CompassProperties.SOUND_PING);
		CompassProperties.putDefaultToServer(stack, CompassProperties.PRIORITY_MODE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HEIGHT_MARKER);
		CompassProperties.putDefaultToServer(stack, CompassProperties.HUD_RENDER_MODE);
		CompassProperties.putDefaultToServer(stack, CompassProperties.TARGET_VALIDATION);
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
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		renderTooltip(guiGraphics, mouseX, mouseY);
		if (minecraft != null && minecraft.player != null) {
			if (minecraft.player.getMainHandItem().getItem() instanceof TravelersCompassItem) {
				this.stack = minecraft.player.getMainHandItem();
			}
		}
	}
	
	@Override
	protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		super.renderTooltip(guiGraphics, mouseX, mouseY);
		if (!TCConfig.SEARCH_REQUIRES_XP.get()) return;
		Slot slot = hoveredSlot;
		if (slot != null && slot.container instanceof CompassContainer) {
			ItemStack carriedStack = getMenu().getCarried();
			if (carriedStack.isEmpty()) {
				if (!ghostStack.isEmpty()) {
					carriedStack = ghostStack;
				}
			}
			if (carriedStack.isEmpty()) {
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
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
		Player player = Minecraft.getInstance().player;
		if (player != null && stack != null) {
			int w = (this.width - this.imageWidth) / 2;
			int h = (this.height - this.imageHeight) / 2;
			graphics.blit(RenderPipelines.GUI_TEXTURED, tabPage.equals(TabPage.SEARCH) ? SEARCH_SCREEN_LOCATION : SETTINGS_SCREEN_LOCATION, w, h, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
			for (int i : CompassProperties.get(stack, CompassProperties.PRIORITY_SLOTS)) {
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