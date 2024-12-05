import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.*;
import java.util.List;
/**
 * Graphical user interface for the 2048 game.
 * Allows the user to interact with the game with W, A, S, and D. 
 * Displays the current game state and user score in a simple window.
 */
public class GUI2048 extends JFrame{
    private logic2048 game;
    private HashMap<Integer, ImageIcon> imageMap;
    private JLabel[][] tiles;
    private JLabel scoreLabel;
    private JPanel boardPanel;
    private JPanel scorePanel;
    private Clip background;
    private JButton undoButton;
    private HighScoreManager highScoreManager;
    private Settings settings;
    

    public GUI2048(){
        game = new logic2048(4);
        loadImages();
        playBackground();
        highScoreManager = new HighScoreManager();
        settings = new Settings();
        applySettings(); 
    }
    
    /**
     * Main method that starts a new game of 2048 and starts the GUI.
     */
    public static void main(String[] args) {
        GUI2048 game = new GUI2048();
        game.UI();
    }
    
    /**
     * Loads the images for each tile
     */
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
    
    /**
     * Initializes the graphical user interface, with a game board and score display
     */
    public void UI(){
        JFrame frame = new JFrame();
        frame.setTitle("2048 game");
        frame.setSize(400,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        makeBoardPanel();
        makeScorePanel();
        createMenuBar();
        frame.add(scoreLabel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        makeControlPanel();
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                useInput(e);
            }
        });
        updateBoard();
        frame.setVisible(true);
    }

    private void applySettings() {
        double volume = settings.getVolume();
        setVolume(volume);
        String theme = settings.getTheme();
        setTheme(theme);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> openSettingsDialog());

        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresItem.addActionListener(e -> displayHighScores());

        menu.add(settingsItem);
        menu.add(highScoresItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void openSettingsDialog() {
        JDialog settingsDialog = new JDialog(this, "Settings", true);
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(3, 2));

        // Volume control
        JLabel volumeLabel = new JLabel("Volume:");
        JSlider volumeSlider = new JSlider(0, 100, (int)(settings.getVolume() * 100));
        volumeSlider.addChangeListener(e -> {
            double volume = volumeSlider.getValue() / 100.0;
            settings.setVolume(volume);
            setVolume(volume);
        });

        // Theme selection
        JLabel themeLabel = new JLabel("Theme:");
        String[] themes = {"Default", "Dark", "Light"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        themeComboBox.setSelectedItem(settings.getTheme());
        themeComboBox.addActionListener(e -> {
            String theme = (String) themeComboBox.getSelectedItem();
            settings.setTheme(theme);
            setTheme(theme);
        });

        // Add components to dialog
        settingsDialog.add(volumeLabel);
        settingsDialog.add(volumeSlider);
        settingsDialog.add(themeLabel);
        settingsDialog.add(themeComboBox);

        // Save and Close button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            settings.saveSettings();
            settingsDialog.dispose();
        });
        settingsDialog.add(new JLabel()); // Placeholder
        settingsDialog.add(saveButton);

        settingsDialog.setVisible(true);
    }

    private void setVolume(double volume) {
    }

    private void setTheme(String theme) {
    }

    private void displayHighScores() {
        List<HighScoreManager.ScoreEntry> highScores = highScoreManager.getHighScores();
        StringBuilder sb = new StringBuilder();
        sb.append("High Scores:\n");
        for (int i = 0; i < highScores.size(); i++) {
            HighScoreManager.ScoreEntry entry = highScores.get(i);
            sb.append(String.format("%d. %s - %d\n", i + 1, entry.getName(), entry.getScore()));
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Initializes the game board in a 4 x 4 grid
     */
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
    
    /**
     * Initializes the score panel
     * 
     * @post	the score panel with be initizaled to 0 points
     */
    private void makeScorePanel(){
        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 1));
        scoreLabel = new JLabel("Score: " + game.getScore());
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(scoreLabel);
    }
    
    /**
     * Handles the user input's from the keyboard (W,A,S,D) and calls the neccessary
     * game functions to update the board and score.
     * Also checks if the game is valid and ends game if the user loses
     * 
     * @param	e - The key pressed by the user
     */
    private void useInput(KeyEvent e){
        boolean validMove = false;
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
                return;
        }
    	if (game.hasMadeMove()) {
            validMove = true;
        }
    
        if (validMove) {
            playSound("Sounds/Move.wav"); 
            updateBoard();
            scoreLabel.setText("Score: " + game.getScore());
            if (!game.hasMoves()) {
                playSound("Sounds/Beep.wav");
                stopBackground(); // Stop background music
                String name = JOptionPane.showInputDialog(this, "Game Over! Your score is: " + game.getScore() + "\nEnter your name:");
                if (name != null && !name.trim().isEmpty()) {
                    highScoreManager.addScore(name, game.getScore());
                }
                displayHighScores();
                System.exit(0);
            }
        }
    }
    
    /**
     * Creates a copy of the current game board to be compared
     * 
     * @param 	board - the current game board being copied
     * @return	a copied version of the same 2D array that is the current game board
     */
    private Tile[][] copyBoard(Tile[][] board) {
        Tile[][] copy = new Tile[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Tile tile = board[i][j];
                if (tile != null) {
                    copy[i][j] = new Tile(tile.getValue(), tile.getRow(), tile.getCol());
                } else {
                    copy[i][j] = null;
                }
            }
        }
        return copy;
    }
    
    /**
     * Compares two boards to see if they are equals
     * 
     * @param	board1 - first board to compare
     * @param	board2 - second board being compared
     * @return	if the boards are equal or not
     */
    private boolean areBoardsEqual(Tile[][] board1, Tile[][] board2) {
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[i].length; j++) {
                Tile tile1 = board1[i][j];
                Tile tile2 = board2[i][j];
                if (tile1 == null && tile2 == null) {
                    continue;
                }
                if (tile1 == null || tile2 == null) {
                    return false;
                }
                if (tile1.getValue() != tile2.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Updates the board state of the GUI based on the current game state
     */
    private void updateBoard() {
        Tile[][] board = game.getGameBoard().getBoard();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Tile tile = board[i][j];
                if (tile == null) {
                    tiles[i][j].setIcon(null);
                    tiles[i][j].setText("");
                    tiles[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    int value = tile.getValue();
                    tiles[i][j].setText("" + value);
                    tiles[i][j].setBackground(getNumberColor(value));
                    tiles[i][j].setFont(new Font("Arial", Font.BOLD, 30));
                    tiles[i][j].setForeground(Color.BLACK);
                }
            }
        }
    }
    
    
    /**
     * Returns the color of the background of each respective tile
     * 
     * @param 	val - the valeue of the tile being colored (2, 4, 8, ...)
     * @return	the color associated with that tile value
     */
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
    /**
     * Starts playing the background music from the Sounds folder in the directory
     * 
     * @post	background music will be playing
     */
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

    /**
     * Stops the background music being played (when game ends)
     * 
     * @post	no background music will be playing
     */
    private void stopBackground() {
        if (background != null && background.isRunning()) {
            background.stop();
            background.close();
        }
    }

    /**
     * Plays the inputted sound effect for a given game action
     * 
     * @param	soundFile - the .wav file that is to be played for a specific action
     * @post 	the .wav file from soundFile will be played one time
     */
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

    private JPanel controlPanel;

    private void makeControlPanel() {
        controlPanel = new JPanel();
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            game.undoMove();
            updateBoard();
            scoreLabel.setText("Score: " + game.getScore());
        });
        controlPanel.add(undoButton);
    }
}