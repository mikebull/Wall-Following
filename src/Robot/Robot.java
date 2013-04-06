/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robot;

import GeneticAlgorithm.Individual.Action;
import Maze.Maze;

/**
 *
 * @author mjpbull
 */
public class Robot {

    // X position in Maze environment
    private int x;
    // Y position in Maze environment
    private int y;
    // Current Direction of Robot
    private Direction currentDirection;

    boolean wallInFront;
    boolean wallBehind;
    boolean wallToLeft;
    boolean wallToRight;

    double fitness = 0;

    /**
     * Directions for Robot to travel in
     */
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    };

    /**
     * Robot Constructor with Direction preset to EAST
     */
    public Robot() {
        currentDirection = Direction.EAST;
    }

    /**
     * Getter for Current Direction
     * @return
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Setter for Current Direction
     * @param direction
     */
    public void changeRobotDirection(Direction direction) {
        currentDirection = direction;
    }

    /**
     * Checks where the walls are in relation to the robot
     * @param maze
     */
    public void determineSensorInputs(Maze maze) {
        // Position of Robot
        int[] position = new int[1];
        position = maze.findCoordinates(maze.getData());
        x = position[0];
        y = position[1];

        // Check what is near robot (implement direction with this soon)
        wallInFront = maze.checkNearRobot(x, y, "up");
        wallBehind = maze.checkNearRobot(x, y, "down");
        wallToLeft = maze.checkNearRobot(x, y, "left");
        wallToRight = maze.checkNearRobot(x, y, "right");
    }

    /**
     * Checks if goal has been reached
     * @param maze
     * @return
     */
    public boolean hasReachedGoal(Maze maze) {
        if(maze.numberOfVisitedWalls() > 28) {
            if(maze.atGoalState()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update Robots State
     * @param direction
     * @param action
     * @param maze
     * @param fitness
     * @return
     */
    public double updateRobotState(Direction direction, Action action, Maze maze, double fitness) {
        // Check to see if robot has found a new wall (take fitness as debugging tool)
        fitness = maze.hasFoundNewWall(direction, maze, fitness);
        // Move robot to new place in maze
        maze.moveRobot(maze, action, direction);

        fitness = maze.numberOfVisitedWalls();

        return fitness;
    }
}
