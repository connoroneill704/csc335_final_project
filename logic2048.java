import java.util.Stack;

public class logic2048 {
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

    public board2048 getGameBoard() {
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
        Tile[][] board = gameBoard.getBoard();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 1; j < gridSize; j++) {
                if (board[i][j] != null) {
                    int col = j;
                    Tile tile = board[i][col];
                    while (col > 0 && board[i][col - 1] == null) {
                        board[i][col - 1] = tile;
                        board[i][col] = null;
                        col--;
                        madeMove = true;
                    }
                    if (col > 0 && board[i][col - 1] != null &&
                        board[i][col - 1].getValue() == tile.getValue() &&
                        !board[i][col - 1].isMerged() && !tile.isMerged()) {

                        int newValue = board[i][col - 1].getValue() * 2;
                        board[i][col - 1].setValue(newValue);
                        board[i][col - 1].setMerged(true);
                        board[i][col] = null;
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

    public void moveRight() {
        madeMove = false;
        saveState();
        gameBoard.resetMergedFlags();
        int gridSize = gameBoard.getGridSize();
        Tile[][] board = gameBoard.getBoard();

        for (int row = 0; row < gridSize; row++) {
            for (int col = gridSize - 2; col >= 0; col--) {
                if (board[row][col] != null) {
                    int currentCol = col;
                    Tile tile = board[row][currentCol];
                    while (currentCol < gridSize - 1 && board[row][currentCol + 1] == null) {
                        board[row][currentCol + 1] = tile;
                        board[row][currentCol] = null;
                        currentCol++;
                        madeMove = true;
                    }
                    if (currentCol < gridSize - 1 && board[row][currentCol + 1] != null &&
                        board[row][currentCol + 1].getValue() == tile.getValue() &&
                        !board[row][currentCol + 1].isMerged() && !tile.isMerged()) {

                        int newValue = board[row][currentCol + 1].getValue() * 2;
                        board[row][currentCol + 1].setValue(newValue);
                        board[row][currentCol + 1].setMerged(true);
                        board[row][currentCol] = null;
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

    public void moveUp() {
        madeMove = false;
        saveState();
        gameBoard.resetMergedFlags();
        int gridSize = gameBoard.getGridSize();
        Tile[][] board = gameBoard.getBoard();

        for (int col = 0; col < gridSize; col++) {
            for (int row = 1; row < gridSize; row++) {
                if (board[row][col] != null) {
                    int currentRow = row;
                    Tile tile = board[currentRow][col];
                    while (currentRow > 0 && board[currentRow - 1][col] == null) {
                        board[currentRow - 1][col] = tile;
                        board[currentRow][col] = null;
                        currentRow--;
                        madeMove = true;
                    }
                    if (currentRow > 0 && board[currentRow - 1][col] != null &&
                        board[currentRow - 1][col].getValue() == tile.getValue() &&
                        !board[currentRow - 1][col].isMerged() && !tile.isMerged()) {

                        int newValue = board[currentRow - 1][col].getValue() * 2;
                        board[currentRow - 1][col].setValue(newValue);
                        board[currentRow - 1][col].setMerged(true);
                        board[currentRow][col] = null;
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

    public void moveDown() {
        madeMove = false;
        saveState();
        gameBoard.resetMergedFlags();
        int gridSize = gameBoard.getGridSize();
        Tile[][] board = gameBoard.getBoard();

        for (int col = 0; col < gridSize; col++) {
            for (int row = gridSize - 2; row >= 0; row--) {
                if (board[row][col] != null) {
                    int currentRow = row;
                    Tile tile = board[currentRow][col];
                    while (currentRow < gridSize - 1 && board[currentRow + 1][col] == null) {
                        board[currentRow + 1][col] = tile;
                        board[currentRow][col] = null;
                        currentRow++;
                        madeMove = true;
                    }
                    if (currentRow < gridSize - 1 && board[currentRow + 1][col] != null &&
                        board[currentRow + 1][col].getValue() == tile.getValue() &&
                        !board[currentRow + 1][col].isMerged() && !tile.isMerged()) {

                        int newValue = board[currentRow + 1][col].getValue() * 2;
                        board[currentRow + 1][col].setValue(newValue);
                        board[currentRow + 1][col].setMerged(true);
                        board[currentRow][col] = null;
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
            gameBoard = new board2048(gameBoard.getGridSize());
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
