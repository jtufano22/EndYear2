import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Spike extends Rectangle {
    private int startX;

    public Spike(int x, int y, int width, int height) {
        super(x, y, width, height);
        setFill(Color.BEIGE);
        startX = x;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(this.getX() - 5, this.getY(), this.getWidth(), this.getHeight());
    }
}
