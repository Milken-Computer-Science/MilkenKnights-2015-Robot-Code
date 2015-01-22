package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;

/** An action that wil pivot the robot to a certain angle using PID and Gyro */
class PIDPivotAction extends AutonomousAction {
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
        this.setpoint = setpoint;
        this.tolerance = tolerance;
    }

    @Override
    public void start() {
        driveSubsystem.resetPIDPosition();
        driveSubsystem.setPivotPIDSetpoint(setpoint);
        driveSubsystem.startPivotPID();
    }

    @Override
    public EndState run() {
        if (driveSubsystem.pidOnTarget(tolerance)) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}
