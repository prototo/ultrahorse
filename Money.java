import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: greg
 * Date: 05/09/13
 * Time: 22:16
 * To change this template use File | Settings | File Templates.
 */
public class Money extends Item {
    private final int MAX_VALUE = 100;
    private int value;

    public Money(float x, float y) {
        super(x, y, 16, 16);

        Random r = new Random();
        this.value = r.nextInt(MAX_VALUE);
        this.EXPIRE = false;
        this.BOUNCEY = true;
//        randomVelocity();

        debugColor = Color.YELLOW;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public void onCollide(Player p) {
        p.attributes.gainMoney(this);
        markForRemoval();
    }
}
