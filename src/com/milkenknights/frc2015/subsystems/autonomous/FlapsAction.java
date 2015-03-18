package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem.ActuatorsState;

/** A quick autonomous action that sets the state of the elevator flaps. */
public class FlapsAction extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    
    ActuatorsState flapsState;
    /**
     * Make a new FlapsAction.
     * @param elevatorSubsystem The elevatorSubsystem to use
     * @param isClosed True if we should close the flaps, false if we should open
     */
    public FlapsAction(ElevatorSubsystem elevatorSubsystem, boolean isClosed) {
        this.elevatorSubsystem = elevatorSubsystem;
        
        this.flapsState = isClosed ? ActuatorsState.CLOSED : ActuatorsState.OPEN;
    }
    
    @Override
    protected void startCode() {
        elevatorSubsystem.setFlapsState(flapsState);
    }

    @Override
    protected EndState periodicCode() {
        return EndState.END;
    }

}
