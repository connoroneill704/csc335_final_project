import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.*;

public class GUI2048 extends JFrame{
    private game2048 game;
    private HashMap<Integer, ImageIcon> imageMap;
    private JLabel[][] tiles;
    private JLabel scoreLabel;
    private JPanel boardPanel;
    private JPanel scorePanel;
    private Clip background;

    public GUI2048(){
        game = new game2048();
        loadImages();
        playBackground(); //starts background music
    }

    public static void main(String[] args) {
        GUI2048 game = new GUI2048();
        game.UI();
    }

    private void loadImages() {
        imageMap = new HashMap<>();
        int[] values = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048};
        for (int val : values) {
            ImageIcon icon = new ImageIcon("images/" + val + ".png");
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            imageMap.put(val, icon);
        }
    }

    public void UI(){
        JFrame frame = new JFrame();
        frame.setTitle("2048 game");
        frame.setSize(400,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        game = new game2048();

        makeBoardPanel();
        makeScorePanel();
        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                useInput(e);
            }
        });
        updateBoard();
        frame.setVisible(true);
    }
    private void makeBoardPanel() {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(4, 4));
        tiles = new JLabel[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setPreferredSize(new Dimension(100, 100));
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tiles[i][j].setOpaque(true);
                tiles[i][j].setBackground(Color.LIGHT_GRAY);
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(tiles[i][j]);
            }
        }
    }
    private void makeScorePanel(){
        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 1));
        scoreLabel = new JLabel("Score: " + game.getScore());
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(scoreLabel);

    }
    private void useInput(KeyEvent e){
        boolean validMove = false;
    	int[][] curBoard = copyBoard(game.getBoard());
    	switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                game.moveUp();
                break;
            case KeyEvent.VK_A:
                game.moveLeft();
                break;
            case KeyEvent.VK_S:
                game.moveDown();
                break;  
            case KeyEvent.VK_D:
                game.moveRight();
                break;
            default:
            	playSound("Sounds/Beep.wav");
                JOptionPane.showMessageDialog(null, "Invalid key! Use W, A, S, D for moves.");
        }
    	//if the board was not changed, dont add a new tile
    	if (!areBoardsEqual(curBoard, game.getBoard())) {
    		validMove = true;
    	}
    	if (validMove) {
            playSound("Sounds/Move.wav"); // Move sound
            game.addNewTile();
            //check if game is over
            if (!game.hasMoves()) {
                JOptionPane.showMessageDialog(null, "Game Over! Your score is: " + game.getScore());
                stopBackground(); // Stop background music
                System.exit(0);
            }
            updateBoard();
            scoreLabel.setText("Score: " + game.getScore());
        }
    }
    
    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }
        return copy;
    }
    
    private boolean areBoardsEqual(int[][] board1, int[][] board2) {
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[i].length; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void updateBoard(){
        int[][] board = game.getBoard();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (board[i][j] == 0){
                    tiles[i][j].setIcon(null);
                    tiles[i][j].setText("");
                    tiles[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    tiles[i][j].setText("" + board[i][j]);
                    tiles[i][j].setBackground(getNumberColor(board[i][j]));
                    tiles[i][j].setFont(new Font("Arial", Font.BOLD, 30)); // Change "30" to your preferred size
                    tiles[i][j].setForeground(Color.BLACK);
                }
            }
        }
    }
    
    private Color getNumberColor(int val){
        switch (val){
	        case 2:
	            return new Color(191, 255, 191); 
	        case 4:
	            return new Color(153, 255, 153); 
	        case 8:
	            return new Color(102, 255, 102);
	        case 16:
	            return new Color(51, 255, 51);  
	        case 32:
	            return new Color(0, 204, 255);   
	        case 64:
	            return new Color(0, 153, 255);   
	        case 128:
	            return new Color(0, 102, 204); 
	        case 256:
	            return new Color(0, 76, 153);   
	        case 512:
	            return new Color(0, 51, 102);    
	        case 1024:
	            return new Color(0, 25, 51);     
	        case 2048:
	            return new Color(0, 0, 30);      
	        default:
	            return new Color(0, 0, 51);  
        }
    }
    
 // Music and Sound Methods
    private void playBackground() {
        try {
            File file = new File("Sounds/Cool.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            background = AudioSystem.getClip();
            background.open(audioStream);
            background.loop(Clip.LOOP_CONTINUOUSLY);
            background.start();
            //volume
            FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-35.0f);
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    private void stopBackground() {
        if (background != null && background.isRunning()) {
            background.stop();
            background.close();
        }
    }

    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
            //volume
            FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}
