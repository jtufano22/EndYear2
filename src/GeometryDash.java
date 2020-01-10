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
    private Block o1 = new Block(width, height -118, 100, 50);
    private Block o2 = new Block(width+500, height -118, 100, 50);
    private Block o3 = new Block(width +250, height -150, 100, 50);
    private Block o4 = new Block(width +250, height -118, 100, 50);
    // particle effect behind player to show movement (optional)
    private ArrayList<Circle> fart = new ArrayList<>(50);

    public int pauseInt = 1;


    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: linear-gradient(from 10% 10% to 100% 100%, #ff0000, #ffc400);");

        ground.setFill(Color.MEDIUMPURPLE);
        dude.relocate(dudeX, dudeY);

        obstacles.add(o1);
        obstacles.add(o2);
        obstacles.add(o3);
        obstacles.add(o4);
        for(Shape s : obstacles) {
            pane.getChildren().add(s);
        }

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
                    if(s instanceof Block) {
                        obsBound = ((Block) s).getBounds();
                    }
                    else if(s instanceof Spike) {
                        obsBound = ((Spike) s).getBounds();
                        break;
                    }

//                    System.out.println(((Block)s).getBounds());
                    if(s instanceof Block &&
                            dudeY + 32 > ((Block) s).getY() &&
                            ((Block)s).getX() < dudeX + 32 &&
                            ((Block)s).getX() + ((Block) s).getWidth() > dudeX + 32 &&
                            !up) {
                        // stay on it
                        dudeY = (int)(((Block) s).getY()) - 32;
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
                        text.setTranslateY(225);
                        text.setTranslateX(65);

                        //button to play again
                        Button playAgain = new Button("Relive!");
                        playAgain.setTextAlignment(TextAlignment.CENTER);
                        playAgain.setFont(Font.font("Times New Roman", 15));
                        playAgain.setTranslateY(40);
                        playAgain.setTranslateX(550);
                        playAgain.setOnMouseClicked(e -> {

                            pane.getChildren().removeAll(text, playAgain);
                            start();
                        }


                    );
                        pane.getChildren().addAll(text, playAgain);


                     }
                    else if (s.getLayoutX() + s.getLayoutBounds().getWidth() <= -750) {
                        pane.getChildren().remove(s);
                        obstaclesPast++;
                        obstacles.set(i, null);
                    }

                    if(s instanceof Spike) {
                        s.setLayoutX(s.getLayoutX() - 5);
                    }
                    else if(s instanceof Block) {
                        ((Block)s).setX(((Block) s).getX()-5);
                    }
                }

                if (!supported && !jump) {
                    fall();
                }

//                Button pause = new Button("Pause");
//                pause.setTextAlignment(TextAlignment.CENTER);
//                pause.setFont(Font.font("Times New Roman", 15));
//                pause.setTranslateY(40);
//                pause.setTranslateX(100);
//                pause.setOnMouseClicked(e -> {
//
//                    if (pauseInt % 2 == 0) {
//                        pause.setText("Resume");
//                        stop();
//                        pauseInt++;
//                    }
//                    else {
//                        pause.setText("Pause");
//                        start();
//                        pauseInt++;
//                    }
//                        });

//                pane.getChildren().addAll(pause);

                dude.relocate(dudeX, dudeY);
                pane.requestFocus();
            }
        }.start();

//        pause.setOnAction(e -> {
//
//            if (stop();
//        });


        pane.getChildren().addAll(dude, ground);
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
