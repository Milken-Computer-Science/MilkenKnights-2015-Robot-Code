package com.milkenknights.common;

import edu.wpi.first.wpilibj.Solenoid;

public class PneumaticSystem {
    /**
     * Gets the amount of pressure we have. TO BE IMPLEMENTED
     */
    public double getPressure() {
        return 0.0;
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
         * Changes the solenoid to the specified state, but only if the rquired
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
         * @param requiredOnPressure The pressure required to turn this solenoid on.
         * @param requiredOffPressure The pressure required to turn this solenoid off.
         */
        public RestrictedSingleSolenoid(int channel, double requiredOnPressure,
                double requiredOffPressure) {
            s = new Solenoid(channel);
            onp = requiredOnPressure;
            offp = requiredOffPressure;
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

    public class RestrictedDoubleSolenoid extends RestrictedSolenoid {
        private Solenoid sola;
        private Solenoid solb;

        private boolean onA;
        private boolean onB;

        private double onPressure;
        private double offPressure;

        private boolean state;

        /**
         * Makes a new RestrictedDoubleSolenoid.
         * @param sachannel The channel of solenoid A.
         * @param sbchannel The channel of solenoid B.
         * @param onA Whether solenoid A should be on or off when this pair is
         *            set to true.
         * @param onB Whether solenoid B should be on or off when this pair is
         *            set to true.
         * @param requiredOnPressure The pressure required to set this pair to true.
         * @param requiredOffPressure The pressure required to set this pair to false.
         */
        public RestrictedDoubleSolenoid(int sachannel, int sbchannel,
                boolean onA, boolean onB,
                double requiredOnPressure, double requiredOffPressure) {
            onPressure = requiredOnPressure;
            offPressure = requiredOffPressure;

            this.onA = onA;
            this.onB = onB;

            sola = new Solenoid(sachannel);
            solb = new Solenoid(sbchannel);

            forceSet(false);
        }

        public boolean get() {
            return state;
        }

        public double getRequiredOnPressure() {
            return onPressure;
        }

        public double getRequiredOffPressure() {
            return offPressure;
        }

        public void forceSet(boolean s) {
            state = s;

            sola.set(onA == s);
            solb.set(onB == s);
        }
    }
}
