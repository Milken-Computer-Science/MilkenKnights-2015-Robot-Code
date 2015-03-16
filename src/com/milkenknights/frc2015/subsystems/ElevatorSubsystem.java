package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.DebugLogger;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that controls the elevator.
 * 
 * @author Jake Reiner
 */
public class ElevatorSubsystem extends MSubsystem {
    /** when this returns true, a tote has been loaded */
    DigitalInput bannerSensor;
    CANTalon elevatorTalonLeft;

    CANTalon elevatorTalonRight;
    Encoder encLeft;
    Encoder encRight;

    /** false means the elevator is at its lowest point */
    DigitalInput hallEffectSensor;

    Solenoid flaps;

    boolean resetMode = false;
    boolean pidMode = true;
    double setpoint = 0;
    double manSpeed = 0;

    public enum ActuatorsState {
        CLOSED(false), OPEN(true);

        public final boolean b;

        private ActuatorsState(boolean b) {
            this.b = b;
        }
    }

    ActuatorsState flapsState;

    public ElevatorSubsystem() {
        elevatorTalonLeft = new CANTalon(
                Constants.leftElevatorTalonDeviceNumber);
        elevatorTalonRight = new CANTalon(
                Constants.rightElevatorTalonDeviceNumber);

        flaps = new Solenoid(Constants.elevatorActuatorDeviceNumber);

        encLeft = new Encoder(Constants.elevatorLeftEncoderDeviceNumberA,
                Constants.elevatorLeftEncoderDeviceNumberB);
        encRight = new Encoder(Constants.elevatorRightEncoderDeviceNumberA,
                Constants.elevatorRightEncoderDeviceNumberB);

        bannerSensor = new DigitalInput(Constants.bannerSensorBlackDeviceNumber);

        hallEffectSensor = new DigitalInput(
                Constants.hallEffectSensorDeviceNumber);

        encLeft.setDistancePerPulse(Constants.elevatorInchesPerPulse);
        encRight.setDistancePerPulse(-Constants.elevatorInchesPerPulse);

        flapsState = ActuatorsState.CLOSED;
    }

    /**
     * Set the elevator flaps
     * 
     * @param s
     *            The status of the flaps
     */
    public void setFlapsState(ActuatorsState s) {
        flapsState = s;
    }

    /**
     * Get the current state of the flaps
     * 
     * @return The state of the flaps
     */
    public ActuatorsState getFlapsState() {
        return flapsState;
    }

    /**
     * If the robot is in reset mode, this will prematurely end the reset.
     */
    public void abortReset() {
        resetMode = false;
    }

    /**
     * Get the elevator encoder position
     * 
     * @return the elevator encoder position.
     */
    public double getPosition() {
        return (encLeft.getDistance() + encRight.getDistance()) / 2;
    }

    /**
     * Get the current setpoint of the elevator
     * 
     * @return The current setpoint
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * Reset the encoder to zero. This should only be called if we know that the
     * elevator is its lowest point.
     */
    public void resetEncoder() {
        encLeft.reset();
        encRight.reset();
    }

    /**
     * Triggers the robot to go in reset mode. In reset mode, the elevator will
     * slowly decrease the setpoint thus lowering the elevator. When the robot
     * is in reset mode, it will not react to any other controls until it is
     * either finished, or if the reset is manually halted by calling
     * abortReset().
     */
    public void resetPosition() {
        resetMode = true;
    }

    public boolean hallEffectSensor() {
        return hallEffectSensor.get();
    }

    /**
     * Set the setpoint of the elevator. This is bounded by the maximum and
     * minimum values of the elevator.
     * 
     * @param setpoint
     *            The desired setpoint of the elevator.
     */
    public void setSetpoint(double setpoint) {
        if (setpoint >= Constants.elevatorMaxDistance) {
            this.setpoint = Constants.elevatorMaxDistance;
        } else if (setpoint <= Constants.elevatorMinDistance) {
            this.setpoint = Constants.elevatorMinDistance;
        } else {
            this.setpoint = setpoint;
        }
    }

    public boolean elevatorZero() {
        return !hallEffectSensor.get();
    }

    /**
     * Tells us if a tote is ready to be picked up by the elevator
     * 
     * @return true if the tote is loaded
     */
    public boolean toteLoaded() {
        return bannerSensor.get();
    }

    private double limit(double val, double lim) {
        if (Math.abs(val) <= lim) {
            return val;
        } else if (val > 0) {
            return lim;
        } else if (val < 0) {
            return -lim;
        } else {
            return 0;
        }
    }

    public void setPIDMode(boolean b) {
        pidMode = b;
    }

    public boolean getPIDMode() {
        return pidMode;
    }

    public void setManSpeed(double d) {
        manSpeed = d;
    }

    public void update() {
        if (resetMode) {
            setpoint -= Constants.elevatorResetDistance;
        }

        if (!hallEffectSensor.get()) {
            resetEncoder();
            resetMode = false;
            if (setpoint < 0) {
                setSetpoint(0);
            }
        }

        if (pidMode) {
            double l_error = (setpoint - encLeft.pidGet());
            double r_error = (setpoint - encRight.pidGet());

            double ff;

            if (encLeft.getRate() < 0) {
                ff = Constants.elevatorFF;
            } else {
                ff = 0;
            }

            elevatorTalonLeft.set(limit(
                    limit(l_error * Constants.elevatorP + ff, .9)
                            + limit(((l_error - r_error) / 2)
                                    * Constants.elevatorSteeringP, .1), 1));
            elevatorTalonRight.set(-limit(
                    limit(r_error * Constants.elevatorP + ff, .9)
                            + limit(((r_error - l_error) / 2)
                                    * Constants.elevatorSteeringP, .1), 1));
        } else {
            elevatorTalonLeft.set(manSpeed);
            elevatorTalonRight.set(-manSpeed);
        }

        flaps.set(flapsState.b);

        SmartDashboard.putBoolean("Elevator Reset Mode", resetMode);
        SmartDashboard.putBoolean("Tote Loaded", toteLoaded());
        SmartDashboard.putNumber("elevator left dist", encLeft.getDistance());
        SmartDashboard.putNumber("elevator right dist", encRight.getDistance());
        SmartDashboard.putNumber("elevator setpoint", setpoint);
    }
}