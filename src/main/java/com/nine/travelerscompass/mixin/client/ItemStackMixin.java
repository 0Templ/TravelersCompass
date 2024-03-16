package com.nine.travelerscompass.mixin.client;

import com.google.common.collect.Lists;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;


@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At("TAIL"), cancellable = true)
    public void travelerscompass$getTooltipLines(@Nullable Player p_41652_, TooltipFlag p_41653_,CallbackInfoReturnable<List<Component>> ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null){
            return;
        }

        if (player.containerMenu instanceof CompassMenu menu && player.getMainHandItem().getItem() instanceof TravelersCompassItem compassItem){
            ItemStack stack = (ItemStack) (Object) this;
            Component forbidenComponent = Component.translatable("options.travelerscompass.tooltip.forbidden").withStyle(ChatFormatting.RED);
        /*    MutableComponent favoriteComponent = Component.empty();
            if (Screen.hasShiftDown()){
                favoriteComponent.append(Component.translatable("options.travelerscompass.tooltip.favorite").withStyle(ChatFormatting.GRAY));
            }*/
            List<Component> list = Lists.newArrayList();
            MutableComponent mutablecomponent = (Component.literal("").append(stack.getHoverName()).withStyle(stack.getRarity().getStyleModifier()));
            if (stack.hasCustomHoverName()) {
                mutablecomponent.withStyle(ChatFormatting.ITALIC);
            }
            list.add(mutablecomponent);
            if (!ConfigUtils.isAllowedToSearch(stack)) {
                list.add(forbidenComponent);
                ci.setReturnValue(list);
            }
        }
    }
}
