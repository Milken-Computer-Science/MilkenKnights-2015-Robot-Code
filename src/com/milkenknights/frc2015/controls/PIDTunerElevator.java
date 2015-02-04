package com.milkenknights.frc2015.controls;

import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.subsystems.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This control system has special controls for testing and tuning PID. It
 * uses three ATK3 controllers: two for driving and one for AUX.
 * @author Jake Reiner
 */
public class PIDTunerElevator extends ControlSystem {
    JStick atka;

    public boolean pidEnabled;

    public PIDTunerElevator(DriveSubsystem sDrive, ElevatorSubsystem sElevator) {
        super(sDrive, sElevator);
        atka = new JStick(2);
    }

    public void teleopInit() {
        pidEnabled = false;
        updateConstants();
    }

    public void teleopPeriodic() {
        atka.update();

        SmartDashboard.putBoolean("pid enabled", pidEnabled);
        SmartDashboard.putNumber("elevator manual speed", elevatorSub.getSpeed());
        SmartDashboard.putNumber("Elevator position", elevatorSub.getPosition());

        if (pidEnabled) {
            elevatorSub.changeMode(true);
        } else {
            elevatorSub.changeMode(false);
            elevatorSub.setSpeed(atka.getAxis(JStick.ATK3_Y)/2);
        }

        // aux ATK 1 enables PID
        if (atka.isReleased(1)) {
            pidEnabled = true;
        }
        
        // aux ATK 10 puts us in manual speed control mode
        if (atka.isReleased(10)) {
            pidEnabled = false;
        }

        // aux ATK 6 sets position to third tote
        if (atka.isPressed(6)) {
            elevatorSub.setPosition(ElevatorSubsystem.Positions.THIRDTOTE);
        }

        // aux ATK 7 sets position to ground
        if (atka.isPressed(7)) {
            elevatorSub.setPosition(ElevatorSubsystem.Positions.GROUND);
        }

        // if left or right joystick Y axis is more then 0.6
        // or button 2 is pressed reset PID
        if (atka.isPressed(2)) {
            pidEnabled = false;
            elevatorSub.resetPosition();
        }

        // aux atk 3 resets pid constants
        if (atka.isPressed(3)) {
            SmartDashboard.putNumber("kp", 0.1);
            SmartDashboard.putNumber("ki", 0.01);
            SmartDashboard.putNumber("kd", 0.001);
        }

        // aux ATK 4 gets new staright PID constants from SmartDashboard
        if (atka.isReleased(4)) {
            updateConstants();
        }
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

        elevatorSub.setPID(kp_in, ki_in, kd_in);
    }
}
