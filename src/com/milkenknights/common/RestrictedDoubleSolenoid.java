package com.milkenknights.common;

import edu.wpi.first.wpilibj.Solenoid;

/**
 * A double solenoid is a pair of solenoids that always have opposite states.
 * @see RestrictedSolenoid
 * 
 * @author Daniel Kessler
 */
public class RestrictedDoubleSolenoid extends RestrictedSolenoid {
    Solenoid solenoidB;
    
    /**
     * Makes a new RestrictedDoubleSolenoid.
     * @param channela The channel of solenoid A. When this DoubleSolenoid
     *                 is set to true, this solenoid will be on.
     * @param channelb The channel of solenoid B. When this DoubleSolenoid
     *                 is set to true, this solenoid will be off.
     * @param initialState If this should be on upon initialization.
     * @param requiredOnPressure The pressure required to set this double solenoid to true.
     * @param requiredOffPressure The pressure required to set this double solenoid to false.
     */
    public RestrictedDoubleSolenoid(int channela, int channelb,
            boolean initialState,
            double requiredOnPressure, double requiredOffPressure) {
        super(channela, initialState, requiredOnPressure, requiredOffPressure);
        
        solenoidB = new Solenoid(channelb);
    }
    
    public void forceSet(boolean state) {
        super.forceSet(state);
        
        solenoidB.set(!state);
    }
}
