import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * The Map loader and renderer class
 */
public class Map {
    private static Map map = new Map();

    private int[][] tiles;
    private BlockEntity[][] blocks;
    public int tilesX, tilesY;

    public Map() {
        /**
         * TODO: MAP GENERATOR AND MAP FILE READER
         */

        tiles = new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        };

        tilesY = tiles.length;
        tilesX = tiles[0].length;
        blocks = getMap();
    }

    public static Map get() {
        return map;
    }

    public float getPixelWidth(float unitSize) {
        return tilesX * unitSize;
    }

    public float getPixelHeight(float unitSize) {
        return tilesY * unitSize;
    }

    /**
     * Draw all the tiles in the loaded map
     *
     * @param batch SpriteBatch for the drawing        bounds.setWidth(1);

     * @param unitSize pixels per unit
     */
    ShapeRenderer sr = new ShapeRenderer();
    public void render(SpriteBatch batch, float unitSize) {
        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                BlockEntity block = getBlock(x, y);

                if (block != null) {
                    float bx = block.position.x * unitSize;
                    float by = block.position.y * unitSize;
                    float bw = block.bounds.width * unitSize;
                    float bh = block.bounds.height * unitSize;
                    batch.draw(block.getTexture(), bx, by, bw, bh);
                }
            }
        }
    }

    /**
     * Get the value of tile in the loaded map file
     *
     * @param x
     * @param y
     * @return int representation of the map tile
     */
    public int getTile(int x, int y) {
        try {
            return tiles[y][x];
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Build and return an array of BlockEntitys that represent the loaded map
     *
     * @return BlockEntity array
     */
    public BlockEntity[][] getMap() {
        BlockEntity[][] blocks = new BlockEntity[tilesX][tilesY];

        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                if (getTile(x, y) == 1) {
                    BlockEntity block = new BlockEntity(new Vector2(x, y));
                    blocks[x][y] = block;
                }
            }
        }

        return blocks;
    }

    /**
     * Get a single BlockEntity from the generate map array
     *
     * @param x
     * @param y
     * @return The BlockEntity
     */
    public BlockEntity getBlock(int x, int y) {
        try {
            return blocks[x][y];
        } catch (Exception e) {
            return null;
        }
    }
}
