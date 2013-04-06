/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GeneticAlgorithm;

import Maze.Maze;
import Robot.Robot;
import Robot.Robot.Direction;
import java.util.Random;

/**
 *
 * @author Mike
 */
public class Individual {

    /**
     * Enumerated values for Actions
     */
    public enum Action {
        MOVE_FORWARD, DO_NOTHING, TURN_LEFT, TURN_RIGHT
    };

    public static Action act;
    private boolean genes[];
    private double fitness;

    public Direction direction;

    // To check if walls are near the Robot
    boolean wallInFront;
    boolean wallBehind;
    boolean wallToLeft;
    boolean wallToRight;

    // Number of individuals
    public static final int SIZE = 32;

    public Individual() {
        genes = new boolean[SIZE];
    }

    /**
     * Gets Fitness Value
     * @return
     */
    public double getFitness() {
        return fitness;
    }

    /**
     * Sets Fitness Value
     * @param fitnessValue
     */
    public void setFitness(double fitnessValue) {
        this.fitness = fitnessValue;
    }

    /**
     * Gets Gene
     * @param index
     * @return Gene at Index
     */
    public boolean getGene(int index) {
        return genes[index];
    }

    /**
     * Returns Gene Array
     * @return
     */
    public boolean[] getGenes() {
        return genes;
    }

    /**
     * Sets Gene
     * @param index
     * @param Gene at Index
     */
    public void setGene(int index, boolean gene) {
        genes[index] = gene;
    }

    /**
     * Set genes randomly
     */
    public void randGenes() {
        Random rand = new Random();
        for(int i = 0; i < SIZE; i++) {
            setGene(i, rand.nextBoolean());
        }
    }

    /**
     * Mutation Method
     */
    public void mutate() {
        Random rand = new Random();
        int index = rand.nextInt(SIZE);
        setGene(index, rand.nextBoolean());
    }

    /**
     * Evaluate fitness
     * @return
     */
    public double evaluate(Maze maze) {
        Robot robot = new Robot();
        Action action;

        for(int i = 0; i < 28; ++i) {
            action = this.getRobotAction();
            direction = changeRobotsDirection(robot, action);
            fitness += robot.updateRobotState(direction, action, maze, fitness);
        }

        fitness = maze.numberOfVisitedWalls();


        // Print q-learning matrix
        /*for (int i =0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                System.out.print(" " + tmp[i][j]);
            }
            System.out.println("");
        }*/

        //fitness = Math.round(fitness / 28);

        this.setFitness(fitness);

        return fitness;
    }    

    /**
     * Change direction of robot based on action
     * @param robot
     * @param action
     * @return
     */
    public Direction changeRobotsDirection(Robot robot, Action action) {
        if(action.equals(action.TURN_LEFT)) {
            if(robot.getCurrentDirection().equals(Direction.EAST)) {
                robot.changeRobotDirection(Direction.NORTH);
            } else if(robot.getCurrentDirection().equals(Direction.NORTH)) {
                robot.changeRobotDirection(Direction.WEST);
            } else if(robot.getCurrentDirection().equals(Direction.WEST)) {
                robot.changeRobotDirection(Direction.SOUTH);
            } else if(robot.getCurrentDirection().equals(Direction.SOUTH)) {
                robot.changeRobotDirection(Direction.EAST);
            }
        }

        if(action.equals(action.TURN_RIGHT)) {
            if(robot.getCurrentDirection().equals(Direction.EAST)) {
                robot.changeRobotDirection(Direction.SOUTH);
            } else if(robot.getCurrentDirection().equals(Direction.NORTH)) {
                robot.changeRobotDirection(Direction.EAST);
            } else if(robot.getCurrentDirection().equals(Direction.WEST)) {
                robot.changeRobotDirection(Direction.NORTH);
            } else if(robot.getCurrentDirection().equals(Direction.SOUTH)) {
                robot.changeRobotDirection(Direction.WEST);
            }
        }

        Direction tmp = robot.getCurrentDirection();
        return tmp;
    }

    /**
     * Return an action from the rule generated through two gene values
     * @param firstRule
     * @param secondRule
     * @return
     */
    public Action getActionFromRule(boolean firstRule, boolean secondRule) {
        Action tmp;

        if(firstRule) {
            tmp = secondRule ? Action.MOVE_FORWARD : Action.DO_NOTHING;
        } else {
            tmp = secondRule ? Action.TURN_LEFT : Action.TURN_RIGHT;
        }

        return tmp;
    }

    int count;

    /**
     * Test Action getter
     * @return
     */
    public Action getRobotAction() {
        // In case gene count overflows reduce count and catch exception
        if(count > 30) {
            count = 0;
        }

        try {
            boolean b1 = genes[count];
            boolean b2 = genes[count+1];
            count += 2;
            return getActionFromRule(b1, b2);
        } catch (ArrayIndexOutOfBoundsException e) {
            count = 0;
            //e.printStackTrace();
        }
        return getActionFromRule(true, true);
    }
}
