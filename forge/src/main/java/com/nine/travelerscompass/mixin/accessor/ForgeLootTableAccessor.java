package com.nine.travelerscompass.mixin.accessor;

import com.nine.travelerscompass.platform.accessor.LootTableAccessor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTable.class)
public abstract class ForgeLootTableAccessor implements LootTableAccessor {

    @Accessor("pools")
    public abstract List<LootPool> travelerscompass$pools();

    @Override
    @Unique
    public List<LootPool> travelerscompass$getPools(){
        return travelerscompass$pools();
    }

}
