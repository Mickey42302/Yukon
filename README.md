# Yukon (Java Edition)

## Introduction

Yukon is a compilation of tweaks for Java Edition. Most of them are designed to make content creation and server administration easier.

However, Yukon also includes changes that are designed to improve the game for all users. If you're interested, feel free to give them a try!

## Structure Voids

In Java Edition, any nearby Barriers and Light Blocks will show where they are located while you are holding the respective item. This is very useful, as the blocks are invisible.

By default, Structure Voids do not show where they are located when the respective item is being held. Yukon implements this behaviour for Structure Voids. This makes working with Structure Voids much easier, as they are invisible and have a much smaller hitbox.

## Reconnect Button

Yukon adds a "Reconnect" button to the screen that is shown when the player is disconnected. This can be used to quickly reconnect to the server.

*Note: To prevent misuse, Yukon does not provide the ability to automatically reconnect to servers. You will need to click the "Reconnect" button.*

## Debug Sticks

Yukon adds a unique texture for the Debug Stick. Since the features of the Debug Stick are like magic, why not give it a texture that makes it look like a magician's wand?

## Creative Inventory

Yukon adds items that are not listed by default to the Creative inventory. Some of these will only be visible if the Operator Utilities section is enabled.

• Uncraftable Potions/Tipped Arrows

• Knowledge Books

• Ender Dragon/Wither Spawn Eggs

• Petrified Oak Slabs

## Debug Hotkeys

Yukon adds some additional debug hotkeys. You can configure them using the Controls menu. To help prevent conflicts, some of these features do not have a key bind set by default.

• F3 + Y = Copy UUID to clipboard

• F3 + Not Bound = Cycle simulation distance (shift to invert)

This mod restores the help hotkey which was removed from the game. If the additional debug hotkeys are enabled using the "HOTKEYS" debug property, they will be listed as well. Similarly, the hotkey for generating a panoramic screenshot will be listed if the "PANORAMIC_SCREENSHOT" debug property is enabled.

• F3 + Q = List all available debug hotkeys

This mod also restores the render distance hotkey which was removed from the game. If you wish to use this feature, you will need to set a key bind in the Controls menu.

• F3 + Not Bound = Cycle render distance (shift to invert)

## Bug Fixes

Yukon includes a patch which adds translation strings that are missing by default. This includes the missing translations for numerous death messages and the missing translations for the "/warden_spawn_tracker" command.

By default, the Awkward, Thick, and Mundane Tipped Arrows use a generic name. This bug was reported over 6 years ago, but has yet to be fixed by Mojang (https://bugs.mojang.com/browse/MC-158539).

Yukon addresses the problem by changing the translations for Awkward, Thick, and Mundane Tipped Arrows. This makes it much easier to differentiate between them.

## Installation

To install this mod, place the JAR file in your "mods" directory. You must also install the Fabric API.

## Disclaimer

This software has been released with fair use in mind; I am not affiliated with Mojang or Microsoft and do not own any of the games I have created content for. **While I work to ensure that my projects are of the best quality, they are provided with absolutely no warranty.**
