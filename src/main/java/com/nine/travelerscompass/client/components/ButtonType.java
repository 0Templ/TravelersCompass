package com.nine.travelerscompass.client.components;

public enum ButtonType {
    MOBS(0),
    BLOCKS(1),
    CONTAINERS(2),
    FLUIDS(3),
    //VILLAGERS(4),
    SPAWNERS(5),
    //INVENTORIES(6),
    DROPPED_ITEMS(7),
    DROPS(8),
    PAUSE(-1),
    INFO(-2),
    WIDE_SEARCH(-3),
    STYLE(-4),
    BLOCKS_DISTANCE(-5),
    MOBS_DISTANCE(-6),
    CONTAINERS_DISTANCE(-7),
    WIDE_DISTANCE(-8),
    PRIORITY_SWITCH(-9),
    LABELS(-10),
    HUD(-11),
    FULL_RESET(-12),
    SOUND(-13),
    LAZY_MODE(-14);
    private final int id;

    ButtonType(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
