package com.yourname.deathspectator.spectator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class SpectatorSession {

    private final UUID playerUuid;
    private final Location deathLocation;
    private Location respawnLocation;
    private final SpectatorAnchor anchor;
    private final long startTimeMs;
    private final long endTimeMs;
    private final GameMode previousGameMode;
    private BukkitTask task;
    private boolean ended;

    public SpectatorSession(UUID playerUuid, Location deathLocation, Location respawnLocation, SpectatorAnchor anchor, long durationSeconds, GameMode previousGameMode) {
        this.playerUuid = playerUuid;
        this.deathLocation = deathLocation;
        this.respawnLocation = respawnLocation;
        this.anchor = anchor;
        this.startTimeMs = System.currentTimeMillis();
        this.endTimeMs = startTimeMs + (durationSeconds * 1000L);
        this.previousGameMode = previousGameMode != null ? previousGameMode : GameMode.SURVIVAL;
        this.ended = false;
    }

    public UUID getPlayerUuid() { return playerUuid; }
    public Location getDeathLocation() { return deathLocation; }
    public Location getRespawnLocation() { return respawnLocation; }
    public void setRespawnLocation(Location respawnLocation) { this.respawnLocation = respawnLocation; }
    public SpectatorAnchor getAnchor() { return anchor; }
    public long getStartTimeMs() { return startTimeMs; }
    public long getEndTimeMs() { return endTimeMs; }
    public GameMode getPreviousGameMode() { return previousGameMode; }
    public BukkitTask getTask() { return task; }
    public void setTask(BukkitTask task) { this.task = task; }
    public boolean isEnded() { return ended; }
    public void setEnded(boolean ended) { this.ended = ended; }
}
