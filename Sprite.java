import java.awt.Graphics;
import java.awt.Image;

/**
 * Created by greg on 11/08/13.
 */
public class Sprite {
    private Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    }
}
