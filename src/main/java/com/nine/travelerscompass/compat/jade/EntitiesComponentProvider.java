package com.nine.travelerscompass.compat.jade;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeSpawnEggItem;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public enum EntitiesComponentProvider implements IEntityComponentProvider {

    INSTANCE;
    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
        Entity entity = entityAccessor.getEntity();
        Player player = entityAccessor.getPlayer();
        if (!(player.getMainHandItem().getItem() instanceof TravelersCompassItem) || !TCConfig.JadeCompatibility.get()){
            return;
        }
        if (entity instanceof LivingEntity livingEntity){
            if (!ConfigUtils.isAllowedToSearch(livingEntity)) {
                tooltip.add(Component.translatable("options.travelerscompass.tooltip.forbidden.entity").withStyle(ChatFormatting.RED));
            }
            else {
                CompassContainer compassContainer = CompassContainer.container(player.getMainHandItem());
                if (livingEntity instanceof Mob mob
                        && ForgeSpawnEggItem.fromEntityType(mob.getType()) != null
                        && !compassContainer.getList().contains(ForgeSpawnEggItem.fromEntityType(mob.getType()))) {
                    tooltip.add(Component.translatable("options.travelerscompass.tooltip.shift.entity").withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(TravelersCompass.MODID);
    }
}