package com.milkenknights.frc2015;

import java.util.LinkedList;
import java.util.ListIterator;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.*;
import com.milkenknights.frc2015.subsystems.BinGrabberSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.autonomous.*;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    LinkedList<MSubsystem> subsystems;
    
    DriveSubsystem driveSubsystem;
    ElevatorSubsystem elevatorSubsystem;
    GroundIntakeSubsystem groundIntakeSubsystem;
    BinGrabberSubsystem binGrabberSubsystem;

    ControlSystem teleControlSystem;
    ControlSystem autoControlSystem;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.pressureTransducerChannel, 
                Constants.transducerScaleFactor, Constants.transducerOffset);
        
        driveSubsystem = new DriveSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        groundIntakeSubsystem = new GroundIntakeSubsystem();
        binGrabberSubsystem = new BinGrabberSubsystem();

        teleControlSystem = new TripleATKControl(driveSubsystem,
                elevatorSubsystem,
                groundIntakeSubsystem,
                binGrabberSubsystem);
        autoControlSystem = new AutonomousControl(driveSubsystem,
                elevatorSubsystem,
                groundIntakeSubsystem,
                binGrabberSubsystem);
        

        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
        subsystems.add(elevatorSubsystem);
        subsystems.add(groundIntakeSubsystem);
        subsystems.add(binGrabberSubsystem);
    }

    public void autonomousInit() {
        autoControlSystem.autonomousInit();
    }

    public void autonomousPeriodic() {
        autoControlSystem.autonomousPeriodic();
        
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }

    public void teleopInit() {
        for (MSubsystem s : subsystems) {
            s.teleopInit();
        }
    }

    public void teleopPeriodic() {
        teleControlSystem.teleopPeriodic();
        
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }
    
    public void disabledPeriodic() {
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }

    public void testPeriodic() {

    }
}
