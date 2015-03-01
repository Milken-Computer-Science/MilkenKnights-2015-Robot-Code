package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

public class BinGrabberSubsystem extends MSubsystem {
    CANTalon grabber;
    double speed;
    
    public BinGrabberSubsystem() {
        grabber = new CANTalon(Constants.binGrabberTalonDeviceNumber);
        speed = 0;
    }
    
    /**
     * Set the speed that the grabber talon should move at.
     * Out is negative.
     * @param speed The desired speed. Should be between -1 and 1.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Get the most recently set speed.
     * @return the most recently set speed.
     */
    public double getSpeed() {
        return speed;
    }
    
    public void update() {
        grabber.set(speed);
    }
}
