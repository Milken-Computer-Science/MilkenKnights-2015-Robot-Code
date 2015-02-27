package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

/**
 * A quick autonomous action that sets the state of the ground intake wheels.
 * 
 * @see GroundIntakeSubsystem#setWheelsState(WheelsState)
 */
public class IntakeWheelsSet extends AutonomousAction {
    GroundIntakeSubsystem groundIntakeSubsystem;

    GroundIntakeSubsystem.WheelsState s;

    public IntakeWheelsSet(GroundIntakeSubsystem groundIntakeSubsystem,
            GroundIntakeSubsystem.WheelsState s) {
        this.groundIntakeSubsystem = groundIntakeSubsystem;

        this.s = s;
    }

    @Override
    public void start() {
        groundIntakeSubsystem.setWheelsState(s);
    }

    @Override
    public EndState run() {
        return EndState.END;
    }

}
