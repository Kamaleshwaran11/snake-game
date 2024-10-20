package org.example;
import javax.swing.*;

public class SnakeGame extends JFrame {
    public SnakeGame() {
        // Set up the game window
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        pack();

        setVisible(true);
        gamePanel.startGame(); // Start the game
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}