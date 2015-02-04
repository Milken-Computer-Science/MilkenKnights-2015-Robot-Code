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

    public enum Positions {
        GROUND(0),
        SCORINGPLATFORM(1),
        STEP(2),
        FIRSTTOTE(3),
        SECONDTOTE(4),
        THIRDTOTE(5);

        public final double position;
        private Positions(double p) {
            position = p;
        }
    }

    public Positions elevatorPosition;

    CANTalon elevatorTalonRight;
    CANTalon elevatorTalonLeft;

    DigitalInput hallEffectSensorLeft;
    DigitalInput hallEffectSensorRight;
    
    Encoder enc_l;
    Encoder enc_r;
    
    PIDController pid_l;
    PIDController pid_r;

    public ElevatorSubsystem() {
        hallEffectSensorLeft = new DigitalInput(
                Constants.hallEffectSensorLeftDeviceNumber);
        hallEffectSensorRight = new DigitalInput(
                Constants.hallEffectSensorRightDeviceNumber);
        
        elevatorTalonRight = new CANTalon(
                Constants.rightElevatorTalonDeviceNumber);
        elevatorTalonLeft = new CANTalon(
                Constants.leftElevatorTalonDeviceNumber);
        
        resetPosition = false;
        positionMode = false;
        
        enc_l = new Encoder(Constants.elevatorLeftEncoderDeviceNumberA,
                Constants.elevatorLeftEncoderDeviceNumberB);
        enc_r = new Encoder(Constants.elevatorRightEncoderDeviceNumberA,
                Constants.elevatorRightEncoderDeviceNumberB);
        
        enc_l.setDistancePerPulse(1);
        enc_r.setDistancePerPulse(1);
        
        pid_l = new PIDController(0,0,0, enc_l, elevatorTalonLeft);
        pid_r = new PIDController(0,0,0, enc_r, elevatorTalonRight);
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
                pid_r.enable();
            } else {
                pid_l.disable();
                pid_r.disable();
            }
        }
    }
    
    /**
     * Tell the elevator to move to a predetermined height. Only works when we
     * are in position mode (otherwise, does nothing).
     * @param position The desired elevator position.
     */
    public void setPosition(Positions position) {
        elevatorPosition = position;
    }

    /**
     * Manually set the speed of the elevator. Only works when we are in manual
     * speed control mode (otherwise, does nothing).
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
     * Get the encoder position of the right side of the elevator.
     * @return the encoder position of the right side of the elevator.
     */
    public double getPosition() {
        return enc_r.getDistance();
    }

    /**
     * Set PID gains for both sides of the elevator.
     */
    public void setPID(double p, double i, double d) {
        pid_l.setPID(p, i, d);
        pid_r.setPID(p, i, d);
    }

    public void update(){
        //System.out.println(""+resetPosition+" "+positionMode+" "+pid_l.isEnable()+" "+pid_r.isEnable());
        /*
        if (resetPosition) {
            boolean leftDone = hallEffectSensorLeft.get();
            boolean rightDone = hallEffectSensorRight.get();
            if (leftDone) {
                elevatorTalonLeft.set(0);
                elevatorTalonLeft.setPosition(0);
            } else {
                elevatorTalonLeft.set(0.1);
            }
            
            if (rightDone) {
                elevatorTalonRight.set(0);
                elevatorTalonRight.setPosition(0);
            } else {
                elevatorTalonRight.set(-0.1);
            }
            
            // once both sides have been reset, leave reset mode
            if (leftDone && rightDone) {
                resetPosition = false;
            }
        } else {
            if (positionMode) {
                pid_l.setSetpoint(-elevatorPosition.position);
                pid_r.setSetpoint(elevatorPosition.position);
            } else {
            */
                elevatorTalonLeft.set(-elevatorSpeed);
                elevatorTalonRight.set(elevatorSpeed);
            /*
            }
        }
        */
    }
}
