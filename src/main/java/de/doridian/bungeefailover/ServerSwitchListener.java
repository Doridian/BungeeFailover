package de.doridian.bungeefailover;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {
	private final BungeeFailover plugin;

	ServerSwitchListener(BungeeFailover plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		plugin.playerNameToServerMap.remove(event.getPlayer().getName().toLowerCase());
	}

	@EventHandler
	public void onPlayerKicked(ServerKickEvent event) {
		String kickReason = event.getKickReason();
		if(kickReason.contains("\u00a7r"))
			return;

		event.setCancelled(true);

		if(event.getPlayer().getServer() == null || plugin.failoverServer.equals(event.getPlayer().getServer().getInfo()))
			event.setCancelServer(plugin.mainServer);
		else
			event.setCancelServer(plugin.failoverServer);
	}

	private void playerSwitchToServer(ProxiedPlayer player, ServerInfo serverInfo) {
		if(serverInfo.equals(plugin.failoverServer))
			return;
		plugin.playerNameToServerMap.put(player.getName().toLowerCase(), serverInfo);
	}

	@EventHandler
	public void onPlayerServerConnect(ServerConnectEvent event) {
		playerSwitchToServer(event.getPlayer(), event.getTarget());
	}

	@EventHandler
	public void onPlayerServerConnected(ServerConnectedEvent event) {
		playerSwitchToServer(event.getPlayer(), event.getServer().getInfo());
	}
}
