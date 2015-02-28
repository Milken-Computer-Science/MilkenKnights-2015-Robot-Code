package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that controls the elevator.
 * 
 * @author Jake Reiner
 */
public class ElevatorSubsystem extends MSubsystem {
    /** when this returns true, a tote has been loaded */
    DigitalInput bannerSensor;
    CANTalon elevatorTalonLeft;

    CANTalon elevatorTalonRight;
    Encoder enc;
    // Encoder enc_r;

    /** false means the elevator is at its lowest point */
    DigitalInput hallEffectSensor;

    PIDController pid;

    boolean resetMode = false;

    public ElevatorSubsystem() {
        hallEffectSensor = new DigitalInput(
                Constants.hallEffectSensorDeviceNumber);

        elevatorTalonLeft = new CANTalon(
                Constants.leftElevatorTalonDeviceNumber);
        elevatorTalonRight = new CANTalon(
                Constants.rightElevatorTalonDeviceNumber);

        elevatorTalonRight.changeControlMode(CANTalon.ControlMode.Follower);
        elevatorTalonRight.set(elevatorTalonLeft.getDeviceID());
        elevatorTalonRight.reverseOutput(true);

        enc = new Encoder(Constants.elevatorLeftEncoderDeviceNumberA,
                Constants.elevatorLeftEncoderDeviceNumberB);

        enc.setDistancePerPulse(Constants.elevatorInchesPerPulse);

        pid = new PIDController(Constants.elevatorPID.kp,
                Constants.elevatorPID.ki, Constants.elevatorPID.kd, enc,
                elevatorTalonLeft);

        bannerSensor = new DigitalInput(Constants.bannerSensorBlackDeviceNumber);
        
        pid.enable();
    }

    /**
     * If the robot is in reset mode, this will prematurely end the reset.
     */
    public void abortReset() {
        resetMode = false;
    }

    /**
     * Enable or disable PID
     * 
     * @param enable
     *            True if we want to enable PID. False if we want to disable PID
     */
    public void enablePID(boolean enable) {
        if (enable) {
            pid.enable();
        } else {
            pid.disable();
        }
    }

    /**
     * Get the elevator encoder position
     * 
     * @return the elevator encoder position.
     */
    public double getPosition() {
        return enc.getDistance();
    }

    /**
     * Get the current setpoint of the elevator
     * 
     * @return The current setpoint
     */
    public double getSetpoint() {
        return pid.getSetpoint();
    }

    /**
     * Reset the encoder to zero. This should only be called if we know that the
     * elevator is its lowest point.
     */
    public void resetEncoder() {
        enc.reset();
    }

    /**
     * Triggers the robot to go in reset mode. In reset mode, the elevator will
     * slowly decrease the setpoint thus lowering the elevator. When the robot
     * is in reset mode, it will not react to any other controls until it is
     * either finished, or if the reset is manually halted by calling
     * abortReset().
     */
    public void resetPosition() {
        resetMode = true;
    }
    
    public boolean hallEffectSensor() {
        return hallEffectSensor.get();
    }

    /**
     * Set the setpoint of the elevator. This is bounded by the maximum and
     * minimum values of the elevator.
     * 
     * @param setpoint The desired setpoint of the elevator.
     */
    public void setSetpoint(double setpoint) {
        if (setpoint >= Constants.elevatorMaxDistance) {
            pid.setSetpoint(Constants.elevatorMaxDistance);
        } else if (setpoint <= Constants.elevatorMinDistance) {
            pid.setSetpoint(Constants.elevatorMinDistance);
        } else {
            pid.setSetpoint(setpoint);
        }
    }

    /**
     * Tells us if a tote is ready to be picked up by the elevator
     * 
     * @return true if the tote is loaded
     */
    public boolean toteLoaded() {
        //return bannerSensor.get();
        return false;
    }
    
    public void teleopInit() {
        pid.setSetpoint(getPosition());
    }

    public void update() {
        if (resetMode) {
            pid.setSetpoint(pid.getSetpoint() - Constants.elevatorResetDistance);
            if (!hallEffectSensor.get()) {
                resetEncoder();
                resetMode = false;
            }
        }
        
        SmartDashboard.putBoolean("Elevator Reset Mode", resetMode);
        SmartDashboard.putNumber("Elevator Setpoint", pid.getSetpoint());
        SmartDashboard.putNumber("Elevator Position", enc.getDistance());
    }
}
