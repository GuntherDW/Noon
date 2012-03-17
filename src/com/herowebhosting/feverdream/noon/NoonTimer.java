package com.herowebhosting.feverdream.noon;

import java.util.Timer;

/**
 * @author GuntherDW
 */
public class NoonTimer {
    private Timer timertask;
    private SunShine sunshine;

    public NoonTimer(Timer timertask, SunShine sunshine) {
        this.timertask = timertask;
        this.sunshine = sunshine;
    }

    public Timer getTimertask() {
        return timertask;
    }

    public void setTimertask(Timer timertask) {
        this.timertask = timertask;
    }

    public SunShine getSunshine() {
        return sunshine;
    }

    public void setSunshine(SunShine sunshine) {
        this.sunshine = sunshine;
    }
}
