package com.milkenknights.frc2015.subsystems;

import java.util.LinkedList;

import com.milkenknights.common.MSubsystem;

public class Subsystems {
    
    LinkedList<MSubsystem> subsystems;
    
    DriveSubsystem driveSubsystem;
    ElevatorSubsystem elevatorSubsystem;
    GroundIntakeSubsystem groundIntakeSubsystem;
    I2CSubsystem i2cSubsystem;

    public Subsystems() {
        driveSubsystem = new DriveSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        groundIntakeSubsystem = new GroundIntakeSubsystem();
        i2cSubsystem = new I2CSubsystem();
        
        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
        subsystems.add(elevatorSubsystem);
        subsystems.add(groundIntakeSubsystem);
        subsystems.add(i2cSubsystem);
    }
    
    public void update() {
        subsystems.stream().forEach(s -> s.update());
    }
    
    public void teleopInit() {
        subsystems.stream().forEach(s -> s.teleopInit());
    }

    /**
     * @return the driveSubsystem
     */
    public DriveSubsystem drive() {
        return driveSubsystem;
    }

    /**
     * @return the elevatorSubsystem
     */
    public ElevatorSubsystem elevator() {
        return elevatorSubsystem;
    }

    /**
     * @return the groundIntakeSubsystem
     */
    public GroundIntakeSubsystem groundIntake() {
        return groundIntakeSubsystem;
    }
}
