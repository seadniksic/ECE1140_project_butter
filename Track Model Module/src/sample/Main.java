package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Network staeughff
        try {
            Network.start_Server();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Track_Model_GUI this_Track_Model_GUI = new Track_Model_GUI();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
