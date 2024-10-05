package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.network.NetworkHandler;
import com.nine.travelerscompass.common.network.packet.ConfigButtonPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ConfigButton extends BaseButton {

    private final ButtonType type;

    public ConfigButton(int x, int y, ButtonType type) {
        super(x, y, 14, 14);
        this.type = type;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }

    @Override
    public boolean mouseClicked(double xMousePos, double yMousePos, int p_93643_) {
        if ((!TCConfig.enableItemEntitiesSearch.get() && this.type.getID() == ButtonType.DROPPED_ITEMS.getID())
                || (!TCConfig.enableFluidSearch.get() && this.type.getID() == ButtonType.FLUIDS.getID())
                || (!TCConfig.enableSpawnerSearch.get() && this.type.getID() == ButtonType.SPAWNERS.getID())
                || (!TCConfig.enableDropSearch.get() && this.type.getID() == ButtonType.DROPS.getID())
                || (!TCConfig.enableContainerSearch.get() && this.type.getID() == ButtonType.CONTAINERS.getID())
                || (!TCConfig.enableMobSearch.get() && this.type.getID() == ButtonType.MOBS.getID())
                || (!TCConfig.enableBlockSearch.get() && this.type.getID() == ButtonType.BLOCKS.getID())
                || (!TCConfig.enableWiderSearch.get() && this.type.getID() == ButtonType.WIDE_SEARCH.getID())) {
            return false;
        }
        return super.mouseClicked(xMousePos, yMousePos, p_93643_);
    }

    @Override
    public void onPress() {
        Player player = Minecraft.getInstance().player;
        if (player == null || ((!TCConfig.enableItemEntitiesSearch.get() && this.type == ButtonType.DROPPED_ITEMS)
                || (!TCConfig.enableFluidSearch.get() && this.type == ButtonType.FLUIDS)
                || (!TCConfig.enableSpawnerSearch.get() && this.type == ButtonType.SPAWNERS)
                || (!TCConfig.enableDropSearch.get() && this.type == ButtonType.DROPS)
                || (!TCConfig.enableContainerSearch.get() && this.type == ButtonType.CONTAINERS)
                || (!TCConfig.enableMobSearch.get() && this.type == ButtonType.MOBS)
                || (!TCConfig.enableBlockSearch.get() && this.type == ButtonType.BLOCKS)
                || (!TCConfig.enableWiderSearch.get() && this.type == ButtonType.WIDE_SEARCH)
                || (TCConfig.forcedLazySearchMode.get() && this.type == ButtonType.LAZY_MODE)
        )) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof TravelersCompassItem) {
            NetworkHandler.CHANNEL.sendToServer(new ConfigButtonPacket(this.type.getID(), controlPressed(), shiftPressed()));
        }
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            int xPos = this.type.getID() * 14;
            int yPos = 0;
            if (this.type == ButtonType.PAUSE) {
                yPos = 56;
                xPos = 0;
            }
            if (this.type == ButtonType.INFO) {
                double rate = TCConfig.searchRate.get() / 20.0;
                String formattedRate = String.format("%.2f", rate);
                this.setTooltip(Tooltip.create((Component.translatable("options.travelerscompass.tooltip.info_button_1").append(
                        Component.translatable("options.travelerscompass.tooltip.info_button_2", formattedRate).append(
                                Component.translatable("options.travelerscompass.tooltip.info_button_3"
                                ))))));

                yPos = 56;
                xPos = 14;
            }
            if (this.type == ButtonType.WIDE_SEARCH) {
                yPos = 56;
                xPos = 28;
            }
            if (this.type == ButtonType.STYLE) {
                yPos = 56;
                xPos = 98;
            }
            //DISTANCE
            if (this.type == ButtonType.BLOCKS_DISTANCE) {
                yPos = 56;
                xPos = 84;
            }
            if (this.type == ButtonType.CONTAINERS_DISTANCE) {
                yPos = 70;
                xPos = 84;
            }
            if (this.type == ButtonType.MOBS_DISTANCE) {
                yPos = 84;
                xPos = 84;
            }
            if (this.type == ButtonType.WIDE_DISTANCE) {
                yPos = 98;
                xPos = 84;
            }
            if (this.type == ButtonType.PRIORITY_SWITCH) {
                yPos = 56;
                xPos = 98;
            }
            if (this.type == ButtonType.LABELS) {
                yPos = 56;
                xPos = 112;
            }
            if (this.type == ButtonType.HUD) {
                yPos = 56;
                xPos = 126;
            }
            if (this.type == ButtonType.FULL_RESET) {
                yPos = 56;
                xPos = 168;
            }
            if (this.type == ButtonType.SOUND) {
                yPos = 56;
                xPos = 182;
            }
            if (this.type == ButtonType.LAZY_MODE) {
                yPos = 84;
                xPos = 168;
            }
            if (stack.getItem() instanceof TravelersCompassItem compassStack) {
                if (this.type.getID() == 0 && compassStack.isSearchingMobs(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 1 && compassStack.isSearchingBlocks(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 2 && compassStack.isSearchingContainers(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 3 && compassStack.isSearchingFluids(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 4 && compassStack.isSearchingVillagers(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 5 && compassStack.isSearchingSpawners(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 6 && compassStack.isSearchingEntitiesInv(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 7 && compassStack.isSearchingItemEntities(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == 8 && compassStack.isSearchingDrops(stack)) {
                    yPos += 28;
                }
                if (this.type.getID() == ButtonType.PAUSE.getID() && compassStack.isPaused(stack)) {
                    yPos += 28;
                }
                if (this.type == ButtonType.PRIORITY_SWITCH && !compassStack.priorityMode(stack)) {
                    yPos += 28;
                }
                if (this.type == ButtonType.LABELS && compassStack.showLabels(stack)) {
                    yPos += 28;
                }
                if (this.type == ButtonType.SOUND) {
                    if (!compassStack.sound(stack)) {
                        xPos += 14;
                    }
                }
                if (this.type == ButtonType.HUD) {
                    if (compassStack.hudModeRequiresHeld(stack)) {
                        yPos += 28;
                    }
                    if (compassStack.hudMode(stack)) {
                        xPos += 14;
                    }
                }
                if (this.type == ButtonType.LAZY_MODE) {
                    if (!TCConfig.forcedLazySearchMode.get()) {
                        xPos += compassStack.isLazyModeOn(stack) ? 14 : 28;
                    }
                }
                if (this.isHovered()) {
                    if (this.type == ButtonType.BLOCKS_DISTANCE
                            || this.type == ButtonType.MOBS_DISTANCE
                            || this.type == ButtonType.CONTAINERS_DISTANCE
                            || this.type == ButtonType.WIDE_DISTANCE) {
                        xPos = 56;
                        yPos = 56;
                        if (shiftPressed()) {
                            xPos += 14;
                        }
                        if (controlPressed()) {
                            yPos += 14;
                        }
                        int value = controlPressed() ? 5 : 1;
                        value *= shiftPressed() ? -1 : 1;
                        if (this.type == ButtonType.BLOCKS_DISTANCE) {
                            if (!(value + compassStack.blockSearchRadius(stack) <= TCConfig.blockSearchRadius.get() && compassStack.blockSearchRadius(stack) + value > 0)) {
                                yPos += 28;
                            }
                        }
                        if (this.type == ButtonType.MOBS_DISTANCE) {
                            if (!(value + compassStack.entitySearchRadius(stack) <= TCConfig.entitySearchRadius.get() && compassStack.entitySearchRadius(stack) + value > 0)) {
                                yPos += 28;
                            }
                        }
                        if (this.type == ButtonType.WIDE_DISTANCE) {
                            if (!(value + compassStack.wideSearchRadius(stack) <= TCConfig.wideSearchRadius.get() && compassStack.wideSearchRadius(stack) + value > 0)) {
                                yPos += 28;
                            }
                        }
                        if (this.type == ButtonType.CONTAINERS_DISTANCE) {
                            if (!(value + compassStack.containerSearchRadius(stack) <= TCConfig.containerSearchRadius.get() && compassStack.containerSearchRadius(stack) + value > 0)) {
                                yPos += 28;
                            }
                        }
                    } else {
                        yPos += 14;
                    }
                }
            }
            if (((!TCConfig.enableItemEntitiesSearch.get() && this.type.getID() == ButtonType.DROPPED_ITEMS.getID())
                    || (!TCConfig.enableFluidSearch.get() && this.type.getID() == ButtonType.FLUIDS.getID())
                    || (!TCConfig.enableSpawnerSearch.get() && this.type.getID() == ButtonType.SPAWNERS.getID())
                    || (!TCConfig.enableDropSearch.get() && this.type.getID() == ButtonType.DROPS.getID())
                    || (!TCConfig.enableContainerSearch.get() && this.type.getID() == ButtonType.CONTAINERS.getID())
                    || (!TCConfig.enableMobSearch.get() && this.type.getID() == ButtonType.MOBS.getID())
                    || (!TCConfig.enableBlockSearch.get() && this.type.getID() == ButtonType.BLOCKS.getID())
                    || (!TCConfig.enableWiderSearch.get() && this.type.getID() == ButtonType.WIDE_SEARCH.getID()))) {
                xPos = 42;
                yPos = 56;
            }
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), xPos, yPos, this.width, this.height);
        }
    }

}