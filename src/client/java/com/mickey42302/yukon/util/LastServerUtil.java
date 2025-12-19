package com.mickey42302.yukon.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;

public enum LastServerUtil
{
	;
	
	private static ServerInfo lastServer;
	
	public static void setLastServer(ServerInfo server)
	{
		lastServer = server;
	}

	public static void reconnect(Screen prevScreen)
	{
		if(lastServer == null)
			return;
		
		ConnectScreen.connect(prevScreen, MinecraftClient.getInstance(),
			ServerAddress.parse(lastServer.address), lastServer, false, null);
	}
}
