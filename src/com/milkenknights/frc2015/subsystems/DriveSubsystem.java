package com.milkenknights.frc2015.subsystems;

import com.kauailabs.nav6.frc.IMU;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that manages the robot's wheels.
 * 
 */
public class DriveSubsystem extends MSubsystem {
    RobotDrive drive;

    Encoder encLeft;
    // Removed encRight because it is physically broken.
    //Encoder encRight;

    IMU gyro;

    double leftSpeed;
    double rightSpeed;

    double pidStraightSetpoint;
    double pidPivotSetpoint;
    
    private double pidLimit = 1;

    public enum DriveMode {
        TANK, PIDSTRAIGHT, PIDPIVOT
    }

    DriveMode driveMode;

    public DriveSubsystem() {
        CANTalon leftTalonA = new CANTalon(Constants.CAN.DRIVE_LEFT_A_TALON);
        CANTalon leftTalonB = new CANTalon(Constants.CAN.DRIVE_LEFT_B_TALON);

        CANTalon rightTalonA = new CANTalon(Constants.CAN.DRIVE_RIGHT_A_TALON);
        CANTalon rightTalonB = new CANTalon(Constants.CAN.DRIVE_RIGHT_B_TALON);

        drive = new RobotDrive(leftTalonA, rightTalonA);

        encLeft = new Encoder(Constants.DIO.DRIVE_LEFT_ENCODER_A,
                Constants.DIO.DRIVE_LEFT_ENCODER_B);
        //encRight = new Encoder(Constants.driveRightEncoderDeviceNumberA, Constants.driveRightEncoderDeviceNumberB);

        gyro = new IMU(new SerialPort(Constants.GYRO.IMU_BAUD_RATE, SerialPort.Port.kMXP));

        leftTalonB.changeControlMode(ControlMode.Follower);
        rightTalonB.changeControlMode(ControlMode.Follower);

        leftTalonB.set(leftTalonA.getDeviceID());
        rightTalonB.set(rightTalonA.getDeviceID());

        encLeft.setDistancePerPulse(-Constants.DRIVE.INCHES_PER_PULSE);
        //encRight.setDistancePerPulse(Constants.DRIVE.INCHES_PER_PULSE);

        driveMode = DriveMode.TANK;
    }

    /**
     * Change the current drive mode
     * 
     * @param mode
     *            The mode to change to
     */
    public void setDriveMode(DriveMode mode) {
        driveMode = mode;
    }

    /**
     * Set the speeds of the left and right side of the robot.
     * 
     * @param left
     *            The desired speed of the robot's left side.
     * @param right
     *            The desired speed of the robot's right side.
     */
    public void tankDrive(double left, double right) {
        leftSpeed = left;
        rightSpeed = right;
    }

    /**
     * Set the setpoint for PID straight driving mode. This should be how far forward you want the
     * robot to move.  By default, sets the limit to 1.
     *
     * @param setpoint The desired PID straight setpoint.
     */
    public void setStraightPIDSetpoint(double setpoint) {
        setStraightPIDSetpoint(setpoint, 1);
    }
    
    /**
     * Set the setpoint for PID straight driving mode. This should be how far forward you want the
     * robot to move.  Also has the option to limit the speed of the robot.
     * @param setpoint The desired PID straight sepoint.
     * @param maxSpeed The maximum speed the robot should travel at-- should be between 0 and 1.
     */
    public void setStraightPIDSetpoint(double setpoint, double speedLimit) {
        pidStraightSetpoint = setpoint;
        pidLimit = speedLimit;
    }

    /**
     * Get whatever we set the straight drive setpoint to be.
     *
     * @return The last set PID straight setpoint.
     */
    public double getStraightPIDSetpoint() {
        return pidStraightSetpoint;
    }

    /**
     * Set the setpoint for PID pivot mode. This should be the angle you want
     * the robot to be facing.
     *
     * @param setpoint The desired PID angle setpoint, between -180 and 180.
     */
    public void setPivotPIDSetpoint(double setpoint) {
        pidPivotSetpoint = ((setpoint + 180) % 360) - 180;
    }

    /**
     * Get whatever we set the pivot setpoint to be.
     *
     * @return The last set PID pivot setpoint.
     */
    public double getPivotPIDSetpoint() {
        return pidPivotSetpoint;
    }

    /**
     * "Zero out" our position. If the robot has moved forward or rotated, this
     * will reset the position back to zero.
     */
    public void resetStraightPIDPosition() {
        encLeft.reset();
        //encRight.reset();
    }

    /**
     * Find out if we have reached our PID target.
     *
     * @param threshold How close/precise we want to be
     * @return true if we have reached the target. false if we are not in a PID
     *         mode at all
     */
    public boolean pidOnTarget(double threshold) {
        if (driveMode == DriveMode.PIDPIVOT) {
            return Math.abs(pivotPIDError()) <= threshold;
        } else if (driveMode == DriveMode.PIDSTRAIGHT) {
            return Math.abs(getStraightPIDSetpoint() - getEncPosition()) <= threshold;
        }
        return false;
    }

    /**
     * Gets the current error of the pivot PID controller.  This has direction
     * @return The number of degrees of error
     */
    public double pivotPIDError() {
        return ((pidPivotSetpoint - gyro.pidGet() + 180) % 360) - 180;
    }
    
    /**
     * Gets the angle that the robot is facing relative to the floor.
     * @return The angle that the robot is facing relative to the floor.
     */
    public double getYaw() {
        return gyro.getYaw();
    }

    /**
     * Return the encoder position
     * @return the encoder distance
     */
    public double getEncPosition() {
        return encLeft.getDistance();
    }
    
    /**
     * Get the (absolute value) speed of the left encoder, in inches per second.
     */
    public double getEncSpeed() {
        return Math.abs(encLeft.getRate());
    }
    
    
    /**
     * Zeroes the yaw of the gyro.
     */
    public void zeroGyroYaw() {
        gyro.zeroYaw();
    }
    
    /**
     * Bounds a value to a certain number
     * 
     * @param val
     *            The value to bound
     * @param lim
     *            The bound
     * @return The bounded number
     */
    private double limit(double val, double lim) {
        if (Math.abs(val) <= lim) {
            return val;
        } else if (val > 0) {
            return lim;
        } else if (val < 0) {
            return -lim;
        } else {
            return 0;
        }
    }

    /**
     * Updates wheel speeds depending on driveMode (which should be set to the
     * desired mode with setDriveMode(). This method should be called during
     * every loop no matter what.
     */
    public void update() {
        switch (driveMode) {
        case TANK:
            drive.tankDrive(leftSpeed, rightSpeed, true);
            break;
        case PIDSTRAIGHT:
            double outputMagnitude = (getStraightPIDSetpoint() - encLeft.pidGet()) * Constants.DRIVE.STRAIGHT_P;
            
            double curve = pivotPIDError() * Constants.DRIVE.PIVOT_P;
            if (outputMagnitude < 0) {
                curve = -curve;
            }
            
            double ff = 0;
            if (encLeft.getRate() >= 0) {
                ff = Constants.DRIVE.STRAIGHT_F;
            } else if (encLeft.getRate() < 0) {
                ff = -Constants.DRIVE.STRAIGHT_F;
            }
            
            drive.drive(limit(outputMagnitude + ff, pidLimit), curve);
            break;
        case PIDPIVOT:
            double m_result = Constants.DRIVE.PIVOT_P * pivotPIDError();

            if (m_result > 1) {
                m_result = 1;
            } else if (m_result < -1) {
                m_result = -1;
            }

            drive.tankDrive(m_result, -m_result);
            break;
        }

        SmartDashboard.putNumber("Drive Distance", encLeft.pidGet());
        SmartDashboard.putNumber("Drive Speed", encLeft.getRate());
        //SmartDashboard.putNumber("r dist", encRight.pidGet());
        SmartDashboard.putNumber("Gyro Yaw", gyro.pidGet());
        SmartDashboard.putNumber("Pivot Setpoint", pidPivotSetpoint);
        SmartDashboard.putString("Drive Mode", driveMode.toString());
        SmartDashboard.putNumber("Drive Straight Setpoint", getStraightPIDSetpoint());
    }
}
