package com.axedgaming.smashing.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SmashingRecipeSerializer implements RecipeSerializer<SmashingRecipe> {

    public static final MapCodec<SmashingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.optionalFieldOf("tool").forGetter(SmashingRecipe::tool),
            Codec.BOOL.optionalFieldOf("requires_empty_hand", false).forGetter(SmashingRecipe::requiresEmptyHand),
            Codec.FLOAT.optionalFieldOf("hurt_player", 0.0F).forGetter(SmashingRecipe::hurtPlayer),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("input").forGetter(SmashingRecipe::input),
            SmashingLoot.CODEC.listOf().fieldOf("outputs").forGetter(SmashingRecipe::outputs)
    ).apply(instance, SmashingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SmashingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC), SmashingRecipe::tool,
            ByteBufCodecs.BOOL, SmashingRecipe::requiresEmptyHand,
            ByteBufCodecs.FLOAT, SmashingRecipe::hurtPlayer,
            ByteBufCodecs.registry(Registries.BLOCK), SmashingRecipe::input,
            SmashingLoot.STREAM_CODEC.apply(ByteBufCodecs.list()), SmashingRecipe::outputs,
            SmashingRecipe::new
    );

    @Override
    public MapCodec<SmashingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SmashingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}