package com.milkenknights.common;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Pneumatic appliances on our robot (solenoids) generally require a certain
 * amount of accumulated pressure to be used. A solenoid cannot work properly
 * if our compressor doesn't have enough pressure, and attempting to use them
 * in this circumstance can cause leakages.  The RestrictedSolenoid class fixes
 * this by providing static methods that check for compressor pressure, and
 * will not actuate solenoids unless the required pressure is obtained.
 * 
 * @author Daniel Kessler
 *
 */
public class RestrictedSolenoid extends Solenoid {
    private static AnalogInput pressureTransducer;

    private static double transducerScaleFactor = 50;
    private static double transducerOffset = -25;

    /**
     * Gets the amount of pressure in the pneumatic system.
     * 
     * TOBE Tested
     * 
     * @return PSI of the pneumatic system
     */
    public static double getPressure() {
        return pressureTransducer.getVoltage()*transducerScaleFactor
                + transducerOffset;
    }

    /**
     * This should be called before using getPressure() or any of the setter
     * functions.
     */
    public static void initPneumaticSystem() {
        pressureTransducer = new AnalogInput(0);
    }

    private double onp;
    private double offp;

    /**
     * Makes a new RestrictedSolenoid.
     * @param channel The solenoid's channel on the PCM to control.
     * @param initialState If this solenoid should be on upon initialization.
     * @param requiredOnPressure The pressure required to turn this solenoid on.
     * @param requiredOffPressure The pressure required to turn this solenoid off.
     */
    public RestrictedSolenoid(int channel, boolean initialState,
            double requiredOnPressure, double requiredOffPressure) {
        super(channel);

        onp = requiredOnPressure;
        offp = requiredOffPressure;

        set(initialState);
    }

    /**
     * Gets the pressure required to turn this solenoid on.
     */
    public double getRequiredOnPressure() {
        return onp;
    }

    /**
     * Gets the pressure required to turn this solenoid off.
     */
    public double getRequiredOffPressure() {
        return offp;
    }

    /**
     * Returns true if we have enough pressure to toggle this solenoid's state.
     */
    public boolean okayToToggle() {
        return (get() && getRequiredOffPressure() > getPressure()) ||
            (!get() && getRequiredOnPressure() > getPressure());
    }

    /**
     * Changes the solenoid to the specified state, but only if the required
     * pressure has been accumulated.  Does nothing if there is not enough
     * pressure.
     * @param state The desired state.
     */
    public void set(boolean state) {
        if (okayToToggle()) {
            forceSet(state);
        }
    }

    /**
     * Changes the solenoid to the specified state, regardless of whether
     * the required pressure has been accumulated.
     * @param state The desired state.
     */
    public void forceSet(boolean state) {
        super.set(state);
    }

    /**
     * Toggles the solenoid's state regardless of whether the required
     * pressure has been accumulated.
     */
    public final void forceToggle() {
        forceSet(!get());
    }
}
