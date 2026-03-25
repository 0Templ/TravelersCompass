package com.nine.travelerscompass.config;

public enum LoaderTarget {

    COMMON("common"),
    FABRIC("fabric"),
    NEOFORGE("neoforge");

    public final String id;

    private static final LoaderTarget CURRENT = detectCurrent();

    LoaderTarget(String id) {
        this.id = id;
    }

    public static LoaderTarget current() {
        return CURRENT;
    }

    public boolean matchesCurrent() {
        return this == COMMON || this == CURRENT;
    }

    private static LoaderTarget detectCurrent() {
        if (classExists("net.neoforged.fml.loading.FMLEnvironment")) {
            return NEOFORGE;
        }
        if (classExists("net.fabricmc.loader.api.FabricLoader")) {
            return FABRIC;
        }
        return COMMON;
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className, false, LoaderTarget.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
    }
}
