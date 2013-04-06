/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Maze;

/**
 *
 * @author Mike
 */
public class QTable {
    private int rows;
    private int columns;
    private int[][] qValue;

    public QTable(Maze maze) {
        qValue = new int[rows][columns];
    }

    public int[][] fillQTable() {
        qValue = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
        };

        return qValue;
    }

    public void setQValue(int row, int column, int value) {
        qValue[row][column] = value;
    }
}
