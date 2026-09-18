package com.yourname.deathspectator.message;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;

public class MessageManager {

    private final DeathSpectatorPlugin plugin;
    private final MiniMessage miniMessage;

    public MessageManager(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
    }

    public Component parse(Player player, String message, long seconds, Location deathLoc) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }

        String formatted = message
                .replace("<seconds>", String.valueOf(seconds));

        if (player != null) {
            formatted = formatted
                    .replace("<player>", player.getName())
                    .replace("<uuid>", player.getUniqueId().toString());
        }

        if (deathLoc != null && deathLoc.getWorld() != null) {
            formatted = formatted
                    .replace("<world>", deathLoc.getWorld().getName())
                    .replace("<x>", String.valueOf(deathLoc.getBlockX()))
                    .replace("<y>", String.valueOf(deathLoc.getBlockY()))
                    .replace("<z>", String.valueOf(deathLoc.getBlockZ()));
        }

        if (plugin.isPapiEnabled() && player != null) {
            formatted = PlaceholderAPI.setPlaceholders(player, formatted);
        }

        return miniMessage.deserialize(formatted);
    }

    public void sendMessage(Player player, String message, long seconds, Location deathLoc) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        Component component = parse(player, message, seconds, deathLoc);
        if (!component.equals(Component.empty())) {
            player.sendMessage(component);
        }
    }

    public void sendActionBar(Player player, String message, long seconds, Location deathLoc) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }
        Component component = parse(player, message, seconds, deathLoc);
        if (!component.equals(Component.empty())) {
            player.sendActionBar(component);
        }
    }

    public void sendTitle(Player player, String titleStr, String subtitleStr, int fadeInTicks, int stayTicks, int fadeOutTicks, long seconds, Location deathLoc) {
        Component titleComp = parse(player, titleStr, seconds, deathLoc);
        Component subtitleComp = parse(player, subtitleStr, seconds, deathLoc);

        Title.Times times = Title.Times.times(
                Duration.ofMillis(fadeInTicks * 50L),
                Duration.ofMillis(stayTicks * 50L),
                Duration.ofMillis(fadeOutTicks * 50L)
        );

        Title title = Title.title(titleComp, subtitleComp, times);
        player.showTitle(title);
    }
}
