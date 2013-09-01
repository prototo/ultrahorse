import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    public Player() {
        super(64, 400, 32, 64);

        this.debugColor = new Color(0.5f, 0.3f, 0.8f, 1);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("sprites/player.pack"));
        idleLeft = setupAnimation(atlas, new String[]{"standing-0"}, true);
        idleRight = setupAnimation(atlas, new String[]{"standing-0"}, false);
        runLeft = setupAnimation(atlas, new String[]{"running-0", "standing-0", "running-1", "standing-0"}, true);
        runRight = setupAnimation(atlas, new String[]{"running-0", "standing-0", "running-1", "standing-0"}, false);
    }
}
