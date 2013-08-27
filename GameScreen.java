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

    public GameScreen() {
        super("sprites/stars.png");

        player = new PlayerEntity(new Vector2(2, 2));
        map = Map.get();

        BG_REPEAT_X = map.getPixelWidth() / background.getWidth();
        BG_REPEAT_Y = map.getPixelHeight() / background.getHeight();
        BG_REPEAT_Y += 1;
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
        CAM_MAX_X = map.getPixelWidth() - CAM_MIN_X;
        CAM_MAX_Y = map.getPixelHeight() - CAM_MIN_Y;

        // update camera!
        updateCamera();
    }

    @Override
    protected void renderSprites() {
        map.render(batch);
        batch.draw(player.getTextureRegion(), player.position.x * map.ppux, player.position.y * map.ppuy);
    }

    @Override
    protected void update(float delta) {
        player.update(delta);
    }
}
