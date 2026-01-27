package com.mickey42302.yukon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.client.util.SelectionManager.setClipboard;

public class YukonClientInit implements ClientModInitializer {

    public static KeyBinding copyUUIDDebugHotkey;

    public static KeyBinding helpDebugHotkey;

    public static KeyBinding renderDistanceDebugHotkey;

    public static KeyBinding simulationDistanceDebugHotkey;

    public static KeyBinding.Category debugCategory = KeyBinding.Category.DEBUG;

    public static KeyBinding mainDebugHotkey;

    private static void getMainDebugHotkey(MinecraftClient client) {
        mainDebugHotkey = MinecraftClient.getInstance().options.debugOverlayKey;
    }

    private static void copyUUID(MinecraftClient client) {
        if (mainDebugHotkey.isPressed() && copyUUIDDebugHotkey.wasPressed()) {
            assert client.player != null;
            String uuid = client.player.getUuidAsString();
            setClipboard(client, uuid);
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.prefix").styled(style -> style.withColor(Formatting.YELLOW).withBold(true)).append(" ").append(Text.translatable("yukon.debug.uuid.copied").styled(style -> style.withColor(Formatting.WHITE).withBold(false))), false);
        }
    }

    private static void renderDistanceDebug(MinecraftClient client) {
        if (mainDebugHotkey.isPressed() && renderDistanceDebugHotkey.wasPressed()) {
            Integer currentViewDistance = client.options.getViewDistance().getValue();
            if (client.isShiftPressed()) {
                assert MinecraftClient.getInstance().player != null;
                if (currentViewDistance <= 31) {
                    client.options.getViewDistance().setValue(currentViewDistance - 1);
                } else {
                    client.options.getViewDistance().setValue(2);
                }
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.prefix").styled(style -> style.withColor(Formatting.YELLOW).withBold(true)).append(" ").append(Text.translatable("yukon.debug.cycle_renderdistance.message", currentViewDistance.toString()).styled(style -> style.withColor(Formatting.WHITE).withBold(false))), false);
            } else {
                assert MinecraftClient.getInstance().player != null;
                if (currentViewDistance <= 31) {
                    client.options.getViewDistance().setValue(currentViewDistance + 1);
                } else {
                    client.options.getViewDistance().setValue(2);
                }
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.prefix").styled(style -> style.withColor(Formatting.YELLOW).withBold(true)).append(" ").append(Text.translatable("yukon.debug.cycle_renderdistance.message", currentViewDistance.toString()).styled(style -> style.withColor(Formatting.WHITE).withBold(false))), false);
            }
        }
    }

    private static void simulationDistanceDebug(MinecraftClient client) {
        if (mainDebugHotkey.isPressed() && simulationDistanceDebugHotkey.wasPressed()) {
            Integer currentSimulationDistance = client.options.getSimulationDistance().getValue();
            if (client.isShiftPressed()) {
                assert MinecraftClient.getInstance().player != null;
                if (currentSimulationDistance <= 31) {
                    client.options.getSimulationDistance().setValue(currentSimulationDistance - 1);
                } else {
                    if (SharedConstants.ALLOW_LOW_SIM_DISTANCE) {
                        client.options.getSimulationDistance().setValue(2);
                    }
                    else {
                        client.options.getSimulationDistance().setValue(5);
                    }
                }
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.prefix").styled(style -> style.withColor(Formatting.YELLOW).withBold(true)).append(" ").append(Text.translatable("yukon.debug.cycle_simulationdistance.message", currentSimulationDistance.toString()).styled(style -> style.withColor(Formatting.WHITE).withBold(false))), false);
            } else {
                assert MinecraftClient.getInstance().player != null;
                if (currentSimulationDistance <= 31) {
                    client.options.getSimulationDistance().setValue(currentSimulationDistance + 1);
                } else {
                    if (SharedConstants.ALLOW_LOW_SIM_DISTANCE) {
                        client.options.getSimulationDistance().setValue(2);
                    }
                    else {
                        client.options.getSimulationDistance().setValue(5);
                    }
                }
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.prefix").styled(style -> style.withColor(Formatting.YELLOW).withBold(true)).append(" ").append(Text.translatable("yukon.debug.cycle_simulationdistance.message", currentSimulationDistance.toString()).styled(style -> style.withColor(Formatting.WHITE).withBold(false))), false);
            }
        }
    }

    private static void helpDebug(MinecraftClient client) {
        if (mainDebugHotkey.isPressed() && helpDebugHotkey.wasPressed()) {
            assert client.player != null;
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.prefix").styled(style -> style.withColor(Formatting.YELLOW).withBold(true)).append(" ").append(Text.translatable("yukon.debug.help.message").styled(style -> style.withColor(Formatting.WHITE).withBold(false))), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.overlay.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugOverlayKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.modifier.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.options.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugOptionsKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.advanced_tooltips.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugShowAdvancedTooltipsKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.chunk_boundaries.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugShowChunkBordersKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.clear_chat.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugClearChatKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.copy_location.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugCopyLocationKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.creative_spectator.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugSpectateKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.crash.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugCrashKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.cycle_renderdistance.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(renderDistanceDebugHotkey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.cycle_simulationdistance.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(simulationDistanceDebugHotkey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.dump_dynamic_textures.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugDumpDynamicTexturesKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.gamemodes.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugSwitchGameModeKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.help.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(helpDebugHotkey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.inspect.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugCopyRecreateCommandKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.pause.help",KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.pause_focus.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugFocusPauseKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.reload_chunks.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugReloadChunkKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.reload_resourcepacks.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugReloadResourcePacksKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.show_hitboxes.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugShowHitboxesKey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.uuid.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(copyUUIDDebugHotkey).getLocalizedText()), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.version.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText(), KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugDumpVersionKey).getLocalizedText()), false);
            if (SharedConstants.HOTKEYS) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.fog.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.frustum.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.frustum_culling_octree.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.sectionpath.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.sectionvisibility.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.smartcull.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.wireframe.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.debugModifierKey).getLocalizedText()), false);
            }
            if (SharedConstants.PANORAMA_SCREENSHOT) {
                MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.panoramic_screenshot.help", KeyBindingHelper.getBoundKeyOf(MinecraftClient.getInstance().options.screenshotKey).getLocalizedText()), false);
            }
        }
    }

    @Override
    public void onInitializeClient() {

        copyUUIDDebugHotkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "yukon.key.debug.uuid",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                debugCategory
        ));

        helpDebugHotkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "yukon.key.debug.help",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                debugCategory
        ));

        renderDistanceDebugHotkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "yukon.key.debug.renderdistance",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                debugCategory
        ));

        simulationDistanceDebugHotkey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "yukon.key.debug.simulationdistance",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                debugCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::getMainDebugHotkey);

        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::copyUUID);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::helpDebug);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::renderDistanceDebug);
        ClientTickEvents.END_CLIENT_TICK.register(YukonClientInit::simulationDistanceDebug);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> content.add(Items.PETRIFIED_OAK_SLAB));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
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