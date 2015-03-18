package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem.DriveMode;

/**
 * The same as PIDStraightAction, but backgrounds immediately after starting
 * the PID loop.
 */
public class PIDStraightBackground extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    double setpoint;
    double tolerance;
    
    boolean firstLoop;
    
    /**
     * Make a new PIDStraightBackground
     * @param driveSubsystem the DriveSubsystem instance to use
     * @param setpoint the distance to travel
     * @param tolerance how close to the desired distance we need to be
     */
    public PIDStraightBackground(DriveSubsystem driveSubsystem, double setpoint,
            double tolerance) {
        this.driveSubsystem = driveSubsystem;
        this.setpoint = setpoint;
        this.tolerance = tolerance;
    }
    
    @Override
    protected void startCode() {
        driveSubsystem.resetStraightPIDPosition();
        driveSubsystem.setStraightPIDSetpoint(setpoint);
        driveSubsystem.setDriveMode(DriveMode.PIDSTRAIGHT);
        firstLoop = true;
    }

    @Override
    protected EndState periodicCode() {
        if (driveSubsystem.pidOnTarget(tolerance)) {
            return EndState.END;
        } else if (firstLoop) {
            firstLoop = false;
            return EndState.BACKGROUND;
        } else {
            return EndState.CONTINUE;
        }
    }

}
