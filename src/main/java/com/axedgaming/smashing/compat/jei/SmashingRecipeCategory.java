package com.axedgaming.smashing.compat.jei;

import com.axedgaming.smashing.Smashing;
import com.axedgaming.smashing.recipe.SmashingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class SmashingRecipeCategory implements IRecipeCategory<SmashingRecipe> {

    public static final ResourceLocation UID =
            ResourceLocation.fromNamespaceAndPath(
                    Smashing.MODID,
                    "smashing"
            );

    private final IDrawable background;
    private final IDrawable icon;

    public SmashingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(150, 50);

        this.icon = helper.createDrawableItemStack(
                new ItemStack(net.minecraft.world.item.Items.IRON_PICKAXE)
        );
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<SmashingRecipe> getRecipeType() {
        return SmashingJeiPlugin.SMASHING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Smashing");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder,
            SmashingRecipe recipe,
            IFocusGroup focuses
    ) {

        if (recipe.tool().isPresent()) {
            builder.addSlot(
                            RecipeIngredientRole.INPUT,
                            10,
                            16
                    )
                    .addIngredients(recipe.tool().get());
        }

        builder.addSlot(
                        RecipeIngredientRole.INPUT,
                        40,
                        16
                )
                .addItemStack(
                        recipe.input().asItem().getDefaultInstance()
                );

        int x = 90;

        for (var output : recipe.outputs()) {
            float chance = output.chance();

            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 16)
                    .addItemStack(output.item())
                    .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                        tooltip.add(Component.literal("Chance: " + formatChance(chance)));
                    });

            x += 20;
        }
    }

    private static String formatChance(float chance) {
        float percent = chance * 100.0F;

        if (percent == Math.round(percent)) {
            return Math.round(percent) + "%";
        }

        return String.format(java.util.Locale.ROOT, "%.2f%%", percent);
    }
}