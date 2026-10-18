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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.*;

public class YukonClientInit implements ClientModInitializer {

    public static KeyMapping copyUUIDDebugHotkey;
    public static KeyMapping helpDebugHotkey;
    public static KeyMapping renderDistanceDebugHotkey;
    public static KeyMapping simulationDistanceDebugHotkey;
    public static KeyMapping.Category debugCategory = KeyMapping.Category.DEBUG;
    public static KeyMapping mainDebugHotkey;

    private static int renderDistanceRepeatTicks = 0;
    private static int simulationDistanceRepeatTicks = 0;

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
        } else if (!mainDebugHotkey.isDown()) {
            copyUUIDDebugHotkey.consumeClick();
        }
    }

    private static void renderDistanceDebug(Minecraft client) {
        if (!mainDebugHotkey.isDown()) {
            renderDistanceRepeatTicks = 0;
            return;
        }
        InputConstants.Key boundKey = KeyMappingHelper.getBoundKeyOf(renderDistanceDebugHotkey);
        if (boundKey.getValue() == InputConstants.UNKNOWN.getValue()) {
            return;
        }
        boolean isDown = false;
        if (boundKey.getType() == InputConstants.Type.KEYBOARD) {
            isDown = InputConstants.isKeyDown(boundKey.getValue());
        }

        if (isDown) {
            if (renderDistanceRepeatTicks == 0) {
                int currentRenderDistance = client.options.renderDistance().get();
                int newRenderDistance = getNewRenderDistance(currentRenderDistance);
                client.options.renderDistance().set(newRenderDistance);
                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.sendSystemMessage(
                        Component.translatable("debug.prefix")
                                .withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true))
                                .append(" ")
                                .append(Component.translatable("yukon.debug.cycle_renderdistance.message", Integer.toString(newRenderDistance))
                                        .withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)))
                );
                renderDistanceRepeatTicks = (renderDistanceRepeatTicks == 0) ? -10 : 1;
            }
            renderDistanceRepeatTicks++;
        } else {
            renderDistanceRepeatTicks = 0;
        }
    }

    private static int getNewRenderDistance(int currentRenderDistance) {
        int newRenderDistance;
        boolean isShiftDown = InputConstants.isKeyDown(InputConstants.KEY_LSHIFT) ||
                InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
        if (isShiftDown) {
            if (currentRenderDistance > 2) {
                newRenderDistance = currentRenderDistance - 1;
            } else {
                newRenderDistance = 32;
            }
        } else {
            if (currentRenderDistance < 32) {
                newRenderDistance = currentRenderDistance + 1;
            } else {
                newRenderDistance = 2;
            }
        }
        return newRenderDistance;
    }

    private static void simulationDistanceDebug(Minecraft client) {
        if (!mainDebugHotkey.isDown()) {
            simulationDistanceRepeatTicks = 0;
            return;
        }
        InputConstants.Key boundKey = KeyMappingHelper.getBoundKeyOf(simulationDistanceDebugHotkey);
        if (boundKey.getValue() == InputConstants.UNKNOWN.getValue()) {
            return;
        }
        boolean isDown = false;
        if (boundKey.getType() == InputConstants.Type.KEYBOARD) {
            isDown = InputConstants.isKeyDown(boundKey.getValue());
        }

        if (isDown) {
            if (simulationDistanceRepeatTicks == 0) {
                int currentSimulationDistance = client.options.simulationDistance().get();
                int newSimulationDistance = getNewSimulationDistance(currentSimulationDistance);
                client.options.simulationDistance().set(newSimulationDistance);

                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.sendSystemMessage(
                        Component.translatable("debug.prefix")
                                .withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true))
                                .append(" ")
                                .append(Component.translatable("yukon.debug.cycle_simulationdistance.message", Integer.toString(newSimulationDistance))
                                        .withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)))
                );
                simulationDistanceRepeatTicks = -10;
            }
            simulationDistanceRepeatTicks++;
        } else {
            simulationDistanceRepeatTicks = 0;
        }
    }

    private static int getNewSimulationDistance(int currentSimulationDistance) {
        int newSimulationDistance;
        boolean isShiftDown = InputConstants.isKeyDown(InputConstants.KEY_LSHIFT) ||
                InputConstants.isKeyDown(InputConstants.KEY_RSHIFT);
        if (isShiftDown) {
            if (currentSimulationDistance > 2) {
                newSimulationDistance = currentSimulationDistance - 1;
            } else {
                newSimulationDistance = 32;
            }
        } else {
            if (currentSimulationDistance < 32) {
                newSimulationDistance = currentSimulationDistance + 1;
            } else {
                newSimulationDistance = SharedConstants.DEBUG_ALLOW_LOW_SIM_DISTANCE ? 2 : 5;
            }
        }
        return newSimulationDistance;
    }

    private static void helpDebug(Minecraft client) {
        if (mainDebugHotkey.isDown() && helpDebugHotkey.consumeClick()) {
            assert client.player != null;
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("debug.prefix").withStyle(style -> style.withColor(ChatFormatting.YELLOW).withBold(true)).append(" ").append(Component.translatable("yukon.debug.help.message").withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false))));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.overlay.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugOverlay).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.modifier.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.options.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugDebugOptions).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.help.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), helpDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.advanced_tooltips.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugShowAdvancedTooltips).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.chunk_boundaries.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugShowChunkBorders).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.clear_chat.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugClearChat).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.copy_location.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugCopyLocation).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.creative_spectator.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugSpectate).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.crash.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugCrash).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.dump_dynamic_textures.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugDumpDynamicTextures).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.gamemodes.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugSwitchGameMode).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.inspect.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugCopyRecreateCommand).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.pause.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.pause_focus.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugFocusPause).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.reload_chunks.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugReloadChunk).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.reload_resourcepacks.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugReloadResourcePacks).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.show_hitboxes.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugShowHitboxes).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.version.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugDumpVersion).getDisplayName()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.uuid.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), copyUUIDDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.cycle_renderdistance.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), renderDistanceDebugHotkey.getTranslatedKeyMessage()));
            Minecraft.getInstance().player.sendSystemMessage(Component.translatable("yukon.debug.cycle_simulationdistance.help", KeyMappingHelper.getBoundKeyOf(Minecraft.getInstance().options.keyDebugModifier).getDisplayName(), simulationDistanceDebugHotkey.getTranslatedKeyMessage()));
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
        } else if (!mainDebugHotkey.isDown()) {
            helpDebugHotkey.consumeClick();
        }
    }

    private void onClientTick(Minecraft client) {
        if (client.player == null) return;
        mainDebugHotkey = client.options.keyDebugModifier;
        copyUUID(client);
        renderDistanceDebug(client);
        simulationDistanceDebug(client);
        helpDebug(client);
    }

    @Override
    public void onInitializeClient() {
        YukonClientConfig.load();

        copyUUIDDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.uuid",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_Y,
                debugCategory
        ));

        helpDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.help",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_Q,
                debugCategory
        ));

        renderDistanceDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.renderdistance",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                debugCategory
        ));

        simulationDistanceDebugHotkey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "yukon.key.debug.simulationdistance",
                InputConstants.Type.KEYBOARD,
                InputConstants.UNKNOWN.getValue(),
                debugCategory
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

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

        // To reduce clutter in the Creative inventory, these items are not listed by default.
        if (YukonClientConfig.getInstance().showBlankItems) {
            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
                content.insertAfter(Items.MAP, Items.FILLED_MAP);
                content.insertAfter(Items.FILLED_MAP, Items.BURIED_TREASURE_MAP);
                content.insertAfter(Items.WRITABLE_BOOK, Items.WRITTEN_BOOK);
                content.insertAfter(Items.WRITTEN_BOOK, Items.KNOWLEDGE_BOOK);
            });

            CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(content -> content.insertAfter(Items.ENCHANTED_BOOK, Items.ENCHANTED_BOOK));
        }

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(content -> {
            ItemStack invertedDaylightDetector = new ItemStack(Items.DAYLIGHT_DETECTOR);
            invertedDaylightDetector.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(DaylightDetectorBlock.INVERTED, true)
            );
            content.insertAfter(Items.DAYLIGHT_DETECTOR, invertedDaylightDetector);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            ItemStack crackedDecoratedPot = new ItemStack(Items.DECORATED_POT);
            crackedDecoratedPot.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(DecoratedPotBlock.CRACKED, true)
            );
            content.insertAfter(Items.DECORATED_POT, crackedDecoratedPot);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(content -> {
            ItemStack crackedDecoratedPot = new ItemStack(Items.DECORATED_POT);
            crackedDecoratedPot.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(DecoratedPotBlock.CRACKED, true)
            );
            content.insertAfter(Items.DECORATED_POT, crackedDecoratedPot);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(content -> {
            ItemStack unstableTNT = new ItemStack(Items.TNT);
            unstableTNT.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.tnt"));
            unstableTNT.set(
                    DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(TntBlock.UNSTABLE, true)
            );
            content.insertAfter(Items.TNT, unstableTNT);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(content -> {
            ItemStack unstableTNT = new ItemStack(Items.TNT);
            unstableTNT.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.tnt"));
            unstableTNT.set(
                    DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(TntBlock.UNSTABLE, true)
            );
            content.insertAfter(Items.TNT, unstableTNT);
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

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(content -> {
            ItemStack summonSculkShrieker = new ItemStack(Items.SCULK_SHRIEKER);
            summonSculkShrieker.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.warden")
                    .append(" ")
                    .append(Component.translatable("block.minecraft.sculk_shrieker")));
            summonSculkShrieker.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY.with(SculkShriekerBlock.CAN_SUMMON, true)
            );
            content.insertAfter(Items.SCULK_SHRIEKER, summonSculkShrieker);
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

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(content -> {
            ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD);
            com.mojang.authlib.GameProfile profile = net.minecraft.client.Minecraft.getInstance().getGameProfile();
            playerHead.set(
                    DataComponents.PROFILE,
                    net.minecraft.world.item.component.ResolvableProfile.createResolved(profile)
            );
            content.insertAfter(Items.PLAYER_HEAD, playerHead);
        });

        // This code uses workarounds to create a Spawn Egg that spawns Illusioners.
        // If Mojang adds a true Spawn Egg for Illusioners, this feature will be removed.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            ItemStack illusionerSpawnEgg = new ItemStack(Items.PILLAGER_SPAWN_EGG);
            illusionerSpawnEgg.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.illusioner"));
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", "minecraft:illusioner");
            EntityType<?> illusionerType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse("minecraft:illusioner"));
            TypedEntityData<EntityType<?>> entityData = TypedEntityData.of(illusionerType, entityTag);
            illusionerSpawnEgg.set(DataComponents.ENTITY_DATA, entityData);
            content.insertAfter(new ItemStack(Items.PILLAGER_SPAWN_EGG), illusionerSpawnEgg);
        });

        // This code uses workarounds to create a Spawn Egg that spawns Killer Bunnies.
        // If Mojang adds a true Spawn Egg for Killer Bunnies, this feature will be removed.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            ItemStack killerBunnySpawnEgg = new ItemStack(Items.RABBIT_SPAWN_EGG);
            killerBunnySpawnEgg.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.killer_bunny"));
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", "minecraft:rabbit");
            entityTag.putInt("RabbitType", 99);
            EntityType<?> rabbitType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse("minecraft:rabbit"));
            TypedEntityData<EntityType<?>> entityData = TypedEntityData.of(rabbitType, entityTag);
            killerBunnySpawnEgg.set(DataComponents.ENTITY_DATA, entityData);
            killerBunnySpawnEgg.set(DataComponents.ENTITY_DATA, entityData);
            content.insertAfter(new ItemStack(Items.RABBIT_SPAWN_EGG), killerBunnySpawnEgg);
        });

        // This code uses workarounds to create a Spawn Egg that spawns Giants.
        // If Mojang adds a true Spawn Egg for Giants, this feature will be removed.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            ItemStack giantSpawnEgg = new ItemStack(Items.ZOMBIE_SPAWN_EGG);
            giantSpawnEgg.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.giant"));
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", "minecraft:giant");
            EntityType<?> giantType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse("minecraft:giant"));
            TypedEntityData<EntityType<?>> entityData = TypedEntityData.of(giantType, entityTag);
            giantSpawnEgg.set(DataComponents.ENTITY_DATA, entityData);
            content.insertAfter(new ItemStack(Items.ZOMBIE_SPAWN_EGG), giantSpawnEgg);
        });

        // This code uses workarounds to create an item that places Spawner Minecarts.
        // If Mojang adds a true item for Spawner Minecarts, this feature will be removed.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            ItemStack spawnerMinecartItem = new ItemStack(Items.BAT_SPAWN_EGG);
            spawnerMinecartItem.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.spawner_minecart"));
            spawnerMinecartItem.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:minecart"));
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", "minecraft:spawner_minecart");
            EntityType<?> minecartType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse("minecraft:spawner_minecart"));
            TypedEntityData<EntityType<?>> entityData = TypedEntityData.of(minecartType, entityTag);
            spawnerMinecartItem.set(DataComponents.ENTITY_DATA, entityData);
            content.insertAfter(new ItemStack(Items.SPAWNER), spawnerMinecartItem);
        });

        // This code uses workarounds to create an item that spawns Lightning Bolts.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(content -> {
            ItemStack lightningBoltSpawnEgg = new ItemStack(Items.BAT_SPAWN_EGG);
            lightningBoltSpawnEgg.set(DataComponents.ITEM_NAME, Component.translatable("entity.minecraft.lightning_bolt"));
            lightningBoltSpawnEgg.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:breeze_rod"));
            CompoundTag entityTag = new CompoundTag();
            entityTag.putString("id", "minecraft:lightning_bolt");
            EntityType<?> lightningBoltType = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.parse("minecraft:lightning_bolt"));
            TypedEntityData<EntityType<?>> entityData = TypedEntityData.of(lightningBoltType, entityTag);
            lightningBoltSpawnEgg.set(DataComponents.ENTITY_DATA, entityData);
            content.accept(lightningBoltSpawnEgg);
        });
    }
}