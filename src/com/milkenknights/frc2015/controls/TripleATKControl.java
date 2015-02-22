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
    private boolean toteGrabbed = false;

    public TripleATKControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atkl = new JStick(0);
        atkr = new JStick(1);
        atka = new JStick(2);

        isCheesy = false;
    }

    public void teleopInit() {
        elevatorSub.enablePID(true);
        // because we will start off in PID mode, we should make sure that
        // we don't start moving the elevator immediately
        elevatorSub.setSetpoint(elevatorSub.getPosition());
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
        if (atka.isPressed(1)) {
            elevatorSub.setSetpoint(elevatorSub.getSetpoint() + atka.getAxis(JStick.ATK3_Y));
            elevatorSub.abortReset();
        }
        
        // aux ATK 2 moves elevator to ground level
        if (atka.isReleased(2)) {
            elevatorSub.setSetpoint(0);
        }
        
        // aux ATK 3 moves elevator to scoring platform
        if (atka.isReleased(3)) {
            elevatorSub.setSetpoint(Constants.scoringPlatformHeight);
        }
        
        // aux ATK 4 moves elevator to first tote height
        if (atka.isReleased(4)) {
            elevatorSub.setSetpoint(Constants.tote1Height);
        }
        
        // aux ATK 5 moves elevator to second tote height
        if (atka.isReleased(5)) {
            elevatorSub.setSetpoint(Constants.tote2Height);
        }
        
        // aux ATK 6 resets tote count
        if (atka.isReleased(6)) {
            elevatorSub.setToteNumber(0);
        }
        
        if (atka.isReleased(7)) {
            groundIntakeSub.setActuators(false);
            //groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.BACKWARD);
        }
        
        // aux ATK 8 toggles intake actuators
        if (atka.isReleased(8)) {
            groundIntakeSub.open();
        }
        
        if (atka.isReleased(9)) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            groundIntakeSub.setActuators(false);
        }
        
        // aux ATK 10 puts the elevator in reset mode
        if (atka.isReleased(10)) {
            elevatorSub.resetPosition();
        }
        
        // if a tote has been loaded, drop the elevator down and pick it up
        // this action should only be taken if the tote was loaded while the
        // elevator was up
        
        if (elevatorSub.toteLoaded() && !toteGrabbed) {
            if (elevatorSub.getPosition() <= Constants.elevatorMinDistance + .2) {
                toteGrabbed = true;
                groundIntakeSub.setActuators(true);
                elevatorSub.setSetpoint(Constants.readyToIntakeHeight);
            } else {
                elevatorSub.setSetpoint(Constants.elevatorMinDistance);
            }
        }
        if (toteGrabbed && elevatorSub.getPosition() >= Constants.tote1Height) {
            toteGrabbed = false;
            groundIntakeSub.setActuators(false);
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
        }
    }
}
