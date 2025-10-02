package com.nine.travelerscompass;

import com.nine.travelerscompass.config.ConfigSyncManager;
import com.nine.travelerscompass.config.cost.SearchCostHelper;
import com.nine.travelerscompass.config.filter.FilterManager;
import net.minecraft.client.player.LocalPlayer;

public class TCClient {

    public static void init(){
    }

    public static void onClientPlayerLogout(LocalPlayer player){
        ConfigSyncManager.SYNCED_VALUES.clear();
        SearchCostHelper.updateCosts();
        FilterManager.reload();
    }
}
