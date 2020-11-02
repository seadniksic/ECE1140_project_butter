package resources;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.FXPermission;
import networking.Network;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public class GUI extends Application {
    static Stage main_Stage = null;
    static Train_Model_Catalogue tc = null;
    static double screen_X;
    static double screen_Y;
    static double current_X;
    static double current_Y;
    final static double TS_SCALE_FACTOR_X = 0.6;
    final static double TS_SCALE_FACTOR_Y = 0.7;
    final static double CS_SCALE_FACTOR_X = 0.2;
    final static double CS_SCALE_FACTOR_Y = 0.4;
    final static String asset_Location = "C:\\Users\\sayba\\Documents\\University\\Fall 2020 T5\\ECE 1140\\Project\\src\\assets\\";
    static Font font1;

    public static void main(String[] args) throws IOException {
        launch(args);
    }


    @Override
    public void init() throws Exception {
        super.init();
        Font.loadFont("af.tff", 90);
        screen_X = Screen.getPrimary().getBounds().getMaxX();
        screen_Y = Screen.getPrimary().getBounds().getMaxY();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        main_Stage = primaryStage;
        Network.start_Server();
        while (Network.connected_Module_2 != true) {
            try {
                Network.connect_To_Modules();
            } catch (Exception e) {

            }

        }
        //Set scene and show
        set_Scene(get_Catalogue_Scene(), "Train Catalogue");
    }

    public static void set_Scene(Scene scene, String title){
        scene.getStylesheets().add(GUI.class.getResource("style.css").toExternalForm());
        main_Stage.setScene(scene);


        if (title.equals("Train View")) {
            double width = TS_SCALE_FACTOR_X * screen_X;
            double height = TS_SCALE_FACTOR_Y * screen_Y;
            main_Stage.setX((screen_X - width) / 2);
            main_Stage.setY((screen_Y - height) / 2);

        } else if (title.equals("Train Catalogue")) {
            double width = CS_SCALE_FACTOR_X * screen_X;
            double height = CS_SCALE_FACTOR_Y * screen_Y;
            main_Stage.setX((screen_X - width) / 2);
            main_Stage.setY((screen_Y - height) / 2);
        }

        main_Stage.setTitle(title);
        main_Stage.show();
        main_Stage.setResizable(false);
    }


    public Scene get_Catalogue_Scene() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Text label = new Text("Train Catalogue");
        label.setStyle("-fx-font: 24 arial;");

        Button button = new Button("Spawn Train");


        ListView<String> list = new ListView<String>();
        list.setPlaceholder(new Label("No Trains To Display"));
        list.setItems(Train_Model_Catalogue.name_List);


        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                try {
                    Train_Model_Catalogue.create_Model(2);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        });
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                int index = list.getSelectionModel().getSelectedIndex();
                if (index != -1) {
                    set_Scene(get_Train_Scene(index), "Train View");
                }
            }
        });

        layout.getChildren().addAll(label, button, list);
        Scene scene = new Scene(layout,CS_SCALE_FACTOR_X * screen_X,CS_SCALE_FACTOR_Y * screen_Y);
        return scene;

    }

    public Scene get_Train_Scene(int index) {
        Font f1 = Font.loadFont("af.tff", 30);
        System.out.println(f1);
        //Fetch correct train instance
        Train_Model train = tc.trains.get(index);
        //Overarching layout
        BorderPane layout = new BorderPane();
        //Bottom Layout
        VBox bottom = new VBox();
        VBox top = new VBox();
        HBox bottom_Bottom = new HBox();
        HBox bottom_Top = new HBox();
        HBox top_Top = new HBox();
        HBox top_Bottom = new HBox();


        Text speed = new Text("Speed: "+ train.get_Velocity());
        Text num_Cars = new Text("Number of Cars: "+ train.num_Cars);
        Text power = new Text("Engine Power: "+ train.get_Engine_Power());
        Text title = new Text("Train Model");
        //title.setId("Train_Model_Title");
        //title.setStyle("-fx-font-family: Arcade Future Display; -fx-font-size:100");
        //new Font("Verdana", 80)
        //no .getClassLoader
        title.setFont(f1);

        Button back_button = new Button("Back");
        Button speed_Button = new Button("Send Speed");
        ToggleButton train_View = new ToggleButton("Train View");
        ToggleButton destruction_Mode = new ToggleButton("Destruction Mode");
        TableView table = new TableView();



        speed_Button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e)  {
                try {
                    Train_Model_Catalogue.test_Send_Speed_Authority(index, 100000, 1000, 10);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });

        back_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e)  {
                set_Scene(get_Catalogue_Scene(), "Train Catalogue");
            }
        });
        try {
            Image temp = new Image(new FileInputStream(asset_Location + "trans_model_1_real.png"));
            ImageView image = new ImageView(temp);
            layout.setCenter(image);
        }catch (Exception e) {
            System.out.println("File Not Found");
        }

        top_Top.getChildren().add(back_button);
        top_Top.setAlignment(Pos.CENTER_LEFT);
        top_Bottom.getChildren().add(title);
        top_Bottom.setPadding(new Insets(0,0,10,0));
        bottom_Bottom.getChildren().addAll(train_View, destruction_Mode);
        bottom_Bottom.setAlignment(Pos.CENTER);
        bottom_Top.getChildren().add(speed_Button);

        top.getChildren().addAll(top_Top, top_Bottom);
        bottom.getChildren().addAll(bottom_Bottom, bottom_Top);

        layout.setTop(top);
        layout.setBottom(bottom);

        return new Scene(layout, TS_SCALE_FACTOR_X * screen_X, TS_SCALE_FACTOR_Y * screen_Y);
    }



}
