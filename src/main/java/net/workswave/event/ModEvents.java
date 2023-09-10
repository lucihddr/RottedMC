package net.workswave.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.workswave.entity.ModEntities;
import net.workswave.entity.custom.MarineEntity;
import net.workswave.rotted.Rotted;

@Mod.EventBusSubscriber(modid = Rotted.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MARINE.get(), MarineEntity.createAttributes().build());
    }
}
