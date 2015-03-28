package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem.DriveMode;
import com.milkenknights.frc2015.subsystems.Subsystems;

/**
 * An action that will move the robot straight using PID
 */
public class PIDStraightAction extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    double setpoint;
    double speedLimit;
    double tolerance;
    
    /**
     * Make a new PIDStraightAction
     * @param driveSubsystem the DriveSubsystem instance to use
     * @param setpoint the distance to travel
     * @param speedLimit the speed limit
     * @param tolerance how close to the desired distance we need to be
     */
    public PIDStraightAction(Subsystems subsystems, double setpoint, double speedLimit,
            double tolerance) {
        this.driveSubsystem = subsystems.drive();
        this.setpoint = setpoint;
        this.speedLimit = speedLimit;
        this.tolerance = tolerance;
    }

    /**
     * Make a new PIDStraightAction
     * @param driveSubsystem the DriveSubsystem instance to use
     * @param setpoint the distance to travel
     * @param tolerance how close to the desired distance we need to be
     */
    public PIDStraightAction(Subsystems subsystems, double setpoint, double tolerance) {
        this(subsystems, setpoint, 1, tolerance);
    }

    
    @Override
    public void startCode() {
        driveSubsystem.setStraightPIDSetpoint(setpoint, speedLimit);
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
