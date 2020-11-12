package GUI.Controllers;

import GUI.Main_GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import networking.*;
import Back_End.*;
import Back_End.Network;

public class trainEngineerController{
    @FXML
    public TextField kpTextField;
    public TextField kiTextField;
    public Label carLabel;
    public CheckBox defaultBox;

    //index for UI
    public int trainEngIndex;

   /* public void start(){
        carLabel.setText(Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).get_Number_Of_Cars()));
    }*/

    public void onMouseClickDefault(MouseEvent mouseEvent) {
        System.out.println("The Defaults have been selected");
    }

    public void onKeyPressedTB1(KeyEvent keyEvent) {
        System.out.println("Text has been entered for Kp");
    }

    public void onKeyPressedTB2(KeyEvent keyEvent) {
        System.out.println("Text has been entered for Ki");
    }

    public void onMouseClickButton(MouseEvent mouseEvent) {

        //The default check box is causing KP and KI to not be set

        System.out.println("The Ok button has been pressed");

        if(kpTextField.getText() != "" && kiTextField.getText() != "" && defaultBox.isSelected()) {
            try {
                //Main_GUI.train_Cat_1.set_Defaults(Integer.valueOf(kpTextField.getText()), Integer.valueOf(kiTextField.getText()), (Integer.valueOf(carLabel.getText())));
                Main_GUI.train_Cat_1.set_Defaults(Integer.valueOf(kpTextField.getText()), Integer.valueOf(kiTextField.getText()), (Integer.valueOf(Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).get_Number_Of_Cars())));
            } catch (Exception e){
                e.printStackTrace();
            }

        } else if(kpTextField.getText() != "" && kiTextField.getText() != ""){
            Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).set_Kp(Integer.valueOf(kpTextField.getText()));
            Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).set_Ki(Integer.valueOf(kiTextField.getText()));
            //Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).set_Number_Of_Cars(Integer.valueOf(carLabel.getText()));
            Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).set_Number_Of_Cars(Integer.valueOf(Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).get_Number_Of_Cars()));
        } else {
            System.out.println("Please enter values for Kp and Ki.");
        }
    }

    public void onMouseClickBack(MouseEvent mouseEvent) {
        System.out.println("Back button has been pressed");
    }

    public void set_Car_Label(int cars){
        // PROBLEM COMMUNICATING BETWEEN THIS CLASS AND TRAIN CONTROLLER CLIENT
        //carLabel.setText(String.valueOf(cars));
    }

    public void updateAttributes() {
        System.out.println("Update attributes hit");
        Integer hold = Main_GUI.train_Cat_1.get_Train_Controller(trainEngIndex).get_Number_Of_Cars();
        System.out.println(hold);
        carLabel.setText(hold.toString());
    }

    public void set_Train_Eng_Index(int i){
        trainEngIndex = i;
        System.out.println("Train Eng Index set");
    }

    public int get_Train_Eng_Index(){
        return trainEngIndex;
    }
}
