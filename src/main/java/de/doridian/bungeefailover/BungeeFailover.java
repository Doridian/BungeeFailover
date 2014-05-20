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
		failoverServer = getProxy().getServerInfo("failover");
		mainServer = getProxy().getServerInfo("main");
		getProxy().getPluginManager().registerCommand(this, new IsOnlineCommand(this));
		getProxy().getPluginManager().registerListener(this, new ServerSwitchListener(this));
	}
}
