package com.mickey42302.yukon.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;

public enum LastServerUtil {
	;

	private static ServerData lastServer;

	public static void setLastServer(ServerData server)
	{
		lastServer = server;
	}

	public static void reconnect(Screen prevScreen)
	{
		if(lastServer == null) {
			System.out.println("Cannot reconnect: lastServer is null.");
			return;
		}

		ConnectScreen.startConnecting(prevScreen, Minecraft.getInstance(),
				ServerAddress.parseString(lastServer.ip), lastServer, false, null);
	}
}