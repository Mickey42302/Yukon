package com.mickey42302.yukon.mixin;

import java.util.stream.Stream;

import com.mickey42302.yukon.util.LastServerUtil;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.text.Text;

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen
{
	
	@Shadow
	@Final
	private DisconnectionInfo info;
	@Shadow
	@Final
	private Screen parent;
	@Shadow
	@Final
	private DirectionalLayoutWidget grid;
	
	private DisconnectedScreenMixin(MinecraftClient client, Text title)
	{
		super(title);
		ButtonWidget reconnectButton = grid.add(ButtonWidget
				.builder(Text.literal("Reconnect"),
						b -> LastServerUtil.reconnect(parent))
				.width(200).build());
		grid.refreshPositions();
		Stream.of(reconnectButton)
				.forEach(this::addDrawableChild);
	}
	
	@Inject(at = @At("TAIL"), method = "init()V")
	private void onInit(CallbackInfo ci)
	{
		addReconnectButtons();
	}
	
	@Unique
	private void addReconnectButtons()
	{
		ButtonWidget reconnectButton = grid.add(ButtonWidget
			.builder(Text.translatable("yukon.reconnect"),
				b -> LastServerUtil.reconnect(parent))
			.width(200).build());
		
		grid.refreshPositions();
		Stream.of(reconnectButton)
			.forEach(this::addDrawableChild);

	}

}
