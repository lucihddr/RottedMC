package net.workswave.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;


public class MarineEntity extends Zombie implements GeoEntity {


    private boolean isLandNavigator;
    public float SwimProgress = 0;
    public float prevSwimProgress = 0;
    boolean searchingForLand;
    private int attackID = 1;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MarineEntity(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        switchNavigator(false);
        this.moveControl = new MarineMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.xpReward = 30;
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 25, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10));
    }
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }
    public boolean canBreatheUnderwater() {
        return true;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public int getWaterSearchRange() {
        return 32;

    }
    @Override
    public boolean shouldEnterWater() {
        return true;
    }
    @Override
    public boolean shouldLeaveWater() {
        return this.getTarget() != null && !this.getTarget().isInWater();
    }
    boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        } else {
            LivingEntity livingentity = this.getTarget();
            return livingentity != null && livingentity.isInWater();
        }
    }
    @Override
    public void tick() {
        super.tick();
        if (isInWater() && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!isInWater() && !this.isLandNavigator) {
            switchNavigator(true);
        }
        this.prevSwimProgress = SwimProgress;
        if (this.isInWater()) {
            if (this.SwimProgress < 10F)
                this.SwimProgress++;
        } else {
            if (this.SwimProgress > 0F)
                this.SwimProgress--;
        }
    }
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.4F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.1D));
        }
        if (this.attackID != 0) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            travelVector = Vec3.ZERO;
            super.travel(travelVector);
            return;
        }
        super.travel(travelVector);

    }

    public void switchNavigator(boolean onLand) {
        if (onLand) {
            this.navigation = new GroundPathNavigatorWide(this, level());
            this.isLandNavigator = true;
        } else {
            this.navigation = new SemiAquaticPathNavigator(this, level());
            this.isLandNavigator = false;
        }
    }
    static class BulldrogiothGoToBeachGoal extends MoveToBlockGoal {
        private final BulldrogiothEntity bulldrogiothEntity;
        public BulldrogiothGoToBeachGoal(BulldrogiothEntity p_32409_, double p_32410_) {
            super(p_32409_, p_32410_, 8, 2);
            this.bulldrogiothEntity = p_32409_;
        }
        public boolean canUse() {
            return super.canUse() && this.bulldrogiothEntity.level().isRaining() && this.bulldrogiothEntity.isInWater() && this.bulldrogiothEntity.getY() >= (double)(this.bulldrogiothEntity.level().getSeaLevel() - 3);
        }
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
        protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
            BlockPos blockpos = p_32414_.above();
            return p_32413_.isEmptyBlock(blockpos) && p_32413_.isEmptyBlock(blockpos.above()) ? p_32413_.getBlockState(p_32414_).entityCanStandOn(p_32413_, p_32414_, this.bulldrogiothEntity) : false;
        }
        public void start() {
            this.bulldrogiothEntity.setSearchingForLand(false);
            super.start();
        }
        public void stop() {
            super.stop();
        }
    }

    static class BulldrogiothSwimUpGoal extends Goal {
        private final BulldrogiothEntity bulldrogiothEntity;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;
        public BulldrogiothSwimUpGoal(BulldrogiothEntity p_32440_, double p_32441_, int p_32442_) {
            this.bulldrogiothEntity = p_32440_;
            this.speedModifier = p_32441_;
            this.seaLevel = p_32442_;
        }
        public boolean canUse() {
            return (this.bulldrogiothEntity.level().isRaining() || this.bulldrogiothEntity.isInWater())&& this.bulldrogiothEntity.getY() < (double)(this.seaLevel - 2);
        }
        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }
        public void tick() {
            if (this.bulldrogiothEntity.getY() < (double)(this.seaLevel - 1) && (this.bulldrogiothEntity.getNavigation().isDone() || this.bulldrogiothEntity.closeToNextPos())) {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.bulldrogiothEntity, 4, 8, new Vec3(this.bulldrogiothEntity.getX(), (double)(this.seaLevel - 1), this.bulldrogiothEntity.getZ()), (double)((float)Math.PI / 2F));
                if (vec3 == null) {
                    this.stuck = true;
                    return;
                }
                this.bulldrogiothEntity.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
            }
        }
        public void start() {
            this.bulldrogiothEntity.setSearchingForLand(true);
            this.stuck = false;
        }
        public void stop() {
            this.bulldrogiothEntity.setSearchingForLand(false);
        }
    }

    public void setSearchingForLand(boolean p_32399_) {
        this.searchingForLand = p_32399_;
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 4D)
                .add(Attributes.ARMOR, 8D)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.ATTACK_KNOCKBACK, 0.3);

    }


    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.DROWNED_AMBIENT_WATER : SoundEvents.DROWNED_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return this.isInWater() ? SoundEvents.DROWNED_HURT_WATER : SoundEvents.DROWNED_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.DROWNED_DEATH_WATER : SoundEvents.DROWNED_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.DROWNED_STEP;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.DROWNED_SWIM;
    }
    protected void customServerAiStep() {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            boolean flag = ((ServerLevel)this.level()).isRaided(this.blockPosition());
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(flag);
        }

        super.customServerAiStep();

    }
    public boolean isVisuallySwimming() {
        return this.isSwimming();
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    if(event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
                    }
                    if(isAggressive() && !isInWater()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("aggression"));
                    }
                    if(isSwimming()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("swim"));
                    }
                    if(isInWater() && getTarget() == null) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("water idle"));

                    }
                    return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));

                }));


     }
     

}
