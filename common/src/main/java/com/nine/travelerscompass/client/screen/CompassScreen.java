package com.nine.travelerscompass.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.client.ClientData;
import com.nine.travelerscompass.client.component.button.*;
import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.client.utils.IconTexture;
import com.nine.travelerscompass.client.utils.RangeUtils;
import com.nine.travelerscompass.client.utils.TextureData;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.data.DataStorage;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.PriorityMode;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.common.utils.TabPage;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.cost.SearchCost;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import com.nine.travelerscompass.mixin.accessor.PlayerAccessor;
import com.nine.travelerscompass.network.packet.c2s.PausePacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
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

    private static final ResourceLocation TEXTURE_1 = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID,"textures/gui/container/compass_screen_1.png");
    private static final ResourceLocation TEXTURE_2 = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID,"textures/gui/container/compass_screen_2.png");
    public static final ResourceLocation PRIORITY_SLOT = ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "textures/gui/priority_slot.png");

    private Player player;
    private ItemStack stack;
    private TabPage tabPage;
    private UUID uuid;

    private int updateTicks;

    private TabButton tabButton1;
    private TabButton tabButton2;

    private WarningButton warningButton;

    private SearchModeButton blocksSearchButton;
    private SearchModeButton mobsSearchButton;
    private ContainersButton containersSearchButton;
    private SearchModeButton fluidsSearchButton;
    private SearchModeButton spawnersSearchButton;
    private SearchModeButton dropsSearchButton;
    private SearchModeButton itemEntitiesSearchButton;
    private InventoriesButton inventoriesSearchButton;

    private VillagersButton villagersSearchButton;

    private SettingsButton pauseButton;
    private WideSearchButton wideSearchButton;
    private InfoButton infoButton;

    //Settings
    private HudButton hudButton;
    private SettingsButton forceChunksLoadButton;
    private SettingsButton targetValidationButton;
    private SettingsButton soundPingButton;
    private SettingsButton attitudeMarkerButton;
    private SettingsButton priorityButton;
    private ResetButton resetButton;
    private RangeButton<Integer> chunksSearchRangeButton;
    private RangeButton<Integer> entitiesSearchRange;
    private RangeButton<Integer> wideSearchRange;

    public CompassScreen(CompassMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageWidth = 177;
        imageHeight = 169;
        this.tabPage = TabPage.SEARCH;
    }

    @Override
    protected void init() {
        super.init();
        if (minecraft == null) return;
        this.player = minecraft.player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof TravelersCompassItem)) {
            ((PlayerAccessor) player).tc$closeContainer();
            return;
        }

        this.stack = stack;
        this.uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
        initButtons();
        this.tabPage = CompassProperties.get(stack, CompassProperties.TAB_PAGE);
        addTabButtons();
        addRenderableWidget(warningButton);
        switch (tabPage){
            case SEARCH -> addSearchButtons();
            case SETTINGS -> addSettingsButton();
        }
    }

    //Popups extend beyond widget bounds — handle scroll manually since default dispatch won't catch them
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (tabPage == TabPage.SETTINGS){
            hudButton.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    protected void containerTick() {
        if (fluidsSearchButton == null || warningButton == null) {
            return;
        }
        this.fluidsSearchButton.tick();
        updateTicks++;
        if (updateTicks % 2 == 0){
            this.infoButton.refreshTooltip();
            this.wideSearchButton.refreshTooltip();
            this.wideSearchRange.refreshTooltip();
            this.pauseButton.refreshTooltip();
        }
        if (updateTicks % 10 == 0){
            this.warningButton.updateState(stack);
            updateTicks = 0;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    private void initButtons(){
        this.blocksSearchButton = new SearchModeButton(leftPos + 7, topPos + 7, 14, 14,
                TCConfig.ENABLE_BLOCKS_SEARCH.get(),
                CompassProperties.BLOCKS,
                new IconTexture(ClientData.BLOCKS_ACTIVE, 2, 2),
                new IconTexture(ClientData.BLOCKS_INACTIVE, 2, 2),
                stack
        );
        this.mobsSearchButton = new SearchModeButton(leftPos + 23, topPos + 7, 14, 14,
                TCConfig.ENABLE_MOBS_SEARCH.get(),
                CompassProperties.MOBS,
                new IconTexture(ClientData.MOBS_ACTIVE, 2, 2),
                new IconTexture(ClientData.MOBS_INACTIVE, 2, 2),
                stack
        );
        this.fluidsSearchButton = new SearchModeButton(leftPos + 39, topPos + 23, 14, 14,
                TCConfig.ENABLE_FLUIDS_SEARCH.get(),
                CompassProperties.FLUIDS,
                new IconTexture(ClientData.FLUIDS_ACTIVE, 2, 2),
                new IconTexture(ClientData.FLUIDS_INACTIVE, 2, 2),
                stack
        ){

            private int frameIndex = 0;
            private int tickCount = 0;
            final int framesPerTick = 2;
            final int maxFrames = 31;

            @Override
            public void tick(){
                tickCount++;
                if (tickCount % framesPerTick == 0){
                    frameIndex = (frameIndex + 1) % maxFrames;
                }
            }

            @Override
            public void renderIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
                IconTexture icon = isToggled() ? iconActive : iconInactive;
                int yOff = 8 * frameIndex;
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                TextureData data = icon.textureData().toBuilder().uv(0, yOff).build();
                ClientUtils.renderTexture(graphics, data, this.getX() + icon.xOffset(), this.getY() + icon.yOffset());
            }
        };

        this.spawnersSearchButton = new SearchModeButton(leftPos + 23, topPos + 23, 14, 14,
                TCConfig.ENABLE_SPAWNERS_SEARCH.get(),
                CompassProperties.SPAWNERS,
                new IconTexture(ClientData.SPAWNERS_ACTIVE, 2, 2),
                new IconTexture(ClientData.SPAWNERS_INACTIVE, 2, 2),
                stack
        );
        this.itemEntitiesSearchButton = new SearchModeButton(leftPos + 23, topPos + 39, 14, 14,
                TCConfig.ENABLE_ITEM_ENTITIES_SEARCH.get(),
                CompassProperties.ITEM_ENTITIES,
                new IconTexture(ClientData.ITEM_ENTITIES_ACTIVE, 2, 2),
                new IconTexture(ClientData.ITEM_ENTITIES_INACTIVE, 2, 2),
                stack
        );
        this.dropsSearchButton = new SearchModeButton(leftPos + 39, topPos + 39, 14, 14,
                TCConfig.ENABLE_DROPS_SEARCH.get(), CompassProperties.DROP,
                new IconTexture(ClientData.DROPS_ACTIVE, 1, 2),
                new IconTexture(ClientData.DROPS_INACTIVE, 1, 2),
                stack
        );

        this.containersSearchButton = new ContainersButton(leftPos + 39, topPos + 7, stack);

        this.villagersSearchButton = new VillagersButton(leftPos + 7, topPos + 23, stack);


        this.inventoriesSearchButton = new InventoriesButton(leftPos + 7, topPos + 39, stack);


        this.wideSearchButton = new WideSearchButton(leftPos + 23, topPos + 55,
                () -> stack
        ) {

            @Override
            public void onClick(double mouseX, double mouseY) {
                super.onClick(mouseX, mouseY);
                pauseButton.updateState();
            }
        };

        this.infoButton = new InfoButton(leftPos + 39, topPos + 55, 14, 14,
                () -> stack
        );

        this.pauseButton = new SettingsButton(leftPos + 7, topPos + 55, 14, 14,
                CompassProperties.PAUSE, (value) -> {
            if ((Boolean) value){
                return new SettingsButton.DisplayDataHolder(
                        new IconTexture(ClientData.RESUME, 2, 2),
                        new IconTexture(ClientData.RESUME_HOVERED, 2, 2),
                        Component.empty());
            }
            return new SettingsButton.DisplayDataHolder(
                    new IconTexture(ClientData.PAUSE, 2, 2),
                    new IconTexture(ClientData.PAUSE_HOVERED, 2, 2),
                    Component.empty());
        }, ()-> stack)
        {

            private SearchState cachedState = SearchState.IDLE;

            @Override
            public void onClick(double mouseX, double mouseY) {
                super.onClick(mouseX, mouseY);
                ClientData.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
                            hudData.setPaused((Boolean) cachedValue);
                            return hudData;
                        }
                );
                ClientData.PROGRESS_DATA_CACHE.remove(uuid);
                Platform.PLATFORM_NETWORK.sendToServer(new PausePacket(uuid));
            }


            @Override
            public void updateState(){
                super.updateState();
                ItemStack stack = supplier.get();
                cachedState = CompassProperties.SEARCH_STATE.get(stack);
            }

            @Override
            public void refreshTooltip(){
                List<Component> list = new ArrayList<>();
                boolean paused = (Boolean) cachedValue;
                String key = "tooltip.travelerscompass.settings." + (paused ? "resume" : "pause");
                Component title = Component.translatable(key);
                list.add(title);
                if (cachedState == SearchState.WIDE_SEARCHING && paused){
                    list.add(Component.translatable(key + ".warning").withStyle(ChatFormatting.GRAY));
                }
                if (shiftPressed){
                    list.add(Component.empty().append(ClientUtils.DESC_ARROW).append(Component.translatable(key + ".desc").withStyle(ChatFormatting.GRAY)));
                }
                this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(list)));
            }
        };

        this.attitudeMarkerButton = new SettingsButton(
                leftPos + 39, topPos + 39, 14, 14,
                CompassProperties.HEIGHT_MARKER, (value) -> {
            if ((Boolean) value){
                return new SettingsButton.DisplayDataHolder(
                        new IconTexture(ClientData.ATTITUDE_MARKER_ACTIVE, 2, 2),
                        new IconTexture(ClientData.ATTITUDE_MARKER_ACTIVE_HOVERED, 2, 2),
                        Toggleable.ENABLED);
            }
            return new SettingsButton.DisplayDataHolder(
                    new IconTexture(ClientData.ATTITUDE_MARKER_INACTIVE, 2, 2),
                    new IconTexture(ClientData.ATTITUDE_MARKER_INACTIVE_HOVERED, 2, 2),
                    Toggleable.DISABLED);
        }, ()-> stack);

        this.priorityButton = new SettingsButton(
                leftPos + 23, topPos + 39, 14, 14,
                CompassProperties.PRIORITY_MODE, (value) -> {
            PriorityMode priorityMode = (PriorityMode) value;
            return switch (priorityMode){
                case OFF -> new SettingsButton.DisplayDataHolder(
                        ClientData.PRIORITY_OFF_ICON,
                        ClientData.PRIORITY_OFF_HOVERED_ICON, Component.empty());
                case NORMAL -> new SettingsButton.DisplayDataHolder(
                        ClientData.PRIORITY_NORMAL_ICON,
                        ClientData.PRIORITY_NORMAL_HOVERED_ICON, Component.empty());
                case INVERTED -> new SettingsButton.DisplayDataHolder(
                        ClientData.PRIORITY_INVERT_ICON,
                        ClientData.PRIORITY_INVERT_HOVERED_ICON, Component.empty());
            };
        }, ()-> stack) {

            @Override
            public void refreshTooltip(){
                List<Component> list = new ArrayList<>();
                String key = "tooltip.travelerscompass.settings.priority_mode";
                Component title = Component.translatable(key);
                Component state = switch ((PriorityMode) cachedValue) {
                    case OFF -> Toggleable.DISABLED;
                    case NORMAL -> Toggleable.ENABLED;
                    case INVERTED -> Toggleable.INVERTED;
                };
                list.add(title);
                if (shiftPressed){
                    String descKey = key + ".desc." + ((PriorityMode) cachedValue).name().toLowerCase();
                    list.add(Component.empty().append(ClientUtils.DESC_ARROW)
                            .append(Component.translatable(descKey).withStyle(ChatFormatting.GRAY)));
                }
                list.add(state);
                this.setTooltip(Tooltip.create(ClientUtils.buildTooltip(list)));
            }
        };

        this.soundPingButton = new SettingsButton(
                leftPos + 7, topPos + 39, 14, 14,
                CompassProperties.SOUND_PING, (value) -> {
            if ((Boolean) value){
                return new SettingsButton.DisplayDataHolder(
                        new IconTexture(ClientData.SOUND_PING_ACTIVE, 3, 3),
                        new IconTexture(ClientData.SOUND_PING_ACTIVE_HOVERED, 3, 3),
                        Toggleable.ENABLED);
            }
            return new SettingsButton.DisplayDataHolder(
                    new IconTexture(ClientData.SOUND_PING_INACTIVE, 3, 3),
                    new IconTexture(ClientData.SOUND_PING_INACTIVE_HOVERED, 3, 3),
                    Toggleable.DISABLED);
        }, ()-> stack);

        this.warningButton = new WarningButton(leftPos - 17, topPos + 76, 21, 22, stack);

        this.hudButton = new HudButton(leftPos + 7, topPos + 55, () -> stack) {

            @Override
            public void onPopupExit(){
                warningButton.hidden = false;
                warningButton.updateState(stack);
            }

            @Override
            public void onPopupEnter(){
                warningButton.hidden = true;
                warningButton.setTooltip(Tooltip.create(Component.empty()));
            }

        };

        this.forceChunksLoadButton = new SettingsButton(
                leftPos + 39, topPos + 23, 14, 14,
                CompassProperties.FORCE_CHUNKS_LOAD, (value) -> {
            if ((Boolean) value){
                return new SettingsButton.DisplayDataHolder(
                        ClientData.FORCE_LOAD_ACTIVE.icon(2, 2),
                        ClientData.FORCE_LOAD_ACTIVE_HOVERED.icon(2, 2),
                        Toggleable.ENABLED);
            }
            return new SettingsButton.DisplayDataHolder(
                    ClientData.FORCE_LOAD_INACTIVE.icon(2, 2),
                    ClientData.FORCE_LOAD_INACTIVE_HOVERED.icon(2, 2),
                    Toggleable.DISABLED);
        }, ()-> stack);

        this.targetValidationButton = new SettingsButton(
                leftPos + 23, topPos + 55, 14, 14,
                CompassProperties.TARGET_VALIDATION, (value) -> {
            if ((Boolean) value){
                return new SettingsButton.DisplayDataHolder(
                        ClientData.TARGET_VALIDATION_ACTIVE.icon(3, 2),
                        ClientData.TARGET_VALIDATION_ACTIVE_HOVERED.icon(3, 2),
                        Toggleable.ENABLED);
            }
            return new SettingsButton.DisplayDataHolder(
                    ClientData.TARGET_VALIDATION_INACTIVE.icon(3, 2),
                    ClientData.TARGET_VALIDATION_INACTIVE_HOVERED.icon(3, 2),
                    Toggleable.DISABLED);
        }, ()-> stack);

        this.resetButton = new ResetButton(leftPos + 39, topPos + 55, (buttonDefault) -> {
            resetSettings();
            entitiesSearchRange.updateState();
            chunksSearchRangeButton.updateState();
            wideSearchRange.updateState();
        });

        this.chunksSearchRangeButton = new RangeButton<>(leftPos + 23, topPos + 23, 14, 14,
                1, TCConfig.BLOCKS_CHUNK_SEARCH_RANGE.get(),
                RangeUtils.INTEGER,
                new IconTexture(ClientData.CHUNKS_RANGE, 2, 2),
                CompassProperties.BLOCK_SEARCH_CHUNK_RANGE, () -> stack);

        this.entitiesSearchRange = new RangeButton<>(leftPos + 23, topPos + 7, 14, 14,
                1, TCConfig.ENTITIES_SEARCH_RANGE.get(),
                RangeUtils.INTEGER,
                new IconTexture(ClientData.ENTITIES_RANGE, 2, 2),
                CompassProperties.ENTITIES_SEARCH_RANGE, () -> stack);

        this.wideSearchRange = new RangeButton<>(leftPos + 39, topPos + 7, 14, 14,
                1, TCConfig.WIDE_SEARCH_RANGE.get(),
                RangeUtils.INTEGER,
                new IconTexture(ClientData.WIDE_SEARCH_RANGE, 2, 2),
                CompassProperties.WIDE_SEARCH_RANGE, () -> stack);


    }

    private void addSearchButtons(){
        addRenderableWidget(blocksSearchButton);
        addRenderableWidget(mobsSearchButton);
        addRenderableWidget(containersSearchButton);
        addRenderableWidget(villagersSearchButton);
        addRenderableWidget(spawnersSearchButton);
        addRenderableWidget(fluidsSearchButton);
        addRenderableWidget(inventoriesSearchButton);
        addRenderableWidget(itemEntitiesSearchButton);
        addRenderableWidget(dropsSearchButton);
        addRenderableWidget(pauseButton);
        addRenderableWidget(wideSearchButton);
        addRenderableWidget(infoButton);
    }

    private void removeSearchButtons(){
        removeWidget(blocksSearchButton);
        removeWidget(mobsSearchButton);
        removeWidget(containersSearchButton);
        removeWidget(villagersSearchButton);
        removeWidget(spawnersSearchButton);
        removeWidget(fluidsSearchButton);
        removeWidget(inventoriesSearchButton);
        removeWidget(itemEntitiesSearchButton);
        removeWidget(dropsSearchButton);
        removeWidget(pauseButton);
        removeWidget(wideSearchButton);
        removeWidget(infoButton);

    }

    private void addSettingsButton(){
        addRenderableWidget(hudButton);
        addRenderableWidget(forceChunksLoadButton);
        addRenderableWidget(targetValidationButton);
        addRenderableWidget(attitudeMarkerButton);
        addRenderableWidget(soundPingButton);
        addRenderableWidget(priorityButton);
        addRenderableWidget(chunksSearchRangeButton);
        addRenderableWidget(entitiesSearchRange);
        addRenderableWidget(wideSearchRange);
        addRenderableWidget(resetButton);
    }

    private void removeSettingsButton(){
        removeWidget(hudButton);
        removeWidget(forceChunksLoadButton);
        removeWidget(targetValidationButton);
        removeWidget(attitudeMarkerButton);
        removeWidget(soundPingButton);
        removeWidget(priorityButton);
        removeWidget(chunksSearchRangeButton);
        removeWidget(entitiesSearchRange);
        removeWidget(wideSearchRange);
        removeWidget(resetButton);
    }

    private void addTabButtons(){
        boolean searchMode = tabPage == TabPage.SEARCH;
        this.tabButton1 = addRenderableWidget(new TabButton(leftPos + 130, topPos + 8, 26, 24, 6, 4, searchMode, ClientData.SEARCH, (button) -> {
            if (tabPage == TabPage.SETTINGS){
                tabButton1.enabled = true;
                tabButton2.enabled = false;
                CompassProperties.TAB_PAGE.sendToServer(stack, TabPage.SEARCH);
                this.tabPage = TabPage.SEARCH;
                addSearchButtons();
                removeSettingsButton();
            }
        }, Component.translatable("tooltip.travelerscompass.search")));
        this.tabButton2 = new TabButton(leftPos + 130, topPos + 42, 26, 24, 6, 4, !searchMode, ClientData.SETTINGS, (button) -> {
            if (tabPage == TabPage.SEARCH){
                tabButton1.enabled = false;
                tabButton2.enabled = true;
                CompassProperties.TAB_PAGE.sendToServer(stack, TabPage.SETTINGS);
                this.tabPage = TabPage.SETTINGS;
                addSettingsButton();
                removeSearchButtons();
            }
        }, Component.translatable("tooltip.travelerscompass.settings"));
        addRenderableWidget(tabButton1);
        addRenderableWidget(tabButton2);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        renderTooltip(guiGraphics, mouseX, mouseY);
        if (minecraft != null && minecraft.player != null){
            if (minecraft.player.getMainHandItem().getItem() instanceof TravelersCompassItem){
                this.stack = minecraft.player.getMainHandItem();
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        if (!TCConfig.SEARCH_REQUIRES_XP.get()) return;
        Slot slot = hoveredSlot;
        if (slot != null && slot.container instanceof CompassContainer){
            Item carried = getMenu().getCarried().getItem();
            SearchCost sc = SearchCostHelper.getCost(carried);
            FilterReason reason = FilterManager.getFilterReason(carried);
            if (reason instanceof FilterReason.Allowed) {
                if (!sc.isFree() && !player.isCreative()) {
                    List<Component> extra = new ArrayList<>();
                    extra.add(sc.reqAsComponent().withStyle(
                            sc.meetsRequirement(player.experienceLevel) ?
                                    ChatFormatting.GRAY : ChatFormatting.RED));
                    extra.add(sc.costAsComponent().withStyle(ChatFormatting.GRAY));
                    guiGraphics.renderComponentTooltip(this.font, extra, mouseX, mouseY);
                }
            }
            else {
                guiGraphics.renderTooltip(this.font, ClientUtils.formatReason(reason), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        Player player = Minecraft.getInstance().player;
        if (player != null && stack != null) {
            graphics.blit(tabPage.equals(TabPage.SEARCH) ? TEXTURE_1 : TEXTURE_2, leftPos, topPos, 0, 0, imageWidth, imageHeight);
            for (int i : CompassProperties.get(stack, CompassProperties.PRIORITY_SLOTS)) {
                int j = i > 2 ? 1 : 0;
                j = i > 5 ? 2 : j;
                int c = i > 2 ? i - 3 : i;
                c = i > 5 ? i - 6 : c;
                graphics.blit(PRIORITY_SLOT, leftPos + 72 + c * 18, topPos + 10 + j * 18, 0, 0, 18, 18, 18, 18);
            }
        }
    }

    private void resetSettings(){
        List<DataStorage<?>> toReset = List.of(
                CompassProperties.TARGET_VALIDATION,
                CompassProperties.SOUND_PING,
                CompassProperties.HEIGHT_MARKER,
                CompassProperties.HUD_RENDER_MODE,
                CompassProperties.PRIORITY_MODE,
                CompassProperties.FORCE_CHUNKS_LOAD,
                CompassProperties.ENTITIES_SEARCH_RANGE,
                CompassProperties.BLOCK_SEARCH_CHUNK_RANGE,
                CompassProperties.WIDE_SEARCH_RANGE
        );
        for (var data : toReset){
            CompassProperties.putDefaultToServer(stack, data);
        }
        targetValidationButton.updateState();
        soundPingButton.updateState();
        attitudeMarkerButton.updateState();
        priorityButton.updateState();
        forceChunksLoadButton.updateState();
        entitiesSearchRange.updateState();
        chunksSearchRangeButton.updateState();
        wideSearchRange.updateState();
        hudButton.resetSettings(stack);
        hudButton.updateState();
    }

    public int leftPos(){
        return leftPos;
    }

    public int topPos(){
        return topPos;
    }

}