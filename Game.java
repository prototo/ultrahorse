import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Canvas {
    boolean gameOn;
    long lastLoopTime;

    public static int width = 400;
    public static int height = 400;

    public static boolean keyUp = false;
    public static boolean keyDown = false;
    public static boolean keyLeft = false;
    public static boolean keyRight = false;

    public static final double FRICTION = 0.75;
    public static final double DRAG = 0.95;
    public static final double GRAVITY = 0.4;

    private Entity player;

    BufferStrategy strategy;

    ArrayList<Entity> horses;

    public Game() {
        Random r = new Random();

        gameOn = true;
        lastLoopTime = 0;

        player = new PlayerEntity(100, 100);

        makeWindow();
        addKeyListener(new KeyInputHandler());
    }

    public void gameLoop() {
        while (gameOn) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            Random r = new Random();
            int rand = r.nextInt(255);
            int rand1 = r.nextInt(255);
            int rand2 = r.nextInt(255);
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);

            player.move();
            player.draw(g);

            g.dispose();
            strategy.show();

            try {
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }
    }

    private void makeWindow() {
        JFrame container = new JFrame("Ultra Horse");

        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(width, height));
        panel.setLayout(null);

        setBounds(0, 0, width, height);
        panel.add(this);

        setIgnoreRepaint(true);

        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        createBufferStrategy(2);
        strategy = getBufferStrategy();
    }


    public class KeyInputHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                keyLeft = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                keyRight = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                keyUp = true;
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                keyDown = true;
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                keyLeft = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                keyRight = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                keyUp = false;
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                keyDown = false;
            }
        }
    }
}
