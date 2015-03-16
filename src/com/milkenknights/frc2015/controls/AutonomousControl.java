package com.milkenknights.frc2015.controls;

import com.milkenknights.common.DebugLogger;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousControl extends ControlSystem {

    private int step = 0;

    public AutonomousControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator, GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void teleopPeriodic() {
        // TODO Auto-generated method stub
        DebugLogger.log(DebugLogger.LVL_STREAM, this, "This is not a Teleop Control System");
    }
    
    public void autonomousInit() {
        step = 0;
    }

    @Override
    public void autonomousPeriodic() {
        //driveForward(81);
        //threeToteAuto();
    }
    
    private void driveForward(int inches) {
        switch (step) {
        case 0:
            driveSub.resetStraightPIDPosition();
            driveSub.setStraightPIDSetpoint(inches);
            driveSub.setPivotPIDSetpoint(0);
            driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
            step++;
            break;
        }
    }
    
    private void threeToteAuto() {
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
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            
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
            } else if (driveSub.getEncPosition() > 20) {
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
                elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
                step++;
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
            elevatorSub.setSetpoint(Constants.elevatorMinDistance);
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

    @Override
    public void teleopInit() {
        // TODO Auto-generated method stub
        DebugLogger.log(DebugLogger.LVL_WARN, this, "This is not a Teleop Control System");
    }
}
