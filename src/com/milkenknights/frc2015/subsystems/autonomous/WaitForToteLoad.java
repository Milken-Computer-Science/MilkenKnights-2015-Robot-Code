package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;

/**
 * Wait until ElevatorSubsystem.toteLoaded() returns true.
 */
public class WaitForToteLoad extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    
    public WaitForToteLoad(ElevatorSubsystem elevatorSubsystem) {
        this.elevatorSubsystem = elevatorSubsystem;
    }

    @Override
    protected void startCode() {
    }

    @Override
    protected EndState periodicCode() {
        if (elevatorSubsystem.toteLoaded()) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }

}
