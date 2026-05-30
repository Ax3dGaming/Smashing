package com.axedgaming.smashing.registry;

import com.axedgaming.smashing.Smashing;
import com.axedgaming.smashing.recipe.SmashingRecipe;
import com.axedgaming.smashing.recipe.SmashingRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Smashing.MODID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Smashing.MODID);

    public static final Supplier<RecipeType<SmashingRecipe>> SMASHING_TYPE =
            RECIPE_TYPES.register("smashing",
                    () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Smashing.MODID, "smashing")));

    public static final Supplier<RecipeSerializer<SmashingRecipe>> SMASHING_SERIALIZER =
            RECIPE_SERIALIZERS.register("smashing", SmashingRecipeSerializer::new);
}