import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tetris extends Application {

    public void start(Stage ps) {
        GridPane tetrisgrid = new GridPane();
        BorderPane border = new BorderPane();


        Button newGame = new Button("New Game");
        Button pause = new Button("Pause");
        Button lvl1 = new Button("Level 1");
        Button lvl2 = new Button("Level 2");
        Button lvl3 = new Button("Level 3");

        newGame.setOnAction(e ->
                );


        lvl1.setStyle("-fx-font-size: 2em; ");
        lvl2.setStyle("-fx-font-size: 2em; ");
        lvl3.setStyle("-fx-font-size: 2em; ");

        VBox rightButtons = new VBox(70);
        rightButtons.setAlignment(Pos.CENTER);
        rightButtons.getChildren().addAll(newGame, pause);
        border.setRight(rightButtons);


        VBox leftButtons = new VBox(70);
        leftButtons.setAlignment(Pos.CENTER);
        leftButtons.getChildren().addAll(lvl1, lvl2,lvl3);
        border.setLeft(leftButtons);

        border.setCenter(tetrisgrid);




        Scene scene = new Scene(border, 900, 900);
        ps.setTitle("Tetris");
        ps.setScene(scene);
        ps.show();

    }


}
