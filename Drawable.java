import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

interface Drawable {
    public void draw(SpriteBatch batch);
    public void drawDebug(ShapeRenderer debug);
}
