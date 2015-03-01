package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.BinGrabberSubsystem;

/** Immediately sets percent voltage speed for the bin grabber, and ends. */
public class BinGrabberSpeedAction extends AutonomousAction {
    BinGrabberSubsystem binGrabberSubsystem;
    
    double speed;
    
    /**
     * Make a new BinGrabberSpeedAction
     * @param binGrabberSubsystem The BinGrabberSubsystem to use
     * @param speed The desired speed.
     */
    public BinGrabberSpeedAction(BinGrabberSubsystem binGrabberSubsystem,
            double speed) {
        this.binGrabberSubsystem = binGrabberSubsystem;
        this.speed = speed;
    }
    
    @Override
    protected void startCode() {
        binGrabberSubsystem.setSpeed(speed);
    }

    @Override
    protected EndState periodicCode() {
        // TODO Auto-generated method stub
        return EndState.END;
    }

}
