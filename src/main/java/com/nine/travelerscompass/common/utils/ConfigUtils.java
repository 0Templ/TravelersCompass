package com.nine.travelerscompass.common.utils;

import com.nine.travelerscompass.TCConfig;
import net.minecraft.core.registries.BuiltInRegistries;
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

        boolean modCheck = true;
        boolean notEmpty = !TCConfig.filteredModListEntities.get().isEmpty() || !TCConfig.filteredModList.get().isEmpty() || !TCConfig.filteredEntities.get().isEmpty();
        if (location != null){
            modCheck = TCConfig.filteredModListEntities.get().contains(location.getNamespace())
                    || TCConfig.filteredModList.get().contains(location.getNamespace());
        }
        if ((modCheck || TCConfig.filteredEntities.get().contains(entity.getType().toString())) && TCConfig.blackListFilter.get()){
            return false;
        }
        if (!(modCheck || TCConfig.filteredEntities.get().contains(entity.getType().toString())) && !TCConfig.blackListFilter.get() && notEmpty){
            return false;
        }
        return true;
    }

    public static boolean isAllowedToSearch(ItemStack stack){
        ResourceLocation location = ForgeRegistries.ITEMS.getKey(stack.getItem());
        boolean modCheck = true;
        List<String> tagLocations = stack.getTags()
                .map(TagKey::location)
                .map(ResourceLocation::toString)
                .toList();
        boolean tagCheck = TCConfig.filteredTagItemList.get().stream().anyMatch(tagLocations::contains);
        boolean itemBlockCheck = TCConfig.filteredItemList.get().contains(stack.getDescriptionId().replaceFirst("block.", "").replaceFirst("item.", ""));
        boolean notEmpty = !TCConfig.filteredTagItemList.get().isEmpty() || !TCConfig.filteredItemList.get().isEmpty() || !TCConfig.filteredModList.get().isEmpty();
        if (location != null){
            modCheck = TCConfig.filteredModListEntities.get().contains(location.getNamespace())
                    || TCConfig.filteredModListItems.get().contains(location.getNamespace());
        }
        if ((modCheck || tagCheck ||itemBlockCheck) && TCConfig.blackListFilter.get()){
            return false;
        }
        if (!(modCheck || tagCheck ||itemBlockCheck) && !TCConfig.blackListFilter.get() && notEmpty){
            return false;
        }
        return true;
    }
    public static boolean hasLootr() {
        return ModList.get().isLoaded("lootr");
    }
}
