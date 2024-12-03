import java.util.Stack;

public class GameLogic {
    private board2048 gameBoard;
    private int score;
    private boolean won;
    private boolean madeMove;
    private Stack<Tile[][]> boardHistory;
    private Stack<Integer> scoreHistory;

    public logic2048(int gridSize) {
        gameBoard = new board2048(gridSize);
        score = 0;
        won = false;
        madeMove = false;
        boardHistory = new Stack<>();
        scoreHistory = new Stack<>();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public int getScore() {
        return score;
    }

    public boolean isWon() {
        return won;
    }

    public boolean hasMadeMove() {
        return madeMove;
    }

    public void moveLeft() {
        madeMove = false;
        saveState();
        gameBoard.resetMergedFlags();
        int gridSize = gameBoard.getGridSize();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 1; j < gridSize; j++) {
                Tile tile = gameBoard.getTile(i, j);
                if (tile != null) {
                    int col = j;
                    while (col > 0 && gameBoard.getTile(i, col - 1) == null) {
                        gameBoard.setTile(i, col - 1, tile);
                        gameBoard.setTile(i, col, null);
                        col--;
                        madeMove = true;
                    }
                    Tile targetTile = gameBoard.getTile(i, col - 1);
                    if (col > 0 && targetTile != null &&
                        targetTile.getValue() == tile.getValue() &&
                        !targetTile.isMerged() && !tile.isMerged()) {

                        int newValue = targetTile.getValue() * 2;
                        targetTile.setValue(newValue);
                        targetTile.setMerged(true);
                        gameBoard.setTile(i, col, null);
                        score += newValue;
                        madeMove = true;
                        if (newValue == 2048) won = true;
                    }
                }
            }
        }
        if (madeMove) {
            gameBoard.addNewTile();
        }
    }

    // move right
    public void moveRight() {
        resetMergedFlags();
        for (int row = 0; row < grid_size; row++) {
            for (int col = grid_size - 2; col >= 0; col--) {
                if (board[row][col] != null) {
                    int currentCol = col;
                    while (currentCol < grid_size - 1 && board[row][currentCol + 1] == null) {
                        board[row][currentCol + 1] = board[row][currentCol];
                        board[row][currentCol] = null;
                        currentCol++;
                        made_move = true;
                    }
                    if (currentCol < grid_size - 1 && board[row][currentCol + 1] != null &&
                        board[row][currentCol + 1].getValue() == board[row][currentCol].getValue() &&
                        !board[row][currentCol + 1].isMerged() && !board[row][currentCol].isMerged()) {
                        
                        int newValue = board[row][currentCol + 1].getValue() * 2;
                        board[row][currentCol + 1].setValue(newValue);
                        board[row][currentCol + 1].setMerged(true);
                        board[row][currentCol] = null;
                        score += newValue;
                        made_move = true;
                        if (newValue == 2048) won = true;
                    }
                }
            }
        }
    }

    // move up
    public void moveUp() {
        resetMergedFlags();
        for (int col = 0; col < grid_size; col++) {
            for (int row = 1; row < grid_size; row++) {
                if (board[row][col] != null) {
                    int currentRow = row;
                    while (currentRow > 0 && board[currentRow - 1][col] == null) {
                        board[currentRow - 1][col] = board[currentRow][col];
                        board[currentRow][col] = null;
                        currentRow--;
                        made_move = true;
                    }
                    if (currentRow > 0 && board[currentRow - 1][col] != null &&
                        board[currentRow - 1][col].getValue() == board[currentRow][col].getValue() &&
                        !board[currentRow - 1][col].isMerged() && !board[currentRow][col].isMerged()) {
                        
                        int newValue = board[currentRow - 1][col].getValue() * 2;
                        board[currentRow - 1][col].setValue(newValue);
                        board[currentRow - 1][col].setMerged(true);
                        board[currentRow][col] = null;
                        score += newValue;
                        made_move = true;
                        if (newValue == 2048) won = true;
                    }
                }
            }
        }
    }

    public void moveDown() {
        resetMergedFlags();
        for (int col = 0; col < grid_size; col++) {
            for (int row = grid_size - 2; row >= 0; row--) {
                if (board[row][col] != null) {
                    int currentRow = row;
                    while (currentRow < grid_size - 1 && board[currentRow + 1][col] == null) {
                        board[currentRow + 1][col] = board[currentRow][col];
                        board[currentRow][col] = null;
                        currentRow++;
                        made_move = true;
                    }
                    if (currentRow < grid_size - 1 && board[currentRow + 1][col] != null &&
                        board[currentRow + 1][col].getValue() == board[currentRow][col].getValue() &&
                        !board[currentRow + 1][col].isMerged() && !board[currentRow][col].isMerged()) {
                        
                        int newValue = board[currentRow + 1][col].getValue() * 2;
                        board[currentRow + 1][col].setValue(newValue);
                        board[currentRow + 1][col].setMerged(true);
                        board[currentRow][col] = null;
                        score += newValue;
                        made_move = true;
                        if (newValue == 2048) won = true;
                    }
                }
            }
        }
    }

    public boolean hasMoves() {
        int gridSize = gameBoard.getGridSize();
        Tile[][] board = gameBoard.getBoard();

        for (int i = 0; i < gridSize; i++) { // loop rows
            for (int j = 0; j < gridSize; j++) { // loop columns
                Tile currentTile = board[i][j];
                if (currentTile == null)
                    return true;
                if (j < gridSize - 1) {
                    Tile rightTile = board[i][j + 1];
                    if (rightTile != null && currentTile.getValue() == rightTile.getValue())
                        return true;
                }
                if (i < gridSize - 1) {
                    Tile downTile = board[i + 1][j];
                    if (downTile != null && currentTile.getValue() == downTile.getValue())
                        return true;
                }
            }
        }
        return false;
    }

    private void saveState() {
        boardHistory.push(gameBoard.copyBoard());
        scoreHistory.push(score);
    }

    public void undoMove() {
        if (!boardHistory.isEmpty() && !scoreHistory.isEmpty()) {
            Tile[][] previousBoard = boardHistory.pop();
            int previousScore = scoreHistory.pop();
            gameBoard = new GameBoard(gameBoard.getGridSize());
            Tile[][] currentBoard = gameBoard.getBoard();
            for (int i = 0; i < gameBoard.getGridSize(); i++) {
                for (int j = 0; j < gameBoard.getGridSize(); j++) {
                    currentBoard[i][j] = previousBoard[i][j];
                }
            }
            score = previousScore;
            madeMove = true;
        } else {
            System.out.println("No moves to undo.");
        }
    }
}
