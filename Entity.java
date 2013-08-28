import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public abstract class Entity {
    protected double x;
    protected double y;

    protected final float RUNNING_FRAME_DURATION = 0.25f;
    protected float stateTime = 0;

    public enum Facing {
        LEFT, RIGHT
    }

    protected Texture texture;
    float speed = 1f;

    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();

    Rectangle bounds = new Rectangle();
    Facing facing = Facing.RIGHT;
    boolean grounded = false;

    public Entity(String ref, Vector2 position) {
        this.position = position;

        this.texture = new Texture(ref);
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setRect();
    }

    protected void setSize() {
        TextureRegion texture = getTextureRegion();
        bounds.setWidth(texture.getRegionWidth() / Horse.baseUnitSize);
        bounds.setHeight(texture.getRegionHeight() / Horse.baseUnitSize);
    }

    protected abstract TextureRegion getTextureRegion();

    protected void setRect() {
        bounds.x = position.x;
        bounds.y = position.y;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void update(float delta) {
        checkCollisions(delta);
        position.add(velocity.cpy().mul(delta));
        setRect();
    }

    private void checkCollisions(float delta) {
        Vector2 vel = velocity.cpy().mul(delta);
        int startX, endX, startY, endY;

        // new rectangle to simulate movement on
        Rectangle bounds = new Rectangle();
        bounds.set(this.bounds);

        // Check collisions on x axis
        startY = (int) bounds.getY();
        endY = (int) (bounds.getY() + bounds.getHeight());

        if (vel.x < 0) {
            startX = endX = (int) Math.floor(bounds.getX() + vel.x);
        } else {
            startX = endX = (int) Math.floor(bounds.getX() + vel.x + bounds.getWidth());
        }

        // move on x
        bounds.x += vel.x;

        ArrayList<BlockEntity> collidable = getCollidable(startX, endX, startY, endY);
        for (BlockEntity block : collidable) {
            if (bounds.overlaps(block.bounds)) {
                velocity.x = 0;
                break;
            }
        }

        // reset x
        bounds.x = this.bounds.x;

        // Check collisions on Y axis
        startX = (int) bounds.getX();
        endX = (int) (bounds.getX() + bounds.getWidth());

        if (vel.y < 0) {
            startY = endY = (int) Math.floor(bounds.getY() + vel.y);
        } else {
            startY = endY = (int) Math.floor(bounds.getY() + vel.y + bounds.getHeight());
        }

        // move on y
        bounds.y += vel.y;

        collidable = getCollidable(startX, endX, startY, endY);
        for (BlockEntity block : collidable) {
            if (bounds.overlaps(block.bounds)) {
                if (velocity.y < 0) {
                    grounded = true;
                    position.y = block.bounds.y + block.bounds.getHeight();
                }

                velocity.y = 0;
                break;
            }
        }

        // WE DONE HERE.
    }

    private ArrayList<BlockEntity> getCollidable(int sx, int ex, int sy, int ey) {
        ArrayList<BlockEntity> collidable = new ArrayList<BlockEntity>();

        for (int x = sx; x <= ex; x++) {
            for (int y = sy; y <= ey; y++) {
                BlockEntity block = Map.get().getBlock(x, y);
                if (block != null) {
                    collidable.add(block);
                }
            }
        }

        return collidable;
    }
}
