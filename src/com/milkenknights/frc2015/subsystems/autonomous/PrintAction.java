package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;

public class PrintAction extends AutonomousAction {
    String msg;
    
    public PrintAction(String msg) {
        this.msg = msg;
    }
    
    @Override
    protected void startCode() {
        System.out.println(msg);
    }

    @Override
    protected EndState periodicCode() {
        return EndState.END;
    }

}
