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
    FrameBuffer shadowMapFB;
    TextureRegion occludersTR;
    Texture shadowMapT;
    ShaderProgram shader;
    ShaderProgram shadows;

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
        shader = new ShaderProgram(vert, Gdx.files.internal("shader/shadowMap.frag").readString());
        shadows = new ShaderProgram(vert, Gdx.files.internal("shader/shadowRender.frag").readString());

        occludersFB = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, lightSize, false);
        occludersTR = new TextureRegion(occludersFB.getColorBufferTexture());
        occludersTR.flip(false, true);
        shadowMapFB = new FrameBuffer(Pixmap.Format.RGBA8888, lightSize, 1, false);
        shadowMapT = shadowMapFB.getColorBufferTexture();
        shadowMapT.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        shadowMapT.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        if (!shader.isCompiled()) System.out.println(shader.getLog());
        if (!shadows.isCompiled()) System.out.println(shadows.getLog());
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
        batch.draw(background, -512 + parallax, -512, background.getWidth() * repeat, background.getHeight() * repeat, 0, repeat, repeat, 0);
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
            map.draw(batch);
            player.draw(batch);
        batch.end();

        drawShadows();

        stage.draw();
    }

    int lightSize = 256;
    float lx, ly;
    private void drawOccluders() {
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
    }
    private void drawShadowMap() {
        drawOccluders();

        shadowMapFB.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batch.setShader(null);
        batch.begin();
        shadows.setUniformf("resolution", lightSize, lightSize);

        cam.setToOrtho(false, shadowMapFB.getWidth(), shadowMapFB.getHeight());
        batch.setProjectionMatrix(cam.combined);
        batch.draw(occludersFB.getColorBufferTexture(), 0, 0, lightSize, shadowMapFB.getHeight());

        batch.end();
        shadowMapFB.end();
    }
    private void drawShadows() {
        lx = player.getCenterX() + 16 - lightSize/2;
        ly = player.getCenterY() + 16 - lightSize/2;
        drawShadowMap();

        cam.setToOrtho(false);
        batch.setProjectionMatrix(cam.combined);

        batch.setShader(shadows);
        batch.begin();
        shadows.setUniformf("resolution", lightSize, lightSize);
        shadows.setUniformf("softShadows", 1f);

        batch.setColor(Color.GREEN);
//        batch.draw(shadowMapT, lx, ly, lightSize, lightSize);
        batch.draw(occludersFB.getColorBufferTexture(), lx, ly);
        batch.setColor(Color.WHITE);

        batch.end();

        System.out.println(lx + " " + ly);
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
