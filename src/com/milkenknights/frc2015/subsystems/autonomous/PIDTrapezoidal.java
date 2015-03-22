package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.DriveSubsystem.DriveMode;
import com.milkenknights.frc2015.subsystems.Subsystems;

import edu.wpi.first.wpilibj.Timer;

/**
 * This action will make the robot's velocity follow a trapezoidal path--
 * accelerating towards a maximum velocity, coasting, then eventually
 * decelerating down to zero.
 *
 * @author Daniel
 */
public class PIDTrapezoidal extends AutonomousAction {
    DriveSubsystem driveSubsystem;
    
    double rampUpTime;
    double rampDownTime;
    double coastTime;
    double maxVelocity;
    
    double tolerance;
    
    double startTime;
    double startDist;
    
    /**
     * Makes a trapezoidal motion controller. The trapezoid will be on a
     * velocity-time graph with points at (0,0), (rampUpTime, maxVelocity),
     * (rampUpTime+coastTime, maxVelocity), (rampUpTime+coastTime+rampDownTime,0).
     * The robot will travel a distance of
     *   (maxVelocity/2)(2*coastTime + rampUpTime + rampDownTime)
     */
    public PIDTrapezoidal(Subsystems subsystems,
            double rampUpTime, double rampDownTime,
            double coastTime, double maxVelocity,
            double tolerance) {
        this.driveSubsystem = subsystems.drive();
        
        this.rampUpTime = rampUpTime;
        this.rampDownTime = rampDownTime;
        this.coastTime = coastTime;
        this.maxVelocity = maxVelocity;
        
        this.tolerance = tolerance;
    }
    
    private double distanceFunction(double time) {
        if (time < rampUpTime) {
            return 0.5 * (maxVelocity / rampUpTime) * Math.pow(time, 2);
        } else if (time < rampUpTime + coastTime) {
            // (1/2)*(maxVelocity/rampUpTime)*rampUpTime^2 +
            //         (time-rampUpTime)*maxVelocity
            // simplifies to this:
            return maxVelocity * (time - 0.5*rampUpTime);
        } else if (time < rampUpTime + coastTime + rampDownTime) {
            // more fun simplification
            return maxVelocity * (time - 0.5*rampUpTime) -
                    0.5 * (maxVelocity / rampDownTime) *
                    Math.pow(time - rampUpTime - coastTime, 2);
        } else {
            // just return the final desired distance
            return (maxVelocity/2)*(2*coastTime + rampUpTime + rampDownTime);
        }
    }
    
    @Override
    public void startCode() {
        startTime = Timer.getFPGATimestamp();
        startDist = driveSubsystem.getEncPosition();
        
        driveSubsystem.setDriveMode(DriveMode.PIDSTRAIGHT);
    }

    @Override
    public EndState periodicCode() {
        double currentTime = Timer.getFPGATimestamp() - startTime;
        
        driveSubsystem.setStraightPIDSetpoint(distanceFunction(currentTime) + startDist);
        
        if (currentTime >= rampUpTime + coastTime + rampDownTime &&
                driveSubsystem.pidOnTarget(tolerance)) {
            return EndState.END;
        } else {
            return EndState.CONTINUE;
        }
    }

}
