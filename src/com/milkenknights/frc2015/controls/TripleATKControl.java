package com.milkenknights.frc2015.controls;

import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.*;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {
    JStick atkr, atkl, atka;

    public boolean isCheesy;
    private boolean autoLoad;
    private boolean lowGear;
    private boolean toteGrabbed;

    public TripleATKControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atkl = new JStick(0);
        atkr = new JStick(1);
        atka = new JStick(2);

        isCheesy = false;
        autoLoad = true;
        toteGrabbed = false;
        lowGear = false;
    }

    public void teleopPeriodic() {
        atkl.update();
        atkr.update();
        atka.update();

        if (isCheesy) {
            // CHEESY DRIVE
            // Power: left ATK y axis
            // Turning: right ATK x axis
            // no quickturn
            driveSub.cheesyDrive(-atkl.getAxis(JStick.ATK3_Y),
                    atkr.getAxis(JStick.ATK3_X), false);
        } else {
            // TANK DRIVE
            // controlled by left and right ATK y axes
            if (lowGear) {
                driveSub.tankDrive(-atkl.getAxis(JStick.ATK3_Y)/2,
                    -atkr.getAxis(JStick.ATK3_Y)/2);
            } else {
                driveSub.tankDrive(-atkl.getAxis(JStick.ATK3_Y),
                    -atkr.getAxis(JStick.ATK3_Y));
            }
        }
        
        // right ATK 1 toggles software gear
        if (atkr.isReleased(1)) {
            lowGear = !lowGear;
        }
        
        /*
        // aux ATK 1 puts us in manual elevator control mode
        // also aborts a reset if we are in one
        if (atka.isPressed(1)) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() +
                    atka.getAxis(JStick.ATK3_Y));
            elevatorSub.abortReset();
        }
        */
        
        // aux ATK 2 toggles actuators
        if (atka.isReleased(2)) {
            groundIntakeSub.toggleActuators();
        }
 
        // aux ATK 5 enables intake mode
        if (atka.isReleased(5)) {
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.INTAKE);
        }
        
        // aux ATK 3 stops intake
        if (atka.isReleased(3)) {
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.STOPPED);
        }
        
        // aux ATK 4 enables intake output mode
        if (atka.isPressed(4)) {
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.OUTPUT);
        }
        
        if (atka.isPressed(6)) {
            elevatorSub.setSetpoint(Constants.elevatorTote1Height);
            groundIntakeSub.setActuators(
                    GroundIntakeSubsystem.ActuatorsState.OPEN);
            groundIntakeSub.setWheelsState(
                    GroundIntakeSubsystem.WheelsState.OUTPUT);
        }
        
        // aux ATK 10 resets elevator position
        if (atka.isPressed(10)) {
            elevatorSub.resetPosition();
        }
        
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
