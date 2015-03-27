package com.milkenknights.frc2015.controls;

import com.milkenknights.common.DebugLogger;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem.FlapsState;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.ActuatorsState;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem.WheelsState;
import com.milkenknights.frc2015.subsystems.Subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {
    Joystick atkr, atkl, atka;

    private int elevatorCommand;
    private boolean released4;
    private boolean released5;
    private boolean released9;

    public TripleATKControl(Subsystems subsystems) {
        super(subsystems);
        atkl = new Joystick(0);
        atkr = new Joystick(1);
        atka = new Joystick(2);

        elevatorCommand = 0;
    }
    
    public void periodic() {
        // TANK DRIVE
        // controlled by left and right ATK y axes
        subsystems.drive().setDriveMode(DriveSubsystem.DriveMode.TANK);
        subsystems.drive().tankDrive(-atkl.getAxis(Joystick.AxisType.kY),
                -atkr.getAxis(Joystick.AxisType.kY));
        
        // aux atk 1 opens everything and stops intake wheels if they are spining.
        if (atka.getRawButton(1)) {
            subsystems.groundIntake().setActuators(ActuatorsState.OPEN);
            subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
            subsystems.elevator().setFlapsState(FlapsState.OPEN);
            elevatorCommand = 0;
        }
        
        // aux atk 2 grabs the bottom-most tote.  Drops the elevator to the bottom and closes flaps.
        if (atka.getRawButton(2)) {
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.MIN);
            subsystems.elevator().setFlapsState(FlapsState.CLOSED);
            elevatorCommand = 0;
        }

        // aux atk 3 prepares us for intaking a tote.  Raises the elevator, opens the intake, and
        // closes flaps.
        if (atka.getRawButton(3)) {
            subsystems.groundIntake().setActuators(ActuatorsState.OPEN);
            subsystems.elevator().setFlapsState(FlapsState.CLOSED);
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
            elevatorCommand = 0;
        }
        
        // While aux atk 4 is held, spin the intake wheels in.  If a tote has been detected, slow
        // down the spinning. Also opens the intake.
        if (atka.getRawButton(4)) {
            elevatorCommand = 0;
            released4 = true;
            subsystems.groundIntake().setActuators(ActuatorsState.OPEN);
            if (!subsystems.elevator().toteLoaded()) {
                subsystems.groundIntake().setWheelsState(WheelsState.INTAKE);
            } else {
                subsystems.groundIntake().setWheelsState(WheelsState.SLOW_INTAKE);
            }
        } else if (released4) {
            subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
            released4 = false;
        }
        
        // While aux atk 5 is held, spin the intake wheels in.  If a tote has been detected, slow
        // down the spinning. Also closes the intake.
        if (atka.getRawButton(5)) {
            elevatorCommand = 0;
            released5 = true;
            subsystems.groundIntake().setActuators(ActuatorsState.CLOSED);
            if (!subsystems.elevator().toteLoaded()) {
                subsystems.groundIntake().setWheelsState(WheelsState.INTAKE);
            } else {
                subsystems.groundIntake().setWheelsState(WheelsState.SLOW_INTAKE);
            }
        } else if (released5) {
            subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
            released5 = false;
        }
        
        /*
        if (atka.getRawButton(6)) {
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
            subsystems.groundIntake().setActuators(ActuatorsState.CLOSED);
            subsystems.groundIntake().setWheelsState(WheelsState.INTAKE);
            elevatorCommand = 1;
        }
        
        if (atka.getRawButton(7)) {
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
            subsystems.groundIntake().setActuators(ActuatorsState.CLOSED);
            subsystems.groundIntake().setWheelsState(WheelsState.INTAKE);
            elevatorCommand = 2;
        }
        */
        
        // aux atk 6 splits our stack
        if (atka.getRawButton(6)) {
            subsystems.elevator().setFlapsState(FlapsState.OPEN);
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
            elevatorCommand = 3;
        }
        
        // aux atk 7 is "problem" mode-- moves our elevator to a higher height, opens ground intake,
        // and closes the flaps.
        if (atka.getRawButton(7)) {
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.MIN+6);
            subsystems.groundIntake().setActuators(ActuatorsState.OPEN);
            subsystems.elevator().setFlapsState(FlapsState.CLOSED);
            elevatorCommand = 0;
        }
        
        // aux atk 8 held down puts the elevator in manual mode, controlled by aux atk y axis.
        // When it is released, our new setpoint becomes whatever the elevator is at the end of
        // manual mode.
        if (atka.getRawButton(8)) {
            elevatorCommand = 0;
            subsystems.elevator().setPIDMode(false);
            subsystems.elevator().setManualSpeed(atka.getAxis(Joystick.AxisType.kY));
            subsystems.elevator().setSetpoint(subsystems.elevator().getPosition());
        } else {
            subsystems.elevator().setPIDMode(true);
        }
        
        // aux atk 9 pushes out whatever is being held. Closes the intake, and outputs while being
        // held down.
        if (atka.getRawButton(9)) {
            elevatorCommand = 0;
            released9 = true;
            subsystems.groundIntake().setActuators(ActuatorsState.CLOSED);
            subsystems.groundIntake().setWheelsState(WheelsState.OUTPUT);
        } else if (released9) {
            released9 = false;
            subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
        }
        
        if (atka.getRawButton(10)) {
            subsystems.groundIntake().setActuators(ActuatorsState.OPEN);
            subsystems.elevator().setFlapsState(FlapsState.CLOSED);
            subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE + 4);
            elevatorCommand = 0;
        }
 
        switch (elevatorCommand) {
        case 0:
            if (subsystems.elevator().getPosition() >= Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE - Constants.ELEVATOR.ACCURACY_THRESHOLD) {
                subsystems.serial().setReadyHumanPlayer(true);
            } else {
                subsystems.serial().setReadyHumanPlayer(false);
            }
            break;
        case 1:
            if (subsystems.elevator().toteLoaded()) {
                if (subsystems.elevator().getPosition() >
                        Constants.ELEVATOR.HEIGHTS.MIN +
                        Constants.ELEVATOR.ACCURACY_THRESHOLD) {
                    subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.MIN);
                    subsystems.groundIntake().setWheelsState(WheelsState.SLOW_INTAKE);
                } else {
                    elevatorCommand = 0;
                    subsystems.groundIntake().setActuators(ActuatorsState.OPEN);
                    subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
                    subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
                }
            }
            break;
        case 2:
            if (subsystems.elevator().toteLoaded()) {
                if (subsystems.elevator().getPosition() > Constants.ELEVATOR.HEIGHTS.MIN + Constants.ELEVATOR.ACCURACY_THRESHOLD) {
                    subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.MIN);
                    subsystems.groundIntake().setWheelsState(WheelsState.SLOW_INTAKE);
                } else {
                    elevatorCommand = 0;
                    subsystems.groundIntake().setWheelsState(WheelsState.STOPPED);
                }
            }
            break;
        case 3:
            if (Math.abs(subsystems.elevator().getSetpoint() - subsystems.elevator().getPosition()) <
                    Constants.ELEVATOR.ACCURACY_THRESHOLD) {
                subsystems.elevator().setFlapsState(FlapsState.CLOSED);
                subsystems.elevator().setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE+8);
            }
            break;
        default:
            break;
        }
        
        SmartDashboard.putNumber("Elevator Command", elevatorCommand);
    }

    @Override
    public void init() {
        DebugLogger.log(DebugLogger.LVL_INFO, this, "Teleop Init");
    }
}
