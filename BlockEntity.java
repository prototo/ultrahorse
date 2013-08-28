import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by greg on 24/08/13.
 */
public class BlockEntity extends Entity {

    float size = 1f;

    public BlockEntity(Vector2 position) {
        super("sprites/block.png", position);

        bounds.setHeight(size);
        bounds.setWidth(size);
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return null;
    }
}
