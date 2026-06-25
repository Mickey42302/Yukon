package com.mickey42302.yukon.mixin;

import com.mickey42302.yukon.util.LastServerUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin extends Screen
{

	private JoinMultiplayerScreenMixin(Component title)
	{
		super(title);
	}

	@Inject(at = @At("HEAD"),
			method = "join(Lnet/minecraft/client/multiplayer/ServerData;)V")
	private void onConnect(ServerData data, CallbackInfo ci)
	{
		LastServerUtil.setLastServer(data);
	}
}