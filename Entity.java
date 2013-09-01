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
    static final float ANIMATION_FRAME_DURATION = 0.25f;

    Vector2 position;
    Vector2 velocity;
    Rectangle bounds;
    float speed = 300f;

    boolean grounded = false;
    boolean facingLeft = false;
    State state = State.IDLE;
    float stateTime = 0;

    Animation idleRight, idleLeft, runRight, runLeft;

    Random r = new Random();
    Color debugColor = new Color(0.25f + r.nextFloat(), 0.25f + r.nextFloat(), 0.25f + r.nextFloat(), 1);

    public Entity(float x, float y, float width, float height) {
        position = new Vector2(x, y);
        velocity = new Vector2(300, 0);
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

    public void act(float delta) {
        stateTime += delta;

        velocity.x = 0;
        if (movement.get("runLeft")) {
            velocity.x = -speed;
        }
        if (movement.get("runRight")) {
            velocity.x = speed;
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
    }

    protected void checkCollisions(float delta, Map map, ArrayList<Entity> entities) {
        int direction;
        Vector2 proposed;
        float startX, endX, startY, endY;
        Entity tile;

        /**
         * Do all the X checks
         */
        if (velocity.x != 0) {
            direction = velocity.x > 0 ? 1 : -1;
            proposed = position.cpy();
            proposed.add(new Vector2(velocity.x, 0).mul(delta));
            startX = direction > 0 ? getRight() : getX();
            endX = direction > 0 ? proposed.x + getWidth() : proposed.x;
            startY = proposed.y;
            endY = proposed.y + getHeight();

            do {
                do {
                    tile = map.collidesWith(startX, startY);
                    if (tile != null) {
                        if (direction > 0) {
                            position.x = tile.getX() - getWidth() - 1;
                        } else {
                            position.x = tile.getRight() + 1;
                        }
                        velocity.x = 0;
                        break;
                    }
                    startY += map.tileSize;
                } while (startY < endY);
                startX += map.tileSize * direction;
            } while (direction > 0 ? startX < endX : startX > endX);
        }

        /**
         * Do all the Y checks
         */
        if (velocity.y != 0) {
            direction = velocity.y > 0 ? 1 : -1;
            proposed = position.cpy();
            proposed.add(new Vector2(0, velocity.y).mul(delta));
            startX = proposed.x;
            endX = proposed.x + getWidth();
            startY = direction > 0 ? getTop() : getY();
            endY = direction > 0 ? proposed.y + getHeight() : proposed.y;

            do {
                do {
                    tile = map.collidesWith(startX, startY);
                    if (tile != null) {
                        if (direction > 0) {
                            position.y = tile.getY() - getHeight() - 1;
                        } else {
                            position.y = tile.getTop() + 1;
                            grounded = true;
                        }
                        velocity.y = 0;
                        break;
                    }
                    startX += map.tileSize;
                } while (startX < endX);
                startY += map.tileSize * direction;
            } while (direction > 0 ? startY < endY : startY > endY);
        }

        // TODO: COLLIDE WITH OTHER ENTITIES
    }

    protected Animation setupAnimation(TextureAtlas atlas, String refs[], boolean flip) {
        Animation animation;

        TextureRegion frames[] = new TextureRegion[refs.length];
        for (int i = 0; i < refs.length; i++) {
            frames[i] = atlas.findRegion(refs[i]);
            if (flip) {
                frames[i].flip(true, false);
            }
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
        bounds.setWidth(region.getRegionWidth());
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void drawDebug(ShapeRenderer debug) {
        debug.setColor(debugColor);
        debug.begin(ShapeRenderer.ShapeType.Rectangle);
        debug.rect(getX(), getY(), getWidth(), getHeight());
        debug.end();
    }

    public void jump() {
        grounded = false;
        velocity.y = 500f;
    }
}
