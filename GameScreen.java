import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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

    FrameBuffer occludersFB;
    FrameBuffer second;
    FrameBuffer third;
    FrameBuffer fourth;
    ShaderProgram shader;
    ShaderProgram reduce;
    ShaderProgram shadow;

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
        shader = new ShaderProgram(vert, Gdx.files.internal("shader/screen.frag").readString());
        reduce = new ShaderProgram(vert, Gdx.files.internal("shader/reduce.frag").readString());
        shadow = new ShaderProgram(vert, Gdx.files.internal("shader/shadow.frag").readString());

        occludersFB = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        second = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        third = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        fourth = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);

        if (!shader.isCompiled()) System.out.println(shader.getLog());
        if (!reduce.isCompiled()) System.out.println(reduce.getLog());
        if (!shadow.isCompiled()) System.out.println(shadow.getLog());
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

        setCameraPosition();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        int repeat = 6;
        float parallax = cam.position.x * 0.5f;

        batch.setShader(null);
        batch.begin();
//        batch.draw(background, -512 + parallax, -512, background.getWidth() * repeat, background.getHeight() * repeat, 0, repeat, repeat, 0);
        batch.end();

        // DEBUG
//        map.drawDebug(debug);
//        player.drawDebug(debug);
        for (Item item : items) {
            item.drawDebug(debug);
        }
        // DEBUG

        batch.begin();
        batch.setShader(null);
        TextureRegion t = new TextureRegion(fourth.getColorBufferTexture());
//            t.flip(false, true);

        batch.draw(third.getColorBufferTexture(), 32 + 256, 32 + 256);
        batch.draw(t, 32, 32 + 256);
        batch.draw(t, 32, 32);
        map.draw(batch);
        player.draw(batch);

        batch.end();

        debug.begin(ShapeRenderer.ShapeType.Box);
        debug.box(32, 32 + 256, 0, 256, 256, 1);
        debug.box(32, 32, 0, 256, 256, 1);
        debug.end();

        drawShadows();

        stage.draw();
    }

    int lightSize = 256;
    float lx, ly;
    private void drawOccluders() {
        float dx = lx - lightSize / 2;
        float dy = ly - lightSize / 2;

        occludersFB.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        cam.setToOrtho(false, occludersFB.getWidth(), occludersFB.getHeight());
        cam.position.set(lx, ly, 0);
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        batch.setShader(null);
        batch.begin();
            map.draw(batch);
            player.draw(batch);
//            for (Item item : items) {
//                item.draw(batch);
//            }
        batch.end();
        occludersFB.end();


        second.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        cam.setToOrtho(false, lightSize, lightSize);
        cam.position.set(lx, ly, 0);
        cam.update();
        batch.setShader(shader);
        batch.begin();
            batch.draw(occludersFB.getColorBufferTexture(), dx, dy);
        batch.end();
        second.end();


        third.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        cam.setToOrtho(false, lightSize, lightSize);
        cam.position.set(lx, ly, 0);
        cam.update();
        batch.setShader(reduce);
        batch.begin();
            batch.draw(second.getColorBufferTexture(), dx, dy);
        batch.end();
        third.end();

        fourth.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        cam.setToOrtho(false, lightSize, lightSize);
        cam.update();
        batch.setShader(shadow);
        batch.begin();
            batch.draw(third.getColorBufferTexture(), dx, dy);
        batch.end();
        fourth.end();
    }
    private void drawShadows() {
        lx = 128 + 32;
        ly = 128 + 32;

        drawOccluders();
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
