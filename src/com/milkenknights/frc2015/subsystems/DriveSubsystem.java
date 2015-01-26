package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.Drive;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that manages the robot's wheels.
 * 
 * @author Daniel Kessler
 * @author Jake Reiner
 */
public class DriveSubsystem extends MSubsystem {
    Drive drive;
        
    boolean slowMode;
    boolean reverseMode;
    boolean runPID;
    boolean runGyro;
    
    double leftSpeed;
    double rightSpeed;
    
    double cheesyPower;
    double cheesyTurn;
    boolean cheesyQuickturn;
    
    PIDController pid_l;
    PIDController pid_r;
    
    Encoder enc_l;
    Encoder enc_r;
    
    double leftSpeedPID;
    double rightSpeedPID;
    
    private enum DriveMode {
        TANK, CHEESY, PIDSTRAIGHT
    }
    
    DriveMode driveMode;
    
    public DriveSubsystem() {
        CANTalon leftTalonA = new CANTalon(Constants.leftTalonDeviceNumberA);
        CANTalon leftTalonB = new CANTalon(Constants.leftTalonDeviceNumberB);
        CANTalon leftTalonC = new CANTalon(Constants.leftTalonDeviceNumberC);
        
        CANTalon rightTalonA = new CANTalon(Constants.rightTalonDeviceNumberA);
        CANTalon rightTalonB = new CANTalon(Constants.rightTalonDeviceNumberB);
        CANTalon rightTalonC = new CANTalon(Constants.rightTalonDeviceNumberC);
        
        CANTalon[] leftWheels = {leftTalonA, leftTalonB, leftTalonC};
        CANTalon[] rightWheels = {rightTalonA, rightTalonB, rightTalonC};
        
        enc_l = new Encoder(0, 1);
        enc_r = new Encoder(7, 6);
        
        enc_l.setDistancePerPulse(Constants.inchesPerPulse);
        enc_r.setDistancePerPulse(Constants.inchesPerPulse);
        
        drive = new Drive(leftWheels, rightWheels,
                Constants.reversedLeftTalons, Constants.reversedRightTalons);
        
        class LPIDOut implements PIDOutput {
            @Override
            public void pidWrite(double output) {
                leftSpeedPID = output;
            }
        }
        class RPIDOut implements PIDOutput {
            @Override
            public void pidWrite(double output) {
                rightSpeedPID = output;
            }
        }
        
        pid_l = new PIDController(Constants.pidStraightP,
                Constants.pidStraightI,
                Constants.pidStraightD,
                enc_l,
                new LPIDOut());
        pid_l.setOutputRange(Constants.minimumWheelSpeed, 1);
        pid_r = new PIDController(Constants.pidStraightP,
                Constants.pidStraightI,
                Constants.pidStraightD,
                enc_r,
                new RPIDOut());
        pid_r.setOutputRange(Constants.minimumWheelSpeed, 1);
    }
    
    public void teleopInit() {
        reverseMode = false;
    }
    
    private void setDriveMode(DriveMode mode) {
        driveMode = mode;
    }

    /**
     * Set the speeds of the left and right side of the robot.
     * 
     * @param left The desired speed of the robot's left side.
     * @param right The desired speed of the robot's right side.
     */
    public void tankDrive(double left, double right) {
        leftSpeed = left;
        rightSpeed = right;
        setDriveMode(DriveMode.TANK);
    }
    
    /**
     * Set robot speeds based on CheesyDrive.
     * 
     * {@inheritDoc Drive#cheesyDrive(double, double, boolean)}
     */
    public void cheesyDrive(double power, double turn, boolean quickturn) {
        drive.cheesyDrive(power, turn, quickturn);
        setDriveMode(DriveMode.CHEESY);
    }
    
    /**
     * Set the PID constants for driving straight
     */
    public void setStraightPID(double p, double i, double d) {
        pid_l.setPID(p, i, d);
        pid_r.setPID(p, i, d);
    }
    
    /**
     * Set the setpoint for PID straight driving mode.
     * This should be how far forward you want the robot to move.
     *
     * @param setpoint The desired PID straight setpoint.
     */
    public void setStraightPIDSetpoint(double setpoint) {
        pid_l.setSetpoint(setpoint);
        pid_r.setSetpoint(setpoint);
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
     * Go to the setpoint that we have set for driving straight.
     */
    public void startStraightPID() {
        setDriveMode(DriveMode.PIDSTRAIGHT);
        pid_l.enable();
        pid_r.enable();
    }

    /**
     * Set the setpoint for PID pivot mode.
     * This should be the angle you want the robot to be facing.
     * NOT IMPLEMENTED
     *
     * @param setpoint The desired PID angle setpoint.
     */
    public void setPivotPIDSetpoint(double setpoint) {

    }

    /**
     * Get whatever we set the pivot setpoint to be.
     * NOT IMPLEMENTED
     *
     * @return The last set PID pivot setpoint.
     */
    public double getPivotPIDSetpoint() {
        return 0;
    }

    /**
     * Go to the setpoint that we have set for pivoting.
     * NOT IMPLEMENTED
     */
    public void startPivotPID() {

    }

    /**
     * "Zero out" our position. If the robot has moved forward or rotated, this will
     * reset the position back to zero.
     */
    public void resetPIDPosition() {
        enc_l.reset();
        enc_r.reset();
    }

    /**
     * Find out if we have reached our PID target.
     *
     * @param threshold How close/precise we want to be
     * @return true if we have reached the target
     */
    public boolean pidOnTarget(double threshold) {
        return Math.abs(pid_l.getError()) <= threshold &&
                Math.abs(pid_r.getError()) <= threshold;
    }
    
    public void moveIndividualMotor(boolean isLeft, double amount) {
        if (isLeft) {
            
        }
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
            drive.tankDrive(leftSpeedPID, rightSpeedPID);
            break;
        }
        
        SmartDashboard.putNumber("l ticks", enc_l.getRaw());
        SmartDashboard.putNumber("r ticks", enc_r.getRaw());
        SmartDashboard.putNumber("l dist", enc_l.pidGet());
        SmartDashboard.putNumber("r dist", enc_r.pidGet());
    }
}
