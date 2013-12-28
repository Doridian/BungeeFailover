package de.doridian.bungeefailover;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;

public class BungeeFailover extends Plugin {
	ServerInfo failoverServer = null;
	ServerInfo mainServer = null;

	final HashMap<String, ServerInfo> playerNameToServerMap = new HashMap<>();

	@Override
	public void onEnable() {
		failoverServer = getProxy().getServerInfo("lobby");
		mainServer = getProxy().getServerInfo("main");
		getProxy().getPluginManager().registerCommand(this, new IsOnlineCommand(this));
		getProxy().getPluginManager().registerListener(this, new ServerSwitchListener(this));
		getLogger().info("Done! (0.00s)");
	}
}
