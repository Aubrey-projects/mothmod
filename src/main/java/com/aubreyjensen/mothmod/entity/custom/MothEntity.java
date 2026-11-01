package com.aubreyjensen.mothmod.entity.custom;

// My imports
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

/*
Controls the moth's movement behavior (flying and perching on a block),
Also switches being idle and flying animations
 */
public class MothEntity extends PathfinderMob {
    // Syncs whether the moth is idle between the server and client
    private static final EntityDataAccessor<Boolean> DATA_IDLE =
            SynchedEntityData.defineId(MothEntity.class, EntityDataSerializers.BOOLEAN);

    // Creates the two different animation states (flying or idle)
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();

    public MothEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        // Allows for 3D flight
        this.moveControl = new FlyingMoveControl(this, 20, true);
        // Turns off gravity
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IDLE, false); // Makes the default state flying
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        // FlyingPathNavigation allows the mob to pathfind through the air
        FlyingPathNavigation nav = new FlyingPathNavigation(this, level);
        nav.setCanOpenDoors(false);
        nav.setCanFloat(true);
        return nav;
    }

    // This disables fall damage since it is a flying creature
    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected void registerGoals() {
        // It's first priority is to flee when attacked
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.5f));
        // It's second priority is to find a wall
        this.goalSelector.addGoal(1, new LandOnWallGoal(this));
        // It's third priority is to fly randomly, without flying straight into water
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0));
    }

    // Checks whether the moth is idle or not
    public boolean isResting() {
        return this.entityData.get(DATA_IDLE);
    }

    // Can set the moth to resting
    private void setResting(boolean resting) {
        this.entityData.set(DATA_IDLE, resting);
    }

    // Sets up the different animations
    private void setupAnimationStates() {
        if(this.isResting()) {
            // This stops the moth flying and has it perch
            this.flyAnimationState.stop();
            this.idleAnimationState.startIfStopped(this.tickCount);
        } else {
            // This stops it's idle animation and starts it flying
            this.idleAnimationState.stop();
            this.flyAnimationState.startIfStopped(this.tickCount);
        }
    }

    @Override
    public void tick() {
        super.tick();

        // This makes it so that the moth doesn't move when resting on a wall
        if (this.isResting()) {
            this.setDeltaMovement(Vec3.ZERO);
        }

        // This makes it so that the client side handles animations
        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    // Adds the moth attributes
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4d)
                .add(Attributes.MOVEMENT_SPEED, 0.7D)
                .add(Attributes.FLYING_SPEED, 0.5d)
                .add(Attributes.FOLLOW_RANGE, 16D);
    }

    /*
     This has the moth locate a wall, fly toward it, snap onto it, and rest for a random amount of time
     */
    static class LandOnWallGoal extends Goal {
        private final MothEntity moth;
        @Nullable private BlockPos perch;
        @Nullable private Direction wallDir;
        private int restTicks;

        LandOnWallGoal(MothEntity moth) {
            this.moth = moth;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            // Randomizes how often the moth looks for a wall
            if (moth.getRandom().nextInt(reducedTickDelay(200)) != 0) return false;
            return findPerch();
        }

        // Scans places nearby to find a place with a wall
        private boolean findPerch() {
            Level level = moth.level();
            RandomSource rng = moth.getRandom();
            BlockPos origin = moth.blockPosition();

            for (int i = 0; i < 20; i++) {
                BlockPos pos = origin.offset(rng.nextInt(13) - 6, rng.nextInt(7) - 3, rng.nextInt(13) - 6);
                if (!level.getBlockState(pos).isAir()) continue;

                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos wall = pos.relative(dir);
                    if (level.getBlockState(wall).isFaceSturdy(level, wall, dir.getOpposite())) {
                        this.perch = pos;
                        this.wallDir = dir;
                        return true;
                    }
                }
            }
            return false;
        }

        // Has the moth fly to the perch position
        @Override
        public void start() {
            moth.getNavigation().moveTo(
                    perch.getX() + 0.5, perch.getY() + 0.5, perch.getZ() + 0.5, 1.0);
        }

        @Override
        public boolean canContinueToUse() {
            if (perch == null || wallDir == null) return false;

            // Flying towards the wall
            if (!moth.isResting()) {
                return nearPerch() || !moth.getNavigation().isDone();
            }

            // Leaves when they are done, the wall gets destroyed, or they get hit
            Level level = moth.level();
            BlockPos wall = perch.relative(wallDir);
            boolean wallGone = !level.getBlockState(wall).isFaceSturdy(level, wall, wallDir.getOpposite());
            boolean spooked = moth.hurtTime > 0 || level.getNearestPlayer(moth, 3.0) != null;
            return restTicks > 0 && !wallGone && !spooked;
        }

        private boolean nearPerch() {
            return moth.distanceToSqr(Vec3.atCenterOf(perch)) < 1.0;
        }

        @Override
        public void tick() {
            if (!moth.isResting()) {
                // When they reach the wall, they snap onto it
                if (nearPerch()) landOnWall();
                return;
            }
            // Counts down till the idle duration is over
            restTicks--;
        }

        // Snaps and rotates the moth directly against the wall's surface and enables resting state
        private void landOnWall() {
            Vec3 spot = Vec3.atCenterOf(perch).relative(wallDir, 0.35);
            float yaw = wallDir.getOpposite().toYRot();

            moth.getNavigation().stop();
            moth.setPos(spot);
            moth.setYRot(yaw);
            moth.setYBodyRot(yaw);
            moth.setYHeadRot(yaw);

            // Rest for 5 to 20 seconds
            restTicks = 100 + moth.getRandom().nextInt(300);
            moth.setResting(true);
        }

        @Override
        public void stop() {
            // Cleans state after finished or gets interrupted
            moth.setResting(false);
            perch = null;
            wallDir = null;
        }
    }
}
