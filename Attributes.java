import java.util.ArrayList;
import java.util.HashMap;

public class Attributes {
    /**
     * enum for all the types of attributes an entity can have
     */
    enum Types {
        HP, MP,
        ATTACK, DEFENSE,

        SPEED, JUMPPOWER, JUMPTIME,

        MONEY
    };

    private HashMap<Types, Float> base;
    private HashMap<Types, Float> real;

    // arraylist of equipment shared with entity
    protected ArrayList<Equipment> equipment;

    public Attributes(ArrayList<Equipment> equipment) {
        this.equipment = equipment;

        base = new HashMap<Types, Float>();
        base.put(Types.MONEY, 0f);
    }

    /**
     * Calculate the real attribute values taking account of buffs from equipment
     */
    protected void calculate() {
        float v;
        Types t;

        real = new HashMap<Types, Float>(base);

        for (Equipment e : equipment) {
            t = e.getBuffType();
            v = get(t);
            v += (getBase(t) * e.getBuff());
            real.put(t, v);
        }
    }

    /**
     * Get any REAL attribute (buffed)
     * @param type
     * @return the floatz
     */
    public float get(Types type) {
        if (real.containsKey(type)) {
            return real.get(type);
        }

        return 0;
    }

    /**
     * Get any BASE attribute
     * @param type
     * @return
     */
    public float getBase(Types type) {
        if (base.containsKey(type)) {
            return base.get(type);
        }

        return 0;
    }

    /**
     * Guess
     *
     * @param type
     * @param v
     */
    public void put(Types type, float v) {
        base.put(type, v);
        calculate();
    }

    /**
     * increment the player's money bank by the value of the money item object
     * @param money
     * @return
     */
    public int gainMoney(Money money) {
        int val = (int) get(Types.MONEY);
        base.put(Types.MONEY, (float) val + money.getValue());

        money.markForRemoval();
        return (int) get(Types.MONEY);
    }

    /**
     * decrement the player's monetary value (buying shit or something)
     * @param loss
     * @return the new value or money
     */
    public int loseMoney(int loss) {
        float val = get(Types.MONEY);
        base.put(Types.MONEY, val - loss);

        return (int) get(Types.MONEY);
    }
}
