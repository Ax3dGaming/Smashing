package com.axedgaming.smashing.recipe;

import com.axedgaming.smashing.registry.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public record SmashingRecipe(
        Optional<Ingredient> tool,
        boolean requiresEmptyHand,
        float hurtPlayer,
        Block input,
        List<SmashingLoot> outputs
) implements Recipe<SmashingInput> {

    @Override
    public boolean matches(SmashingInput inputData, Level level) {
        if (!inputData.blockState().is(input)) {
            return false;
        }

        if (requiresEmptyHand) {
            return inputData.tool().isEmpty();
        }

        return tool.isPresent() && tool.get().test(inputData.tool());
    }

    @Override
    public ItemStack assemble(SmashingInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.getFirst().item();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        tool.ifPresent(ingredients::add);
        return ingredients;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.SMASHING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.SMASHING_TYPE.get();
    }
}