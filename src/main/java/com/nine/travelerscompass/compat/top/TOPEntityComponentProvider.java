package com.nine.travelerscompass.compat.top;

import com.nine.travelerscompass.TCConfig;
import com.nine.travelerscompass.TravelersCompass;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class TOPEntityComponentProvider implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return new ResourceLocation(TravelersCompass.MODID, "mob_info").toString();
    }


    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo info, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (!(player.getMainHandItem().getItem() instanceof TravelersCompassItem) || !TCConfig.TheOneProbeCompatibility.get()){
            return;
        }
        if (entity instanceof LivingEntity livingEntity){
            if (!ConfigUtils.isAllowedToSearch(livingEntity)) {
                info.text(CompoundText.create().style(TextStyleClass.ERROR)
                        .text(Component.translatable("options.travelerscompass.tooltip.forbidden.entity"))
                        .style(TextStyleClass.ERROR));
            }
            else {
                CompassContainer compassContainer = CompassContainer.container(player.getMainHandItem());
                if (livingEntity instanceof Mob mob
                        && ForgeSpawnEggItem.fromEntityType(mob.getType()) != null
                        && !compassContainer.getList().contains(ForgeSpawnEggItem.fromEntityType(mob.getType()))) {
                    info.text(CompoundText.create().style(TextStyleClass.LABEL)
                            .text(Component.translatable("options.travelerscompass.tooltip.shift.entity"))
                            .style(TextStyleClass.LABEL));
                }
            }
        }
    }
}