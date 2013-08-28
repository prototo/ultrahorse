import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

/**
 * Created by greg on 23/08/13.
 */
public class GDXGame extends Game {
    private boolean SHOW_MAIN_MENU = true;

    private MainScreen main;
    private GameScreen game;

    @Override
    public void create() {
//        SHOW_MAIN_MENU = false;

        Gdx.graphics.setVSync(true);

        main = new MainScreen(this);
        game = new GameScreen();

        if (SHOW_MAIN_MENU) {
            setScreen(main);
        } else {
            setScreen(game);
        }
    }

    public void startGame() {
        main.dispose();
        setScreen(game);
    }
}
