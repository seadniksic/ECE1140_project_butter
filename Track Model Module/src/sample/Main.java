package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Track_Model_GUI this_Track_Model_GUI = new Track_Model_GUI();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
