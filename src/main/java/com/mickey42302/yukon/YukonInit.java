package com.mickey42302.yukon;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class YukonInit implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("Loading Yukon...");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> {
            content.add(Items.PETRIFIED_OAK_SLAB);
            content.add(Items.ENDER_DRAGON_SPAWN_EGG);
            content.add(Items.WITHER_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addAfter(Items.POTION, Items.POTION);
            content.addAfter(Items.SPLASH_POTION, Items.SPLASH_POTION);
            content.addAfter(Items.LINGERING_POTION, Items.LINGERING_POTION);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> content.addAfter(Items.TIPPED_ARROW, Items.TIPPED_ARROW));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(Items.KNOWLEDGE_BOOK));

    }
}
