package com.nine.travelerscompass.common.utils;

import com.nine.travelerscompass.TCConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ConfigUtils {

    public static boolean isAllowedToSearch(LivingEntity entity){
        ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        boolean shouldSearchModCheck = true;
        if (location != null){
            shouldSearchModCheck = TCConfig.blackListFilter.get() != TCConfig.filteredEntityByModIDList.get().contains(location.getNamespace());
        }
        return TCConfig.blackListFilter.get() != (TCConfig.filteredEntityList.get().contains(entity.getType().toString()) && shouldSearchModCheck);
    }

    public static boolean isAllowedToSearch(ItemStack stack){
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(stack.getItem());
        boolean modCheck = false;
        if (location != null) {
            modCheck =  TCConfig.filteredModList.get().contains(location.getNamespace());
        }
        List<String> tagLocations = stack.getTags()
                .map(TagKey::location)
                .map(ResourceLocation::toString)
                .toList();
        boolean tagCheck = TCConfig.filteredTagItemList.get().stream().anyMatch(tagLocations::contains);
        boolean itemBlockCheck = TCConfig.filteredItemList.get().contains(stack.getDescriptionId().replaceFirst("block.", "").replaceFirst("item.", ""));

        if ((tagCheck || itemBlockCheck || modCheck) && TCConfig.blackListFilter.get()) {
            return false;
        }
        else return tagCheck || itemBlockCheck || modCheck || TCConfig.blackListFilter.get();
    }
    public static boolean hasLootr() {
        return ModList.get().isLoaded("lootr");
    }
}
