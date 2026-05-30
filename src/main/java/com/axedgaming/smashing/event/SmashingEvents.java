package com.axedgaming.smashing.event;

import com.axedgaming.smashing.Smashing;
import com.axedgaming.smashing.recipe.SmashingInput;
import com.axedgaming.smashing.recipe.SmashingLoot;
import com.axedgaming.smashing.recipe.SmashingRecipe;
import com.axedgaming.smashing.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Smashing.MODID)
public class SmashingEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null || player.isCreative()) {
            return;
        }

        ItemStack tool = player.getMainHandItem();
        BlockPos pos = event.getPos();
        BlockState blockState = event.getState();

        SmashingInput input = new SmashingInput(tool, blockState);

        level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.SMASHING_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.matches(input, level))
                .findFirst()
                .ifPresent(recipe -> {
                    event.setCanceled(true);

                    level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);

                    runSmashingRecipe(level, pos, recipe);

                    if (!tool.isEmpty() && tool.isDamageableItem()) {
                        tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }

                    if (recipe.requiresEmptyHand() && recipe.hurtPlayer() > 0.0F) {
                        player.hurt(level.damageSources().generic(), recipe.hurtPlayer());
                    }
                });
    }

    private static void runSmashingRecipe(ServerLevel level, BlockPos pos, SmashingRecipe recipe) {
        for (SmashingLoot loot : recipe.outputs()) {
            if (level.random.nextFloat() < loot.chance()) {
                ItemStack stack = loot.item().copy();

                if (stack.isEmpty()) {
                    continue;
                }

                ItemEntity itemEntity = new ItemEntity(
                        level,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        stack
                );

                itemEntity.setPickUpDelay(10);
                level.addFreshEntity(itemEntity);
            }
        }
    }
}