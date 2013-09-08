import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Base block entity for map tile objects
 */
public class Block extends Entity {
    private Texture texture;

    public Block(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void setTexture(String ref) {
        texture = new Texture(Gdx.files.internal(ref));
    }

    public void drawDebug(ShapeRenderer debug) {
        debug.setColor(debugColor);
        debug.begin(ShapeRenderer.ShapeType.FilledRectangle);
        debug.filledRect(getX(), getY(), getWidth(), getHeight());
        debug.end();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY());
    }
}
