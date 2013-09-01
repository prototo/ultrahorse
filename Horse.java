import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Main class, doin' its shit
 */
public class Horse {
    private static final String TITLE = "HORSE";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static void main (String []args) {
        new LwjglApplication(new HorseGame(WIDTH, HEIGHT), TITLE, WIDTH, HEIGHT, false);
    }
}
