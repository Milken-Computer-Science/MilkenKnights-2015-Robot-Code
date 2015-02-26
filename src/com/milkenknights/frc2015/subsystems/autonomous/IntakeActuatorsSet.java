package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

public class IntakeActuatorsSet extends AutonomousAction {
    GroundIntakeSubsystem groundIntakeSubsystem;

    GroundIntakeSubsystem.ActuatorsState s;

    public IntakeActuatorsSet(GroundIntakeSubsystem groundIntakeSubsystem,
            GroundIntakeSubsystem.ActuatorsState s) {
        this.groundIntakeSubsystem = groundIntakeSubsystem;

        this.s = s;
    }

    @Override
    public void start() {
        groundIntakeSubsystem.setActuators(s);
    }

    @Override
    public EndState run() {
        return EndState.END;
    }

}
