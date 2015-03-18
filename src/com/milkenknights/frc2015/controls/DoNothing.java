package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.WheelsState;

public class DoNothing extends ControlSystem {

    public DoNothing(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
    }

    @Override
    public void init() {
        driveSub.setDriveMode(DriveSubsystem.DriveMode.TANK);
        driveSub.tankDrive(0, 0);
        
        elevatorSub.setSetpoint(elevatorSub.getPosition());
        elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
        
        groundIntakeSub.setWheelsState(WheelsState.STOPPED);
        groundIntakeSub.setActuators(
                GroundIntakeSubsystem.ActuatorsState.CLOSED);
    }

    @Override
    public void periodic() { }
}
    