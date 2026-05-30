package com.axedgaming.smashing.compat.jei;

import com.axedgaming.smashing.Smashing;
import com.axedgaming.smashing.recipe.SmashingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.JeiPlugin;
import net.minecraft.client.Minecraft;

import java.util.List;

@JeiPlugin
public class SmashingJeiPlugin implements IModPlugin {

    public static final mezz.jei.api.recipe.RecipeType<SmashingRecipe> SMASHING_RECIPE_TYPE =
            RecipeType.create(
                    Smashing.MODID,
                    "smashing",
                    SmashingRecipe.class
            );

    @Override
    public net.minecraft.resources.ResourceLocation getPluginUid() {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                Smashing.MODID,
                "jei_plugin"
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new SmashingRecipeCategory(
                        registration.getJeiHelpers().getGuiHelper()
                )
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;

        if (level == null) {
            return;
        }

        List<SmashingRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(com.axedgaming.smashing.registry.ModRecipes.SMASHING_TYPE.get())
                .stream()
                .map(holder -> holder.value())
                .toList();

        registration.addRecipes(SMASHING_RECIPE_TYPE, recipes);
    }
}