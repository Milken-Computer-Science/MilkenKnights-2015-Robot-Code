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
    private boolean toteGrabbed;

    public ATKGuitarControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atkl = new JStick(0);
        atkr = new JStick(1);
        guitar = new JStick(2);

        isCheesy = false;
        toteGrabbed = false;
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
            driveSub.tankDrive(-atkl.getAxis(JStick.ATK3_Y),
                    -atkr.getAxis(JStick.ATK3_Y));
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

        // holding down aux ATK trigger puts us in manual speed control mode
        if (guitar.isPressed(1)) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() + guitar.getAxis(JStick.ATK3_Y));
            elevatorSub.abortReset();
        }
        
        if (guitar.isReleased(2)) {
            elevatorSub.setSetpoint(Constants.elevatorScoringPlatformHeight);
        }
        
        if (guitar.isReleased(3)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
        }
        
        if (guitar.isReleased(4)) {
            groundIntakeSub.open();
        }
        
        if (guitar.isReleased(5)) {
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
        }
        
        if (guitar.isReleased(6)) {

        }
        
        if (guitar.isReleased(7)) {
            
        }
        
        if (guitar.isReleased(8)) {
            
        }
        
        if (guitar.isReleased(9)) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
        }
        
        // aux ATK 10 puts the elevator in reset mode
        if (guitar.isReleased(10)) {
            elevatorSub.resetPosition();
        }
        
        // if a tote has been loaded, drop the elevator down and pick it up
        // this action should only be taken if the tote was loaded while the
        // elevator was up
        
        if (elevatorSub.toteLoaded() && !toteGrabbed) {
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
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
        }
        
        System.out.println(toteGrabbed);
    }
}
