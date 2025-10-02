package com.nine.travelerscompass.compat.top;

import com.nine.travelerscompass.config.NeoForgeTCConfig;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TheOneProbeSetup implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe top) {
        if (NeoForgeTCConfig.THE_ONE_PROBE_COMPATIBILITY.get()){
            top.registerProvider(new TOPBlockComponentProvider());
            top.registerEntityProvider(new TOPEntityComponentProvider());
        }
        return null;
    }

}