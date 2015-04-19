package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

public class BinGrabberSubsystem extends MSubsystem {
    CANTalon leftTalon;
    CANTalon rightTalon;
    
    boolean mDown = false;
    boolean mUp = false;
    
    public BinGrabberSubsystem() {
        leftTalon = new CANTalon(Constants.CAN.BINGRABBER_LEFT_TALON);
        rightTalon = new CANTalon(Constants.CAN.BINGRABBER_RIGHT_TALON);
    }
    
    public void moveDown() {
        mDown = true;
        mUp = false;
    }
    
    public void moveUp() {
        mUp = true;
        mDown = false;
    }
    
    public void stop() {
        mDown = false;
        mUp = false;
    }
    
    public void speed(double speed) {
        leftTalon.set(speed);
        rightTalon.set(speed);
    }
    
    public void update() {
        if (leftTalon.getOutputCurrent() > Constants.BIN_GRABBER.MAX_CURRENT && 
                rightTalon.getOutputCurrent() > Constants.BIN_GRABBER.MAX_CURRENT) {
            mDown = false;
            mUp = false;
        }
        
        if (mDown) {
            leftTalon.set(1);
            rightTalon.set(1);
        } else if (mUp) {
            leftTalon.set(-1);
            rightTalon.set(-1);
        } else {
            leftTalon.set(0);
            rightTalon.set(0);
        }
    }
    
}
