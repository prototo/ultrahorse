import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by greg on 23/08/13.
 */
public class Screen implements com.badlogic.gdx.Screen {
    ArrayList<Entity> tiles;
    PlayerEntity player;

    SpriteBatch batch = new SpriteBatch();
    Map map;
    OrthographicCamera cam;

    private Texture background;

    float CAMWIDTH = Horse.width;
    float CAMHEIGHT = Horse.height;
    float CAM_MIN_X = CAMWIDTH/2;
    float CAM_MIN_Y = CAMHEIGHT/2;
    float CAM_MAX_X, CAM_MAX_Y;

    public Screen() {
        player = new PlayerEntity(new Vector2(2, 2));
        map = Map.get();
        cam = new OrthographicCamera(CAMWIDTH, CAMHEIGHT);
        background = new Texture("sprites/stars.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    /**
     * Update the camera position to focus on the player entity
     * staying inside the map bounds
     */
    private void updateCamera() {
        float camx = player.position.x * map.ppux;
        float camy = player.position.y * map.ppuy;

        if (camx < CAM_MIN_X) {
            camx = CAM_MIN_X;
        }
        if (camx > CAM_MAX_X) {
            camx = CAM_MAX_X;
        }

        if (camy < CAM_MIN_Y) {
            camy = CAM_MIN_Y;
        }
//        else if (camy > CAM_MAX_Y) {
//            camy = CAM_MAX_Y;
//        }

        cam.position.x = camx;
        cam.position.y = camy;
        cam.update();
    }

    @Override
    public void resize(int width, int height) {
        map.setSize(width, height);

        // update camera!
        CAM_MAX_X = map.getPixelWidth() - CAM_MIN_X;
        CAM_MAX_Y = map.getPixelHeight() - CAM_MIN_Y;
        updateCamera();
    }

    @Override
    public void render(float delta) {
        GL10 gl = Gdx.graphics.getGL10();

        gl.glClearColor(0.5f, 0.5f, 0.8f, 1f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // update the player
        player.update(delta);

        updateCamera();
        batch.setProjectionMatrix(cam.combined);

        // render the background slightly behind for parallax
        float parallax = cam.position.x - CAM_MIN_X;
        parallax *= 0.8;

        // draw all the things
        batch.begin();
        batch.draw(background, parallax, 0, background.getWidth() * 3, background.getHeight() * 3, 0, 3, 3, 0);
        map.render(batch);
        batch.draw(player.getTextureRegion(), player.position.x * map.ppux, player.position.y * map.ppuy, player.bounds.width * map.ppux, player.bounds.height * map.ppuy);
        batch.end();
    }


    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
