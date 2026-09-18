package com.yourname.deathspectator.hook;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import com.yourname.deathspectator.spectator.SpectatorSession;
import com.yourname.deathspectator.util.TimeUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final DeathSpectatorPlugin plugin;

    public PlaceholderAPIHook(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "deathspectator";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().isEmpty() ? "yourname" : plugin.getPluginMeta().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        boolean active = plugin.getSpectatorManager().isSpectating(player.getUniqueId());

        switch (params.toLowerCase()) {
            case "active":
                return String.valueOf(active);

            case "time_remaining":
                if (!active) return "0";
                SpectatorSession session = plugin.getSpectatorManager().getSession(player.getUniqueId());
                return session != null ? String.valueOf(TimeUtil.calculateRemainingSeconds(session.getEndTimeMs())) : "0";

            case "world":
                if (!active) return "";
                SpectatorSession sWorld = plugin.getSpectatorManager().getSession(player.getUniqueId());
                return (sWorld != null && sWorld.getDeathLocation().getWorld() != null) ? sWorld.getDeathLocation().getWorld().getName() : "";

            default:
                return null;
        }
    }
}
