import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by greg on 27/08/13.
 */
public class BulletEntity extends Entity {
    private float speed;
    private boolean alive;


    Animation bulletAnimation;

    public BulletEntity(Vector2 position, int direction) {
        super("sprites/player/shot-0.png", position);

        alive = true;
        speed = 15f;
        velocity.x = speed * direction;

        setupAnimation();
    }

    public void update(float delta) {
        super.update(delta);

        stateTime += delta;
        if (velocity.x == 0) {
            die();
        }
    }

    private void setupAnimation() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("sprites/player.pack"));

        // right standing animation
        TextureRegion[] standRightFrames = new TextureRegion[2];
        for (int i = 0; i < standRightFrames.length; i++) {
            standRightFrames[i] = atlas.findRegion("shot-" + i);
        }
        bulletAnimation = new Animation(RUNNING_FRAME_DURATION, standRightFrames);
    }

    public TextureRegion getTextureRegion() {
        return bulletAnimation.getKeyFrame(stateTime, true);
    }

    private void die() {
        this.alive = false;
    }

    public boolean isDead() {
        return !alive;
    }
}
