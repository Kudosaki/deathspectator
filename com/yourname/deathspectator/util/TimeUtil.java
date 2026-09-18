package com.yourname.deathspectator.util;

public final class TimeUtil {

    private TimeUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static long calculateRemainingSeconds(long endTimeMs) {
        long remainingMs = endTimeMs - System.currentTimeMillis();
        if (remainingMs <= 0) {
            return 0;
        }
        return (long) Math.ceil(remainingMs / 1000.0);
    }
}
