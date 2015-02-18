package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;

/** An action that will move the position of the elevator */
public class ElevatorMovePID extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    
    double setpoint;
    double tolerance;
    
    /**
     * Make a new PIDStraightAction
     * @param elevatorSubsystem the ElevatorSubsystems instance to use
     * @param setpoint where the elevator should be
     * @param tolerance how close to the desired position we need to be
     */
    public ElevatorMovePID(ElevatorSubsystem elevatorSubsystem, double setpoint,
            double tolerance) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.setpoint = setpoint;
        this.tolerance = tolerance;
    }
    
    @Override
    public void start() {
        elevatorSubsystem.setSetpoint(setpoint);
        elevatorSubsystem.changeMode(true);
    }

    @Override
    public EndState run() {
        if (Math.abs(elevatorSubsystem.getPosition() - setpoint) < tolerance) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }
}
