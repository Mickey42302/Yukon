package com.mickey42302.yukon.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

	@Final
	@Shadow
	private static Set<Item> BLOCK_MARKER_ITEMS;

	static {
		BLOCK_MARKER_ITEMS = Set.of(Items.BARRIER, Items.LIGHT, Items.STRUCTURE_VOID);
	}

}
