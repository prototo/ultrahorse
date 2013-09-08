import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import java.util.ArrayList;

public class GameScreen extends Screen {
    Map map;
    Texture background;
    Collider collide;

    Player player;
    Controller controller;
    Stats stats;

    Skin skin;
    Stage stage;
    Label label;

    ArrayList<Item> items = new ArrayList<Item>();

    public GameScreen(int width, int height) {
        super(width, height);
    }

    public void show() {
        super.show();
        map = new Map();
        stats = new Stats();
        player = new Player(stats);
        controller = new Controller(player);
        collide = new Collider(map);
        background = new Texture(Gdx.files.internal("sprites/stars.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        setupUI();

        Gdx.input.setInputProcessor(controller);
    }

    private void setupUI() {
        Table table = new Table();
        table.setFillParent(true);

        skin = new Skin();
        stage = new Stage();
        stage.addActor(table);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        label = new Label("THIS IS A LABEL", skin);
        table.setPosition(50, height - 50);
        table.addActor(label);
    }

    private void setCameraPosition() {
        // center camera on player center
        float camX = player.getCenterX();
        float camY = player.getCenterY();

        // clip camera to map edges
        if (camX < this.width / 2) {
            camX = this.width / 2;
        } else if (camX > map.getWidth() - this.width / 2) {
            camX = map.getWidth() - this.width / 2;
        }
        if (camY < this.height / 2) {
            camY = this.height / 2;
        } else if (camY > map.getHeight() - this.height / 2) {
            camY = map.getHeight() - this.height / 2;
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
                collide.entities(player, item);
                item.update(delta);
            }
        }

        // mo money
//        items.add(new Money(player.getX(), cam.position.y));

        label.setText("" + player.stats.getMoney());

        setCameraPosition();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        int repeat = 6;
        float parallax = cam.position.x * 0.5f;

        batch.begin();
        batch.draw(background, -512 + parallax, -512, background.getWidth() * repeat, background.getHeight() * repeat, 0, repeat, repeat, 0);
        batch.end();

        // DEBUG
        map.drawDebug(debug);
        player.drawDebug(debug);
        for (Item item : items) {
            item.drawDebug(debug);
        }
        // DEBUG

        batch.begin();
        map.draw(batch);
        player.draw(batch);
//        for (Item item : items) {
//            item.draw(batch);
//        }
        batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
