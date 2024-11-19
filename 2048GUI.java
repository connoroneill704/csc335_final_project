import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class 2048GUI{
    private 2048game game;
    private JLabel[][] tiles;
    private JLabel scoreLabel;
    public 2048GUI{
        setTitle("2048 game");
        setSize(400,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        game = new Game2048();
        makeBoardPanel();
        makeScorePanel();
        add(scoreLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                useInput(e);
            }
        });
        updateBoard();
        setVisible(true);
    }
    private void makeBoardPanel() {

    }
    private void makeScorePanel(){

    }
    private void useInput(KeyEvent e){

    }
    private void updateBoard(){

    }
    private Color getNumberColor(int val){

    }
}