package com.nine.travelerscompass.common.item;

public enum CompassData {

    SEARCHING_MOBS(0),
    SEARCHING_CONTAINERS(1),
    SEARCHING_BLOCKS(2),
    SEARCHING_FLUIDS(3),
    SEARCHING_VILLAGERS(4),
    SEARCHING_SPAWNERS(5),
    SEARCHING_ENTITIES_INV(6),
    SEARCHING_DROPPED_ITEMS(7),
    SEARCHING_ENTITIES_DROP(8),
    PAUSED(-1),
    WIDE_SEARCH(-2),
    WIDE_SEARCH_SIGNAL(-20),
    PRIORITY_MODE(-3),
    SHOW_LABELS(-4),
    STYLE_DATA_DARK(-101),
    STYLE_DATA_LIGHT(-102),
    SEARCHING_VILLAGERS_GOODS(401),
    SEARCHING_VILLAGERS_COST(402),
    SEARCHING_MOBS_INV(601),
    SEARCHING_PLAYERS_INV(602),
    SEARCHING_MINECARTS_INV(603),
    HUD_SHOW(-201),
    HUD_SHOW_HAND(-202),
    SOUND(-301),
    LAZY_MODE(-402);
    private final int id;

    CompassData(int id) {
        this.id = id;
    }


    public int getID() {
        return id;
    }
}