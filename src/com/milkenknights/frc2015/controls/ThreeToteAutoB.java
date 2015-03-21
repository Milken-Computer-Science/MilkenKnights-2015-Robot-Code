package com.milkenknights.frc2015.controls;

import java.util.Iterator;
import java.util.LinkedList;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.autonomous.AutonWait;
import com.milkenknights.frc2015.subsystems.autonomous.ElevatorMoveAction;
import com.milkenknights.frc2015.subsystems.autonomous.FakeUltrasonic;
import com.milkenknights.frc2015.subsystems.autonomous.FlapsAction;
import com.milkenknights.frc2015.subsystems.autonomous.IntakeActuatorsSet;
import com.milkenknights.frc2015.subsystems.autonomous.IntakeWheelsSet;
import com.milkenknights.frc2015.subsystems.autonomous.PIDPivotAction;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightAction;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightBackground;
import com.milkenknights.frc2015.subsystems.autonomous.PrintAction;
import com.milkenknights.frc2015.subsystems.autonomous.WaitForAndLoadTote;
import com.milkenknights.frc2015.subsystems.autonomous.WaitForDriveDistance;
import com.milkenknights.frc2015.subsystems.autonomous.WaitForToteLoad;

/**
 * A three tote autonomous that uses AutonomousList
 */
public class ThreeToteAutoB extends AutonomousList {
    LinkedList<AutonomousAction> actions;
    
    public ThreeToteAutoB(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        
        actions = new LinkedList<AutonomousAction>();
        
        for (int i = 0; i < 2; i++) {
            actions.add(new FlapsAction(elevatorSub, true));
            actions.add(new ElevatorMoveAction(elevatorSub,
                    Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE+1.5,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            actions.add(new IntakeActuatorsSet(groundIntakeSub,
                    GroundIntakeSubsystem.ActuatorsState.CLOSED));
            actions.add(new IntakeWheelsSet(groundIntakeSub,
                    GroundIntakeSubsystem.WheelsState.RIGHT));
            
            actions.add(new PIDStraightBackground(driveSub, 81, 1.3));
            
            actions.add(new WaitForDriveDistance(driveSub, 12, true));
            actions.add(new IntakeActuatorsSet(groundIntakeSub,
                    GroundIntakeSubsystem.ActuatorsState.OPEN));

            actions.add(new WaitForDriveDistance(driveSub, 40, true));
            actions.add(new IntakeWheelsSet(groundIntakeSub,
                    GroundIntakeSubsystem.WheelsState.INTAKE));

            actions.add(new WaitForDriveDistance(driveSub, 70, true));
            actions.add(new IntakeActuatorsSet(groundIntakeSub,
                    GroundIntakeSubsystem.ActuatorsState.CLOSED));
            actions.add(new IntakeWheelsSet(groundIntakeSub,
                    GroundIntakeSubsystem.WheelsState.SLOW_INTAKE));

            actions.add(new WaitForToteLoad(elevatorSub));
            actions.add(new ElevatorMoveAction(elevatorSub, 0,
                    Constants.ELEVATOR.ACCURACY_THRESHOLD));
            
            actions.add(new IntakeWheelsSet(groundIntakeSub,
                    GroundIntakeSubsystem.WheelsState.STOPPED));
        }
        
        actions.add(new ElevatorMoveAction(elevatorSub,
                Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE+1.5,
                Constants.ELEVATOR.ACCURACY_THRESHOLD));
        
        actions.add(new IntakeWheelsSet(groundIntakeSub,
                GroundIntakeSubsystem.WheelsState.STOPPED));
        
        actions.add(new PIDPivotAction(driveSub, 90, 0.35));
        
        actions.add(new PIDStraightAction(driveSub, 50, 0.35));
        
        actions.add(new PIDPivotAction(driveSub, 90, 0.35));
        
        actions.add(new ElevatorMoveAction(elevatorSub,
                0, Constants.ELEVATOR.ACCURACY_THRESHOLD));
        
        actions.add(new FlapsAction(elevatorSub, false));
        
        actions.add(new PIDStraightAction(driveSub, 30, 0.35));
    }

    @Override
    protected Iterator<AutonomousAction> getAutonomousIterator() {
        return actions.iterator();
    }

}
