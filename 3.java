import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener, Runnable {
    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private static final int WIDTH = TILE_SIZE * GRID_SIZE;
    private static final int HEIGHT = TILE_SIZE * GRID_SIZE;

    private LinkedList<Point> snake;
    private Point food;
    private int direction; // 0: up, 1: right, 2: down, 3: left
    private boolean isRunning;
    private int score;
    private Random random;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = 1; // Start moving right
        isRunning = true;
        score = 0;
        random = new Random();
        spawnFood();
    }

    private void spawnFood() {
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 0: newHead.y--; break; // Up
            case 1: newHead.x++; break; // Right
            case 2: newHead.y++; break; // Down
            case 3: newHead.x--; break; // Left
        }

        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            score++;
            spawnFood();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }

        if (checkCollision(newHead)) {
            isRunning = false;
        }
    }

    private boolean checkCollision(Point head) {
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            return true; // Collision with wall
        }
        for (Point body : snake) {
            if (head.equals(body) && head != snake.getFirst()) {
                return true; // Collision with itself
            }
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && direction != 2) direction = 0;
        if (key == KeyEvent.VK_RIGHT && direction != 3) direction = 1;
        if (key == KeyEvent.VK_DOWN && direction != 0) direction = 2;
        if (key == KeyEvent.VK_LEFT && direction != 1) direction = 3;
        if (key == KeyEvent.VK_R && !isRunning) restartGame();
    }

    private void restartGame() {
        snake.clear();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = 1;
        isRunning = true;
        score = 0;
        spawnFood();
    }

    @Override
    public void run() {
        while (true) {
            if (isRunning) {
                move();
                repaint();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        new Thread(game).start();
    }
}
