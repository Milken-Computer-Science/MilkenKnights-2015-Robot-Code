package com.milkenknights.common;

public class PIDGains {
    public final double kp;
    public final double ki;
    public final double kd;
    
    public PIDGains(double p, double i, double d) {
        kp = p;
        ki = i;
        kd = d;
    }
}
