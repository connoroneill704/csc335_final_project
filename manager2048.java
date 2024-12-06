import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

/**
 * Manages all peices of game to run together with high score
 * 
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class manager2048 {
    private logic2048 gameLogic;
    private String mode;
    private int moves;
    private int timeLeft;
    private boolean gameOver;
    private HighScoreManager highScoreManager;
    private static Scanner scanner;
    private boolean secondLife = false;

    /**
     * Constructs a manager unit to manage game and WOW factors
     * 
     * @param	- mode - the game mode chosen by the user
     * @post	- mode will be set to mode
     * @post	- game logic and high score will initialize their objects
     * @post	- game over will be set to false
     * @post	- scanner will be set to new scanner
     * @post	- if a special game mode, sets their respective paramater
     */
    public manager2048(String mode) {
        this.mode = mode;
        gameLogic = new logic2048(4); // Assuming a 4x4 grid
        highScoreManager = new HighScoreManager();
        gameOver = false;
        scanner= new Scanner(System.in);

        if (mode.equalsIgnoreCase("Time Trial")) {
            startTimer(40);
        } else if (mode.equalsIgnoreCase("Move Limit")) {
            moves = 30;
        }
    }
    
    /**
     * Constructs a manager unit to manage game and WOW factors
     * 
     * @param	- mode - the game mode chosen by the user
     * @param	- scanner - the inputted scanner to be used
     * @post	- mode will be set to mode
     * @post	- game logic and high score will initialize their objects
     * @post	- game over will be set to false
     * @post	- scanner will be set to inputted scanner
     * @post	- if a special game mode, sets their respective paramater
     */
    public manager2048(String mode, Scanner scanner) {
    	this.mode = mode;
        gameLogic = new logic2048(4); // Assuming a 4x4 grid
        highScoreManager = new HighScoreManager();
        gameOver = false;

        if (mode.equalsIgnoreCase("Time Trial")) {
            startTimer(40);
        } else if (mode.equalsIgnoreCase("Move Limit")) {
        	moves = 30;
        }
        this.scanner = scanner;
    }

    /**
     * Starts/Runs the text based 2048 game
     */
    public void startGame() {
        printBoard();

        while (!gameOver) {
            if (mode.equalsIgnoreCase("Time Trial") && timeLeft == 0) {
                System.out.println("Time up. Game Over");
                gameOver = true;
                break;
            }
            if (mode.equalsIgnoreCase("Move Limit") && moves == 0) {
                System.out.println("No more moves. Game over :(");
                gameOver = true;
                break;
            }

            System.out.print("Enter move (W/A/S/D/exit): ");
            String input = scanner.nextLine().toLowerCase();
            if (input.isEmpty()) {
                System.out.println("No input detected. Please use W, A, S, D, or exit.");
                continue;
            }
            if (input.equals("u")) {
                gameLogic.undoMove();
                printBoard();
                continue;
            }
            if (input.equals("exit")) {
                System.out.println("Quit");
                gameOver = true;
                break;
            }

            char move = input.charAt(0);
            processMove(move);

            if (gameLogic.hasMadeMove()) {
                if (mode.equalsIgnoreCase("Move Limit")) {
                    moves--;
                }
                printBoard();
                if (gameLogic.isWon()) {
                    System.out.println("Congratulations! You've reached 2048!");
                    gameOver = true;
                    break;
                }
                if (!gameLogic.hasMoves()) {
                	if (!secondLife) {
	                	MineSweeperGame game = new MineSweeperGame(8, 8, 1);
	                    SwingUtilities.invokeLater(() -> new MineSweeperUI(game));
	                    if (game.getHasWon()) {
	                    	secondLife = true;
	                    	gameLogic.randomizeBoard();
	                    }
                	}
                    if (!gameLogic.hasMoves())
                    	System.out.println("Game Over!");
                    	gameOver = true;
                    	break;
                    }
                }
            }
        
        endGame();
        scanner.close();
    }

    /**
     * Proccesses correct game logic for inputted command
     * 
     * @param	- command - the key pressed by the user to be moved
     * @post	- a move will be made if command is valid input (w, a, s, d)
     */
    private void processMove(char command) {
        switch (command) {
            case 'w':
                gameLogic.moveUp();
                break;
            case 'a':
                gameLogic.moveLeft();
                break;
            case 's':
                gameLogic.moveDown();
                break;
            case 'd':
                gameLogic.moveRight();
                break;
            default:
                System.out.println("Invalid move.");
        }
    }

    /**
     * Prints text based UI for current game board
     * 
     * @post 	- prints game grid, high score, and game mode indicators
     */
    private void printBoard() {
        Tile[][] board = gameLogic.getGameBoard().getBoard();
        int gridSize = gameLogic.getGameBoard().getGridSize();

        System.out.println("Score: " + gameLogic.getScore());

        if (mode.equalsIgnoreCase("Move Limit")) {
            System.out.println("Moves Left: " + moves);
        }
        if (mode.equalsIgnoreCase("Time Trial")) {
            System.out.println("Time Left: " + timeLeft);
        }

        for (int i = 0; i < gridSize; i++) {
            System.out.println("+-----".repeat(gridSize) + "+");
            for (int j = 0; j < gridSize; j++) {
                String num = (board[i][j] != null) ? String.valueOf(board[i][j].getValue()) : " ";
                System.out.printf("|%4s ", num);
            }
            System.out.println("|");
        }
        System.out.println("+-----".repeat(gridSize) + "+\n");
    }

    /**
     * Prompts for username entry, adds user and their score to highScoreManager, then displays high scores
     * 
     * @post	- username entered will be added with end score to high score file
     */
    private void endGame() {
        System.out.print("Enter your username: ");
        String name = scanner.nextLine();
        highScoreManager.addScore(name, gameLogic.getScore());
        displayHighScores();
        scanner.close();
    }

    /**
     * Prints the high scores for each entry in highScoreManager
     * 
     * @post	- a print block with every high score in order
     */
    private void displayHighScores() {
        System.out.println("\n--- High Scores ---");
        List<HighScoreManager.ScoreEntry> highScores = highScoreManager.getHighScores();
        for (int i = 0; i < highScores.size(); i++) {
            HighScoreManager.ScoreEntry entry = highScores.get(i);
            System.out.printf("%d. %s - %d\n", i + 1, entry.getName(), entry.getScore());
        }
    }

    /**
     * Starts timer for time trial mode
     * 
     * @param	- seconds - the duration (in seconds) of the timer
     * @post	- a timer will be running, when the timer hits zero, it will stop
     */
    private void startTimer(int seconds) {
        Timer timer = new Timer();
        timeLeft = seconds;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                } else {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * Selects the game mode that the user wants to play
     * 
     * @return	- mode field will be set to chosen mode
     */
    private static String selectGameMode() {
        System.out.println("Choose a game mode: Traditional, Time Trial, Move Limit");
        String input = scanner.nextLine().trim().toLowerCase();

        String mode;
        if (input.equals("traditional")) {
            mode = "Traditional";
        } else if (input.equals("time trial")) {
            mode = "Time Trial";
        } else if (input.equals("move limit")) {
            mode = "Move Limit";
        } else {
            System.out.println("Invalid choice. Defaulting to Traditional mode.");
            mode = "Traditional";
        }
        scanner.close();
        return mode;
    }
    
    /**
     * Gets the game mode 
     * 
     * @return	- the game mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Gets the status if the game is over 
     * 
     * @return	- the gameOver field
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the gameLogic
     * 
     * @return	- the gameLogic field
     */
    public logic2048 getGameLogic() {
        return gameLogic;
    }
    
    /**
     * Gets the high score manager 
     * 
     * @return	- the highScoreManager field
     */
    public HighScoreManager getHighScoreManager() {
        return highScoreManager;
    }
    
    /**
     * Runs manager2048.java by calling to select a game mode, then starting the game
     */
    public static void main(String[] args) {
        String mode = selectGameMode();
        manager2048 gameManager = new manager2048(mode);
        gameManager.startGame();
    }
}
