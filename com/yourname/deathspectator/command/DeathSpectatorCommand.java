package com.yourname.deathspectator.command;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import com.yourname.deathspectator.spectator.SpectatorSession;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeathSpectatorCommand implements CommandExecutor {

    private final DeathSpectatorPlugin plugin;

    public DeathSpectatorCommand(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "reload":
                if (!sender.hasPermission("deathspectator.reload")) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgNoPermission(), 0, null);
                    return true;
                }
                plugin.getConfigManager().loadConfig();
                sender.sendMessage(Component.text("DeathSpectator configuration reloaded successfully.", NamedTextColor.GREEN));
                return true;

            case "version":
                if (!sender.hasPermission("deathspectator.admin")) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgNoPermission(), 0, null);
                    return true;
                }
                sender.sendMessage(Component.text("DeathSpectator v" + plugin.getPluginMeta().getVersion(), NamedTextColor.GOLD));
                sender.sendMessage(Component.text("Target: Paper 1.21.11", NamedTextColor.GRAY));
                sender.sendMessage(Component.text("PlaceholderAPI: " + (plugin.isPapiEnabled() ? "Installed" : "Not installed"), NamedTextColor.GRAY));
                return true;

            case "force":
                if (!sender.hasPermission("deathspectator.force")) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgNoPermission(), 0, null);
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /deathspectator force <player>", NamedTextColor.RED));
                    return true;
                }
                Player targetForce = Bukkit.getPlayer(args[1]);
                if (targetForce == null) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgPlayerNotFound(), 0, null);
                    return true;
                }
                plugin.getSpectatorManager().startSession(targetForce, targetForce.getLocation(), targetForce.getLocation());
                sender.sendMessage(Component.text("Forced death spectator view on " + targetForce.getName(), NamedTextColor.GREEN));
                return true;

            case "stop":
            case "respawn":
                if (!sender.hasPermission("deathspectator.stop") && !sender.hasPermission("deathspectator.respawn")) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgNoPermission(), 0, null);
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /deathspectator " + sub + " <player>", NamedTextColor.RED));
                    return true;
                }
                Player targetStop = Bukkit.getPlayer(args[1]);
                if (targetStop == null) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgPlayerNotFound(), 0, null);
                    return true;
                }
                if (!plugin.getSpectatorManager().isSpectating(targetStop.getUniqueId())) {
                    plugin.getMessageManager().sendMessage(sender instanceof Player ? (Player) sender : null, plugin.getConfigManager().getMsgNotSpectating(), 0, null);
                    return true;
                }
                plugin.getSpectatorManager().endSession(targetStop.getUniqueId(), false, true);
                sender.sendMessage(Component.text("Stopped spectator session for " + targetStop.getName(), NamedTextColor.GREEN));
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("--- DeathSpectator Commands ---", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/deathspectator reload - Reload configuration", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/deathspectator version - Check version and dependencies", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/deathspectator force <player> - Force spectate testing view", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/deathspectator stop <player> - Stop spectator mode", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/deathspectator respawn <player> - Force immediate respawn", NamedTextColor.YELLOW));
    }
}
