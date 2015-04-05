package com.milkenknights.frc2015.controls;

import com.milkenknights.common.AutonomousAction.CurrentState;
import com.milkenknights.frc2015.subsystems.DriveSubsystem;
import com.milkenknights.frc2015.subsystems.Subsystems;
import com.milkenknights.frc2015.subsystems.autonomous.PIDStraightAction;
import com.milkenknights.frc2015.subsystems.autonomous.PIDTrapezoidal;

public class Move50Auto extends ControlSystem {
    PIDStraightAction straight;
    public Move50Auto(Subsystems subsystems) {
        super(subsystems);
        
        straight = new PIDStraightAction(subsystems, 65, 3);
    }
    
    @Override
    public void init() {
        straight.start();
    }
    
    @Override
    public void periodic() {
        if(straight.getCurrentState() != CurrentState.ENDED) {
            straight.periodicRun();
        }
    }
}
