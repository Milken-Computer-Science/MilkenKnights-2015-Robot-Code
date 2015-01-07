package com.milkenknights.frc2015;

import java.util.LinkedList;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.ControlSystem;
import com.milkenknights.frc2015.controls.TripleATKControl;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
    LinkedList<MSubsystem> subsystems;
    
    DriveSubsystem driveSubsystem;

    ControlSystem controlSystem;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.pressureTransducerChannel, 
                Constants.transducerScaleFactor, Constants.transducerOffset);
        driveSubsystem = new DriveSubsystem();

        controlSystem = new TripleATKControl(driveSubsystem);

        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
    }

    Timer autonTimer;

    public void autonomousInit() {
        autonTimer = new Timer();
        autonTimer.start();
    }

    public void autonomousPeriodic() {
        // Move the robot forward at full speed for 3 seconds
        if (autonTimer.get() < 3) {
            driveSubsystem.tankDrive(1,1);
        } else {
            driveSubsystem.tankDrive(0,0);
        }

    }

    public void teleopInit() {
        for (MSubsystem s : subsystems) {
            s.teleopInit();
        }
    }

    public void teleopPeriodic() {
        controlSystem.teleopPeriodic();
    }

    public void testPeriodic() {

    }

}
