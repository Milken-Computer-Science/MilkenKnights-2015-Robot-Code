package com.milkenknights.frc2015.controls;

import java.util.Iterator;
import java.util.LinkedList;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.ActuatorsState;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.WheelsState;
import com.milkenknights.frc2015.subsystems.Subsystems;
import com.milkenknights.frc2015.subsystems.autonomous.AutonWait;
import com.milkenknights.frc2015.subsystems.autonomous.ElevatorMoveAction;
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
        
        for (int i = 0; i < 2; i++) {
            actions.add(new FlapsAction(subsystems, true));
            actions.add(new IntakeActuatorsSet(subsystems,
                    ActuatorsState.OPEN));

            actions.add(new ElevatorMoveAction(subsystems,
                    Constants.ELEVATOR.HEIGHTS.TWO_TOTE,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            actions.add(new PIDStraightAction(subsystems, (i*77)+37-i, 1.3));
            
            actions.add(new IntakeWheelsSet(subsystems,
                    WheelsState.SLOW_INTAKE));
            actions.add(new IntakeActuatorsSet(subsystems,
                    ActuatorsState.CLOSED));
            
            actions.add(new PIDPivotAction(subsystems, 45, 5));
            
            actions.add(new IntakeWheelsSet(subsystems, WheelsState.OUTPUT));
            actions.add(new AutonWait(0.3));
            
            actions.add(new IntakeWheelsSet(subsystems, WheelsState.STOPPED));
            actions.add(new IntakeActuatorsSet(subsystems, ActuatorsState.OPEN));
            actions.add(new PIDPivotAction(subsystems, 0, 2.5));
            
            actions.add(new AutonWait(0.15));

            actions.add(new PIDStraightBackground(subsystems, (i*77)+80, 1.3));
            actions.add(new WaitForDriveDistance(subsystems, (i*77)+73, true));
            
            actions.add(new IntakeActuatorsSet(subsystems,
                    GroundIntakeSubsystem.ActuatorsState.CLOSED));
            actions.add(new IntakeWheelsSet(subsystems,
                    WheelsState.SLOW_INTAKE));
            
            actions.add(new ElevatorMoveAction(subsystems,
                    Constants.ELEVATOR.HEIGHTS.MIN,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
        }
        
        actions.add(new IntakeWheelsSet(subsystems,
                GroundIntakeSubsystem.WheelsState.STOPPED));
        
        actions.add(new PIDPivotAction(subsystems, 90, 5));
        
        actions.add(new ResetDriveEncoders(subsystems));
        
        actions.add(new PIDStraightAction(subsystems, 80, 0.35));
        
        actions.add(new FlapsAction(subsystems, false));
        
        actions.add(new ResetDriveEncoders(subsystems));
        
        actions.add(new PIDStraightAction(subsystems, 0, 0.35));
    }

    @Override
    protected Iterator<AutonomousAction> getAutonomousIterator() {
        return actions.iterator();
    }
}
