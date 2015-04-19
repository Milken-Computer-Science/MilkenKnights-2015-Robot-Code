package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.subsystems.Subsystems;

import edu.wpi.first.wpilibj.Joystick;

public class BinGrabberTest extends ControlSystem {

    Joystick atka;
    
    boolean released1 = false;
    boolean released2 = false;
    boolean released3 = false;
    boolean released4 = false;
    boolean released5 = false;
    boolean released6 = false;
    
    protected BinGrabberTest(Subsystems subsystems) {
        super(subsystems);
        // TODO Auto-generated constructor stub
        atka = new Joystick(2);
        
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        subsystems.binGrabber().speed(atka.getRawAxis(2));
        
        if (atka.getRawButton(1)) {
            released1 = true;
        } else if (released1) {
            released1 = false;
            subsystems.binLifter().lift(!subsystems.binLifter().liftUp);
        }
        
        if (atka.getRawButton(2)) {
            released2 = true;
        } else if (released2) {
            released2 = false;
            subsystems.binLifter().moveFlaps(!subsystems.binLifter().flaps);
        }
        
        if (atka.getRawButton(3)) {
            released3 = true;
        } else if (released3) {
            released3 = false;
            subsystems.binLifter().closeLeftClamp(!subsystems.binLifter().leftClamp);
        }
        
        if (atka.getRawButton(4)) {
            released4 = true;
        } else if (released4) {
            released4 = false;
            subsystems.binLifter().closeRightClamp(!subsystems.binLifter().rightClamp);
        }
        
        if (atka.getRawButton(5)) {
            released5 = true;
        } else if (released5) {
            released5 = false;
            subsystems.binLifter().moveLeftClamp(!subsystems.binLifter().moveLeft);
        }
        
        if (atka.getRawButton(6)) {
            released6 = true;
        } else if (released6) {
            released6 = false;
            subsystems.binLifter().moveRightClamp(!subsystems.binLifter().moveRight);
        }
    }

    @Override
    public void periodic() {
        // TODO Auto-generated method stub

    }

}
