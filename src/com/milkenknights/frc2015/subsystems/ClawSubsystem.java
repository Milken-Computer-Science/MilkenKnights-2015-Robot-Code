package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.Solenoid;

public class ClawSubsystem extends MSubsystem {
    
    Solenoid clawSolenoid;
    
    boolean clawOpen;
    
    public ClawSubsystem() {
        clawSolenoid = new Solenoid(Constants.clawSolenoidDeviceNumber);
        
        clawOpen = false;
        clawSolenoid.set(false);
    }
    
    public void TeleopInit() {
        
    }
    
    public void openClaw() {
        clawOpen = true;
    }
    
    public void closeClaw() {
        clawOpen = false;
    }
    
    public void toggleClaw() {
        clawOpen = !clawOpen;
    }
    
    public void Update() {
        clawSolenoid.set(clawOpen);
    }
}
