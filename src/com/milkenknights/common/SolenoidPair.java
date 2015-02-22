package com.milkenknights.common;

import edu.wpi.first.wpilibj.Solenoid;

public class SolenoidPair {
    
    Solenoid sol_a;
    Solenoid sol_b;

    public SolenoidPair(int channelA, int channelB, boolean state) {
        sol_a = new Solenoid(channelA);
        sol_b = new Solenoid(channelB);
        set(state);
    }
    
    public void set(boolean b) {
        sol_a.set(b);
        sol_b.set(b);
    }
    
    public boolean get() {
        return sol_a.get();
    }

}
