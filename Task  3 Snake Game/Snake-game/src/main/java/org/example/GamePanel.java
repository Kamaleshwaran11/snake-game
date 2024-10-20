package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private static final int TILE_SIZE = 20; // Size of each tile
    private static final int WIDTH = 20; // Width of the game board
    private static final int HEIGHT = 20; // Height of the game board
    private static final int GAME_SPEED = 100; // Speed of the game

    private ArrayList<Point> snake; // List to store the snake's body parts
    private Point food; // The food's location
    private char direction; // Current direction of the snake
    private boolean running; // Game state

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'S') direction = 'W'; // Up
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'W') direction = 'S'; // Down
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'D') direction = 'A'; // Left
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'A') direction = 'D'; // Right
                        break;
                }
            }
        });
        snake = new ArrayList<>();
        direction = 'D'; // Start moving to the right
    }

    public void startGame() {
        running = true;
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2)); // Start position
        spawnFood();

        Timer timer = new Timer(GAME_SPEED, this);
        timer.start(); // Start the game loop
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        // Ensure food is not placed on the snake
        while (snake.contains(food)) {
            food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Draw snake
            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + (snake.size() - 1), 10, 20);
        } else {
            // Game over message
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();
            checkCollision();
            checkFoodCollision();
            repaint(); // Redraw the game panel
        }
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'W': newHead.translate(0, -1); break; // Up
            case 'S': newHead.translate(0, 1); break; // Down
            case 'A': newHead.translate(-1, 0); break; // Left
            case 'D': newHead.translate(1, 0); break; // Right
        }

        snake.add(0, newHead); // Add new head
        snake.remove(snake.size() - 1); // Remove the tail
    }

    private void checkCollision() {
        Point head = snake.get(0);
        // Check wall collisions
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }
        // Check self-collision
        if (snake.subList(1, snake.size()).contains(head)) {
            running = false;
        }
    }

    private void checkFoodCollision() {
        Point head = snake.get(0);
        if (head.equals(food)) {
            // Grow the snake
            snake.add(new Point(head)); // Add a new segment
            spawnFood(); // Spawn new food
        }
    }
}