package com.milkenknights.common;

import edu.wpi.first.wpilibj.Utility;

public class MTimer {

    long startTime;
    boolean running;
    
    public MTimer() {
        running = false;
        startTime = -1;
    }
    
    public void start() {
        startTime = Utility.getFPGATime();
        running = true;
    }
    
    public void stop() {
        running = false;
        startTime = -1;
    }
    
    /**
     * Checks if the timer is already running.  If it is not running -> start
     */
    public void safeStart() {
        if (getRunning()) {
            start();
        }
    }
    
    /**
     * Has the period passed
     * @param i The period in seconds
     * @return if the period has passed
     */
    public boolean hasPeriodPassed(long i) {
        if (running) {
            return startTime + (i * 1000000) <= Utility.getFPGATime();
        } else {
            return false;
        }
    }
    
    public boolean getRunning() {
        return running;
    }
}
