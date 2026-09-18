package com.yourname.deathspectator.config;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class ConfigManager {

    private final DeathSpectatorPlugin plugin;
    private final ConfigValidator validator;

    private boolean debug;
    private EntityType entityType;
    private boolean entityInvisible;
    private boolean entityInvulnerable;
    private boolean entityGravity;
    private boolean entityMarker;
    private boolean entitySmall;

    private double offsetX;
    private double offsetY;
    private double offsetZ;

    private double targetX;
    private double targetY;
    private double targetZ;

    private boolean lookEnabled;
    private double yawOffset;
    private double pitchOffset;

    private int respawnDelay;
    private boolean sneakToSkip;
    private boolean automaticRespawn;

    private boolean countdownEnabled;
    private String countdownDisplay;
    private String countdownMessage;
    private String countdownTitle;
    private String countdownSubtitle;

    private String deathTitle;
    private String deathSubtitle;
    private int titleFadeIn;
    private int titleStay;
    private int titleFadeOut;
    private String deathChat;

    private String msgEntered;
    private String msgSkipped;
    private String msgRespawned;
    private String msgExited;
    private String msgNoPermission;
    private String msgPlayerNotFound;
    private String msgNotSpectating;

    private boolean hookPapi;

    public ConfigManager(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
        this.validator = new ConfigValidator(plugin.getLogger());
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        this.debug = config.getBoolean("plugin.debug", false);

        this.entityType = validator.validateEntityType(config.getString("spectator.entity.type", "ARMOR_STAND"));
        this.entityInvisible = config.getBoolean("spectator.entity.invisible", true);
        this.entityInvulnerable = config.getBoolean("spectator.entity.invulnerable", true);
        this.entityGravity = config.getBoolean("spectator.entity.gravity", false);
        this.entityMarker = config.getBoolean("spectator.entity.marker", true);
        this.entitySmall = config.getBoolean("spectator.entity.small", false);

        this.offsetX = config.getDouble("spectator.position.offset-x", 0.0);
        this.offsetY = config.getDouble("spectator.position.offset-y", 2.2);
        this.offsetZ = config.getDouble("spectator.position.offset-z", 0.0);

        this.targetX = config.getDouble("spectator.target.offset-x", 0.0);
        this.targetY = config.getDouble("spectator.target.offset-y", 1.0);
        this.targetZ = config.getDouble("spectator.target.offset-z", 0.0);

        this.lookEnabled = config.getBoolean("spectator.look.enabled", true);
        this.yawOffset = config.getDouble("spectator.look.yaw-offset", 0.0);
        this.pitchOffset = config.getDouble("spectator.look.pitch-offset", 0.0);

        this.respawnDelay = validator.validateDelay(config.getInt("death.respawn.delay", 3));
        this.sneakToSkip = config.getBoolean("death.respawn.sneak-to-skip", true);
        this.automaticRespawn = config.getBoolean("death.respawn.automatic-respawn", true);

        this.countdownEnabled = config.getBoolean("death.respawn.countdown.enabled", true);
        this.countdownDisplay = validator.validateDisplayType(config.getString("death.respawn.countdown.display", "ACTION_BAR"));
        this.countdownMessage = config.getString("death.respawn.countdown.message", "<gray>Respawning in <yellow><seconds></yellow><gray>...");
        this.countdownTitle = config.getString("death.respawn.countdown.title", "<red>You Died");
        this.countdownSubtitle = config.getString("death.respawn.countdown.subtitle", "<gray>Respawning in <yellow><seconds></yellow>s...");

        this.deathTitle = config.getString("messages.death.title", "<red>You Died");
        this.deathSubtitle = config.getString("messages.death.subtitle", "<gray>Your journey ends here...");
        this.titleFadeIn = config.getInt("messages.death.fade-in", 10);
        this.titleStay = config.getInt("messages.death.stay", 50);
        this.titleFadeOut = config.getInt("messages.death.fade-out", 20);
        this.deathChat = config.getString("messages.death.chat", "");

        this.msgEntered = config.getString("messages.spectator.entered", "");
        this.msgSkipped = config.getString("messages.spectator.skipped", "<yellow>Respawn countdown skipped.");
        this.msgRespawned = config.getString("messages.spectator.respawned", "");
        this.msgExited = config.getString("messages.spectator.exited", "");
        this.msgNoPermission = config.getString("messages.errors.no-permission", "<red>You do not have permission.");
        this.msgPlayerNotFound = config.getString("messages.errors.player-not-found", "<red>Player not found.");
        this.msgNotSpectating = config.getString("messages.errors.not-spectating", "<red>Player is not spectating.");

        this.hookPapi = config.getBoolean("hooks.placeholderapi", true);
    }

    public boolean isDebug() { return debug; }
    public EntityType getEntityType() { return entityType; }
    public boolean isEntityInvisible() { return entityInvisible; }
    public boolean isEntityInvulnerable() { return entityInvulnerable; }
    public boolean isEntityGravity() { return entityGravity; }
    public boolean isEntityMarker() { return entityMarker; }
    public boolean isEntitySmall() { return entitySmall; }
    public double getOffsetX() { return offsetX; }
    public double getOffsetY() { return offsetY; }
    public double getOffsetZ() { return offsetZ; }
    public double getTargetX() { return targetX; }
    public double getTargetY() { return targetY; }
    public double getTargetZ() { return targetZ; }
    public boolean isLookEnabled() { return lookEnabled; }
    public double getYawOffset() { return yawOffset; }
    public double getPitchOffset() { return pitchOffset; }
    public int getRespawnDelay() { return respawnDelay; }
    public boolean isSneakToSkip() { return sneakToSkip; }
    public boolean isAutomaticRespawn() { return automaticRespawn; }
    public boolean isCountdownEnabled() { return countdownEnabled; }
    public String getCountdownDisplay() { return countdownDisplay; }
    public String getCountdownMessage() { return countdownMessage; }
    public String getCountdownTitle() { return countdownTitle; }
    public String getCountdownSubtitle() { return countdownSubtitle; }
    public String getDeathTitle() { return deathTitle; }
    public String getDeathSubtitle() { return deathSubtitle; }
    public int getTitleFadeIn() { return titleFadeIn; }
    public int getTitleStay() { return titleStay; }
    public int getTitleFadeOut() { return titleFadeOut; }
    public String getDeathChat() { return deathChat; }
    public String getMsgEntered() { return msgEntered; }
    public String getMsgSkipped() { return msgSkipped; }
    public String getMsgRespawned() { return msgRespawned; }
    public String getMsgExited() { return msgExited; }
    public String getMsgNoPermission() { return msgNoPermission; }
    public String getMsgPlayerNotFound() { return msgPlayerNotFound; }
    public String getMsgNotSpectating() { return msgNotSpectating; }
    public boolean isHookPapi() { return hookPapi; }
}
