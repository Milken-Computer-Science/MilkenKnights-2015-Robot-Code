package com.milkenknights.frc2015;

import com.milkenknights.common.PIDGains;

/** A listing of constants/settings used throughout the robot code. */
public class Constants {
    // CAN Device Numbers
    public static final int groundIntakeLeftTalonDeviceNumber = 1;
    public static final int leftElevatorTalonDeviceNumber = 2;
    public static final int leftTalonDeviceNumberA = 3; // CIM
    public static final int leftTalonDeviceNumberB = 4; // CIM
    public static final int leftTalonDeviceNumberC = 5; // 550
    public static final int rightTalonDeviceNumberC = 6; // 550
    public static final int rightTalonDeviceNumberB = 7; // CIM
    public static final int rightTalonDeviceNumberA = 8; // CIM
    public static final int rightElevatorTalonDeviceNumber = 9;
    public static final int groundIntakeRightTalonDeviceNumber = 10;
    
    // Analog Ports
    public static final int pressureTransducerChannel = 0;
    
    // DIO Ports
    public static final int hallEffectSensorDeviceNumber = 9;
    public static final int elevatorLeftEncoderDeviceNumberA = 4;
    public static final int elevatorLeftEncoderDeviceNumberB = 5;
    public static final int elevatorRightEncoderDeviceNumberA = 6;
    public static final int elevatorRightEncoderDeviceNumberB = 7;
    public static final int driveLeftEncoderDeviceNumberA = 0;
    public static final int driveLeftEncoderDeviceNumberB = 1;
    public static final int driveRightEncoderDeviceNumberA = 2;
    public static final int driveRightEncoderDeviceNumberB = 3;
    public static final int bannerSensorWhiteDeviceNumber = 10;
    public static final int bannerSensorBlackDeviceNumber = 11;
    
    // solenoid ports
    public static final int groundIntakeFirstActuatorDeviceNumber = 0;
    public static final int groundIntakeSecondActuatorDeviceNumber = 1;

    // Pressure Transducer
    public static final double transducerScaleFactor = 50;
    public static final double transducerOffset = -25;
    
    public static final int imuBaudRate = 57600;
    
    // 4 inch wheel diameter. encoder does 360 pulses per revolution
    public static final double driveInchesPerPulse = 4 * Math.PI / 360;
    
    public static final double elevatorInchesPerPulse = 1.25 * Math.PI / 360;
    
    // PID constants for driving straight
    public static final PIDGains driveStraightPID =
            new PIDGains(0.012, 0.00001, 0.005);
    public static final PIDGains drivePivotPID =
            new PIDGains(0.0085, 0.00001, 0);
    
    public static final double minimumWheelSpeed = 0.0069;

    // GroundIntake talon speed
    public static final double groundIntakeTalonSpeed = 1;
    public static final double groundIntakeTalonSlowSpeed = .5;
    


    // Elevator PID constants
    public static final PIDGains elevatorPID = new PIDGains(0.195, 0, 0);
    
    public static final double elevatorResetDistance = 0.25;
    public static final double elevatorMaxDistance = 35;
    public static final double elevatorMinDistance = 0;
    
    public static final double elevatorScoringPlatformHeight = 3;
    public static final double elevatorReadyToIntakeHeight = 24;
    public static final double elevatorStepHeight = 12;
    public static final double elevatorTote1Height = 14;
    public static final double elevatorTote2Height = 28;
    
    // autonomous Threshold
    public static final double elevatorThreshold = 0.35;
}
