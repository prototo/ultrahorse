import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by greg on 01/09/13.
 */
public class Controller implements InputProcessor {
    Entity entity;

    public Controller(Entity entity) {
        this.entity = entity;

        this.entity.setControlled(true);
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.LEFT) {
            entity.movement.put("runLeft", true);
        } else if (i == Input.Keys.RIGHT) {
            entity.movement.put("runRight", true);
        }

        if (i == Input.Keys.SPACE) {
            entity.movement.put("jump", true);
        }

        return false;
    }

    @Override
    public boolean keyUp(int i) {
        if (i == Input.Keys.LEFT) {
            entity.movement.put("runLeft", false);
        } else if (i == Input.Keys.RIGHT) {
            entity.movement.put("runRight", false);
        }

        if (i == Input.Keys.SPACE) {
            entity.movement.put("jump", false);
        }

        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
