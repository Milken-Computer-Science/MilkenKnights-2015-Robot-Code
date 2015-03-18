package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;

/**
 * Immediately sets percent voltage for both drive motors, and ends
 * @author Jake
 */
public class StraightAction extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    
    double speed;
    
    /**
     * Make a new StraightAction
     * @param driveSubsystem The drive subsystem to use
     * @param speed The percent voltage to use from -1 to 1
     */
    public StraightAction(DriveSubsystem driveSubsystem, double speed) {
        this.driveSubsystem = driveSubsystem;
        this.speed = speed;
    }

    @Override
    protected void startCode() {
        driveSubsystem.setDriveMode(DriveSubsystem.DriveMode.TANK);
        driveSubsystem.tankDrive(speed, speed);
    }

    @Override
    protected EndState periodicCode() {
        return EndState.END;
    }

}
