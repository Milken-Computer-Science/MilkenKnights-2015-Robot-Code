package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;

/** An action that will move the position of the elevator */
public class ElevatorMoveAction extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    
    double setpoint;
    double tolerance;
    
    /**
     * Make a new PIDStraightAction
     * @param elevatorSubsystem the ElevatorSubsystems instance to use
     * @param setpoint where the elevator should be
     * @param tolerance how close to the desired position we need to be
     */
    public ElevatorMoveAction(ElevatorSubsystem elevatorSubsystem, double setpoint,
            double tolerance) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.setpoint = setpoint;
        this.tolerance = tolerance;
    }
    
    @Override
    public void startCode() {
        elevatorSubsystem.setSetpoint(setpoint);
    }

    @Override
    public EndState periodicCode() {
        if (Math.abs(elevatorSubsystem.getPosition() - setpoint) < tolerance) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}