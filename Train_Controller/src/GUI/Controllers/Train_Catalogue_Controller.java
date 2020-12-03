package GUI.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import Back_End.*;
import GUI.Controllers.*;
import GUI.*;



public class Train_Catalogue_Controller{
    @FXML
    public ListView tc_List;

    public homeController homeCont = new homeController();

    public void initialize(){
        // set the ListView to the observable lists
       tc_List.setItems(Train_Controller_Catalogue.tc_Name_List);
    }

    public void tc_Selected(MouseEvent mouseEvent) throws Exception {

        // Sets home controller index equal to the train controller catalogue selected by the user
        //Print statements for troubleshooting
        int index = tc_List.getSelectionModel().getSelectedIndex();
        System.out.println(index);
        homeCont.set_Index_Home(index); // lauches the home UI of the index that was selected
        System.out.println("home controller index:" + homeCont.get_Index_Home());



       FXMLLoader homeLoad = new FXMLLoader(getClass().getResource("/GUI/FXML/home.fxml"));
       homeLoad.setController((homeCont)); // manually load controller instance
       try{
           Parent homeRoot = homeLoad.load();
           Main_GUI.launch_UI(homeRoot, "Home TC " + Integer.toString(index));
       } catch (Exception e){
           System.out.println("The Try/Catch statement to load the FXML file failed");
           e.printStackTrace();
       }

    }


}
