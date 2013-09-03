import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Base block entity for map tile objects
 */
public class Block extends Entity {
    public Block(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void drawDebug(ShapeRenderer debug) {
        debug.setColor(debugColor);
        debug.begin(ShapeRenderer.ShapeType.FilledRectangle);
        debug.filledRect(getX(), getY(), getWidth(), getHeight());
        debug.end();
    }
}
