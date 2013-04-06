package Maze;

import GeneticAlgorithm.Individual.Action;
import Robot.Robot.Direction;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mjpbull
 */
public class Maze {

    /**
     * Status of states in maze environment
     */
    private enum status {
        WALL, 
        WALL_VISITED,
        OPEN,
        ROBOT
    };

    private int rows = 8;
    private int columns = 8;
    private status[][] data;
    
    /**
     * Create M-by-N matrix of 0's
     * @param rows
     * @param columns
     */
    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        data = new status[rows][columns];
    }

    /**
     * Create matrix based on 2d array
     * @param data
     */
    public Maze(status[][] data) {
        rows = data.length;
        columns = data[0].length;
        this.data = new status[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                    this.data[i][j] = data[i][j];
            }
        }
    }

    /**
     * Getter for data within maze
     * @return
     */
    public status[][] getData() {
        return data;
    }

    /**
     * Find coordinates of Robot
     * @param coordinates
     * @return
     */
    public int[] findCoordinates(status[][] coordinates) {
        for(int i = 0; i < coordinates.length; i++) {
            for(int j = 0; j < coordinates[i].length; j++) {
                if(coordinates[i][j].equals(status.ROBOT)) {
                    return new int[] {i, j};
                }
            }
        }
        // ERROR
        return new int[] {-1, -1};
    }

    /**
     * Return the status of a given coordinate
     * @param x
     * @param y
     * @return
     */
    public status getLocationStatus(int x, int y) {
        return data[x][y];
    }

    // check what is near robot
    public boolean checkNearRobot(int x, int y, String instructions) {
        if(instructions.equals("left")) {
            if(data[x-1][y] == status.WALL) {
                return true;
            } else {
                return false;
            }
        }

        if(instructions.equals("right")) {
            if(data[x+1][y] == status.WALL) {
                return true;
            } else {
                return false;
            }
        }

        if(instructions.equals("up")) {
            if(data[x][y+1] == status.WALL) {
                return true;
            } else {
                return false;
            }
        }

        if(instructions.equals("down")) {
            if(data[x][y-1] == status.WALL) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Fill maze with predefined values
     * @return
     */
    public Maze fillMaze() {
        Maze maze;

        // Maze is defined
        data = new status[][] {
            {status.WALL, status.WALL, status.WALL, status.WALL, status.WALL, status.WALL, status.WALL, status.WALL},
            {status.WALL, status.ROBOT, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.WALL},
            {status.WALL, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.WALL},
            {status.WALL, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.WALL, status.WALL},
            {status.WALL, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.WALL, status.WALL},
            {status.WALL, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.WALL},
            {status.WALL, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.OPEN, status.WALL},
            {status.WALL, status.WALL, status.WALL, status.WALL, status.WALL, status.WALL, status.WALL, status.WALL},
        };

        maze = new Maze(data);

        return maze;
    }

    /**
     * Check if okay, then move Robot around Maze
     * @param maze
     * @param action
     * @param direction
     * @return
     */
    public Maze moveRobot(Maze maze, Action action, Direction direction) {
        // Get location of robot
        int[] position = findCoordinates(maze.getData());
        int x = position[0];
        int y = position[1];

        // If needed, instantiate QTable and use code below to set up Q Values

        // Moving forward
        if(action.equals(action.MOVE_FORWARD)) {
            if(direction.equals(direction.EAST)) {
                if(!(data[x][y+1].equals(status.WALL))) {
                    if(!(data[x][y+1].equals(status.WALL_VISITED))) {
                        data[x][y+1] = status.ROBOT;
                        data[x][y] = status.OPEN;
                    }
                }
            } else if(direction.equals(direction.NORTH)) {
                if(!(data[x-1][y].equals(status.WALL))) {
                    if(!(data[x-1][y].equals(status.WALL_VISITED))) {
                        data[x-1][y] = status.ROBOT;
                        data[x][y] = status.OPEN;
                    }
                }
            } else if(direction.equals(direction.WEST)) {
                if(!(data[x][y-1].equals(status.WALL))) {
                    if(!(data[x][y-1].equals(status.WALL_VISITED))) {
                        data[x][y-1] = status.ROBOT;
                        data[x][y] = status.OPEN;
                    }
                }
            } else if(direction.equals(direction.SOUTH)) {
                if(!(data[x+1][y].equals(status.WALL))) {
                    if(!(data[x+1][y].equals(status.WALL_VISITED))) {
                        data[x+1][y] = status.ROBOT;
                        data[x][y] = status.OPEN;
                    }
                }
            }
        }

        return maze;
    }

    /**
     * Checks how many walls have been visited by the robot
     * @return
     */
    public int numberOfVisitedWalls() {
        int count = 0;
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                if(data[i][j] == status.WALL_VISITED) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks to see if you are at the goal state
     * @return
     */
    public boolean atGoalState() {
        if(data[2][1] == status.ROBOT) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * If you've found a new wall up the fitness
     * @param direction
     * @param maze
     * @param fitness
     * @return
     */
    public double hasFoundNewWall(Direction direction, Maze maze, double fitness) {
        // Get position of robot
        int[] position = findCoordinates(maze.getData());
        int x = position[0];
        int y = position[1];

        if(direction.equals(direction.EAST)) {
            if(data[x-1][y].equals(status.WALL)) {
                data[x-1][y] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x-1][y+1].equals(status.WALL)) {
                data[x-1][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x][y+1].equals(status.WALL)) {
                data[x][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y].equals(status.WALL)) {
                data[x+1][y] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y+1].equals(status.WALL)) {
                data[x+1][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
        }

        if(direction.equals(direction.NORTH)) {
            if(data[x][y-1].equals(status.WALL)) {
                data[x][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x-1][y-1].equals(status.WALL)) {
                data[x-1][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x-1][y].equals(status.WALL)) {
                data[x-1][y] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x-1][y+1].equals(status.WALL)) {
                data[x-1][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x][y+1].equals(status.WALL)) {
                data[x][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
        }

        if(direction.equals(direction.SOUTH)) {
            if(data[x][y-1].equals(status.WALL)) {
                data[x][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y-1].equals(status.WALL)) {
                data[x+1][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y].equals(status.WALL)) {
                data[x+1][y] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y+1].equals(status.WALL)) {
                data[x+1][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x][y+1].equals(status.WALL)) {
                data[x][y+1] = status.WALL_VISITED;
                fitness += 1;
            }
        }

        if(direction.equals(direction.WEST)) {
            if(data[x-1][y].equals(status.WALL)) {
                data[x-1][y] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x-1][y-1].equals(status.WALL)) {
                data[x-1][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x][y-1].equals(status.WALL)) {
                data[x][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y-1].equals(status.WALL)) {
                data[x+1][y-1] = status.WALL_VISITED;
                fitness += 1;
            }
            if(data[x+1][y].equals(status.WALL)) {
                data[x+1][y] = status.WALL_VISITED;
                fitness += 1;
            }
        }
        return fitness;
    }
    
}
