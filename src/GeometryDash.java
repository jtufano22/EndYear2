import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class GeometryDash extends Application {

    private Button pause;
    // player
    private ImageView dude = new ImageView("squareDude.png");

    // dimensions of scene
    private int height = 500;
    private int width = 750;

    // coordinates of player
    private int dudeX = 63;
    private int dudeY = height - 100;

    // boundary for player and obstacles
    private Rectangle2D dudeBound = new Rectangle2D(dudeX, dudeY, 32, 32);
    private Rectangle2D obsBound;

    // jumping stuff
    private boolean jump = false;
    private boolean up = false;
    private int startingPos;

    // the ground
    private Rectangle ground = new Rectangle(0, height-68, width+28, 78);

    // all obstacles must be instantiated here
    private int obstaclesPast = 0;
    private ArrayList<Shape> obstacles = new ArrayList<>(100);
    private Rectangle o1 = new Rectangle(width, height -118, 100, 50);

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
                    if (up) {
                        jump();
                    } else {
                        fall();
                    }
                }
                boolean supported = false;

                // everything to do with obstacles goes here
                for(int i = obstaclesPast; i < obstacles.size(); i++) {
                    Shape s = obstacles.get(i);
                    if(s instanceof Rectangle) {
                        obsBound = new Rectangle2D(s.getLayoutX() + width,
                                ((Rectangle) s).getY(),
                                s.getLayoutBounds().getWidth(),
                                s.getLayoutBounds().getHeight());
                    }
                    else if(s instanceof Polygon) {

                        break;
                    }

//                    System.out.println((dudeY + 32) + " = " + ((Rectangle) s).getY());
//                    System.out.println("\n" + (750 + s.getLayoutX()) + " < " + (dudeX + 32) + "\n" +
//                            (750 + s.getLayoutX() + ((Rectangle) s).getWidth()) + ">"  + (dudeX + 32));
                    if(s instanceof Rectangle &&
                            dudeY + 32 == ((Rectangle) s).getY() &&
                            (width + s.getLayoutX()) < dudeX + 32 &&
                            (width + s.getLayoutX() + ((Rectangle) s).getWidth()) > dudeX + 32 &&
                            !up) {
                        // stay on it
                        dudeY = (int)(((Rectangle) s).getY()) - 32;
                        jump = false;
                        up = false;
                        supported = true;
                        System.out.println("stay up");
                    }

                    else if (hits(dudeBound, obsBound)) {
                        // die
                        stop();
                    }
                    else if (s.getLayoutX() + s.getLayoutBounds().getWidth() <= -750) {
                        pane.getChildren().remove(s);
                        obstaclesPast++;
                        obstacles.set(i, null);
                    }


                    s.setLayoutX(s.getLayoutX() - 5);
                }

                if (!supported && !jump && dudeY < height-100) {
                    dudeY += 5;
                }


                dude.relocate(dudeX, dudeY);
                pane.requestFocus();
            }
        }.start();

//        pause.setOnAction(e -> {
//
//            if (stop();
//        });



        Scene scene = new Scene(pane, width, height);
        stage.setScene(scene);
        stage.setTitle("Square Dude Dodging These Hoes");
        stage.getIcons().add(new Image("squareDude.png"));
        stage.setResizable(false);
        stage.show();
    }

    // jumping and falling
    private void jump() {
        if(startingPos-100 < dudeY) {
            dudeY-=5;
        }
        else{
            up = false;
        }
        dudeBound = new Rectangle2D(dudeX, dudeY, 32, 32);
    }
    private void fall() {
        if(height-100 > dudeY) {
            dudeY+=5;
        }
        else{
            jump = false;
        }
        dudeBound = new Rectangle2D(dudeX, dudeY, 32, 32);
    }

    // collision
    private boolean hits(Rectangle2D o1, Rectangle2D o2) {
        return (o1.getMaxX() == o2.getMinX()) && (o1.getMaxY() >= o2.getMinY());
    }
}