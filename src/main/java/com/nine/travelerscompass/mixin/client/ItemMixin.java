package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void travelerscompass$appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.containerMenu instanceof CompassMenu menu && player.getMainHandItem().getItem() instanceof TravelersCompassItem compassItem) {
            Component forbidenComponent = Component.translatable("options.travelerscompass.tooltip.forbidden").withStyle(ChatFormatting.RED);
            MutableComponent coloredTargetName = Component.translatable("options.travelerscompass.tooltip.favorite")
                    .withStyle(Component.translatable("options.travelerscompass.tooltip.favorite").getStyle().withColor(0xd4b16a));
            if (!ConfigUtils.isAllowedToSearch(stack)) {
                tooltipComponents.add(forbidenComponent);
                return;
            }
            NonNullList<ItemStack> checkList = menu.getItems();
            checkList.subList(9, menu.getItems().size()).clear();
            if (!(checkList.contains(stack))) {
                return;
            }
            List<ItemStack> list = new ArrayList<>();
            ArrayList<Integer> favoriteIndexes = compassItem.favoriteSlots(player.getMainHandItem());
            for (int i : favoriteIndexes) {
                list.add(menu.getItems().get(i));
            }
            if (list.contains(stack)) {
                tooltipComponents.add(coloredTargetName);
            }
        }
    }
}
