package com.milkenknights.frc2015.subsystems;

import com.milkenknights.common.Drive;
import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.SolenoidPair;

import edu.wpi.first.wpilibj.CANTalon;

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
		TANK, CHEESY
	}
	
	DriveMode driveMode;
	
	public DriveSubsystem(int leftPort, int rightPort) {
		drive = new Drive(new CANTalon(leftPort), new CANTalon(rightPort));
		// this solenoid pair is TRUE if the robot is in high gear
		driveGear = new SolenoidPair(2, 1, true, false, true);
	}
	
	public void teleopInit() {
		reverseMode = false;
	}
	
	private void setDriveMode(DriveMode mode) {
		driveMode = mode;
	}

	public void tankDrive(double left, double right) {
		leftSpeed = left;
		rightSpeed = right;
		setDriveMode(DriveMode.TANK);
	}
	
	public void cheesyDrive(double power, double turn, boolean quickturn) {
		drive.cheesyDrive(power, turn, quickturn);
		setDriveMode(DriveMode.CHEESY);
	}
	
	public void setStraightPIDSetpoint(double setpoint) {
		//leftPID.changeSetpoint(setpoint);
		//rightPID.changeSetpoint(setpoint);
	}
	
	/*
	public boolean pidOnTarget(double threshold) {
		return leftPID.onTarget(threshold) && rightPID.onTarget(threshold);
	}
	*/
	
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