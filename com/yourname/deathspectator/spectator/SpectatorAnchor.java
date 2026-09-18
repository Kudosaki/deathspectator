package com.yourname.deathspectator.spectator;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface SpectatorAnchor {
    void spawn(Location location);
    void remove();
    boolean isValid();
    Entity getEntity();
    Location getLocation();
}
