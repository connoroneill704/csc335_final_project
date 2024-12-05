import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class board2048 {
    private int gridSize;
    private Tile[][] board;

    public board2048(int gridSize) {
        this.gridSize = gridSize;
        board = new Tile[gridSize][gridSize];
        // Initialize the board with two tiles
        addNewTile();
        addNewTile();
    }

    public Tile[][] getBoard() {
        return board;
    }

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

    public void resetMergedFlags() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (board[i][j] != null) {
                    board[i][j].reset();
                }
            }
        }
    }

    public void setTile(int row, int col, Tile tile) {
        if (tile != null) {
            tile.setRow(row);
            tile.setCol(col);
        }
        board[row][col] = tile;
    }

    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    public int getGridSize() {
        return gridSize;
    }

    // Method to copy the board for undo functionality
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
}
