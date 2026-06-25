package com.mickey42302.yukon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
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

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(content -> content.accept(Items.PETRIFIED_OAK_SLAB));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            content.accept(Items.ENDER_DRAGON_SPAWN_EGG);
            content.accept(Items.WITHER_SPAWN_EGG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(content -> {
            content.insertAfter(Items.POTION, Items.POTION);
            content.insertAfter(Items.SPLASH_POTION, Items.SPLASH_POTION);
            content.insertAfter(Items.LINGERING_POTION, Items.LINGERING_POTION);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(content -> content.insertAfter(Items.TIPPED_ARROW, Items.TIPPED_ARROW));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> content.accept(Items.KNOWLEDGE_BOOK));
    }
}