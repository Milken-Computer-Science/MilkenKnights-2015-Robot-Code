package com.milkenknights.frc2015.controls;

import com.milkenknights.common.MTimer;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem.DriveMode;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.WheelsState;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A three tote autonomous that attempts to grab knocked down bins and toss them
 * to the side.  Does not use AutonomousList.
 */
public class ThreeToteAutoD extends ControlSystem {
    private MTimer timer;

    private int step;
    
    public ThreeToteAutoD(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        
        timer = new MTimer();
    }

    @Override
    public void init() {
        step = 0;
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Auto Step", step);
        switch (step) {
        case 0:
            driveSub.resetStraightPIDPosition();
            driveSub.setStraightPIDSetpoint(0);
            driveSub.setPivotPIDSetpoint(0);
            driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            step++;
            break;
        case 1:
            elevatorSub.setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
            
            if (elevatorSub.getPosition() > elevatorSub.getSetpoint() - Constants.ELEVATOR.ACCURACY_THRESHOLD) {
                step++;
            }
            break;
        case 2:
            driveSub.setStraightPIDSetpoint(35);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            
            if (driveSub.getEncPosition() > 34) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
                driveSub.setDriveMode(DriveMode.PIDPIVOT);
                driveSub.setPivotPIDSetpoint(45);
                if (driveSub.pidOnTarget(5)) {
                    groundIntakeSub.setWheelsState(WheelsState.OUTPUT);
                    timer.safeStart();
                    if (timer.hasPeriodPassed(0.25)) {
                        driveSub.setPivotPIDSetpoint(0);
                        groundIntakeSub.setWheelsState(WheelsState.STOPPED);
                        groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
                        step++;
                    }
                }
            }
            break;
        }
    }

}
