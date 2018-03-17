package sudoku.hfad.com.gridviewexample;

import java.util.Random;

/**
 * Created by tuana on 10-03-2018.
 */

public class SudokuSolver {
    private int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Random rnd = new Random();
    private int[][] grid;
    private boolean[][] rowSet, colSet, boxSet;
    private boolean genFlag;

    public SudokuSolver() {
        grid = new int[9][9];
        rowSet = new boolean[9][10];
        colSet = new boolean[9][10];
        boxSet = new boolean[9][10];
        randomValuesArray(10);
    }

    void clearGrid() {
        for (int r = 0; r < 9; ++r) {
            for (int c = 0; c < 9; ++c) {
                grid[r][c] = 0;
            }
        }
        for (int i = 0; i < 9; ++i) {
            for (int v = 1; v <= 9; ++v) {
                rowSet[i][v] = false;
                colSet[i][v] = false;
                boxSet[i][v] = false;
            }
        }
    }

    void randomValuesArray(int numberOfTurns) {
        for (int turn = 0; turn < numberOfTurns; ++turn) {
            int randomIndex = 1 + rnd.nextInt(8);

            int tmp = values[0];
            values[0] = values[randomIndex];
            values[randomIndex] = tmp;
        }
    }

    void generateGrid(int pos) {
        if (pos == 81) {
            genFlag = false;
        } else if (genFlag) {
            int row = pos / 9;
            int col = pos - row * 9;
            int box = (row / 3) * 3 + col / 3;
            randomValuesArray(5);
            for (int val : values) {
                if (genFlag && rowSet[row][val] == false && colSet[col][val] == false && boxSet[box][val] == false) {
                    grid[row][col] = val;
                    rowSet[row][val] = true;
                    colSet[col][val] = true;
                    boxSet[box][val] = true;
                    generateGrid(pos + 1);
                    rowSet[row][val] = false;
                    colSet[col][val] = false;
                    boxSet[box][val] = false;
                }
            }
        }
    }

    int[][] getRandomGrid() {
        clearGrid();
        genFlag = true;
        generateGrid(0);
        return grid;
    }

    int[][] getRandomGrid(int numberOfEmptyCells) {
        grid = getRandomGrid();
        eraseCells(numberOfEmptyCells);
        return grid;
    }

    void eraseCells(int numberOfEmptyCells) {
        for (int i = 0; i < numberOfEmptyCells; ) {
            int row = rnd.nextInt(9);
            int col = rnd.nextInt(9);
            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                ++i;
            }
        }
    }

    public boolean checkUniqueValueGrid(int[][] grid) {
        if(grid == null) return false;
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 9; ++col) {
                if (Integer.bitCount(grid[row][col]) > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkAcceptedGrid(int[][] grid) {
        if(!checkUniqueValueGrid(grid)) {
            return false;
        }

        boolean[][] rowSet = new boolean[9][10];
        boolean[][] colSet = new boolean[9][10];
        boolean[][] boxSet = new boolean[9][10];

        for(int row = 0; row < 9; ++row) {
            for(int col = 0; col < 9; ++col) {
                int x = Cell.toIndex.get(grid[row][col]);
                if(x  == 0) return false;
                int box = (row / 3) * 3 + col / 3;
                if(rowSet[row][x] || colSet[col][x] || boxSet[box][x]) return false;
                rowSet[row][x] = true;
                colSet[col][x] = true;
                boxSet[box][x] = true;
            }
        }

        return true;
    }
}
