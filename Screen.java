import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;

/**
 * General screen stuff that can be shared
 */
public abstract class Screen implements com.badlogic.gdx.Screen{
    private int width, height;
    protected OrthographicCamera cam;
    protected Vector2 center;
    protected SpriteBatch batch;
    protected ShapeRenderer debug;

    protected abstract void gameStep(float delta);

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        this.center = new Vector2(0, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gameStep(delta);
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        debug.setProjectionMatrix(cam.combined);
    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fit.apply(this.width, this.height, width, height);
        int viewportX = (int) (width - size.x) / 2;
        int viewportY = (int) (height - size.y) / 2;
        int viewportWidth = (int) size.x;
        int viewportHeight = (int) size.y;
        float zoom = width < viewportWidth ?
                    (float) this.height / viewportHeight :
                    (float) this.width / viewportWidth;

        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        cam.position.set(center.x, center.y, 0);
        cam.zoom = zoom;
        cam.viewportWidth = viewportWidth;
        cam.viewportHeight = viewportHeight;
    }

    @Override
    public void show() {
        this.cam = new OrthographicCamera(width, height);
        this.batch = new SpriteBatch();
        this.debug = new ShapeRenderer();
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
