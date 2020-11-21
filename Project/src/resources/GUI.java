package resources;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import networking.Network;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    final static String asset_Location = "C:\\Users\\sayba\\Documents\\University\\Fall 2020 T5\\ECE 1140\\ECE1140_project_butter\\Project\\src\\assets\\";
    static Font font1;
    static ObservableList<Property> main_data;
    static ObservableList<Property> advanced_data;
    static Property speed_Prop;
    static Property power_Prop;
    static Property brake_Prop;
    static Property door_Prop;
    static int current_index;
    static TableView<Property> current_State;



    public static void main(String[] args) throws IOException {
        launch(args);
    }


    @Override
    public void init() throws Exception {
        super.init();
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

    public static void refresh_Table(int id) {
        Train_Model train = Train_Model_Catalogue.trains.get(id);
        main_data.removeAll();
        ObservableList<Property> temp = FXCollections.observableArrayList(
                new Property("Speed", train.get_Velocity(), "m/s"),
                new Property("Power", train.get_Engine_Power(), "watts"),
                new Property("Brake", !train.get_Brake_Status() ? "off" : "on", ""),
                new Property("Left Doors", !train.get_Left_Door_Status() ? "closed" : "open", ""),
                new Property("Right Doors", !train.get_Right_Door_Status() ? "closed" : "open", "")
        );
        FXCollections.copy(main_data, temp);
        System.out.println("---------Update Ran-----------");
    }

    public static void set_Scene(Scene scene, String title){
        //scene.getStylesheets().add("style.css");
        main_Stage.setScene(scene);


        if (title.equals("Train View")) {
            double width = TS_SCALE_FACTOR_X * screen_X;
            double height = TS_SCALE_FACTOR_Y * screen_Y;
            current_X = (screen_X - width) / 2;
            current_Y = (screen_Y - height) / 2;
            main_Stage.setX(current_X);
            main_Stage.setY(current_Y);

        } else if (title.equals("Train Catalogue")) {
            double width = CS_SCALE_FACTOR_X * screen_X;
            double height = CS_SCALE_FACTOR_Y * screen_Y;
            current_X = (screen_X - width) / 2;
            current_Y = (screen_Y - height) / 2;
            main_Stage.setX(current_X);
            main_Stage.setY(current_Y);
        }

        main_Stage.setTitle(title);
        main_Stage.show();
        main_Stage.setResizable(false);
    }


    public Scene get_Catalogue_Scene() {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
//
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
        Font f2 = Font.font("Verdana", 30);
        //Fetch correct train instance and data
        Train_Model train = tc.trains.get(index);
        current_index = index;

        main_data = FXCollections.observableArrayList(
                new Property("Speed", train.get_Velocity(), "m/s"),
                new Property("Power", train.get_Engine_Power(), "watts"),
                new Property("Brake", !train.get_Brake_Status() ? "off" : "on", ""),
                new Property("Left Doors", !train.get_Left_Door_Status() ? "closed" : "open", ""),
                new Property("Right Doors", !train.get_Right_Door_Status() ? "closed" : "open", "")
        );

        advanced_data = FXCollections.observableArrayList(
                new Property("Passengers", train.get_Passengers(), train.get_Passengers() == 1 ? "person" : "people"),
                new Property("Grade", train.get_Grade(), "degrees"),
                new Property("Cabin Temperature", train.get_Temperature(), "F"),
                new Property("Engine Power Limit", 100000, "watts"),
                new Property("Length", train.get_Num_Cars() * Train_Model.car_Length, "meters"),
                new Property("Mass", train.get_Num_Cars() * Train_Model.car_Mass, "watts"),
                new Property("Cars", train.get_Num_Cars(), "cars")
        );

        //Overarching layout
        BorderPane layout = new BorderPane();
        //More granular layout
        VBox bottom = new VBox();
        VBox center = new VBox();
        VBox top = new VBox();
        HBox bottom_Bottom = new HBox();
        HBox bottom_Middle = new HBox();
        HBox bottom_Top = new HBox();
        HBox top_Top = new HBox();
        HBox top_Middle = new HBox();
        HBox top_Bottom = new HBox();
        HBox center_bottom = new HBox();

        TextField textField = new TextField("");
        TextField space = new TextField("");
        space.setVisible(false);
        space.setMinWidth(1300);
        Button new_Speed = new Button("Submit Speed");
        Text speed = new Text("Speed: "+ train.get_Velocity());
        Text num_Cars = new Text("Number of Cars: "+ train.num_Cars);
        Text power = new Text("Engine Power: "+ train.get_Engine_Power());
        Text title = new Text("Train Model " + train.id);
        title.setFont(f2);

        Text failure = new Text("Giblets");
        failure.setFill(Color.RED);
        failure.setFont(Font.font("Verdana", 30));
        if (train.get_Failure().equals("None")) {
            failure.setVisible(false);
        } else {
            failure.setText(train.get_Failure() + " Failure");
        }

        Button back_button = new Button("Back");
        Button advanced_info = new Button("Advanced Information");
        Button ebrake = new Button("Emergency brake");
        ebrake.setLayoutX(500);
        ebrake.setLayoutY(500);

        MenuItem singal_failure = new MenuItem("Signal Failure");
        MenuItem brake_failure = new MenuItem("Brake Failure");
        MenuItem engine_failure = new MenuItem("Engine Failure");
        MenuButton destruction_Mode = new MenuButton("Destruction", null, singal_failure, brake_failure, engine_failure);

        //Creating tables for data
        current_State = new TableView();
        TableView advanced_table = new TableView();
        TableView attributes = new TableView();
        current_State.setMaxSize(365, 200);
        current_State.setFixedCellSize(40);
        advanced_table.setFixedCellSize(80);
        advanced_table.setMinHeight(900);

        //Creating tables for displaying data
        TableColumn name_col = new TableColumn("Name");
        TableColumn value_col = new TableColumn("Value");
        TableColumn unit_col = new TableColumn("Unit");
        name_col.setCellValueFactory(new PropertyValueFactory<Property, String>("name"));
        value_col.setCellValueFactory(new PropertyValueFactory<Property, String>("value"));
        unit_col.setCellValueFactory(new PropertyValueFactory<Property, String>("unit"));
        name_col.prefWidthProperty().setValue(120);
        value_col.prefWidthProperty().setValue(120);
        unit_col.prefWidthProperty().setValue(120);

        TableColumn name_col2 = new TableColumn("Name");
        TableColumn value_col2 = new TableColumn("Value");
        TableColumn unit_col2 = new TableColumn("Unit");
        name_col2.setCellValueFactory(new PropertyValueFactory<Property, String>("name"));
        value_col2.setCellValueFactory(new PropertyValueFactory<Property, String>("value"));
        unit_col2.setCellValueFactory(new PropertyValueFactory<Property, String>("unit"));
        name_col2.prefWidthProperty().setValue(120);
        value_col2.prefWidthProperty().setValue(120);
        unit_col2.prefWidthProperty().setValue(120);

        TableColumn name_col3 = new TableColumn("Name");
        TableColumn value_col3 = new TableColumn("Value");
        TableColumn unit_col3 = new TableColumn("Unit");
        name_col3.setCellValueFactory(new PropertyValueFactory<Property, String>("name"));
        value_col3.setCellValueFactory(new PropertyValueFactory<Property, String>("value"));
        unit_col3.setCellValueFactory(new PropertyValueFactory<Property, String>("unit"));
        name_col3.prefWidthProperty().setValue(120);
        value_col3.prefWidthProperty().setValue(120);
        unit_col3.prefWidthProperty().setValue(120);

        //Labeling Tables
        Label current_state_label = new Label("Current State");
        Label attributes_label = new Label("Attributes");
        VBox current_State_Label_Contatiner = new VBox();
        VBox attributes_Label_Contatiner = new VBox();

        current_State.setItems(main_data);
        current_State.getColumns().addAll(name_col, value_col, unit_col);

        //attributes.setItems();

        advanced_table.setItems(advanced_data);
        advanced_table.getColumns().addAll(name_col2, value_col2, unit_col2);

        current_State_Label_Contatiner.getChildren().addAll(current_state_label, current_State);
        attributes_Label_Contatiner.getChildren().addAll(attributes_label);


        center_bottom.getChildren().addAll(current_State);
        VBox menu = new VBox();
        //menu.prefHeightProperty().bind(layout.heightProperty());
        menu.setPrefWidth(360);
        menu.setPrefHeight(700);
        menu.getChildren().addAll(advanced_table);
        menu.setTranslateX(-360);
        AtomicBoolean advanced_menu_isopen = new AtomicBoolean(false);
        TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);
        menuTranslation.setFromX(-360);
        menuTranslation.setToX(0);


        //Event handlers
        advanced_info.setOnMouseClicked(evt -> {
            if (advanced_menu_isopen.get() == false) {
                menuTranslation.setRate(1);
                menuTranslation.play();
                advanced_menu_isopen.set(true);
            } else {
                menuTranslation.setRate(-1);
                menuTranslation.play();
                advanced_menu_isopen.set(false);
            }
        });

        ebrake.setOnMouseClicked(evt -> {
            try {
                train.set_Emergency_Brake_Status(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        new_Speed.setOnMouseClicked(evt -> {
            String text = textField.getText();
            try {
                Train_Model_Catalogue.test_Send_Speed_Authority(index, Integer.parseInt(text), 10000, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        singal_failure.setOnAction(event -> {
            try {
                train.set_Failure("Signal");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            failure.setText("!Signal Failure");
            failure.setVisible(true);
        });
        brake_failure.setOnAction(event -> {
            try {
                train.set_Failure("Brake");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            failure.setText("!Brake Failure");
            failure.setVisible(true);
        });
        engine_failure.setOnAction(event -> {
            try {
                train.set_Failure("Engine");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            failure.setText("!Engine Failure");
            failure.setVisible(true);
        });

        back_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e)  {
                set_Scene(get_Catalogue_Scene(), "Train Catalogue");
            }
        });

        top_Top.getChildren().addAll(back_button, space, ebrake);
        top_Top.setAlignment(Pos.CENTER_LEFT);

        //top_Bottom.getChildren().addAll(main_Table);
        top_Bottom.setAlignment(Pos.CENTER);

        bottom_Top.getChildren().addAll(destruction_Mode);
        bottom_Top.setAlignment(Pos.CENTER);
//        bottom_Top.setPadding(new Insets(0,0,0,200));

        bottom_Bottom.getChildren().addAll(new_Speed, textField, advanced_info);

        top.getChildren().addAll(top_Top, top_Middle, top_Bottom);
        bottom.getChildren().addAll(bottom_Bottom, bottom_Middle, bottom_Top);

        //Find and load main image
        try {
            Image temp = new Image(new FileInputStream(asset_Location + "trans_model_1_real.png"));
            ImageView image = new ImageView(temp);
            center.getChildren().addAll(title, image, center_bottom, failure);
            center.setAlignment(Pos.CENTER);
        }catch (Exception e) {
            System.out.println("File Not Found");
            center.getChildren().addAll(failure, title, center_bottom);
        }



        center.setTranslateX(-250);
        TranslateTransition centerTranslation = new TranslateTransition(Duration.millis(500), center);
        centerTranslation.setFromX(-250);
        centerTranslation.setToX(110);

        advanced_info.setOnMouseClicked(evt -> {
            if (advanced_menu_isopen.get() == false) {
                menuTranslation.setRate(1);
                centerTranslation.setRate(1);
                menuTranslation.play();
                centerTranslation.play();

                advanced_menu_isopen.set(true);

            } else {
                menuTranslation.setRate(-1);
                centerTranslation.setRate(-1);
                menuTranslation.play();
                centerTranslation.play();
                advanced_menu_isopen.set(false);
            }
        });

        //center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(0,200,0,0));

        Pane p = new Pane();

        layout.setTop(top);
        layout.setCenter(center);
        layout.setBottom(bottom);
        layout.setLeft(menu);

        try {
            Image background = new Image(new FileInputStream(asset_Location + "test_background.png"));
            ImageView image = new ImageView(background);
            p.getChildren().addAll(image, layout);
        }catch (Exception e) {
            System.out.println("File Not Found");
            p.getChildren().add(layout);
        }

        return new Scene(p, TS_SCALE_FACTOR_X * screen_X, TS_SCALE_FACTOR_Y * screen_Y);
    }

    public static class Property {
        public String name;
        public Object value;
        public String unit;

        Property(String name, Object value, String unit) {
            this.name = name;
            this.value = value;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }
        public Object getValue() {
            return value;
        }
        public String getUnit() {
            return unit;
        }

        public void setValue(Object value_In) {
            Object last_Value = value;
            value = value_In;


        }

    }

}
