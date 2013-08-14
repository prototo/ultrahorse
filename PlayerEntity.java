import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Created by greg on 8/13/13.
 */
public class PlayerEntity extends Entity {

    private double jumpSpeed = 20;
    private int a = 0;
    private int b = 0;

    private ArrayList<Sprite> standing;

    public PlayerEntity(double x, double y) {
        super("sprites/horse_0.png", x, y);

        setupAnimations();
    }

    private void setupAnimations() {
        standing = new ArrayList<Sprite>();
//        standing.add(SpriteStore.get().getSprite("sprites/player/standing/0.png"));
        standing.add(SpriteStore.get().getSprite("sprites/player/standing/1.png"));
        standing.add(SpriteStore.get().getSprite("sprites/player/standing/2.png"));
//        standing.add(SpriteStore.get().getSprite("sprites/player/standing/1.png"));
    }

    @Override
    public void draw(Graphics g) {
        if (b++ > 2) {
            a++;
            if (a == standing.size()) {
                a = 0;
            }
            b = 0;
        }
        sprite = standing.get(a);
        super.draw(g);
    }

    @Override
    public void accelerate() {
        if (Game.keyUp && grounded) {
            dy -= jumpSpeed;
            grounded = false;
        }
        if (Game.keyDown) {
        }

        if (Game.keyRight) {
            dx += maxx * ax;
        }
        if (Game.keyLeft) {
            dx -= maxx * ax;
        }
    }
}
