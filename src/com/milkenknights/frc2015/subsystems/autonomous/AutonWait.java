package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;

import edu.wpi.first.wpilibj.Timer;

/** This AutonomousAction stalls for a given amount of time. */
public class AutonWait extends AutonomousAction {
    double startTime;
    double waitTime;

    /**
     * Create a new AutonWait.
     * @param time The time to wait, in seconds.
     */
    public AutonWait(double time) {
        waitTime = time;
    }

    @Override
    public void startCode() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public EndState periodicCode() {
        if (Timer.getFPGATimestamp() - startTime >= waitTime) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}