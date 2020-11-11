package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Track_Model_Builder_GUI {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Stage builder_Stage;
    public Scene builder_Scene;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Track_Model_Builder_GUI(){
        open_Builder_Window();
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    private void saveTextToFile(String param_String, File param_File){
        try{
            PrintWriter this_printWriter = new PrintWriter(param_File);
            this_printWriter.println(param_String);
            this_printWriter.close();
        }catch(IOException e){
            System.out.println("Darn");
        }
    }
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------

    //*******************************************************************************************************************************************************
    // Opens the window where the user can edit the track                                                                                                   *
    //*******************************************************************************************************************************************************
    public void open_Builder_Window(){
        // Configure objects in window
        MenuBar this_MenuBar = new MenuBar();
        Menu file_Menu = new Menu("File");
        MenuItem new_Track_MI = new MenuItem("New Track");
        new_Track_MI.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                FileChooser this_fileChooser = new FileChooser();
                this_fileChooser.setTitle("New Track");
                this_fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", ".txt"));
                File this_file = this_fileChooser.showSaveDialog(builder_Stage); //TODO: Look at this first in case if issues
                if(this_file != null){
                    saveTextToFile("Hello", this_file);
                }
            }
        });
        file_Menu.getItems().add(new_Track_MI);
        this_MenuBar.getMenus().add(file_Menu);

        Label description_label = new Label("Welcome to the track builder!");

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(this_MenuBar, description_label);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        // Configure scene, stage and show
        builder_Scene = new Scene(this_VBox, 500, 200);
        builder_Stage = new Stage();
        builder_Stage.setTitle("Track Builder");
        builder_Stage.setScene(builder_Scene);
        builder_Stage.show();

    }
}

// SAFEKEEPING
//        StackPane this_StackPane = new StackPane();
//        this_StackPane.getChildren().addAll(this_MenuBar, this_VBox);
//        this_StackPane.setAlignment(this_MenuBar, Pos.TOP_CENTER);
