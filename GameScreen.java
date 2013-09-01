import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen extends Screen {
    Map map;
    Player player;
    Controller controller;

    public GameScreen(int width, int height) {
        super(width, height);
    }

    public void show() {
        super.show();
        map = new Map();
        player = new Player();
        controller = new Controller(player);

        Gdx.input.setInputProcessor(controller);
    }

    @Override
    protected void gameStep(float delta) {
        player.act(delta);
        player.checkCollisions(delta, map, null);
        player.update(delta);

        cam.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        player.draw(batch);
        batch.end();

//        player.drawDebug(debug);
        map.drawDebug(debug);
    }
}
