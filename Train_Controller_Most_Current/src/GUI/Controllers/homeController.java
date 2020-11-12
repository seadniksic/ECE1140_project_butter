package GUI.Controllers;

import GUI.Main_GUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class homeController {
    //public Label carLabel;
    private int index;

    //controller objects for driver and train engineer
    public driverController driverCont = new driverController();
    public trainEngineerController trainEngCont = new trainEngineerController();

    public homeController(){
        // I was getting an error for not having a default constructor, even though it was not being used
        // So don't delete it even though it isn't being used
        System.out.println("Default Constructor");
        System.out.println("index in base constructor: " + index);
    }

  public homeController(int i){
      index = i;
      System.out.println("in the home controller constructor");
      System.out.println("Index: " + index);
      /*try {
          System.out.println("try statement in constructor with parameter");
          launch_Home(index);
      } catch (Exception e){
          e.printStackTrace();
      }*/

     try{
         Parent homeRoot = FXMLLoader.load(getClass().getResource("/GUI/FXML/home.fxml"));
         System.out.println("loading complete");
         Scene home = new Scene(homeRoot);
         Stage homeStage = new Stage();

         homeStage.setScene(home);
         homeStage.show();
     } catch (Exception e){
         System.out.println("there is an error in the way that the UI is loading");
         e.printStackTrace();
     }
     //this.index = i;
    }

    public void onClickEvent(MouseEvent mouseEvent) {
        System.out.println("Anchor button has been clicked");
    }

    public void b1ButtonClicked(MouseEvent mouseEvent) throws Exception {

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
       /* try {

            //System.out.println("Driver button has been clicked");
            // Opens up the Driver UI from button press on the Home UI

            System.out.println("Index in DriverLoad: " + index);

            Parent driverRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/FXML/driver.fxml"));
            Scene driver = new Scene(driverRoot);
            Stage driverStage = new Stage();
            driverStage.setTitle("Driver UI");

            driverStage.setScene(driver);
            driverStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }*/
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

            //System.out.println("Driver button has been clicked");
            // Opens up the Driver UI from button press on the Home UI

            /*System.out.println("Index in TELoad: " + this.index);

            Parent trainEngineerRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/FXML/Train_Engineer.FXML"));
            Scene trainEngineer = new Scene(trainEngineerRoot);
            Stage trainEngineerStage = new Stage();
            trainEngineerStage.setTitle("Train Engineer UI");
            //trainEngineerController.carLabel.setText(Integer.toString(Main_GUI.train_Cat_1.get_Train_Controller(index).get_Number_Of_Cars()));

            trainEngineerStage.setScene(trainEngineer);
            trainEngineerStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        }

    public void launch_Home(int i) throws Exception{
      // opens up the home UI
        index = i;
        System.out.println("Entered launch home");
        //Parent homeRoot = FXMLLoader.load(getClass().getResource("GUI/FXML/home.fxml"));
        Parent homeRoot = FXMLLoader.load(getClass().getResource("/GUI/FXML/home.fxml"));
        System.out.println("load");
        Scene homeScene = new Scene(homeRoot);
        System.out.println("set scene");
        Stage homeStage = new Stage();
        System.out.println("setStage");
        homeStage.setTitle("Train Controller Home");

        homeStage.setScene(homeScene);
        System.out.println("set the scene to the stage");
        homeStage.show();
    }

    private void launch_Train_Engineer(int i) throws Exception{
            // Opens up the Driver UI from button press on the Home UI


        Parent trainEngineerRoot = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/FXML/Train_Engineer.FXML"));
        Scene trainEngineer = new Scene(trainEngineerRoot);
        Stage trainEngineerStage = new Stage();
        trainEngineerStage.setTitle("Train Engineer UI");



        trainEngineerStage.setScene(trainEngineer);
        trainEngineerStage.show();

    }

    public void set_Index_Home(int j){
        index = j;
    }

    public int get_Index_Home(){
        return index;
    }

}
