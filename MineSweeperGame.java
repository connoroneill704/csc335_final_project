/**
 * Names: Connor O'Neill, Eli Jordan, Derek Hoshaw, 
 * Net Ids: connoroneill ejordan3 dthoshaw
 */

import java.util.Random;

/**
 * Core game logic for Minesweeper.
 */
public class MineSweeperGame {
    private final int rows;
    private final int cols;
    private final int mines;
    private final Cell[][] grid;

    public MineSweeperGame(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.grid = new Cell[rows][cols];
        initializeGrid();
        placeMines();
        calculateAdjacentMines();
    }

    /**
     * Initializes the grid with empty cells.
     */
    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Randomly places mines on the grid.
     */
    private void placeMines() {
        Random rand = new Random();
        int placedMines = 0;

        while (placedMines < mines) {
            int row = rand.nextInt(rows);
            int col = rand.nextInt(cols);

            if (!grid[row][col].isMine()) {
                grid[row][col].setMine(true);
                placedMines++;
            }
        }
    }

    /**
     * Calculates the number of adjacent mines for each cell.
     */
    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!grid[i][j].isMine()) {
                    grid[i][j].setAdjacentMines(countAdjacentMines(i, j));
                }
            }
        }
    }

    /**
     * Counts the number of mines adjacent to a given cell.
     *
     * @param row the row index
     * @param col the column index
     * @return the number of adjacent mines
     */
    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int newRow = row + r;
                int newCol = col + c;
                if (isValidCell(newRow, newCol) && grid[newRow][newCol].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if a cell is within the grid boundaries.
     *
     * @param row the row index
     * @param col the column index
     * @return true if valid, false otherwise
     */
    public boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Retrieves a specific cell from the grid.
     *
     * @param row the row index
     * @param col the column index
     * @return the cell at the specified position
     */
    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    /**
     * Retrieves the number of rows in the Minesweeper game grid.
     *
     * @return the number of rows in the grid
     */
    public int getRows() {
        return rows;
    }

    /**
     * Retrieves the number of columns in the Minesweeper game grid.
     *
     * @return the number of columns in the grid
     */
    public int getCols() {
        return cols;
    }
}
