package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * 
 * @author Jake Reiner
 *
 */
public class ChuteIntakeSubsystem extends MSubsystem {
    CANTalon chuteIntakeTalon;
    
    boolean intake;
    
    public ChuteIntakeSubsystem() {
        chuteIntakeTalon = new CANTalon(Constants.chuteIntakeTalonDeviceNumber);
    }
    
    public void TeleopInit() {
        
    }
    
    public void Intake() {
        intake = true;
    }
    
    public void Update() {
        if (intake) {
            chuteIntakeTalon.set(1);
        }
        else {
            chuteIntakeTalon.set(0);
        }
        intake = false;
    }
}
