import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Created by greg on 11/08/13.
 */
public class SpriteStore {
    private static SpriteStore store = new SpriteStore();
    private HashMap sprites = new HashMap();

    public static SpriteStore get() {
        return store;
    }

    public Sprite getSprite(String ref) {
        if (sprites.get(ref) != null) {
            return (Sprite) sprites.get(ref);
        }

        BufferedImage sourceImage = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(ref);
            if (url == null) {
                broken("Sprite doesn't exit: " + ref);
            }
            sourceImage = ImageIO.read(url);
        } catch (Exception e) {

        }

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

        image.getGraphics().drawImage(sourceImage, 0, 0, null);

        Sprite sprite = new Sprite(image);
        sprites.put(ref, sprite);

        return sprite;
    }

    private void broken(String msg) {
        System.err.println(msg);
        System.exit(0);
    }
}
