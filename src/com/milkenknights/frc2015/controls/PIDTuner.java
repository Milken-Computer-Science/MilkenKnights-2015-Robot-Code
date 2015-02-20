package com.milkenknights.frc2015.controls;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.subsystems.*;
import com.milkenknights.frc2015.subsystems.autonomous.PIDTrapezoidal;

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
    public boolean runningAutonAction;
    public boolean deadbandTune;
    
    private AutonomousAction action;

    public PIDTuner(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atkl = new JStick(0);
        atkr = new JStick(1);
        atka = new JStick(2);

        isCheesy = false;
    }
    
    public void teleopInit() {
        pidEnabled = false;
        runningAutonAction = false;
        deadbandTune = false;
        updateConstants();
    }

    public void teleopPeriodic() {
        atkl.update();
        atkr.update();
        atka.update();
        
        double l_knob_precise = (2-atkl.getAxis(JStick.ATK3_KNOB))/30;
        double r_knob_precise = (2-atkr.getAxis(JStick.ATK3_KNOB))/30;
        SmartDashboard.putBoolean("deadband_tune_on", deadbandTune);
        SmartDashboard.putNumber("l_knob_precise",l_knob_precise);
        SmartDashboard.putNumber("r_knob_precise",r_knob_precise);
        SmartDashboard.putNumber("setpoint_cur", driveSub.getStraightPIDSetpoint());
        
        SmartDashboard.putBoolean("pid enabled", pidEnabled);
        SmartDashboard.putBoolean("running auton action", runningAutonAction);

        if (!pidEnabled) {
            if (deadbandTune) {
                // DEADBAND TUNING MODE
                // wheel speed is controlled by the knob, but divided by
                // 15 for precise movements
                driveSub.tankDrive(l_knob_precise, r_knob_precise);
            } else if (isCheesy) {
                // CHEESY DRIVE
                // Power: left ATK y axis
                // Turning: right ATK x axis
                // no quickturn
                driveSub.cheesyDrive(-atkl.getAxis(JStick.ATK3_Y),
                        atkr.getAxis(JStick.ATK3_X), false);
            } else if (runningAutonAction) {
                action.run();
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
        if (atka.isReleased(1)) {
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
        
        // if left or right joystick Y axis is more then 0.6
        // or button 2 is pressed reset PID
        if (((pidEnabled || runningAutonAction) &&
                (Math.abs(atkl.getAxis(JStick.ATK3_Y)) > 0.6
                    || Math.abs(atkr.getAxis(JStick.ATK3_Y)) > Math.abs(0.6)))
                || atka.isPressed(2)) {
            driveSub.resetPIDPosition();
            pidEnabled = false;
        }
        
        // aux atk 3 resets pid constants
        if (atka.isPressed(3)) {
            SmartDashboard.putNumber("kp", 0.1);
            SmartDashboard.putNumber("ki", 0.01);
            SmartDashboard.putNumber("kd", 0.001);
            SmartDashboard.putNumber("setpoint", 30);
        }
        
        // aux ATK 4 gets new staright PID constants from SmartDashboard
        if (atka.isReleased(4)) {
            updateConstants();
        }
        
        // aux ATK 11 puts us in deadband tuning mode. aux ATK 10 disables it
        if (atka.isPressed(11)) {
            deadbandTune = true;
        } else if (atka.isPressed(10)) {
            deadbandTune = false;
        }
        
        // right ATK 11 starts the trapezoidal auton action
        if (atkr.isReleased(11)) {
            action = new PIDTrapezoidal(driveSub, 0.5, 0.5, 1, 100, 1);
            startAutonAction();
        }
    }
    
    private void startAutonAction() {
        action.start();
        runningAutonAction = true;
    }
    
    private void updateConstants() {
        double kp_in = SmartDashboard.getNumber("kp",-1);
        double ki_in = SmartDashboard.getNumber("ki",-1);
        double kd_in = SmartDashboard.getNumber("kd",-1);
        double sp_in = SmartDashboard.getNumber("setpoint",-1);
        
        System.out.println("kp "+kp_in+" ki "+ki_in+" kd "+kd_in);
        
        SmartDashboard.putNumber("kp_cur", kp_in);
        SmartDashboard.putNumber("ki_cur", ki_in);
        SmartDashboard.putNumber("kd_cur", kd_in);
        SmartDashboard.putNumber("setpoint_cur", sp_in);
        
        driveSub.setStraightPID(kp_in, ki_in, kd_in);
        driveSub.setStraightPIDSetpoint(sp_in);
    }
}
