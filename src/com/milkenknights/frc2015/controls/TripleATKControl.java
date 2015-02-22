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
    private boolean toteGrabbed;

    public TripleATKControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator,
            GroundIntakeSubsystem sGroundIntake) {
        super(sDrive, sElevator, sGroundIntake);
        atkl = new JStick(0);
        atkr = new JStick(1);
        atka = new JStick(2);

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
        
        if (atka.isReleased(2)) {
            elevatorSub.setSetpoint(Constants.elevatorScoringPlatformHeight);
        }
        
        if (atka.isReleased(3)) {
            elevatorSub.setSetpoint(Constants.elevatorReadyToIntakeHeight);
        }
        
        if (atka.isReleased(4)) {
            groundIntakeSub.open();
        }
        
        if (atka.isReleased(5)) {
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
        }
        
        if (atka.isReleased(6)) {

        }
        
        if (atka.isReleased(7)) {
            
        }
        
        if (atka.isReleased(8)) {
            
        }
        
        if (atka.isReleased(9)) {
            groundIntakeSub.setWheelsState(GroundIntakeSubsystem.WheelsState.STOPPED);
            groundIntakeSub.setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
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
    }
}
