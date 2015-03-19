package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem.DriveMode;

/**
 * An action that will move the robot straight using PID
 */
public class PIDStraightAction extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    double setpoint;
    double tolerance;
    
    /**
     * Make a new PIDStraightAction
     * @param driveSubsystem the DriveSubsystem instance to use
     * @param setpoint the distance to travel
     * @param tolerance how close to the desired distance we need to be
     */
    public PIDStraightAction(DriveSubsystem driveSubsystem, double setpoint,
            double tolerance) {
        this.driveSubsystem = driveSubsystem;
        this.setpoint = setpoint;
        this.tolerance = tolerance;
    }

    @Override
    public void startCode() {
        driveSubsystem.setStraightPIDSetpoint(setpoint);
        driveSubsystem.setDriveMode(DriveMode.PIDSTRAIGHT);
    }

    @Override
    public EndState periodicCode() {
        if (driveSubsystem.pidOnTarget(tolerance)) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}
