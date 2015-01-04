package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.DriveSubsystem;

/**
 * An abstract class for control systems. Implementations should access this
 * class and and the teleopPeriodic method, and control the subsystems based on
 * joystick inputs.
 * 
 * Getting Joystick instances should be handled by the implementing class.
 * 
 * @author Daniel
 */
public abstract class ControlSystem {
        protected DriveSubsystem driveSub;
        
        protected ControlSystem(DriveSubsystem sDrive) {
                driveSub = sDrive;
        }
        
        public abstract void teleopPeriodic();
}
