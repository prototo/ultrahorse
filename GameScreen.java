import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends Screen {
    Map map;
    Player player;
    Controller controller;
    Texture background;
    Collider collide;

    ArrayList<Item> items = new ArrayList<Item>();

    public GameScreen(int width, int height) {
        super(width, height);
    }

    public void show() {
        super.show();
        map = new Map();
        player = new Player();
        controller = new Controller(player);
        collide = new Collider(map);
        background = new Texture(Gdx.files.internal("sprites/stars.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        Gdx.input.setInputProcessor(controller);
    }

    private void setCameraPosition() {
        // center camera on player center
        float camX = player.getCenterX();
        float camY = player.getCenterY();

        // clip camera to map edges
        if (camX < cam.viewportWidth / 2) {
            camX = cam.viewportWidth / 2;
        } else if (camX > map.getWidth() - cam.viewportWidth / 2) {
            camX = map.getWidth() - cam.viewportWidth / 2;
        }
        if (camY < cam.viewportHeight / 2) {
            camY = cam.viewportHeight / 2;
        } else if (camY > map.getHeight() - cam.viewportHeight / 2) {
            camY = map.getHeight() - cam.viewportHeight / 2;
        }

        cam.position.set(camX, camY, 0);
    }

    @Override
    protected void gameStep(float delta) {
        player.act(delta);
        collide.withMap(player, delta);
        player.update(delta);

        // deal with all the items
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            if (item.remove) {
                items.remove(item);
            } else {
                item.act(delta);
                collide.withMap(item, delta);
                item.update(delta);
            }
        }
        if (new Random().nextInt(10) == 1) {
            Item item = new Item(player.getCenterX(), player.getCenterY(), 16, 16);
            item.randomVelocity().setBouncey(true).setExpire(true); // SUGAR
            items.add(item);
        }

        setCameraPosition();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        int repeat = 6;
        float parallax = cam.position.x * 0.5f;

        batch.begin();
        batch.draw(background, -512 + parallax, -512, background.getWidth() * repeat, background.getHeight() * repeat, 0, repeat, repeat, 0);
        player.draw(batch);

//        for (Item item : items) {
//            item.draw(batch);
//        }

        batch.end();

        player.drawDebug(debug);
        map.drawDebug(debug);

        for (Item item : items) {
            item.drawDebug(debug);
        }
    }
}
