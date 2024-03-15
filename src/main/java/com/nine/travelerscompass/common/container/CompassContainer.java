package com.nine.travelerscompass.common.container;

import com.nine.travelerscompass.common.item.TravelersCompassItem;
import com.nine.travelerscompass.common.utils.ConfigUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CompassContainer implements Container {
    private final ItemStack containerStack;
    private final NonNullList<ItemStack> inventory;
    private final int maxStackSize;

    private CompassContainer(ItemStack containerStack, int inventorySize, int maxStackSize) {
        this.containerStack = containerStack;
        this.maxStackSize = maxStackSize;
        inventory = NonNullList.<ItemStack>withSize(inventorySize, ItemStack.EMPTY);
        load(containerStack.getOrCreateTag());
    }
    public ArrayList<Item> getFavoriteList(TravelersCompassItem containerStack, ItemStack stack){
        ArrayList<Integer> list1 = containerStack.favoriteSlots(stack);
        ArrayList<Item> list2 = new ArrayList<>();
        for (int i : list1) {
            Item item = this.getItem(i).getItem();
            if (!(item instanceof AirItem)){
                list2.add(item);
            }
        }
        return list2;
    }

    public static CompassContainer container(ItemStack containerHolder) {
        return new CompassContainer(containerHolder, 9, 1);
    }
    public List<Item> getList(){
        List<Item> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Item item = this.getItem(i).getItem();
            if (!(item instanceof AirItem)){
                list.add(item);
            }
        }
        return list;
    }
    public boolean hasAny(ItemStack itemStack){
        for(int i = 0; i < 9; ++i) {
            if (itemStack.is(this.getItem(i).getItem())){
                return true;
            }
        }
        return false;
    }
    public int getFirstEmptySlot(){
        for(int i = 0; i < 9; ++i) {
            if (this.getItem(i).is(ItemStack.EMPTY.getItem())){
                return i;
            }
        }
        return 0;
    }
    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return inventory.get(index);
    }

    public void load(CompoundTag tag) {
        ListTag items = tag.getList("ItemInventory", Tag.TAG_COMPOUND);

        for (int i = 0; i < items.size(); i++) {
            CompoundTag item = items.getCompound(i);
            int slot = item.getInt("Slot");

            if (slot < getContainerSize())
                inventory.set(slot, ItemStack.of(item));
        }
    }

    public void save(CompoundTag tag) {
        ListTag items = new ListTag();

        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty()) {
                CompoundTag item = new CompoundTag();

                item.putInt("Slot", i);
                getItem(i).save(item);
                items.add(item);
            }
        }

        tag.put("ItemInventory", items);
    }

    @Override
    public ItemStack removeItem(int index, int size) {
        ItemStack stack = getItem(index);


        if (!stack.isEmpty())
            if (stack.getCount() > size) {
                stack = stack.split(size);
                setChanged();
            }
            else {
                setItem(index, ItemStack.EMPTY);
            }
        stack.setCount(0);
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = getItem(index);

        setItem(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {

        if (stack.getItem() instanceof TravelersCompassItem){
        return;
        }
        if (stack == ItemStack.EMPTY){
            inventory.set(index, stack);
            if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
            setChanged();
        }
        if (!ConfigUtils.isAllowedToSearch(stack)){
            return;
        }
        inventory.set(index, stack);
            if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
            setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public void setChanged() {
        for (int i = 0; i < getContainerSize(); i++) {
            if (!getItem(i).isEmpty() && getItem(i).getCount() == 0)
                inventory.set(i, ItemStack.EMPTY);
        }
        save(containerStack.getTag());
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void startOpen(Player player) {}

    @Override
    public void stopOpen(Player player) {}

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty())
                return false;
        }

        return true;
    }
}

