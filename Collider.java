import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Performs collisions on given object
 */
public class Collider {

    Map map;

    public Collider(Map map) {
        this.map = map;
    }

    public void entities(Player p, Entity e) {
        if (p.bounds.overlaps(e.bounds)) {
            e.onCollide(p);
        }
    }

    /**
     * get a list of blocks the entity maybe colliding with after movement
     *
     * @param e
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @return
     */
    private ArrayList<Block> getCollidableMapTiles(Entity e, float startX, float endX, float startY, float endY) {
        ArrayList<Block> collidable = new ArrayList<Block>();
        for (float x = startX; x <= endX; x += e.getWidth() / 2) {
            for (float y = startY; y <= endY; y += e.getHeight() / 3) {
                Block block = map.collidesWith(x, y);

                if (block != null) {
                    collidable.add(block);
                }
            }
        }
        return collidable;
    }

    /**
     * collide the given entity with relevant map tiles
     *
     * @param e
     * @param delta
     */
    public void withMap(Entity e, float delta) {
        Rectangle bounds = new Rectangle();
        Vector2 vel = e.velocity.cpy().mul(delta);
        ArrayList<Block> collidable;
        float startX, endX, startY, endY;

        // Check collisions on X axis
        bounds.set(e.bounds);

        bounds.x += vel.x;
        startY = bounds.getY();
        endY = bounds.getY() + e.getHeight();
        startX = vel.x > 0 ? bounds.getX() + bounds.getWidth() : bounds.getX();
        endX = startX;

        collidable = getCollidableMapTiles(e, startX, endX, startY, endY);
        for (Block block : collidable) {
            if (bounds.overlaps(block.bounds)) {
                e.onCollide(block, true, false);
                break;
            }
        }

        // Check collisions on Y axis
        bounds.set(e.bounds);

        bounds.y += vel.y;
        startX = bounds.getX();
        endX = bounds.getX() + e.getWidth();
        startY = vel.y > 0 ? bounds.getY() + bounds.getHeight() : bounds.getY();
        endY = startY;

        collidable = getCollidableMapTiles(e, startX, endX, startY, endY);
        for (Block block : collidable) {
            if (bounds.overlaps(block.bounds)) {
                e.onCollide(block, false, true);
                break;
            }
        }

        if (e.velocity.y != 0) {
            e.grounded = false;
        }
    }
}
