import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by greg on 29/08/13.
 */
public class HostileEntity extends Entity {

    int direction = 1;
    boolean idle = false;
    int changeDirectionChance = 20;
    int changeIdle = 20;

    public HostileEntity(Vector2 position) {
        super("sprites/player/running-0.png", position);
        setSize();

        velocity.set(8, 0);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x * Screen.unitSize, position.y * Screen.unitSize, bounds.getWidth() * Screen.unitSize, bounds.getHeight() * Screen.unitSize);
    }

    public void update(float delta) {
        velocity.y += (-40f * delta);   // GRAVITY :D

        Random r = new Random();
        if (r.nextInt(changeDirectionChance) == 1) {
            direction *= -1;
        }

        if (r.nextInt(changeIdle) == 1) {
            idle = !idle;
        }

        super.update(delta);
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return null;
    }
}
