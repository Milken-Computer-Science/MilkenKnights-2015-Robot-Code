package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.Subsystems;

/**
 * A quick autonomous action that sets the state of the ground intake wheels.
 * 
 * @see GroundIntakeSubsystem#setWheelsState(WheelsState)
 */
public class IntakeWheelsSet extends AutonomousAction {
    GroundIntakeSubsystem groundIntakeSubsystem;

    GroundIntakeSubsystem.WheelsState s;

    public IntakeWheelsSet(Subsystems subsystems,
            GroundIntakeSubsystem.WheelsState s) {
        this.groundIntakeSubsystem = subsystems.groundIntake();

        this.s = s;
    }

    @Override
    public void startCode() {
        groundIntakeSubsystem.setWheelsState(s);
    }

    @Override
    public EndState periodicCode() {
        return EndState.END;
    }

}
