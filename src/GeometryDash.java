import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GeometryDash extends Application {
    // player
    private ImageView dude = new ImageView("squareDude.png");

    // dimensions of scene
    private int height = 500;
    private int width = 750;

    // coordinates of player
    private int dudeX = 64;
    private int dudeY = height - 100;

    // boundary for player
    private Rectangle dudeBound = new Rectangle(dudeX, dudeY, 32, 32);

    // jumping stuff
    private boolean jump = false;
    private boolean up = false;
    private int startingPos;

    // the ground
    private Rectangle ground = new Rectangle(0, height-68, width+28, 78);

    // all obstacles must be instantiated here
    private int obstaclesPast = 0;
    private ArrayList<Shape> obstacles = new ArrayList<>(100);
    Rectangle o1 = new Rectangle(width, height -118, 25, 50);

    // particle effect behind player to show movement (optional)
    private ArrayList<Circle> fart = new ArrayList<>(50);

    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: linear-gradient(from 10% 10% to 100% 100%, #ff0000, #ffc400);");

        obstacles.add(o1);
        for(Shape s : obstacles) {
            pane.getChildren().add(s);
        }

        ground.setFill(Color.MEDIUMPURPLE);
        dude.relocate(dudeX, dudeY);
        pane.getChildren().addAll(dude, ground);

        new AnimationTimer() {
            public void handle(long now){
                //jumping
                pane.setOnKeyPressed(e ->
                {
                    KeyCode key = e.getCode();
                    if((key.equals(KeyCode.W) || key.equals(KeyCode.UP) || key.equals(KeyCode.SPACE)) && !jump) {
                        jump = true;
                        up = true;
                        startingPos = dudeY;
                    }
                });

                if(jump) {
                    jump();
                }
                dude.relocate(dudeX, dudeY);


                // everything to do with obstacles goes here
                for(int i = obstaclesPast; i < obstacles.size(); i++) {
                    Shape s = obstacles.get(i);
                    if (dudeBound.intersects(s.getLayoutBounds())) {
                        // game end
                        break;
                    }
                    else if (s.getLayoutX() <= -775) {
                        pane.getChildren().remove(s);
                        obstaclesPast++;
                        obstacles.set(i, null);
                    }
                    else {
                        s.setLayoutX(s.getLayoutX() - 5);
                    }
                }

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
