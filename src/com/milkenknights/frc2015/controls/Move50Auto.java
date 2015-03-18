package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

public class Move50Auto extends ControlSystem {
    public Move50Auto(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
    }
    
    @Override
    public void init() {
        driveSub.resetStraightPIDPosition();
        driveSub.setStraightPIDSetpoint(50);
        driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
    }
    
    @Override
    public void periodic() {}
}
