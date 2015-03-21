package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.Constants;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem;
import com.milkenknights.frc2015.subsystems.ElevatorSubsystem.ActuatorsState;
import com.milkenknights.frc2015.subsystems.GroundIntakeSubsystem;

/**
 * Backgrounds immediately and carries out the following procedure:
 * 
 * - Wait for ultrasonic to be less than 12 inches
 * - Close intake actuators
 * - Set wheels to slow intake
 * - Wait for banner / wait for ultrasonic to be less than 2
 * - Drop elevator to 0
 * - Set intake to open
 * - Raise elevator to intake height (ends immediately setting setpoint)
 * @author Daniel
 */
public class WaitForAndLoadTote extends AutonomousAction {
    ElevatorSubsystem elevatorSubsystem;
    GroundIntakeSubsystem groundIntakeSubsystem;
    
    int stage;
    
    public interface UltrasonicSensor {
        public double reading();
    }
    
    UltrasonicSensor ultrasonic;
    
    public WaitForAndLoadTote(ElevatorSubsystem elevatorSubsystem,
            GroundIntakeSubsystem groundIntakeSubsystem,
            UltrasonicSensor ultrasonic) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.groundIntakeSubsystem = groundIntakeSubsystem;
        
        this.ultrasonic = ultrasonic;
    }
    
    
    @Override
    public void startCode() {
        stage = -1;
    }

    @Override
    public EndState periodicCode() {
        if (stage == -1) {
            stage++;
            return EndState.BACKGROUND;
        } else if (stage == 0) {
            if (ultrasonic.reading() <= 12) {
                stage++;
                System.out.println("WAITING FOR A TOTE");
                groundIntakeSubsystem.setActuators(
                        GroundIntakeSubsystem.ActuatorsState.CLOSED);
                groundIntakeSubsystem.setWheelsState(
                        GroundIntakeSubsystem.WheelsState.SLOW_INTAKE);
            }
        } else if (stage == 1) {
            // wait for banner OR check if ultrasonic sensor is less than 2
            // this might be changed
            if (elevatorSubsystem.toteLoaded()) {
                System.out.println("FOUND TOTE");
                elevatorSubsystem.setSetpoint(0);
                elevatorSubsystem.setPIDMode(true);
                stage++;
            }
        } else if (stage == 2) {
            if (elevatorSubsystem.getPosition() < 14) {
                elevatorSubsystem.setFlapsState(ActuatorsState.OPEN);
            }
            if (elevatorSubsystem.getPosition() < Constants.ELEVATOR.ACCURACY_THRESHOLD) {
                groundIntakeSubsystem.setActuators(
                        GroundIntakeSubsystem.ActuatorsState.OPEN);
                elevatorSubsystem.setSetpoint(Constants.ELEVATOR.HEIGHTS.READY_TO_INTAKE);
                elevatorSubsystem.setFlapsState(ActuatorsState.CLOSED);
                System.out.println("done");
                return EndState.END;
            }
        }
        return EndState.CONTINUE;
    }
}
