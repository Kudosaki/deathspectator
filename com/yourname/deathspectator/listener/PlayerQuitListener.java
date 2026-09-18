package com.yourname.deathspectator.listener;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final DeathSpectatorPlugin plugin;

    public PlayerQuitListener(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getSpectatorManager().endSession(event.getPlayer().getUniqueId(), false, true);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        plugin.getSpectatorManager().endSession(event.getPlayer().getUniqueId(), false, true);
    }
}
