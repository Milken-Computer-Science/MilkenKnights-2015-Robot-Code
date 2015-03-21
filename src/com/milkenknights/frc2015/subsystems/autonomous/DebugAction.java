package com.milkenknights.frc2015.subsystems.autonomous;

import com.milkenknights.common.AutonomousAction;
import com.milkenknights.common.DebugLogger;

public class DebugAction extends AutonomousAction {
    private Object obj;
    private int lvl;
    private String msg;
    
    public DebugAction(int lvl, Object obj, String msg) {
        this.obj = obj;
        this.lvl = lvl;
        this.msg = msg;
    }
    @Override
    protected void startCode() {
        DebugLogger.log(lvl, obj, msg);
    }

    @Override
    protected EndState periodicCode() {
        return EndState.END;
    }

}
