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

    public float getWidth() {
        return tiles.length * tileSize;
    }

    public float getHeight() {
        return tiles[0].length * tileSize;
    }

    // amazing procedural generation
    private boolean should(int x, int y) {
        boolean should = false;

        // map borders
        should |= x == 0;
        should |= y == 0;
        should |= x == tiles.length - 1;
        should |= y == tiles.length - 1;

        return should;
    }

    private boolean shouldSecond(int x, int y) {
        boolean should = false;
        Random r = new Random();
        int rchance = 30; int pchance = 4;

        should |= r.nextInt(rchance) == 1;

        for (int nx = x - 1; nx <= x + 1; nx++) {
            for (int ny = y - 1; ny <= y + 1; ny++) {
                should |= getTile(nx, ny) != null && r.nextInt(pchance) == 1;
            }
        }

        return should;
    }

    private void populate() {
        boolean runs = true;

        for (int x = tiles.length - 1; x >= 0; x--) {
            for (int y = tiles[0].length - 1; y >= 0; y--) {
                if (runs ? should(x, y) : getTile(x, y) != null ? true : shouldSecond(x, y)) {
                    Block block = (new Block(x * tileSize, y * tileSize, tileSize, tileSize));
                    if (getTile(x, y+1) == null) {
                        block.setTexture("sprites/env/ground.png");
                    } else {
                        block.setTexture("sprites/env/dirt.png");
                    }
                    tiles[x][y] = block;
                }

                if (runs && x == 0 && y == 0) {
                    runs = false;
                    x = tiles.length - 1;
                    y = tiles[0].length - 1;
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
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles.length; y++) {
                if (getTile(x, y) != null) {
                    getTile(x, y).draw(batch);
                }
            }
        }
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
