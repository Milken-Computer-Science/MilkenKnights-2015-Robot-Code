package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;

/**
 * A fake ultrasonic sensor, whose sensor reading can be whatever we want it to
 * be.
 * @author Daniel
 */
public class FakeUltrasonic implements WaitForAndLoadTote.UltrasonicSensor {
    double r = 0;
    
    @Override
    public double reading() {
        return r;
    }
    
    public void setReading(double r) {
        this.r = r;
    }
    
    class SetReadingAction extends AutonomousAction {
        double a;
        public SetReadingAction(double a) {
            this.a = a;
        }
        @Override
        public void start() {
            setReading(a);
        }

        @Override
        public EndState run() {
            return EndState.END;
        }
    }
    
    public AutonomousAction setReadingAction(double r) {
        return new SetReadingAction(r);
    }
}

