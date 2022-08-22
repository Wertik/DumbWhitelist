package space.devport.wertik;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {

    private final WhitelistPlugin plugin;

    public PlayerListener(WhitelistPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(final PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Allow OP at all times
        if (player.isOp() || player.hasPermission("dumbwhitelist.bypass")) {
            return;
        }

        if (plugin.isWhitelisted(player.getName())) {
            return;
        }

        event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, plugin.getSerializer().deserialize("<red>You're not whitelisted!"));
    }

    /*@EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(plugin.getSerializer().deserialize("<aqua>Odkaz na mapu světa: <pink><a:https://map.devport.space/>here!</a>"));
    }*/
}
