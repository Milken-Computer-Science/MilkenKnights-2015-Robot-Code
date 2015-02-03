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
     * 
     * @param mode True changes mode to position mode. False changes mode to
     * PercentVbus
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
     * Tell the elevator to move to a predetermined height.
     * @param position The desired elevator position.
     */
    public void setPosition(Positions position) {
        changeMode(true);
        elevatorPosition = position;
    }
    
    public void setSpeed(double speed) {
        elevatorSpeed = speed;
    }

    public void resetPosition() {
        changeMode(false);
        if (hallEffectSensorLeft.get()) {
            elevatorTalonLeft.set(0);
            elevatorTalonLeft.setPosition(0);
        }
        else {
            elevatorTalonLeft.set(0.1);
        }
        if (hallEffectSensorRight.get()) {
            elevatorTalonRight.set(0);
            elevatorTalonRight.setPosition(0);
        }
        else {
            elevatorTalonRight.set(0.1);
        }
    }
    
    public void setPID(double p, double i, double d) {
        elevatorTalonRight.setPID(p, i, d);
        elevatorTalonLeft.setPID(p, i, d);
    }
    
    public void update(){
        if (positionMode) {
            elevatorTalonRight.set(elevatorPosition.position);
            elevatorTalonLeft.set(-elevatorPosition.position);
        }
        else {
            elevatorTalonRight.set(elevatorSpeed);
            elevatorTalonLeft.set(-elevatorSpeed);
            
        }
    }
}
