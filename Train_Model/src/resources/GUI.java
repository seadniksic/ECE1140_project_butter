package resources;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import networking.Network;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
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
    final static String asset_Location = "C:\\Users\\sayba\\Documents\\University\\Fall 2020 T5\\ECE 1140\\ECE1140_project_butter\\Train_Model\\src\\assets\\";
    static Font font1;
    static ObservableList<Property> main_data;
    static ObservableList<Property> advanced_data;
    static ObservableList<Property> non_vital_data;
    static ObservableList<Property> attributes_data;
    static Property speed_Prop;
    static Property power_Prop;
    static Property brake_Prop;
    static Property door_Prop;
    static int current_index;
    //static TableView<Property> current_State;



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
        //Set scene and show
        set_Scene(get_Catalogue_Scene(), "Train Catalogue");
    }

    public static void refresh_Table(int id) {
        Train_Model train = Train_Model_Catalogue.trains.get(id);
        System.out.println("GET");
        if (main_data != null) {
            main_data.removeAll();
            DecimalFormat df = new DecimalFormat("0.0000");
            ObservableList<Property> temp = FXCollections.observableArrayList(
                    new Property("Speed", df.format(train.get_Velocity() * 2.23694), "mph"),
                    new Property("Power", train.get_Engine_Power(), "watts"),
                    new Property("Brake", !train.get_Brake_Status() ? "off" : "on", ""),
                    new Property("Emergency Brake", !train.get_Emergency_Brake_Status() ? "off" : "on", "")
            );

            ObservableList<Property> temp2 = FXCollections.observableArrayList(
                    new Property("Grade", train.get_Grade(), "degrees"),
                    new Property("Positive_Force", train.get_Force().get(1), "Newtons"),
                    new Property("Negative Force", train.get_Force().get(2), "Newtons"),
                    new Property("Effective Force", train.get_Force().get(0), "Newtons"),
                    new Property("Max Force", train.get_Force().get(3), "Newtons")
            );

            ObservableList<Property> temp3 = FXCollections.observableArrayList(
                    new Property("Left Doors", !train.get_Left_Door_Status() ? "closed" : "open", ""),
                    new Property("Right Doors", !train.get_Right_Door_Status() ? "closed" : "open", ""),
                    new Property("Internal Lights", train.get_Int_Lights() ? "on" : "off", ""),
                    new Property("External Lights", train.get_Ext_Lights() ? "on" : "off", ""),
                    new Property("Cabin Temperature", train.get_Temperature(), "F"),
                    new Property("Advertisements", train.get_Advertisements(), "")
            );

            ObservableList<Property> temp4 = FXCollections.observableArrayList(
                    new Property("Passengers", train.get_Passengers(), train.get_Passengers() == 1 ? "person" : "people"),
                    new Property("Engine Power Limit", 120, "kilowatts"),
                    new Property("Length", train.get_Num_Cars() * Train_Model.car_Length, "meters"),
                    new Property("Mass", train.get_Num_Cars() * Train_Model.car_Mass, "kg"),
                    new Property("Cars", train.get_Num_Cars(), "cars")

            );
            FXCollections.copy(main_data, temp);
            FXCollections.copy(advanced_data, temp2);
            FXCollections.copy(non_vital_data, temp3);
            FXCollections.copy(attributes_data, temp4);
            System.out.println("---------Update Ran-----------");
        }
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
        VBox layout = new VBox();
        VBox main = new VBox();
        main.setAlignment(Pos.CENTER);
        main.setPadding(new Insets(30,10,10,10));
        main.setSpacing(10);

        Button b = new Button("Connect to Track Model");
        Button c = new Button("Connect to Train Controller");
        Button test = new Button("Send");

//
        Text label = new Text("Train Catalogue");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-font: 24 arial; -fx-padding: 10 10 10 10;");

        Button button = new Button("Spawn Train");


        ListView<String> list = new ListView<String>();
        list.setPlaceholder(new Label("No Trains To Display"));
        list.setItems(Train_Model_Catalogue.name_List);

        Menu menu = new Menu("Connect To Modules");
        MenuItem menuItem1 = new MenuItem("Track Model");
        MenuItem menuItem2 = new MenuItem("Train Controller");

        menu.getItems().add(menuItem1);
        menu.getItems().add(menuItem2);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        EventHandler<ActionEvent> event_tc = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Network.connect_To_Train_Controller();
            }
        };

        EventHandler<ActionEvent> event_tm = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                Network.connect_To_Track_Model();
            }
        };

        menuItem1.setOnAction(event_tm);
        menuItem2.setOnAction(event_tc);


        b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Network.connect_To_Track_Model();
            }
        });
        c.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                Network.connect_To_Train_Controller();
            }
        });

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
                    try {
                        set_Scene(get_Train_Scene(index), "Train View");
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            }
        });

        main.getChildren().addAll(label,button,list);
        layout.getChildren().addAll(menuBar, main);
        Scene scene = new Scene(layout, CS_SCALE_FACTOR_X * screen_X,CS_SCALE_FACTOR_Y * screen_Y);
        return scene;

    }

    public Scene get_Train_Scene(int index) throws FileNotFoundException {
        Font f2 = Font.font("Verdana", 30);
        //Fetch correct train instance and data
        Train_Model train = tc.trains.get(index);
        current_index = index;

        Button test = new Button("Send");
        test.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                for (int i = 0; i < 1000; i++) {
                    System.out.println(i);
                    try {
                        Thread.sleep(500);
                        Network.tm_Interface.outer_Update_Occupancy(1, index, i);
                    } catch (RemoteException | InterruptedException remoteException) {
                        remoteException.printStackTrace();
                    }
                }
            }
        });
        DecimalFormat df = new DecimalFormat("0.00");

        main_data = FXCollections.observableArrayList(
                new Property("Speed", df.format(train.get_Velocity() * 2.23694), "mph"),
                new Property("Power", train.get_Engine_Power(), "watts"),
                new Property("Brake", !train.get_Brake_Status() ? "off" : "on", ""),
                new Property("Emergency Brake", !train.get_Emergency_Brake_Status() ? "off" : "on", "")
        );

        advanced_data = FXCollections.observableArrayList(
                new Property("Grade", train.get_Grade(), "degrees"),
                new Property("Positive_Force", train.get_Force().get(1), "Newtons"),
                new Property("Negative Force", train.get_Force().get(2), "Newtons"),
                new Property("Effective Force", train.get_Force().get(0), "Newtons"),
                new Property("Max Force", train.get_Force().get(3), "Newtons")
        );

        non_vital_data = FXCollections.observableArrayList(
                new Property("Left Doors", !train.get_Left_Door_Status() ? "closed" : "open", ""),
                new Property("Right Doors", !train.get_Right_Door_Status() ? "closed" : "open", ""),
                new Property("Internal Lights", train.get_Int_Lights() ? "on" : "off", ""),
                new Property("External Lights", train.get_Ext_Lights() ? "on" : "off", ""),
                new Property("Cabin Temperature", train.get_Temperature(), "F"),
                new Property("Advertisements", train.get_Advertisements(), "")
        );

        attributes_data = FXCollections.observableArrayList(
                new Property("Passengers", train.get_Passengers(), train.get_Passengers() == 1 ? "person" : "people"),
                new Property("Engine Power Limit", 120, "kilowatts"),
                new Property("Length", train.get_Num_Cars() * Train_Model.car_Length, "meters"),
                new Property("Mass", train.get_Num_Cars() * Train_Model.car_Mass, "kg"),
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
        TextField space2 = new TextField("");

        space2.setVisible(false);
        space2.setMinHeight(100);
//        Image back_Logo = new Image("UIControls/logo.png");
//        ImageView view = new ImageView(img);
        space.setVisible(false);
        space.setMinWidth(1300);

        Button new_Speed = new Button("Submit Speed");
        Text speed = new Text("Speed: "+ train.get_Velocity());
        Text num_Cars = new Text("Number of Cars: "+ train.num_Cars);
        Text power = new Text("Engine Power: "+ train.get_Engine_Power());
        Text title = new Text("Train Model " + train.id);
        //title.setFont(f2);
        title.setStyle("-fx-font: 40 arial");

        Text failure = new Text("Giblets");
        failure.setFill(Color.RED);
        failure.setFont(Font.font("Verdana", 30));
        if (train.get_Failure().equals("None")) {
            failure.setVisible(false);
        } else {
            failure.setText(train.get_Failure() + " Failure");
        }

        Button back_button = new Button("Back");
        back_button.setStyle("-fx-border-radius: 0px");

        Button advanced_info = new Button("Advanced Information");
        Button ebrake = new Button("Emergency brake");
        ebrake.setLayoutX(500);
        ebrake.setLayoutY(500);

        MenuItem singal_failure = new MenuItem("Signal Failure");
        MenuItem brake_failure = new MenuItem("Brake Failure");
        MenuItem engine_failure = new MenuItem("Engine Failure");
        MenuButton destruction_Mode = new MenuButton("Destruction", null, singal_failure, brake_failure, engine_failure);

        //Creating tables for data
        TableView<Property> main_table = new TableView();
        TableView<Property> advanced_table = new TableView();
        TableView<Property> attributes_table = new TableView();
        TableView<Property> non_vital_table = new TableView();
        main_table.setMaxSize(365, 250);
        main_table.setFixedCellSize(40);
        advanced_table.setFixedCellSize(80);
        advanced_table.setMinSize(360, 800);
        non_vital_table.setMaxSize(365, 250);
        non_vital_table.setFixedCellSize(30);
        attributes_table.setMaxSize(365, 250);
        attributes_table.setFixedCellSize(30);

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

        TableColumn name_col4 = new TableColumn("Name");
        TableColumn value_col4 = new TableColumn("Value");
        TableColumn unit_col4 = new TableColumn("Unit");
        name_col4.setCellValueFactory(new PropertyValueFactory<Property, String>("name"));
        value_col4.setCellValueFactory(new PropertyValueFactory<Property, String>("value"));
        unit_col4.setCellValueFactory(new PropertyValueFactory<Property, String>("unit"));
        name_col4.prefWidthProperty().setValue(120);
        value_col4.prefWidthProperty().setValue(120);
        unit_col4.prefWidthProperty().setValue(120);

        //Labeling Tables
        Text main_label = new Text("Vital");
        Text attributes_label = new Text("Attributes");
        Text non_vital_label = new Text("Non-Vital");

        main_label.setStyle("-fx-font: 28 arial;");
        attributes_label.setStyle("-fx-font: 28 arial;");
        non_vital_label.setStyle("-fx-font: 28 arial;");

//        main_label.setPrefSize(120,0);
//        attributes_label.setPrefSize(120,0);
//        non_vital_label.setPrefSize(120,0);

        VBox main_Label_Contatiner = new VBox();
        main_Label_Contatiner.setPadding(new Insets(10, 10, 10, 10));
        VBox attributes_Label_Contatiner = new VBox();
        attributes_Label_Contatiner.setPadding(new Insets(10, 10, 10, 70));
        VBox non_Vital_Label_Container = new VBox();
        non_Vital_Label_Container.setPadding(new Insets(10, 10, 10, 70));

        main_table.setItems(main_data);
        main_table.getColumns().addAll(name_col, value_col, unit_col);

        advanced_table.setItems(advanced_data);
        advanced_table.getColumns().addAll(name_col2, value_col2, unit_col2);

        attributes_table.setItems(attributes_data);
        attributes_table.getColumns().addAll(name_col3, value_col3, unit_col3);

        non_vital_table.setItems(non_vital_data);
        non_vital_table.getColumns().addAll(name_col4, value_col4, unit_col4);

        main_Label_Contatiner.getChildren().addAll(main_label, main_table);
        attributes_Label_Contatiner.getChildren().addAll(attributes_label, attributes_table);
        non_Vital_Label_Container.getChildren().addAll(non_vital_label, non_vital_table);

        main_Label_Contatiner.setAlignment(Pos.CENTER);
        attributes_Label_Contatiner.setAlignment(Pos.CENTER);
        non_Vital_Label_Container.setAlignment(Pos.CENTER);



        center_bottom.getChildren().addAll(attributes_Label_Contatiner, main_Label_Contatiner, non_Vital_Label_Container);
        VBox menu = new VBox();
        //menu.prefHeightProperty().bind(layout.heightProperty());
        menu.setPrefWidth(330);
        menu.setPrefHeight(700);
        menu.getChildren().addAll(advanced_table);
        menu.setTranslateX(-360);
        menu.setTranslateY(-100);
        AtomicBoolean advanced_menu_isopen = new AtomicBoolean(false);
        TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);
        menuTranslation.setFromX(-360);
        menuTranslation.setToX(0);


        //Event handlers
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


        Image e_image;
        ImageView e_brake_image = new ImageView();
        if (!train.get_Emergency_Brake_Status()) {
            try {
                Image temp_img = new Image(new FileInputStream(asset_Location + "lever2.png"));
                e_brake_image.setImage(temp_img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Image temp_img = new Image(new FileInputStream(asset_Location + "lever3.png"));
                e_brake_image.setImage(temp_img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        e_brake_image.setPreserveRatio(true);
        e_brake_image.setFitHeight(100);
        e_brake_image.setFitWidth(100);
        //e_brake_image.setTranslateX(-80);

        e_brake_image.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (!train.get_Emergency_Brake_Status()) {
                    try {
                        Image temp_img = new Image(new FileInputStream(asset_Location + "lever3.png"));
                        e_brake_image.setImage(temp_img);
                        train.set_Emergency_Brake_Status(true);
                    } catch (FileNotFoundException | RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        VBox ebrake_image_container = new VBox();
        Text ebrake_image_label = new Text("Passenger Emergency Brake");
        ebrake_image_container.getChildren().addAll(e_brake_image, ebrake_image_label);
        ebrake_image_container.setAlignment(Pos.CENTER);

        top_Top.getChildren().addAll(back_button, space, ebrake_image_container);
        top_Top.setAlignment(Pos.CENTER_LEFT);

        top_Bottom.getChildren().addAll();
        top_Bottom.setAlignment(Pos.CENTER);

        //bottom_Top.getChildren().addAll(e_brake_image);
        bottom_Top.setAlignment(Pos.CENTER);
//        bottom_Top.setPadding(new Insets(0,0,0,200));
        bottom_Middle.getChildren().add(destruction_Mode);


        bottom_Bottom.getChildren().addAll(advanced_info, new_Speed, textField);

        top.getChildren().addAll(top_Top, top_Middle, top_Bottom);
        bottom.getChildren().addAll(bottom_Top, bottom_Middle, bottom_Bottom);

        //Find and load main image
        Image temp = new Image(new FileInputStream(asset_Location + "train_2.png"));
        ImageView train_im = new ImageView(temp);

        center.getChildren().addAll(title, train_im, center_bottom, failure);
        center.setAlignment(Pos.CENTER);




        center.setTranslateX(-250);
        TranslateTransition centerTranslation = new TranslateTransition(Duration.millis(500), center);
        centerTranslation.setFromX(-250);
        centerTranslation.setToX(110);
        Pane p = new Pane();
        ImageView image = null;
        ImageView finalImage = image;
        advanced_info.setOnMouseClicked(evt -> {
            if (advanced_menu_isopen.get() == false) {
                menuTranslation.setRate(1);
                center.setEffect(new GaussianBlur(6));
                if (finalImage != null) {
                    finalImage.setEffect(new GaussianBlur());
                }
                centerTranslation.setRate(1);
                menuTranslation.play();
                centerTranslation.play();

                advanced_menu_isopen.set(true);

            } else {
                menuTranslation.setRate(-1);
                center.setEffect(null);
                //bottom.setEffect(null);
                centerTranslation.setRate(-1);
                menuTranslation.play();
                centerTranslation.play();
                advanced_menu_isopen.set(false);
            }
        });

        //center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(0,200,0,0));
        layout.setTop(top);
        layout.setCenter(center);
        layout.setBottom(bottom);
        layout.setLeft(menu);

        try {
            Image background = new Image(new FileInputStream(asset_Location + "test_background.png"));
            image = new ImageView(background);
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
    public void tableHeightHelper(TableView<Property> table, int rowHeight, int headerHeight, int margin) {
        table.prefHeightProperty().bind(Bindings.max(2, Bindings.size(table.getItems()))
                .multiply(rowHeight)
                .add(headerHeight)
                .add(margin));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }

    public ImageView get_Train_State(Train_Model t) throws FileNotFoundException {
        Image temp;

        if (!t.get_Int_Lights() && !t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_1.png"));

        } else if (!t.get_Int_Lights() && !t.get_Ext_Lights() && t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_2.png"));

        } else if (!t.get_Int_Lights() && t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_3.png"));

        } else if (!t.get_Int_Lights() && t.get_Ext_Lights() && t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_4.png"));

        } else if (t.get_Int_Lights() && !t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_5.png"));

        } else if (t.get_Int_Lights() && !t.get_Ext_Lights() && t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_6.png"));

        } else if (t.get_Int_Lights() && t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_7.png"));

        } else {
            temp = new Image(new FileInputStream(asset_Location + "train_8.png"));
        }

        ImageView image = new ImageView(temp);
        return image;


    }




    public void change_Train_State(int id) throws FileNotFoundException {
        Image temp;
        Train_Model t = Train_Model_Catalogue.trains.get(id);

        if (!t.get_Int_Lights() && !t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_1.png"));

        } else if (!t.get_Int_Lights() && !t.get_Ext_Lights() && t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_2.png"));

        } else if (!t.get_Int_Lights() && t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_3.png"));

        } else if (!t.get_Int_Lights() && t.get_Ext_Lights() && t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_4.png"));

        } else if (t.get_Int_Lights() && !t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_5.png"));

        } else if (t.get_Int_Lights() && !t.get_Ext_Lights() && t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_6.png"));

        } else if (t.get_Int_Lights() && t.get_Ext_Lights() && !t.get_Left_Door_Status()) {
            temp = new Image(new FileInputStream(asset_Location + "train_7.png"));

        } else {
            temp = new Image(new FileInputStream(asset_Location + "train_8.png"));
        }

        ImageView image = new ImageView(temp);
    }

}
