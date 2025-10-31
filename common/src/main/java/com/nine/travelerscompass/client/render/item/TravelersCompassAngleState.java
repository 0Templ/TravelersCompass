package com.nine.travelerscompass.client.render.item;

import com.nine.travelerscompass.common.data.CompassComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.NeedleDirectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TravelersCompassAngleState extends NeedleDirectionHelper {
	
	private final RandomSource random = RandomSource.create();
	private final NeedleDirectionHelper.Wobbler noTargetWobbler = this.newWobbler(0.8F);
	private final NeedleDirectionHelper.Wobbler wobbler = this.newWobbler(0.8F);
	
	public TravelersCompassAngleState() {
		super(true);
	}
	
	@Override
	protected float calculate(ItemStack stack, ClientLevel level, int seed, ItemOwner owner) {
		BlockPos pos = CompassComponents.FOUND_BLOCK_POS.get(stack).blockPos();
		long time = level.getGameTime();
		
		return !isValidCompassTargetPos(pos)
				? getRandomlySpinningRotation(seed, time)
				: getRotationTowardsCompassTarget(seed, owner, time, pos);
	}
	
	private float getRandomlySpinningRotation(int seed, long time) {
		if (this.noTargetWobbler.shouldUpdate(time)) {
			this.noTargetWobbler.update(time, this.random.nextFloat());
		}
		float f = this.noTargetWobbler.rotation() + hash(seed) / 2.1474836E9F;
		return Mth.positiveModulo(f, 1.0F);
	}
	
	private float getRotationTowardsCompassTarget(int seed, ItemOwner owner, long gameTime, BlockPos targetPos) {
		float toTarget = (float) getAngleFromEntityToPos(owner, targetPos);
		float camYaw = getWrappedVisualRotationY(owner);
		float ret;
		if (owner.asLivingEntity() instanceof Player player && player.isLocalPlayer() && player.level().tickRateManager().runsNormally()) {
			if (this.wobbler.shouldUpdate(gameTime)) {
				this.wobbler.update(gameTime, 0.5F - (camYaw - 0.25F));
			}
			
			ret = toTarget + this.wobbler.rotation();
		} else {
			ret = 0.5F - (camYaw - 0.25F - toTarget);
		}
		
		return Mth.positiveModulo(ret, 1.0F);
	}
	
	private boolean isValidCompassTargetPos(BlockPos pos) {
		return pos != null;
	}
	
	private static double getAngleFromEntityToPos(ItemOwner owner, BlockPos pos) {
		Vec3 vec3 = Vec3.atCenterOf(pos);
		var living = owner.asLivingEntity();
		if (living == null) return 0;
		return Math.atan2(vec3.z() - living.getZ(), vec3.x() - living.getX()) / (float) (Math.PI * 2);
	}
	
	private static float getWrappedVisualRotationY(ItemOwner owner) {
		return Mth.positiveModulo(owner.getVisualRotationYInDegrees() / 360.0F, 1.0F);
	}
	
	private static int hash(int seed) {
		return seed * 1327217883;
	}
	
}
