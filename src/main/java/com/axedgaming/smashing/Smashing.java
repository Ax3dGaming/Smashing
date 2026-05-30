package com.axedgaming.smashing;

import com.axedgaming.smashing.event.SmashingEvents;
import com.axedgaming.smashing.registry.ModRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Smashing.MODID)
public class Smashing {
    public static final String MODID = "smashing";

    public Smashing(IEventBus modEventBus) {
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);

        NeoForge.EVENT_BUS.register(SmashingEvents.class);
    }
}
