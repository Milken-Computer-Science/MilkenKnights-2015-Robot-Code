package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.AutonomousAction.EndState;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;
import com.milkenknights.frc2015.subsystems.Subsystems;

import edu.wpi.first.wpilibj.Timer;

/** Background immediately, and outtake ("button 9") after a certain amount of seconds. */
public class OuttakeAtTime extends AutonomousAction {
    Subsystems subsystems;
    double waitTime;
    
    double startTime;
    
    public OuttakeAtTime(Subsystems subsytems, double waitTime) {
        this.subsystems = subsytems;
        this.waitTime = waitTime;
    }

    @Override
    protected void startCode() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    protected EndState periodicCode() {
        if (Timer.getFPGATimestamp() - startTime >= waitTime - 0.25) {
            subsystems.groundIntake().setActuators(GroundIntakeSubsystem.ActuatorsState.CLOSED);
            subsystems.drive().setStraightPIDSetpoint(20);
        }
        if (Timer.getFPGATimestamp() - startTime >= waitTime) {
            subsystems.groundIntake().setWheelsState(GroundIntakeSubsystem.WheelsState.OUTPUT);
            return EndState.END;
        } else {
            return EndState.BACKGROUND;
        }
    }
}
