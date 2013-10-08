import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;

public class GameScreen extends Screen implements InputProcessor {
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

    FrameBuffer occluders;
    FrameBuffer shadowMap;
    ShaderProgram shadow;
    ShaderProgram blur;

    public GameScreen(int width, int height) {
        super(width, height);
    }

    public void show() {
        super.show();
        map = new Map(items);
        stats = new Stats();
        player = new Player(stats);
        controller = new Controller(player);
        collide = new Collider(map);
        background = new Texture(Gdx.files.internal("sprites/stars.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        setupUI();

        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(controller);
        multi.addProcessor(this);
        Gdx.input.setInputProcessor(multi);

        ShaderProgram.pedantic = false;
        String vert = Gdx.files.internal("shader/screen.vert").readString();
        shadow = new ShaderProgram(vert, Gdx.files.internal("shader/shadow.frag").readString());
        blur = new ShaderProgram(vert, Gdx.files.internal("shader/blur.frag").readString());

        int lightSize = 256;
        occluders = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        shadowMap = new FrameBuffer(Pixmap.Format.RGBA8888, 1, lightSize, false);

        if (!shadow.isCompiled()) System.out.println(shadow.getLog());
        if (!blur.isCompiled()) System.out.println(blur.getLog());
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
        cam.setToOrtho(false);

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
        cam.update();
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
        label.setText("");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        setCameraPosition();

        int repeat = 6;
        float parallax = cam.position.x * 0.5f;

        batch.begin();
        batch.setShader(null);
        batch.draw(background, -512 + parallax, -512, background.getWidth() * repeat, background.getHeight() * repeat, 0, repeat, repeat, 0);
        batch.end();

        // DEBUG
//        map.drawDebug(debug);
//        player.drawDebug(debug);
        for (Item item : items) {
            item.drawDebug(debug);
        }
        // DEBUG

        drawLights();
        drawEntities();

//        stage.draw();

    }

    private void drawEntities() {
        batch.begin();
        batch.setShader(null);
        map.draw(batch);
        player.draw(batch);
//            for (Item item : items) {
//                item.draw(batch);
//            }
        batch.end();
    }

    OrthographicCamera c = new OrthographicCamera();
    private void drawLight(float lx, float ly, int lightSize, Color lightColor) {
        float dx = lx - lightSize / 2;
        float dy = ly - lightSize / 2;

        occluders = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        shadowMap = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, 1, false);
        Texture shadowMapTex = shadowMap.getColorBufferTexture();

        shadowMapTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        occluders.begin();
        c.setToOrtho(false, occluders.getWidth(), occluders.getHeight());
        c.position.set(lx, ly, 0);
        c.update();
        batch.setProjectionMatrix(c.combined);
        batch.setShader(null);
        drawEntities();
        occluders.end();

        shadowMap.begin();
        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        batch.setShader(shadow);
        batch.begin();
        shadow.setUniformf("resolution", lightSize, lightSize);
        c.setToOrtho(false, shadowMap.getWidth(), shadowMap.getHeight());
        batch.setProjectionMatrix(c.combined);
//        batch.draw(occluders.getColorBufferTexture(), dx, dy, lightSize, shadowMap.getHeight());
        batch.draw(occluders.getColorBufferTexture(), 0, 0, lightSize, shadowMap.getHeight());
        batch.end();
        shadowMap.end();

        batch.setProjectionMatrix(cam.combined);

        batch.setShader(blur);
        batch.begin();
        blur.setUniformf("resolution", lightSize, lightSize);
        Color bc = batch.getColor();
        batch.setColor(lightColor);
        batch.draw(shadowMap.getColorBufferTexture(), dx, dy, lightSize, lightSize);
        batch.setColor(bc);
        batch.end();

        occluders.dispose(); shadowMap.dispose();
    }
    private void drawLights() {
        drawLight(256, 64, 512, new Color(1, 0.5f, 0.5f, 0.5f));
        drawLight(512, 128, 1024, new Color(0.5f, 1, 0.5f, 0.5f));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    /**
     * INPUT PROCESSOR STUFF
     */

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.R) {
            map.populate();
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean keyUp(int i) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean keyTyped(char c) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean scrolled(int i) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
