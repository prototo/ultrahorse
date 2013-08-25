import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by greg on 14/08/13.
 */
public class Map {
    private static Map map = new Map();

    private int[][] tiles;

    public int dim = 16;

    private OrthographicCamera cam;
    private BlockEntity[][] blocks;
    private SpriteBatch batch;

    private float camWidth = 16f;
    private float camHeight = 16f;
    public float ppux = 32f;
    public float ppuy = 32f;
    private int width;
    private int height;

    public Map() {
        // read this in from a file or something
        tiles = new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        };

        batch = new SpriteBatch();
        cam = new OrthographicCamera(16, 16);
        cam.position.set(8, 8, 0);
        cam.update();
        blocks = getMap();
    }

    public static Map get() {
        return map;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        ppux = (float) width / camWidth;
        ppuy = (float) height / camHeight;
    }

    public void render() {
        batch.begin();
        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                BlockEntity block = getBlock(x, y);

                if (block != null) {
                    float bx = block.position.x * ppuy;
                    float by = block.position.y * ppuy;
                    float bw = block.bounds.width * ppux;
                    float bh = block.bounds.height * ppuy;

                    batch.draw(block.getTexture(), bx, by, bw, bh);
                }
            }
        }
        batch.end();
    }

    public int getTile(int x, int y) {
        if (x >= dim || y >= dim || x < 0 || y < 0) {
            return 1;
        }
        return tiles[y][x];
    }

    public BlockEntity[][] getMap() {
        BlockEntity[][] blocks = new BlockEntity[dim][dim];

        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                if (getTile(x, y) == 1) {
                    BlockEntity block = new BlockEntity(new Vector2(x, y));
                    blocks[x][y] = block;
                }
            }
        }

        return blocks;
    }

    public BlockEntity getBlock(int x, int y) {
        try {
            return blocks[x][y];
        } catch (Exception e) {
            return null;
        }
    }
}
