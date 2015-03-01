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
    
    public GamepadControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake,
            BinGrabberSubsystem sBinGrabber) {
        super(sDrive, sElevator, sGroundIntake, sBinGrabber);
        xbox = new JStick(0);
        atk = new JStick(1);
        
        isCheesy = false;
    }
    
    public void teleopInit() {}
    
    public void teleopPeriodic() {
        xbox.update();
        atk.update();
        
        if (isCheesy) {
            // CHEESY DRIVE
            // Power: left stick Y
            // Turning: right stick X
            // Quickturn: left trigger 50% down
            driveSub.cheesyDrive(xbox.getAxis(JStick.XBOX_LSY),
                    xbox.getAxis(JStick.XBOX_RSX),
                    xbox.getAxis(JStick.XBOX_LTRIG) > 0.75);
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
