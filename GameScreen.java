import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import javax.print.attribute.standard.PDLOverrideSupported;

/**
 * Created by greg on 23/08/13.
 */
public class GameScreen extends Screen {
    protected PlayerEntity player;
    protected Map map;

    protected HostileEntity hostile;

    public GameScreen() {
        super("sprites/stars.png");

        player = new PlayerEntity(new Vector2(2, 2));
        map = Map.get();

        hostile = new HostileEntity(new Vector2(8, 8));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(player);
    }

    /**
     * Update the camera position to focus on the player entity
     * staying inside the map bounds
     */
    protected void updateCamera() {
        float x = player.position.x * unitSize;
        float y = player.position.y * unitSize;

        if (x < CAM_MIN_X) {
            x = CAM_MIN_X;
        }
        if (x > CAM_MAX_X) {
            x = CAM_MAX_X;
        }

        if (y < CAM_MIN_Y) {
            y = CAM_MIN_Y;
        }

        cam.position.x = x;
        cam.position.y = y;
        cam.update();
    }

    @Override
    public void resize(int width, int height) {
        // update the unitSize, etc
        super.resize(width, height);

        float mapWidth = map.getPixelWidth(unitSize);
        float mapHeight = map.getPixelHeight(unitSize);

        CAM_MAX_X = mapWidth - CAM_MIN_X;
        CAM_MAX_Y = mapHeight - CAM_MIN_Y;
        BG_REPEAT_X = mapWidth / background.getWidth();
        BG_REPEAT_Y = mapHeight / background.getHeight();
        BG_REPEAT_Y += 1;

        // update camera!
        updateCamera();
    }

    @Override
    protected void renderSprites() {
        map.render(batch, unitSize);
        player.draw(batch, unitSize);
        hostile.draw(batch);
    }

    @Override
    protected void update(float delta) {
        player.setSize();
        player.update(delta);

        hostile.update(delta);
    }
}
