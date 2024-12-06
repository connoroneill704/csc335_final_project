import java.util.Stack;

/**
 * Manages game logic for 2048
 * 
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class logic2048 {
    private board2048 gameBoard;
    private int score;
    private boolean won;
    private boolean madeMove;
    private Stack<Tile[][]> boardHistory;
    private Stack<Integer> scoreHistory;
    private boolean secondLife = false;

    /**
     * Constructs a logic unit to manipulate a board2048.java object
     * 
     * @param	- gridSize - the amounts of columns and rows to be used for the game board
     * @post	- score and won will both be set to defaults of 0 and false
     * @post	- a 2048 board will be created at the inputted grid size
     * @post	- the board history and score history stacks will be created 
     */
    public logic2048(int gridSize) {
        gameBoard = new board2048(gridSize);
        score = 0;
        won = false;
        madeMove = false;
        boardHistory = new Stack<>();
        scoreHistory = new Stack<>();
    }

    /**
     * Gets the game board 
     * 
     * @return	- the game board
     */
    public board2048 getGameBoard() {
        return gameBoard;
    }

    /**
     * Gets the score 
     * 
     * @return	- the current game score 
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the status if the game is won 
     * 
     * @return	- the isWon field 
     */
    public boolean isWon() {
        return won;
    }

    /**
     * Gets the status if a move has been made 
     * 
     * @return	- the madeMove field
     */
    public boolean hasMadeMove() {
        return madeMove;
    }

    /**
     * Makes a move left for every tile 
     * 
     * @post	- each tile will be moved left (if possible)
     * @post	- if two tiles of the same values collide on their move, they will be merged
     * @post	- a new tile will be added to the game board 
     */
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

    /**
     * Makes a move right for every tile 
     * 
     * @post	- each tile will be moved right (if possible)
     * @post	- if two tiles of the same values collide on their move, they will be merged
     * @post	- a new tile will be added to the game board 
     */
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

    /**
     * Makes a move up for every tile 
     * 
     * @post	- each tile will be moved up (if possible)
     * @post	- if two tiles of the same values collide on their move, they will be merged
     * @post	- a new tile will be added to the game board 
     */
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

    /**
     * Makes a move down for every tile 
     * 
     * @post	- each tile will be moved down (if possible)
     * @post	- if two tiles of the same values collide on their move, they will be merged
     * @post	- a new tile will be added to the game board 
     */
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

    /**
     * Checks if the game board has moves left 
     * 
     * @return	- true - if their is a valid move to be made
     * @return 	- false - if their are no valid moves to be made
     */
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

    /**
     * Saves a copy of the game board and score for the undo button
     * 
     * @post	- boardHistory will push a copy of the current game board on its stack
     * @post	- scoreHistory will push the current score on its stack
     */
    private void saveState() {
        boardHistory.push(gameBoard.copyBoard());
        scoreHistory.push(score);
    }

    /**
     * Undo's the previous move 
     * 
     * @post	- the game board will be set to the previous board (the next on boardHistory stack)
     * @post	- the score will be set to the previous score (the next on scoreHistory stack)
     * @post	- if both stacks are empty, print, no moves to undo
     */
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
    
    public void randomizeBoard() {
    	gameBoard.randomizeBoard();
    }
    
    public boolean getSecLife() {
    	return secondLife;
    }
    
    public void setSecLife() {
    	secondLife = true;
    }
}
