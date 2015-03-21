/**
 * 
 */
package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.I2C;

/**
 * @author austinshalit
 *
 */
public class I2CSubsystem extends MSubsystem {
    
    private I2C arduino;
    
    public I2CSubsystem() {
        arduino = new I2C(I2C.Port.kOnboard, Constants.I2C.ARDUINO);
    }
    /**
     * This function will be run periodically
     */
    public void update() {
        
    }
}
