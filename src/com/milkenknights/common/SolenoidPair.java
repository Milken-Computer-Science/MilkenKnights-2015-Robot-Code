package com.milkenknights.common;

import edu.wpi.first.wpilibj.Solenoid;

public class SolenoidPair {
        private Solenoid sa;
        private Solenoid sb;
        
        private boolean state;
        private boolean onA;
        private boolean onB;
        
        public boolean get() {
                return state;
        }
        
        public void set(boolean on) {
                state = on;
                sa.set(onA == on);
                sb.set(onB == on);
        }
        
        public void toggle() {
                state = !state;
                set(state);
        }

        public SolenoidPair(int a, int b, boolean onA, boolean onB, boolean initial) {
                sa = new Solenoid(a);
                sb = new Solenoid(b);
        
                this.onA = onA;
                this.onB = onB;

                set(initial);
        }
}
