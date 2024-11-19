import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class game2048 {
    private int grid_size=4;
    private int[][] board;
    private int score;
    private boolean made_move;
    private boolean won;
    //for game modes
    private static String mode;
    private int moves;
    private int timeLeft;

    public game2048() {
        board=new int[grid_size][grid_size];
        score=0;
        addNewTile();
        addNewTile();
    }

    private void addNewTile() {
        Random rand=new Random();
        int value=rand.nextDouble() < 0.9 ? 2 : 4;
        int emptyCells=0;
        for (int[] row : board)
            for (int cell : row)
                if (cell == 0) emptyCells++;
        if (emptyCells == 0) return;
        int location=rand.nextInt(emptyCells);
        int count=0;
        outerloop:
        for (int i=0; i < grid_size; i++) {
            for (int j=0; j < grid_size; j++) {
                if (board[i][j] == 0) {
                    if (count == location) {
                        board[i][j]=value;
                        break outerloop;
                    }
                    count++;
                }
            }
        }
    }
    private void printBoard() {
    	System.out.println("Score: " + score);
    	
    	if(mode.equals("Move Limit")) {
    		System.out.println("Moves Left: " + moves);
    	}
    	if (mode.equals("Time Trial")) {
    		System.out.println("Time Left: " + timeLeft);
    	}
        
        for (int i=0; i < grid_size; i++) {
            System.out.println("+-----".repeat(grid_size) + "+");
            for (int j=0; j < grid_size; j++) {
                String num=board[i][j] != 0 ? String.valueOf(board[i][j]) : " ";
                System.out.printf("|%4s ", num);
            }
            System.out.println("|");
        }
        System.out.println("+-----".repeat(grid_size) + "+\n");
    }

    private void move(char command) {
        made_move=false;
        if(command=='w'){
            moveUp();
        }
        else if(command=='s'){
            moveDown();
        } 
        else if(command=='a'){
            moveLeft();
        }
        else if(command=='d'){
            moveRight();
        } else{
            System.out.println("Invalid move.");
        }
        if (made_move) {
            addNewTile();
        } else {
            System.out.println("Can't move in that direction.");
        }
    }

    private void moveLeft() {
        for (int i=0; i < grid_size; i++) {
            int[] row=new int[grid_size];
            int location=0;
            boolean[] move_completed=new boolean[grid_size];

            for (int j=0; j < grid_size; j++) {
                if (board[i][j] != 0) {
                    if (location > 0 && row[location - 1] == board[i][j] && !move_completed[location - 1]) {
                        row[location - 1] *= 2;
                        score += row[location - 1];
                        move_completed[location - 1]=true;
                        if (row[location - 1] == 2048) won=true;
                        made_move=true;
                    } else {
                        if (board[i][j] != row[location]) {
                            made_move=true;
                        }
                        row[location]=board[i][j];
                        location++;
                    }
                }
            }
            board[i]=row;
        }
    }

    private void moveRight() {
        for (int i=0; i < grid_size; i++) {
            int[] row=new int[grid_size];
            int location=grid_size - 1;
            boolean[] move_completed=new boolean[grid_size];

            for (int j=grid_size - 1; j >= 0; j--) {
                if (board[i][j] != 0) {
                    if (location < grid_size - 1 && row[location + 1] == board[i][j] && !move_completed[location + 1]) {
                        row[location + 1] *= 2;
                        score += row[location + 1];
                        move_completed[location + 1]=true;
                        if (row[location + 1] == 2048) won=true;
                        made_move=true;
                    } else {
                        if (board[i][j] != row[location]) {
                            made_move=true;
                        }
                        row[location]=board[i][j];
                        location--;
                    }
                }
            }
            board[i]=row;
        }
    }

    private void moveUp() {
        for (int j=0; j < grid_size; j++) {
            int[] col=new int[grid_size];
            int location=0;
            boolean[] move_completed=new boolean[grid_size];

            for (int i=0; i < grid_size; i++) {
                if (board[i][j] != 0) {
                    if (location > 0 && col[location - 1] == board[i][j] && !move_completed[location - 1]) {
                        col[location - 1] *= 2;
                        score += col[location - 1];
                        move_completed[location - 1]=true;
                        if (col[location - 1] == 2048) won=true;
                        made_move=true;
                    } else {
                        if (board[i][j] != col[location]) {
                            made_move=true;
                        }
                        col[location]=board[i][j];
                        location++;
                    }
                }
            }
            for (int i=0; i < grid_size; i++) {
                board[i][j]=col[i];
            }
        }
    }

    private void moveDown() {
        for (int j=0; j < grid_size; j++) {
            int[] col=new int[grid_size];
            int location=grid_size - 1;
            boolean[] move_completed=new boolean[grid_size];
            for (int i=grid_size - 1; i >= 0; i--) {
                if (board[i][j] != 0) {
                    if (location < grid_size - 1 && col[location + 1] == board[i][j] && !move_completed[location + 1]) {
                        col[location + 1] *= 2;
                        score += col[location + 1];
                        move_completed[location + 1]=true;
                        if (col[location + 1] == 2048) won=true;
                        made_move=true;
                    } else {
                        if (board[i][j] != col[location]) {
                            made_move=true;
                        }
                        col[location]=board[i][j];
                        location--;
                    }
                }
            }
            for (int i=0; i < grid_size; i++) {
                board[i][j]=col[i];
            }
        }
    }

    private boolean hasMoves() {
        for (int i=0; i < grid_size; i++) {
            for (int j=0; j < grid_size; j++) {
                if (board[i][j] == 0)
                    return true;
                if (j < grid_size - 1 && board[i][j] == board[i][j + 1])
                    return true;
                if (i < grid_size - 1 && board[i][j] == board[i + 1][j])
                    return true;
            }
        }
        return false;
    }

    private void gameLoop() {
        Scanner scanner=new Scanner(System.in);
        //Check game modes
        if (mode.equals("Time Trial")) {
        	startTimer(20);
        }else if(mode.equals("Move Limit")) {
        	moves = 10;
        }
        //Print init board (no moves yet)
        printBoard();
        
        while (true) {
        	//if time trial mode and no time left, end game
        	if (mode.equals("Time Trial") && timeLeft == 0) {
        		System.out.println("Time up. Game Over");
        		break;
        	}
        	//if move limit mode and no moves left, end game
        	if (mode.equals("Move Limit") && moves == 0) {
        		System.out.println("No more moves. Game over :(");
        		break;
        	}
        	//User input for move
            System.out.print("Enter move (W/A/S/D): ");
            String input=scanner.nextLine().toLowerCase();
            if (input.isEmpty()) {
                System.out.println("No input detected. Please use W, A, S, or D.");
                continue;
            }
            char move=input.charAt(0);
            move(move);
            
            if (made_move) {
            	if (mode.equals("Move Limit")) {
            		moves--;
            	}
                printBoard();
                if (won) {
                    System.out.println("Congratulations! You've reached 2048!");
                    break;
                }
                if (!hasMoves()) {
                    System.out.println("Game Over!");
                    break;
                }
            }
        }
        scanner.close();
    }
    
    //generic timer method 
    private void startTimer(int seconds) {
    	Timer timer = new Timer();
    	timeLeft = seconds;
    	
    	TimerTask task = new TimerTask() {
    		@Override
    		public void run() {
    			if (timeLeft > 0) {
    				timeLeft--;
    			}else {
    				timer.cancel();
    			}
    		}
    	};
    	timer.scheduleAtFixedRate(task, 0, 1000);
    	
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Ask user for which game mode they want to play
        System.out.println("Choose a game mode\nTraditional, Time Trial, Move Limit\n");
        String input = scanner.nextLine().toLowerCase();
        
        if (input.equals("traditional")) {
        	mode = "Traditional";
        }else if(input.equals("time trial")) {
        	mode = "Time Trial";
        }else if(input.equals("move limit")) {
        	mode = "Move Limit";
        }else {
        	System.out.println("Invalid choice. Default Traditional");
        	mode = "Traditional";
        }
        
    	//pass the game mode found above to the game loop to play the correct game mode
        game2048 game=new game2048();
        game.gameLoop();
        
        scanner.close();
    }
}
