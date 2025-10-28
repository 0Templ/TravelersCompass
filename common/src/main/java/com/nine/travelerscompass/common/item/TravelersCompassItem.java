package com.nine.travelerscompass.common.item;

import com.nine.travelerscompass.client.ClientCache;
import com.nine.travelerscompass.client.utils.ClientTickable;
import com.nine.travelerscompass.common.container.CompassContainer;
import com.nine.travelerscompass.common.data.CompassProperties;
import com.nine.travelerscompass.common.search.SearchManager;
import com.nine.travelerscompass.common.search.location.ILocationObject;
import com.nine.travelerscompass.common.search.location.WithUUID;
import com.nine.travelerscompass.common.utils.FoundBlockPos;
import com.nine.travelerscompass.common.utils.HeightAttitude;
import com.nine.travelerscompass.common.utils.PriorityMode;
import com.nine.travelerscompass.common.utils.SearchState;
import com.nine.travelerscompass.config.TCConfig;
import com.nine.travelerscompass.config.filter.FilterManager;
import com.nine.travelerscompass.config.filter.FilterReason;
import com.nine.travelerscompass.network.packet.s2c.HudDataPacket;
import com.nine.travelerscompass.platform.Platform;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class TravelersCompassItem extends Item implements ClientTickable {
	
	public static final int STATE_EMPTY = 0;
	public static final int STATE_SEARCHING = 1;
	public static final int STATE_FOUND = 2;
	
	public TravelersCompassItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		CompassContainer compassContainer = CompassContainer.container(stack);
		Vec3 lookVector = player.getLookAngle();
		Vec3 eyePosition = player.getEyePosition(1.0F);
		Vec3 traceEnd = eyePosition.add(lookVector.x * 5.0D, lookVector.y * 5.0D, lookVector.z * 5.0D);
		BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, traceEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
		BlockPos blockPos = hitResult.getBlockPos();
		if (!level.isClientSide) {
			if (player.isShiftKeyDown()) {
				ItemStack clickedStack = level.getBlockState(blockPos).getBlock().asItem().getDefaultInstance();
				if (!compassContainer.hasAny(clickedStack)) {
					compassContainer.setItem(compassContainer.getFirstEmptySlot(), clickedStack, player);
					if (FilterManager.passesFilters(clickedStack)) {
						level.playSound(null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
						return InteractionResult.SUCCESS;
					}
				}
			}
			UUID uuid = CompassProperties.COMPASS_UUID.get(stack);
			SearchManager.addWatcher(uuid, player.getUUID());
			Platform.PLATFORM.openMenu(player, stack);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity living, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		ItemStack stack = player.getItemInHand(hand);
		if (!player.level().isClientSide && player.isShiftKeyDown() && living instanceof Mob mob) {
			SpawnEggItem eggItem = SpawnEggItem.byId(mob.getType());
			CompassContainer compassContainer = CompassContainer.container(stack);
			if (eggItem != null) {
				ItemStack eggStack = eggItem.getDefaultInstance();
				if (!compassContainer.hasAny(eggStack)) {
					FilterReason filterReason = FilterManager.getFilterReason(mob.getType());
					if (filterReason.isAllowed()) {
						compassContainer.setItem(compassContainer.getFirstEmptySlot(), eggStack, player);
						player.level().playSound(null, player.getOnPos(), SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
						return InteractionResult.SUCCESS;
					}
				}
			}
			
		}
		return InteractionResult.PASS;
	}
	
	
	@Override
	public void clientInventoryTick(ItemStack stack, ClientLevel level, Entity entity, EquipmentSlot slot) {
		if (entity instanceof Player player) {
			var pos = CompassProperties.FOUND_BLOCK_POS.get(stack);
			final UUID uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
			if (uuid.getMostSignificantBits() == 0L && uuid.getLeastSignificantBits() == 0L) {
				return;
			}
			boolean selected = slot == EquipmentSlot.MAINHAND;
			if (!ClientCache.HUD_DATA_CACHE.containsKey(uuid)) {
				ClientCache.updateAllHudSettings(stack, selected);
			} else {
				ClientCache.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
					hudData.setSelected(selected);
					return hudData;
				});
				if (player.tickCount % 20 == 0) {
					ClientCache.HUD_DATA_CACHE.computeIfPresent(uuid, (id, hudData) -> {
						hudData.setSearchState(CompassProperties.SEARCH_STATE.get(stack));
						return hudData;
					});
				}
			}
		}
	}
	
	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
		if (entity instanceof ServerPlayer player) {
			final UUID uuid = CompassProperties.get(stack, CompassProperties.COMPASS_UUID);
			syncCompassUUID(player, stack, uuid);
			final boolean searching = SearchManager.inQueue(uuid);
			if (!searching) {
				if (!CompassContainer.container(stack).isEmpty()) {
					tryToScan(stack, player, uuid);
				}
			}
			PriorityMode priorityMode = CompassProperties.PRIORITY_MODE.get(stack);
			ILocationObject locationObject = SearchManager.getClosestLocation(player.blockPosition(), uuid, priorityMode);
			boolean validLocation = locationObject != null;
			if (player.tickCount % 5 == 0) {
				CompassContainer container = CompassContainer.container(stack);
				if (player.tickCount % 20 == 0) {
					SearchState searchState = CompassProperties.SEARCH_STATE.get(stack);
					if (!searching && searchState != SearchState.IDLE) {
						CompassProperties.SEARCH_STATE.set(stack, SearchState.IDLE);
					}
					if (container.isEmpty()) {
						SearchManager.clearFoundBlocks(uuid);
					}
					if (CompassProperties.TARGET_VALIDATION.get(stack)) {
						SearchManager.validatePositions(level, uuid);
					}
					SearchManager.validatePriority(stack);
					
					//Updating hud renderer
					if (TCConfig.ENABLE_HUD.get()) {
						var clientLocationObject = locationObject;
						{
							if (clientLocationObject != null && !TCConfig.SHOW_COORDS.get()) {
								clientLocationObject = clientLocationObject.withBlockPos(null);
							}
							Platform.PLATFORM_NETWORK.sendToClient(player, new HudDataPacket(clientLocationObject, uuid));
						}
					}
				}
				FoundBlockPos foundBlockPos;
				if (!validLocation) {
					CompassProperties.PRIORITY_ITEM_FOUND.set(stack, false);
					foundBlockPos = new FoundBlockPos();
				} else {
					foundBlockPos = new FoundBlockPos(locationObject.blockPos(), true);
					CompassProperties.PRIORITY_ITEM_FOUND.set(stack, locationObject.priority());
				}
				updateState(stack, validLocation, container.isEmpty());
				String targetID = validLocation ? locationObject.descriptionId() : "";
				UUID targetUUID = validLocation && locationObject instanceof WithUUID withUUID ? withUUID.uuid() : Util.NIL_UUID;
				
				//Sound ping
				{
					if (validLocation && !CompassProperties.FOUND_BLOCK_POS.get(stack).equals(foundBlockPos) && CompassProperties.SOUND_PING.get(stack)) {
						boolean shouldPing = false;
						if (!targetUUID.equals(CompassProperties.TARGET_UUID.get(stack))) {
							shouldPing = true;
						}
						if (!targetID.isEmpty() && !targetID.equals(CompassProperties.TARGET_ID.get(stack))) {
							shouldPing = true;
						}
						if (shouldPing) {
							playPingSound(locationObject, player);
						}
					}
				}
				CompassProperties.TARGET_ID.set(stack, targetID);
				CompassProperties.TARGET_UUID.set(stack, targetUUID);
				CompassProperties.FOUND_BLOCK_POS.set(stack, foundBlockPos);
			}
			updateTargetAttitude(stack, player.blockPosition(), locationObject);
		}
	}
	
	private void playPingSound(ILocationObject locationObject, Player player) {
		Level level = player.level();
		if (locationObject.priority()) {
			level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.3F, 0.3F);
		}
		level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, 0.44F + level.random.nextFloat() / 10);
		level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, 1F);
	}
	
	private void updateState(ItemStack stack, boolean validLocation, boolean empty) {
		int currentState = CompassProperties.COMPASS_STATE.get(stack);
		int newState = STATE_EMPTY;
		if (validLocation) {
			newState = STATE_FOUND;
		} else if (CompassProperties.SEARCH_STATE.get(stack) != SearchState.IDLE) {
			newState = STATE_SEARCHING;
		} else if (!empty) {
			newState = STATE_SEARCHING;
		}
		if (currentState != newState) {
			CompassProperties.COMPASS_STATE.set(stack, newState);
		}
	}
	
	private void updateTargetAttitude(ItemStack stack, BlockPos playerPos, ILocationObject locationData) {
		HeightAttitude heightAttitude;
		HeightAttitude current = CompassProperties.TARGET_HEIGHT.get(stack);
		if (locationData == null) {
			heightAttitude = HeightAttitude.NONE;
		} else {
			BlockPos targetPos = locationData.blockPos();
			int pY = playerPos.getY();
			int tY = targetPos.getY();
			if (pY - tY > 0) {
				heightAttitude = HeightAttitude.DOWN;
			} else if (pY - tY <= -2) {
				heightAttitude = HeightAttitude.UP;
			} else {
				heightAttitude = HeightAttitude.SAME;
			}
		}
		if (current != heightAttitude) {
			CompassProperties.TARGET_HEIGHT.set(stack, heightAttitude);
		}
	}
	
	private void tryToScan(ItemStack stack, ServerPlayer player, UUID uuid) {
		if (!CompassProperties.get(stack, CompassProperties.PAUSE)) {
			int cooldown = CompassProperties.SEARCH_COOLDOWN.get(stack);
			if (cooldown > 0) {
				CompassProperties.SEARCH_COOLDOWN.set(stack, cooldown - 1);
			}
			if (cooldown <= 0) {
				CompassContainer container = CompassContainer.container(stack);
				if (container.isEmpty()) {
					CompassProperties.SEARCH_COOLDOWN.set(stack, 20);
					return;
				}
				CompassProperties.SEARCH_COOLDOWN.set(stack, TCConfig.SEARCH_INTERVAL.get());
				CompassProperties.SEARCH_STATE.set(stack, SearchState.SEARCHING);
				SearchManager.startSearch(stack, player, container, false, (result) -> {
					CompassProperties.SEARCH_STATE.set(stack, SearchState.IDLE);
					PriorityMode priorityMode = CompassProperties.PRIORITY_MODE.get(stack);
					SearchManager.validatePriority(stack);
					SearchManager.saveClosest(result.get(), player.blockPosition(), uuid, TCConfig.MAX_CACHED_LOCATIONS.get(), priorityMode);
					if (CompassProperties.TARGET_VALIDATION.get(stack)) {
						SearchManager.validatePositions(player.level(), uuid);
					}
				});
			}
		}
	}
	
	private void syncCompassUUID(Player player, ItemStack stack, UUID uuid) {
		if (uuid.getMostSignificantBits() == 0L && uuid.getLeastSignificantBits() == 0L) {
			CompassProperties.COMPASS_UUID.set(stack, UUID.randomUUID());
			player.containerMenu.broadcastChanges();
		}
	}
	
}
