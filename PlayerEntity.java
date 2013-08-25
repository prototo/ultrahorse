import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;

import java.util.HashMap;

/**
 * Created by greg on 8/13/13.
 */
public class PlayerEntity extends Entity implements InputProcessor {

    enum State {
        STANDING, RUNNING, JUMPING
    }
    private State state = State.STANDING;

    float ACCELERATION = 5f;
    float FRICTION = 0.5f;
    float GRAVITY = -30f;
    float JUMP_FORCE = 10f;
    float JUMP_TIMER = 0.4f;
    float jumpTimer = 0f;

    private final float RUNNING_FRAME_DURATION = 0.3f;
    private float stateTime = 0f;
    private boolean facingLeft = false;

    private Animation runLeftAnimation;
    private Animation runRightAnimation;
    private TextureRegion standLeft;
    private TextureRegion standRight;

    public PlayerEntity(Vector2 position) {
        super("sprites/player/standing/1.png", position);

        this.speed = 6f;
        this.bounds.setSize(1, 1);

        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.SPACE, false);

        setupAnimations();
        Gdx.input.setInputProcessor(this);
    }

    public void update(float delta) {
        stateTime += delta;

        // UPDATE X
        if (keys.get(Keys.LEFT)) {
            velocity.x = -speed;
            facingLeft = true;
        } else if (keys.get(Keys.RIGHT)) {
            velocity.x = speed;
            facingLeft = false;
        } else {
            velocity.x = 0;
        }

        // update state and stateTime for animations
        if (state == State.STANDING && velocity.x != 0) {
            state = State.RUNNING;
            stateTime = 0;
        } else if (state == State.RUNNING && velocity.x == 0) {
            state = State.STANDING;
            stateTime = 0;
        }

        // UPDATE Y
        if (keys.get(Keys.SPACE)) {
            state = State.JUMPING;
            if (jumpTimer < JUMP_TIMER) {
                jumpTimer += delta;
                velocity.y = JUMP_FORCE;
            }
        }

        if (state == State.JUMPING) {
            acceleration.y = GRAVITY;
        }

        // update
        velocity.add(acceleration.mul(delta));
        super.update(delta);

        // RAD COLLISION DETECTION
        if (position.y <= 1f) {
            position.y = 1f;
            velocity.y = 0;
            acceleration.y = 0;
            jumpTimer = 0;

            if (velocity.x == 0) {
                state = State.STANDING;
            } else {
                state = State.RUNNING;
            }
        }
    }

    /**
     * setup the animations for the player
     */
    private void setupAnimations() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("sprites/player/running/running.pack"));

        // right idle texture
        standRight = atlas.findRegion("1");

        // left idle texture
        standLeft = new TextureRegion(standRight);
        standLeft.flip(true, false);

        // right running animation
        TextureRegion[] runRightFrames = new TextureRegion[4];
        for (int i = 0; i < runRightFrames.length; i++) {
            runRightFrames[i] = atlas.findRegion(""+i);
        }
        runRightAnimation = new Animation(RUNNING_FRAME_DURATION, runRightFrames);

        TextureRegion[] runLeftFrames = new TextureRegion[4];
        for(int i = 0; i < runLeftFrames.length; i++) {
            runLeftFrames[i] = new TextureRegion(runRightFrames[i]);
            runLeftFrames[i].flip(true, false);
        }
        runLeftAnimation = new Animation(RUNNING_FRAME_DURATION, runLeftFrames);
    }

    /**
     * get the correct texture region object to render
     *
     * @return TextureRegion
     */
    public TextureRegion getTextureRegion() {
        if (state == State.RUNNING) {
            if (velocity.x > 0) {
                return runRightAnimation.getKeyFrame(stateTime, true);
            } else if (velocity.x < 0) {
                return runLeftAnimation.getKeyFrame(stateTime, true);
            }
        }

        return facingLeft ? standLeft : standRight;
    }

    /**
     * INPUT PROCESSOR METHODS BELOW
     */

    HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();

    @Override
    public boolean keyDown(int i) {
        keys.put(i, true);
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        keys.put(i, false);
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
