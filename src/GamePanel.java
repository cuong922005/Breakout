import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*; 
import java.awt.image.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private static GamePanel instance;

    private JButton backButton;

    
    // Định nghĩa các hằng số và biến
    static final int GAME_WIDTH = 461;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (1.25));
    static final Dimension SCREEN_SIZE1 = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int PADDLE_WIDTH = 55;
    static final int PADDLE_HEIGHT = 10;
    static final int BALL_DIAMETER = 8;
    int lives = 3;
    int score = 0;
    int hits = 0;
    int choice = 0;
    int inclinationSelection = 0;
    String welcomeMessage = "PRESS SPACE";
    boolean attractModeActive = true;
    boolean soundPlaying;
    boolean allCleared;
    static final int rows = 14;
    static final int columns = 8;
    static final int brickWidth = 32;
    static final int brickHeight = 10;
    static final int BORDER_OFFSET = 20;
    static final int DISTANZA = 20;
    BufferedImage buffer;
    Graphics graphics;
    Paddle paddle1;
    Ball ball;
    Brick[][] brick;
    Welcome welcome;
    Lives livesUI;
    Score scoreUI;
    Font atari;
    Color ballColor;
    Random random;
    Clip sound;
    GameThread gameThread;
    

    GamePanel() { 
        random = new Random();
        brick = new Brick[rows][columns];
        livesUI = new Lives(GAME_WIDTH - 20, GAME_HEIGHT - 20, 20, 20);
        scoreUI = new Score(GAME_WIDTH - 20, GAME_HEIGHT - 20, 20, 20);
        ballColor = Color.white;

        try {
            InputStream fontLocation = getClass().getResourceAsStream("fonts/Atari.ttf");
            atari = Font.createFont(Font.TRUETYPE_FONT, fontLocation).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setFocusable(true);
        this.setPreferredSize(SCREEN_SIZE1);
        this.addKeyListener(new AL());

        attractModePaddles();
        newBricks();
        newBall();
        newWelcome();
    }

    public static GamePanel getInstance() {
        if (instance == null) {
            instance = new GamePanel();
        }
        return instance;
    }
    
    public void start() {
        if (gameThread == null || !gameThread.getRunning()) {
            gameThread = new GameThread(this);
            gameThread.setRunning(true);
            gameThread.start();
        }
    }

    public void newPaddles() {
        paddle1 = new Paddle((GAME_WIDTH - PADDLE_WIDTH) / 2, GAME_HEIGHT - (PADDLE_HEIGHT - DISTANZA / 2) - 50, PADDLE_WIDTH, PADDLE_HEIGHT);
    }

    public void newBricks() {
        for (int p = 0; p < rows; p++) {
            for (int l = 0; l < columns; l++) {
                brick[p][l] = new Brick(p, l, brickWidth, brickHeight);
            }
        }
    }

    public void newBall() {
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT / 2) - (BALL_DIAMETER / 2), BALL_DIAMETER, BALL_DIAMETER);
        ball.setDY(1);
        hits = 0;
    }

    public void newWelcome() {
        welcome = new Welcome(GAME_WIDTH / 2, GAME_HEIGHT / 2, GAME_WIDTH / 15, GAME_HEIGHT / 15);
    }

    public void destroyWelcome() {
        welcomeMessage = " ";
    }

    public void playSound(String fileName) {
        if (!soundPlaying) {
            try {
                sound = AudioSystem.getClip();
                sound.open(AudioSystem.getAudioInputStream(getClass().getResource("audio/" + fileName)));
                soundPlaying = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Couldn't play sound due to an error. Check above this message to see what happened.");
            }
        }

        if (soundPlaying) {
            sound.start();
        }

        soundPlaying = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graphics = buffer.getGraphics();
        draw(graphics);
        g.drawImage(buffer, 0, 0, this);
    }

    public void draw(Graphics g) {
        allCleared = true;
        if (attractModeActive) {
            switch (choice) {
                case 0:
                    ballColor = Color.cyan;
                    break;
                case 1:
                    ballColor = Color.magenta;
                    break;
                case 2:
                    ballColor = Color.red;
                    break;
                case 3:
                    ballColor = Color.orange;
                    break;
                case 4:
                    ballColor = Color.yellow;
                    break;
                case 5:
                    ballColor = Color.green;
                    break;
                default:
                    ballColor = Color.white;
                    break;
            }
        }
        paddle1.draw(g);
        ball.draw(g, ballColor);
        welcome.draw(g, atari, GAME_WIDTH, GAME_HEIGHT, welcomeMessage);

        for (int p = 0; p < rows; p++) {
            for (int l = 0; l < columns; l++) {
                if (brick[p][l] != null) {
                    brick[p][l].draw(g);
                    allCleared = false;
                }
            }
        }

        if (allCleared) {
            beginAttractMode();
            welcomeMessage = "YOU WON!";
        }

        livesUI.draw(g, atari, GAME_WIDTH, GAME_HEIGHT, lives);
        scoreUI.draw(g, atari, GAME_WIDTH, GAME_HEIGHT, score);
        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        paddle1.move();
        ball.move();
    }

    public void checkCollision() {
        if (paddle1.x <= 0)
            paddle1.x = 0;
        if (paddle1.x >= GAME_WIDTH - PADDLE_WIDTH)
            paddle1.x = GAME_WIDTH - PADDLE_WIDTH;
        if (ball.y <= 0) {
            ball.dy = -ball.dy;
            playSound("boundary.wav");
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.dy = -ball.dy;
            if (lives > 0) {
                lives--;
            }
            checkIfLost(lives);
            newBall();
            playSound("boundary.wav");
        }
        if (ball.x <= 0) {
            ball.dx = -ball.dx;
            playSound("boundary.wav");
            if (attractModeActive) {
                choice = random.nextInt(6);
            }
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            ball.dx = -ball.dx;
            playSound("boundary.wav");
            if (attractModeActive) {
                choice = random.nextInt(6);
            }
        }
        if (ball.intersects(paddle1)) {
            double inclination;
            if (!attractModeActive) {
                hits++;
                if (ball.x + (BALL_DIAMETER / 2) <= paddle1.x + PADDLE_WIDTH / 8) {
                    inclination = -1.6;
                } else if (ball.x + (BALL_DIAMETER / 2) <= paddle1.x + (PADDLE_WIDTH / 8) * 2) {
                    inclination = -1.4;
                } else if (ball.x + (BALL_DIAMETER / 2) <= paddle1.x + (PADDLE_WIDTH / 8) * 3) {
                    inclination = -0.7;
                } else if (ball.x + (BALL_DIAMETER / 2) <= paddle1.x + (PADDLE_WIDTH / 8) * 5) {
                    inclination = 0.55;
                    if (random.nextInt(2) == 0) {
                        inclination = inclination * -1;
                    }
                } else if (ball.x + (BALL_DIAMETER / 2) <= paddle1.x + (PADDLE_WIDTH / 8) * 6) {
                    inclination = 0.7;
                } else if (ball.x + (BALL_DIAMETER / 2) <= paddle1.x + (PADDLE_WIDTH / 8) * 7) {
                    inclination = 1.4;
                } else {
                    inclination = 1.6;
                }
            } else {
                choice = random.nextInt(6);
                inclinationSelection = random.nextInt(3);
                switch (inclinationSelection) {
                    case 0:
                        inclination = 1.6;
                        break;
                    case 1:
                        inclination = 1.4;
                        break;
                    case 2:
                        inclination = 0.7;
                        break;
                    default:
                        inclination = 0.55;
                        break;
                }
                inclinationSelection = random.nextInt(2);
                if (inclinationSelection == 0) {
                    inclination = inclination * -1;
                }
            }

            if (hits < 4) {
                ball.setDY(1);
            } else if (hits >= 4 && hits < 12) {
                ball.setDY(1.5);
            } else if (hits >= 12) {
                ball.setDY(2);
            }
            ball.dy = -ball.dy;
            ball.setDX(inclination);
            playSound("paddle.wav");
        }

        for (int r = 0; r < rows; r++) {
            for (int t = 0; t < columns; t++) {
                if (brick[r][t] != null) {
                    if (ball.intersects(brick[r][t])) {
                        ball.dy = -ball.dy;
                        playSound("brick.wav");
                        if (!attractModeActive) {
                            brick[r][t] = null;
                            switch (t) {
                                case 0:
                                case 1:
                                    score += 7;
                                    break;
                                case 2:
                                case 3:
                                    score += 5;
                                    break;
                                case 4:
                                case 5:
                                    score += 3;
                                    break;
                                default:
                                    score += 1;
                                    break;
                            }
                        } else {
                            choice = random.nextInt(4);
                        }
                    }
                }
            }
        }
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && !attractModeActive) {
                paddle1.setDeltaX(-1);
            }
            if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && !attractModeActive) {
                paddle1.setDeltaX(1);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE && attractModeActive) {
                attractModeActive = false;
                beginGame();
            }
        }

        public void keyReleased(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && !attractModeActive) {
                paddle1.setDeltaX(0);
            }
            if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && !attractModeActive) {
                paddle1.setDeltaX(0);
            }
        }
    }

    public void checkIfLost(int lives) {
        if (lives < 1) {
            beginAttractMode();
        }
    }

    public void beginAttractMode() {
        attractModePaddles();
        newWelcome();
        attractModeActive = true;
        welcomeMessage = "PRESS SPACE";
    }

    public void attractModePaddles() {
        paddle1 = new Paddle(0, GAME_HEIGHT - (PADDLE_HEIGHT - DISTANZA / 2) - 50, GAME_WIDTH, PADDLE_HEIGHT);
    }

    public void beginGame() {
        newPaddles();
        newBall();
        newBricks();
        destroyWelcome();
        lives = 3;
        score = 0;
        ballColor = Color.white;
    }
}
