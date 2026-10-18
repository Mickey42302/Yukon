package com.mickey42302.yukon;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;

import net.minecraft.SharedConstants;
import org.slf4j.Logger;

public class YukonInit implements ModInitializer {

    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {

        if (Boolean.parseBoolean(System.getProperty("MC_DEBUG_PRINT_PROPERTIES", "true"))) {
            System.out.println("Yukon debug property: MC_DEBUG_TEST_WORLD: bool");
        }

        if (SharedConstants.DEBUG_DONT_SAVE_WORLD) {
            LOGGER.warn("Warning: The DONT_SAVE_WORLD debug property is enabled. Any changes made to your worlds will be lost!");
        }

    }
}