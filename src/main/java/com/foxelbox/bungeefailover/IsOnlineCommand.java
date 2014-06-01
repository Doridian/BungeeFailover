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

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class IsOnlineCommand extends Command {
	private final BungeeFailover plugin;

	IsOnlineCommand(BungeeFailover plugin) {
		super("isonline", "bungeefailover.no.one.is.supposed.to.have.isonline");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender commandSender, String[] args) {
		final String serverName = args[0];

		final ServerInfo serverInfo = plugin.getProxy().getServerInfo(serverName);
		if(serverInfo == null || serverInfo.equals(plugin.failoverServer))
			return;

		for(ProxiedPlayer player : plugin.failoverServer.getPlayers()) {
			ServerInfo wantedServer = plugin.playerNameToServerMap.get(player.getName().toLowerCase());
			if((wantedServer == null && plugin.mainServer.equals(serverInfo)) || serverInfo.equals(wantedServer)) {
				player.connect(serverInfo);
			}
		}
	}
}
