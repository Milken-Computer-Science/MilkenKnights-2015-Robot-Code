package com.milkenknights.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj.SpeedController;

/**
 * An extension of RobotDrive that adds Cheesy Drive and maybe more in the
 * future.
 * @author Daniel Kessler
 */
public class Drive {
    SpeedController[] leftMotors;
    SpeedController[] rightMotors;
    
    List<Integer> flippedLeftMotors;
    List<Integer> flippedRightMotors;
    
    // thanks to team 254 for CheesyDrive
    // cheesy drive uses one joystick for throttle, and the other for turning
    // also supports a "quickturn" function that allows the robot to spin
    // in place
    double old_turn;
    double neg_inertia_accumulator;
    double quickStopAccumulator;
    /**
     * Team 254's cheesy drive
     *
     * Use one joystick for throttle, and the other for turning.
     * Also supports a "quickturn" function that allows the robot
     * to spin in place
     *
     * @param power How fast the robot should go
     * @param turn The direction the robot should turn in
     * @param spin Whether or not the robot should go in "quickturn" mode
     * @return False if power is zero.
     */
    public void cheesyDrive(double power, double turn, boolean spin) {
        double neg_inertia = turn - old_turn;
        old_turn = turn;

        turn = curveInput(turn,2);

        double neg_inertia_scalar = 2.5;

        double neg_inertia_power = neg_inertia * neg_inertia_scalar;
        neg_inertia_accumulator += neg_inertia_power;
        turn += neg_inertia_power;
        if(neg_inertia_accumulator > 1) {
            neg_inertia_accumulator -= 1;
        } else if (neg_inertia_accumulator < -1) {
            neg_inertia_accumulator += 1;
        } else {
            neg_inertia_accumulator = 0;
        }

        double rPower = 0;
        double lPower = 0;

        if (spin) {
            rPower = turn;
            lPower = -turn;
        } else {
            double overPower = 0.0;
            double angular_power = 0;
            angular_power = power * turn - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0;
            }

            rPower = lPower = power;
            lPower += angular_power;
            rPower -= angular_power;

            if (lPower > 1) {
                rPower-= overPower * (lPower - 1);
                lPower = 1;
            } else if (rPower > 1) {
                lPower -= overPower * (rPower - 1);
                rPower = 1;
            } else if (lPower < -1) {
                rPower += overPower * (-1 - lPower);
                lPower = -1;
            } else if (rPower < -1) {
                lPower += overPower * (-1 - rPower);
                rPower = -1;
            }
        }

        tankDrive(lPower, rPower);
    }
    
    public void tankDrive(double lPower, double rPower) {
        int i = 0;
        for (SpeedController m : leftMotors) {
            if (flippedLeftMotors.contains(i)) {
                m.set(-lPower);
            } else {
                m.set(lPower);
            }
            i++;
        }
        i = 0;
        for (SpeedController m : rightMotors) {
            if (flippedRightMotors.contains(i)) {
                m.set(-rPower);
            } else {
                m.set(rPower);
            }
            i++;
        }

    }
    
    /**
     * Get the speed of the right side of the robot.
     * @return The speed of the right side of the robot.
     */
    public double getRight() {
        return rightMotors[0].get();
    }
    
    /**
     * Get the speed of the left side of the robot.
     * @return The speed of the left side of the robot.
     */
    public double getLeft() {
        return leftMotors[0].get();
    }
    
    public Drive(SpeedController[] left, SpeedController[] right) {
        this(left, right, new int[0], new int[0]);
    }
    
    /**
     * Make a new Drive instance, and reverse motors
     * @param left all of the SpeedControllers on the left side of the robot
     * @param right all of the SpeedControllers on the right side of the robot
     * @param reversedLeftMotors The array indexes of SpeedControllers in the
     *                           left parameter that should be flipped
     * @param reversedRightMotors The array indexes of SpeedControllers in the
     *                            right parameter that should be flipped
     */
    public Drive(SpeedController[] left, SpeedController[] right,
            int[] reversedLeftMotors, int[] reversedRightMotors) {
        leftMotors = left;
        rightMotors = right;
        flippedLeftMotors = Arrays.stream(reversedLeftMotors)
                .boxed().collect(Collectors.toList());
        flippedRightMotors = Arrays.stream(reversedRightMotors)
                .boxed().collect(Collectors.toList());
        }
    
    /**
     * Applies a sine function to input
     * @param in The original input
     * @param iterations How many times the sine function should be applied
     * @return The result of applying the sine function that many times.
     */
    private double curveInput(double in, int iterations) {
        if (iterations > 0) {
            return curveInput(Math.sin(Math.PI*in/2),iterations-1);
        } else {
            return in;
        }
    }
}
