package GUI.Controllers;

import GUI.Main_GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;


public class homeController {
    //public Label carLabel;
    private int index;

    //controller objects for driver and train engineer
    public driverController driverCont = new driverController();
    public trainEngineerController trainEngCont = new trainEngineerController();

    @FXML
    public Button driverButton;
    public Button trainEngineerButton;

    public homeController(){
        // I was getting an error for not having a default constructor, even though it was not being used
        // So don't delete it even though it isn't being used
        System.out.println("Default Constructor");
        System.out.println("index in base constructor: " + index);
    }


    public void onClickEvent(MouseEvent mouseEvent) {

        System.out.println("Anchor button has been clicked");
    }

    public void b1ButtonClicked(MouseEvent mouseEvent) throws Exception {

        //close home stage before driver launches
        Stage stage = (Stage) driverButton.getScene().getWindow();
        stage.close();

        FXMLLoader driverLoad = new FXMLLoader(getClass().getResource("/GUI/FXML/driver.fxml"));
        driverLoad.setController(driverCont); // manually load controller instance
        try{
            Parent driverRoot = driverLoad.load();
            Main_GUI.launch_UI(driverRoot, "Driver TC " + Integer.toString(index));
        } catch (Exception e){
            System.out.println("The Try/Catch statement to load the FXML file failed");
            e.printStackTrace();
        }

        driverCont.set_Index_Driver(index);
        driverCont.update_Labels();
    }

    public void b2ButtonClicked(MouseEvent mouseEvent)throws Exception {

            FXMLLoader trainEngLoad = new FXMLLoader(getClass().getResource("/GUI/FXML/Train_Engineer.fxml"));
            trainEngLoad.setController(trainEngCont); // manually load controller instance
            try {
                Parent trainEngRoot = trainEngLoad.load();
                Main_GUI.launch_UI(trainEngRoot, "Train Engineer TC " + Integer.toString(index));
            } catch (Exception e) {
                System.out.println("The Try/Catch statement to load the FXML file failed");
                e.printStackTrace();
            }

            trainEngCont.set_Train_Eng_Index(index);
            trainEngCont.updateAttributes();


        }

    public void set_Index_Home(int j){
        index = j;
    }

    public int get_Index_Home(){
        return index;
    }

}
