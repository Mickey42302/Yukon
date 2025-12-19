package com.mickey42302.yukon.mixin;

import com.mickey42302.yukon.MultiplayerScreenWidgets;
import com.mickey42302.yukon.util.LastServerUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenWidgetsMixin extends Screen implements MultiplayerScreenWidgets
{
	@Shadow
	protected MultiplayerServerListWidget serverListWidget;
	
	private MultiplayerScreenWidgetsMixin(Text title)
	{
		super(title);
	}
	
	@Inject(at = @At("HEAD"),
		method = "connect(Lnet/minecraft/client/network/ServerInfo;)V")
	private void onConnect(ServerInfo entry, CallbackInfo ci)
	{
		LastServerUtil.setLastServer(entry);
	}
	
	@Override
	public MultiplayerServerListWidget yukon$getServerListSelector()
	{
		return serverListWidget;
	}

	@Override
	public void yukon$connectToServer(ServerInfo server)
	{
		connect(server);
	}

	@Shadow
    public void connect(ServerInfo entry)
	{
		
	}
}
