package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.Drive;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.SolenoidPair;
import com.milkenknights.frc2015.Constants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;

/**
 * The subsystem that manages the robot's wheels and gear solenoids.
 * 
 * @author Daniel Kessler
 * @author Jake Reiner
 */
public class DriveSubsystem extends MSubsystem {
    Drive drive;
    SolenoidPair driveGear;
        
    boolean normalDriveGear;
    boolean slowMode;
    boolean reverseMode;
    boolean runPID;
    boolean runGyro;
    
    double leftSpeed;
    double rightSpeed;
    
    double cheesyPower;
    double cheesyTurn;
    boolean cheesyQuickturn;
    
    private enum DriveMode {
        TANK, CHEESY, PIDSTRAIGHT
    }
    
    DriveMode driveMode;
    
    public DriveSubsystem() {
        // TODO: replace these with config values
        CANTalon leftWheels = new CANTalon(Constants.leftTalonDeviceNumber);
        CANTalon rightWheels = new CANTalon(Constants.rightTalonDeviceNumber);

        drive = new Drive(leftWheels, rightWheels);
        // this solenoid pair is TRUE if the robot is in high gear
        driveGear = new SolenoidPair(Constants.gearSolenoidAChannel,
                Constants.gearSolenoidBChannel, true, false, true);
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
     * Set the setpoint for PID straight driving mode.
     * This should be how far forward you want the robot to move.
     * NOT IMPLEMENTED
     *
     * @param setpoint The desired PID straight setpoint.
     */
    public void setStraightPIDSetpoint(double setpoint) {
        
    }

    /**
     * Get whatever we set the setpoint to be.
     * NOT IMPLEMENTED
     *
     * @return The last set PID straight setpoint.
     */
    public double getStraightPIDSetpoint() {
        return 0;
    }
    
    /**
     * Find out if we have reached our PID target.
     * NOT IMPLEMENTED
     *
     * @param threshold How close/presice we want to be
     * @return true if we have reachde the target
     */
    public boolean pidOnTarget(double threshold) {
        return false;
    }

    /**
     * "Zero out" our position. If the robot has moved forward or rotated, this will
     * reset the position back to zero.
     * NOT IMPLEMENTED
     */
    public void resetPIDPosition() {

    }

    /**
     * Go to the setpoint that we have set.
     * NOT IMPLEMENTED
     */
    public void startStraightPID() {

    }
    
    /** Changes the gear solenoids to the opposite of whatever they are. */
    public void toggleGear() {
        driveGear.toggle();
    }
    
    /**
     * Change the gear solenoids to either high gear or low gear.
     * @param g A value of true means high gear; false means low gear.
     */
    public void setGear(boolean g) {
        driveGear.set(g);
    }
    
    /**
     * Get the current state of the gear solenoids.
     * @return true if we are in high gear.
     */
    public boolean getGearState() {
        return driveGear.get();
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
            
        case CHEESY:
            drive.cheesyDrive(cheesyPower, cheesyTurn, cheesyQuickturn);
        }
    }
}
