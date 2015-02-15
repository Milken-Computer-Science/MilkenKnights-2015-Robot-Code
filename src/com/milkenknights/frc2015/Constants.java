package com.milkenknights.frc2015;

/** A listing of constants/settings used throughout the robot code. */
public class Constants {
    // CAN Device Numbers
    public static final int leftElevatorTalonDeviceNumber = 2;
    public static final int leftTalonDeviceNumberA = 3; // CIM
    public static final int leftTalonDeviceNumberB = 4; // CIM
    public static final int leftTalonDeviceNumberC = 5; // 550
    public static final int rightTalonDeviceNumberC = 6; // 550
    public static final int rightTalonDeviceNumberB = 7; // CIM
    public static final int rightTalonDeviceNumberA = 8; // CIM
    public static final int rightElevatorTalonDeviceNumber = 9;
    
    // these correspond to index numbers, so A->0, B->1, and C->2
    // these are the wheels that should be reversed
    public static final int[] reversedLeftTalons = {2};
    public static final int[] reversedRightTalons = {0,1};
    
    // Analog Ports
    public static final int pressureTransducerChannel = 0;
    
    // DIO Ports
    public static final int hallEffectSensorDeviceNumber = 9;
    public static final int elevatorLeftEncoderDeviceNumberA = 6;
    public static final int elevatorLeftEncoderDeviceNumberB = 7;
    public static final int elevatorRightEncoderDeviceNumberA = 4;
    public static final int elevatorRightEncoderDeviceNumberB = 5;
    public static final int driveLeftEncoderDeviceNumberA = 0;
    public static final int driveLeftEncoderDeviceNumberB = 1;
    public static final int driveRightEncoderDeviceNumberA = 2;
    public static final int driveRightEncoderDeviceNumberB = 3;
    
    // Pressure Transducer
    public static final double transducerScaleFactor = 50;
    public static final double transducerOffset = -25;
    
    // 4 inch wheel diameter. encoder does 360 pulses per revolution
    // This is equal to 4pi/360
    public static final double driveInchesPerPulse = 0.03490658503;
    
    // INCORRECT VALUE. FIX THIS
    public static final double elevatorInchesPerPulse = 0.01114794;
    
    // PID constants for driving straight
    public static final double pidStraightP = 0.1;
    public static final double pidStraightI = 0.01;
    public static final double pidStraightD = 0.001;
    
    public static final double minimumWheelSpeed = 0.0069;
}
