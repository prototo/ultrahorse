import com.badlogic.gdx.Game;

public class HorseGame extends Game {
    private GameScreen gameScreen;

    public HorseGame(int width, int height) {
        gameScreen = new GameScreen(width, height);
    }

    @Override
    public void create() {
        setScreen(gameScreen);
    }
}
