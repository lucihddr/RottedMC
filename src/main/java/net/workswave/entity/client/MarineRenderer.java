package net.workswave.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.workswave.entity.custom.MarineEntity;
import net.workswave.rotted.Rotted;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarineRenderer extends GeoEntityRenderer<MarineEntity> {
    public MarineRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MarineModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MarineEntity animatable) {
        return new ResourceLocation(Rotted.MODID, "textures/entity/marine.png");
    }

    @Override
    public void render(MarineEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
