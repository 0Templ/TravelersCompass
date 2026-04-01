package com.nine.travelerscompass.compat.jade;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeSetup implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BlocksComponentProvider.INSTANCE, Block.class);
        registration.registerEntityComponent(EntitiesComponentProvider.INSTANCE, Mob.class);
    }


}