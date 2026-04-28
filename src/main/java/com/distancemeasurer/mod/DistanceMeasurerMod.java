package com.distancemeasurer.mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.glfw.GLFW;

@Mod(DistanceMeasurerMod.MOD_ID)
public class DistanceMeasurerMod {

    public static final String MOD_ID = "distancemeasurer";

    public static KeyBinding TOGGLE_KEY;

    public DistanceMeasurerMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        TOGGLE_KEY = new KeyBinding(
            "key.distancemeasurer.toggle",
            KeyConflictContext.IN_GAME,
            net.minecraft.client.util.InputMappings.getKey("key.keyboard.g"),
            "key.categories.distancemeasurer"
        );
        ClientRegistry.registerKeyBinding(TOGGLE_KEY);
        MinecraftForge.EVENT_BUS.register(new DistanceMeasurer());
    }
}
