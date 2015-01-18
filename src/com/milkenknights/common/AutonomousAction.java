package com.milkenknights.common;

/**
 * In autononomous mode, we stack a bunch of AutonomousActions to create a
 * procedure. 
 * @author Daniel Kessler
 */
public abstract class AutonomousAction {
    /** The possibilities for what this action can do after each loop. */
    public enum EndState {
        /** Continue and run another round of our run() loop. */
        CONTINUE,
        /**
         * Keep running this action in the background, and go to the next
         * action.
         */
        BACKGROUND,
        /** Stop running this AutonomousAction. */
        END
    }
    
    /** This will be run once before the action starts */
    public abstract void start();
    
    /**
     * Run this periodically when this action is active
     * @return what this action should do next.
     * @see EndState
     */
    public abstract EndState run();
}
