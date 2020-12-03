package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OverallSoftware extends Application {
    /*public static Button b1 = new Button("Button");

    public static void update_Button() {
        /*Character character = 0;
        character++;
        b1.setText(character.toString());
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception{
        Network.start_Server();

        primaryStage.setTitle("Track Controller Overview");
        Parent root = FXMLLoader.load(getClass().getResource("../resources/sample.fxml"));

        //HBox hBox = new HBox();
        //hBox.getChildren().addAll(root, b1);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
