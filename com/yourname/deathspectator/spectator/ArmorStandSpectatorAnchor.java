package com.yourname.deathspectator.spectator;

import com.yourname.deathspectator.DeathSpectatorPlugin;
import com.yourname.deathspectator.util.EntityUtil;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class ArmorStandSpectatorAnchor implements SpectatorAnchor {

    private final DeathSpectatorPlugin plugin;
    private ArmorStand armorStand;

    public ArmorStandSpectatorAnchor(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void spawn(Location location) {
        if (location == null || location.getWorld() == null) {
            return;
        }
        this.armorStand = location.getWorld().spawn(location, ArmorStand.class, stand -> {
            stand.setInvisible(plugin.getConfigManager().isEntityInvisible());
            stand.setInvulnerable(plugin.getConfigManager().isEntityInvulnerable());
            stand.setGravity(plugin.getConfigManager().isEntityGravity());
            stand.setMarker(plugin.getConfigManager().isEntityMarker());
            stand.setSmall(plugin.getConfigManager().isEntitySmall());
            stand.setPersistent(false);
            stand.setCustomNameVisible(false);
            EntityUtil.markAsAnchor(plugin, stand);
        });
    }

    @Override
    public void remove() {
        if (armorStand != null && armorStand.isValid()) {
            armorStand.remove();
        }
        armorStand = null;
    }

    @Override
    public boolean isValid() {
        return armorStand != null && armorStand.isValid();
    }

    @Override
    public Entity getEntity() {
        return armorStand;
    }

    @Override
    public Location getLocation() {
        return armorStand != null ? armorStand.getLocation() : null;
    }
}
