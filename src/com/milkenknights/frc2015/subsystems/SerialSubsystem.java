/**
 * 
 */
package com.milkenknights.frc2015.subsystems;

import java.util.Arrays;

import com.milkenknights.common.DebugLogger;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Data is structured as follows: 0 - Alliance 1 - RobotMode 2 - Auto Step 3 -
 * Ready for HumanPlayer
 *
 * @author austinshalit
 *
 */
public class SerialSubsystem extends MSubsystem {

    private SerialPort arduino;
    private byte[] data = new byte[4];
    private byte autoStep;
    private boolean readyForHumanPlayer;
    private long cycleCount;

    public SerialSubsystem() {
        try {
            arduino = new SerialPort(9600, SerialPort.Port.kUSB);
        } catch (Exception e) {
            DebugLogger.log(DebugLogger.LVL_WARN, this, "Arduino not connected!");
            SmartDashboard.putBoolean("Arduino Connected", false);
        }
        autoStep = 0;
        cycleCount = 0;
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
        cycleCount++;
        if (cycleCount % 25 == 0) {
            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Red) {
                data[0] = 1;
            } else {
                data[0] = 0;
            }

            if (RobotState.isOperatorControl() && RobotState.isEnabled()) {
                data[1] = 2;
            } else if (RobotState.isAutonomous() && RobotState.isEnabled()) {
                data[1] = 1;
            } else {
                data[1] = 0;
            }

            data[2] = autoStep;
            data[3] = (byte) (readyForHumanPlayer ? 1 : 0);
            
            SmartDashboard.putString("Serial Data", Arrays.toString(data));

            try {
                arduino.writeString(Arrays.toString(data) + "\n");
                SmartDashboard.putBoolean("Arduino Connected", true);
            } catch (Exception e) {
                DebugLogger.log(DebugLogger.LVL_WARN, this, "Arduino not connected!");
                SmartDashboard.putBoolean("Arduino Connected", false);
            }
        }
    }
}
