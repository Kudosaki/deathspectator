package com.yourname.deathspectator.spectator;

import com.yourname.deathspectator.DeathSpectatorPlugin;

public class AnchorFactory {

    private final DeathSpectatorPlugin plugin;

    public AnchorFactory(DeathSpectatorPlugin plugin) {
        this.plugin = plugin;
    }

    public SpectatorAnchor createAnchor() {
        return new ArmorStandSpectatorAnchor(plugin);
    }
}
