package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ThreeToteAutoA extends ControlSystem {

    private int step = 0;

    public ThreeToteAutoA(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator, GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
    }
        
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
            step++;
            break;
        case 1:
            elevatorSub.setSetpoint(Constants.elevatorTote2Height);
            
            if (elevatorSub.getPosition() > elevatorSub.getSetpoint() - Constants.elevatorThreshold) {
                step++;
            }
            break;
        case 2:
            driveSub.setStraightPIDSetpoint(81);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            //timer.safeStart();
            
            if (driveSub.getEncPosition() > 70) {
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                step++;
            } else if (driveSub.getEncPosition() > 40) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            } else if (driveSub.getEncPosition() > 12) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.RIGHT);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            } else if (driveSub.getEncPosition() > 10) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.RIGHT);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            }
            break;
        case 3:
            elevatorSub.setSetpoint(Constants.elevatorMinDistance);
            if (elevatorSub.getPosition() < Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                //elevatorSub.setSetpoint(Constants.elevatorTote2Height);
                //step++;
            }
            break;
        case 4:
            driveSub.setStraightPIDSetpoint(162);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);

            if (driveSub.getEncPosition() > 70 + 81) {
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                step++;
            } else if (driveSub.getEncPosition() > 40 + 81) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            } else if (driveSub.getEncPosition() > 20 + 81) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.RIGHT);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            } else if (driveSub.getEncPosition() > 10 + 81) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.RIGHT);
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            }
            break;
        case 5:
            elevatorSub.setSetpoint(Constants.elevatorTote2Height);
            if (elevatorSub.getPosition() < Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDPIVOT);
                driveSub.setPivotPIDSetpoint(90);
                if (driveSub.pidOnTarget(10)) {
                    driveSub.resetStraightPIDPosition();
                    step++;
                }     
            }
            break;
        case 6:
            driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
            driveSub.setStraightPIDSetpoint(50);
            if (driveSub.pidOnTarget(2)) {
                driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDPIVOT);
                driveSub.setPivotPIDSetpoint(180);
                if (driveSub.pidOnTarget(10)) {
                    elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                    if (elevatorSub.getPosition() < elevatorSub.getSetpoint() + Constants.elevatorThreshold) {
                        elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.OPEN);
                        driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
                        driveSub.setStraightPIDSetpoint(-30);
                        step++;
                    }
                }
            }
            break;
        case 7:
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            break;
        }
    }
}
