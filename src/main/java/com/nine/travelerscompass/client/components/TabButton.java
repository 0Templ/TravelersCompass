package com.nine.travelerscompass.client.components;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.CompassMode;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;


public class TabButton extends Button {
    private int type;
    public TabButton(int x, int y, int width, int height, int type, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.type = type;
    }
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Player player =  Minecraft.getInstance().player;
        if (player != null) {
            int j = 72;
            int k;
            int d = this.type;
            k = (d == 0) ? 0 : 52;
            ItemStack stack = player.getMainHandItem();
            CompassContainer compassContainer = CompassContainer.container(stack);
            if (stack.getItem() instanceof TravelersCompassItem compassStack){
                List<Integer> modes = compassStack.selectedModes(stack);
                boolean nothingSelected = modes.isEmpty() || (modes.contains(CompassMode.PAUSED.getID()) && modes.size() == 1);
                if(compassStack.configMode(stack) && d != 1){
                    k += 26;
                }
                if(!compassStack.configMode(stack) && d == 1){
                    k += 26;
                }
                if(d == 3){
                    j = 170;
                    if (compassStack.positionRelativeToTarget(stack) == 5 || compassContainer.isEmpty() || nothingSelected || (TravelersCompassItem.getFoundPosition(stack) != null && TCConfig.xpDrain.get() && !player.getAbilities().instabuild)){
                        k = 0;
                    }
                    else {
                        return;
                    }
                }
            }
            pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), k, j, this.width, this.height);
        }
    }
    private static final ResourceLocation TEXTURE = new ResourceLocation(TravelersCompass.MODID, "textures/gui/component/gui_components.png");
}