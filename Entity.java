import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Entity {
    private Sprite sprite;
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double moveSpeed;

    private Rectangle me = new Rectangle();
    private Rectangle him = new Rectangle();

    public Entity(String ref, double x, double y) {
        Random r = new Random();

        this.sprite = SpriteStore.get().getSprite(ref);
        this.moveSpeed = 5;
        this.x = x;
        this.y = y;
        this.dx = r.nextInt((int) moveSpeed);
        this.dy = 0;
    }

    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    public void move(ArrayList<Entity> entities) {
        if (x < 0) {
            dx = Math.abs(dx);
        }
        if (x + sprite.getWidth() > Game.width) {
            dx = Math.abs(dx);
            dx *= -1;
        }

        for (Entity horse : entities) {
            if (!this.equals(horse) && collidesWith(horse)) {
                dx *= -1;
            }
        }

        this.y += this.dy;
        this.x += this.dx;
    }

    public boolean collidesWith(Entity other) {
        me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());

        return me.intersects(him);
    }
}
