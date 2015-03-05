package com.milkenknights.frc2015.controls;

import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.*;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {
    Joystick atkr, atkl, atka;

    public boolean isCheesy;
    private boolean autoLoad;
    private boolean toteGrabbed;

    public TripleATKControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake,
            BinGrabberSubsystem sBinGrabber) {
        super(sDrive, sElevator, sGroundIntake, sBinGrabber);
        atkl = new Joystick(0);
        atkr = new Joystick(1);
        atka = new Joystick(2);

        isCheesy = false;
        autoLoad = false;
        toteGrabbed = false;
    }

    public void teleopPeriodic() {

        /*if (isCheesy) {
            // CHEESY DRIVE
            // Power: left ATK y axis
            // Turning: right ATK x axis
            // no quickturn
            driveSub.cheesyDrive(-atkl.getAxis(Joystick.AxisType.kY),
                    atkr.getAxis(Joystick.AxisType.kX), false);
        } 
        */
        
            // TANK DRIVE
            // controlled by left and right ATK y axes
                driveSub.tankDrive(-atkl.getAxis(Joystick.AxisType.kY),
                    -atkr.getAxis(Joystick.AxisType.kY));

                /*
        if (atka.getRawButton(3)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
        } else if (atka.getRawButton(2)) {
            elevatorSub.setSetpoint(Constants.elevatorTote1Height);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
        }
        
        if (atka.getRawButton(6)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            if (elevatorSub.toteLoaded() && !toteGrabbed && autoLoad) {
                if (elevatorSub.getPosition() > Constants.elevatorMinDistance) {
                    elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                    groundIntakeSub.setWheelsState(
                            GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
                } else {
                    toteGrabbed = true;
                    groundIntakeSub.setActuators(
                            GroundIntakeSubsystem.ActuatorsState.OPEN);
                    elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
                }
            }
            if (toteGrabbed && elevatorSub.getPosition() >=
                    Constants.elevatorTote1Height) {
                toteGrabbed = false;
                groundIntakeSub.setWheelsState(
                        GroundIntakeSubsystem.WheelsState.STOPPED);
            }
        }
        
        */
        
        // aux ATK 1 puts us in manual elevator control mode
        // also aborts a reset if we are in one
        if (atka.getRawButton(1)) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() +
                    atka.getAxis(Joystick.AxisType.kY));
            elevatorSub.abortReset();
        }
        
        // aux ATK 2 toggles actuators
        if (atka.getRawButton(2)) {
            groundIntakeSub.toggleActuators();
        }
 
        // aux ATK 5 enables intake mode
        if (atka.getRawButton(5)) {
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.INTAKE);
        }
        
        // aux ATK 3 stops intake
        if (atka.getRawButton(3)) {
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.STOPPED);
        }
        
        // aux ATK 4 enables intake output mode
        if (atka.getRawButton(4)) {
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.OUTPUT);
        }
        
        if (atka.getRawButton(6)) {
            elevatorSub.setSetpoint(Constants.elevatorTote1Height);
            groundIntakeSub.setActuators(
                    GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.OUTPUT);
        }
        
        // aux ATK 10 resets elevator position
        if (atka.getRawButton(10)) {
            elevatorSub.resetPosition();
        }
        
        /*
        double knob = atka.getRawAxis(JStick.ATK3_KNOB);
        if (Math.abs(knob) > 0.5) {
            binGrabberSub.setSpeed(knob);
        } else {
            binGrabberSub.setSpeed(0);
        }
        */
        
        
        // if a tote has been loaded, drop the elevator down and pick it up
        // this action should only be taken if the tote was loaded while the
        // elevator was up
        if (elevatorSub.toteLoaded() && !toteGrabbed && autoLoad) {
            if (elevatorSub.getPosition() > Constants.elevatorMinDistance) {
                elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                groundIntakeSub.setWheelsState(
                        GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
            } else {
                toteGrabbed = true;
                groundIntakeSub.setActuators(
                        GroundIntakeSubsystem.ActuatorsState.OPEN);
                elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            }
        }
        if (toteGrabbed && elevatorSub.getPosition() >=
                Constants.elevatorTote1Height) {
            toteGrabbed = false;
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.STOPPED);
        }
    }

    @Override
    public void teleopInit() {
        
    }
}
