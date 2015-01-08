package com.milkenknights.frc2015.controls;

import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.subsystems.*;

/**
 * This control system uses one gamepad (xbox) and one AUX ATK3.
 * 
 * @author Daniel
 */
public class GamepadControl extends ControlSystem {
    JStick xbox;
    JStick atk;
    
    public boolean isCheesy;
    
    public GamepadControl(DriveSubsystem sDrive) {
        super(sDrive);
        xbox = new JStick(1);
        atk = new JStick(2);
        
        isCheesy = false;
    }
    
    public void teleopPeriodic() {
        xbox.update();
        atk.update();
        
        if (isCheesy) {
            // CHEESY DRIVE
            // Power: left stick Y
            // Turning: right stick X
            // Quickturn: either trigger 50% down
            driveSub.cheesyDrive(xbox.getAxis(JStick.XBOX_LSY),
                    xbox.getAxis(JStick.XBOX_RSX),
                    Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5);
        } else {
            // TANK DRIVE
            // controlled by left and right xbox y axes
            driveSub.tankDrive(xbox.getAxis(JStick.XBOX_LSY),
                    xbox.getAxis(JStick.XBOX_RSY));
        }
        
        // XBOX_A toggles between cheesy and tank
        if (xbox.isReleased(JStick.XBOX_A)) {
            isCheesy = !isCheesy;
        }
        
        // XBOX_X switches to tank drive. XBOX_Y switches to cheesy
        if (xbox.isReleased(JStick.XBOX_X)) {
            isCheesy = false;
        }
        
        if (xbox.isReleased(JStick.XBOX_Y)) {
            isCheesy = true;
        }

    }
}
