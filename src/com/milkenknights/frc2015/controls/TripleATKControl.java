package com.milkenknights.frc2015.controls;

import com.milkenknights.common.DebugLogger;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * 
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {
    Joystick atkr, atkl, atka;

    private int elevatorCommand;
    
    private boolean released4;
    private boolean released5;
    private boolean released9;

    public TripleATKControl(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atkl = new Joystick(0);
        atkr = new Joystick(1);
        atka = new Joystick(2);

        elevatorCommand = 0;
    }

    public void teleopPeriodic() {
        SmartDashboard.putNumber("Elevator Command", elevatorCommand);

        // TANK DRIVE
        // controlled by left and right ATK y axes
        driveSub.tankDrive(-atkl.getAxis(Joystick.AxisType.kY),
                -atkr.getAxis(Joystick.AxisType.kY));
        
        driveSub.setDriveMode(DriveSubsystem.DriveMode.TANK);

        if (atka.getRawButton(3)) {
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight - 3);
            elevatorCommand = 0;
        }
        
        if (atka.getRawButton(10)) {
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight + 1);
            elevatorCommand = 0;
        }
        
        if (atka.getRawButton(2)) {
            elevatorSub.setSetpoint(Constants.elevatorMinDistance);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            elevatorCommand = 0;
        }

        if (atka.getRawButton(6)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub
                    .setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            elevatorCommand = 3;
        }
        
        if (atka.getRawButton(7)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            elevatorCommand = 4;
        }
        
        if (atka.getRawButton(1)) {
            elevatorCommand = 0;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.OPEN);
        }
        
        if (atka.getRawButton(4)) {
            elevatorCommand = 0;
            released4 = true;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            if (!elevatorSub.toteLoaded()) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            } else {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
            }
        } else if (released4) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            released4 = false;
        }
        
        if (atka.getRawButton(5)) {
            elevatorCommand = 0;
            released5 = true;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            if (!elevatorSub.toteLoaded()) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            } else {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
            }
        } else if (released5) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            released5 = false;
        }
        
        if (atka.getRawButton(9)) {
            elevatorCommand = 0;
            released9 = true;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.OUTPUT);
        } else if (released9) {
            released9 = false;
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
        }
        
        if (atka.getRawButton(8)) {
            elevatorCommand = 0;
            elevatorSub.setPIDMode(false);
            elevatorSub.setManualSpeed(atka.getAxis(Joystick.AxisType.kY));
            elevatorSub.setSetpoint(elevatorSub.getPosition());
        } else {
            elevatorSub.setPIDMode(true);
        }
        
        switch (elevatorCommand) {
        case 0:
            break;
        case 1:
            groundIntakeSub
                    .setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            elevatorCommand = 0;
            break;
        case 2:
            elevatorSub.setSetpoint(Constants.elevatorMinDistance);
            elevatorSub.setFlapsState(ElevatorSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub
                    .setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            break;
        case 3:
            if (elevatorSub.toteLoaded()) {
                if (elevatorSub.getPosition() > Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                    elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                    groundIntakeSub
                            .setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                } else {
                    elevatorCommand = 0;
                    groundIntakeSub
                            .setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
                    elevatorSub
                            .setSetpoint(Constants.elevatorReadyToIntakeHeight);
                    groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
                }
            }
            break;
        case 4:
            if (elevatorSub.toteLoaded()) {
                if (elevatorSub.getPosition() > Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                    elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                    groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                } else {
                    elevatorCommand = 0;
                    groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
                }
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void autonomousPeriodic() {
        // TODO Auto-generated method stub
        DebugLogger.log(DebugLogger.LVL_STREAM, this, "This is not an Autonomous Control System");
    }

    @Override
    public void autonomousInit() {
        // TODO Auto-generated method stub
        DebugLogger.log(DebugLogger.LVL_WARN, this, "This is not an Autonomous Control System");
    }

    @Override
    public void teleopInit() {
        // TODO Auto-generated method stub
        DebugLogger.log(DebugLogger.LVL_INFO, this, "Teleop Init");
    }
}
