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

    public PIDTunerElevator(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atka = new JStick(2);
    }

    public void teleopInit() {
        updateConstants();
    }

    public void teleopPeriodic() {
        atka.update();

        SmartDashboard.putBoolean("pid enabled", elevatorSub.inPositionMode());
        SmartDashboard.putNumber("elevator manual speed", elevatorSub.getSpeed());
        SmartDashboard.putNumber("Elevator position", elevatorSub.getPosition());

        elevatorSub.setSpeed(-atka.getAxis(JStick.ATK3_Y));

        // aux ATK 1 enables PID
        if (atka.isReleased(1)) {
            elevatorSub.changeMode(true);
        }
        
        // aux ATK 2 puts us in manual speed control mode (disables pid)
        if (atka.isReleased(2)) {
            elevatorSub.changeMode(false);
        }

        // button 10 does the reset PID thing
        if (atka.isPressed(10)) {
            elevatorSub.changeMode(false);
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
