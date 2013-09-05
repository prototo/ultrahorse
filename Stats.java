/**
 * Created with IntelliJ IDEA.
 * User: greg
 * Date: 05/09/13
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class Stats {
    private int money;

    public Stats() {
        // read in from however the fuck you save games
        money = 0;
    }

    /**
     * return the amount of money the player has
     * @return
     */
    public int getMoney() {
        return money;
    }

    /**
     * increment the player's money bank by the value of the money item object
     * @param money
     * @return
     */
    public int gainMoney(Money money) {
        this.money += money.getValue();
        money.markForRemoval();
        return getMoney();
    }

    /**
     * decrement the player's monetary value (buying shit or something)
     *
     * @param loss
     * @return the new value or money
     */
    public int loseMoney(int loss) {
        this.money -= loss;
        if (this.money < 0) {
            this.money = 0;
        }
        return getMoney();
    }
}
