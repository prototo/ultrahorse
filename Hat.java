import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Hat {
    Vector2 position;
    Texture texture;
    Sprite sprite;
    Player player;
    float rotation;

    public Hat(Player player, String ref) {
        this.player = player;
        this.texture = new Texture(Gdx.files.internal("sprites/hats/" + ref + ".png"));
        this.sprite = new Sprite(this.texture);
        this.rotation = -30;
        this.position = new Vector2(0, 0);
        update();
    }

    public void update() {
        if (player.facingLeft) {
            position.x = player.getRight() - sprite.getWidth() + 2;
        } else {
            position.x = player.getX() - 2;
        }
        position.y = player.getTop() - 2;
    }

    public void draw(SpriteBatch batch) {
        sprite.setRotation(player.facingLeft ? -25 : 25);
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    public void augment() {
        player.JUMP_TIMER *= 2;
    }

    public void deaugment() {
        player.JUMP_TIMER /= 2;
    }
}
