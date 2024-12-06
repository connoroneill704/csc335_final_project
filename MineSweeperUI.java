/**
 * Names: Connor O'Neill, Eli Jordan, Derek Hoshaw, 
 * Net Ids: connoroneill ejordan3 dthoshaw
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI for the Minesweeper game.
 */
public class MineSweeperUI extends JFrame {
    private final MineSweeperGame game;
    private final JButton[][] buttons;
    private final ImageIcon giftIcon;
    private final ImageIcon flagIcon;
    private final ImageIcon mineIcon;
    private boolean gameOver = false;


    /**
     * Constructs the Minesweeper UI with a given game instance.
     * Initializes the game board, loads icons for the cells,
     * and sets up the main game window.
     *
     * @param game the Minesweeper game instance containing the game logic
     */
    public MineSweeperUI(MineSweeperGame game) {
        this.game = game;
        this.buttons = new JButton[game.getRows()][game.getCols()];
        
        this.giftIcon = new ImageIcon("MineSweeperIcons/gift.png");
        this.flagIcon = new ImageIcon("MineSweeperIcons/candy_cane.png");
        this.mineIcon = new ImageIcon("MineSweeperIcons/o_bomb.png");
        
        setTitle("Minesweeper");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createMenu();
        initializeBoard();
        setVisible(true);
    }

    /**
     * Creates the menu bar for the Minesweeper game.
     * Adds a "Help" menu with a "Game Rules" option that
     * displays a dialog box explaining how to play the game.
     */
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu helpMenu = new JMenu("Help");
        JMenuItem gameRulesItem = new JMenuItem("Game Rules");

        gameRulesItem.addActionListener(e -> showGameRules());

        helpMenu.add(gameRulesItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Displays the rules of the Minesweeper game in a dialog box.
     * Explains how to play the game, the win and lose conditions,
     * and provides basic gameplay instructions.
     */
    private void showGameRules() {
        String rules = """
                How to Play Minesweeper:
                - Left-click to reveal a tile.
                - Right-click to flag a suspected mine.
                - Avoid clicking on mines to keep playing.

                Win Condition:
                - Reveal all non-mine tiles.

                Lose Condition:
                - Click a mine.
                """;

        JOptionPane.showMessageDialog(this, rules, "Game Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Initializes the game board by creating a grid of buttons.
     * Each button is initialized with the default gift icon and
     * configured to respond to left and right mouse clicks.
     * 
     * - Left-click reveals the cell.
     * - Right-click toggles a flag on the cell.
     *
     * The layout is set to a grid matching the game's row and column count.
     */
    private void initializeBoard() {
        setLayout(new GridLayout(game.getRows(), game.getCols()));

        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                JButton button = new JButton(giftIcon); // Set gift icon as default
                buttons[i][j] = button;

                int row = i;
                int col = j;

                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(row, col);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            revealCell(row, col);
                        }
                    }
                });

                add(button);
            }
        }
    }


    /**
     * Toggles a flag on a cell.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    private void toggleFlag(int row, int col) {
    	// Prevent further interaction if the game is over
    	if (gameOver) {
        	return; 
        }

        Cell cell = game.getCell(row, col);
        if (!cell.isRevealed()) {
            cell.toggleFlag();
            buttons[row][col].setIcon(cell.isFlagged() ? flagIcon : giftIcon); // Toggle between flag and gift icons
        }
    }


    /**
     * Reveals the specified cell and triggers game-over logic if the cell is a mine.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    private void revealCell(int row, int col) {
        if (gameOver) {
            return; // Do nothing if the game is already over
        }

        Cell cell = game.getCell(row, col);
        if (cell.isRevealed() || cell.isFlagged()) {
            return;
        }

        cell.reveal();

        if (cell.isMine()) {
            buttons[row][col].setIcon(mineIcon);
            buttons[row][col].setDisabledIcon(mineIcon);
            buttons[row][col].setBackground(Color.RED);
            buttons[row][col].setOpaque(true);
            revealAllMines();
            JOptionPane.showMessageDialog(this, "BOOM! Game Over!");
            gameOver = true;
        } else {
            buttons[row][col].setIcon(null);
            if (cell.getAdjacentMines() > 0) {
                buttons[row][col].setText(String.valueOf(cell.getAdjacentMines()));
            } else {
                buttons[row][col].setText("");
            }
            buttons[row][col].setFont(new Font("Arial", Font.BOLD, 18));
            buttons[row][col].setBackground(Color.WHITE);
            buttons[row][col].setOpaque(true);
        }
        buttons[row][col].setEnabled(false);

        if (cell.getAdjacentMines() == 0) {
            for (int r = -1; r <= 1; r++) {
                for (int c = -1; c <= 1; c++) {
                    int newRow = row + r;
                    int newCol = col + c;
                    if (game.isValidCell(newRow, newCol)) {
                        revealCell(newRow, newCol);
                    }
                }
            }
        }

        // Check if the player has won after revealing the cell
        checkWin();
    }

    
    /**
     * Checks if the player has won the game by revealing all non-mine cells.
     * If all non-mine cells are revealed, displays a win message.
     */
    private void checkWin() {
        if (gameOver) {
            return; // Do nothing if the game is already over
        }

        boolean allNonMineRevealed = true;

        // Check if all non-mine cells have been revealed
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                Cell cell = game.getCell(i, j);
                if (!cell.isMine() && !cell.isRevealed()) {
                    allNonMineRevealed = false;
                    break;
                }
            }
        }

        if (allNonMineRevealed) {
            // Player wins if all non-mine cells are revealed
            JOptionPane.showMessageDialog(this, "You won! Congratulations!");
            revealAllMines();
            gameOver = true;
        }
    }


    
    /**
     * Reveals all mines on the board after the game ends.
     */
    private void revealAllMines() {
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getCols(); j++) {
                Cell cell = game.getCell(i, j);
                if (cell.isMine()) {
                    buttons[i][j].setIcon(mineIcon); // Display the bomb icon
                    buttons[i][j].setDisabledIcon(mineIcon); // Ensure the bomb icon is visible when disabled
                    buttons[i][j].setBackground(Color.RED); // Highlight the mine cells
                    buttons[i][j].setOpaque(true); // Ensure the background color is applied
                }
                buttons[i][j].setEnabled(false); // Disable all buttons
            }
        }
    }
}
