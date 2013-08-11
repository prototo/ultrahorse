import java.awt.*;
import java.util.Random;

/**
 * Created by greg on 11/08/13.
 */
public class Entity {
    private Sprite sprite;
    private double x;
    private double y;
    private double moveSpeed;

    private Random r;

    public Entity(String ref, double x, double y) {
        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;
        this.moveSpeed = 5;

        r = new Random();
    }

    public void draw(Graphics g) {
        int s = r.nextInt(200);

        sprite.draw(g, (int) x, (int) y, s, s);
    }

    public void move() {
        this.x = r.nextInt(Game.width);
        this.y = r.nextInt(Game.height);
    }
}
