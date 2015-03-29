package com.milkenknights.frc2015.controls;

import java.util.Iterator;
import java.util.LinkedList;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.ActuatorsState;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.WheelsState;
import com.milkenknights.frc2015.subsystems.Subsystems;
import com.milkenknights.frc2015.subsystems.autonomous.AutonWait;
import com.milkenknights.frc2015.subsystems.autonomous.DebugAction;
import com.milkenknights.frc2015.subsystems.autonomous.ElevatorMoveAction;
import com.milkenknights.frc2015.subsystems.autonomous.ElevatorMoveBackground;
import com.milkenknights.frc2015.subsystems.autonomous.FlapsAction;
import com.milkenknights.frc2015.subsystems.autonomous.IntakeActuatorsSet;
import com.milkenknights.frc2015.subsystems.autonomous.IntakeWheelsSet;
import com.milkenknights.frc2015.subsystems.autonomous.OuttakeAtTime;
import com.milkenknights.frc2015.subsystems.autonomous.PIDPivotAction;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightAction;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightBackground;
import com.milkenknights.frc2015.subsystems.autonomous.ResetDriveEncoders;
import com.milkenknights.frc2015.subsystems.autonomous.WaitForDriveDistance;
import com.milkenknights.frc2015.subsystems.autonomous.ZeroGyroAction;

/**
 * A three tote autonomous that attempts to grab knocked down bins and toss them
 * to the side.  Uses AutonomousList.
 */
public class ThreeToteAuto extends AutonomousList {
    public enum Strategy {
        QUALS, ELIM
    }
    
    Strategy strategy;
    
    LinkedList<AutonomousAction> actions;
    
    public ThreeToteAuto(Subsystems subsystems, Strategy strategy) {
        super(subsystems);
        
        this.strategy = strategy;
        
        actions = new LinkedList<AutonomousAction>();
        
        actions.add(new OuttakeAtTime(subsystems, 14.5));

        actions.add(new ZeroGyroAction(subsystems));
        actions.add(new ResetDriveEncoders(subsystems));
        
        /** The distance between totes */
        double cycle_dist = 81;
        
        for (int i = 0; i < 2; i++) {
            // pick up the tote we're holding
            actions.add(new ElevatorMoveAction(subsystems,
                    Constants.ELEVATOR.HEIGHTS.MIN,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            // close flaps and lift up the tote
            actions.add(new FlapsAction(subsystems, true));
            actions.add(new IntakeActuatorsSet(subsystems, ActuatorsState.OPEN));

            actions.add(new ElevatorMoveAction(subsystems,
                    Constants.ELEVATOR.HEIGHTS.KNOCK_BIN,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            // knock over the bin in front of us
            actions.add(new PIDStraightAction(subsystems, (i*cycle_dist)+37, 1.75));
            
            // as we pass over the knocked over bin, suck it in
            actions.add(new IntakeWheelsSet(subsystems,
                    WheelsState.SLOW_INTAKE));
            actions.add(new IntakeActuatorsSet(subsystems,
                    ActuatorsState.CLOSED));
            actions.add(new AutonWait(0.15));
            
            // rotate to the side while we are holding the bin
            actions.add(new PIDPivotAction(subsystems, 50, 7.5));
            
            // spit out the bin
            actions.add(new IntakeWheelsSet(subsystems, WheelsState.OUTPUT));
            actions.add(new AutonWait(0.3));
            
            actions.add(new IntakeWheelsSet(subsystems, WheelsState.STOPPED));
            actions.add(new IntakeActuatorsSet(subsystems, ActuatorsState.OPEN));
            
            // save time by moving down the elevator in preparation to pick up the next tote
            actions.add(new ElevatorMoveBackground(subsystems,
                    19,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            // put ourselves back in a straight line
            actions.add(new PIDPivotAction(subsystems, -1.5, 4));
            
            actions.add(new AutonWait(0.05));
            
            // move back by a small amount to help us realign to a more precise angle
            actions.add(new PIDStraightAction(subsystems, (i*cycle_dist)+38, 14.5));

            // move forward to enclose the next tote
            actions.add(new PIDStraightBackground(subsystems, (i*cycle_dist)+67.5, 1.3));
            actions.add(new WaitForDriveDistance(subsystems, (i*cycle_dist)+64.5, true));
            
            // once we have enclosed the tote, intake
            actions.add(new IntakeActuatorsSet(subsystems,
                    GroundIntakeSubsystem.ActuatorsState.CLOSED));
            actions.add(new IntakeWheelsSet(subsystems,
                    WheelsState.INTAKE));
        }
        
        actions.add(new ElevatorMoveAction(subsystems, 
                Constants.ELEVATOR.HEIGHTS.ONE_TOTE, 
                Constants.ELEVATOR.ACCURACY_THRESHOLD));
        
        actions.add(new IntakeWheelsSet(subsystems, GroundIntakeSubsystem.WheelsState.STOPPED));
        
        // if we are using the elimination strategy, make a small pivot that takes us to the center
        // of the field. otherwise, bring us to a corner of the field
        double endPivot = 65;
        if (strategy == Strategy.QUALS) {
            endPivot = 110;
        }
        actions.add(new PIDPivotAction(subsystems, endPivot, 5));

        actions.add(new ResetDriveEncoders(subsystems));
        
        // move to the auto zone
        actions.add(new FlapsAction(subsystems, false));
        
        actions.add(new PIDStraightAction(subsystems, 100, 0.75, 3));
        
        // once we get there, spit out the totes and start moving backwards so we aren't
        // touching the stack
        actions.add(new IntakeWheelsSet(subsystems, GroundIntakeSubsystem.WheelsState.OUTPUT));
        
        actions.add(new PIDStraightAction(subsystems, 40, 4));

        //actions.add(new IntakeWheelsSet(subsystems, GroundIntakeSubsystem.WheelsState.STOPPED));
    }

    @Override
    protected Iterator<AutonomousAction> getAutonomousIterator() {
        return actions.iterator();
    }
}
