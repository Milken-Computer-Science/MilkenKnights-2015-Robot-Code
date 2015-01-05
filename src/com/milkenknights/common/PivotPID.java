package com.milkenknights.common;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * This class wraps a PIDController and does some behind-the-scenes work to
 * make the robot spin to a desired angle.
 * @author Daniel Kessler
 */
public class PivotPID {
    public final PIDController pid;
    
    /**
     *  this class pretends to be a single PIDOutput, but is really two outputs
     *  that always move in opposite directions, causing robot pivoting.
     */
    private static class OppositeDirPIDO implements PIDOutput {
        private PIDOutput l;
        private PIDOutput r;
        public final boolean isReversed;

        public OppositeDirPIDO(PIDOutput left, PIDOutput right,
                boolean reversed) {
            l = left;
            r = right;
            isReversed = reversed;
        }

        public void pidWrite(double output) {
            l.pidWrite(output * (isReversed ? -1 : 1));
            r.pidWrite(output * (isReversed ? 1 : -1));
        }
    }

    /**
     * This class wraps a Gyro and rewrites the pidGet method to return a value
     * between -180 and 180. It ensures the robot will take the shortest pivot
     * path (e.g. if we are at 355 degrees and we want to go to 10 degrees, the
     * reported angle will be wrapped to -5 instead of 355).
     */
    private class GyroAngleMod implements PIDSource {
        private Gyro gyro;

        public GyroAngleMod(Gyro gyro) {
            this.gyro = gyro;
        }

        public double pidGet() {
            double rawAngle = gyro.getAngle() % 360;

            int mod360 = (int) ((pid.getSetpoint() - rawAngle) / 180);

            return rawAngle + (mod360 * 360);
        }

    }

    /**
     * Constructor parameters are the same as the ones for PIDController,
     * except the PIDSource must be a Gyro and we reqiure two outputs, one for
     * each side of the robot.
     */
    public PivotPID(double Kp, double Ki, double Kd, double Kf, Gyro source,
            PIDOutput left, PIDOutput right) {
        pid = new PIDController(Kp, Ki, Kd, Kf, new GyroAngleMod(source),
                new OppositeDirPIDO(left, right, false));
    }

    public PivotPID(double Kp, double Ki, double Kd, double Kf, Gyro source,
            PIDOutput left, PIDOutput right, double period) {
        pid = new PIDController(Kp, Ki, Kd, Kf, new GyroAngleMod(source),
                new OppositeDirPIDO(left, right, false), period);
    }

    public PivotPID(double Kp, double Ki, double Kd, Gyro source,
            PIDOutput left, PIDOutput right) {
        pid = new PIDController(Kp, Ki, Kd, new GyroAngleMod(source),
                new OppositeDirPIDO(left, right, false));
    }

    public PivotPID(double Kp, double Ki, double Kd, Gyro source,
            PIDOutput left, PIDOutput right, double period) {
        pid = new PIDController(Kp, Ki, Kd, new GyroAngleMod(source),
                new OppositeDirPIDO(left, right, false), period);
    }

}
