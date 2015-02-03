package com.milkenknights.frc2015.subsystems;

import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The subsystem that controls the elevator.
 * @author Jake Reiner
 */
public class ElevatorSubsystem {
    boolean positionMode = true;

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

    public ElevatorSubsystem() {
        hallEffectSensorLeft = new DigitalInput(Constants.hallEffectSensorLeftDeviceNumber);
        hallEffectSensorRight = new DigitalInput(Constants.hallEffectSensorRightDeviceNumber);

        elevatorTalonRight = new CANTalon(Constants.rightElevatorTalonDeviceNumber);
        elevatorTalonLeft = new CANTalon(Constants.leftElevatorTalonDeviceNumber);

        elevatorTalonRight.changeControlMode(CANTalon.ControlMode.Position);
        elevatorTalonLeft.changeControlMode(CANTalon.ControlMode.Position);
    }

    /**
     * Change the mode for us controlling the elevator.
     * 
     * @param mode True changes mode to position mode. False changes mode to
     * manual speed control mode.
     */
    public void changeMode(boolean mode) {
        positionMode = mode;
        if (mode) {
            elevatorTalonRight.changeControlMode(CANTalon.ControlMode.Position);
            elevatorTalonLeft.changeControlMode(CANTalon.ControlMode.Position);
        }
        else {
            elevatorTalonRight.changeControlMode(CANTalon.ControlMode.PercentVbus);
            elevatorTalonLeft.changeControlMode(CANTalon.ControlMode.PercentVbus);
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
        return elevatorTalonRight.getPosition();
    }

    /**
     * Set PID gains for both sides of the elevator.
     */
    public void setPID(double p, double i, double d) {
        elevatorTalonRight.setPID(p, i, d);
        elevatorTalonLeft.setPID(p, i, d);
    }

    public void update(){
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
                elevatorTalonRight.set(elevatorPosition.position);
                elevatorTalonLeft.set(-elevatorPosition.position);
            } else {
                elevatorTalonRight.set(elevatorSpeed);
                elevatorTalonLeft.set(-elevatorSpeed);

            }
        }
    }
}
