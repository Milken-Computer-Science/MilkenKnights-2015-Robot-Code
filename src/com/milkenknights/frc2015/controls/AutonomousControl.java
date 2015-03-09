package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.BinGrabberSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousControl extends ControlSystem {

    private int step = 0;
    private boolean toteGrabbed;

    public AutonomousControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator, GroundIntakeSubsystem sGroundIntake,
            BinGrabberSubsystem sBinGrabber) {
        super(sDrive, sElevator, sGroundIntake, sBinGrabber);
        // TODO Auto-generated constructor stub
        
        toteGrabbed = false;
    }

    @Override
    public void teleopPeriodic() {
        // TODO Auto-generated method stub

    }
    
    public void autonomousInit() {
        step = 0;
    }

    @Override
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("Auto Step", step);
        switch (step) {
        case 0:
            driveSub.resetStraightPIDPosition();
            driveSub.setStraightPIDSetpoint(0);
            driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
            toteGrabbed = false;
            step++;
            break;
        case 1:
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            
            if (elevatorSub.getPosition() > elevatorSub.getSetpoint() - Constants.elevatorThreshold) {
                step++;
            }
            break;
        case 2:
            driveSub.setStraightPIDSetpoint(81);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            
            if (elevatorSub.toteLoaded()) {
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                step++;
            }
            break;
        case 3:
            elevatorSub.setSetpoint(Constants.elevatorMinDistance);
            if (elevatorSub.getPosition() < Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
                elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
                step++;
            } else if (elevatorSub.getPosition() < 8) {
                elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.OPEN);
            }
            break;
        case 4:
            driveSub.setStraightPIDSetpoint(162);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            
            if (elevatorSub.toteLoaded()) {
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                step++;
            }
            break;
        case 5:
            elevatorSub.setSetpoint(Constants.elevatorMinDistance);
            if (elevatorSub.getPosition() < Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
                elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
                driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDPIVOT);
                driveSub.setPivotPIDSetpoint(90);
                if (driveSub.pidOnTarget(2)) {
                    driveSub.resetStraightPIDPosition();
                    step++;
                }     
            } else if (elevatorSub.getPosition() < 8) {
                elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.OPEN);
            }
            break;
        case 6:
            driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
            driveSub.setStraightPIDSetpoint(50);
            if (driveSub.pidOnTarget(2)) {
                driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDPIVOT);
                driveSub.setPivotPIDSetpoint(180);
                if (driveSub.pidOnTarget(2)) {
                    elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                    if (elevatorSub.getPosition() < elevatorSub.getSetpoint() + Constants.elevatorThreshold) {
                        elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.OPEN);
                        driveSub.setDriveMode(DriveSubsystem.DriveMode.PIDSTRAIGHT);
                        driveSub.setStraightPIDSetpoint(30);
                        step++;
                    }
                }
            }
        }
    }
}
