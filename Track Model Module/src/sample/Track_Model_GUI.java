package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Track_Model_GUI {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Stage start_Stage;
    public Scene start_Scene;
    public Track_Model_Builder_GUI this_TMBGUI;
    public Track_Model_Murphy_GUI this_TMMGUI;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Track_Model_GUI(){
        open_Start_Window();
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------

    private void map_Builder_Button(Button paramButton) {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(this_TMBGUI == null) {
                    this_TMBGUI = new Track_Model_Builder_GUI();
                }
                this_TMBGUI.swap_to_Start_Scene();
            }
        };
        paramButton.setOnAction(event);
    }

    private void map_Murphy_Button(Button paramButton) {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(this_TMMGUI == null || !this_TMMGUI.murphy_Stage.isShowing()) {
                    this_TMMGUI = new Track_Model_Murphy_GUI();
                }
                this_TMMGUI.swap_to_Start_Scene();
            }
        };
        paramButton.setOnAction(event);
    }

    private void map_Connect_To_Track_Controller_Button(Button paramButton) {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Network.connect_To_Modules();
                    if(Network.connected_Module_1){
                        System.out.println("Connected to Tony");
                    }
                } catch (Exception e) {
                }
            }
        };
        paramButton.setOnAction(event);
    }

    private void map_Connect_To_Train_Model_Button(Button paramButton) {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                while (!Network.connected_Module_2) {
                    try {
                        Network.connect_To_Modules();
                        if(Network.connected_Module_2){
                            System.out.println("Connected to Sead");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        };
        paramButton.setOnAction(event);
    }

    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------

    public void open_Start_Window(){
        // Configure buttons and labels in window
        Label intro_Label = new Label("Choose user");
        Button builder_Button = new Button("Builder");
        map_Builder_Button(builder_Button);

        Button murphy_Button = new Button("Murphy");
        map_Murphy_Button(murphy_Button);

        Button connect_To_Track_Controller_Button = new Button("Connect To Track Controller");
        map_Connect_To_Track_Controller_Button(connect_To_Track_Controller_Button);

        Button connect_To_Train_Model_Button = new Button("Connect To Train Model");
        map_Connect_To_Train_Model_Button(connect_To_Train_Model_Button);

        HBox button_Holder = new HBox();
        button_Holder.getChildren().addAll(builder_Button, murphy_Button, connect_To_Track_Controller_Button, connect_To_Train_Model_Button);
        button_Holder.setAlignment(Pos.CENTER);
        button_Holder.setSpacing(20);
        VBox everything_Holder = new VBox();
        everything_Holder.getChildren().addAll(intro_Label, button_Holder);
        everything_Holder.setAlignment(Pos.CENTER);
        everything_Holder.setSpacing(20);
        // Configure scene, stage and show
        start_Scene = new Scene(everything_Holder, 500, 200);
        start_Stage = new Stage();
        start_Stage.setTitle("Track Model");
        start_Stage.setScene(start_Scene);
        start_Stage.show();
    }


}
