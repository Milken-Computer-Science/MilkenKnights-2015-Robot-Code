package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.Subsystems;

/** Waits until we are moving at a certain speed. */
public class WaitForDriveSpeed extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    
    double speed;
    boolean waitForGreater;
    
    /**
     * Make a new WaitForDriveSpeed.
     * @param driveSubsystem The DriveSubsystem to use
     * @param dist The speed to wait for, in inches per second.
     * @param waitForGreater True if we should wait until we are greater than
     *                       the speed. False if we should wait until we are
     *                       less than the speed.
     */
    public WaitForDriveSpeed(Subsystems subsystems, double speed,
            boolean waitForGreater) {
        this.driveSubsystem = subsystems.drive();
        
        this.speed = speed;
        this.waitForGreater = waitForGreater;
    }

    @Override
    protected void startCode() {}

    @Override
    protected EndState periodicCode() {
        if ((driveSubsystem.getEncSpeed() > speed) == waitForGreater) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}
