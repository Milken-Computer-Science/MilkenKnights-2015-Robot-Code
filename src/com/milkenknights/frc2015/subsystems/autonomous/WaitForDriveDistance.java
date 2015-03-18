package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;

/**
 * Waits until we have drove past a certain distance. A good complement to
 * PIDStraightBackground.
 */
public class WaitForDriveDistance extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    
    double dist;
    boolean waitForGreater;
    
    /**
     * Make a new WaitForDriveDistance.
     * @param driveSubsystem The DriveSubsystem to use
     * @param dist The distance to wait for
     * @param waitForGreater True if we should wait until we are greater than
     *                       the distance. False if we should wait until we are
     *                       less than the distance.
     */
    public WaitForDriveDistance(DriveSubsystem driveSubsystem, double dist,
            boolean waitForGreater) {
        this.driveSubsystem = driveSubsystem;
        
        this.dist = dist;
        this.waitForGreater = waitForGreater;
    }

    @Override
    protected void startCode() {}

    @Override
    protected EndState periodicCode() {
        if ((driveSubsystem.getEncPosition() > dist) == waitForGreater) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}
