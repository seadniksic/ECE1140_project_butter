package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        // Network staeughff
//        Network.start_Server();
//        while (Network.connected_Module_2 != true) {
//            try {
//                Network.connect_To_Modules();
//            } catch (Exception e) {
//            }
//
//        }

        Track_Model_GUI this_Track_Model_GUI = new Track_Model_GUI();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
