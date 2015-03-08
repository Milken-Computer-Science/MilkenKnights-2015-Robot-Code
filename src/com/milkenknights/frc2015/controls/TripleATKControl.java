package com.milkenknights.frc2015.controls;

import java.util.LinkedList;

import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.*;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * 
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {
    Joystick atkr, atkl, atka;

    public boolean isCheesy;
    private boolean autoLoad;
    private int elevatorCommand;

    public TripleATKControl(DriveSubsystem sDrive, ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake, BinGrabberSubsystem sBinGrabber) {
        super(sDrive, sElevator, sGroundIntake, sBinGrabber);
        atkl = new Joystick(0);
        atkr = new Joystick(1);
        atka = new Joystick(2);

        isCheesy = false;
        autoLoad = false;
        elevatorCommand = 0;
    }

    public void teleopPeriodic() {

        // TANK DRIVE
        // controlled by left and right ATK y axes
        driveSub.tankDrive(-atkl.getAxis(Joystick.AxisType.kY),
                -atkr.getAxis(Joystick.AxisType.kY));

        if (atka.getRawButton(3)) {
            elevatorCommand = 1;
        }
        
        if (atka.getRawButton(2)) {
            elevatorCommand = 2;
        }

        if (atka.getRawButton(6)) {
            elevatorCommand = 3;
        }
        
        if (atka.getRawButton(7)) {
            elevatorCommand = 4;
        }
        
        if (atka.getRawButton(1)) {
            elevatorCommand = 0;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
        }
        
        if (atka.getRawButton(4)) {
            elevatorCommand = 0;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            if (!elevatorSub.toteLoaded()) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            } else {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            }
        }
        
        if (atka.getRawButton(5)) {
            elevatorCommand = 0;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            if (!elevatorSub.toteLoaded()) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
            } else {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            }
        }
        
        if (atka.getRawButton(9)) {
            elevatorCommand = 0;
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.OUTPUT);
        }
        
        

        // aux ATK 1 puts us in manual elevator control mode
        // also aborts a reset if we are in one
        if (atka.getRawButton(8)) {
            elevatorCommand = 0;
            elevatorSub.setPIDMode(false);
            elevatorSub.setManSpeed(atka.getAxis(Joystick.AxisType.kY));
            elevatorSub.setSetpoint(elevatorSub.getPosition());
            elevatorSub.abortReset();
        } else {
            elevatorSub.setPIDMode(true);
        }
        
        switch (elevatorCommand) {
        case 0:
            return;
        case 1:
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub
                    .setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            return;
        case 2:
            elevatorSub.setSetpoint(Constants.elevatorTote1Height);
            groundIntakeSub
                    .setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            return;
        case 3:
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub
                    .setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            if (elevatorSub.toteLoaded() && autoLoad) {
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
            return;
        case 4:
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            if (elevatorSub.toteLoaded() && autoLoad) {
                if (elevatorSub.getPosition() > Constants.elevatorMinDistance + Constants.elevatorThreshold) {
                    elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                    groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                } else {
                    elevatorCommand = 0;
                    groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
                }
            }
            return;
        default:
            return;
        }
    }
}
