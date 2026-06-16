//package com.nine.travelerscompass.compat.jade;
//
//import com.nine.travelerscompass.TCCommon;
//import com.nine.travelerscompass.common.container.CompassContainer;
//import com.nine.travelerscompass.common.item.TravelersCompassItem;
//import com.nine.travelerscompass.config.filter.FilterManager;
//import com.nine.travelerscompass.config.filter.FilterReason;
//import net.minecraft.ChatFormatting;
//import net.minecraft.core.Holder;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.SpawnEggItem;
//import snownee.jade.api.EntityAccessor;
//import snownee.jade.api.IEntityComponentProvider;
//import snownee.jade.api.ITooltip;
//import snownee.jade.api.config.IPluginConfig;
//
//import java.util.Optional;
//
//public enum EntitiesComponentProvider implements IEntityComponentProvider {
//
//    INSTANCE;
//
//    @Override
//    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
//        Entity entity = entityAccessor.getEntity();
//        Player player = entityAccessor.getPlayer();
//        ItemStack stack = player.getMainHandItem();
//        if (!(stack.getItem() instanceof TravelersCompassItem)) {
//            return;
//        }
//        if (entity instanceof Mob mob) {
//            var type = mob.getType();
//            FilterReason reason = FilterManager.getFilterReason(type);
//            if (reason instanceof FilterReason.Allowed) {
//				Optional<SpawnEggItem> eggItem = SpawnEggItem.byId(mob.getType())
//						.map(Holder::value)
//						.filter(SpawnEggItem.class::isInstance)
//						.map(SpawnEggItem.class::cast);
//                if (eggItem.isPresent()){
//                    if (CompassContainer.container(stack).getList().contains(eggItem.get().asItem())) {
//                        return;
//                    }
//                }
//                tooltip.add(Component.translatable("waila.travelerscompass.info.entity").withStyle(ChatFormatting.GRAY));
//            }
//            else {
//                tooltip.add(Component.translatable("waila.travelerscompass.forbidden_entity").withStyle(ChatFormatting.RED));
//            }
//        }
//    }
//
//    @Override
//    public Identifier getUid() {
//        return Identifier.parse(TCCommon.MODID);
//    }
//}