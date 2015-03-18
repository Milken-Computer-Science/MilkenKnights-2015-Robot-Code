package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem.DriveMode;

/** An action that will pivot the robot to a certain angle using PID and Gyro */
public class PIDPivotAction extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    double setpoint;
    double tolerance;
    
    /**
     * Make a new PIDPivotAction.
     * @param driveSubsystem the DriveSubsystem instance to use
     * @param setpoint the angle, in degrees, that we want to go to
     * @param tolerance how close to the desired angle we need to be
     */
    public PIDPivotAction(DriveSubsystem driveSubsystem, double setpoint,
            double tolerance) {
        this.driveSubsystem = driveSubsystem;
        
        // bound setpoint between -180 and 180
        this.setpoint = ((setpoint + 180) % 360) - 180;
        
        this.tolerance = tolerance;
    }

    @Override
    public void startCode() {
        driveSubsystem.setPivotPIDSetpoint(setpoint);
        driveSubsystem.setDriveMode(DriveMode.PIDPIVOT);
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
