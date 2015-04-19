package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.Subsystems;

import edu.wpi.first.wpilibj.Joystick;

public class BinGrabberTest extends ControlSystem {

    Joystick atka;
    
    protected BinGrabberTest(Subsystems subsystems) {
        super(subsystems);
        // TODO Auto-generated constructor stub
        atka = new Joystick(2);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        subsystems.binGrabber().speed(atka.getRawAxis(2));
        
        
    }

    @Override
    public void periodic() {
        // TODO Auto-generated method stub

    }

}
