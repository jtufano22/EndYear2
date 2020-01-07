import javafx.scene.shape.Polygon;
import javafx.geometry.Rectangle2D;

public class Spike extends Polygon {
    private int startX;

    public Spike(int x, int y, int width, int height) {
        super(x, y, x + width, y, (double)(x + width)/2, y-height);
        startX = x;
    }
    public Rectangle2D getBounds() {
        return new Rectangle2D(this.getLayoutX() + startX, this.getLayoutBounds().getMinY(), this.getLayoutBounds().getWidth(), this.getLayoutBounds().getHeight());
    }
}
