package com.milkenknights.frc2015;

/** A listing of constants/settings used throughout the robot code. */
public class Constants {
    // CAN Device Numbers
    public static final int leftTalonDeviceNumberA = 1;
    public static final int leftTalonDeviceNumberB = 2;
    public static final int leftTalonDeviceNumberC = 3;
    public static final int rightTalonDeviceNumberA = 4;
    public static final int rightTalonDeviceNumberB = 5;
    public static final int rightTalonDeviceNumberC = 6;
    
    public static final int elevatorTalonDeviceNumber = 7;
    
    // DIO Ports
    public static final int pressureTransducerChannel = 0;
    
    // Pressure Transducer
    public static final double transducerScaleFactor = 50;
    public static final double transducerOffset = -25;
}
