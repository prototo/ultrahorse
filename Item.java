import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

/**
 * Base class for static entities such as pickups or projectiles
 */
public class Item extends Entity {
    protected boolean BOUNCEY;
    protected final float DAMP = 0.7f;
    protected final float FRICTION = 0.95f;
    protected boolean EXPIRE = false;

    public boolean remove = false;

    public Item(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    // on collide with map tile block
    public void onCollide(Block b, boolean onX, boolean onY) {
        if (onX) {
            if (velocity.x > 0) {
                position.x = b.getX() - getWidth() - 1;
            } else {
                position.x = b.getRight() + 1;
            }

            if (BOUNCEY && velocity.x != 0) {
                velocity.x *= -DAMP;
            } else {
                velocity.x = 0;
            }
        }

        if (onY) {
            if (velocity.y < 0) {
                position.y = b.getTop();
            } else {
                position.y = b.getY() - getHeight() - 1;
            }

            if (BOUNCEY && Math.abs(velocity.y) > 50) {
                velocity.y *= -DAMP;
            } else {
                velocity.y = 0;
                grounded = true;
            }
        }
    }

    public Item setBouncey(boolean bouncey) {
        this.BOUNCEY = bouncey;
        return this;
    }

    public Item setExpire(boolean expire) {
        this.EXPIRE = expire;
        return this;
    }

    /**
     * Give this item a random velocity based on it's speed stat
     */
    public Item randomVelocity() {
        Random r = new Random();
        velocity.x = (r.nextFloat() * speed * 2) - speed;   // left or right
        velocity.y = r.nextFloat() * speed; // only up
        return this;
    }

    @Override
    public void act(float delta) {
        if (EXPIRE) {
            if (velocity.x == 0 && grounded) {
                markForRemoval();
            }
        }

        super.act(delta);
        velocity.x *= FRICTION;

        if (Math.abs(velocity.x * delta) < 0.1) {
            velocity.x = 0;
        }

        if (EXPIRE && velocity.x == 0 && grounded) {
            markForRemoval();
        }
    }

    /**
     * Mark this item for removal in the next game step
     */
    protected void markForRemoval() {
        this.remove = true;
    }

    public void drawDebug(ShapeRenderer debug) {
        debug.begin(ShapeRenderer.ShapeType.FilledCircle);
        debug.setColor(debugColor);
        debug.filledCircle(getCenterX(), getCenterY(), getWidth() / 2);
        debug.end();

        debug.begin(ShapeRenderer.ShapeType.Circle);
        debug.setColor(Color.BLACK);
        debug.circle(getCenterX(), getCenterY(), getWidth() / 2);
        debug.end();
    }
}
