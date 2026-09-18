package com.yourname.deathspectator.config;

import org.bukkit.entity.EntityType;
import java.util.logging.Logger;

public class ConfigValidator {

    private final Logger logger;

    public ConfigValidator(Logger logger) {
        this.logger = logger;
    }

    public int validateDelay(int delay) {
        if (delay < 0) {
            logger.warning("Config validation: death.respawn.delay cannot be negative (" + delay + "). Defaulting to 3.");
            return 3;
        }
        return delay;
    }

    public EntityType validateEntityType(String typeName) {
        try {
            EntityType type = EntityType.valueOf(typeName.toUpperCase());
            if (!type.isSpawnable()) {
                logger.warning("Config validation: EntityType " + typeName + " is not spawnable. Defaulting to ARMOR_STAND.");
                return EntityType.ARMOR_STAND;
            }
            return type;
        } catch (Exception e) {
            logger.warning("Config validation: Invalid EntityType '" + typeName + "'. Defaulting to ARMOR_STAND.");
            return EntityType.ARMOR_STAND;
        }
    }

    public String validateDisplayType(String displayType) {
        if (displayType == null) return "ACTION_BAR";
        String upper = displayType.toUpperCase();
        switch (upper) {
            case "ACTION_BAR":
            case "TITLE":
            case "CHAT":
            case "NONE":
                return upper;
            default:
                logger.warning("Config validation: Invalid countdown display '" + displayType + "'. Defaulting to ACTION_BAR.");
                return "ACTION_BAR";
        }
    }
}
