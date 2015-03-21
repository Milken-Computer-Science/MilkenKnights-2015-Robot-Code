package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.SolenoidPair;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

public class GroundIntakeSubsystem extends MSubsystem {
    CANTalon rightTalon;
    CANTalon leftTalon;
    
    SolenoidPair actuators;
    
    public enum WheelsState {
        INTAKE, OUTPUT, SLOW_INTAKE, RIGHT, STOPPED
    }
    
    public enum ActuatorsState {
        CLOSED(false), OPEN(true);

        public final boolean b;
        
        private ActuatorsState(boolean b) {
            this.b = b;
        }
    }
   
    private ActuatorsState actuatorsState;
    private WheelsState wheelsState;
    
    public GroundIntakeSubsystem() {
        leftTalon = new CANTalon(Constants.CAN.GROUNDINTAKE_LEFT_TALON);
        rightTalon = new CANTalon(Constants.CAN.GROUNDINTAKE_RIGHT_TALON);
        actuators = new SolenoidPair(Constants.SOLENOID.GROUNDINTAKE_LEFT, 
                Constants.SOLENOID.GROUNDINTAKE_RIGHT, false);
        
        actuatorsState = ActuatorsState.CLOSED;
        wheelsState = WheelsState.STOPPED;
    }
    
    /**
     * Start moving the wheels forward, backward, or stop them.
     * @param wheelsState The desired state of the wheels
     */
    public void setWheelsState(WheelsState wheelsState) {
        this.wheelsState = wheelsState;
    }
    
    /**
     * Get the most recently set state of the intake wheels
     * @return The most recently set state of the intake wheels
     */
    public WheelsState getWheelsState() {
        return wheelsState;
    }
    
    /**
     * Change the state of the actuators
     * @param s If this is true, close the actuators
     */
    public void setActuators(ActuatorsState s) {
        actuatorsState = s;
    }
    
    /**
     * Get the most recently set state of the actuators
     * @return The most recently set state of the actuators
     */
    public ActuatorsState getActuatorsState() {
        return actuatorsState;
    }
    
    public void update() {
        switch (wheelsState) {
        case INTAKE:
            leftTalon.set(-Constants.GROUND_INTAKE.INTAKE_SPEED);
            rightTalon.set(Constants.GROUND_INTAKE.INTAKE_SPEED);
            break;
        case OUTPUT:
            leftTalon.set(Constants.GROUND_INTAKE.INTAKE_SPEED);
            rightTalon.set(-Constants.GROUND_INTAKE.INTAKE_SPEED);
            break;
        case SLOW_INTAKE:
            leftTalon.set(-Constants.GROUND_INTAKE.INTAKE_SLOW_SPEED);
            rightTalon.set(Constants.GROUND_INTAKE.INTAKE_SLOW_SPEED);
            break;
        case RIGHT:
            leftTalon.set(-Constants.GROUND_INTAKE.INTAKE_SPEED);
            rightTalon.set(-Constants.GROUND_INTAKE.INTAKE_SPEED);
            break;
        default:
            leftTalon.set(0);
            rightTalon.set(0);
            break;
        }
        
        actuators.set(actuatorsState.b);
    }
}
