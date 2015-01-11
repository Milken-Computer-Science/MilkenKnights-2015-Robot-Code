package com.milkenknights.frc2015;

import java.util.LinkedList;
import java.util.ListIterator;

import com.milkenknights.common.AutonomousAction;
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

    ListIterator<AutonomousAction> autonomousSequence;
    LinkedList<AutonomousAction> runningActions;

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
            public EndState run() {
                if (driveSubsystem.pidOnTarget(1)) {
                    return EndState.END;
                } else {
                    return EndState.CONTINUE;
                }
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
            public EndState run() {
                if (driveSubsystem.pidOnTarget(1)) {
                    return EndState.END;
                } else {
                    return EndState.CONTINUE;
                }
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
            public EndState run() {
                if (Timer.getFPGATimestamp() - startTime >= waitTime) {
                    return EndState.END;
                } else {
                    return EndState.CONTINUE;
                }
            }
        }

        LinkedList<AutonomousAction> autonomousList =
                new LinkedList<AutonomousAction>();
        
        runningActions = new LinkedList<AutonomousAction>();

        // COMPOSE THE PID STEPS HERE
        autonomousList.add(new PIDStraightAction(15));

        autonomousSequence = autonomousList.listIterator();
    }

    public void autonomousPeriodic() {
        // if this ends up being true at the end of the while loop, start the
        // next queued AutonomousAction.
        // If runningActions is ever empty (e.g. at the beginning of
        // autonomous), the while loop will never happen, startNextAction will
        // stay true, and we will find the next action to add.
        boolean startNextAction = true;
        
        // Loop through the list of currently running actions. We use a manual
        // ListIterator instead of the syntax shortcut because we need to
        // remove the element mid-loop when it ends.
        ListIterator<AutonomousAction> i = runningActions.listIterator();
        while (i.hasNext()) {
            AutonomousAction a = i.next();
            startNextAction = false;
            
            // run the action and find out what to do next based on its return
            // value.
            switch(a.run()) {
            case CONTINUE:
                break;
            // END doesn't break because it falls through and sets
            // startNextAction to true.
            case END:
                i.remove();
            case BACKGROUND:
                startNextAction = true;
                break;
            }
        }
        
        if (startNextAction && autonomousSequence.hasNext()) {
            runningActions.add(autonomousSequence.next());
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
