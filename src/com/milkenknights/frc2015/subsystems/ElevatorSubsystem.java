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

    boolean pidMode = true;
    double setpoint = 0;
    double manSpeed = 0;

    public enum FlapsState {
        CLOSED(false), OPEN(true);

        public final boolean b;

        private FlapsState(boolean b) {
            this.b = b;
        }
    }

    FlapsState flapsState;

    public ElevatorSubsystem() {
        elevatorTalonLeft = new CANTalon(Constants.CAN.ELEVATOR_LEFT_TALON);
        elevatorTalonRight = new CANTalon(Constants.CAN.ELEVATOR_RIGHT_TALON);

        flaps = new Solenoid(Constants.SOLENOID.ELEVATOR_FLAPS);

        encLeft = new Encoder(Constants.DIO.ELEVATOR_LEFT_ENCODER_A,
                Constants.DIO.ELEVATOR_LEFT_ENCODER_B);
        encRight = new Encoder(Constants.DIO.ELEVATOR_RIGHT_ENCODER_A,
                Constants.DIO.ELEVATOR_RIGHT_ENCODER_B);

        bannerSensor = new DigitalInput(Constants.DIO.ELEVATOR_BANNER_BLACK);
        hallEffectSensor = new DigitalInput(Constants.DIO.ELEVATOR_HALL_EFFECT);

        encLeft.setDistancePerPulse(Constants.ELEVATOR.INCHES_PER_PULSE);
        encRight.setDistancePerPulse(-Constants.ELEVATOR.INCHES_PER_PULSE);

        flapsState = FlapsState.CLOSED;
    }

    /**
     * Set the elevator flaps
     * 
     * @param s
     *            The status of the flaps
     */
    public void setFlapsState(FlapsState s) {
        flapsState = s;
    }

    /**
     * Get the current state of the flaps
     * 
     * @return The state of the flaps
     */
    public FlapsState getFlapsState() {
        return flapsState;
    }

    /**
     * Get the average elevator encoder position
     * 
     * @return the average elevator encoder position.
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
     * Set the setpoint of the elevator. This is bounded by the maximum and
     * minimum values of the elevator.
     * 
     * @param setpoint
     *            The desired setpoint of the elevator.
     */
    public void setSetpoint(double setpoint) {
        if (setpoint >= Constants.ELEVATOR.HEIGHTS.MAX) {
            this.setpoint = Constants.ELEVATOR.HEIGHTS.MAX;
        } else if (setpoint <= Constants.ELEVATOR.HEIGHTS.MIN) {
            this.setpoint = Constants.ELEVATOR.HEIGHTS.MIN;
        } else {
            this.setpoint = setpoint;
        }
    }

    /**
     * Returns if the elevator is at its lowest point by using the hall effect
     * sensor
     * 
     * @return If the elevator is zeroed
     */
    public boolean isElevatorZero() {
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

    /**
     * Bounds a value to a certain number
     * 
     * @param val
     *            The value to bound
     * @param lim
     *            The bound
     * @return The bounded number
     */
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

    /**
     * Set if PID is enabled or not
     * 
     * @param b
     *            If PID should be enabled
     */
    public void setPIDMode(boolean b) {
        pidMode = b;
    }

    /**
     * Gets if the elevator is in PID mode
     * 
     * @return the current mode of the elevator
     */
    public boolean getPIDMode() {
        return pidMode;
    }

    /**
     * Set the manual speed of the elevator
     * 
     * @param speed
     *            The speed
     */
    public void setManualSpeed(double speed) {
        manSpeed = speed;
    }

    public void update() {
        if (isElevatorZero()) {
            resetEncoder();
        }

        if (pidMode) {
            double l_error = (setpoint - encLeft.pidGet());
            double r_error = (setpoint - encRight.pidGet());
            
            double l_speed = limit(l_error * Constants.ELEVATOR.P, .85);
            double r_speed = limit(r_error * Constants.ELEVATOR.P, .85);
            
            double l_steer = limit((r_error - l_error) * Constants.ELEVATOR.STEERING_P, .15);
            double r_steer = limit((l_error - r_error) * Constants.ELEVATOR.STEERING_P, .15);

            elevatorTalonLeft.set(l_speed + l_steer);
            elevatorTalonRight.set(-(r_speed + r_steer));
        } else {
            elevatorTalonLeft.set(manSpeed);
            elevatorTalonRight.set(-manSpeed);
        }

        flaps.set(flapsState.b);

        SmartDashboard.putBoolean("Elevator Zeroed", isElevatorZero());
        SmartDashboard.putBoolean("Tote Loaded", toteLoaded());
        SmartDashboard.putNumber("Elevator Left Distance", encLeft.getDistance());
        SmartDashboard.putNumber("Elevator Right Distance", encRight.getDistance());
        SmartDashboard.putNumber("Elevator Setpoint", setpoint);
    }
}