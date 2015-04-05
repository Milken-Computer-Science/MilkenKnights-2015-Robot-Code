package com.milkenknights.frc2015;

import com.milkenknights.common.RestrictedSolenoid;
import com.milkenknights.frc2015.controls.DoNothing;
import com.milkenknights.frc2015.controls.Move50Auto;
import com.milkenknights.frc2015.controls.ControlSystem;
import com.milkenknights.frc2015.controls.ThreeToteAuto;
import com.milkenknights.frc2015.controls.TripleATKControl;
import com.milkenknights.frc2015.subsystems.Subsystems;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
    private Subsystems subsystems;

    private ControlSystem teleControlSystem;
    private ControlSystem autoControlSystem;
    
    private SendableChooser autoChooser;

    public void robotInit() {
        RestrictedSolenoid.initPressureSensor(Constants.ANALOG.PRESSURE_TRANSDUCER, 
                Constants.PRESSURE_TRANSDUCER.SCALE_FACTOR,
                Constants.PRESSURE_TRANSDUCER.OFFSET);
        
        subsystems = new Subsystems();

        teleControlSystem = new TripleATKControl(subsystems);
        
        autoChooser = new SendableChooser();
        autoChooser.addDefault("Do Nothing", new DoNothing(subsystems));
        autoChooser.addObject("Drive Forward 50\"", new Move50Auto(subsystems));
        autoChooser.addObject("Three Tote Auto Qual", new ThreeToteAuto(subsystems,
                ThreeToteAuto.Strategy.QUALS));
        autoChooser.addObject("Three Tote Auto Elim", new ThreeToteAuto(subsystems,
                ThreeToteAuto.Strategy.ELIM));
        SmartDashboard.putData("Autonomous Selector", autoChooser);
    }

    public void autonomousInit() {
        //autoControlSystem = (ControlSystem) autoChooser.getSelected();
        //autoControlSystem = new ThreeToteAuto(subsystems, ThreeToteAuto.Strategy.ELIM);
        autoControlSystem = new Move50Auto(subsystems);
        
        if (autoControlSystem == null) {
            autoControlSystem = new DoNothing(subsystems);
        }
        autoControlSystem.init();
        
        subsystems.update();
    }

    public void autonomousPeriodic() {
        autoControlSystem.periodic();
        
        subsystems.update();
    }

    public void teleopInit() {
        teleControlSystem.init();
        
        subsystems.teleopInit();
    }

    public void teleopPeriodic() {
        teleControlSystem.periodic();
        
        subsystems.update();
    }
    
    public void disabledPeriodic() {
        subsystems.update();
    }

    public void testPeriodic() {

    }
}
