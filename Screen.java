import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by greg on 23/08/13.
 */
public abstract class Screen implements com.badlogic.gdx.Screen {
    protected SpriteBatch batch = new SpriteBatch();
    protected OrthographicCamera cam;
    protected Texture background;

    public static float unitSize;

    float CAMWIDTH = Horse.width;
    float CAMHEIGHT = Horse.height;
    float CAM_MIN_X = CAMWIDTH/2;
    float CAM_MIN_Y = CAMHEIGHT/2;
    float CAM_MAX_X, CAM_MAX_Y;
    float BG_REPEAT_X = 1, BG_REPEAT_Y = 1;

    public Screen(String backgroundRef) {
        cam = new OrthographicCamera(CAMWIDTH, CAMHEIGHT);
        background = new Texture(backgroundRef);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    /**
     * Update the camera position to focus on the player entity
     * staying inside the map bounds
     */
    protected abstract void updateCamera();

    public void resize(int width, int height) {
        // update the 'global' window dimensions
        cam.viewportHeight = height;
        cam.viewportWidth = width;

        // update the unit pixel size
        unitSize = (float) height / (float) Horse.unitsAcross;

        updateCamera();
    }

    protected abstract void renderSprites();
    protected abstract void update(float delta);

    public void render(float delta) {
        update(delta);

        GL10 gl = Gdx.graphics.getGL10();

        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        updateCamera();
        batch.setProjectionMatrix(cam.combined);

        // render the background slightly behind for parallax
        float parallax = cam.position.x - CAM_MIN_X;
        parallax *= 0.8;

        batch.begin();

            // draw the background
            batch.draw(
                    background, parallax, 0,
                    background.getWidth() * BG_REPEAT_X, background.getHeight() * BG_REPEAT_Y,
                    0, BG_REPEAT_Y, BG_REPEAT_X, 0
            );

            // draw everything else
            renderSprites();

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
