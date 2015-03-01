package com.milkenknights.frc2015.controls;

import com.milkenknights.common.JStick;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.*;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 * @author Daniel
 */
public class ATKGuitarControl extends ControlSystem {
    JStick atkr, atkl, guitar;

    public boolean isCheesy;
    private boolean autoLoad;
    private boolean lowGear;
    private boolean toteGrabbed;

    public ATKGuitarControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake,
            BinGrabberSubsystem sBinGrabber) {
        super(sDrive, sElevator, sGroundIntake, sBinGrabber);
        atkl = new JStick(0);
        atkr = new JStick(1);
        guitar = new JStick(2);

        isCheesy = false;
        autoLoad = true;
        toteGrabbed = false;
        lowGear = false;
    }

    public void teleopInit() {
        elevatorSub.enablePID(true);
        elevatorSub.setSetpoint(elevatorSub.getPosition());
    }

    public void teleopPeriodic() {
        atkl.update();
        atkr.update();
        guitar.update();

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
        
        if (atkr.isReleased(1)) {
            lowGear = !lowGear;
        }

        // left ATK 7 toggles between cheesy and tank
        if (atkl.isReleased(7)) {
            isCheesy = !isCheesy;
        }

        // left ATK 8 switches to tank drive.
        if (atkl.isReleased(8)) {
            isCheesy = false;
        }

        // left ATK 9 switches to cheesy drive.
        if (atkl.isReleased(9)) {
            isCheesy = true;
        }

        if (guitar.getPOV(0) == 0) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() + .5);
            elevatorSub.abortReset();
        } else if (guitar.getPOV(0) == 180) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() - .5);
            elevatorSub.abortReset();
        }
        
        if (guitar.isPressed(JStick.GUIRAR_GREEN)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
            autoLoad = true;
        }
        
        if (guitar.isPressed(JStick.GUIRAR_RED)) {
            elevatorSub.setSetpoint(Constants.elevatorScoringPlatformHeight);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.OPEN);
            autoLoad = false;
        }
        
        if (guitar.isPressed(JStick.GUIRAR_YELLOW)) {

        }
        
        if (guitar.isPressed(JStick.GUIRAR_BLUE)) {
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
        }
        
        if (guitar.isReleased(JStick.GUIRAR_ORANGE)) {
            if (groundIntakeSub.getWheelsState() == GroundIntakeSubsystem.WheelsState.STOPPED) {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.OUTPUT);
            } else {
                groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            }
        }
        
        // aux ATK 10 puts the elevator in reset mode
        if (guitar.isPressed(JStick.GUITAR_BACK)) {
            elevatorSub.resetPosition();
        }

        // if a tote has been loaded, drop the elevator down and pick it up
        // this action should only be taken if the tote was loaded while the
        // elevator was up
        
        if (elevatorSub.toteLoaded() && !toteGrabbed && autoLoad) {
            if (elevatorSub.getPosition() <= Constants.elevatorMinDistance) {
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
}
