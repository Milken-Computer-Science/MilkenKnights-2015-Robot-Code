package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.SolenoidPair;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;

public class GroundIntakeSubsystem extends MSubsystem {
    CANTalon rightTalon;
    CANTalon leftTalon;
    
    SolenoidPair actuators;
    
    public enum WheelsState {
        INTAKE, OUTPUT, SLOW_INTAKE, STOPPED
    }
    
    public enum ActuatorsState {
        CLOSED, OPEN
    }
    
    WheelsState wheelsState;
    ActuatorsState actuatorsState;
    
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
        setWheelsState(WheelsState.INTAKE);
        setActuators(ActuatorsState.OPEN);
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
        if (wheelsState == WheelsState.INTAKE) {
            leftTalon.set(-Constants.groundIntakeTalonSpeed);
        } else if (wheelsState == WheelsState.OUTPUT) {
            leftTalon.set(Constants.groundIntakeTalonSpeed);
        } else if (wheelsState == WheelsState.SLOW_INTAKE) {
            leftTalon.set(Constants.groundIntakeTalonSlowSpeed);
        } else {
            leftTalon.set(0);
        }
        actuators.set(actuatorsState == ActuatorsState.OPEN ? true : false);
    }
}
