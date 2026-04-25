package com.distancemeasurer.mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DistanceMeasurerMod.MOD_ID)
public class DistanceMeasurerMod {

    public static final String MOD_ID = "distancemeasurer";

    public DistanceMeasurerMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new DistanceMeasurer());
    }
}
