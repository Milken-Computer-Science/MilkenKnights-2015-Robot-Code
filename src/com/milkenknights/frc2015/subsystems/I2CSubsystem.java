/**
 * 
 */
package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Data is structured as follows:
 * 0 - Alliance
 * 1 - RobotMode
 * 2 - Auto Step
 * 3 - Ready for HumanPlayer
 *
 * @author austinshalit
 *
 */
public class I2CSubsystem extends MSubsystem {
    
    private I2C arduino;
    private byte[] data = new byte[4];
    private byte autoStep;
    private boolean readyForHumanPlayer;
    
    public I2CSubsystem() {
        arduino = new I2C(I2C.Port.kOnboard, Constants.I2C.ARDUINO);
        autoStep = 0;
        readyForHumanPlayer = false;
    }
    
    public void setAutoStep(byte autoStep) {
        this.autoStep = autoStep;
    }
    
    public void setReadyHumanPlayer(boolean b) {
        readyForHumanPlayer = b;
    }
    
    /**
     * This function will be run periodically
     */
    public void update() {
        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
            data[0] = 1;
        } else {
            data[0] = 0;
        }
        
        if (RobotState.isOperatorControl()) {
            data[1] = 2;
        } else if (RobotState.isAutonomous()) {
            data[1] = 1;
        } else {
            data[1] = 0;
        }
        
        data[2] = autoStep;
        data[3] = (byte) (readyForHumanPlayer ? 1 : 0);
        
        arduino.writeBulk(data);
        
        SmartDashboard.putString("I2C Data", data.toString());
    }
}
