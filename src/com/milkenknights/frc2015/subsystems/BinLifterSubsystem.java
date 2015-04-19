package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.Solenoid;

public class BinLifterSubsystem extends MSubsystem {
    Solenoid leftLifterSolenoid;
    Solenoid rightLifterSolenoid;
    
    Solenoid leftFlapSolenoid;
    Solenoid rightFlapSolenoid;
    
    Solenoid leftClampSolenoid;
    Solenoid rightClampSolenoid;
    
    Solenoid leftMoverSolenoid;
    Solenoid rightMoverSolenoid;
    
    public boolean liftUp = false;
    public boolean flaps = false;
    public boolean leftClamp = false;
    public boolean rightClamp = false;
    public boolean moveLeft = false;
    public boolean moveRight = false;
    
    public BinLifterSubsystem() {
        leftLifterSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_LEFT_LIFTER);
        rightLifterSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_RIGHT_LIFTER);
        
        leftFlapSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_LEFT_FLAP);
        rightFlapSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_RIGHT_FLAP);
        
        leftClampSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_LEFT_CLAMP);
        rightClampSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_RIGHT_CLAMP);
        
        leftMoverSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_LEFT_MOVER);
        rightMoverSolenoid = new Solenoid(Constants.SOLENOID.BINLIFTER_RIGHT_MOVER);
    }
    
    /**
     * 
     * @param up if true lift up if false go down
     */
    public void lift(boolean up) {
        liftUp = up;
    }
    
    /**
     * 
     * @param up If true flaps up if false flaps down
     */
    public void moveFlaps(boolean up) {
        flaps = up;
    }
    
    /**
     * 
     * @param close If true close left clamp if false open it
     */
    public void closeLeftClamp(boolean close) {
        leftClamp = close;
    }
    
    /**
     * 
     * @param close If true close right clamp if false open it
     */
    public void closeRightClamp(boolean close) {
        rightClamp = close;
    }
    
    /**
     * 
     * @param move
     */
    public void moveLeftClamp(boolean move) {
        moveLeft = move;
    }
    
    public void moveRightClamp(boolean move) {
        moveRight = move;
    }
    
    public void update() {
        leftLifterSolenoid.set(liftUp);
        rightLifterSolenoid.set(liftUp);
        
        leftFlapSolenoid.set(flaps);
        rightFlapSolenoid.set(flaps);
        
        leftClampSolenoid.set(leftClamp);
        rightClampSolenoid.set(rightClamp);
        
        leftMoverSolenoid.set(moveLeft);
        rightMoverSolenoid.set(moveRight);
    }
    
}
