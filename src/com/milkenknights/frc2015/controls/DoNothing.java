package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

public class DoNothing extends ControlSystem {

    public DoNothing(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
    }

    @Override
    public void init() { }

    @Override
    public void periodic() { }
}
