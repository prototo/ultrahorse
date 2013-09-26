import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: greg
 * Date: 26/09/13
 * Time: 23:06
 * To change this template use File | Settings | File Templates.
 */
public class Equipment extends Item {
    enum Types {
        HAT,
        CAPE,
        WAND,
        BOOTS,
        GLOVES
    };

    protected Types type;
    protected Attributes.Types buffType;
    protected float buff;

    public Equipment(float x, float y, float width, float height) {
        super(x, y, width, height);

        // type = Types.HAT;
        buffType = Attributes.Types.JUMPPOWER;
        buff = 0.05f;   // 5%
    }

    public Attributes.Types getBuffType() {
        return buffType;
    }

    public float getBuff() {
        return buff;
    }

    public Types getType() {
        return type;
    }

    /**
     * Perform some operation on the Entity that caused damage to the
     * wearer of this item
     *
     * @param e opposing entity
     */
    public void onReceiveDamage(Entity e) {
        // do stuff, like damage them back
    }

    @Override
    public void onCollide(Player p) {
        p.equip(this);
    }
}
