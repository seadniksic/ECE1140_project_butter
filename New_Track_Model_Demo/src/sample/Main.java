package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{


        Line blueLine = new Line(1, 1);
        Scene scene = new Scene(blueLine.GUIGridpane);
        stage.setTitle("Track Model");
        stage.setScene(scene);
        stage.show();

        blueLine.importLineFile("importThisFile.txt");
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
