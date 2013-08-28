import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by greg on 24/08/13.
 */
public class BlockEntity extends Entity {

    public BlockEntity(Vector2 position) {
        super("sprites/block.png", position);

        bounds.setWidth(1f);
        bounds.setHeight(1f);
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return null;
    }
}
