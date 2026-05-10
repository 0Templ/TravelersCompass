package com.nine.travelerscompass.mixin.accessor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.SpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerAccessor {

    @Invoker("closeContainer")
    void tc$closeContainer();

}
