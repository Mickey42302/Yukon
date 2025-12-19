package com.mickey42302.yukon;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;

public interface MultiplayerScreenWidgets
{
	MultiplayerServerListWidget yukon$getServerListSelector();

	void yukon$connectToServer(ServerInfo server);
}
