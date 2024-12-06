import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a game board in 2048
 * 
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class board2048 {
    private int gridSize;
    private Tile[][] board;

    /**
     * Constructs a 2048 board to be filled with tiles and manipulated by logic2048.java
     * 
     * @param	- gridSize - the amounts of columns and rows to be used for the game board
     * @post	- the game board will be set to the gridSize inputted, and have two randomly placed tiles. 
     */
    public board2048(int gridSize) {
        this.gridSize = gridSize;
        board = new Tile[gridSize][gridSize];
        // Initialize the board with two tiles
        addNewTile();
        addNewTile();
    }
    
    /**
     * Gets the board grid 
     * 
     * @return	- the board field 
     */
    public Tile[][] getBoard() {
        return board;
    }

    /**
     * Adds a new tile to the game board (either 2 or 4, higher probability to be 2)
     * 
     * @post	- a new tile will be added to the board at a random, unoccuppied position, with a value 2 or 4
     */
    public void addNewTile() {
        Random rand = new Random();
        int value = rand.nextDouble() < 0.9 ? 2 : 4;

        List<int[]> emptyCells = new ArrayList<>();

        for (int i = 0; i < gridSize; i++) { // loop rows
            for (int j = 0; j < gridSize; j++) { // loop columns
                if (board[i][j] == null) { // if cell is empty
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return;
        }

        int[] randCell = emptyCells.get(rand.nextInt(emptyCells.size()));
        board[randCell[0]][randCell[1]] = new Tile(value, randCell[0], randCell[1]);
    }

    /**
     * Resets the merged status for all tiles
     * 
     * @post	- every tile in play will have a merged status of false
     */
    public void resetMergedFlags() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] != null) {
                    board[i][j].reset();
                }
            }
        }
    }

    /**
     * Sets the location fields to the tile object inputted
     * 
     * @param	- row - the new row of the tile object
     * @param	- col - the new column of the tile object
     * @param	- tile - the tile being manipulated
     * @pre		- row is in range of playable rows
     * @pre		- col is in range of playable columns
     * @post	- the tile object inputted will be moved to the index [row][column]
     */
    public void setTile(int row, int col, Tile tile) {
        if (tile != null) {
            tile.setRow(row);
            tile.setCol(col);
        }
        board[row][col] = tile;
    }

    /**
     * Gets the tile object at a grid location 
     * 
     * @param	- row - the new row of the tile object
     * @param	- col - the new column of the tile object
     * @pre		- row is in range of playable rows
     * @pre		- col is in range of playable columns
     * @return	- the tile at the index [row][column]
     */
    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    /**
     * Gets the grid size 
     * 
     * @return	- the grid size field 
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * Creates a copy of the board to be used for the undo button 
     * 
     * @return	- a copy of the current board grid 
     */
    public Tile[][] copyBoard() {
        Tile[][] copy = new Tile[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] != null) {
                    Tile originalTile = board[i][j];
                    copy[i][j] = new Tile(originalTile.getValue(), originalTile.getRow(), originalTile.getCol());
                    copy[i][j].setMerged(originalTile.isMerged());
                } else {
                    copy[i][j] = null;
                }
            }
        }
        return copy;
    }
    
    /**
     * Randomizes the positions of the existing tiles on the board.
     * Ensures all existing tiles are shuffled without altering their values.
     */
    public void randomizeBoard() {
        Random rand = new Random();
        List<Tile> tiles = new ArrayList<>();

        // Collect all non-null tiles
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] != null) {
                    tiles.add(board[i][j]);
                    board[i][j] = null; // Clear the board
                }
            }
        }

        // Shuffle the tiles
        for (Tile tile : tiles) {
            int row, col;
            do {
                row = rand.nextInt(gridSize);
                col = rand.nextInt(gridSize);
            } while (board[row][col] != null); // Find an empty spot
            tile.setRow(row);
            tile.setCol(col);
            board[row][col] = tile;
        }
    }

}
