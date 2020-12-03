package GUI;

import Back_End.Network;
import Back_End.Train_Controller_Catalogue;
import GUI.Controllers.homeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main_GUI extends Application {
    public static Train_Controller_Catalogue train_Cat_1 = new Train_Controller_Catalogue();

    public void init(){

    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        // starts the server and connects to the modules I should connect with
        //In my case, I only connect to the train model's module
        Network.start_Server();
        Network.connect_To_Modules();

        // These are just commented out manual tests
        //REMOVE THIS BLOCK OF CODE BEFORE SUBMISSION
        /*train_Cat_1.add_Train_Controller(4);
        train_Cat_1.set_Commanded_Speed_Authority(0, 50, 10000);
        train_Cat_1.add_Train_Controller(3);
        //train_Cat_1.set_Commanded_Speed_Authority(1, 22, 5000);
        train_Cat_1.add_Train_Controller(5);
        train_Cat_1.set_Commanded_Speed_Authority(2, 35, 250000);*/


        //loads the Train Catalogue UI
        // Other UI's load in the Controllers for each respective UI. I.E. The Home Controller will load Driver/Train Engineer UI's
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/FXML/Train_Catalogue.FXML"));
        primaryStage.setTitle("Train Controller Catalogue");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void launch_UI(Parent h, String s) {

        try {

            Scene homeScene = new Scene(h);
            Stage homeStage = new Stage();
            homeStage.setTitle(s);

            homeStage.setScene(homeScene);
            homeStage.show();
        } catch (Exception e) {
            System.out.println("The home UI was unable to launch");
        }

    }

    public static void main(String[] args) {

        launch(args);
    }
}
