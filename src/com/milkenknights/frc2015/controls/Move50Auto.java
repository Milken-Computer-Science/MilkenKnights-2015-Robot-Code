package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.Subsystems;

public class Move50Auto extends ControlSystem {
    public Move50Auto(Subsystems subsystems) {
        super(subsystems);
    }
    
    @Override
    public void init() {
        subsystems.drive().resetStraightPIDPosition();
        subsystems.drive().setStraightPIDSetpoint(50);
        subsystems.drive().setPivotPIDSetpoint(subsystems.drive().getYaw());
        subsystems.drive().setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
    }
    
    @Override
    public void periodic() {}
}
