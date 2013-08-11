import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Canvas {
    boolean gameOn;
    long lastLoopTime;

    public static int width = 800;
    public static int height = 600;

    BufferStrategy strategy;

    ArrayList<Entity> horses;

    public Game() {
        gameOn = true;
        lastLoopTime = 0;

        horses = new ArrayList<Entity>(100);

        for (int i = 0; i < 20; i++) {
            Entity horse = new Entity("sprites/horse_0.png", 100, 100);
            horses.add(horse);
        }
        makeWindow();
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
            g.setColor(new Color(rand, rand1, rand2));
            g.fillRect(0, 0, width, height);

            for (Entity horse : horses) {
                horse.move();
                horse.draw(g);
            }

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
}
