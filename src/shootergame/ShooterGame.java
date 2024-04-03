package shootergame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class ShooterGame extends JPanel implements KeyListener, ActionListener {
    private final int WIDTH = 900;
    private final int HEIGHT = 700;
    private final int VELOCITY = 13;
    private final int BULLET_VELOCITY = 17;
    private final int ENEMY_VELOCITY = 9;
    private final int ENEMY_WIDTH = 30;
    private final int ENEMY_HEIGHT = 30;
    private final int GAME_SPEED = 25;
    private final int PLAYER_WIDTH = 30;
    private final int PLAYER_HEIGHT = 30;
    private final int PADDING = 10;
    private final int MAX_BULLETS = 7;
    private Rectangle player;
    private Rectangle enemy;
    private Direction direction;
    private ArrayList<Rectangle> bullets;
    private Random random;
    private Timer timer;
    private int score;
    private boolean running;

    private enum Direction {
        UP, DOWN
    }

    ShooterGame() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this);
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        player = new Rectangle(PADDING, 50, PLAYER_WIDTH, PLAYER_HEIGHT);
        enemy = new Rectangle(WIDTH - ENEMY_WIDTH, HEIGHT / 2 + ENEMY_HEIGHT / 2,
            PLAYER_WIDTH, PLAYER_HEIGHT);
        bullets = new ArrayList<Rectangle>();
        random = new Random();
        score = 0;
        running = true;
        timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g;
        g2D.setPaint(Color.RED);
        g2D.fill(player);
        g2D.setPaint(Color.GREEN);

        for (Rectangle bullet : bullets) {
            g2D.fill(bullet);
        }

        g2D.setPaint(Color.BLUE);
        g2D.fill(enemy);

        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        if (direction == Direction.DOWN &&
            player.y < HEIGHT - PADDING - PLAYER_HEIGHT) {
            player.y += VELOCITY;
        } else if (direction == Direction.UP &&
            player.y > PADDING) {
            player.y -= VELOCITY;
        }

        for (Rectangle bullet : bullets) {
            bullet.x += BULLET_VELOCITY;
        }

        enemy.x -= ENEMY_VELOCITY;
    }

    public void shoot() {
        if (bullets.size() < MAX_BULLETS) {
            bullets.add(new Rectangle(
                player.x + PLAYER_WIDTH - PADDING,
                player.y + PLAYER_HEIGHT / 2 - 2,
                PADDING, 4
            ));
        }
    }

    public void resetEnemy() {
        enemy.x = WIDTH - ENEMY_WIDTH;
        enemy.y = random.nextInt(ENEMY_HEIGHT, HEIGHT) - ENEMY_HEIGHT;
    }

    public void checkCollision() {
        if (enemy.x <= 0) {
            resetEnemy();
            running = false;
        }

        if (bullets.size() == 0) {
            return;
        }

        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).intersects(enemy)) {
                resetEnemy();
                bullets.remove(i);
                score++;
                return;
            }

            if (bullets.get(i).x >= WIDTH) {
                bullets.remove(i);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_J:
                direction = Direction.DOWN;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_K:
                direction = Direction.UP;
                break;
            case KeyEvent.VK_SPACE:
                shoot();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public void game() {
        if (running) {
            move();
            checkCollision();
            repaint();
            System.out.println(score);
        } else {
            System.out.println("Game Over!");
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game();
    }
}
