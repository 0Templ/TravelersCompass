package com.nine.travelerscompass.mixin.client;

import com.nine.travelerscompass.client.utils.ClientUtils;
import com.nine.travelerscompass.common.container.menu.CompassMenu;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.cost.SearchCost;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void travelerscompass$appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (player.containerMenu instanceof CompassMenu && player.getMainHandItem().getItem() instanceof TravelersCompassItem) {
            Item item = stack.getItem();
            FilterReason filterReason = FilterManager.getFilterReason(item);
            if (!(filterReason instanceof FilterReason.Allowed)){
                Component reasonComponent = ClientUtils.formatReason(filterReason);
                tooltipComponents.add(reasonComponent);
                return;
            }
            if (TCConfig.SEARCH_REQUIRES_XP.get()){
                SearchCost sc = SearchCostHelper.getCost(item);
                if (!sc.isFree()) {
                    boolean reqOk = sc.meetsRequirement(player.experienceLevel) || player.isCreative();
                    Component req = sc.reqAsComponent().withStyle(reqOk ? ChatFormatting.GRAY : ChatFormatting.RED);
                    Component cost = sc.costAsComponent().withStyle(ChatFormatting.GRAY);
                    if (sc.reqLevel() > 0) {
                        tooltipComponents.add(req);
                    }
                    if (sc.costLevel() > 0) {
                        tooltipComponents.add(cost);
                    }
                }
            }
        }
    }
}
