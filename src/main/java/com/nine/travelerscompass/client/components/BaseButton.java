package com.nine.travelerscompass.client.components;


import com.nine.travelerscompass.TravelersCompass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class BaseButton extends AbstractButton {

    public static final ResourceLocation TEXTURE = new ResourceLocation(TravelersCompass.MODID, "textures/gui/component/gui_components.png");

    public boolean initial = false;
    public boolean post = false;

    public BaseButton(int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
    }

    public final boolean controlPressed() {
        Minecraft mc = Minecraft.getInstance();
        return GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS;
    }

    public final boolean shiftPressed() {
        Minecraft mc = Minecraft.getInstance();
        return GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
    }

    @Override
    public void onPress() {

    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput elementOutput) {

    }
}
