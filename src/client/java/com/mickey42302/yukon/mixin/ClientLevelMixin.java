package com.mickey42302.yukon.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

	@Final
    @Shadow
	private static Set<Item> MARKER_PARTICLE_ITEMS;

	static {
		MARKER_PARTICLE_ITEMS = Set.of(Items.BARRIER, Items.LIGHT, Items.STRUCTURE_VOID);
	}

}