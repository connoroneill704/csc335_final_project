import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.HashMap;
public class GUI2048 extends JFrame{
    private game2048 game;
    private HashMap<Integer, ImageIcon> imageMap;
    private JLabel[][] tiles;
    private JLabel scoreLabel;
    private JPanel boardPanel;
    private JPanel scorePanel;

    public GUI2048(){
        game = new game2048();
        loadImages();
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
        }
        game.addNewTile();
        if (game.hasMoves() == false){
            JOptionPane.showMessageDialog(null, "Game Over! Your score is: " + game.getScore());
            System.exit(0);
        }
        updateBoard();
        scoreLabel.setText("Score: " + game.getScore());

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
                    //tiles[i][j].setIcon(imageMap.get(board[i][j]));
                    tiles[i][j].setText("" + board[i][j]);
                    tiles[i][j].setBackground(getNumberColor(board[i][j]));
                }
            }
        }

    }
    private Color getNumberColor(int val){
        switch (val){
            case 2:
                return new Color(160, 220, 250);
            case 4:
                return new Color(140, 220, 250);
            case 8:
                return new Color(120, 220, 250);
            case 16:
                return new Color(100, 220, 250);
            case 32:
                return new Color(80, 220, 250);
            case 64:
                return new Color(60, 220, 250);
            case 128:
                return new Color(40, 220, 250);
            case 256:
                return new Color(20, 220, 250);
            case 512:
                return new Color(0, 220, 250);
            case 1024:
                return new Color(20, 100, 250);
            case 2048:
                return new Color(10, 80, 225);
            default:
                return new Color(0, 60, 225);
        }
    }
}
