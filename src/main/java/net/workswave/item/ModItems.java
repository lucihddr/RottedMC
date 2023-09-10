package net.workswave.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.workswave.entity.ModEntities;
import net.workswave.rotted.Rotted;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Rotted.MODID);

    public static final RegistryObject<Item> ROTTEN = ITEMS.register("rotten_brain",
            () -> new Item(new Item.Properties().food(ModFoods.ROTTEN_BRAIN)));

    public static final RegistryObject<Item> MARINE_SPAWN_EGG = ITEMS.register("marine_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MARINE, 0x5AB333,0xB1E83D, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
