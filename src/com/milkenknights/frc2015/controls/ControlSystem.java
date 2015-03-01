package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.BinGrabberSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

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
    protected GroundIntakeSubsystem groundIntakeSub;
    protected BinGrabberSubsystem binGrabberSub;
    
    protected ControlSystem(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake,
            BinGrabberSubsystem sBinGrabber) {
        driveSub = sDrive;
        elevatorSub = sElevator;
        groundIntakeSub = sGroundIntake;
        binGrabberSub = sBinGrabber;
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
