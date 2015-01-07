package com.milkenknights.common;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * A Solenoid Pair is two solenoids that always have the same state.
 * @see RestrictedSolenoid
 * 
 * @author Daniel Kessler
 */
public class RestrictedSolenoidPair extends RestrictedSolenoid {
    Solenoid solenoidB;

    /**
     * Makes a new RestrictedSolenoidPair.
     * @param channela The channel of solenoid A.
     * @param channelb The channel of solenoid B.
     * @param initialState If this pair should be on upon initialization.
     * @param requiredOnPressure The pressure required to set this double solenoid to true.
     * @param requiredOffPressure The pressure required to set this double solenoid to false.
     */
    public RestrictedSolenoidPair(int channela, int channelb,
            boolean initialState,
            double requiredOnPressure, double requiredOffPressure) {
        super(channela, initialState, requiredOnPressure, requiredOffPressure);
        
        solenoidB = new Solenoid(channelb);
    }

    public void forceSet(boolean state) {
        super.forceSet(state);
        
        solenoidB.set(state);
    }
}
