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
        
        // Toggle programmed low gear
        if (atkr.isReleased(1)) {
            lowGear = !lowGear;
        }
        
        // Move the elevator and abort a reset
        if (atka.isPressed(1)) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() + atka.getAxis(JStick.ATK3_Y));
            elevatorSub.abortReset();
        }
        
        
        if (atka.isReleased(2)) {
            groundIntakeSub.setActuators(groundIntakeSub.getActuatorsState() == GroundIntakeSubsystem.ActuatorsState.CLOSED ? GroundIntakeSubsystem.ActuatorsState.OPEN : GroundIntakeSubsystem.ActuatorsState.CLOSED);
        }
 
        if (atka.isReleased(5)) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.INTAKE);
        }
        
        if (atka.isReleased(3)) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
        }
        
        if (atka.isPressed(4)) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.OUTPUT);
        }
        
        // Reset elevator position
        if (atka.isPressed(10)) {
            elevatorSub.resetPosition();
        }

        // if a tote has been loaded, drop the elevator down and pick it up
        // this action should only be taken if the tote was loaded while the
        // elevator was up
        
        if (elevatorSub.toteLoaded() && !toteGrabbed && autoLoad) {
            if (elevatorSub.getPosition() <= Constants.elevatorMinDistance + .5) {
                toteGrabbed = true;
                groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
                elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            } else {
                elevatorSub.setSetpoint(Constants.elevatorMinDistance);
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
            }
        }
        if (toteGrabbed && elevatorSub.getPosition() >= Constants.elevatorTote1Height) {
            toteGrabbed = false;
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
        }
    }

    @Override
    public void teleopInit() {
        
    }
}
