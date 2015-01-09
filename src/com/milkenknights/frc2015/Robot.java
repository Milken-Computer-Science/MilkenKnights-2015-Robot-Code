package com.milkenknights.frc2015;

import java.util.LinkedList;
import java.util.ListIterator;

import com.milkenknights.common.MSubsystem;
import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.ControlSystem;
import com.milkenknights.frc2015.controls.TripleATKControl;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
    LinkedList<MSubsystem> subsystems;
    
    DriveSubsystem driveSubsystem;

    ControlSystem controlSystem;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.pressureTransducerChannel, 
                Constants.transducerScaleFactor, Constants.transducerOffset);
        driveSubsystem = new DriveSubsystem();

        controlSystem = new TripleATKControl(driveSubsystem);

        subsystems = new LinkedList<MSubsystem>();
        subsystems.add(driveSubsystem);
    }

    private abstract class AutonomousAction {
        /** This will be run once before the action starts */
        public abstract void start();
        /**
         * Run this periodically when this action is active
         * @return true when this action is done
         */
        public abstract boolean run();
    }

    ListIterator<AutonomousAction> autonomousSequence;
    AutonomousAction currentAction;
    Timer autonTimer;

    public void autonomousInit() {
        class PIDStraightAction extends AutonomousAction {
            double setpoint;
            
            public PIDStraightAction(double setpoint) {
                this.setpoint = setpoint;
            }

            @Override
            public void start() {
                driveSubsystem.resetPIDPosition();
                driveSubsystem.setStraightPIDSetpoint(setpoint);
                driveSubsystem.startStraightPID();
            }

            @Override
            public boolean run() {
                return driveSubsystem.pidOnTarget(1); 
            }
        }

        class PIDPivotAction extends AutonomousAction {
            double setpoint;
            
            public PIDPivotAction(double setpoint) {
                this.setpoint = setpoint;
            }

            @Override
            public void start() {
                driveSubsystem.resetPIDPosition();
                driveSubsystem.setPivotPIDSetpoint(setpoint);
                driveSubsystem.startPivotPID();
            }

            @Override
            public boolean run() {
                return driveSubsystem.pidOnTarget(1); 
            }
        }

        class PIDWaitAction extends AutonomousAction {
            double startTime;
            double waitTime;

            public PIDWaitAction(double time) {
                waitTime = time;
            }

            @Override
            public void start() {
                startTime = Timer.getFPGATimestamp();
            }

            @Override
            public boolean run() {
                return Timer.getFPGATimestamp() - startTime >= waitTime;
            }
        }

        LinkedList<AutonomousAction> autonomousList =
                new LinkedList<AutonomousAction>();

        // COMPOSE THE PID STEPS HERE
        autonomousList.add(new PIDStraightAction(15));

        autonomousSequence = autonomousList.listIterator();
        currentAction = autonomousSequence.next();
    }

    public void autonomousPeriodic() {
        if (currentAction != null && currentAction.run()) {
            if (autonomousSequence.hasNext()) {
                currentAction = autonomousSequence.next();
            } else {
                currentAction = null;
            }
        }
    }

    public void teleopInit() {
        for (MSubsystem s : subsystems) {
            s.teleopInit();
        }
    }

    public void teleopPeriodic() {
        controlSystem.teleopPeriodic();
    }

    public void testPeriodic() {

    }

}
