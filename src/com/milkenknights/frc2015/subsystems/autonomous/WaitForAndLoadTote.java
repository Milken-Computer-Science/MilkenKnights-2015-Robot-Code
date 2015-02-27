package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.AutonomousAction.EndState;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

public class WaitForAndLoadTote extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    GroundIntakeSubsystem groundIntakeSubsystem;
    
    int stage;
    
    public WaitForAndLoadTote(ElevatorSubsystem elevatorSubsystem,
            GroundIntakeSubsystem groundIntakeSubsystem) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.groundIntakeSubsystem = groundIntakeSubsystem;
    }
    
    private double ultrasonicReading() {
        return 0;
    }
    
    @Override
    public void start() {
        stage = -1;
    }

    @Override
    public EndState run() {
        if (stage == -1) {
            stage++;
            return EndState.BACKGROUND;
        } else if (stage == 0) {
            if (ultrasonicReading() > 12) {
                stage++;
                groundIntakeSubsystem.setActuators(
                        GroundIntakeSubsystem.ActuatorsState.CLOSED);
                groundIntakeSubsystem.setWheelsState(
                        GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
            }
        } else if (stage == 1) {
            // wait for banner OR check ultrasonic sensor
            // this might be changed
            if (elevatorSubsystem.toteLoaded()) {
                elevatorSubsystem.setSetpoint(0);
                elevatorSubsystem.enablePID(true);
                stage++;
            }
        } else if (stage == 2) {
            if (elevatorSubsystem.getPosition() < 0.01) {
                groundIntakeSubsystem.setActuators(
                        GroundIntakeSubsystem.ActuatorsState.OPEN);
                elevatorSubsystem.setSetpoint(Constants.elevatorReadyToIntakeHeight);
                return EndState.END;
            }
        }
        return EndState.CONTINUE;
    }
}
