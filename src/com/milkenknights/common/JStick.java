package com.milkenknights.common;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A wrapper for Joystick that adds helpful utility functions.
 * 
 * @author Daniel Kessler
 */
public class JStick {
    public static final int XBOX_A = 1;
    public static final int XBOX_B = 2;
    public static final int XBOX_X = 3;
    public static final int XBOX_Y = 4;
    public static final int XBOX_LB = 5;
    public static final int XBOX_RB = 6;
    public static final int XBOX_BACK = 7;
    public static final int XBOX_START = 8;
    public static final int XBOX_LJ = 9;
    public static final int XBOX_RJ = 10;

    public static final int XBOX_LSX = 0; // left stick x
    public static final int XBOX_LSY = 1; // left stick y
    public static final int XBOX_LTRIG = 2; // always greater than 0.5
    public static final int XBOX_RTRIG = 3; // always greater than 0.5
    public static final int XBOX_RSX = 4; // right stick x
    public static final int XBOX_RSY = 5; // right stick y

    public static final int ATK3_X = 0; // ATK3 stick x
    public static final int ATK3_Y = 1; // ATK3 stick y
    public static final int ATK3_KNOB = 2; // + side is negative
    
    public static final int GUIRAR_GREEN = 0;
    public static final int GUIRAR_RED = 1;
    public static final int GUIRAR_YELLOW = 3;
    public static final int GUIRAR_BLUE = 2;
    public static final int GUIRAR_ORANGE = 4;

    private Joystick jstick;
    private boolean[] buttonPressed;
    private boolean[] buttonLastPressed;
    private double[] axes;
    private double[] slowAxes;

    private double slow;

    /**
     * Make a new JStick.
     * @param port The port of the joystick.
     */
    public JStick(int port) {
        // initialize everything
        jstick = new Joystick(port);
        buttonPressed = new boolean[jstick.getButtonCount() + 1];
        buttonLastPressed = new boolean[jstick.getButtonCount() + 1];
        axes = new double[jstick.getAxisCount()];
        slowAxes = new double[jstick.getAxisCount()];
        slow = 2;
    }

    /**
     * Update the stored joystick values. Should be called only once per loop.
     */
    public void update() {
        for(int i = 1; i < buttonPressed.length; ++i) {
            buttonLastPressed[i] = buttonPressed[i];
            buttonPressed[i] = jstick.getRawButton(i);
        }

        for(int i = 0; i < axes.length; i++) {
            double newAxis = jstick.getRawAxis(i);

            if (newAxis - axes[i] > slow) {
                slowAxes[i] += slow;
            } else if (axes[i] - newAxis > slow) {
                slowAxes[i] -= slow;
            } else {
                slowAxes[i] = newAxis;
            }
            axes[i] = newAxis;
        }
    }

    /**
     * The output of joystick axes can be slowed down
     * so that after each update its output will only
     * deviate from previous value at a maximum of the
     * slow value.
     *
     * @param s The new slow value. This should be a positive number.
     */
    public void setSlow(double s) {
        slow = Math.abs(s);
    }

    /**
     * @see setSlow
     * @return The slow value.
     */
    public double getSlow() {
        return slow;
    }

    /**
     * Gets the button value
     *
     * @param b The button number to be read.
     * @return The state of the button since the last update.
     */
    public boolean isJustPressed(int b) {
        if (b >= 0 && b < buttonPressed.length) {
            return buttonPressed[b] && !buttonLastPressed[b];
        } else {
            return false;
        }
    }
    
    public boolean isPressed(int b) {
        if (b >= 0 && b < buttonPressed.length) {
            return buttonPressed[b];
        }
        return false;
    }

    /**
     * Gets whether or not the button is being released
     *
     * @param b The button number to be read.
     * @return True if the button was pressed in the last update but not now.
     */
    public boolean isReleased(int b) {
        if (b >= 0 && b < buttonPressed.length) {
            return !buttonPressed[b] && buttonLastPressed[b];
        } else {
            return false;
        }
    }
    
    /**
     * Gets whether or not the button has just been pressed
     * 
     * @param b The button number to be read.
     * @return True if the button was not pressed in the last update but
     *         pressed now.
     */
    public boolean justPressed(int b) {
        if (b >= 0 && b < buttonPressed.length) {
            return buttonPressed[b] && !buttonLastPressed[b];
        } else {
            return false;
        }
    }

    /**
     * Gets the value of the axis.
     *
     * @param b The axis to read.
     * @return The value of the axis since the last update.
     */
    public double getAxis(int b) {
        if(b >= 0 && b < axes.length)
            return axes[b];
        else return 0;
    }

    /**
     * @see #setSlow(double)
     * 
     * @param b The axis to read.
     * @return The value of the axis, modified by the slow value.
     */
    public double getSlowedAxis(int b) {
        if(b >= 0 && b < axes.length)
            return slowAxes[b];
        else return 0;
    }
}
