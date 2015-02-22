package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;

/**
 * The subsystem that controls the elevator.
 * @author Jake Reiner
 */
public class ElevatorSubsystem extends MSubsystem {
    boolean resetMode = false;
    int toteCount = 0;
    
    CANTalon elevatorTalonRight;
    CANTalon elevatorTalonLeft;

    /** false means the elevator is at its lowest point */
    DigitalInput hallEffectSensor;

    Encoder enc_l;
    //Encoder enc_r;

    PIDController pid;
    //PIDController pid_r;
    
    /** when this returns true, a tote has been loaded */
    DigitalInput bannerSensor;

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

        enc_l = new Encoder(Constants.elevatorLeftEncoderDeviceNumberA,
                Constants.elevatorLeftEncoderDeviceNumberB);
        //enc_r = new Encoder(Constants.elevatorRightEncoderDeviceNumberA,
        //        Constants.elevatorRightEncoderDeviceNumberB);

        enc_l.setDistancePerPulse(Constants.elevatorInchesPerPulse);

        pid = new PIDController(Constants.elevatorPID.kp,Constants.elevatorPID.ki,Constants.elevatorPID.kd, enc_l, elevatorTalonLeft);

        bannerSensor = new DigitalInput(Constants.bannerSensorBlackDeviceNumber);
    }

    /**
     * Change the mode for us controlling the elevator.
     * 
     * @param mode True changes mode to position mode. False changes mode to
     * manual speed control mode.
     */
    public void enablePID(boolean b) {
        if (b) {
            pid.enable();
        } else {
            pid.disable();
        }
    }
    
    /**
     * Move the elevator position. Only does something if we are in position
     * mode.
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
    
    public double getSetpoint() {
        return pid.getSetpoint();
    }

    /**
     * Triggers the robot to go in reset mode. In reset mode, the elevator will
     * descend at a slow speed until the bottom hall effect sensors are hit.
     * When the robot is in reset mode, it will not react to any other controls
     * until it is either finished, or if the reset is manually halted by
     * calling abortReset().
     */
    public void resetPosition() {
        resetMode = true;
    }

    /**
     * If the robot is in reset mode, this will prematurely end the reset.
     */
    public void abortReset() {
        resetMode = false;
    }
    
    /**
     * Get the average between the elevator encoder positions.
     * @return the average between the elevator encoder positions.
     */
    public double getPosition() {
        return enc_l.getDistance();
    }
    
    /**
     * Reset the encoders to zero. This should only be called if we know that
     * the elevator is its lowest point.
     */
    public void resetEncoders() {
        enc_l.reset();
    }

    /**
     * 
     * @param toteNumber Number of totes on the elevator
     */
    public void setToteNumber(int toteNumber) {
        toteCount = toteNumber;
    }
    
    public int getToteNumber() {
        return toteCount;
    }
    
    /**
     * Tells us if a tote is ready to be picked up by the elevator
     * @return true if the tote is loaded
     */
    public boolean toteLoaded() {
        return bannerSensor.get();
    }
    
    public void teleopInit() {
        pid.setSetpoint(enc_l.getDistance());
    }

    public void update(){
        if (!hallEffectSensor.get()) {
            enc_l.reset();
            if (resetMode) {
                setSetpoint(1);
                resetMode = false;
            }
        }
        
        if (resetMode) {
            pid.setSetpoint(pid.getSetpoint() - Constants.resetElevatorDistance);
        }
        System.out.println(getSetpoint() + " " + enc_l.getDistance());
    }
}
