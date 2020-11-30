package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OverallSoftware extends Application {
    public static TrackControllerCatalogue tc = new TrackControllerCatalogue();
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Network.start_Server();

        primaryStage.setTitle("Track Controller Overview");
        Parent root = FXMLLoader.load(getClass().getResource("../resources/sample.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
