package com.yourname.deathspectator;

import com.yourname.deathspectator.command.DeathSpectatorCommand;
import com.yourname.deathspectator.command.DeathSpectatorTabCompleter;
import com.yourname.deathspectator.config.ConfigManager;
import com.yourname.deathspectator.hook.PlaceholderAPIHook;
import com.yourname.deathspectator.listener.PlayerDeathListener;
import com.yourname.deathspectator.listener.PlayerInputListener;
import com.yourname.deathspectator.listener.PlayerQuitListener;
import com.yourname.deathspectator.listener.PlayerRespawnListener;
import com.yourname.deathspectator.message.MessageManager;
import com.yourname.deathspectator.spectator.DeathSpectatorManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathSpectatorPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private MessageManager messageManager;
    private DeathSpectatorManager spectatorManager;
    private boolean papiEnabled;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();

        this.messageManager = new MessageManager(this);
        this.spectatorManager = new DeathSpectatorManager(this);

        this.spectatorManager.cleanOrphanEntities();

        if (getConfigManager().isHookPapi() && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.papiEnabled = new PlaceholderAPIHook(this).register();
            if (this.papiEnabled) {
                getLogger().info("PlaceholderAPI detected and hooked successfully.");
            }
        }

        registerListeners();
        registerCommands();

        getLogger().info("DeathSpectator v" + getPluginMeta().getVersion() + " has been enabled.");
    }

    @Override
    public void onDisable() {
        if (spectatorManager != null) {
            spectatorManager.stopAllSessions();
        }
        getLogger().info("DeathSpectator has been disabled.");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInputListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    private void registerCommands() {
        PluginCommand cmd = getCommand("deathspectator");
        if (cmd != null) {
            cmd.setExecutor(new DeathSpectatorCommand(this));
            cmd.setTabCompleter(new DeathSpectatorTabCompleter());
        }
    }

    public ConfigManager getConfigManager() { return configManager; }
    public MessageManager getMessageManager() { return messageManager; }
    public DeathSpectatorManager getSpectatorManager() { return spectatorManager; }
    public boolean isPapiEnabled() { return papiEnabled; }
}
