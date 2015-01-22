package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;

import edu.wpi.first.wpilibj.Timer;

/** This AutonomousAction stalls for a given amount of time. */
class AutonWait extends AutonomousAction {
    double startTime;
    double waitTime;

    public AutonWait(double time) {
        waitTime = time;
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public EndState run() {
        if (Timer.getFPGATimestamp() - startTime >= waitTime) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}