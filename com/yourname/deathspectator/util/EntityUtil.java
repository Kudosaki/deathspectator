package com.yourname.deathspectator.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public final class EntityUtil {

    private EntityUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static NamespacedKey getAnchorKey(Plugin plugin) {
        return new NamespacedKey(plugin, "spectator_anchor");
    }

    public static void markAsAnchor(Plugin plugin, Entity entity) {
        entity.getPersistentDataContainer().set(getAnchorKey(plugin), PersistentDataType.BYTE, (byte) 1);
    }

    public static boolean isAnchor(Plugin plugin, Entity entity) {
        if (entity == null) {
            return false;
        }
        return entity.getPersistentDataContainer().has(getAnchorKey(plugin), PersistentDataType.BYTE);
    }
}
