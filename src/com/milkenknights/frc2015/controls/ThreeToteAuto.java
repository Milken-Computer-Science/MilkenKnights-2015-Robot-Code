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
    LinkedList<AutonomousAction> actions;
    
    public ThreeToteAuto(Subsystems subsystems) {
        super(subsystems);
        
        actions = new LinkedList<AutonomousAction>();

        actions.add(new ZeroGyroAction(subsystems));
        actions.add(new ResetDriveEncoders(subsystems));
        
        /** The distance between totes */
        double cycle_dist = 81;
        
        for (int i = 0; i < 2; i++) {
            actions.add(new ElevatorMoveAction(subsystems,
                    Constants.ELEVATOR.HEIGHTS.MIN,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));

            actions.add(new FlapsAction(subsystems, true));
            actions.add(new IntakeActuatorsSet(subsystems, ActuatorsState.OPEN));

            actions.add(new ElevatorMoveAction(subsystems,
                    Constants.ELEVATOR.HEIGHTS.KNOCK_BIN,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            actions.add(new PIDStraightAction(subsystems, (i*cycle_dist)+36, 1.3));
            
            actions.add(new IntakeWheelsSet(subsystems,
                    WheelsState.SLOW_INTAKE));
            actions.add(new IntakeActuatorsSet(subsystems,
                    ActuatorsState.CLOSED));
            actions.add(new AutonWait(0.15));
            
            actions.add(new PIDPivotAction(subsystems, 50, 7.5));
            
            actions.add(new IntakeWheelsSet(subsystems, WheelsState.OUTPUT));
            actions.add(new AutonWait(0.3));
            
            actions.add(new IntakeWheelsSet(subsystems, WheelsState.STOPPED));
            actions.add(new IntakeActuatorsSet(subsystems, ActuatorsState.OPEN));
            actions.add(new PIDPivotAction(subsystems, 0, 5));
            
            actions.add(new AutonWait(0.05));
            
            actions.add(new PIDStraightAction(subsystems, (i*cycle_dist)+35, 1.3));

            actions.add(new PIDStraightBackground(subsystems, (i*cycle_dist)+71, 1.3));
            actions.add(new WaitForDriveDistance(subsystems, (i*cycle_dist)+68, true));
            
            actions.add(new IntakeActuatorsSet(subsystems,
                    GroundIntakeSubsystem.ActuatorsState.CLOSED));
            actions.add(new IntakeWheelsSet(subsystems,
                    WheelsState.SLOW_INTAKE));
        }
        
        actions.add(new ElevatorMoveBackground(subsystems, 
                Constants.ELEVATOR.HEIGHTS.ONE_TOTE, 
                Constants.ELEVATOR.ACCURACY_THRESHOLD));
        
        actions.add(new IntakeWheelsSet(subsystems, GroundIntakeSubsystem.WheelsState.STOPPED));

        actions.add(new PIDPivotAction(subsystems, 50, 5));

        actions.add(new ResetDriveEncoders(subsystems));

        actions.add(new PIDStraightAction(subsystems, 82, 5));

        actions.add(new FlapsAction(subsystems, false));
        actions.add(new IntakeActuatorsSet(subsystems,
                GroundIntakeSubsystem.ActuatorsState.OPEN));
        actions.add(new AutonWait(0.25));

        actions.add(new PIDStraightAction(subsystems, 40, 4));

    }

    @Override
    protected Iterator<AutonomousAction> getAutonomousIterator() {
        return actions.iterator();
    }
}
