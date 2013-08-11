import java.awt.Graphics;
import java.util.Random;

/**
 * Created by greg on 11/08/13.
 */
public class Entity {
    private Sprite sprite;
    private double x;
    private double y;

    private Random r;

    public Entity(String ref, double x, double y) {
        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;

        r = new Random();
    }

    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    public void move() {
        if (r.nextInt(100) > 95) {
            this.x = r.nextInt(Game.width - sprite.getWidth());
            this.y = r.nextInt(Game.height - sprite.getHeight());
        }
    }
}
