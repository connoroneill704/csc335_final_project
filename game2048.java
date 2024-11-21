import java.util.*;
//For Sounds
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.AudioInputStream;
import java.io.File;


class game2048 {
    private int grid_size=4;
    private int[][] board;
    private int score;
    private boolean made_move;
    private boolean won;
    //for game modes
    private static String mode;
    private int moves;
    private int timeLeft;
    //for sounds
    private Clip background;

    public game2048() {
        board=new int[grid_size][grid_size];    // create size of board
        score=0;
        addNewTile();   // add 2 tiles to start game
        addNewTile();
    }
    // add new tile to board
    public void addNewTile() { 
        Random rand=new Random();   // init random
        int value=rand.nextDouble() < 0.9 ? 2 : 4;  // 90% chance of 2, 10% chance of 4
        
        List<int[]> emptyCells=new ArrayList<>();  // list for empty cells

        for (int i=0; i < grid_size; i++) { // loop rows
            for (int j=0; j < grid_size; j++) { // loop columns
                if (board[i][j] == 0) { // if cell is empty
                    emptyCells.add(new int[]{i, j}); // add cell to list
                }
            }
        }

        if (emptyCells.isEmpty()) { // no empty cells
            return; // skip add
        }

        int[] randCell=emptyCells.get(rand.nextInt(emptyCells.size())); // get random empty cell from the list
        board[randCell[0]][randCell[1]]=value; // add new cell to board

    }

    public int[][] getBoard() {
        return board;
    }

    // display board
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

    // cast moves
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
        	playSound("Sounds/Beep.wav");
            System.out.println("Invalid move.");
        }
        if (made_move) {
        	playSound("Sounds/Move.wav");
            addNewTile();
        } else {
        	playSound("Sounds/Beep.wav");
            System.out.println("Can't move in that direction.");
        }
    }

    // move left
    public void moveLeft() {
        for (int i=0; i < grid_size; i++) { // loop rows
            int[] row=new int[grid_size];   // create new row
            int location=0; 
            boolean[] move_completed=new boolean[grid_size];    // track moves

            for (int j=0; j < grid_size; j++) { // loop columns
                if (board[i][j] != 0) { // if cell is not empty
                    if (location > 0 && row[location - 1] == board[i][j] && !move_completed[location - 1]) {    // if cell is same as cell to the left
                        row[location - 1] *= 2;   // double cell value
                        score += row[location - 1];   // update score
                        move_completed[location - 1]=true;
                        if (row[location - 1] == 2048) won=true;    // if new cell is 2048, game is won
                        made_move=true; 
                    } else {    // if cell is different
                        if (board[i][j] != row[location]) { 
                            made_move=true;
                        }
                        row[location]=board[i][j];  // move cell to the left
                        location++; // next cell
                    }
                }
            }
            board[i]=row;   // update row
        }
    }

    // move right
    public void moveRight() {
        for (int i=0; i < grid_size; i++) { // loop rows
            int[] row=new int[grid_size];   // create new row
            int location=grid_size - 1; 
            boolean[] move_completed=new boolean[grid_size];    

            for (int j=grid_size - 1; j >= 0; j--) {    // loop columns
                if (board[i][j] != 0) { // if cell is not empty
                    if (location < grid_size - 1 && row[location + 1] == board[i][j] && !move_completed[location + 1]) {    // if cell is same as cell to the right
                        row[location + 1] *= 2;     // double cell value
                        score += row[location + 1]; // update score
                        move_completed[location + 1]=true;  
                        if (row[location + 1] == 2048) won=true;    // if new cell is 2048, game is won
                        made_move=true; 
                    } else {    // if cell is different
                        if (board[i][j] != row[location]) {
                            made_move=true;
                        }
                        row[location]=board[i][j];  // move cell to the right
                        location--; // next cell
                    }
                }
            }
            board[i]=row;   // update row
        }
    }

    // move up
    public void moveUp() {
        for (int j=0; j < grid_size; j++) { // loop columns
            int[] col=new int[grid_size];   // create new column
            int location=0; // start at first cell
            boolean[] move_completed=new boolean[grid_size];

            for (int i=0; i < grid_size; i++) { // loop rows
                if (board[i][j] != 0) { // if cell is not empty
                    if (location > 0 && col[location - 1] == board[i][j] && !move_completed[location - 1]) {    // if cell is same as cell above
                        col[location - 1] *= 2;    // double cell value
                        score += col[location - 1];   // update score
                        move_completed[location - 1]=true;  
                        if (col[location - 1] == 2048) won=true;    // if new cell is 2048, game is won
                        made_move=true;
                    } else {    // if cell is different
                        if (board[i][j] != col[location]) { 
                            made_move=true;
                        }
                        col[location]=board[i][j];  // move cell up
                        location++; // next cell
                    }
                }
            }
            for (int i=0; i < grid_size; i++) { // update column
                board[i][j]=col[i];
            }
        }
    }

    // move down
    public void moveDown() {
        for (int j=0; j < grid_size; j++) { // loop columns
            int[] col=new int[grid_size];   
            int location=grid_size - 1; 
            boolean[] move_completed=new boolean[grid_size];
            for (int i=grid_size - 1; i >= 0; i--) {    // loop rows
                if (board[i][j] != 0) { // if cell is not empty
                    if (location < grid_size - 1 && col[location + 1] == board[i][j] && !move_completed[location + 1]) {    // if cell is same as cell below
                        col[location + 1] *= 2;   // double cell value
                        score += col[location + 1]; // update score
                        move_completed[location + 1]=true;  
                        if (col[location + 1] == 2048) won=true;
                        made_move=true; 
                    } else {    // if cell is different
                        if (board[i][j] != col[location]) {
                            made_move=true;
                        }
                        col[location]=board[i][j];  // move cell down
                        location--; // next cell
                    }
                }
            }
            for (int i=0; i < grid_size; i++) { // update column
                board[i][j]=col[i];
            }
        }
    }
    // check if there are any moves left
    public boolean hasMoves() {    
        for (int i=0; i < grid_size; i++) { // loop rows
            for (int j=0; j < grid_size; j++) { // loop columns
                if (board[i][j] == 0)   // if cell is empty
                    return true;    // moves left
                if (j < grid_size - 1 && board[i][j] == board[i][j + 1])    // if cell is same as cell to the right
                    return true;    // moves left
                if (i < grid_size - 1 && board[i][j] == board[i + 1][j])    // if cell is same as cell below
                    return true;    // moves left
            }
        }
        return false;   // no moves left
    }

    // game loop
    private void gameLoop() {
        Scanner scanner=new Scanner(System.in);
        //Check game modes
        if (mode.equals("Time Trial")) {
        	startTimer(40);
        }else if(mode.equals("Move Limit")) {
        	moves = 30;
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
            System.out.print("Enter move (W/A/S/D/exit): ");
            String input=scanner.nextLine().toLowerCase();
            if (input.isEmpty()) {
            	System.out.println("No input detected. Please use W, A, S, D, or exit.");
                continue;
            }
            if (input.equals("exit")) {
            	System.out.println("Quit");
            	break;
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
    
    // Timer for Time Trial mode
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
    	//runs task (run()) once every second. When no time left, timer cancels
    	timer.scheduleAtFixedRate(task, 0, 1000);
    }
    
    // music
    private void playBackground() {
    	try {
    		File file = new File("Sounds/Cool.wav");
    		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            background = AudioSystem.getClip();
            background.open(audioStream);
            background.loop(Clip.LOOP_CONTINUOUSLY); // Loop music
            background.start();
            //volume
            FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-35.0f);
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    // end music
    private void stopBackground() {
        if (background != null && background.isRunning()) {
        	background.stop();
        	background.close();
        }
    }
    
    // sound effects
    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            //volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.start();
            
           
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Ask user for which game mode they want to play
        System.out.println("Choose a game mode\nTraditional, Time Trial, Move Limit");
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
        
        game2048 game=new game2048();
        game.playBackground();
        
        game.gameLoop();
        game.stopBackground();
        
        scanner.close();
    }

    public int getScore() {
        return score;
    }
}

