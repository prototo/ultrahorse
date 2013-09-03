import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Map class bitches
 */
public class Map implements Drawable {

    public final float tileSize = 32;
    Block[][] tiles;

    public Map() {
        tiles = new Block[50][50];
        populate();
    }

    private void populate() {
        Random r = new Random();
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                if (r.nextInt(20) == 1 || x == 0 || y == 0 || x == tiles.length - 1|| y == tiles.length - 1) {
                    tiles[x][y] = (new Block(x * tileSize, y * tileSize, tileSize, tileSize));
                }
            }
        }
    }

    public Block getTile(int x, int y) {
        try {
            return tiles[x][y];
        } catch (Exception e) {
            return null;
        }
    }

    public Block collidesWith(float x, float y) {
        int tileX = (int) Math.floor(x / tileSize);
        int tileY = (int) Math.floor(y / tileSize);
        return getTile(tileX, tileY);
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void drawDebug(ShapeRenderer debug) {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                if (getTile(x, y) != null) {
                    getTile(x, y).drawDebug(debug);
                }
            }
        }
    }
}
