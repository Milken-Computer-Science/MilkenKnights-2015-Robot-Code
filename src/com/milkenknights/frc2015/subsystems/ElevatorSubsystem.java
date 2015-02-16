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
    boolean positionMode;

    double elevatorSpeed;
    boolean resetPosition;
    
    int toteCount = 0;
    boolean goingUp;
    
    boolean manualPIDConstants;

    CANTalon elevatorTalonRight;
    CANTalon elevatorTalonLeft;

    /** false means the elevator is at its lowest point */
    DigitalInput hallEffectSensor;

    Encoder enc_l;
    // right encoder stuff commented out because it should just follow the
    // left talon
    //Encoder enc_r;

    PIDController pid_l;
    //PIDController pid_r;

    public ElevatorSubsystem() {
        hallEffectSensor = new DigitalInput(
                Constants.hallEffectSensorDeviceNumber);

        elevatorTalonLeft = new CANTalon(
                Constants.leftElevatorTalonDeviceNumber);
        elevatorTalonRight = new CANTalon(
                Constants.rightElevatorTalonDeviceNumber);

        resetPosition = false;
        positionMode = false;

        enc_l = new Encoder(Constants.elevatorLeftEncoderDeviceNumberA,
                Constants.elevatorLeftEncoderDeviceNumberB);
        //enc_r = new Encoder(Constants.elevatorRightEncoderDeviceNumberA,
        //        Constants.elevatorRightEncoderDeviceNumberB);

        enc_l.setDistancePerPulse(Constants.elevatorInchesPerPulse);
        //enc_r.setDistancePerPulse(-Constants.elevatorInchesPerPulse);

        pid_l = new PIDController(0,0,0, enc_l, elevatorTalonLeft);
        //pid_r = new PIDController(0,0,0, enc_r, elevatorTalonRight);
    }

    /**
     * Change the mode for us controlling the elevator.
     * 
     * @param mode True changes mode to position mode. False changes mode to
     * manual speed control mode.
     */
    public void changeMode(boolean mode) {
        if (mode != positionMode) {
            if (mode) {
                pid_l.enable();
                //pid_r.enable();
                resetPosition = false;
            } else {
                if (pid_l.isEnable()) {
                    pid_l.disable();
                }
                /*
                if (pid_r.isEnable()) {
                    pid_r.disable();
                }
                */
            }
        }
        positionMode = mode;
    }

    /** Returns true if we are in position mode. */
    public boolean inPositionMode() {
        return positionMode;
    }
    
    /**
     * Move the elevator position. Only does something if we are in position
     * mode.
     * @param setpoint The desired setpoint of the elevator.
     */
    public void setSetpoint(double setpoint) {
        pid_l.setSetpoint(setpoint);
        //pid_r.setSetpoint(-setpoint);
    }

    /**
     * Manually set the speed of the elevator.  Only works when we are in manual
     * speed control mode (otherwise, does nothing).
     * 
     * A positive value will move the elevator up.
     * @param speed The desired speed.
     */
    public void setSpeed(double speed) {
        elevatorSpeed = speed;
    }

    /**
     * Return the speed that we told to elevator to move at.
     * @return The last set value from setSpeed.
     */
    public double getSpeed() {
        return elevatorSpeed;
    }

    /**
     * Triggers the robot to go in reset mode. In reset mode, the elevator will
     * descend at a slow speed until the bottom hall effect sensors are hit.
     * When the robot is in reset mode, it will not react to any other controls
     * until it is either finished, or if the reset is manually halted by
     * calling abortReset().
     */
    public void resetPosition() {
        resetPosition = true;
    }

    /**
     * If the robot is in reset mode, this will prematurely end the reset.
     */
    public void abortReset() {
        resetPosition = false;
    }

    /**
     * Get the average between the elevator encoder positions.
     * @return the average between the elevator encoder positions.
     */
    public double getPosition() {
        //return (enc_l.getDistance() - enc_r.getDistance())/2;
        return enc_l.getDistance();
    }

    /**
     * Set PID gains for both sides of the elevator.
     */
    public void setPID(double p, double i, double d) {
        pid_l.setPID(p, i, d);
        //pid_r.setPID(p, i, d);
    }
    
    /**
     * Reset the encoders to zero. This should only be called if we know that
     * the elevator is its lowest point.
     */
    public void resetEncoders() {
        enc_l.reset();
        //enc_r.reset();
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
    
    public void teleopInit() {
        changeMode(false);
    }
    
    /**
     * Use manual PID constants instead of different PID constants for up, down
     * and different totes.
     * @param enable If set to true use manual PID constants.
     */
    public void manualPIDPosition(boolean enable) {
        manualPIDConstants = enable;
        if (manualPIDConstants) {
            setPID(Constants.manualElevatorPID.kp,
                    Constants.manualElevatorPID.ki, 
                    Constants.manualElevatorPID.kd);
        }
    }

    public void update(){
        if (!hallEffectSensor.get()) {
            elevatorTalonLeft.setPosition(0);
            enc_l.reset();
            resetPosition = false;
        }

        if (!positionMode) {
            if (resetPosition) {
                elevatorTalonLeft.set(Constants.resetElevatorSpeed);
            } else {
                elevatorTalonLeft.set(elevatorSpeed);
                //elevatorTalonRight.set(-elevatorSpeed);
            }
        } else if (!manualPIDConstants) {
            if (elevatorTalonRight.getSetpoint() > elevatorTalonRight.getPosition()) {
                setPID(Constants.elevatorUpPID[toteCount].kp, 
                        Constants.elevatorUpPID[toteCount].ki, 
                        Constants.elevatorUpPID[toteCount].kd);
            } else {
                setPID(Constants.elevatorDownPID[toteCount].kp, 
                        Constants.elevatorDownPID[toteCount].ki, 
                        Constants.elevatorDownPID[toteCount].kd);
            }
        }
        
        // The right talon should just follow the left talon
        elevatorTalonRight.set(-elevatorTalonLeft.get());
    }
}
