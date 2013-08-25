import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Entity {
    protected double x;
    protected double y;

    public enum Facing {
        LEFT, RIGHT
    }

    protected Texture texture;
    float speed = 1f;
    float size = 1f;

    Vector2 position = new Vector2();
    Vector2 acceleration = new Vector2();
    Vector2 velocity = new Vector2();

    Rectangle bounds = new Rectangle();
    Facing facing = Facing.RIGHT;

    public Entity(String ref, Vector2 position) {
        this.position = position;

        this.texture = new Texture(ref);
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.bounds.setSize((int) size, (int) size);
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void update(float delta) {
        Vector2 newPos = position.cpy().add(velocity.cpy().mul(delta));

        int x = (int) Math.floor(newPos.x);
        int y = (int) Math.floor(newPos.y);
        int tile;

        if (velocity.x > 0) {
            tile = Map.get().getTile(x + 1, y);
            if (tile == 1) {
                newPos.x = (float) x;
            }
        } else {
            tile = Map.get().getTile(x, y);
            if (tile == 1) {
                newPos.x = (float) x + 1;
            }
        }

        /*
        if (velocity.y > 0) {
            tile = Map.get().getTile(x, y + 1);
            if (tile == 1) {
                newPos.y = (float) y;
            }
        } else {
            tile = Map.get().getTile(x, y);
            if (tile == 1) {
                newPos.y = (float) y + 1;
            }
        }
        */

        position = newPos;
    }
}
