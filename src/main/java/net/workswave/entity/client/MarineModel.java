package net.workswave.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.MarineEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MarineModel extends GeoModel<MarineEntity> {
    @Override
    public ResourceLocation getModelResource(MarineEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "geo/marine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarineEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/marine.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MarineEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "animations/marine.animations.json");
    }

    @Override
    public void setCustomAnimations(MarineEntity animatable, long instanceId, AnimationState<MarineEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone arms = this.getAnimationProcessor().getBone("arms");
        CoreGeoBone right_arm = this.getAnimationProcessor().getBone("right_arm");
        CoreGeoBone left_arm = this.getAnimationProcessor().getBone("left_arm");

    }
}
