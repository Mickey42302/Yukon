package com.mickey42302.yukon;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;

import net.minecraft.SharedConstants;
import org.slf4j.Logger;

public class YukonInit implements ModInitializer {

    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {

        if (SharedConstants.CHASE_COMMAND) {
            LOGGER.warn("Warning: The \"/chase\" command is enabled. This feature is known to have a dangerous security flaw.");
            LOGGER.warn("To close the security hole, unset the \"CHASE_COMMAND\" debug property.");
            LOGGER.warn("Learn more: https://feedback.minecraft.net/hc/en-us/community/posts/41725200212365-Fix-permission-level-requirement-for-chase");
        }

    }
}