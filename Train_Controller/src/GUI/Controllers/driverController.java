package GUI.Controllers;

import Back_End.Network;
import Back_End.Train_Controller_Catalogue;
//import Main
import GUI.Main_GUI;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.text.DecimalFormat;


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
    public Pane errorBox;
    public Label errorLabel;
    public Label errorTypeLabel;

    public int indexDriver;

    public driverController(){

        //System.out.println("default constructor"); // same deal as homeController
       // setpointLabel.setText(java.util.Calendar.getInstance().getTime().toString());


        // restricts brake and ebrake from being set at the same time on vital section
        /*ToggleGroup vital_Group = new ToggleGroup();
        brakeButton.setToggleGroup(vital_Group);
        eBrakeButton.setToggleGroup(vital_Group);

        //restricts brake and ebrake from being set at the same time on nonvital section
        ToggleGroup nv_Group = new ToggleGroup();
        brakeButtonNV.setToggleGroup(nv_Group);
        eBrakeButtonNV.setToggleGroup(nv_Group);*/


    }

    public void onMouseClickManMode(MouseEvent mouseEvent) {
        System.out.println("Manual mode has been selected");

        if(speedIn.isVisible()==true && setSpeedButton.isVisible() == true) {
            speedIn.setVisible(false);
            setSpeedButton.setVisible(false);
            Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Manual_Mode(false);
        } else {
        // alert for comfirmation of manual mode
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Manual Mode");
        alert.setContentText("You are entering Manual Mode.");
        alert.showAndWait();

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
        //System.out.println("speed button clicked");
        System.out.println("speed in: " + Integer.valueOf(speedIn.getText()));
        //System.out.println("comm speed: " + Integer.valueOf(commSpeedLabel.getText()));
        System.out.println(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Commanded_Speed()*2.23694);
        if(Integer.valueOf(speedIn.getText()) > Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Commanded_Speed()*2.23694){
            // alert for notification that input speed is greater than commanded speed
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Manual Speed");
            alert.setContentText("The speed you entered is higher than the commanded speed. Please enter a valid speed.");
            alert.showAndWait();
        }
        Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Manual_Speed(Integer.valueOf(speedIn.getText()));
        //System.out.println("manual speed set");
    }

    public void updateTheStuff(MouseEvent mouseEvent) {
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
        //Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Announcements(announcementsRB.isSelected());
        // change this to a text field that takes a string and then displays it to the Train Model UI

    }

    public void set_Temp_Clicked(MouseEvent mouseEvent){
        if(Integer.valueOf(tempIn.getText()) >= 60 && Integer.valueOf(tempIn.getText()) <= 80){
            Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).set_Desired_Temperature(Integer.valueOf(tempIn.getText()));
        }
    }

    public void click_Fix_Train(MouseEvent mouseEvent) throws RemoteException {
        System.out.println("Fix this train");
        Main_GUI.train_Cat_1.remove_Failure_Status(indexDriver);
    }

    public void set_Index_Driver(int i){
        indexDriver = i;
        System.out.println("Driver index set as " + indexDriver);
    }

    public int get_Index_Driver(){
        return indexDriver;
    }


    public void update_Labels() {
        //speedIn = Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Manual_Speed());
        //System.out.println("next position: " + Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Next_Stop());

        //truncate the velocity to 2 decimal places
            DecimalFormat df = new DecimalFormat("0.00");
            currSpeedLabel.setText(df.format(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Current_Speed() * 2.23694)); // 2.23694 is the conversion factor for m/s to mph
            commSpeedLabel.setText(df.format(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Commanded_Speed() * 2.23694)); //updates commanded speed label
            authorityLabel.setText(Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Authority())); // authority (int) from train controller class
            positionLabel.setText(df.format(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Position()));
            if(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Next_Stop() != null) {
                //System.out.println(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Next_Stop());
                nextStopLabel.setText(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Next_Stop());
            } else {
                nextStopLabel.setText("No Next Stop update");
            }
            if (Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Setpoint_Update() != null) {
                setpointLabel.setText(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Setpoint_Update()); //need to create setpoint update variable
            } else {
                setpointLabel.setText("No setpoint update");
            }
            eBrakeButton.setSelected(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_E_Brake_On());
            eBrakeButtonNV.setSelected(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_E_Brake_On());
            brakeButton.setSelected(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Brakes_On());
            brakeButtonNV.setSelected(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Brakes_On());
            currTempLabel.setText(df.format(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Current_Temperature())); // don't have a current temperature either

        /*if(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Failure_Bool() == true){
            if(errorBox.isVisible()==true ) {
                errorBox.setVisible(false);
                errorLabel.setVisible(false);
                errorTypeLabel.setVisible(false);
            } else {
                //errorLabel.setText("Error:");
                errorBox.setVisible(true);
                errorLabel.setVisible(true);
                errorTypeLabel.setVisible(true);
                errorTypeLabel.setText(Main_GUI.train_Cat_1.get_Train_Controller(indexDriver).get_Failure_Mode());
            }
        }*/
        /*else{
            errorBox.setVisible(false);
            errorLabel.setVisible(false);
            errorTypeLabel.setVisible(false);
        }*/

    }


}
