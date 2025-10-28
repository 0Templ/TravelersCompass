package com.nine.travelerscompass.common.search;

import com.nine.travelerscompass.common.search.criterion.TypedCriteria;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.matcher.BlockEntityMatcher;
import com.nine.travelerscompass.common.search.matcher.BlockMatcher;
import com.nine.travelerscompass.common.search.matcher.EntityMatcher;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SearchProcess {
	
	private volatile boolean isAsyncRunning;
	private final AtomicBoolean canceled = new AtomicBoolean(false);
	
	final List<BlockMatcher> blockMatchers;
	final List<BlockEntityMatcher> blockEntityMatchers;
	final List<EntityMatcher> entityMatchers;
	final SearchOptions options;
	
	final TypedCriteria criteria;
	public final boolean allowChunkGen;
	
	public final boolean wideSearch;
	
	public final Level level;
	final BlockPos playerPos;
	final Queue<ChunkPos> deferredChunks = new ArrayDeque<>();
	
	private final ChunkIterator chunkIterator;
	
	int lastSyncedProgress;
	public final int chunksToScan;
	public int chunksPassed = 0;
	public int chunksPassedLastTick = 0;
	public int chunksLoaded = 0;
	int chunksLoadedLastTick = 0;
	
	public int chunksForceLoaded = 0;
	public int chunksForceLoadedLastTick = 0;
	
	final int entitiesSearchRange;
	
	public final SearchResult result = new SearchResult();
	public final Consumer<SearchResult> onComplete;
	
	public SearchProcess(Player player,
						 boolean allowChunkGen,
						 SearchOptions options,
						 TypedCriteria criteria,
						 List<BlockMatcher> bMatchers,
						 List<BlockEntityMatcher> beMatchers,
						 List<EntityMatcher> eMatchers,
						 Consumer<SearchResult> onComplete) {
		this.allowChunkGen = allowChunkGen;
		this.wideSearch = options.wideSearch();
		this.level = player.level();
		this.criteria = criteria;
		this.options = options;
		this.entitiesSearchRange = options.getEntitiesSearchRange();
		this.playerPos = player.blockPosition();
		this.blockMatchers = bMatchers;
		this.blockEntityMatchers = beMatchers;
		this.entityMatchers = eMatchers;
		this.onComplete = onComplete;
		this.chunksToScan = (int) Math.pow((1 + (options.getChunksSearchRange() * 2)), 2);
		
		chunkIterator = new ChunkIterator(playerPos, options.getChunksSearchRange());
	}
	
	public void scanEntities() {
		AABB aabb = new AABB(Vec3.atLowerCornerOf(playerPos.offset(-entitiesSearchRange, -entitiesSearchRange, -entitiesSearchRange)),
				Vec3.atLowerCornerOf(playerPos.offset(entitiesSearchRange, entitiesSearchRange, entitiesSearchRange)));
		List<Entity> list = level.getEntitiesOfClass(Entity.class, aabb);
		if (entityMatchers.isEmpty()) {
			return;
		}
		for (Entity entity : list) {
			FilterReason filterReason = FilterManager.getFilterReason(entity.getType());
			if (filterReason.isAllowed()) {
				for (EntityMatcher matcher : entityMatchers) {
					List<ILocationObject> data = matcher.match(criteria, options, entity, level, entity.blockPosition());
					if (data != null) {
						result.addAll(data);
					}
				}
			} else {
			
			}
		}
	}
	
	
	//Method with moving heavy operations for servers out of the main thread
	//Lightening the server's workload and lower latency in the main thread
	public boolean tickAsync(int chunksLimit, int generationLimit) {
		if (isAsyncRunning || chunksLimit == 0) {
			return false;
		}
		MinecraftServer server = level.getServer();
		if (server == null || canceled.get()) {
			return true;
		}
		chunksPassedLastTick = 0;
		chunksForceLoadedLastTick = 0;
		chunksLoadedLastTick = 0;
		List<ChunkAccess> chunkList = new ArrayList<>();
		while (chunkIterator.hasNext()) {
			chunksPassed++;
			chunksPassedLastTick++;
			ChunkPos pos = chunkIterator.next();
			boolean chunkLoaded = level.hasChunk(pos.x, pos.z);
			boolean force = allowChunkGen && !chunkLoaded && generationLimit > 0 && chunksForceLoadedLastTick <= generationLimit;
			if (force || chunkLoaded) {
				ChunkAccess chunkAccess = level.getChunk(pos.x, pos.z, ChunkStatus.FULL, force);
				chunksLoaded++;
				chunksLoadedLastTick++;
				if (force) {
					chunksForceLoaded++;
					chunksForceLoadedLastTick++;
				}
				if (chunkAccess != null) {
					chunkList.add(chunkAccess);
				}
			} else if (allowChunkGen && wideSearch) {
				deferredChunks.add(pos);
			}
			
			if (chunksPassedLastTick >= chunksLimit) {
				break;
			}
		}
		if (wideSearch && allowChunkGen && chunksLoadedLastTick < chunksLimit) {
			int lim = chunksLimit - chunkList.size();
			while (lim > 0 && !deferredChunks.isEmpty()) {
				ChunkPos pos = deferredChunks.peek();
				boolean loaded = level.hasChunk(pos.x, pos.z);
				boolean force = generationLimit > 0 && chunksForceLoadedLastTick < generationLimit;
				if (!loaded && force) {
					break;
				}
				ChunkAccess currentChunk = level.getChunk(pos.x, pos.z, ChunkStatus.FULL, !loaded);
				if (currentChunk == null) {
					break;
				}
				deferredChunks.poll();
				chunkList.add(currentChunk);
				lim--;
				if (!loaded) {
					chunksForceLoaded++;
					chunksForceLoadedLastTick++;
				}
				chunksLoaded++;
				chunksLoadedLastTick++;
			}
		}
		
		List<BlockPos> blockEntityPositions = new ArrayList<>();
		isAsyncRunning = true;
		CompletableFuture.supplyAsync(() -> {
			List<ILocationObject> localDataList = new ArrayList<>();
			for (ChunkAccess chunkAccess : chunkList) {
				LevelChunkSection[] sections = chunkAccess.getSections();
				int minSectionY = chunkAccess.getMinSectionY();
				int baseX = chunkAccess.getPos().x << 4;
				int baseZ = chunkAccess.getPos().z << 4;
				
				blockEntityPositions.addAll(chunkAccess.getBlockEntitiesPos());
				
				for (int s = 0; s < sections.length; s++) {
					LevelChunkSection section = sections[s];
					if (section == null || section.hasOnlyAir()) continue;
					PalettedContainer<BlockState> palette = section.getStates();
					boolean shouldScanBlocks = palette.maybeHas(state
							-> criteria.placeableBlocks.contains(state.getBlock()));
					if (shouldScanBlocks) {
						int baseY = (minSectionY + s) << 4;
						BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
						for (int x = 0; x < 16; x++) {
							for (int z = 0; z < 16; z++) {
								for (int y = 0; y < 16; y++) {
									BlockState state = section.getBlockState(x, y, z);
									if (state.isAir()) continue;
									for (BlockMatcher matcher : blockMatchers) {
										mutablePos.set(baseX + x, baseY + y, baseZ + z);
										ILocationObject data = matcher.match(criteria, mutablePos.immutable(), state);
										if (data != null) {
											localDataList.add(data);
										}
									}
								}
							}
						}
					}
				}
			}
			return localDataList;
		}).thenAcceptAsync(threadResult -> {
			server.execute(() -> {
				if (this.canceled.get()) {
					isAsyncRunning = false;
					return;
				}
				try {
					//Block entities check
					//Can't perform most of the BlockEntity related operations outside the main thread.
					//Todo: find thread safe BlockEntity matchers?
					for (BlockPos pos : blockEntityPositions) {
						BlockState state = level.getBlockState(pos);
						final BlockEntity be = level.getBlockEntity(pos);
						if (be != null) {
							for (BlockEntityMatcher matcher : blockEntityMatchers) {
								List<ILocationObject> data = matcher.match(criteria, options, pos, state, be);
								if (data != null) {
									result.addAll(data);
								}
							}
						}
					}
					result.addAll(threadResult);
				} finally {
					isAsyncRunning = false;
				}
			});
		}, SearchManager.SCAN_EXECUTOR);
		
		return ((chunksPassed >= chunksToScan) && !canScanAboveLimit()) || this.canceled.get();
	}
	
	private boolean canScanAboveLimit() {
		return this.wideSearch && this.allowChunkGen && !deferredChunks.isEmpty();
	}
	
	public void setCanceled() {
		this.canceled.set(true);
	}
	
	public boolean shouldSyncProgress() {
		int current = (chunksPassed * 100) / chunksToScan;
		if (current != lastSyncedProgress || chunksToScan <= chunksPassed) {
			lastSyncedProgress = current;
			return true;
		}
		return false;
	}
	
}