import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class manager2048 {
    private logic2048 gameLogic;
    private String mode;
    private int moves;
    private int timeLeft;
    private boolean gameOver;
    private HighScoreManager highScoreManager;
    private Timer timer;
    private Scanner scanner;

    public manager2048(String mode) {
        this.mode = mode;
        gameLogic = new logic2048(4); // Assuming a 4x4 grid
        highScoreManager = new HighScoreManager();
        gameOver = false;

        if (mode.equalsIgnoreCase("Time Trial")) {
            startTimer(40);
        } else if (mode.equalsIgnoreCase("Move Limit")) {
            moves = 30;
        }
    }
    
    public manager2048(String mode, Scanner scanner) {
        this(mode);
        this.scanner = scanner;
    }

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
                    System.out.println("Game Over!");
                    gameOver = true;
                    break;
                }
            }
        }
        endGame();
        scanner.close();
    }

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

    private void endGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String name = scanner.nextLine();
        highScoreManager.addScore(name, gameLogic.getScore());
        displayHighScores();
        scanner.close();
    }

    private void displayHighScores() {
        System.out.println("\n--- High Scores ---");
        List<HighScoreManager.ScoreEntry> highScores = highScoreManager.getHighScores();
        for (int i = 0; i < highScores.size(); i++) {
            HighScoreManager.ScoreEntry entry = highScores.get(i);
            System.out.printf("%d. %s - %d\n", i + 1, entry.getName(), entry.getScore());
        }
    }

    // Timer for Time Trial mode
    private void startTimer(int seconds) {
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
    public static void main(String[] args) {
        String mode = selectGameMode();
        manager2048 gameManager = new manager2048(mode);
        gameManager.startGame();
    }

    private static String selectGameMode() {
        Scanner scanner = new Scanner(System.in);
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
    public String getMode() {
        return mode;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public logic2048 getGameLogic() {
        return gameLogic;
    }
}
