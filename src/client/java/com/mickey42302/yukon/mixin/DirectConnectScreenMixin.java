package com.mickey42302.yukon.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.DirectConnectScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import com.mickey42302.yukon.util.LastServerUtil;

@Mixin(DirectConnectScreen.class)
public class DirectConnectScreenMixin extends Screen
{
	@Shadow
	@Final
	private ServerInfo serverEntry;
	
	private DirectConnectScreenMixin(MinecraftClient client, Text title)
	{
		super(title);
	}
	
	@Inject(at = @At("TAIL"), method = "saveAndClose()V")
	private void onSaveAndClose(CallbackInfo ci)
	{
		LastServerUtil.setLastServer(serverEntry);
	}
}
