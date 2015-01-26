package com.milkenknights.frc2015;

import java.util.LinkedList;
import java.util.ListIterator;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.ControlSystem;
import com.milkenknights.frc2015.controls.PIDTuner;
import com.milkenknights.frc2015.controls.TripleATKControl;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightAction;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {
    LinkedList<MSubsystem> subsystems;
    
    DriveSubsystem driveSubsystem;

    ControlSystem controlSystem;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.pressureTransducerChannel, 
                Constants.transducerScaleFactor, Constants.transducerOffset);
        
        driveSubsystem = new DriveSubsystem();

        controlSystem = new PIDTuner(driveSubsystem);

        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
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

        // COMPOSE THE PID STEPS HERE
        autonomousList.add(new PIDStraightAction(driveSubsystem, 15, 1));

        autonomousSequence = autonomousList.listIterator();
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
            switch(a.run()) {
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

    }
}
