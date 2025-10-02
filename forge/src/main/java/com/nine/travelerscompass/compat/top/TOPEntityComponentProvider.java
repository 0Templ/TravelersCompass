package com.nine.travelerscompass.compat.top;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class TOPEntityComponentProvider implements IProbeInfoEntityProvider {

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof TravelersCompassItem)) {
            return;
        }
        if (entity instanceof Mob mob) {
            var type = mob.getType();
            FilterReason reason = FilterManager.getFilterReason(type);
            if (reason instanceof FilterReason.Allowed) {
                SpawnEggItem spawnEggItem = ForgeSpawnEggItem.fromEntityType(type);
                if (spawnEggItem != null){
                    if (CompassContainer.container(stack).getList().contains(spawnEggItem.asItem())) {
                        return;
                    }
                }
                info.text(CompoundText.create().style(TextStyleClass.LABEL)
                        .text(Component.translatable("waila.travelerscompass.info.entity"))
                        .style(TextStyleClass.LABEL));
            }
            else {
                info.text(CompoundText.create().style(TextStyleClass.ERROR)
                        .text(Component.translatable("waila.travelerscompass.forbidden_entity"))
                        .style(TextStyleClass.ERROR));
            }
        }
    }

    @Override
    public String getID() {
        return ResourceLocation.fromNamespaceAndPath(TCCommon.MODID, "mob_info").toString();
    }
}