package com.milkenknights.frc2015.controls;

import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.subsystems.*;

/**
 * This control system has special controls for testing and tuning PID. It
 * uses three ATK3 controllers: two for driving and one for AUX.
 * @author Daniel
 */
public class PIDTuner extends ControlSystem {
    JStick atkr, atkl, atka;

    public boolean isCheesy;

    public PIDTuner(DriveSubsystem sDrive) {
        super(sDrive);
        atkl = new JStick(0);
        atkr = new JStick(1);
        atka = new JStick(2);

        isCheesy = false;
    }

    public void teleopPeriodic() {
        atkl.update();
        atkr.update();
        atka.update();

        if (isCheesy) {
            // CHEESY DRIVE
            // Power: left ATK y axis
            // Turning: right ATK x axis
            // no quickturn
            driveSub.cheesyDrive(-atkl.getAxis(JStick.ATK3_Y),
                    atkr.getAxis(JStick.ATK3_X), false);
        } else {
            // TANK DRIVE
            // controlled by left and right ATK y axes
            driveSub.tankDrive(-atkl.getAxis(JStick.ATK3_Y),
                    -atkr.getAxis(JStick.ATK3_Y));
        }

        // left ATK 7 toggles between cheesy and tank
        if (atkl.isReleased(7)) {
            isCheesy = !isCheesy;
        }

        // left ATK 8 switches to tank drive. 9 switches to cheesy
        if (atkl.isReleased(8)) {
            isCheesy = false;
        }

        if (atkl.isReleased(9)) {
            isCheesy = true;
        }
    }
}
