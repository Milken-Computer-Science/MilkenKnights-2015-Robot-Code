package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;

public class PickUpTote extends AutonomousAction {

    ElevatorSubsystem elevatorSubsystem;
    
    boolean goingDown;
    boolean waiting;
    
    public PickUpTote(ElevatorSubsystem elevatorSubsystem) {
        this.elevatorSubsystem = elevatorSubsystem;
    }
    
    @Override
    public void start() {
        elevatorSubsystem.changeMode(true);
        elevatorSubsystem.setSetpoint(Constants.tote1Height);
        waiting = true;
    }

    @Override
    public EndState run() {
        if (waiting) {
            if (elevatorSubsystem.toteLoaded()) {
                waiting = false;
                goingDown = true;
                elevatorSubsystem.setSetpoint(0);
            }
        } else if (goingDown) {
            if (elevatorSubsystem.getPosition() <= 3) {
                elevatorSubsystem.changeMode(false);
                elevatorSubsystem.setSpeed(Constants.resetElevatorSpeed);
            }
            if (elevatorSubsystem.getPosition() <= 0.1) {
                goingDown = false;
                elevatorSubsystem.changeMode(true);
                elevatorSubsystem.setSetpoint(Constants.tote1Height);
            }
        } else {
            if (Math.abs(elevatorSubsystem.getPosition() - Constants.tote1Height) <= 0.5) {
                return EndState.END;
            } else if (elevatorSubsystem.getPosition() >= 0.3) {
                return EndState.BACKGROUND;
            }
        }
        
        return EndState.CONTINUE;
    }

}
