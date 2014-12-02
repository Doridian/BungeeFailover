/**
 * This file is part of BungeeFailover.
 *
 * BungeeFailover is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BungeeFailover is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BungeeFailover.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.foxelbox.bungeefailover;

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
		if(event.getPlayer() == null || event.getPlayer().getServer() == null || plugin.mainServer == null || plugin.failoverServer == null)
			return;
		
		String kickReason = event.getKickReason();
		if(kickReason.startsWith("[Kicked] "))
			return;
		System.out.println("KR: " + kickReason);

		event.setCancelled(true);

		if(plugin.failoverServer.equals(event.getPlayer().getServer().getInfo()))
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
