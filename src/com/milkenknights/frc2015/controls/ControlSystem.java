package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;

/**
 * An abstract class for control systems. A control system should extend this
 * class and override the teleopPeriodic method to control subsystems based on
 * joystick inputs.
 * 
 * @author Daniel Kessler
 */
public abstract class ControlSystem {
    protected DriveSubsystem driveSub;
    protected ElevatorSubsystem elevatorSub;
    
    protected ControlSystem(DriveSubsystem sDrive, ElevatorSubsystem sElevator) {
        driveSub = sDrive;
        elevatorSub = sElevator;
    }
    
    /**
     * This function is run once at the start of the teleop period.
     */
    public abstract void teleopInit();

    /**
     * This function is run periodically during the teleop period.
     */
    public abstract void teleopPeriodic();
}
