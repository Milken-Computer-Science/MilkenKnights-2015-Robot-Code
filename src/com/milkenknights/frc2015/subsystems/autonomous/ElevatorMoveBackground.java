package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;

/**
 * The same as ElevatorMoveAction, but backgrounds immediately after starting
 * the PID loop.
 */
public class ElevatorMoveBackground extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    
    double setpoint;
    double tolerance;
    
    boolean firstLoop;
    
    /**
     * Make a new ElevatorMoveBackground
     * @param elevatorSubsystem the ElevatorSubsystem instance to use
     * @param setpoint where the elevator should be
     * @param tolerance how close to the desired position we need to be
     */
    public ElevatorMoveBackground(ElevatorSubsystem elevatorSubsystem,
            double setpoint, double tolerance) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.setpoint = setpoint;
        this.tolerance = tolerance;
    }
    
    @Override
    public void startCode() {
        elevatorSubsystem.setSetpoint(setpoint);
        elevatorSubsystem.setPIDMode(true);
        
        firstLoop = true;
    }

    @Override
    public EndState periodicCode() {
        if (Math.abs(elevatorSubsystem.getPosition() - setpoint) < tolerance) {
            return EndState.END;
        } else if (firstLoop) {
            firstLoop = false;
            return EndState.BACKGROUND;
        } else {
            return EndState.CONTINUE;
        }
    }
}