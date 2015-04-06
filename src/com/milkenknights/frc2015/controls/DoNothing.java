package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.WheelsState;
import com.milkenknights.frc2015.subsystems.Subsystems;

/** Stops all of the subsystems from actively doing anything. */
public class DoNothing extends ControlSystem {
    public DoNothing(Subsystems subsystems) {
        super(subsystems);
    }

    @Override
    public void init() {
        subsystems.drive().setDriveMode(DriveSubsystem.DriveMode.TANK);
        subsystems.drive().tankDrive(0, 0);
        
        subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.MIN);
        subsystems.elevator().setFlapsState(ElevatorSubsystem.FlapsState.CLOSED);
        
        subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
        subsystems.groundIntake().setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
    }

    @Override
    public void periodic() { }
}
    