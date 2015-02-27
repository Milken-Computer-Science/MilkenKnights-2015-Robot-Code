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
   
    ActuatorsState actuatorsState;
    WheelsState wheelsState;
    
    public GroundIntakeSubsystem() {
        leftTalon = new CANTalon(Constants.groundIntakeLeftTalonDeviceNumber);
        rightTalon = new CANTalon(Constants.groundIntakeRightTalonDeviceNumber);
        actuators = new SolenoidPair(
                Constants.groundIntakeFirstActuatorDeviceNumber,
                Constants.groundIntakeSecondActuatorDeviceNumber,
                false);
        
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
            leftTalon.set(-Constants.groundIntakeTalonSpeed);
            rightTalon.set(Constants.groundIntakeTalonSpeed);
            break;
        case OUTPUT:
            leftTalon.set(Constants.groundIntakeTalonSpeed);
            rightTalon.set(-Constants.groundIntakeTalonSpeed);
            break;
        case SLOW_INTAKE:
            leftTalon.set(-Constants.groundIntakeTalonSlowSpeed);
            rightTalon.set(Constants.groundIntakeTalonSlowSpeed);
            break;
        case RIGHT:
            leftTalon.set(-Constants.groundIntakeTalonSpeed);
            rightTalon.set(-Constants.groundIntakeTalonSpeed);
            break;
        default:
            leftTalon.set(0);
            rightTalon.set(0);
            break;
        }
        
        actuators.set(actuatorsState.b);
    }
}
