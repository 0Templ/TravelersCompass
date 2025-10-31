package com.nine.travelerscompass.common.search;

import com.nine.travelerscompass.client.utils.SearchProgress;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassComponents;
import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.BlockMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;
import com.nine.travelerscompass.common.search.matcher.PositionMatchers;
import com.nine.travelerscompass.common.utils.PriorityMode;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.network.packet.s2c.SearchProgressSyncPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SearchManager {
	
	public static boolean scannedSmth = false;
	
	private static final int CHUNKS_PER_TICK = TCConfig.MAX_CHUNK_SCANS_PER_TICK_ON_SERVER.get();
	private static final int CHUNKS_PER_COMPASS = TCConfig.MAX_CHUNK_SCANS_PER_COMPASS.get();
	private static final int GENERATIONS_PER_TICK = TCConfig.MAX_FORCE_CHUNK_GENERATION_PER_TICK.get();
	
	public static final ExecutorService SCAN_EXECUTOR =
			Executors.newFixedThreadPool(2, r -> {
				Thread t = new Thread(r, "Compass-scan-executor");
				t.setDaemon(true);
				return t;
			});
	
	//Stack - Player
	
	private static final Map<UUID, SearchProcess> ACTIVE_SCAN_PROCESSES = new HashMap<>();
	private static final Queue<UUID> PROCESS_QUEUE = new ArrayDeque<>();
	
	public static final Map<UUID, CachedLocations> FOUND_DATA_CACHE = new ConcurrentHashMap<>();
	public static final Map<UUID, UUID> PROGRESS_WATCHERS = new HashMap<>();
	
	public static void addWatcher(UUID compass, UUID player) {
		PROGRESS_WATCHERS.putIfAbsent(compass, player);
	}
	
	public static void removeWatcher(UUID compass, ServerPlayer player) {
		PROGRESS_WATCHERS.remove(compass, player.getUUID());
		//Sending a packet in pursuit of what can be sent from tick() to avoid progress jumps on the client
		Platform.PLATFORM_NETWORK.sendToClient(player, new SearchProgressSyncPacket(new SearchProgress(-1, -1), compass));
	}
	
	public static boolean inQueue(UUID uuid) {
		return ACTIVE_SCAN_PROCESSES.containsKey(uuid);
	}
	
	private static void notifyWatcher(Level level, SearchProgress progress, UUID uuid) {
		if (level instanceof ServerLevel serverLevel) {
			if (PROGRESS_WATCHERS.containsKey(uuid)) {
				UUID playerUuid = PROGRESS_WATCHERS.get(uuid);
				var server = serverLevel.getServer();
				Player player = server.getPlayerList().getPlayer(playerUuid);
				if (player instanceof ServerPlayer serverPlayer) {
					Platform.PLATFORM_NETWORK.sendToClient(serverPlayer, new SearchProgressSyncPacket(progress, uuid));
				}
			}
		}
	}
	
	public static void tick() {
		List<UUID> toRemove = new ArrayList<>();
		int totalChunksScanned = 0;
		int totalChunksGenerated = 0;
		int availableChunks;
		int generationLimit;
		int qSize = PROCESS_QUEUE.size();
		scannedSmth = qSize > 0;
		for (int i = 0; i < qSize; i++) {
			UUID uuid = PROCESS_QUEUE.poll();
			var job = ACTIVE_SCAN_PROCESSES.get(uuid);
			if (job == null) continue;
			if (job.shouldSyncProgress()) {
				int progress = (job.wideSearch && job.allowChunkGen) ? job.chunksLoaded : job.chunksPassed;
				notifyWatcher(job.level, new SearchProgress(progress, job.chunksToScan - 1), uuid);
			}
			availableChunks = calculateChunksBudget(totalChunksScanned);
			generationLimit = calculateGenerationBudget(totalChunksGenerated);
			if (job.tickAsync(availableChunks, generationLimit)) {
				if (job.onComplete != null) {
					job.scanEntities();
					job.onComplete.accept(job.result);
					notifyWatcher(job.level, new SearchProgress(0, job.chunksToScan - 1), uuid);
				}
				
				toRemove.add(uuid);
			} else {
				PROCESS_QUEUE.add(uuid);
			}
			totalChunksScanned += job.chunksPassedLastTick;
			totalChunksGenerated += job.chunksForceLoadedLastTick;
		}
		for (UUID uuid : toRemove) {
			ACTIVE_SCAN_PROCESSES.remove(uuid);
		}
	}
	
	private static int calculateChunksBudget(int scanned) {
		if (scanned >= CHUNKS_PER_TICK) {
			return 0;
		}
		if ((CHUNKS_PER_COMPASS + scanned) > CHUNKS_PER_TICK) {
			return CHUNKS_PER_TICK - scanned;
		}
		return CHUNKS_PER_COMPASS;
	}
	
	private static int calculateGenerationBudget(int generated) {
		if (generated >= GENERATIONS_PER_TICK) {
			return 0;
		}
		return GENERATIONS_PER_TICK - generated;
	}
	
	
	public static void startWideSearch(ItemStack stack, ServerPlayer player, CompassContainer container) {
		UUID uuid = CompassComponents.COMPASS_UUID.get(stack);
		SearchManager.stopScan(uuid);
		CompassComponents.PAUSE.set(stack, true);
		CompassComponents.SEARCH_STATE.set(stack, SearchState.WIDE_SEARCHING);
		startSearch(stack, player, container, true, (result) -> {
			validatePriority(stack);
			saveClosest(result.get(), player.blockPosition(), uuid, TCConfig.MAX_CACHED_LOCATIONS.get(), CompassComponents.PRIORITY_MODE.get(stack));
			CompassComponents.SEARCH_STATE.set(stack, SearchState.IDLE);
			if (CompassComponents.TARGET_VALIDATION.get(stack)) {
				validatePositions(player.level(), uuid);
			}
		});
	}
	
	public static void startSearch(ItemStack stack, ServerPlayer player, CompassContainer container, boolean wideSearch, Consumer<SearchResult> onComplete) {
		UUID uuid = CompassComponents.get(stack, CompassComponents.COMPASS_UUID);
		Level level = player.level();
		if (ACTIVE_SCAN_PROCESSES.containsKey(uuid)) {
			return;
		}
		if (!PROGRESS_WATCHERS.containsKey(uuid)) {
			PROGRESS_WATCHERS.put(uuid, player.getUUID());
		}
		var options = new SearchOptions(stack, player, wideSearch);
		TypedCriteria criteria = new TypedCriteria(TypedCriteria.extractCriteria(container, stack, level));
		
		List<BlockMatcher> blocksMatcher = PositionMatchers.BLOCK_MATCHERS.stream()
				.filter(m -> m.isAllowed(options))
				.toList();
		List<BlockEntityMatcher> blockEntitiesMatcher = PositionMatchers.BLOCK_ENTITY_MATCHERS.stream()
				.filter(m -> m.isAllowed(options))
				.toList();
		List<EntityMatcher> entitiesMatcher = PositionMatchers.ENTITY_MATCHERS.stream()
				.filter(m -> m.isAllowed(options))
				.toList();
		
		boolean allowChunkGen = CompassComponents.FORCE_CHUNKS_LOAD.get(stack) &&
				TCConfig.MAX_FORCE_CHUNK_GENERATION_PER_TICK.get() > 0;
		
		PROCESS_QUEUE.add(uuid);
		ACTIVE_SCAN_PROCESSES.put(uuid,
				new SearchProcess(
						player,
						allowChunkGen,
						options,
						criteria,
						blocksMatcher,
						blockEntitiesMatcher,
						entitiesMatcher,
						onComplete
				)
		);
	}
	
	public static void clearFoundBlocks(UUID uuid) {
		FOUND_DATA_CACHE.remove(uuid);
	}
	
	public static class CachedLocations {
		
		private static final int MIN_RESORT_DIST = 5;
		private static final int FUll_RESORT_TICKS = 100;
		private static final int QUICK_RESORT_TICKS = 20;
		private static final int QUICK_RESORT_AMOUNT = 5;
		
		public List<ILocationObject> locations;
		private int ticksSinceSort = 0;
		private BlockPos closestPos = BlockPos.ZERO;
		
		CachedLocations(List<ILocationObject> locations) {
			this.locations = locations;
		}
		
		public ILocationObject getClosest(BlockPos playerPos, PriorityMode priorityMode) {
			ticksSinceSort++;
			boolean fullResort = false;
			boolean quickResort = false;
			int size = locations.size();
			if (size > 0) {
				if (playerPos.distSqr(closestPos) > (MIN_RESORT_DIST * MIN_RESORT_DIST)) {
					fullResort = true;
				} else if (ticksSinceSort >= FUll_RESORT_TICKS) {
					fullResort = true;
					ticksSinceSort = 0;
				} else if (ticksSinceSort % QUICK_RESORT_TICKS == 0) {
					quickResort = true;
				}
				if (fullResort) {
					sort(playerPos, priorityMode, size);
				} else if (quickResort) {
					sort(playerPos, priorityMode, QUICK_RESORT_AMOUNT);
				}
			}
			return size == 0 ? null : locations.getFirst();
		}
		
		private void sort(BlockPos playerPos, PriorityMode priorityMode, int limit) {
			int size = Math.min(locations.size(), limit);
			locations.subList(0, size).sort(getComparator(playerPos, priorityMode));
			this.closestPos = locations.getFirst().blockPos();
		}
		
		private Comparator<ILocationObject> getComparator(BlockPos playerPos, PriorityMode priorityMode) {
			return (a, b) -> {
				double distA = a.blockPos().distSqr(playerPos);
				double distB = b.blockPos().distSqr(playerPos);
				boolean pa = a.priority();
				boolean pb = b.priority();
				
				switch (priorityMode) {
					case NORMAL -> {
						if (pa != pb) {
							return pa ? -1 : 1;
						}
					}
					case OFF -> {
						return Double.compare(distA, distB);
					}
					case INVERTED -> {
						if (pa != pb) {
							return pa ? 1 : -1;
						}
					}
				}
				return Double.compare(distA, distB);
			};
		}
		
	}
	
	public static ILocationObject getClosestLocation(BlockPos playerPos, UUID uuid, PriorityMode priorityMode) {
		if (FOUND_DATA_CACHE.containsKey(uuid)) {
			return FOUND_DATA_CACHE.get(uuid).getClosest(playerPos, priorityMode);
		}
		return null;
	}
	
	public static void stopScan(UUID uuid) {
		if (ACTIVE_SCAN_PROCESSES.containsKey(uuid)) {
			ACTIVE_SCAN_PROCESSES.get(uuid).setCanceled();
		}
		ACTIVE_SCAN_PROCESSES.remove(uuid);
		PROCESS_QUEUE.remove(uuid);
	}
	
	public static void saveClosest(List<ILocationObject> allData, BlockPos playerPos, UUID uuid, int limit, PriorityMode priorityMode) {
		Comparator<ILocationObject> byDistance = Comparator.comparingDouble(p -> p.blockPos().distSqr(playerPos));
		List<ILocationObject> all = new ArrayList<>(allData);
		if (priorityMode.equals(PriorityMode.OFF)) {
			all.sort(byDistance);
			FOUND_DATA_CACHE.put(uuid, new CachedLocations(all.subList(0, Math.min(all.size(), limit))));
			return;
		}
		List<ILocationObject> priority = new ArrayList<>();
		List<ILocationObject> nonPriority = new ArrayList<>();
		for (ILocationObject loc : all) {
			if (loc.priority()) {
				priority.add(loc);
			} else {
				nonPriority.add(loc);
			}
		}
		priority.sort(byDistance);
		nonPriority.sort(byDistance);
		List<ILocationObject> result = new ArrayList<>();
		if (priorityMode == PriorityMode.NORMAL) {
			result.addAll(priority.subList(0, Math.min(priority.size(), limit)));
			int remaining = limit - result.size();
			result.addAll(nonPriority.subList(0, Math.min(nonPriority.size(), remaining)));
		} else if (priorityMode == PriorityMode.INVERTED) {
			result.addAll(nonPriority.subList(0, Math.min(nonPriority.size(), limit)));
			int remaining = limit - result.size();
			result.addAll(priority.subList(0, Math.min(priority.size(), remaining)));
		}
		FOUND_DATA_CACHE.put(uuid, new CachedLocations(result));
	}
	
	public static void validatePositions(Level level, UUID uuid) {
		if (!FOUND_DATA_CACHE.containsKey(uuid)) {
			return;
		}
		List<ILocationObject> dataList = FOUND_DATA_CACHE.get(uuid).locations;
		if (dataList == null) {
			return;
		}
		if (level instanceof ServerLevel serverLevel) {
			dataList.removeIf((s -> !s.isValid(serverLevel)));
			dataList.replaceAll((s -> s.update(serverLevel).orElse(s)));
		}
		
	}
	
	public static void validatePriority(ItemStack stack) {
		UUID uuid = CompassComponents.COMPASS_UUID.get(stack);
		if (!FOUND_DATA_CACHE.containsKey(uuid)) {
			return;
		}
		List<ILocationObject> dataList = SearchManager.FOUND_DATA_CACHE.get(uuid).locations;
		if (dataList == null) {
			return;
		}
		Set<Integer> set = Set.copyOf(CompassComponents.PRIORITY_SLOTS.get(stack));
		for (ILocationObject object : dataList) {
			object.withPriority(set.contains(object.slotIndex()));
		}
	}
	
	public static void updateSlotPriority(UUID uuid, int slotIndex, boolean priority) {
		if (!FOUND_DATA_CACHE.containsKey(uuid)) {
			return;
		}
		List<ILocationObject> locations = FOUND_DATA_CACHE.get(uuid).locations;
		for (int i = 0; i < locations.size(); i++) {
			ILocationObject data = locations.get(i);
			if (data.slotIndex() == slotIndex) {
				locations.set(i, data.withPriority(priority));
			}
		}
	}
	
}
