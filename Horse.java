import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Horse {
    public static final String gameTitle = "Horse Mage Simulator 2013";

    public static int width = 800;
    public static int height = 800;

    public static void main(String []args) {
        new LwjglApplication(
            new GDXGame(), gameTitle, width, height, false
        );
    }
}