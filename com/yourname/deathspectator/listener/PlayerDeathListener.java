package com.yourname.deathspectator.listener;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final DeathSpectatorPlugin plugin;

    public PlayerDeathListener(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getConfigManager().getRespawnDelay() <= 0) {
            return;
        }

        plugin.getSpectatorManager().markPendingDeath(player.getUniqueId(), player.getLocation());

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (player.isOnline() && player.isDead()) {
                player.spigot().respawn();
            }
        });
    }
}
