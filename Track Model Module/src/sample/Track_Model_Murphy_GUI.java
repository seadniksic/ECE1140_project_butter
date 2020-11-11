package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Track_Model_Murphy_GUI {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Stage murphy_Stage;
    public Scene murphy_Scene;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Track_Model_Murphy_GUI(){
        open_Murphy_Window();
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    //*******************************************************************************************************************************************************
    // Opens the window where the user can play as Murphy                                                                                                   *
    //*******************************************************************************************************************************************************
    public void open_Murphy_Window(){
        // Configure labels in window
        Label description_Label = new Label("Hello, Murphy!");
        VBox everything_Holder = new VBox();
        everything_Holder.getChildren().addAll(description_Label);
        everything_Holder.setAlignment(Pos.CENTER);
        everything_Holder.setSpacing(20);
        // Configure scene, stage and show
        murphy_Scene = new Scene(everything_Holder, 500, 200);
        murphy_Stage = new Stage();
        murphy_Stage.setTitle("Murphy");
        murphy_Stage.setScene(murphy_Scene);
        murphy_Stage.show();
    }
}
