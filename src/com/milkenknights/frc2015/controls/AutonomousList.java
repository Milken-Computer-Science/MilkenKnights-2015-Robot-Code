package com.milkenknights.frc2015.controls;

import java.util.Iterator;
import java.util.LinkedList;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

public abstract class AutonomousList extends ControlSystem {
    protected AutonomousList(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator, GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
    }

    /**
     * This should return an iterator that goes through all desired autonomous
     * actions.
     * @return an iterator of AutonomousActions
     */
    protected abstract Iterator<AutonomousAction> getAutonomousIterator();
    private Iterator<AutonomousAction> autonomousSequence;
    
    /**
     * The list of autonomous actions that are currently running, including
     * ones that have been backgrounded.
     */
    private LinkedList<AutonomousAction> runningActions;

    public void init() {
        autonomousSequence = getAutonomousIterator();
    }
    
    public void periodic() {
        // if this ends up being true at the end of the while loop, start the
        // next queued AutonomousAction.
        // If runningActions is ever empty (e.g. at the beginning of
        // autonomous), the while loop will never happen, startNextAction will
        // stay true, and we will find the next action to add.
        boolean startNextAction = true;
        
        // Loop through the list of currently running actions. We use a manual
        // ListIterator instead of the syntax shortcut because we need to
        // remove the element mid-loop when it ends.
        Iterator<AutonomousAction> i = runningActions.listIterator();
        while (i.hasNext()) {
            AutonomousAction a = i.next();
            startNextAction = false;
            
            // run the action and find out what to do next based on its return
            // value.
            switch(a.periodicRun()) {
            case CONTINUE:
                break;
            case END:
                i.remove();
                startNextAction = true;
                break;
            case BACKGROUND:
                startNextAction = true;
                break;
            case END_FORK:
                i.remove();
                break;
            }
        }
        
        if (startNextAction && autonomousSequence.hasNext()) {
            AutonomousAction nextAction = autonomousSequence.next();
            nextAction.start();
            runningActions.add(nextAction);
        }
    }
}
