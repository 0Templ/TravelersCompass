package com.nine.travelerscompass.config;

import com.nine.travelerscompass.TCCommon;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigSyncManager {

    private static final byte BOOLEAN = 0;
    private static final byte INTEGER = 1;
    private static final byte DOUBLE = 2;
    private static final byte STRING = 3;
    private static final byte ENUM = 4;
    private static final byte LIST = 5;
    private static final byte FLOAT = 6;

    private static final Map<String, ConfigValue<?>> ALL_CONFIGS = new HashMap<>();
    private static final Map<String, ConfigValue<?>> SYNC_CONFIGS = new HashMap<>();

    public static final Map<String, Object> SYNCED_VALUES = new HashMap<>();

    public static void checkSyncable(ConfigValue<?> config){
        ALL_CONFIGS.put(config.path(), config);
        if (config.shouldSync()){
            SYNC_CONFIGS.put(config.path(), config);
        }
    }

    public static FriendlyByteBuf write(FriendlyByteBuf buf){
        buf.writeVarInt(SYNC_CONFIGS.size());
        for (Map.Entry<String, ConfigValue<?>> entry : SYNC_CONFIGS.entrySet()) {
            String path = entry.getKey();
            ConfigValue<?> config = entry.getValue();
            Object value = config.get();
            buf.writeUtf(path);
            buf.writeByte(getId(config.clazz()));
            writeTypedValue(buf, value, config.clazz());
        }
        return buf;
    }

    public static Map<String, Object> read(FriendlyByteBuf buf){
        int size = buf.readVarInt();
        Map<String, Object> ret = new HashMap<>();
        for (int i = 0; i < size; i++){
            String path = buf.readUtf();
            byte type = buf.readByte();
            ConfigValue<?> config = ALL_CONFIGS.get(path);
            if (config != null){
                Object value = readTypedValue(buf, config.clazz(), type);
                if (value != null){
                    ret.put(path, value);
                }
            }
            else {
                TCCommon.LOGGER.warn("Could not sync config value: {}", path);
            }
        }
        return ret;
    }

    private static <T> void writeTypedValue(FriendlyByteBuf buf, Object value, Class<T> clazz) {
        if (clazz == Boolean.class) {
            buf.writeBoolean((Boolean) value);
        } else if (clazz == Integer.class) {
            buf.writeInt((Integer) value);
        } else if (clazz == Double.class) {
            buf.writeDouble((Double) value);
        } else if (clazz == String.class) {
            buf.writeUtf((String) value);
        } else if (clazz == Float.class) {
            buf.writeFloat((Float) value);
        } else if (Enum.class.isAssignableFrom(clazz)) {
            buf.writeEnum((Enum<?>) value);
        } else if (List.class.isAssignableFrom(clazz)) {
            List<?> list = (List<?>) value;
            buf.writeVarInt(list.size());
            if (list.isEmpty()) return;
            Class<?> contentClass = list.get(0).getClass();
            buf.writeByte(getId(contentClass));
            if (Enum.class.isAssignableFrom(contentClass)) {
                buf.writeUtf(contentClass.getName());
            }
            for (Object el : list) {
                writeTypedValue(buf, el, contentClass);
            }
        } else {
            throw new IllegalArgumentException("Unsupported config type: " + clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private static Object readTypedValue(FriendlyByteBuf buf, Class<?> clazz, byte type) {
        return switch (type) {
            case BOOLEAN -> buf.readBoolean();
            case INTEGER -> buf.readInt();
            case DOUBLE -> buf.readDouble();
            case STRING -> buf.readUtf();
            case FLOAT -> buf.readFloat();
            case ENUM -> buf.readEnum((Class<? extends Enum>) clazz);
            case LIST -> {
                int size = buf.readVarInt();
                List<Object> ret = new ArrayList<>();
                if (size > 0) {
                    byte contentType = buf.readByte();
                    Class<?> contentClass = switch (contentType) {
                        case BOOLEAN -> Boolean.class;
                        case INTEGER -> Integer.class;
                        case DOUBLE -> Double.class;
                        case STRING -> String.class;
                        case FLOAT -> Float.class;
                        case ENUM -> {
                            String classPath = buf.readUtf();
                            try {
                                yield Class.forName(classPath);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException("Unknown enum class: " + classPath);
                            }
                        }
                        default -> throw new IllegalArgumentException("Unknown element type in list: " + contentType);
                    };
                    for (int i = 0; i < size; i++) {
                        var val = readTypedValue(buf, contentClass, contentType);
                        ret.add(val);
                    }
                }
                yield ret;
            }
            default -> throw new IllegalArgumentException("Unknown type ID: " + type);
        };
    }

    private static byte getId(Class<?> clazz) {
        if (clazz == Boolean.class) return BOOLEAN;
        if (clazz == Double.class) return DOUBLE;
        if (clazz == Integer.class) return INTEGER;
        if (clazz == String.class) return STRING;
        if (clazz == Float.class) return FLOAT;
        if (Enum.class.isAssignableFrom(clazz)) return ENUM;
        if (List.class.isAssignableFrom(clazz)) return LIST;
        throw new RuntimeException("Unsupported config type: " + clazz);
    }

}
