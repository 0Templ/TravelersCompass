package com.nine.travelerscompass.client.screen;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.client.components.ConfigButton;
import com.nine.travelerscompass.client.components.SearchButton;
import com.nine.travelerscompass.client.components.TabButton;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.item.CompassMode;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.SearchButtonPacket;
import net.minecraft.ChatFormatting;
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

import java.util.List;

public class CompassScreen extends AbstractContainerScreen<CompassMenu> {
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("travelerscompass:textures/gui/container/compass_screen.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("travelerscompass:textures/gui/container/compass_screen_settings.png");
    private static final ResourceLocation WIDGETS = new ResourceLocation("travelerscompass:textures/gui/component/gui_components.png");
    private SearchButton mobButton;
    private SearchButton blockButton;
    private SearchButton containerButton;
    private TabButton configButton;
    private TabButton searchButton;
    private TabButton statusButton;
    private ConfigButton itemEntityButton;
    private ConfigButton villagerButton;
    private ConfigButton fluidButton;
    private ConfigButton spawnerButton;
    private ConfigButton infoButton;
    private ConfigButton pauseButton;
    private ConfigButton mobsInvButton;
    private ConfigButton dropsButton;
    private CompassMenu menu;

    public CompassScreen(CompassMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        imageHeight = 172;
    }


    @Override
    protected void init() {
        super.init();
        mobButton = new SearchButton(leftPos+24, topPos+11,18, 18,1);
        containerButton = new SearchButton(leftPos+24, topPos+31,18, 18,2);
        blockButton = new SearchButton(leftPos+24, topPos+51,18, 18,3);

        villagerButton = new ConfigButton(leftPos+10,topPos+10 , 14  ,14 ,1);
        itemEntityButton = new ConfigButton(leftPos+26,topPos+10 , 14  ,14 ,2);
        fluidButton = new ConfigButton(leftPos+10,topPos+26 , 14  ,14 ,3);
        spawnerButton = new ConfigButton(leftPos+26,topPos+26 , 14  ,14 ,4);
        mobsInvButton = new ConfigButton(leftPos+10,topPos+42 , 14  ,14 ,5);
        dropsButton = new ConfigButton(leftPos+26,topPos+42 , 14  ,14 ,6);
        pauseButton = new ConfigButton(leftPos+10,topPos+58 , 14  ,14 ,7);
        infoButton = new ConfigButton(leftPos+26,topPos+58 , 14  ,14 ,8);

        Player player = Minecraft.getInstance().player;
       if (player!=null){
            ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack && compassStack.configMode(stack)){
                removeSearchButtons();
                addConfigButtons();
            }
            if (stack.getItem() instanceof TravelersCompassItem compassStack && !compassStack.configMode(stack)){
                removeConfigButtons();
                addSearchButtons();
            }
        }
        renderTabButtons();
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {}

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
        if (player!=null){
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack){
                if (compassStack.configMode(player.getMainHandItem())){
                    guiGraphics.blit(TEXTURE_2, leftPos, topPos, 0, 0, imageWidth, imageHeight);
                }
                else {
                    guiGraphics.blit(TEXTURE_1, leftPos, topPos, 0, 0, imageWidth, imageHeight);
                }
                for (int i : compassStack.favoriteSlots(stack)){
                    int j = i > 2 ? 1 : 0;
                    j = i > 5 ? 2 : j;
                    int c = i > 2 ? i-3 : i;
                    c = i > 5 ? i-6 : c;
                    guiGraphics.blit(WIDGETS, leftPos+61+c*18, topPos+13+j*18, 0, 152, 18, 18);
                }
            }
        }
    }
    public void renderTabButtons(){
        statusButton = new TabButton(leftPos+152, topPos+63,24, 19,3, (configButton) -> {});
        searchButton = new TabButton(leftPos+119, topPos+14,26, 24,0, (configButton) ->
        {
            Player player =  Minecraft.getInstance().player;
            if (player == null ){
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack && !compassStack.configMode(stack)){
                return;
            }
            if(stack.getItem() instanceof TravelersCompassItem compassStack){
                removeConfigButtons();
                addSearchButtons();
                NetworkHandler.CHANNEL.sendToServer(new SearchButtonPacket(5));
                compassStack.setConfigMode(stack, false);
            }

        });
        configButton = new TabButton(leftPos+119, topPos+43,26, 24,1,(configButton) ->
        {
            Player player =  Minecraft.getInstance().player;
            if (player == null ){
                return;
            }
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof TravelersCompassItem compassStack && compassStack.configMode(stack)){
                return;
            }
            if(stack.getItem() instanceof TravelersCompassItem compassStack){
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
        if (player == null || minecraft == null){
            return;
        }
        ItemStack stack = player.getMainHandItem();
        boolean shiftKeyPressed = GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(minecraft.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (stack.getItem() instanceof TravelersCompassItem compassStack) {
            CompassContainer compassContainer = CompassContainer.container(stack);

            boolean fluids = compassStack.isSearchingFluids(stack);
            boolean villagers = compassStack.isSearchingVillagers(stack);
            boolean droppedItems = compassStack.isSearchingItemEntities(stack);
            boolean spawners = compassStack.isSearchingSpawners(stack);
            boolean invS = compassStack.isSearchingMobsInv(stack);
            boolean drops = compassStack.isSearchingDrops(stack);
            boolean pause = compassStack.isPaused(stack);
            boolean blocks = compassStack.isSearchingBlocks(stack);
            boolean containers = compassStack.isSearchingContainers(stack);
            boolean mobs = compassStack.isSearchingMobs(stack);

            String blocksTooltip = Component.translatable("options.travelerscompass.tooltip.block_button").getString();
            if (shiftKeyPressed) {
                blocksTooltip +=(Component.translatable("options.travelerscompass.tooltip.blocks.info").getString());
            }
            blocksTooltip +=(blocks ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());


            String containersTooltip = Component.translatable("options.travelerscompass.tooltip.container_button").getString();
            if (shiftKeyPressed) {
                containersTooltip +=(Component.translatable("options.travelerscompass.tooltip.containers.info").getString());
            }
            containersTooltip +=(containers ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String mobsTooltip = Component.translatable("options.travelerscompass.tooltip.mob_button").getString();
            if (shiftKeyPressed) {
                mobsTooltip +=(Component.translatable("options.travelerscompass.tooltip.mobs.info").getString());
            }
            mobsTooltip +=(mobs ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());


            String fluidTooltip = Component.translatable("options.travelerscompass.tooltip.fluid_button").getString();
            if (shiftKeyPressed) {
                fluidTooltip +=(Component.translatable("options.travelerscompass.tooltip.fluids.info").getString());
            }
            fluidTooltip +=(fluids ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String villagerTooltip = Component.translatable("options.travelerscompass.tooltip.villager_button").getString();
            if (shiftKeyPressed) {
                villagerTooltip +=(Component.translatable("options.travelerscompass.tooltip.villagers.info").getString());
            }
            villagerTooltip +=(villagers ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String itemEntityTooltip = Component.translatable("options.travelerscompass.tooltip.item_entity_button").getString();
            if (shiftKeyPressed) {
                itemEntityTooltip +=(Component.translatable("options.travelerscompass.tooltip.item_entity.info").getString());
            }
            itemEntityTooltip +=(droppedItems ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String spawnerTooltip = Component.translatable("options.travelerscompass.tooltip.spawner_button").getString();
            if (shiftKeyPressed) {
                spawnerTooltip +=(Component.translatable("options.travelerscompass.tooltip.spawners.info").getString());
            }
            spawnerTooltip +=(spawners ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                    Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String mobsInvToolTip = Component.translatable("options.travelerscompass.tooltip.mobs_inv_button").getString();
            if (shiftKeyPressed) {
                mobsInvToolTip +=(Component.translatable("options.travelerscompass.tooltip.mobs_inv.info").getString());
            }
            mobsInvToolTip += (invS ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                            Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String dropsToolTip = Component.translatable("options.travelerscompass.tooltip.mobs_drop_button").getString();
            if (shiftKeyPressed) {
                dropsToolTip +=(Component.translatable("options.travelerscompass.tooltip.drops.info").getString());
            }
            dropsToolTip += (drops ? Component.translatable("options.travelerscompass.tooltip.active").getString() :
                            Component.translatable("options.travelerscompass.tooltip.not_active").getString());

            String pauseToolTip = pause ? Component.translatable("options.travelerscompass.tooltip.play").getString() :
                    Component.translatable("options.travelerscompass.tooltip.pause_1").getString();
            if (shiftKeyPressed) {
                pauseToolTip += (pause ? Component.translatable("options.travelerscompass.tooltip.play_more").getString() :
                                Component.translatable("options.travelerscompass.tooltip.pause_more").getString());
            }
            Component statusToolTip = Component.empty();
            List<Integer> modes = compassStack.selectedModes(stack);
            boolean nothingSelected = modes.isEmpty() || (modes.contains(CompassMode.PAUSED.getID()) && modes.size() == 1);
            if (compassStack.positionRelativeToTarget(stack) == 5){
                statusToolTip = Component.translatable("options.travelerscompass.tooltip.not_enough_exp").append(Component.translatable("options.travelerscompass.tooltip.not_enough_exp_more").withStyle(ChatFormatting.GRAY));
            }
            else if (compassContainer.isEmpty()){
                statusToolTip = Component.translatable("options.travelerscompass.tooltip.empty").append(Component.translatable("options.travelerscompass.tooltip.empty_more").withStyle(ChatFormatting.GRAY));
            }
            else if (nothingSelected){
                statusToolTip = Component.translatable("options.travelerscompass.tooltip.not_selected_1").append(Component.translatable("options.travelerscompass.tooltip.not_selected_2").withStyle(ChatFormatting.GRAY));
            }
            else if (TravelersCompassItem.getFoundPosition(stack) != null && TCConfig.xpDrain.get() && !player.getAbilities().instabuild){
                statusToolTip = Component.translatable("options.travelerscompass.tooltip.enough_exp").append(Component.translatable("options.travelerscompass.tooltip.enough_exp_more",TCConfig.xpCost.get(),TCConfig.xpDrainRate.get()/20).withStyle(ChatFormatting.GRAY));
            }
            Component foundPos;
            Component distance;
            if (TravelersCompassItem.getFoundPosition(stack) != null){
                foundPos = Component.literal("\n"+
                        TravelersCompassItem.getFoundPosition(stack).getX()+" "+
                        TravelersCompassItem.getFoundPosition(stack).getY()+" "+
                        TravelersCompassItem.getFoundPosition(stack).getZ()+"\n"
                ).withStyle(ChatFormatting.GRAY);
                distance = Component.literal("\n"+(int) Math.sqrt(TravelersCompassItem.getFoundPosition(stack).distSqr(player.getOnPos()))).withStyle(ChatFormatting.GRAY);
            }
            else {
                foundPos = Component.literal("\n"+Component.translatable("options.travelerscompass.tooltip.none").getString() + "\n").withStyle(ChatFormatting.GRAY);
                distance = Component.literal("\n"+Component.translatable("options.travelerscompass.tooltip.none").getString()).withStyle(ChatFormatting.GRAY);
            }
            infoButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.info_button_1")
                    .append(foundPos).append(Component.translatable("options.travelerscompass.tooltip.info_button_2").append(distance))));
            fluidButton.setTooltip(Tooltip.create(Component.literal(fluidTooltip)));
            villagerButton.setTooltip(Tooltip.create(Component.literal(villagerTooltip)));
            itemEntityButton.setTooltip(Tooltip.create(Component.literal(itemEntityTooltip)));
            spawnerButton.setTooltip(Tooltip.create(Component.literal(spawnerTooltip)));
            mobsInvButton.setTooltip(Tooltip.create(Component.literal(mobsInvToolTip)));
            dropsButton.setTooltip(Tooltip.create(Component.literal(dropsToolTip)));
            pauseButton.setTooltip(Tooltip.create(Component.literal(pauseToolTip)));
            statusButton.setTooltip(Tooltip.create(statusToolTip));
            blockButton.setTooltip(Tooltip.create(Component.literal(blocksTooltip)));
            mobButton.setTooltip(Tooltip.create(Component.literal(mobsTooltip)));
            containerButton.setTooltip(Tooltip.create(Component.literal(containersTooltip)));
            checkDisabledButtons();
        }
    }
    private void checkDisabledButtons(){
        if (!TCConfig.enableMobSearch.get()){
            mobButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableBlockSearch.get()){
            blockButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableContainerSearch.get()){
            containerButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableVillagersSearch.get()){
            villagerButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableItemEntitiesSearch.get()){
            itemEntityButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableFluidSearch.get()){
            fluidButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableSpawnerSearch.get()){
            spawnerButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableDropSearch.get()){
            dropsButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
        if (!TCConfig.enableMobsInventorySearch.get()){
            mobsInvButton.setTooltip(Tooltip.create(Component.translatable("options.travelerscompass.tooltip.disabled")));
        }
    }

    private void addSearchButtons(){
        addRenderableWidget(blockButton);
        addRenderableWidget(containerButton);
        addRenderableWidget(mobButton);
    }
    private void removeSearchButtons(){
        removeWidget(blockButton);
        removeWidget(containerButton);
        removeWidget(mobButton);
    }
    private void addConfigButtons(){
        addRenderableWidget(villagerButton);
        addRenderableWidget(itemEntityButton);
        addRenderableWidget(fluidButton);
        addRenderableWidget(spawnerButton);
        addRenderableWidget(infoButton);
        addRenderableWidget(mobsInvButton);
        addRenderableWidget(dropsButton);
        addRenderableWidget(pauseButton);
    }
    private void removeConfigButtons(){
        removeWidget(villagerButton);
        removeWidget(itemEntityButton);
        removeWidget(fluidButton);
        removeWidget(spawnerButton);
        removeWidget(infoButton);
        removeWidget(mobsInvButton);
        removeWidget(dropsButton);
        removeWidget(pauseButton);
    }
}