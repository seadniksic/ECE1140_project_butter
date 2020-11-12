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
        Network.start_Server();
        Network.connect_To_Modules();

        /*train_Cat_1.add_Train_Controller(4);
        train_Cat_1.set_Commanded_Speed_Authority(0, 50, 10000);
        train_Cat_1.add_Train_Controller(3);
        //train_Cat_1.set_Commanded_Speed_Authority(1, 22, 5000);
        train_Cat_1.add_Train_Controller(5);
        train_Cat_1.set_Commanded_Speed_Authority(2, 35, 250000);*/



        Parent root = FXMLLoader.load(getClass().getResource("/GUI/FXML/Train_Catalogue.FXML"));
        primaryStage.setTitle("Train Controller Catalogue");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        /*Parent root = FXMLLoader.load(getClass().getResource("/GUI/FXML/home.FXML"));
        primaryStage.setTitle("Train Controller Home");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();*/
        //new homeController(0);

        //This line is throwing an error
        //Controller.set_my_Module_Name();
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
