import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Map class bitches
 */
public class Map implements Drawable {

    public final float tileSize = 32;
    Block[][] tiles;

    ArrayList<Item> items;

    HashMap<String, Texture> textures = new HashMap<String, Texture>();

    public Map(ArrayList<Item> items) {
        tiles = new Block[50][50];
        this.items = items;
        setupTextures();
        populate();
    }

    public float getWidth() {
        return tiles.length * tileSize;
    }

    public float getHeight() {
        return tiles[0].length * tileSize;
    }

    /**
     * Setup the textures for map tiles
     */
    private void setupTextures() {

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

    private void populate() {
        int blocks[][] = new int[tiles.length][tiles[0].length];
        Random r = new Random();
        int rchance = 25; int pchance = 2;

        // borders
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                blocks[x][y] = should(x, y) ? 1 : 0;
            }
        }

        // land masses
        for (int x = 1; x < tiles.length - 1; x++) {
            for (int y = 1; y < tiles[0].length - 1; y++) {
                if (blocks[x][y] == 0) {
                    blocks[x][y] = blocks[x][y] | (r.nextInt(rchance) == 1 ? 1 : 0);
                    blocks[x][y] = blocks[x][y] | (blocks[x][y+1] & (r.nextInt(pchance) == 1 ? 1 : 0));
                    blocks[x][y] = blocks[x][y] | (blocks[x][y-1] & (r.nextInt(pchance) == 1 ? 1 : 0));
                    blocks[x][y] = blocks[x][y] | (blocks[x+1][y] & (r.nextInt(pchance) == 1 ? 1 : 0));
                    blocks[x][y] = blocks[x][y] | (blocks[x-1][y] & (r.nextInt(pchance) == 1 ? 1 : 0));
                }
            }
        }

        // fill tiles
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                if (blocks[x][y] == 1) {
                    Block block = new Block(x * tileSize, y * tileSize, tileSize, tileSize);
                    String ref;
                    int above, below, left, right;

                    try {
                        above = blocks[x][y+1];
                        ref = above == 1 ? "dirt" : "ground";
                        above = ref == "ground" ? 1 : above;
                    } catch (Exception e) {
                        above = 1;
                        ref = "dirt";
                    }
                    try {
                        below = blocks[x][y-1];
                    } catch (Exception e) {
                        below = 1;
                    }
                    try {
                        left = blocks[x-1][y];
                    } catch (Exception e) {
                        left = 1;
                    }
                    try {
                        right = blocks[x+1][y];
                    } catch (Exception e) {
                        right = 1;
                    }

                    ref = "sprites/env/" + ref + above + right + below + left + ".png";

                    // add texture to the hashmap if it's not set yet
                    if (!textures.containsKey(ref)) {
                        textures.put(ref, new Texture(Gdx.files.internal(ref)));
                    }

                    block.setTexture(textures.get(ref));
                    tiles[x][y] = block;
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
