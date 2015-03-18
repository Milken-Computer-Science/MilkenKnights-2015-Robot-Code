package com.milkenknights.common;

/**
 * In autononomous mode, we stack a bunch of AutonomousActions to create a
 * procedure. 
 * @author Daniel Kessler
 */
public abstract class AutonomousAction {
    private CurrentState currentState;
    
    public enum CurrentState {
        /** The action is running and in the foreground. */
        FOREGROUND,
        /** The action is running and in the background. */
        BACKGROUND,
        /** The action has not started yet. */
        NOT_STARTED,
        /** The action has ended. */
        ENDED,
    }
    
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
        END,
        /**
         * The action has ended after being backgrounded. This never has to be
         * returned from periodicCode.
         */
        END_FORK
    }
    
    /** This will be run once before the action starts */
    protected abstract void startCode();
    
    public void start() {
        startCode();
        currentState = CurrentState.FOREGROUND;
    }
    
    /**
     * Run this periodically when this action is active
     * @return what this action should do next.
     * @see EndState
     */
    protected abstract EndState periodicCode();
    
    /**
     * Run periodicCode, and then change our current state depending on its
     * returned EndState.
     * 
     * This method should only be called when the action is running
     * (foregrounded or backgrounded).
     * 
     * @return BACKGROUND if this is the first time the AutonomousAction was
     *         backgrounded. END if this action just ended. Otherwise, returns
     *         CONTINUE.
     */
    public EndState periodicRun() {
        EndState out = EndState.CONTINUE;
        switch (periodicCode()) {
        case BACKGROUND:
            if (currentState == CurrentState.FOREGROUND) {
                out = EndState.BACKGROUND;
            }
            currentState = CurrentState.BACKGROUND;
            break;
        case CONTINUE:
            break;
        case END:
        case END_FORK:
            if (currentState == CurrentState.BACKGROUND) {
                out = EndState.END_FORK;
            } else {
                out = EndState.END;
            }
            currentState = CurrentState.ENDED;
            break;
        }
        
        return out;
    }
    
    /**
     * Get the state of this AutonomousAction.
     * 
     * @see CurrentState
     * @return The current state of this AutonomousAction
     */
    public CurrentState getCurrentState() {
        return currentState;
    }
    
    public AutonomousAction() {
        currentState = CurrentState.NOT_STARTED;
    }
}