import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    Stats stats;
    Hat hat;

    public Player(Stats stats) {
        super(64, 400, 16, 48);

        this.debugColor = new Color(0.5f, 0.3f, 0.8f, 1);
        this.stats = stats;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("sprites/player.pack"));
        idleLeft = setupAnimation(atlas, new String[]{"standing-0"}, true);
        idleRight = setupAnimation(atlas, new String[]{"standing-0"}, false);
        runLeft = setupAnimation(atlas, new String[]{"running-0", "standing-0", "running-1", "standing-0"}, true);
        runRight = setupAnimation(atlas, new String[]{"running-0", "standing-0", "running-1", "standing-0"}, false);

        wearHat(new Hat(this, "fez"));
    }

    public void wearHat(Hat hat) {
        this.hat = hat;
        hat.augment();
    }

    public void removeHat() {
        hat.deaugment();
        hat = null;
    }

    @Override
    public void draw(SpriteBatch batch) {
        hat.update();
        hat.draw(batch);
        super.draw(batch);
    }
}
