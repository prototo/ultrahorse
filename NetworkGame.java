import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: greg
 * Date: 09/09/13
 * Time: 19:18
 * To change this template use File | Settings | File Templates.
 */
public class NetworkGame extends GameScreen {
    Kryo kryo;
    Player opponent;

    public NetworkGame(int width, int height) {
        super(width, height);
    }

    public void render(float delta) {
        super.render(delta);
        if (opponent != null) {
            batch.begin();
            opponent.draw(batch);
            batch.end();
        }
    }

    public void show() {
        super.show();
        opponent = new Player(new Stats());
        opponent.tint = new Color(0, 1, 1, 1);
    }

    protected void register() {
        kryo.register(Vector2.class);
        kryo.register(String.class);
    }
}
