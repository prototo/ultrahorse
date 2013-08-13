import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class Entity {
    protected Sprite sprite;

    protected double x;
    protected double y;
    protected double dx = 0;
    protected double dy = 0;
    protected double ax = 0.2;
    protected double ay = 0.2;
    protected double maxx = 5;
    protected double maxy = 5;

    protected boolean grounded = false;

    private int facing;

    private Rectangle me = new Rectangle();
    private Rectangle him = new Rectangle();

    public Entity(String ref, double x, double y) {
        Random r = new Random();

        this.sprite = SpriteStore.get().getSprite(ref);
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    }

    public boolean collidesWith(Entity other) {
        me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(), other.sprite.getHeight());

        return me.intersects(him);
    }

    public abstract void accelerate();

    public void move() {
        this.accelerate();

        if (dx > maxx) dx = maxx;
        if (dx < maxx * -1) dx = maxx * -1;

        if (dy > maxy) dy = maxy;
//        if (dy < maxy * -1) dy = maxy * -1;

        dx *= Game.FRICTION;
        dy += Game.GRAVITY;
        dy *= Game.DRAG;

        if (x + dx > Game.width - sprite.getWidth()) {
            x = Game.width - sprite.getWidth();
            dx = 0;
        } else if (x + dx < 0) {
            x = 0;
            dx = 0;
        } else {
            x += dx;
        }

        if (y + dy > Game.height - sprite.getHeight()) {
            y = Game.height - sprite.getHeight();
            grounded = true;
        } else {
            y += dy;
        }
    }
}
