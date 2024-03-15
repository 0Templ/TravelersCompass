package com.nine.travelerscompass.common.item;

public enum CompassMode {

    SEARCHING_MOBS(0),
    SEARCHING_CONTAINERS(1),
    SEARCHING_BLOCKS(2),
    SEARCHING_FLUIDS(3),
    SEARCHING_VILLAGERS(4),
    SEARCHING_SPAWNERS(5),
    SEARCHING_MOBS_INV(6),
    SEARCHING_DROPPED_ITEMS(7),
    SEARCHING_MOB_DROP(8),
    PAUSED(9);
    private int id;
    CompassMode(int id) {
        this.id = id;
    }
    public int getID() {
        return id;
    }
}