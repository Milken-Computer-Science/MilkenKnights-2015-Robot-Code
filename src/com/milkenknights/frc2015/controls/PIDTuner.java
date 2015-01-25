package com.milkenknights.frc2015.controls;

import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.subsystems.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This control system has special controls for testing and tuning PID. It
 * uses three ATK3 controllers: two for driving and one for AUX.
 * @author Daniel
 */
public class PIDTuner extends ControlSystem {
    JStick atkr, atkl, atka;

    public boolean isCheesy;
    public boolean pidEnabled;

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

        if (!pidEnabled) {
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
        
        // aux ATK 1 enables PID
        if (atka.isPressed(1)) {
            driveSub.startStraightPID();
            pidEnabled = true;
        }
        
        // aux ATK 6 sets PID setpoint to 12 inches
        if (atka.isPressed(6)) {
            driveSub.setStraightPIDSetpoint(12);
        }
        
        // aux ATK 7 sets PID setpoint to 72 inches
        if (atka.isPressed(7)) {
            driveSub.setStraightPIDSetpoint(72);
        }
        
        // When PID enabled and left or right joystick Y axis is more then 0.6
        // or button 2 is pressed reset PID
        if (pidEnabled) {
            if (Math.abs(atkl.getAxis(JStick.ATK3_Y)) > 0.6 ||
                    Math.abs(atkr.getAxis(JStick.ATK3_Y)) > Math.abs(0.6) ||
                    atka.isPressed(2)) {
                driveSub.resetPIDPosition();
                pidEnabled = false;
            }
        }
        
        // aux ATK 4 gets new staright PID constants from SmartDashboard
        if (atka.isReleased(4)) {
            double kp_in = SmartDashboard.getNumber("kp",-1);
            double ki_in = SmartDashboard.getNumber("ki",-1);
            double kd_in = SmartDashboard.getNumber("kd",-1);
            
            System.out.println("kp "+kp_in+" ki "+ki_in+" kd "+kd_in);
            
            SmartDashboard.putString("recently received",
                    "kp "+kp_in+" ki "+ki_in+" kd "+kd_in);
            
            driveSub.setStraightPID(kp_in, ki_in, kd_in);
        }
    }
}
