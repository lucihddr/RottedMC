package net.workswave.rotted;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.workswave.entity.ModEntities;
import net.workswave.entity.client.MarineRenderer;
import net.workswave.item.ModCreativeModTabs;
import net.workswave.item.ModItems;
import org.slf4j.Logger;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(Rotted.MODID)
public class Rotted {
    public static final String MODID = "rotted";
    private static final Logger LOGGER = LogUtils.getLogger();
    public Rotted()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
    }



    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS);
        event.accept(ModItems.ROTTEN);
    }



    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.MARINE.get(), MarineRenderer::new);
        }
    }
}
