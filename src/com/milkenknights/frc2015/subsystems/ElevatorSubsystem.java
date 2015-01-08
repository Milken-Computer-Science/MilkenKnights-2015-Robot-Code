package com.milkenknights.frc2015.subsystems;

import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * The subsystem that controls the elevator subsystem.
 * @author Jake Reiner
 */
public class ElevatorSubsystem {
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
    
    Positions elevatorPosition;

    CANTalon elevatorTalon;

    public ElevatorSubsystem() {
        elevatorTalon = new CANTalon(Constants.elevatorTalonDeviceNumber);

        elevatorTalon.changeControlMode(CANTalon.ControlMode.Position);
    }

    /**
     * Tell the elevator to move to a predetermined height.
     * @param position The desired elevator position.
     */
    public void setPosition(Positions position) {
        elevatorPosition = position;
    }

    public void update(){
        elevatorTalon.set(elevatorPosition.position);
    }
}
