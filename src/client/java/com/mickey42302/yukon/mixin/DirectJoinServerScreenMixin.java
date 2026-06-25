package com.mickey42302.yukon.mixin;

import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mickey42302.yukon.util.LastServerUtil;

@Mixin(DirectJoinServerScreen.class)
public class DirectJoinServerScreenMixin extends Screen
{
	@Shadow
	@Final
	private ServerData serverData;

	private DirectJoinServerScreenMixin(Component title)
	{
		super(title);
	}

	@Inject(at = @At("TAIL"), method = "init()V")
	private void onSaveAndClose(CallbackInfo ci)
	{
		LastServerUtil.setLastServer(serverData);
	}
}