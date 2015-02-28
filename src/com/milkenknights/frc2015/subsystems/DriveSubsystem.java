package com.milkenknights.frc2015.subsystems;

import com.kauailabs.nav6.frc.IMU;
import com.milkenknights.common.Drive;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that manages the robot's wheels.
 * 
 * @author Daniel Kessler
 * @author Jake Reiner
 */
public class DriveSubsystem extends MSubsystem {
    Drive drive;
    
    double leftSpeed;
    double rightSpeed;
    
    double cheesyPower;
    double cheesyTurn;
    boolean cheesyQuickturn;
    
    double leftPivotPIDSpeed;
    double rightPivotPIDSpeed;
    
    PIDController pid_l;
    PIDController pid_r;
    PIDController pid_pivot;
    
    Encoder enc_l;
    Encoder enc_r;
    
    IMU gyro;
    
    public enum DriveMode {
        TANK, CHEESY, PIDSTRAIGHT, PIDPIVOT
    }
    
    DriveMode driveMode;
    
    public DriveSubsystem() {
        CANTalon leftTalonA = new CANTalon(Constants.leftTalonDeviceNumberA);
        CANTalon leftTalonB = new CANTalon(Constants.leftTalonDeviceNumberB);
        CANTalon leftTalonC = new CANTalon(Constants.leftTalonDeviceNumberC);
        
        CANTalon rightTalonA = new CANTalon(Constants.rightTalonDeviceNumberA);
        CANTalon rightTalonB = new CANTalon(Constants.rightTalonDeviceNumberB);
        CANTalon rightTalonC = new CANTalon(Constants.rightTalonDeviceNumberC);
        
        leftTalonB.changeControlMode(ControlMode.Follower);
        leftTalonC.changeControlMode(ControlMode.Follower);
        rightTalonB.changeControlMode(ControlMode.Follower);
        rightTalonC.changeControlMode(ControlMode.Follower);
        
        leftTalonC.reverseOutput(true);
        rightTalonC.reverseOutput(true);
        
        leftTalonB.set(leftTalonA.getDeviceID());
        leftTalonC.set(leftTalonA.getDeviceID());
        rightTalonB.set(rightTalonA.getDeviceID());
        rightTalonC.set(rightTalonA.getDeviceID());
        
        enc_l = new Encoder(Constants.driveLeftEncoderDeviceNumberA,
                Constants.driveLeftEncoderDeviceNumberB);
        enc_r = new Encoder(Constants.driveRightEncoderDeviceNumberA,
                Constants.driveRightEncoderDeviceNumberB);
        
        enc_l.setDistancePerPulse(-Constants.driveInchesPerPulse);
        enc_r.setDistancePerPulse(-Constants.driveInchesPerPulse);
        
        driveMode = DriveMode.TANK;
        
        gyro = new IMU(
                new SerialPort(Constants.imuBaudRate, SerialPort.Port.kMXP));
        
        drive = new Drive(leftTalonA, rightTalonA,
                Constants.minimumWheelSpeed);
        
        pid_l = new PIDController(Constants.driveStraightPID.kp,
                Constants.driveStraightPID.ki,
                Constants.driveStraightPID.kd,
                enc_l, leftTalonA);
        pid_r = new PIDController(Constants.driveStraightPID.kp,
                Constants.driveStraightPID.ki,
                Constants.driveStraightPID.kd,
                enc_r, rightTalonA);
        
        class PivotController implements PIDOutput {
            @Override
            public void pidWrite(double output) {
                leftPivotPIDSpeed = output;
                rightPivotPIDSpeed = output;
            }
        }
        
        pid_pivot = new PIDController(Constants.drivePivotPID.kp,
                Constants.drivePivotPID.ki,
                Constants.drivePivotPID.kd,
                gyro,
                new PivotController());
        pid_pivot.setInputRange(-180, 180);
        pid_pivot.setContinuous(true);
    }
    
    /**
     * Change the current drive mode
     * 
     * @param mode The mode to change to
     */
    public void setDriveMode(DriveMode mode) {
        driveMode = mode;
        if (driveMode == DriveMode.PIDSTRAIGHT) {
            pid_l.enable();
            pid_r.enable();
        } else {
            safeDisablePID(pid_l);
            safeDisablePID(pid_r);
        }
        
        if (driveMode == DriveMode.PIDPIVOT) {
            pid_pivot.enable();
        } else {
            safeDisablePID(pid_pivot);
        }
    }

    /**
     * Set the speeds of the left and right side of the robot.
     * 
     * @param left The desired speed of the robot's left side.
     * @param right The desired speed of the robot's right side.
     */
    public void tankDrive(double left, double right) {
        leftSpeed = left;
        rightSpeed = -right;
    }
    
    /**
     * Set robot speeds based on CheesyDrive.
     * 
     * {@inheritDoc Drive#cheesyDrive(double, double, boolean)}
     */
    public void cheesyDrive(double power, double turn, boolean quickturn) {
        //drive.cheesyDrive(power, turn, quickturn);
    }
    
    /**
     * Set the setpoint for PID straight driving mode.
     * This should be how far forward you want the robot to move.
     *
     * @param setpoint The desired PID straight setpoint.
     */
    public void setStraightPIDSetpoint(double setpoint) {
        pid_l.setSetpoint(setpoint);
        pid_r.setSetpoint(-setpoint);
    }

    /**
     * Get whatever we set the straight drive setpoint to be.
     *
     * @return The last set PID straight setpoint.
     */
    public double getStraightPIDSetpoint() {
        return pid_l.getSetpoint();
    }

    /**
     * Set the setpoint for PID pivot mode.
     * This should be the angle you want the robot to be facing.
     *
     * @param setpoint The desired PID angle setpoint, between -180 and 180.
     */
    public void setPivotPIDSetpoint(double setpoint) {
        pid_pivot.setSetpoint(setpoint);
    }

    /**
     * Get whatever we set the pivot setpoint to be.
     *
     * @return The last set PID pivot setpoint.
     */
    public double getPivotPIDSetpoint() {
        return pid_pivot.getSetpoint();
    }

    /**
     * "Zero out" our position. If the robot has moved forward or rotated, this
     * will reset the position back to zero.
     */
    public void resetPIDPosition() {
        enc_l.reset();
        enc_r.reset();
        pid_pivot.reset();
    }

    /**
     * Find out if we have reached our straight PID target.
     *
     * @param threshold How close/precise we want to be
     * @return true if we have reached the target
     */
    public boolean pidOnTarget(double threshold) {
        return Math.abs(pid_l.getError()) <= threshold &&
                Math.abs(pid_r.getError()) <= threshold;
    }
    
    /**
     * Safely disables a PID controller by first checking if it is enabled. 
     * 
     * @param pidController The PID controller
     */
    private void safeDisablePID(PIDController pidController) {
        if (pidController.isEnable()) {
            pidController.disable();
        }
    }

    public void teleopInit() {
        setDriveMode(DriveMode.TANK);
    }
    
    /**
     * Updates wheel speeds depending on driveMode (which should be set to the
     * desired mode with setDriveMode().
     * This method should be called during every loop no matter what.
     */
    public void update() {
        switch (driveMode) {
        case TANK:
            drive.tankDrive(leftSpeed, rightSpeed);
            break;  
        case CHEESY:
            drive.cheesyDrive(cheesyPower, cheesyTurn, cheesyQuickturn);
            break;
        case PIDSTRAIGHT:
            break;
        case PIDPIVOT:
            drive.tankDrive(leftPivotPIDSpeed, rightPivotPIDSpeed);
            break;
        }
        
        SmartDashboard.putNumber("l dist", enc_l.pidGet());
        SmartDashboard.putNumber("r dist", enc_r.pidGet());
        SmartDashboard.putNumber("l setpoint", pid_l.getSetpoint());
        SmartDashboard.putNumber("r setpoint", pid_r.getSetpoint());
        SmartDashboard.putNumber("gyro", gyro.pidGet());
        SmartDashboard.putBoolean("l pid", pid_l.isEnable());
        SmartDashboard.putBoolean("r pid", pid_l.isEnable());
        SmartDashboard.putBoolean("pivot pid", pid_pivot.isEnable());
    }
}
