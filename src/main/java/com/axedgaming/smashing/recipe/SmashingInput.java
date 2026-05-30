package com.axedgaming.smashing.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public record SmashingInput(ItemStack tool, BlockState blockState) implements RecipeInput {

    @Override
    public ItemStack getItem(int slot) {
        if (slot != 0) {
            throw new IllegalArgumentException("No item for slot " + slot);
        }

        return tool;
    }

    @Override
    public int size() {
        return 1;
    }
}