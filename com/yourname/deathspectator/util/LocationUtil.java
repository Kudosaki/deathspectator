package com.yourname.deathspectator.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class LocationUtil {

    private LocationUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Location calculateCameraLocation(
            Location deathLoc,
            double posX, double posY, double posZ,
            double targetX, double targetY, double targetZ,
            double yawOffset, double pitchOffset
    ) {
        Location cameraLoc = deathLoc.clone().add(posX, posY, posZ);
        Location targetLoc = deathLoc.clone().add(targetX, targetY, targetZ);

        Vector direction = targetLoc.toVector().subtract(cameraLoc.toVector());
        if (direction.lengthSquared() > 0.0001) {
            cameraLoc.setDirection(direction);
        }

        cameraLoc.setYaw((float) (cameraLoc.getYaw() + yawOffset));
        cameraLoc.setPitch((float) (cameraLoc.getPitch() + pitchOffset));

        return cameraLoc;
    }
}
