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
import com.milkenknights.frc2015.subsystems.autonomous.IntakeActuatorsSet;
import com.milkenknights.frc2015.subsystems.autonomous.IntakeWheelsSet;
import com.milkenknights.frc2015.subsystems.autonomous.PIDPivotAction;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightAction;
import com.milkenknights.frc2015.subsystems.autonomous.PrintAction;
import com.milkenknights.frc2015.subsystems.autonomous.WaitForAndLoadTote;

public class ThreeToteAutoC extends AutonomousList {
    LinkedList<AutonomousAction> actions;
    
    public ThreeToteAutoC(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        
        // FAKE ULTRASONIC SENSOR FOR AUTONOMOUS
        FakeUltrasonic fakeUltrasonic = new FakeUltrasonic();
        
        actions.add(new ElevatorMoveAction(elevatorSub,
                Constants.elevatorReadyToIntakeHeight,
                Constants.elevatorThreshold));
        
        actions.add(new IntakeActuatorsSet(groundIntakeSub,
                GroundIntakeSubsystem.ActuatorsState.OPEN));
        
        // repeat this twice
        for (int i = 0; i < 2; i++) {
            actions.add(fakeUltrasonic.setReadingAction(30));
            actions.add(new IntakeWheelsSet(groundIntakeSub,
                    GroundIntakeSubsystem.WheelsState.RIGHT));

            actions.add(new PIDStraightAction(driveSub, 40, 0.35));

            actions.add(new IntakeWheelsSet(groundIntakeSub,
                    GroundIntakeSubsystem.WheelsState.INTAKE));
            actions.add(new WaitForAndLoadTote(elevatorSub,
                    groundIntakeSub,
                    fakeUltrasonic));
            
            actions.add(new PrintAction("Load the tote now!"));
            actions.add(fakeUltrasonic.setReadingAction(7));
            actions.add(new AutonWait(5));
            actions.add(new PIDStraightAction(driveSub, 41, 0.35));
            actions.add(new PrintAction("tote should have been loaded"));
            
            //actions.add(fakeUltrasonic.setReadingAction(7));
        }
        
        actions.add(new PIDPivotAction(driveSub, 90, 0.35));
        
        actions.add(new PIDStraightAction(driveSub, 50, 0.35));
    }

    @Override
    protected Iterator<AutonomousAction> getAutonomousIterator() {
        return actions.iterator();
    }

}
