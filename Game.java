import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends Canvas {
    boolean gameOn;
    long lastLoopTime;

    public static int width = 800;
    public static int height = 600;

    private boolean keyUp = false;
    private boolean keyDown = false;
    private boolean keyLeft = false;
    private boolean keyRight = false;

    BufferStrategy strategy;

    Entity horse;

    public Game() {
        gameOn = true;
        lastLoopTime = 0;

        horse = new Entity("sprites/horse_0.png", 200, 200);

        makeWindow();
        addKeyListener(new KeyInputHandler());
    }

    public void gameLoop() {
        while (gameOn) {
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.CYAN);
            g.fillRect(0, 0, width, height);

            horse.move(keyUp, keyDown, keyLeft, keyRight);
            horse.draw(g);

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
