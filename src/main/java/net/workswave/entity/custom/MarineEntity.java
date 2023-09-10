package net.workswave.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class MarineEntity extends Zombie implements GeoEntity {

    boolean searchingForLand;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MarineEntity(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.waterNavigation = new WaterBoundPathNavigation(this, pLevel);
        this.groundNavigation = new GroundPathNavigation(this, pLevel);
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


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 1D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.ATTACK_DAMAGE, 6D)
                .add(Attributes.ARMOR, 2.0D);
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
