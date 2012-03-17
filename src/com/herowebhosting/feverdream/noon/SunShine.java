package com.herowebhosting.feverdream.noon;

import org.bukkit.World;

import java.util.TimerTask;
import java.util.logging.Logger;

public class SunShine extends TimerTask {
    public long wantedTime = 0L;
    public World clock = null;
    private Logger log = Logger.getLogger("Minecraft");
    public boolean enable = true;

    public SunShine(World w) {
        this.clock = w;
    }

    public void setWantedTime(long time) {
        this.wantedTime = time;
    }

    public void run() {
        long time = this.clock.getTime();
        long relativeTime = time;
        long dayStart = wantedTime;
        try {

            if ((relativeTime > 12000L) && (dayStart == 0)) {
                this.clock.setTime(0L);
                // log.log(Level.INFO, "[Noon] Setting time for "+this.clock.getName());
                /* log.log(Level.INFO, "Time is now "+this.clock.getTime()); */
            } else if ((relativeTime > 13800L) && (dayStart == 12000)) {
                this.clock.setTime(12000L);
                // log.log(Level.INFO, "[Noon] Setting time for "+this.clock.getName());
                /* log.log(Level.INFO, "Time is now "+this.clock.getTime()); */

            } else if ((relativeTime > 22200L) && (dayStart == 13800)) {
                this.clock.setTime(13800L);
                // log.log(Level.INFO, "[Noon] Setting time for "+this.clock.getName());
                /* log.log(Level.INFO, "Time is now "+this.clock.getTime()); */

            } else if ((relativeTime > 0L) && (dayStart == 22200)) {
                this.clock.setTime(22200L);
                // log.log(Level.INFO, "[Noon] Setting time for "+this.clock.getName());
                /* log.log(Level.INFO, "Time is now "+this.clock.getTime()); */

            } else if ((relativeTime > 13500L) && (dayStart == 21900)) {
                if (!(relativeTime > 21900)) {
                    this.clock.setTime(21900L);
                    // log.log(Level.INFO, "[Noon] Setting time for "+this.clock.getName());
                    /* log.log(Level.INFO, "Time is now "+this.clock.getTime()); */
                }

            }

        } catch (Exception ex) {
            log.severe("[NOON] A Bukkit Bug created an exception.  This means that bukkit is still not thread safe.:");
            log.severe(ex.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SunShine sunShine = (SunShine) o;

        if (clock != null ? !clock.equals(sunShine.clock) : sunShine.clock != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return clock != null ? clock.hashCode() : 0;
    }
}