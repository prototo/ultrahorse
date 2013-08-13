/**
 * Created by greg on 8/13/13.
 */
public class PlayerEntity extends Entity {

    private double jumpSpeed = 20;

    public PlayerEntity(double x, double y) {
        super("sprites/horse_0.png", x, y);
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
