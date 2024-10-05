package com.nine.travelerscompass.client.screen;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.client.components.*;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.SearchButtonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class CompassScreen extends AbstractContainerScreen<CompassMenu> {
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("travelerscompass:textures/gui/container/compass_screen_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("travelerscompass:textures/gui/container/compass_screen_2.png");
    private static final ResourceLocation WIDGETS = new ResourceLocation("travelerscompass:textures/gui/component/gui_components.png");
    private TabButton configButton;
    private TabButton searchButton;
    private TabButton statusButton;
    private ConfigButton mobButton;
    private ConfigButton blockButton;
    private ConfigButton containerButton;
    private ConfigButton itemEntityButton;
    private TraderButton villagerButton;
    private ConfigButton fluidButton;
    private ConfigButton spawnerButton;
    private ConfigButton infoButton;
    private ConfigButton pauseButton;
    private InventoryButton mobsInvButton;
    private ConfigButton dropsButton;
    private ConfigButton wideSearchButton;
    private ConfigButton blocksDistanceButton;
    private ConfigButton mobsDistanceButton;
    private ConfigButton containersDistanceButton;
    private ConfigButton wideDistanceButton;
    private ConfigButton prioritySwitch;
    private ConfigButton labelSwitch;
    private ConfigButton fullResetButton;
    private ConfigButton soundButton;
    private ConfigButton lazyButton;
    private HUDButton hudButton;

    public CompassScreen(CompassMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 172;
    }

    @Override
    protected void init() {
        super.init();
        mobButton = new ConfigButton(leftPos + 22, topPos + 42, ButtonType.MOBS);
        containerButton = new ConfigButton(leftPos + 6, topPos + 10, ButtonType.CONTAINERS);
        blockButton = new ConfigButton(leftPos + 6, topPos + 42, ButtonType.BLOCKS);
        itemEntityButton = new ConfigButton(leftPos + 38, topPos + 10, ButtonType.DROPPED_ITEMS);
        fluidButton = new ConfigButton(leftPos + 22, topPos + 26, ButtonType.FLUIDS);
        spawnerButton = new ConfigButton(leftPos + 38, topPos + 26, ButtonType.SPAWNERS);
        dropsButton = new ConfigButton(leftPos + 38, topPos + 42, ButtonType.DROPS);
        pauseButton = new ConfigButton(leftPos + 6, topPos + 58, ButtonType.PAUSE);
        infoButton = new ConfigButton(leftPos + 38, topPos + 58, ButtonType.INFO);
        wideSearchButton = new ConfigButton(leftPos + 22, topPos + 58, ButtonType.WIDE_SEARCH);

        blocksDistanceButton = new ConfigButton(leftPos + 38, topPos + 10, ButtonType.BLOCKS_DISTANCE);
        mobsDistanceButton = new ConfigButton(leftPos + 22, topPos + 26, ButtonType.MOBS_DISTANCE);
        containersDistanceButton = new ConfigButton(leftPos + 38, topPos + 26, ButtonType.CONTAINERS_DISTANCE);
        wideDistanceButton = new ConfigButton(leftPos + 22, topPos + 10, ButtonType.WIDE_DISTANCE);
        prioritySwitch = new ConfigButton(leftPos + 22, topPos + 42, ButtonType.PRIORITY_SWITCH);
        labelSwitch = new ConfigButton(leftPos + 38, topPos + 42, ButtonType.LABELS);
        fullResetButton = new ConfigButton(leftPos + 38, topPos + 58, ButtonType.FULL_RESET);
        soundButton = new ConfigButton(leftPos + 6, topPos + 10, ButtonType.SOUND);
        lazyButton = new ConfigButton(leftPos + 6, topPos + 26, ButtonType.LAZY_MODE);

        villagerButton = new TraderButton(leftPos + 22, topPos - 14, 14, 38);
        hudButton = new HUDButton(leftPos - 13, topPos + 58, 49, 27);
        mobsInvButton = new InventoryButton(leftPos - 29, topPos + 26, 49, 14);

        renderTabButtons();
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack && compassStack.configMode(stack)) {
                removeSearchButtons();
                addConfigButtons();
            }
            if (stack.getItem() instanceof TravelersCompassItem compassStack && !compassStack.configMode(stack)) {
                removeConfigButtons();
                addSearchButtons();
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        updateTooltips();
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        renderBackground(guiGraphics);
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                if (compassStack.configMode(player.getMainHandItem())) {
                    guiGraphics.blit(TEXTURE_2, leftPos - 18, topPos - 35, 0, 0, imageWidth + 18, imageHeight + 35);
                } else {
                    guiGraphics.blit(TEXTURE_1, leftPos - 18, topPos - 35, 0, 0, imageWidth + 18, imageHeight + 35);
                }

                for (int i : compassStack.favoriteSlots(stack)) {
                    int j = i > 2 ? 1 : 0;
                    j = i > 5 ? 2 : j;
                    int c = i > 2 ? i - 3 : i;
                    c = i > 5 ? i - 6 : c;
                    guiGraphics.blit(WIDGETS, leftPos + 71 + c * 18, topPos + 13 + j * 18, 0, 158, 18, 18);
                }
            }
        }
    }

    public void renderTabButtons() {
        statusButton = new TabButton(leftPos - 18, topPos + 79, 23, 22, 3, (configButton) -> {
        });
        searchButton = new TabButton(leftPos + 129, topPos + 14, 26, 24, 0, (configButton) ->
        {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack && !compassStack.configMode(stack)) {
                return;
            }
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                removeConfigButtons();
                addSearchButtons();
                NetworkHandler.CHANNEL.sendToServer(new SearchButtonPacket(5));
                compassStack.setConfigMode(stack, false);
            }

        });
        configButton = new TabButton(leftPos + 129, topPos + 43, 26, 24, 1, (configButton) ->
        {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack && compassStack.configMode(stack)) {
                return;
            }
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                removeSearchButtons();
                addConfigButtons();
                NetworkHandler.CHANNEL.sendToServer(new SearchButtonPacket(4));
                compassStack.setConfigMode(stack, true);
            }

        });
        searchButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.searching")));
        configButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.config")));

        addRenderableWidget(statusButton);
        addRenderableWidget(searchButton);
        addRenderableWidget(configButton);
    }

    private void updateTooltips() {
        Player player = Minecraft.getInstance().player;
        if (player == null || minecraft == null) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        boolean shiftKeyPressed = GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (stack.getItem() instanceof TravelersCompassItem compassStack) {
            CompassContainer compassContainer = CompassContainer.container(stack);

            boolean fluids = compassStack.isSearchingFluids(stack);
            boolean droppedItems = compassStack.isSearchingItemEntities(stack);
            boolean spawners = compassStack.isSearchingSpawners(stack);
            boolean invS = compassStack.isSearchingEntitiesInv(stack);
            boolean drops = compassStack.isSearchingDrops(stack);
            boolean pause = compassStack.isPaused(stack);
            boolean blocks = compassStack.isSearchingBlocks(stack);
            boolean containers = compassStack.isSearchingContainers(stack);
            boolean priorityMode = compassStack.priorityMode(stack);
            boolean labels = compassStack.showLabels(stack);
            boolean mobs = compassStack.isSearchingMobs(stack);
            boolean sound = compassStack.sound(stack);
            boolean lazyMode = compassStack.isLazyModeOn(stack);

            String blocksTooltip = Component.translatable("options.travelerscompass.tooltip.block_button").getString();
            if (shiftKeyPressed) {
                blocksTooltip += (Component.translatable("options.travelerscompass.tooltip.blocks.info").getString());
            }
            blocksTooltip += (blocks ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());


            String containersTooltip = Component.translatable("options.travelerscompass.tooltip.container_button").getString();
            if (shiftKeyPressed) {
                containersTooltip += (Component.translatable("options.travelerscompass.tooltip.containers.info").getString());
            }
            containersTooltip += (containers ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String mobsTooltip = Component.translatable("options.travelerscompass.tooltip.mob_button").getString();
            if (shiftKeyPressed) {
                mobsTooltip += (Component.translatable("options.travelerscompass.tooltip.mobs.info").getString());
            }
            mobsTooltip += (mobs ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());


            String fluidTooltip = Component.translatable("options.travelerscompass.tooltip.fluid_button").getString();
            if (shiftKeyPressed) {
                fluidTooltip += (Component.translatable("options.travelerscompass.tooltip.fluids.info").getString());
            }
            fluidTooltip += (fluids ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String itemEntityTooltip = Component.translatable("options.travelerscompass.tooltip.item_entity_button").getString();
            if (shiftKeyPressed) {
                itemEntityTooltip += (Component.translatable("options.travelerscompass.tooltip.item_entity.info").getString());
            }
            itemEntityTooltip += (droppedItems ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String spawnerTooltip = Component.translatable("options.travelerscompass.tooltip.spawner_button").getString();
            if (shiftKeyPressed) {
                spawnerTooltip += (Component.translatable("options.travelerscompass.tooltip.spawners.info").getString());
            }
            spawnerTooltip += (spawners ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String mobsInvToolTip = Component.translatable("options.travelerscompass.tooltip.mobs_inv_button").getString();
            if (shiftKeyPressed) {
                mobsInvToolTip += (Component.translatable("options.travelerscompass.tooltip.mobs_inv.info").getString());
            }
            mobsInvToolTip += (invS ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String dropsToolTip = Component.translatable("options.travelerscompass.tooltip.mobs_drop_button").getString();
            if (shiftKeyPressed) {
                dropsToolTip += (Component.translatable("options.travelerscompass.tooltip.drops.info").getString());
            }
            dropsToolTip += (drops ? Component.translatable("options.travelerscompass.tooltip.enabled").getString() :
                    Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String pauseToolTip = pause ? Component.translatable("options.travelerscompass.tooltip.play").getString() :
                    Component.translatable("options.travelerscompass.tooltip.pause_1").getString();
            pauseToolTip += (pause ? Component.translatable("options.travelerscompass.tooltip.play_more").getString() :
                    Component.translatable("options.travelerscompass.tooltip.pause_more").getString());
            String priorityTooltip = Component.translatable("options.travelerscompass.tooltip.priority_button").getString();
            priorityTooltip += (priorityMode ? Component.translatable("options.travelerscompass.tooltip.priority_button_1").getString() :
                    Component.translatable("options.travelerscompass.tooltip.priority_button_2").getString());
            if (shiftKeyPressed) {
                priorityTooltip += (Component.translatable("options.travelerscompass.tooltip.priority_button_3").getString());
            }

            String labelTooltip = (labels ? Component.translatable("options.travelerscompass.tooltip.label_button_1").getString() :
                    Component.translatable("options.travelerscompass.tooltip.label_button_2").getString());
            labelTooltip += (Component.translatable("options.travelerscompass.tooltip.label_button_3").getString());

            String resetTooltip = Component.translatable("options.travelerscompass.tooltip.full_reset_button").getString();
            resetTooltip += Component.translatable("options.travelerscompass.tooltip.full_reset_button_1").getString();

            String blockSearchRadiusTooltip = Component.translatable("options.travelerscompass.tooltip.block_search_radius").getString();
            if (shiftKeyPressed) {
                blockSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.block_search_radius_1").getString());
            } else {
                blockSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.shift_to_decrease").getString());
            }
            blockSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.current_value", compassStack.blockSearchRadius(stack)).getString());


            String containerSearchRadiusTooltip = Component.translatable("options.travelerscompass.tooltip.container_search_radius").getString();
            if (shiftKeyPressed) {
                containerSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.container_search_radius_1").getString());
            } else {
                containerSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.shift_to_decrease").getString());
            }
            containerSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.current_value", compassStack.containerSearchRadius(stack)).getString());

            String wideSearchTooltip = Component.translatable("options.travelerscompass.tooltip.wide_search_button").getString();
            wideSearchTooltip += (Component.translatable("options.travelerscompass.tooltip.wide_search_button_1").getString());

            //SEARCH RADIUS
            String entitySearchRadiusTooltip = Component.translatable("options.travelerscompass.tooltip.entity_search_radius").getString();
            if (shiftKeyPressed) {
                entitySearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.entity_search_radius_1").getString());
            } else {
                entitySearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.shift_to_decrease").getString());
            }
            entitySearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.current_value", compassStack.entitySearchRadius(stack)).getString());

            String wideSearchRadiusTooltip = Component.translatable("options.travelerscompass.tooltip.wide_search_radius").getString();
            if (shiftKeyPressed) {
                wideSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.wide_search_radius_1").getString());
            } else {
                wideSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.shift_to_decrease").getString());
            }
            wideSearchRadiusTooltip += (Component.translatable("options.travelerscompass.tooltip.current_value", compassStack.wideSearchRadius(stack)).getString());

            String soundButtonTooltip = Component.translatable("options.travelerscompass.tooltip.sound_switch").getString();
            if (shiftKeyPressed) {
                soundButtonTooltip += (Component.translatable("options.travelerscompass.tooltip.sound_switch_1").getString());
            }
            soundButtonTooltip += sound ? (Component.translatable("options.travelerscompass.tooltip.enabled").getString()) : (Component.translatable("options.travelerscompass.tooltip.disabled").getString());

            String lazyButtonTooltip = Component.translatable("options.travelerscompass.tooltip.lazy_mode").getString();
            if (shiftKeyPressed) {
                lazyButtonTooltip += (Component.translatable("options.travelerscompass.tooltip.lazy_mode_1").getString());
            }
            lazyButtonTooltip += TCConfig.forcedLazySearchMode.get() ? Component.translatable("options.travelerscompass.tooltip.enabled_config").getString() : (lazyMode ? (Component.translatable("options.travelerscompass.tooltip.enabled").getString()) : (Component.translatable("options.travelerscompass.tooltip.disabled").getString()));


            fluidButton.setTooltip(Tooltip.create(Component.literal(fluidTooltip)));
            itemEntityButton.setTooltip(Tooltip.create(Component.literal(itemEntityTooltip)));
            spawnerButton.setTooltip(Tooltip.create(Component.literal(spawnerTooltip)));
            mobsInvButton.setTooltip(Tooltip.create(Component.literal(mobsInvToolTip)));
            dropsButton.setTooltip(Tooltip.create(Component.literal(dropsToolTip)));
            pauseButton.setTooltip(Tooltip.create(Component.literal(pauseToolTip)));
            blockButton.setTooltip(Tooltip.create(Component.literal(blocksTooltip)));
            mobButton.setTooltip(Tooltip.create(Component.literal(mobsTooltip)));
            containerButton.setTooltip(Tooltip.create(Component.literal(containersTooltip)));

            blocksDistanceButton.setTooltip(Tooltip.create(Component.literal(blockSearchRadiusTooltip)));
            mobsDistanceButton.setTooltip(Tooltip.create(Component.literal(entitySearchRadiusTooltip)));
            containersDistanceButton.setTooltip(Tooltip.create(Component.literal(containerSearchRadiusTooltip)));
            wideDistanceButton.setTooltip(Tooltip.create(Component.literal(wideSearchRadiusTooltip)));
            wideSearchButton.setTooltip(Tooltip.create(Component.literal(wideSearchTooltip)));
            prioritySwitch.setTooltip(Tooltip.create(Component.literal(priorityTooltip)));
            labelSwitch.setTooltip(Tooltip.create(Component.literal(labelTooltip)));
            fullResetButton.setTooltip(Tooltip.create(Component.literal(resetTooltip)));
            soundButton.setTooltip(Tooltip.create(Component.literal(soundButtonTooltip)));
            lazyButton.setTooltip(Tooltip.create(Component.literal(lazyButtonTooltip)));

            checkDisabledButtons();
        }
    }

    private void checkDisabledButtons() {
        if (!TCConfig.enableMobSearch.get()) {
            mobButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableBlockSearch.get()) {
            blockButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableContainerSearch.get()) {
            containerButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableWiderSearch.get()) {
            wideSearchButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableItemEntitiesSearch.get()) {
            itemEntityButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableFluidSearch.get()) {
            fluidButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableSpawnerSearch.get()) {
            spawnerButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableDropSearch.get()) {
            dropsButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
        if (!TCConfig.enableMobsInventorySearch.get()) {
            mobsInvButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled_config")));
        }
    }

    private void addConfigButtons() {
        addRenderableWidget(blocksDistanceButton);
        addRenderableWidget(mobsDistanceButton);
        addRenderableWidget(containersDistanceButton);
        addRenderableWidget(wideDistanceButton);
        addRenderableWidget(prioritySwitch);
        addRenderableWidget(labelSwitch);
        addRenderableWidget(fullResetButton);
        addRenderableWidget(hudButton);
        addRenderableWidget(lazyButton);
        addRenderableWidget(soundButton);

    }

    private void removeConfigButtons() {
        removeWidget(blocksDistanceButton);
        removeWidget(mobsDistanceButton);
        removeWidget(containersDistanceButton);
        removeWidget(wideDistanceButton);
        removeWidget(prioritySwitch);
        removeWidget(labelSwitch);
        removeWidget(fullResetButton);
        removeWidget(hudButton);
        removeWidget(lazyButton);
        removeWidget(soundButton);

    }

    private void addSearchButtons() {
        addRenderableWidget(villagerButton);
        addRenderableWidget(itemEntityButton);
        addRenderableWidget(fluidButton);
        addRenderableWidget(spawnerButton);
        addRenderableWidget(infoButton);
        addRenderableWidget(mobsInvButton);
        addRenderableWidget(dropsButton);
        addRenderableWidget(pauseButton);
        addRenderableWidget(blockButton);
        addRenderableWidget(mobButton);
        addRenderableWidget(containerButton);
        addRenderableWidget(wideSearchButton);
    }

    private void removeSearchButtons() {
        removeWidget(villagerButton);
        removeWidget(itemEntityButton);
        removeWidget(fluidButton);
        removeWidget(spawnerButton);
        removeWidget(infoButton);
        removeWidget(mobsInvButton);
        removeWidget(dropsButton);
        removeWidget(pauseButton);
        removeWidget(blockButton);
        removeWidget(mobButton);
        removeWidget(containerButton);
        removeWidget(wideSearchButton);
    }
}