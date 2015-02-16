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

    public TripleATKControl(DriveSubsystem sDrive,
            ElevatorSubsystem sElevator) {
        super(sDrive, sElevator);
        atkl = new JStick(0);
        atkr = new JStick(1);
        atka = new JStick(2);

        isCheesy = false;
    }

    public void teleopInit() {
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
        elevatorSub.changeMode(!atka.isPressed(1));
        
        // aux ATK y manually moves the elevator (while the trigger is pressed)
        elevatorSub.setSpeed(-atka.getAxis(JStick.ATK3_Y));
        
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
    }
}
