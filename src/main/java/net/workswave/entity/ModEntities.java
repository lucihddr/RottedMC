package net.workswave.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.entity.custom.MarineEntity;
import net.workswave.rotted.Rotted;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Rotted.MODID);


    public static final RegistryObject<EntityType<MarineEntity>> MARINE = ENTITY_TYPES.register("marine", () -> EntityType.Builder.of(MarineEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .build(new ResourceLocation(Rotted.MODID, "marine").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
