package com.milkenknights.common;

import edu.wpi.first.wpilibj.CANTalon;

public class MKCANTalon extends CANTalon {
    
    public MKCANTalon(int deviceNumber) {
        super(deviceNumber);
        // TODO Auto-generated constructor stub
    }
    
    public MKCANTalon(int deviceNumber, int controlPeriodMs) {
        super(deviceNumber, controlPeriodMs);
        // TODO Auto-generated constructor stub
    }

    boolean reverseOutput = false;
    
    @Override
    public void set(double outputValue) {
        if (!reverseOutput) {
            super.set(outputValue);
        } else {
            super.set(-outputValue);
        }
    }
    
    public void reversePolarity(boolean reversed) {
        reverseOutput = reversed;
    }
}
