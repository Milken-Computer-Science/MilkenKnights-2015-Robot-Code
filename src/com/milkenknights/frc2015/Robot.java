package com.milkenknights.frc2015;

import java.util.LinkedList;
import java.util.ListIterator;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.*;
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

    ControlSystem controlSystem;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.pressureTransducerChannel, 
                Constants.transducerScaleFactor, Constants.transducerOffset);
        
        driveSubsystem = new DriveSubsystem();
        elevatorSubsystem = new ElevatorSubsystem();
        groundIntakeSubsystem = new GroundIntakeSubsystem();

        controlSystem = new TripleATKControl(driveSubsystem,
                elevatorSubsystem,
                groundIntakeSubsystem);

        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
        subsystems.add(elevatorSubsystem);
        subsystems.add(groundIntakeSubsystem);
    }
    
    /** An iterator through all the sequence of autonomous actions. */
    ListIterator<AutonomousAction> autonomousSequence;
    
    /**
     * The list of autonomous actions that are currently running, including
     * ones that have been backgrounded.
     */
    LinkedList<AutonomousAction> runningActions;

    public void autonomousInit() {
        LinkedList<AutonomousAction> autonomousList =
                new LinkedList<AutonomousAction>();
        
        runningActions = new LinkedList<AutonomousAction>();
        
        // FAKE ULTRASONIC SENSOR FOR AUTONOMOUS
        FakeUltrasonic fakeUltrasonic = new FakeUltrasonic();
        
        // COMPOSE THE AUTONOMOUS STEPS HERE
        /*
        //autonomousList.add(new IntakeActuatorsSet(groundIntakeSubsystem, GroundIntakeSubsystem.ActuatorsState.OPEN));
        //autonomousList.add(new ElevatorMoveAction(elevatorSubsystem, Constants.elevatorReadyToIntakeHeight, .5));
        //autonomousList.add(new IntakeWheelsSet(groundIntakeSubsystem, GroundIntakeSubsystem.WheelsState.RIGHT));
        //autonomousList.add(new PIDStraightAction(driveSubsystem, 20, 1));
        //autonomousList.add(new IntakeWheelsSet(groundIntakeSubsystem, GroundIntakeSubsystem.WheelsState.INTAKE));
        autonomousList.add(new PIDStraightAction(driveSubsystem, 81, 1));
        autonomousList.add(new AutonWait(2));
        autonomousList.add(new PIDStraightAction(driveSubsystem, 162, 1));
        autonomousList.add(new PIDPivotAction(driveSubsystem, 90, 1));
        */
        
        /*
        autonomousList.add(new ElevatorMoveAction(elevatorSubsystem,
                Constants.elevatorReadyToIntakeHeight,
                Constants.elevatorThreshold));
        
        autonomousList.add(new IntakeActuatorsSet(groundIntakeSubsystem,
                GroundIntakeSubsystem.ActuatorsState.OPEN));
        
        // repeat this twice
        for (int i = 0; i < 2; i++) {
            autonomousList.add(fakeUltrasonic.setReadingAction(30));
            autonomousList.add(new IntakeWheelsSet(groundIntakeSubsystem,
                    GroundIntakeSubsystem.WheelsState.RIGHT));

            autonomousList.add(new PIDStraightAction(driveSubsystem, 40, 0.35));

            autonomousList.add(new IntakeWheelsSet(groundIntakeSubsystem,
                    GroundIntakeSubsystem.WheelsState.INTAKE));
            autonomousList.add(new WaitForAndLoadTote(elevatorSubsystem,
                    groundIntakeSubsystem,
                    fakeUltrasonic));

            autonomousList.add(new PIDStraightAction(driveSubsystem, 41, 0.35));

            autonomousList.add(fakeUltrasonic.setReadingAction(7));
        }
        
        autonomousList.add(new PIDPivotAction(driveSubsystem, 90, 0.35));
        
        autonomousList.add(new PIDStraightAction(driveSubsystem, 50, 0.35));
        */
        
        //autonomousList.add(new PIDStraightAction(driveSubsystem, 40, 0.35));
        //autonomousList.add(new PIDPivotAction(driveSubsystem, 90, 0.35));
        
        autonomousList.add(new StraightAction(driveSubsystem, 0.6));
        autonomousList.add(new AutonWait(3));
        autonomousList.add(new StraightAction(driveSubsystem, 0));       
        
        autonomousSequence = autonomousList.listIterator();
        
        driveSubsystem.resetPIDPosition();
    }

    public void autonomousPeriodic() {
        // if this ends up being true at the end of the while loop, start the
        // next queued AutonomousAction.
        // If runningActions is ever empty (e.g. at the beginning of
        // autonomous), the while loop will never happen, startNextAction will
        // stay true, and we will find the next action to add.
        boolean startNextAction = true;
        
        // Loop through the list of currently running actions. We use a manual
        // ListIterator instead of the syntax shortcut because we need to
        // remove the element mid-loop when it ends.
        ListIterator<AutonomousAction> i = runningActions.listIterator();
        while (i.hasNext()) {
            AutonomousAction a = i.next();
            startNextAction = false;
            
            // run the action and find out what to do next based on its return
            // value.
            switch(a.periodicRun()) {
            case CONTINUE:
                break;
            // END doesn't break because it falls through and sets
            // startNextAction to true.
            case END:
                i.remove();
            case BACKGROUND:
                startNextAction = true;
                break;
            }
        }
        
        if (startNextAction && autonomousSequence.hasNext()) {
            AutonomousAction nextAction = autonomousSequence.next();
            nextAction.start();
            runningActions.add(nextAction);
        }
        
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }

    public void teleopInit() {
        controlSystem.teleopInit();
        for (MSubsystem s : subsystems) {
            s.teleopInit();
        }
    }

    public void teleopPeriodic() {
        controlSystem.teleopPeriodic();
        
        for (MSubsystem s : subsystems) {
            s.update();
        }
    }

    public void testPeriodic() {
        System.out.println(elevatorSubsystem.hallEffectSensor());
    }
}
