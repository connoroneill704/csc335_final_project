import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.*;
import java.util.List;

/**
 * Runs the Graphical User Interface for the 2048 game
 * 
 * Connor O'Neil 	- connoroneil
 * Eli Jordan		- ejordan3
 * Derek Hoshaw 	- dthoshaw
 * AJ Becerra		- ajbecerra
 */
public class GUI2048 {
    private logic2048 game;
    private HashMap<Integer, ImageIcon> imageMap;
    private TileLabel[][] tiles;
    private JLabel scoreLabel;
    private JPanel boardPanel;
    private JPanel scorePanel;
    private Clip background;
    private JButton undoButton;
    private HighScoreManager highScoreManager;
    private Settings settings;
    private JFrame frame;
    private JPanel controlPanel;
    private int timeLeft;
    private int movesLeft;
    private Timer timer; 

    /**
     * Constructs the GUI for 2048 game
     */
    public GUI2048() {
        // Initialize the main JFrame
        frame = new JFrame("2048 game");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setContentPane(new BackgroundPanel("images/christmas_background.jpg"));

        loadImages();
        playBackground();
        highScoreManager = new HighScoreManager();
        settings = new Settings();
        applySettings();

        createMenuBar();
        makeControlPanel();
        startNewGame();
        
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        updateBoard();
        frame.setVisible(true);

        setupKeyBindings();
    }

    /**
     * Runs GUI2048.java
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI2048());
    }

    /**
     * Applies the settings from the settings file to the current game
     */
    private void applySettings() {
        double volume = settings.getVolume();
        setVolume(volume);
        String theme = settings.getTheme();
        setTheme(theme);
    }

    /**
     * Creates menu bar to move around the GUI
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> startNewGame());

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> openSettingsDialog());

        JMenuItem highScoresItem = new JMenuItem("High Scores");
        highScoresItem.addActionListener(e -> displayHighScores());

        menu.add(newGameItem);
        menu.add(settingsItem);
        menu.add(highScoresItem);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }

    /**
     * Starts a new game of 2048 by prompting for a grid size selection, game mode selection, then starts the game
     */
    private void startNewGame() {
        // Prompt for grid size
        String[] gridSizes = {"4", "5", "6"};
        String gridSizeStr = (String) JOptionPane.showInputDialog(
                frame,
                "Select Grid Size:",
                "New Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gridSizes,
                gridSizes[0]);
        int gridSize = 4; // Default
        if (gridSizeStr != null && !gridSizeStr.isEmpty()) {
            gridSize = Integer.parseInt(gridSizeStr);
        }

        // Prompt for game mode
        String[] gameModes = {"Traditional", "Time Trial", "Move Limit"};
        String gameMode = (String) JOptionPane.showInputDialog(
                frame,
                "Select Game Mode:",
                "New Game",
                JOptionPane.QUESTION_MESSAGE,
                null,
                gameModes,
                gameModes[0]);

        game = new logic2048(gridSize);
        // If logic2048 supports setting the mode, set it here: game.setMode(gameMode);
        if (timer != null) {
            timer.stop();
        }

        switch (gameMode) {
            case "Time Trial":
                timeLeft = 60; // Set to 60 seconds
                startTimer();
                break;
            case "Move Limit":
                movesLeft = 30; // Set to 30 moves
                break;
        }
        makeScorePanel();
        makeBoardPanel(gridSize);
        updateBoard();
        frame.requestFocusInWindow();
    }

    /**
     * Opens the settings dialog box and allows for changes to be made to the theme and volume
     */
    private void openSettingsDialog() {
        JDialog settingsDialog = new JDialog(frame, "Settings", true);
        settingsDialog.setSize(300, 200);
        settingsDialog.setLayout(new GridLayout(3, 2));

        JLabel volumeLabel = new JLabel("Volume:");
        JSlider volumeSlider = new JSlider(0, 100, (int)(settings.getVolume() * 100));
        volumeSlider.addChangeListener(e -> {
            double volume = volumeSlider.getValue() / 100.0;
            settings.setVolume(volume);
            setVolume(volume);
        });

        JLabel themeLabel = new JLabel("Theme:");
        String[] themes = {"Christmas"};
        JComboBox<String> themeComboBox = new JComboBox<>(themes);
        themeComboBox.setSelectedItem(settings.getTheme());
        themeComboBox.addActionListener(e -> {
            String theme = (String) themeComboBox.getSelectedItem();
            settings.setTheme(theme);
            setTheme(theme);
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            settings.saveSettings();
            settingsDialog.dispose();
        });

        settingsDialog.add(volumeLabel);
        settingsDialog.add(volumeSlider);
        settingsDialog.add(themeLabel);
        settingsDialog.add(themeComboBox);
        settingsDialog.add(new JLabel()); // placeholder
        settingsDialog.add(saveButton);

        settingsDialog.setLocationRelativeTo(frame);
        settingsDialog.setVisible(true);
    }

    /**
     * Sets the volume of the sounds
     */
    private void setVolume(double volume) {
    	try {
            if (background != null && background.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20);
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            System.err.println("Error adjusting volume: " + e.getMessage());
        }
    }

    /**
     * Sets the theme of the game
     */
    private void setTheme(String theme) {
    	frame.repaint();
    }

    /**
     * Displays the high scores dialog box 
     */
    private void displayHighScores() {
        List<HighScoreManager.ScoreEntry> highScores = highScoreManager.getHighScores();
        StringBuilder sb = new StringBuilder();
        sb.append("High Scores:\n");
        for (int i = 0; i < highScores.size(); i++) {
            HighScoreManager.ScoreEntry entry = highScores.get(i);
            sb.append(String.format("%d. %s - %d\n", i + 1, entry.getName(), entry.getScore()));
        }
        JOptionPane.showMessageDialog(frame, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates the display area for the 2048 board game
     */
    private void makeBoardPanel(int gridSize) {
        if (boardPanel != null) {
            frame.remove(boardPanel);
        }
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(gridSize, gridSize));
        tiles = new TileLabel[gridSize][gridSize];
        Font tileFont = new Font("Arial", Font.BOLD, 24);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                tiles[i][j] = new TileLabel(null, 0, tileFont);
                tiles[i][j].setPreferredSize(new Dimension(400 / gridSize, 400 / gridSize));
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tiles[i][j].setVerticalAlignment(SwingConstants.CENTER);
                tiles[i][j].setOpaque(true);
                tiles[i][j].setBackground(Color.LIGHT_GRAY);
                tiles[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(tiles[i][j]);
            }
        }
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Creates display panel for the score of the game
     */
    private void makeScorePanel() {
        if (scorePanel == null) {
            scorePanel = new JPanel();
            scorePanel.setLayout(new GridLayout(1, 1));
            frame.add(scorePanel, BorderLayout.NORTH);
        }

        // Remove old components from the score panel
        scorePanel.removeAll();

        // Create and add the updated score label
        scoreLabel = new JLabel(getScoreDisplay());
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scorePanel.add(scoreLabel);
        scorePanel.revalidate();
        scorePanel.repaint();
    }

    /**
     * Creates the score and game mode display fields
     */
    private String getScoreDisplay() {
        StringBuilder display = new StringBuilder("Score: " + game.getScore());
        if (timeLeft > 0) {
            display.append(" | Time Left: ").append(timeLeft).append("s");
        } else if (movesLeft > 0) {
            display.append(" | Moves Left: ").append(movesLeft);
        }
        return display.toString();
    }
    
    /**
     * Starts the timer for the game
     */
    private void startTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            scoreLabel.setText(getScoreDisplay()); // Update the display every second

            if (timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(frame, "Time's up! Game Over.");
                endGame();
            }
        });
        timer.start();
    }
    
    /**
     * Runs when the game ends to save high score and choose to start a new game
     */
    private void endGame() {
        if (timer != null) {
            timer.stop();
        }
        String name = JOptionPane.showInputDialog(frame, "Game Over! Your score is: " + game.getScore() + "\nEnter your name:");
        if (name != null && !name.trim().isEmpty()) {
            highScoreManager.addScore(name, game.getScore());
        }
        displayHighScores();

        int choice = JOptionPane.showConfirmDialog(
            frame,
            "Would you like to start a new game?",
            "Game Over",
            JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            startNewGame();
        }
    }

    /**
     * Runs the game for one input and checks for game status every input
     */
    private void useInput(int keyCode) {
        boolean validMove = false;
        switch (keyCode) {
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
                JOptionPane.showMessageDialog(frame, "Invalid key! Use W, A, S, D for moves.");
                return;
        }

        if (game.hasMadeMove()) {
            validMove = true;
        }

        if (validMove) {
            playSound("Sounds/Move.wav");
            updateBoard();
            scoreLabel.setText("Score: " + game.getScore());

            // Check for win
            if (game.isWon()) {
                playSound("Sounds/Win.wav");
                int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "You reached 2048! Would you like to continue playing?",
                    "Congratulations!",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.NO_OPTION) {
                    startNewGame();
                    return;
                }
            }

            // Check for game over
            if (!game.hasMoves()) {
                playSound("Sounds/Beep.wav");

                if (!game.getSecLife()) {
                    // Launch Minesweeper for second chance
                    game.setSecLife();
                    MineSweeperGame mineGame = new MineSweeperGame(8, 8, 1); // 8x8 Minesweeper grid with 10 mines
                    MineSweeperUI mineUI = new MineSweeperUI(mineGame);

                    // Wait for Minesweeper to finish
                    JOptionPane.showMessageDialog(frame, "Complete Minesweeper to earn a second chance!");
                    mineUI.showGameRules();

                    // Block until Minesweeper is closed
                    mineUI.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            if (mineGame.getHasWon()) {
                                // Player won Minesweeper
                                game.randomizeBoard();
                                updateBoard();
                                JOptionPane.showMessageDialog(frame, "You earned a second chance! The board has been randomized.");
                            } else {
                                // Player lost Minesweeper
                                endGame();
                            }
                        }
                    });
                    return;
                }

                // Game over if second chance was already used
                stopBackground();
                String name = JOptionPane.showInputDialog(frame, "Game Over! Your score is: " + game.getScore() + "\nEnter your name:");
                if (name != null && !name.trim().isEmpty()) {
                    highScoreManager.addScore(name, game.getScore());
                }
                displayHighScores();

                // Offer to start a new game instead of exiting
                int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "No more moves left! Would you like to start a new game?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    startNewGame();
                }
            }
        }
    }

    /**
     * Updates the game board GUI based on new positions
     */
    private void updateBoard() {
        Tile[][] board = game.getGameBoard().getBoard();
        int gridSize = game.getGameBoard().getGridSize();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Tile tile = board[i][j];
                TileLabel tileLabel = tiles[i][j];
                if (tile == null) {
                    tileLabel.setTileIcon(null);
                    tileLabel.setValue(0);
                    tileLabel.setBackground(Color.LIGHT_GRAY);
                } else {
                    int value = tile.getValue();
                    tileLabel.setTileIcon(imageMap.get(value));
                    tileLabel.setValue(value);
                    tileLabel.setBackground(getNumberColor(value));
                }
            }
        }
        scoreLabel.setText("Score: " + game.getScore());
    }

    /**
     * Returns a color for each tile based on the input value
     */
    private Color getNumberColor(int val) {
        switch (val) {
            case 2: return new Color(255, 230, 230);
            case 4: return new Color(230, 255, 230);
            case 8: return new Color(255, 204, 204);
            case 16: return new Color(204, 255, 204);
            case 32: return new Color(255, 153, 153);
            case 64: return new Color(153, 255, 153);
            case 128: return new Color(255, 102, 102);
            case 256: return new Color(102, 255, 102);
            case 512: return new Color(255, 255, 153);
            case 1024: return new Color(255, 255, 102);
            case 2048: return new Color(255, 255, 51);
            default: return new Color(255, 255, 255);
        }
    }

    /**
     * Plays background music for the game 
     */
    private void playBackground() {
        try {
            File file = new File("Sounds/Cool.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            background = AudioSystem.getClip();
            background.open(audioStream);
            background.loop(Clip.LOOP_CONTINUOUSLY);
            background.start();
            FloatControl gainControl = (FloatControl) background.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-35.0f);
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Stops background music for the game 
     */
    private void stopBackground() {
        if (background != null && background.isRunning()) {
            background.stop();
            background.close();
        }
    }

    /**
     * Plays sound effect for the game 
     */
    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    /**
     * Creates control panel for undo button 
     */
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

    /**
     * Parses user inputs for each action performed
     */
    private void setupKeyBindings() {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useInput(KeyEvent.VK_W);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useInput(KeyEvent.VK_A);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useInput(KeyEvent.VK_S);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useInput(KeyEvent.VK_D);
            }
        });
    }

    /**
     * loads each tile image
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
}
