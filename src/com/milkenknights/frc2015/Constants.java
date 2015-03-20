package com.milkenknights.frc2015;

/** A listing of constants/settings used throughout the robot code. */
public class Constants {
    public static final boolean DEBUGLOG_ENABLED = true; //Enables and disables the debug log
    public static final int     DEBUGLOG_DEFAULT_LOGLEVEL = 5; //Controls the standard logging level for the DebugLog
    public static final int     DEBUGLOG_INFO_DISPLAYFREQ = 100; //Controls how often the DebugLog displays diagnostic information
    public static boolean       INTRO_MESSAGE = true; //Whether to display the welcome message
    
    // CAN Device Numbers
    public static final int groundIntakeLeftTalonDeviceNumber = 1;
    public static final int leftElevatorTalonDeviceNumber = 2;
    public static final int leftTalonDeviceNumberA = 3;
    public static final int leftTalonDeviceNumberB = 4;
    public static final int rightTalonDeviceNumberB = 7;
    public static final int rightTalonDeviceNumberA = 8;
    public static final int rightElevatorTalonDeviceNumber = 9;
    public static final int groundIntakeRightTalonDeviceNumber = 10;
    public static final int binGrabberTalonDeviceNumber = 6;
    
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
    public static final int bannerSensorBlackDeviceNumber = 8;
    
    // solenoid ports
    public static final int groundIntakeLeftActuatorDeviceNumber = 0;
    public static final int groundIntakeRightActuatorDeviceNumber = 1;
    public static final int elevatorActuatorDeviceNumber = 2;

    // Pressure Transducer
    public static final double transducerScaleFactor = 50;
    public static final double transducerOffset = -25;
    
    // Gyro
    public static final int imuBaudRate = 57600;
    public static final double gyroMaximumInput = 180;
    public static final double gyroMinimumInput = -180;
    
    // Drive Constants
    public static final double driveInchesPerPulse = 4 * Math.PI / 360;
    public static final double elevatorInchesPerPulse = 1.25 * Math.PI / 360;

    public static final double driveStraightP = 0.015;
    public static final double driveStraightF = 0.18;
    public static final double driveSteeringP = 0.002;
    public static final double drivePivotP = 0.041;

    // GroundIntake Constants
    public static final double groundIntakeTalonSpeed = 1;
    public static final double groundIntakeTalonSlowSpeed = .5;
    
    // Elevator Constants
    public static final double elevatorP = 0.277;
    public static final double elevatorSteeringP = 0.1;
    public static final double elevatorFF = 0.12;
    
    public static final double elevatorResetDistance = 0.2;
    public static final double elevatorMaxDistance = 36;
    public static final double elevatorMinDistance = 0;
    
    public static final double elevatorScoringPlatformHeight = 3;
    public static final double elevatorReadyToIntakeHeight = 21;
    public static final double elevatorStepHeight = 12;
    public static final double elevatorTote1Height = 14;
    public static final double elevatorTote2Height = 28.65;

    public static final double elevatorThreshold = 0.5;
}
