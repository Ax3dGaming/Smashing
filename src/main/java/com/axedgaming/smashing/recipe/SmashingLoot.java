package com.axedgaming.smashing.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record SmashingLoot(ItemStack item, float chance) {

    public static final Codec<SmashingLoot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("item").forGetter(SmashingLoot::item),
            Codec.FLOAT.fieldOf("chance").forGetter(SmashingLoot::chance)
    ).apply(instance, SmashingLoot::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SmashingLoot> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, SmashingLoot::item,
            ByteBufCodecs.FLOAT, SmashingLoot::chance,
            SmashingLoot::new
    );
}