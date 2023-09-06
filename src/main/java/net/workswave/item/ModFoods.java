package net.workswave.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties ROTTEN_BRAIN = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.2f).effect(() -> new MobEffectInstance(MobEffects.POISON, 200), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200), 1f).build();
}
