package com.milkenknights.frc2015.subsystems;

import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

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

    public ElevatorSubsystem() {
        elevatorTalonRight = new CANTalon(Constants.rightElevatorTalonDeviceNumber);
        elevatorTalonLeft = new CANTalon(Constants.leftElevatorTalonDeviceNumber);

        elevatorTalonRight.changeControlMode(CANTalon.ControlMode.Position);
        elevatorTalonLeft.changeControlMode(CANTalon.ControlMode.Position);
    }

    
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
