package com.yourname.deathspectator.listener;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import com.yourname.deathspectator.spectator.SpectatorSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;

public class PlayerInputListener implements Listener {

    private final DeathSpectatorPlugin plugin;

    public PlayerInputListener(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!event.isSneaking()) {
            return;
        }

        if (!plugin.getConfigManager().isSneakToSkip()) {
            return;
        }

        if (plugin.getSpectatorManager().isSpectating(player.getUniqueId())) {
            plugin.getSpectatorManager().endSession(player.getUniqueId(), true, true);
        }
    }

    @EventHandler
    public void onStopSpectating(PlayerStopSpectatingEntityEvent event) {
        Player player = event.getPlayer();
        SpectatorSession session = plugin.getSpectatorManager().getSession(player.getUniqueId());

        if (session != null && !session.isEnded()) {
            if (event.getSpectatorTarget().equals(session.getAnchor().getEntity())) {
                if (plugin.getConfigManager().isSneakToSkip()) {
                    plugin.getSpectatorManager().endSession(player.getUniqueId(), true, true);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
