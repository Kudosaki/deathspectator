package com.yourname.deathspectator.spectator;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import com.yourname.deathspectator.config.ConfigManager;
import com.yourname.deathspectator.util.EntityUtil;
import com.yourname.deathspectator.util.LocationUtil;
import com.yourname.deathspectator.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DeathSpectatorManager {

    private final DeathSpectatorPlugin plugin;
    private final Map<UUID, SpectatorSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<UUID, Location> pendingDeathLocations = new ConcurrentHashMap<>();
    private final AnchorFactory anchorFactory;

    public DeathSpectatorManager(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
        this.anchorFactory = new AnchorFactory(plugin);
    }

    public void markPendingDeath(UUID uuid, Location deathLoc) {
        pendingDeathLocations.put(uuid, deathLoc);
    }

    public Location getAndRemovePendingDeath(UUID uuid) {
        return pendingDeathLocations.remove(uuid);
    }

    public void startSession(Player player, Location deathLoc, Location respawnLoc) {
        ConfigManager cfg = plugin.getConfigManager();
        if (cfg.getRespawnDelay() <= 0) {
            if (cfg.isDebug()) {
                plugin.getLogger().info("Respawn delay is 0. Bypassing spectator mode for " + player.getName());
            }
            return;
        }

        endSession(player.getUniqueId(), false, false);

        Location cameraLoc = LocationUtil.calculateCameraLocation(
                deathLoc,
                cfg.getOffsetX(), cfg.getOffsetY(), cfg.getOffsetZ(),
                cfg.getTargetX(), cfg.getTargetY(), cfg.getTargetZ(),
                cfg.getYawOffset(), cfg.getPitchOffset()
        );

        SpectatorAnchor anchor = anchorFactory.createAnchor();
        anchor.spawn(cameraLoc);

        if (!anchor.isValid()) {
            plugin.getLogger().severe("Failed to spawn camera anchor for " + player.getName());
            return;
        }

        GameMode prevMode = player.getGameMode() == GameMode.SPECTATOR ? GameMode.SURVIVAL : player.getGameMode();
        SpectatorSession session = new SpectatorSession(
                player.getUniqueId(),
                deathLoc,
                respawnLoc,
                anchor,
                cfg.getRespawnDelay(),
                prevMode
        );

        player.setGameMode(GameMode.SPECTATOR);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline() && anchor.isValid()) {
                player.setSpectatorTarget(anchor.getEntity());
            }
        }, 1L);

        plugin.getMessageManager().sendTitle(
                player,
                cfg.getDeathTitle(),
                cfg.getDeathSubtitle(),
                cfg.getTitleFadeIn(),
                cfg.getTitleStay(),
                cfg.getTitleFadeOut(),
                cfg.getRespawnDelay(),
                deathLoc
        );

        plugin.getMessageManager().sendMessage(player, cfg.getDeathChat(), cfg.getRespawnDelay(), deathLoc);
        plugin.getMessageManager().sendMessage(player, cfg.getMsgEntered(), cfg.getRespawnDelay(), deathLoc);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    endSession(player.getUniqueId(), false, false);
                    cancel();
                    return;
                }

                if (!anchor.isValid()) {
                    if (cfg.isDebug()) {
                        plugin.getLogger().warning("Anchor missing for " + player.getName() + ". Ending session.");
                    }
                    endSession(player.getUniqueId(), false, true);
                    cancel();
                    return;
                }

                long remainingSec = TimeUtil.calculateRemainingSeconds(session.getEndTimeMs());

                if (cfg.isCountdownEnabled()) {
                    String display = cfg.getCountdownDisplay();
                    switch (display) {
                        case "ACTION_BAR":
                            plugin.getMessageManager().sendActionBar(player, cfg.getCountdownMessage(), remainingSec, deathLoc);
                            break;
                        case "TITLE":
                            plugin.getMessageManager().sendTitle(player, cfg.getCountdownTitle(), cfg.getCountdownSubtitle(), 0, 20, 10, remainingSec, deathLoc);
                            break;
                        case "CHAT":
                            if (remainingSec > 0) {
                                plugin.getMessageManager().sendMessage(player, cfg.getCountdownMessage(), remainingSec, deathLoc);
                            }
                            break;
                    }
                }

                if (remainingSec <= 0) {
                    if (cfg.isAutomaticRespawn()) {
                        endSession(player.getUniqueId(), false, true);
                    }
                    cancel();
                }
            }
        };

        session.setTask(runnable.runTaskTimer(plugin, 0L, 10L));
        activeSessions.put(player.getUniqueId(), session);

        if (cfg.isDebug()) {
            plugin.getLogger().info("Started spectator session for " + player.getName());
        }
    }

    public void endSession(UUID uuid, boolean skipped, boolean respawnPlayer) {
        SpectatorSession session = activeSessions.remove(uuid);
        if (session == null) {
            return;
        }

        session.setEnded(true);

        if (session.getTask() != null) {
            session.getTask().cancel();
        }

        if (session.getAnchor() != null) {
            session.getAnchor().remove();
        }

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            player.setSpectatorTarget(null);
            player.setGameMode(session.getPreviousGameMode());

            if (respawnPlayer) {
                Location respawnLoc = session.getRespawnLocation();
                if (respawnLoc != null && respawnLoc.getWorld() != null) {
                    player.teleport(respawnLoc);
                }
            }

            ConfigManager cfg = plugin.getConfigManager();
            if (skipped) {
                plugin.getMessageManager().sendMessage(player, cfg.getMsgSkipped(), 0, session.getDeathLocation());
            } else {
                plugin.getMessageManager().sendMessage(player, cfg.getMsgExited(), 0, session.getDeathLocation());
            }
        }

        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("Ended spectator session for UUID " + uuid + " (skipped: " + skipped + ")");
        }
    }

    public void stopAllSessions() {
        for (UUID uuid : activeSessions.keySet()) {
            endSession(uuid, false, true);
        }
        activeSessions.clear();
        pendingDeathLocations.clear();
    }

    public boolean isSpectating(UUID uuid) {
        return activeSessions.containsKey(uuid);
    }

    public SpectatorSession getSession(UUID uuid) {
        return activeSessions.get(uuid);
    }

    public void cleanOrphanEntities() {
        int cleaned = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (EntityUtil.isAnchor(plugin, entity)) {
                    entity.remove();
                    cleaned++;
                }
            }
        }
        if (cleaned > 0) {
            plugin.getLogger().info("Cleaned up " + cleaned + " orphaned spectator anchor entities.");
        }
    }
}
