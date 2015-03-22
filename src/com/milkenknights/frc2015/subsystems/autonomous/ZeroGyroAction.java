package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.Subsystems;

/**
 * A quick autonomous action that sets the state of the ground intake wheels.
 * 
 * @see GroundIntakeSubsystem#setWheelsState(WheelsState)
 */
public class ZeroGyroAction extends AutonomousAction {
    DriveSubsystem driveSubsystem;

    public ZeroGyroAction(Subsystems subsystems) {
        this.driveSubsystem = subsystems.drive();
    }

    @Override
    public void startCode() {
        driveSubsystem.zeroGyroYaw();
    }

    @Override
    public EndState periodicCode() {
        return EndState.END;
    }

}
