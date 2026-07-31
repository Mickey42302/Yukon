package com.mickey42302.yukon;

import com.mickey42302.yukon.config.YukonClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.*;
import org.lwjgl.glfw.GLFW;

public class YukonClientInit implements ClientModInitializer {

    public static KeyMapping copyUUIDDebugHotkey;

    public static KeyMapping helpDebugHotkey;

    public static KeyMapping renderDistanceDebugHotkey;

    public static KeyMapping simulationDistanceDebugHotkey;

    public static KeyMapping.Category debugCategory = KeyMapping.Category.DEBUG;

    public static KeyMapping mainDebugHotkey;

    private static void getMainDebugHotkey(Minecraft client) {
        mainDebugHotkey = Minecraft.getInstance().options.keyDebugModifier;
    }

    private static void copyUUID(Minecraft client) {
        if (mainDebugHotkey.isDown() && copyUUIDDebugHotkey.consumeClick()) {
            assert client.player != null;
            String uuid = client.player.getStringUUID();
            client.keyboardHandler.setClipboard(uuid);
            System.out.println(uuid + " has been copied to the clipboard.");

            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.translatable("debug.prefix")
                            .withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true))
                            .append(" ")
                            .append(Component.translatable("yukon.debug.uuid.copied")
                                    .withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)))
            );
        }
    }

    private static void renderDistanceDebug(Minecraft client) {
        if (mainDebugHotkey.isDown() && renderDistanceDebugHotkey.consumeClick()) {
            int currentViewDistance = client.options.renderDistance().get();
            assert Minecraft.getInstance().player != null;
            if (client.options.keyShift.isDown()) {
                if (currentViewDistance <= 31) {
                    client.options.renderDistance().set(currentViewDistance - 1);
                } else {
                    client.options.renderDistance().set(2);
                }
            } else {
                if (currentViewDistance <= 31) {
                    client.options.renderDistance().set(currentViewDistance + 1);
                } else {
                    client.options.renderDistance().set(2);
                }
            }
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.translatable("debug.prefix")
                            .withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true))
                            .append(" ")
                            .append(Component.translatable("yukon.debug.cycle_renderdistance.message", Integer.toString(currentViewDistance))
                                    .withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)))
            );
        }
    }

    private static void simulationDistanceDebug(Minecraft client) {
        if (mainDebugHotkey.isDown() && simulationDistanceDebugHotkey.consumeClick()) {
            int currentSimulationDistance = client.options.simulationDistance().get();
            assert Minecraft.getInstance().player != null;
            if (client.options.keyShift.isDown()) {
                if (currentSimulationDistance <= 31) {
                    client.options.simulationDistance().set(currentSimulationDistance - 1);
                } else {
                    if (SharedConstants.DEBUG_ALLOW_LOW_SIM_DISTANCE) {
                        client.options.simulationDistance().set(2);
                    } else {
                        client.options.simulationDistance().set(5);
                    }
                }
            } else {
                if (currentSimulationDistance <= 31) {
                    client.options.simulationDistance().set(currentSimulationDistance + 1);
                } else {
                    if (SharedConstants.DEBUG_ALLOW_LOW_SIM_DISTANCE) {
                        client.options.simulationDistance().set(2);
                    } else {
                        client.options.simulationDistance().set(5);
                    }
                }
            }
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.translatable("debug.prefix")
                            .withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true))
                            .append(" ")
                            .append(Component.translatable("yukon.debug.cycle_simulationdistance.message", Integer.toString(currentSimulationDistance))
                                    .withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)))
            );
        }
    }

    private static void helpDebug(Minecraft client) {
        if (mainDebugHotkey.isDown() && helpDebugHotkey.consumeClick()) {
            assert client.player != null;
            assert Minecraft.getInstance().player != null;

            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("debug.prefix").withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true)).append(" ").append(Component.translatable("yukon.debug.help.message").withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false))));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.overlay.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugOverlay).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.modifier.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.options.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugDebugOptions).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.advanced_tooltips.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugShowAdvancedTooltips).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.chunk_boundaries.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugShowChunkBorders).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.clear_chat.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugClearChat).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.copy_location.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugCopyLocation).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.creative_spectator.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugSpectate).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.crash.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugCrash).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.cycle_renderdistance.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), renderDistanceDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.cycle_simulationdistance.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), simulationDistanceDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.dump_dynamic_textures.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugDumpDynamicTextures).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.gamemodes.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugSwitchGameMode).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.help.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), helpDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.inspect.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugCopyRecreateCommand).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.pause.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.pause_focus.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugFocusPause).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.reload_chunks.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugReloadChunk).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.reload_resourcepacks.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugReloadResourcePacks).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.show_hitboxes.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugShowHitboxes).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.uuid.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), copyUUIDDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.version.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugDumpVersion).getDisplayName()));
            if (SharedConstants.DEBUG_HOTKEYS) {
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.fog.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.frustum.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.frustum_culling_octree.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.sectionpath.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.sectionvisibility.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.smartcull.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.wireframe.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
            }
            if (SharedConstants.DEBUG_FEATURE_COUNT) {
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.feature_counts.print.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.feature_counts.reset.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
            }
            if (SharedConstants.DEBUG_PANORAMA_SCREENSHOT) {
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.panoramic_screenshot.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyScreenshot).getDisplayName()));
            }
        }
    }

    @Override
    public void onInitializeClient() {

        YukonClientConfig.load();

        copyUUIDDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.uuid",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                debugCategory
        ));

        helpDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.help",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                debugCategory
        ));

        renderDistanceDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.renderdistance",
                InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(),
                debugCategory
        ));

        simulationDistanceDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.simulationdistance",
                InputConstants.Type.KEYSYM,
                InputConstants.UNKNOWN.getValue(),
                debugCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::getMainDebugHotkey);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::copyUUID);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::helpDebug);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::renderDistanceDebug);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::simulationDistanceDebug);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            content.accept(Items.ENDER_DRAGON_SPAWN_EGG);
            content.accept(Items.WITHER_SPAWN_EGG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(content -> {
            content.insertAfter(Items.LINGERING_POTION, Items.LINGERING_POTION);
            content.insertAfter(Items.POTION, Items.POTION);
            content.insertAfter(Items.SPLASH_POTION, Items.SPLASH_POTION);
            content.insertAfter(Items.SUSPICIOUS_STEW, Items.SUSPICIOUS_STEW);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(content -> content.insertAfter(Items.TIPPED_ARROW, Items.TIPPED_ARROW));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
            content.insertAfter(Items.MAP, Items.FILLED_MAP);
            content.insertAfter(Items.WRITABLE_BOOK, Items.WRITTEN_BOOK);
            content.insertAfter(Items.WRITTEN_BOOK, Items.KNOWLEDGE_BOOK);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(content -> content.insertAfter(Items.ENCHANTED_BOOK, Items.ENCHANTED_BOOK));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(content -> {
            ItemStack invertedDaylightDetector = new ItemStack(Items.DAYLIGHT_DETECTOR);
            invertedDaylightDetector.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(DaylightDetectorBlock.INVERTED, true)
            );
            content.insertAfter(Items.DAYLIGHT_DETECTOR, invertedDaylightDetector);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(content -> {
            ItemStack unstableTNT = new ItemStack(Items.TNT);
            unstableTNT.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.tnt"));
            unstableTNT.set(
                    DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(TntBlock.UNSTABLE, true)
            );
            content.accept(unstableTNT);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(content -> content.insertAfter(Items.OAK_SLAB, Items.PETRIFIED_OAK_SLAB));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            ItemStack ominousVault = new ItemStack(Items.VAULT);
            ominousVault.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(VaultBlock.OMINOUS, true)
            );
            content.insertAfter(Items.VAULT, ominousVault);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            ItemStack summonSculkShrieker = new ItemStack(Items.SCULK_SHRIEKER);
            summonSculkShrieker.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.warden")
                    .append(" ")
                    .append(Component.translatable("block.minecraft.sculk_shrieker")));
            summonSculkShrieker.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(SculkShriekerBlock.CAN_SUMMON, true)
            );
            content.insertAfter(Items.CREAKING_HEART, summonSculkShrieker);
        });
    }
}