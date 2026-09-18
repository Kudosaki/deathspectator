package com.yourname.deathspectator.listener;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final DeathSpectatorPlugin plugin;

    public PlayerRespawnListener(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location deathLoc = plugin.getSpectatorManager().getAndRemovePendingDeath(player.getUniqueId());

        if (deathLoc != null) {
            Location respawnLoc = event.getRespawnLocation().clone();
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (player.isOnline()) {
                    plugin.getSpectatorManager().startSession(player, deathLoc, respawnLoc);
                }
            });
        }
    }
}
