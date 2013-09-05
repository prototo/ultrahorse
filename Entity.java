import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Entity implements Drawable {
    private enum State { IDLE, RUNNING };
    public HashMap<String, Boolean> movement = new HashMap<String, Boolean>();

    static final float GRAVITY = -15f;
    float ANIMATION_FRAME_DURATION = 0.2f;
    float JUMP_TIMER = 0.3f;

    protected boolean CONTROLLED = false;

    Vector2 position;
    Vector2 velocity;
    Rectangle bounds;
    float speed = 300f;
    float jumpSpeed = 400f;
    float jumpTimer = 0;

    boolean grounded = false;
    boolean facingLeft = false;
    State state = State.IDLE;
    float stateTime = 0;

    Animation idleRight, idleLeft, runRight, runLeft;

    Random r = new Random();
    Color debugColor = new Color(0.05f + r.nextFloat(), 0.05f + r.nextFloat(), 0.05f + r.nextFloat(), 1);

    public Entity(float x, float y, float width, float height) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle();
        bounds.set(position.x, position.y, width, height);

        // initialise movement hashmap
        movement.put("runLeft", false);
        movement.put("runRight", false);
        movement.put("jump", false);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getTop() {
        return getY() + getHeight();
    }

    public float getRight() {
        return getX() + getWidth();
    }

    public float getWidth() {
        return bounds.getWidth();
    }

    public float getHeight() {
        return bounds.getHeight();
    }

    public float getCenterX() {
        return getX() + getWidth() / 2;
    }

    public float getCenterY() {
        return getY() + getHeight() / 2;
    }

    public Vector2 getCenter() {
        return new Vector2(getCenterX(), getCenterY());
    }

    public void act(float delta) {
        stateTime += delta;

        if (!grounded) {
            jumpTimer += delta;

            if (!movement.get("jump")) {
                stopJump();
            }
        }

        if (CONTROLLED) {
            velocity.x = 0;
        }

        if (movement.get("runLeft")) {
            velocity.x = -speed;
        }
        if (movement.get("runRight")) {
            velocity.x = speed;
        }
        if (movement.get("jump")) {
            jump();
        }

        if (velocity.x != 0) {
            state = State.RUNNING;
            facingLeft = velocity.x < 0;
        } else {
            state = State.IDLE;
        }

        velocity.y += GRAVITY;
    }

    public void update(float delta) {
        position.add(velocity.cpy().mul(delta));
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }

    protected Animation setupAnimation(TextureAtlas atlas, String refs[], boolean flip) {
        Animation animation;

        TextureRegion frames[] = new TextureRegion[refs.length];
        for (int i = 0; i < refs.length; i++) {
            frames[i] = new TextureRegion(atlas.findRegion(refs[i]));
            frames[i].flip(flip, false);
        }
        animation = new Animation(ANIMATION_FRAME_DURATION, frames);

        return animation;
    }

    protected TextureRegion getTextureRegion() {
        Animation animation;

        if (state == State.RUNNING) {
            animation = facingLeft ? runLeft : runRight;
        } else {
            animation = facingLeft ? idleLeft : idleRight;
        }

        return animation.getKeyFrame(stateTime, true);
    }

    public void draw(SpriteBatch batch) {
        TextureRegion region = getTextureRegion();
        float halfWidth = getWidth() / 2;
        batch.draw(region, getCenterX() - halfWidth, getY());
    }

    @Override
    public void drawDebug(ShapeRenderer debug) {
        debug.setColor(debugColor);
        debug.begin(ShapeRenderer.ShapeType.Rectangle);
        debug.rect(getX(), getY(), getWidth(), getHeight());
        debug.end();
    }

    public void jump() {
        if (jumpTimer < JUMP_TIMER) {
            velocity.y = jumpSpeed;
            grounded = false;
        }
    }

    public void stopJump() {
        jumpTimer = JUMP_TIMER;
    }

    /**
     * Collider methods
     */
    public void onCollide(Object o) {

    }

    public void onCollide(Entity e) {

    }

    public void onCollide(Player p) {

    }

    /**
     * What to do when colliding with a map tile block
     *
     * @param b
     * @param onX
     * @param onY
     */
    public void onCollide(Block b, boolean onX, boolean onY) {
        if (onX) {
            if (velocity.x > 0) {
                position.x = b.getX() - getWidth() - 1;
            } else {
                position.x = b.getRight() + 1;
            }

            velocity.x = 0;
        }

        if (onY) {
            if (velocity.y < 0) {
                grounded = true;
                jumpTimer = 0;
                position.y = b.getTop();
            } else {
                // hit the ceiling, stop jumping
                stopJump();
                position.y = b.getY() - getHeight() - 1;
            }

            velocity.y = 0;
        }
    }

    public void setControlled(boolean controlled) {
        this.CONTROLLED = controlled;
    }
}
