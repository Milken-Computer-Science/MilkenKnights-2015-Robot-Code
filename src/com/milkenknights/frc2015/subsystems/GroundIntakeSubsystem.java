package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoidPair;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;

public class GroundIntakeSubsystem extends MSubsystem {
    CANTalon rightTalon;
    CANTalon leftTalon;
    
    boolean actuatorsState;
    
    /** when this is false, the actuators are opened */
    RestrictedSolenoidPair actuators;
    
    public enum WheelsState {
        FORWARD, BACKWARD, STOPPED
    }
    
    WheelsState wheelsState;
    
    public GroundIntakeSubsystem() {
        leftTalon = new CANTalon(Constants.groundIntakeLeftTalonDeviceNumber);
        rightTalon = new CANTalon(Constants.groundIntakeRightTalonDeviceNumber);
        actuators = new RestrictedSolenoidPair(
                Constants.groundIntakeFirstActuatorDeviceNumber,
                Constants.groundIntakeSecondActuatorDeviceNumber,
                false, 0, 0);
    }
    
    /**
     * Start moving the wheels forward, backward, or stop them.
     * @param wheelsState The desired state of the wheels
     */
    public void setWheelsState(WheelsState wheelsState) {
        this.wheelsState = wheelsState;
    }
    
    /**
     * Change the state of the actuators
     * @param s If this is true, close the actuators
     */
    public void setActuators(boolean s) {
        actuatorsState = s;
    }
    
    public void update() {
        if (wheelsState == WheelsState.FORWARD) {
            leftTalon.set(Constants.groundIntakeTalonSpeed);
            rightTalon.set(Constants.groundIntakeTalonSpeed);
        } else if (wheelsState == WheelsState.BACKWARD) {
            leftTalon.set(-Constants.groundIntakeTalonSpeed);
            rightTalon.set(-Constants.groundIntakeTalonSpeed);
        } else {
            leftTalon.set(0);
            rightTalon.set(0);
        }
        actuators.set(actuatorsState);
    }
}
