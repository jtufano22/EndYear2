import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
    private int startX;

    public Block(int x, int y, int width, int height) {
        super(x, y, width, height);
        startX = x;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
