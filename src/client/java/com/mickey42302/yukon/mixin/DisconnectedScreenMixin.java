package com.mickey42302.yukon.mixin;

import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.mickey42302.yukon.util.LastServerUtil;
import com.mickey42302.yukon.config.YukonConfig; // Import your config

@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin extends Screen {

	@Shadow
	@Final
	private Screen parent;
	@Shadow
	@Final
	private LinearLayout layout;

	private DisconnectedScreenMixin(Component title)
	{
		super(title);
	}

	@Inject(method = "init()V", at = @At("TAIL"))
	private void onInit(CallbackInfo ci)
	{
		if (YukonConfig.getInstance().showReconnectButton) {
			addReconnectButton();
		}
	}

	@Unique
	private void addReconnectButton()
	{
		Button reconnectButton = layout.addChild(Button
				.builder(Component.translatable("yukon.reconnect"),
						_ -> LastServerUtil.reconnect(parent))
				.width(200).build());

		layout.arrangeElements();
		Stream.of(reconnectButton)
				.forEach(this::addRenderableWidget);
	}
}