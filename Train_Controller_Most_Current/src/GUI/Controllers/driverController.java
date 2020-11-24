package GUI.Controllers;

import Back_End.Network;
import Back_End.Train_Controller_Catalogue;
//import Main
import GUI.Main_GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class driverController {
    @FXML
    public TextField speedIn;
    public RadioButton brakeButton;
    public RadioButton eBrakeButton;
    public RadioButton brakeButtonNV;
    public RadioButton eBrakeButtonNV;
    public Label currSpeedLabel;
    public Label commSpeedLabel;
    public Label authorityLabel;
    public Label positionLabel;
    public Label nextStopLabel;
    public Label setpointLabel;
    public Label currTempLabel;
    public TextField tempIn;
    public Button setSpeedButton;
    public Button backButton;
    public RadioButton doorsLeftRB;
    public RadioButton doorsRightRB;
    public RadioButton inLightsRB;
    public RadioButton extLightsRB;
    public RadioButton advertisementsRB;
    public RadioButton announcementsRB;


    public int indexDriver;

    public driverController(){

        System.out.println("default constructor"); // same deal as homeController
       // setpointLabel.setText(java.util.Calendar.getInstance().getTime().toString());
    }

    public driverController(int i){
        indexDriver = i;
        System.out.println("in the home controller constructor");
        System.out.println("Index: " + indexDriver);

        try{
            Parent driverRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/FXML/driver.fxml"));
            Scene driver = new Scene(driverRoot);
            Stage driverStage = new Stage();
            driverStage.setTitle("Driver UI");

            driverStage.setScene(driver);
            driverStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onMouseClickManMode(MouseEvent mouseEvent) {
        System.out.println("Manual mode has been selected");

        if(speedIn.isVisible()==true && setSpeedButton.isVisible() == true) {
            speedIn.setVisible(false);
            setSpeedButton.setVisible(false);
            Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Manual_Mode(false);
        } else {
        speedIn.setVisible(true);
        setSpeedButton.setVisible(true);
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Manual_Mode(true);
        }
        //Button event handles setting the manual speed
    }

    public void onMouseClickBrake(MouseEvent mouseEvent) {
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Brakes_On(brakeButton.isSelected());
    }

    public void onMouseClickEBrake(MouseEvent mouseEvent) {
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_E_Brake_On(eBrakeButton.isSelected());
    }

    public void onMouseClickBack(MouseEvent mouseEvent) {
        //System.out.println("Back button has been selected");
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void onMouseClickBrakeNV(MouseEvent mouseEvent) {
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Brakes_On(brakeButtonNV.isSelected());
    }

    public void onMouseClickEBrakeNV(MouseEvent mouseEvent) {
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_E_Brake_On(eBrakeButtonNV.isSelected());
    }

    public void setSpeedOnClick(MouseEvent mouseEvent){
        System.out.println("speed button clicked");
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Manual_Speed(Integer.valueOf(speedIn.getText()));
        System.out.println("manual speed set");
    }

    public void updateTheStuff(MouseEvent mouseEvent){
        update_Labels();
    }

    public void doorsLeftClicked(MouseEvent mouseEvent){
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Open_Doors_Left(doorsLeftRB.isSelected());

    }

    public void doorsRightClicked(MouseEvent mouseEvent){
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Open_Doors_Right(doorsRightRB.isSelected());

    }

    public void intLightsOn(MouseEvent mouseEvent){
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Internal_Lights(inLightsRB.isSelected());
    }

    public void extLightsOn(MouseEvent mouseEvent){
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_External_Lights(extLightsRB.isSelected());

    }

    public void advertisementsOn(MouseEvent mouseEvent){
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Advertisements(advertisementsRB.isSelected());
    }

    public void announcementsOn(MouseEvent mouseEvent){
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Announcements(announcementsRB.isSelected());

    }

    public void set_Index_Driver(int i){
        indexDriver = i;
        System.out.println("Driver index set as " + indexDriver);
    }

    public int get_Index_Driver(){
        return indexDriver;
    }

    public void update_Labels(){
        //speedIn = Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Manual_Speed());
        currSpeedLabel.setText(Double.toString(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Current_Speed())); //I need a current speed variable in train controller
        commSpeedLabel.setText(Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Commanded_Speed())); //updates commanded speed label
        authorityLabel.setText(Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Authority())); // authority (int) from train controller class
        //positionLabel; // how am I gettin position??????
        //nextStopLabel; // need to create next stop variable
        if(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Setpoint_Update() != null) {
            setpointLabel.setText(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Setpoint_Update()); //need to create setpoint update variable
        } else {
            setpointLabel.setText("No setpoint update");
        }
        //eBrakeButton.set
        //currTempLabel; // don't have a current temperature either
        //tempIn; // dont have a variable for this either*/
     }

     // currSpeed from Sead
     //


}
