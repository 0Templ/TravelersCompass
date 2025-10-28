package com.nine.travelerscompass.platform;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nine.travelerscompass.config.ConfigSyncManager;
import com.nine.travelerscompass.config.ConfigType;
import com.nine.travelerscompass.config.ConfigValue;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class FabricPlatformConfigHelper extends IPlatformConfigHelper {
	
	public static final CommentedFileConfig COMMON = CommentedFileConfig.builder(getConfigPath("travelerscompass-common"))
			.autosave()
			.sync()
			.preserveInsertionOrder()
			.build();
	
	public static final CommentedFileConfig COST = CommentedFileConfig.builder(getConfigPath("travelerscompass-cost"))
			.autosave()
			.sync()
			.preserveInsertionOrder()
			.build();
	
	@Override
	public ConfigValue<Integer> registerInt(String key, int defaultValue, int min, int max, ConfigValue.Section section, ConfigType type, boolean shouldSync, String comment) {
		return register(key, defaultValue, section, type, Integer.class, shouldSync, comment);
	}
	
	@Override
	public <T> ConfigValue<T> register(String key, T defaultValue, ConfigValue.Section section, ConfigType type, Class<T> clazz, boolean shouldSync, String comment) {
		CommentedFileConfig config = getConfig(type);
		FabricConfigValue<T> value = new FabricConfigValue<>(config, key, defaultValue, section, type, clazz, shouldSync);
		if (!config.contains(value.path())) {
			config.set(value.path(), value.defaultValue());
		}
		if (defaultValue.getClass().isEnum()) {
			Class<?> enumClass = defaultValue.getClass();
			Object[] values = enumClass.getEnumConstants();
			comment += "\nAvailable values: ";
			String[] names = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				names[i] = ((Enum<?>) values[i]).name();
			}
			comment += String.join(", ", names);
		}
		config.setComment(value.path(), comment);
		ConfigSyncManager.checkSyncable(value);
		return value;
	}
	
	public static class FabricConfigValue<T> extends ConfigValue<T> {
		
		private final UnmodifiableConfig config;
		
		public FabricConfigValue(UnmodifiableConfig config, String key, T defaultValue, Section section, ConfigType type, Class<T> clazz, boolean shouldSync) {
			super(key, defaultValue, section, type, clazz, shouldSync);
			this.config = config;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public T get() {
			if (ConfigSyncManager.SYNCED_VALUES.containsKey(path())) {
				return (T) ConfigSyncManager.SYNCED_VALUES.get(path());
			}
			Object value = config.get(path());
			if (clazz != null) {
				if (clazz.isEnum() && value instanceof String str) {
					return (T) Enum.valueOf((Class<Enum>) clazz, str);
				}
			}
			return (T) value;
		}
		
	}
	
	private CommentedFileConfig getConfig(ConfigType type) {
		return switch (type) {
			case COST -> COST;
			case COMMON -> COMMON;
		};
	}
	
	@Override
	public <T> T get(ConfigValue<T> value) {
		return ((FabricConfigValue<T>) value).config.get(value.path());
	}
	
	private static File getConfigPath(String id) {
		return FabricLoader.getInstance()
				.getConfigDir()
				.resolve(id + ".toml").toFile();
	}
	
}
