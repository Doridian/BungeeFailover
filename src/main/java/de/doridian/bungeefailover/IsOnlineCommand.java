package de.doridian.bungeefailover;

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
