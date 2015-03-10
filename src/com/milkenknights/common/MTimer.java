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
    
    public boolean hasPeriodPassed(long i) {
        if (running) {
            return startTime + i <= Utility.getFPGATime();
        } else {
            return false;
        }
    }
    
    public boolean getRunning() {
        return running;
    }
}
