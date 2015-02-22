package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.SolenoidPair;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class GroundIntakeSubsystem extends MSubsystem {
    CANTalon rightTalon;
    CANTalon leftTalon;
    
    boolean actuatorsState;
    
    /** when this is false, the actuators are opened */
    SolenoidPair actuators;
    
    public enum WheelsState {
        FORWARD, BACKWARD, STOPPED
    }
    
    WheelsState wheelsState;
    
    public GroundIntakeSubsystem() {
        leftTalon = new CANTalon(Constants.groundIntakeLeftTalonDeviceNumber);
        rightTalon = new CANTalon(Constants.groundIntakeRightTalonDeviceNumber);
        actuators = new SolenoidPair(
                Constants.groundIntakeFirstActuatorDeviceNumber,
                Constants.groundIntakeSecondActuatorDeviceNumber,
                false);
        
        rightTalon.changeControlMode(ControlMode.Follower);
        rightTalon.set(leftTalon.getDeviceID());
        rightTalon.reverseOutput(true);
    }
    
    public void open() {
        setWheelsState(WheelsState.FORWARD);
        setActuators(true);
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
    public void setActuators(boolean s) {
        actuatorsState = s;
    }
    
    /**
     * Get the most recently set state of the actuators
     * @return The most recently set state of the actuators
     */
    public boolean getActuatorsState() {
        return actuatorsState;
    }
    
    public void update() {
        if (wheelsState == WheelsState.FORWARD) {
            leftTalon.set(-Constants.groundIntakeTalonSpeed);
        } else if (wheelsState == WheelsState.BACKWARD) {
            leftTalon.set(Constants.groundIntakeTalonSpeed);
        } else {
            leftTalon.set(0);
        }
        actuators.set(actuatorsState);
    }
}
