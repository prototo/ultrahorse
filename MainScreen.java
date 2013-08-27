import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

public class MainScreen extends Screen implements InputProcessor {
    private float offset;

    GDXGame game;
    PlayerEntity horse;
    BitmapFont font;
    double fontAlpha = 0.99, fontAlphaModifier = -0.005;

    private final CharSequence START_TEXT = "Press Space";

    public MainScreen(GDXGame game) {
        super("sprites/stars.png");
        this.game = game;

        offset = 10000;
        BG_REPEAT_Y = offset / background.getHeight();

        horse = new PlayerEntity(new Vector2(CAMWIDTH/2, CAMHEIGHT/2));

        font = new BitmapFont(Gdx.files.internal("resource/calibri.fnt"), Gdx.files.internal("resource/calibri.png"), false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void updateCamera() {
        cam.position.x = Horse.width / 2;
        cam.position.y = Horse.height / 2 + offset;
        cam.update();
    }

    @Override
    protected void renderSprites() {
        batch.draw(horse.getTextureRegion(), 50, 0, 100, 200);
        font.setColor(1, 1, 1, (float) fontAlpha);
        font.draw(batch, START_TEXT, CAMWIDTH - 100 - font.getBounds(START_TEXT).width, 100);

        fontAlpha += fontAlphaModifier;
        if (fontAlpha < 0.5 || fontAlpha == 1) {
            fontAlphaModifier *= -1;
        }
    }

    @Override
    protected void update(float delta) {
        if (offset >= 1) {
            offset *= 0.99;
        }

    }

    @Override
    public boolean keyDown(int i) {
        System.out.println(i);
        if (offset <= 50 && i == Input.Keys.SPACE) {
            game.startGame();
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        System.out.println(i);
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        System.out.println(c);
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
