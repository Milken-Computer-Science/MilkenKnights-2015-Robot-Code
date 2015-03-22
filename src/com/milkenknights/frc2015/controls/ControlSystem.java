package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.Subsystems;

/**
 * An abstract class for control systems. A control system should extend this
 * class and override the teleopPeriodic method to control subsystems based on
 * joystick inputs.
 * 
 * @author Daniel Kessler
 */
public abstract class ControlSystem {
    protected Subsystems subsystems;
    
    protected ControlSystem(Subsystems subsystems) {
        this.subsystems = subsystems;
    }

    /**
     * This function is run at the start of the teleop period.
     */
    public abstract void init();
    
    /**
     * This function is run periodically during the teleop period.
     */
    public abstract void periodic();
}
