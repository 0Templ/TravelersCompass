package com.nine.travelerscompass.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TheOneProbeRegistry implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe top) {
        top.registerProvider(new TOPBlockComponentProvider());
        top.registerEntityProvider(new TOPEntityComponentProvider());
        return null;
    }
}