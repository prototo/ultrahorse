import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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

    public Screen() {
        player = new PlayerEntity(new Vector2(2, 2));
        map = Map.get();
    }

    @Override
    public void resize(int width, int height) {
        map.setSize(width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor( 0.5f, 0.5f, 0.8f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // update the player
        player.update(delta);

        // draw all the things
        map.render();
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
