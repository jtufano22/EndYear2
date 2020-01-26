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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class GeometryDash extends Application {
    private boolean supported = false;

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
    private ArrayList<Rectangle> obstacles = new ArrayList<>(100);
    private Block o1 = new Block(width, height -118, 100, 50, false);
    private Block o2 = new Block(width+500, height -118, 100, 50, false);
    private Block o3 = new Block(width +250, height -150, 100, 50, false);
    private Block o4 = new Block(width +250, height -118, 100, 50, false);
    private Block o5 = new Block(width +700, height -118, 500, 50, false);
    private Block o6 = new Block(width +800, height -150, 410,50 , false);
//    private Spike o5 = new Spike(width-50, height-68,  50, 50);
    // particle effect behind player to show movement (optional)
    private ArrayList<Circle> fart = new ArrayList<>(50);

    public int pauseInt = 1;


    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: linear-gradient(from 10% 10% to   100% 100%, #ff0000, #ffc400);");

        ground.setFill(Color.MEDIUMPURPLE);
        dude.relocate(dudeX, dudeY);
        pane.getChildren().addAll(dude, ground);

        obstacles.add(o1);
        obstacles.add(o2);
        obstacles.add(o3);
        obstacles.add(o4);
        obstacles.add(o5);
        obstacles.add(o6);
//        obstacles.add(o5);
//        for(Shape s : obstacles) {
//            pane.getChildren().add(s);
//        }

        AnimationTimer t = new AnimationTimer() {
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

                supported = false;
                // everything to do with obstacles goes here
                for(int i = 0; i < obstacles.size(); i++) {
                    Rectangle s = obstacles.get(i);
                    if(s instanceof Block) {
                        obsBound = ((Block) s).getBounds();
                        if(s.getX() < 750 && !((Block)s).onScreen) {
                            pane.getChildren().add((Block)s);
                            ((Block) s).onScreen = true;
                        }
                    }
                    else if(s instanceof Spike) {
                        obsBound = ((Spike) s).getBounds();
//                        if(s.getX() < 750 && !((Spike)s).onScreen) {
//                            pane.getChildren().add(s);
//                            ((Spike) s).onScreen = true;
//                        }
                    }

                    if(s instanceof Block &&
                            dudeY + 32 >= s.getY() &&
                            s.getX() < dudeX + 32 &&
                            s.getX() + s.getWidth() > dudeX &&
                            !up) {
                        // stay on it
                        dudeY = (int)((s).getY()) - 32;
                        jump = false;
                        up = false;
                        supported = true;
                    }

                    else if (hits(dudeBound, obsBound)) {
                        // die
                        stop();

                        //game over text
                        Text text = new Text("Game Over!!");
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.setFont(Font.font("Times New Roman", 60));
                        text.setWrappingWidth(500);
                        text.setFill(Color.DARKBLUE);
                        text.setTranslateY(200);
                        text.setTranslateX(110);
                        pane.getChildren().add(text);

                        //rectangle that falls over the screen after death
                        Rectangle r = new Rectangle(500, 500);
                        r.setFill(Color.rgb(64, 64, 64, 0.4));
                        r.widthProperty().bind(pane.widthProperty());
                        r.heightProperty().bind(pane.heightProperty());
                        pane.getChildren().add(r);

                        //restart button
//                        Image re = new Image("restart.png");
//                        ImageView res = new ImageView(re);
//                        res.setFitHeight(64);
//                        res.setFitWidth(64);
//                        res.setX(325);
//                        res.setY(250);
//                        pane.getChildren().add(res);
//                        res.setOnMousePressed(e -> start());



                     }
                    else if (s.getX() + s.getWidth() <= 0) {
                        pane.getChildren().remove(s);
                        obstaclesPast++;
                        obstacles.remove(i);
                    }

                    s.setX(s.getX() - 5);
                }

                if (!supported && !jump) {
                    fall();
                }

                dude.relocate(dudeX, dudeY);
                pane.requestFocus();
            }
        };
        t.start();

//        pause.setOnAction(e -> {
//
//            if (stop();
//        });


        Image pause1 = new Image("pauseButton.png");
        ImageView psbutton = new ImageView(pause1);
        psbutton.setFitHeight(100);
        psbutton.setFitWidth(100);
        psbutton.setY(50);
        psbutton.setX(600);
        psbutton.setOnMouseClicked(e -> {
            pauseInt++;
            System.out.println(pauseInt);
            if (pauseInt % 2 == 0) {

                psbutton.setImage(new Image("resume.png"));
                t.stop();

            }
            else {
                psbutton.setImage(new Image("pauseButton.png"));
                t.start();
            }
        });

        pane.getChildren().addAll(psbutton);

        Scene scene = new Scene(pane, width, height);
        stage.setScene(scene);
        stage.setTitle("Square Dude Dodging These Rectanghoes");
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
        //return (o1.getMaxX() == o2.getMinX() && o1.getMaxY() >= o2.getMinY());
        return o1.intersects(o2);
    }
}
