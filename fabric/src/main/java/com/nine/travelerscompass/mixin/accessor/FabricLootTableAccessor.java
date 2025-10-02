package com.nine.travelerscompass.mixin.accessor;

import com.nine.travelerscompass.platform.accessor.LootTableAccessor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Arrays;
import java.util.List;

@Mixin(LootTable.class)
public abstract class FabricLootTableAccessor implements LootTableAccessor {

    @Accessor("pools")
    public abstract LootPool[] travelerscompass$pools();

    @Override
    public List<LootPool> travelerscompass$getPools(){
        return Arrays.stream(travelerscompass$pools()).toList();
    }

}
