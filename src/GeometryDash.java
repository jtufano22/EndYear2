import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GeometryDash extends Application {
    private ImageView dude = new ImageView("squareDude.png");

    private int height = 500;
    private int width = 750;

    private int dudeX = 64;
    private int dudeY = height - 100;

    private boolean jump = false;
    private boolean up = false;
    private int startingPos;

    private Rectangle ground = new Rectangle(0, height-68, width+28, 78);


    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: radial-gradient(from 100% 100% to 100% 100%, #000000, #FFFFFF);");

        ground.setFill(Color.DARKBLUE);

        dude.relocate(dudeX, dudeY);
        pane.getChildren().addAll(dude, ground);

        new AnimationTimer() {
            public void handle(long now){
                pane.setOnKeyPressed(e ->
                {
                    KeyCode key = e.getCode();
                    if(key.equals(KeyCode.W) || key.equals(KeyCode.UP) || key.equals(KeyCode.SPACE)) {
                        jump = true;
                        up = true;
                        startingPos = dudeY;
                    }
                });
                if(jump) {
                    jump();
                }
                dude.relocate(dudeX, dudeY);
                pane.requestFocus();
            }
        }.start();

        Scene scene = new Scene(pane, width, height);
        stage.setScene(scene);
        stage.setTitle("Square Dude Dodging These Hoes");
        stage.getIcons().add(new Image("squareDude.png"));
        stage.setResizable(false);
        stage.show();
    }

    public void jump() {
        if(up && startingPos-96 < dudeY) {
            dudeY-=4;
        }
        else if(startingPos-96 >= dudeY) {
            up = false;
        }

        if(!up && height-100 > dudeY) {
            dudeY+=4;
        }
        else if (height-100 <= dudeY) {
            jump = false;
        }
    }
}
