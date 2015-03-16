package com.milkenknights.frc2015;

import java.util.LinkedList;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.AutonomousControl;
import com.milkenknights.frc2015.controls.ControlSystem;
import com.milkenknights.frc2015.controls.TripleATKControl;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    LinkedList<MSubsystem> subsystems;
    
    DriveSubsystem driveSubsystem;
    ElevatorSubsystem elevatorSubsystem;
    GroundIntakeSubsystem groundIntakeSubsystem;

    ControlSystem teleControlSystem;
    ControlSystem autoControlSystem;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.pressureTransducerChannel, 
                Constants.transducerScaleFactor, Constants.transducerOffset);
        
        driveSubsystem = new DriveSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        groundIntakeSubsystem = new GroundIntakeSubsystem();

        teleControlSystem = new TripleATKControl(driveSubsystem,
                elevatorSubsystem,
                groundIntakeSubsystem);
        autoControlSystem = new AutonomousControl(driveSubsystem,
                elevatorSubsystem,
                groundIntakeSubsystem);
        

        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
        subsystems.add(elevatorSubsystem);
        subsystems.add(groundIntakeSubsystem);
    }

    public void autonomousInit() {
        autoControlSystem.autonomousInit();
        
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }

    public void autonomousPeriodic() {
        autoControlSystem.autonomousPeriodic();
        
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }

    public void teleopInit() {
        teleControlSystem.teleopInit();
        
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
