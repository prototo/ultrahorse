import com.badlogic.gdx.Game;

/**
 * Created by greg on 23/08/13.
 */
public class GDXGame extends Game {
    @Override
    public void create() {
        setScreen(getGameScreen());
    }

    public Screen getGameScreen() {
        return new Screen();
    }
}
