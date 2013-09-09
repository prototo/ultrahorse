import com.badlogic.gdx.Game;

public class HorseGame extends Game {
    private GameScreen gameScreen;
    private ServerGame server;
    private ClientGame client;

    boolean isClient = true;
//    boolean isClient = false;

    public HorseGame(int width, int height) {
        if (isClient) {
            client = new ClientGame(width, height);
        } else {
            server = new ServerGame(width, height);
        }
    }

    @Override
    public void create() {
        if (isClient) {
            setScreen(client);
        } else {
            setScreen(server);
        }
    }
}
