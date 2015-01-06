package com.milkenknights.common;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticSystem {
    
    private final AnalogInput pressureTransducer;
    
    private final double transducerScaleFactor = 50;
    private final double transducerOffset = -25;
    
    public PneumaticSystem() {
        pressureTransducer = new AnalogInput(0);
    }
    
    /**
     * Gets the amount of pressure we have
     * 
     * @return  PSI of the pneumatic system
     */
    public double getPressure() {
        return pressureTransducer.getVoltage()*transducerScaleFactor + transducerOffset;
    }

    public abstract class RestrictedSolenoid  {
        /** Gets the current state of the solenoid */
        public abstract boolean get();

        /**
         * Gets the pressure required to turn this solenoid on.
         */
        public abstract double getRequiredOnPressure();

        /**
         * Gets the pressure required to turn this solenoid off.
         */
        public abstract double getRequiredOffPressure();
        
        /**
         * Returns true if we have enough pressure to toggle this solenoid's state.
         */
        public final boolean okayToToggle() {
            return (get() && getRequiredOffPressure() > getPressure()) ||
                    (!get() && getRequiredOnPressure() > getPressure());
        }
        
        /**
         * Changes the solenoid to the specified state, but only if the required
         * pressure has been accumulated. Does nothing if there is not enough
         * pressure.
         * @param state The desired state.
         */
        public final void set(boolean state) {
            if (okayToToggle()) {
                forceSet(state);
            }
        }
        
        /**
         * Toggles the solenoid to the opposite state that it is currently in, but only
         * if the required pressure has been accumulated. Does nothing if there is not
         * enough pressure.
         */
        public final void toggle() {
            set(!get());
        }

        /**
         * Changes the solenoid to the specified state, regardless of whether
         * the required pressure has been accumulated.
         * @param state The desired state.
         */
        public abstract void forceSet(boolean state);

        /**
         * Toggles the solenoid's state regardless of whether the required
         * pressure has been accumulated.
         */
        public final void forceToggle() {
            forceSet(!get());
        }
    }
    
    public class RestrictedSingleSolenoid extends RestrictedSolenoid {
        private Solenoid s;
        private double onp;
        private double offp;
        
        /**
         * Makes a new RestrictedSingleSolenoid.
         * @param channel The solenoid's channel on the PCM to control.
         * @param initialState If this solenoid should be on upon initialization.
         * @param requiredOnPressure The pressure required to turn this solenoid on.
         * @param requiredOffPressure The pressure required to turn this solenoid off.
         */
        public RestrictedSingleSolenoid(int channel, boolean initialState,
                double requiredOnPressure, double requiredOffPressure) {
            s = new Solenoid(channel);
            onp = requiredOnPressure;
            offp = requiredOffPressure;
            
            forceSet(initialState);
        }

        public boolean get() {
            return s.get();
        }

        public double getRequiredOnPressure() {
            return onp;
        }

        public double getRequiredOffPressure() {
            return offp;
        }

        public void forceSet(boolean state) {
            s.set(state);
        }
    }

    /**
     * A Double Solenoid is a pair of solenoids that always have opposite 
     * states.
     */
    public class RestrictedDoubleSolenoid extends RestrictedSolenoid {
        private Solenoid sola;
        private Solenoid solb;

        private double onPressure;
        private double offPressure;

        /**
         * Makes a new RestrictedDoubleSolenoid.
         * @param sachannel The channel of solenoid A. When this DoubleSolenoid
         *                  is set to true, this solenoid will be on.
         * @param sbchannel The channel of solenoid B. When this DoubleSolenoid
         *                  is set to true, this solenoid will be off.
         * @param initialState If this should be on upon initialization.
         * @param requiredOnPressure The pressure required to set this double solenoid to true.
         * @param requiredOffPressure The pressure required to set this double solenoid to false.
         */
        public RestrictedDoubleSolenoid(int sachannel, int sbchannel,
                boolean initialState,
                double requiredOnPressure, double requiredOffPressure) {
            onPressure = requiredOnPressure;
            offPressure = requiredOffPressure;

            sola = new Solenoid(sachannel);
            solb = new Solenoid(sbchannel);

            forceSet(initialState);
        }

        public boolean get() {
            return sola.get();
        }

        public double getRequiredOnPressure() {
            return onPressure;
        }

        public double getRequiredOffPressure() {
            return offPressure;
        }

        public void forceSet(boolean s) {
            sola.set(s);
            solb.set(!s);
        }
    }
    
    /**
     * A Solenoid Pair is two solenoids that always have the same state.
     */
    public class RestrictedSolenoidPair extends RestrictedSolenoid {
        private Solenoid sola;
        private Solenoid solb;

        private double onPressure;
        private double offPressure;

        /**
         * Makes a new RestrictedSolenoidPair.
         * @param sachannel The channel of solenoid A.
         * @param sbchannel The channel of solenoid B.
         * @param initialState If this should be on upon initialization.
         * @param requiredOnPressure The pressure required to set this pair to true.
         * @param requiredOffPressure The pressure required to set this pair to false.
         */
        public RestrictedSolenoidPair(int sachannel, int sbchannel,
                boolean initialState,
                double requiredOnPressure, double requiredOffPressure) {
            onPressure = requiredOnPressure;
            offPressure = requiredOffPressure;

            sola = new Solenoid(sachannel);
            solb = new Solenoid(sbchannel);

            forceSet(initialState);
        }

        public boolean get() {
            return sola.get();
        }

        public double getRequiredOnPressure() {
            return onPressure;
        }

        public double getRequiredOffPressure() {
            return offPressure;
        }

        public void forceSet(boolean s) {
            sola.set(s);
            solb.set(s);
        }
    }
}
