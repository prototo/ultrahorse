import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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

    float CAMWIDTH = Horse.width;
    float CAMHEIGHT = Horse.height;

    public Screen() {
        player = new PlayerEntity(new Vector2(2, 2));
        map = Map.get();

        cam = new OrthographicCamera(CAMWIDTH, CAMHEIGHT);
        cam.position.set(CAMHEIGHT / 2, CAMHEIGHT / 2, 0);
    }

    @Override
    public void resize(int width, int height) {
        map.setSize(width, height);
    }

    @Override
    public void render(float delta) {
        GL10 gl = Gdx.graphics.getGL10();

        gl.glClearColor(0.5f, 0.5f, 0.8f, 1f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // update the player
        player.update(delta);

        // work out new camera position
        float camx, camy;
        camx = (player.position.x * map.ppux) - cam.position.x;
        camy = (player.position.y * map.ppuy) - cam.position.y;

        cam.translate(camx, camy);
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        // draw all the things
        map.render(batch);
        batch.begin();
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
