import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Main class, doin' its shit
 */
public class Horse {
    private static final String TITLE = "HORSE";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main (String []args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = WIDTH;
        config.height = HEIGHT;
        config.title = TITLE;
        config.useGL20 = true;

        new LwjglApplication(new HorseGame(WIDTH, HEIGHT), config);
    }
}
