package com.distancemeasurer.mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("distancemeasurer")
public class DistanceMeasurer {

    public static final String MOD_ID = "distancemeasurer";
    public static final Logger LOGGER = LogManager.getLogger();

    public DistanceMeasurer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new MeasureEventHandler());
        MinecraftForge.EVENT_BUS.register(new KeyBindingHandler());
        LOGGER.info("Distance Measurer mod loaded!");
    }
}
