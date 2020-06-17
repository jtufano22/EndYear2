import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


public class GeometryDash extends Application {
    private boolean supported = false;
    private String type = "spaceship";

    private Button pause;
    // player

    private ImageView dude;

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

    public int pauseInt = 1;

    public void start(Stage stage){
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: linear-gradient(from 10% 10% to   100% 100%, #ff0000, #ffc400);");

        ground.setFill(Color.MEDIUMPURPLE);
        dude.relocate(dudeX, dudeY);
        pane.getChildren().addAll(dude, ground);

        AnimationTimer t = new AnimationTimer() {
            public void handle(long now){
                //jumping



                if (type.equals("regular")) {
                    pane.setOnKeyPressed(e ->
                    {
                        Image a = new Image("squareDude.png");
                        dude.setImage(a);
                        KeyCode key = e.getCode();
                        if((key.equals(KeyCode.W) || key.equals(KeyCode.UP) || key.equals(KeyCode.SPACE)) && !jump) {
                            jump = true;
                            up = true;
                            startingPos = dudeY;
                        }
                    });

                    if (jump) {
                        if (up) {
                            jump();
                        } else {
                            fall();
                        }
                    }
                    if (!supported && !jump) {
                        fall();
                    }
                }

                else if (type.equals("ufo")) {
                    Image a = new Image("ufo.png");
                    dude.setImage(a);

                    pane.setOnKeyPressed(e ->
                    {
                        KeyCode key = e.getCode();
                        if((key.equals(KeyCode.W) || key.equals(KeyCode.UP) || key.equals(KeyCode.SPACE)) ) {
                            jump = true;
                            up = true;
                            startingPos = dudeY;
                        }
                    });
                    if (jump) {
                        if (up) {
                            jump();
                        }
                        else {
                            fall();
                        }
                    }
                    if (!supported && !jump) {
                        fall();
                    }
                }

                else if (type.equals("spaceship")) {
                    Image a = new Image("ufo.png");
                    dude.setImage(a);

                    pane.setOnKeyPressed(e ->
                    {
                        KeyCode key = e.getCode();
                        if((key.equals(KeyCode.W) || key.equals(KeyCode.UP) || key.equals(KeyCode.SPACE)) ) {
                            dudeY -= 10;
                            jump = true;
                            startingPos = dudeY;
                        }
                    });


                    if (!supported && !jump) {
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
                        if(s.getX() < 750 && !((Spike)s).onScreen) {
                            pane.getChildren().add(s);
                            ((Spike) s).onScreen = true;
                        }
                    }

                    if(s instanceof Block &&
                            dudeY + 32 >= s.getY() &&
                            dudeY < s.getY() &&
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
                        pane.getChildren().removeAll(pane.getChildren());
                        pane.getChildren().add(text);

                        //game over sound
                        getHostServices().showDocument("https://www.youtube.com/watch?v=hlnpkrJs6wM&t=0s");

                    }
                    else if (s.getX() + s.getWidth() <= 0) {
                        pane.getChildren().remove(s);
                        obstacles.remove(i);
                    }

                    s.setX(s.getX() - 5);
                }



                if(obstacles.isEmpty()) {
                    stop();

                    // win text
                    Text win = new Text("You Win!");
                    win.setTextAlignment(TextAlignment.CENTER);
                    win.setFont(Font.font("Times New Roman", 60));
                    win.setWrappingWidth(500);
                    win.setFill(Color.DARKBLUE);
                    win.setTranslateY(200);
                    win.setTranslateX(110);
                    pane.getChildren().removeAll(pane.getChildren());
                    pane.getChildren().add(win);

                    //win sound
                    String musicFile = "MonstersInc2.mp3";

                    Media sound = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.setVolume(10.0);
                    mediaPlayer.setAutoPlay(true);
                    mediaPlayer.play();
                }

                dude.relocate(dudeX, dudeY);
                pane.requestFocus();
            }
        };
        t.start();

        Image pause1 = new Image("pauseButton.png");
        ImageView psbutton = new ImageView(pause1);
        psbutton.setFitHeight(100);
        psbutton.setFitWidth(100);
        psbutton.setY(50);
        psbutton.setX(600);
        Circle pauseCircle = new Circle(50);
        pauseCircle.setCenterX(650);
        pauseCircle.setCenterY(100);
        pauseCircle.setFill(Color.TRANSPARENT);
        pauseCircle.setOnMouseClicked(e -> {
            pauseInt++;
            if (pauseInt % 2 == 0) {

                psbutton.setImage(new Image("resume.png"));
                t.stop();

            }
            else {
                psbutton.setImage(new Image("pauseButton.png"));
                t.start();
            }
        });

        pane.getChildren().addAll(psbutton, pauseCircle);

        Scene scene = new Scene(pane, width, height);
        stage.setScene(scene);
        stage.setTitle("Square Dude Dodging These Rectanghoes");
        stage.getIcons().add(new Image("squareDude.png"));
        stage.setResizable(false);
        stage.show();
    }

    // jumping and falling
    private void jump() {
        if(startingPos-102 < dudeY) {
            dudeY-=6;
        }
        else{
            up = false;
        }
        dudeBound = new Rectangle2D(dudeX, dudeY, 32, 32);
    }

    private void fall() {
        if(dudeY + 6 >= height - 100) {
            dudeY = height - 100;
        }
        if(height-100 > dudeY) {
            dudeY+=6;
        }
        else{
            jump = false;
        }
        dudeBound = new Rectangle2D(dudeX, dudeY, 32, 32);
    }
    // collision
    private boolean hits(Rectangle2D o1, Rectangle2D o2) {
        return o1.intersects(o2);
    }

    public void addObjects() throws IOException, ParseException {

    }
}
