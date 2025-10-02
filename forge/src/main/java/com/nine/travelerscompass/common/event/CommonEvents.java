package com.nine.travelerscompass.common.event;

import com.nine.travelerscompass.TCCommon;
import com.nine.travelerscompass.common.search.SearchManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = TCCommon.MODID)
public class CommonEvents {

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        TCCommon.updateCache();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        SearchManager.tick();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TCCommon.onServerPlayerLogin(player);
        }
    }

}
