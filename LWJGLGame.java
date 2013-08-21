import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Random;

import  static org.lwjgl.opengl.GL11.*;

/**
 * Created by greg on 21/08/13.
 */
public class LWJGLGame implements Game {

    String gameTitle = "Horse Mage Simulator 2013";

    // display dimensions
    int width = 800;
    int height = 600;

    int rotate = 0;

    long lastFrame;

    /**
     * Constructor
     */
    public LWJGLGame() {
        makeDisplay();
        gameLoop();
    }

    /**
     * Create the game display
     */
    public void makeDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            Display.setTitle(gameTitle);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Run the game loop
     */
    public void gameLoop() {
        initGL();

        while (!Display.isCloseRequested()) {
            update();

            Display.update();
            Display.sync(60);   // cap to 60fps
        }

        Display.destroy();
    }

    /**
     * Update everything that needs updating, one step
     */
    void update() {
        int delta = getDelta();

        drawGL();
    }

    int getDelta() {
        long time = Sys.getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    void initGL() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, 0, height, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(0f, 0f, 0f, 0f);
    }

    void drawGL() {
        // clear screen and depth buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // set drawing colour
        glColor3f(1f, 0f, 0f);

        // draw some shit
        // I REMEMBERED ENOUGH TO DO THIS OMG
        glPushMatrix();
        glTranslatef(200, 200, 0);
        glRotatef(rotate, 0, 0, 1);
        glBegin(GL_QUADS);
            glVertex2f(-100, -100);
            glVertex2f(100, -100);
            glVertex2f(100, 100);
            glVertex2f(-100, 100);
        glEnd();
        glPopMatrix();

        rotate += 2;
        if (rotate > 360) {
            rotate -= 360;
        }
    }
}
