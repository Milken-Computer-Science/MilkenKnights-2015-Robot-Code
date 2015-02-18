package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

/**
 * A simple autonomous action that sets the state of the ground intake
 * actuators.
 * 
 * @see GroundIntakeSubsystem#setActuators(boolean)
 */
public class IntakeActuatorsSet extends AutonomousAction {
    GroundIntakeSubsystem groundIntakeSubsystem;

    boolean state;

    public IntakeActuatorsSet(GroundIntakeSubsystem groundIntakeSubsystem,
            boolean state) {
        this.groundIntakeSubsystem = groundIntakeSubsystem;

        this.state = state;
    }

    @Override
    public void start() {
        groundIntakeSubsystem.setActuators(state);
    }

    @Override
    public EndState run() {
        return EndState.END;
    }
}
