package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import sample.Network;
import sample.OverallSoftware;
import sample.TrackController;


import java.io.FileNotFoundException;
import java.rmi.RemoteException;


public class trackControllerUI {

    VBox root = new VBox(); //Blocks
    VBox root2 = new VBox(); //Open
    VBox root3 = new VBox(); //Faults
    VBox root4 = new VBox(); //Lights

    @FXML
    ScrollPane blockBox;

    @FXML
    ScrollPane blockBox2;

    @FXML
    ScrollPane lightBox;

    @FXML
    ScrollPane faultBox;

    @FXML
    Label tabLabel;

    @FXML
    Label fileLabel;

    @FXML
    Label switchLabel;

    @FXML
    Label xLabel;

    @FXML
    Rectangle xBox;

    @FXML
    Pane trackPane;

    @FXML
    Pane signalPane;

    @FXML
    Pane faultPane;

    @FXML
    Pane plcPane;

    @FXML
    Pane debugPane;

    @FXML
    TextField plcField;

    @FXML
    TextField trainMoveField;

    @FXML
    TextField openField;

    @FXML
    TextField closeField;

    @FXML
    TextField manualField;

    @FXML
    TextField faultField;

    @FXML
    RadioButton defaultRadio;

    @FXML
    RadioButton otherRadio;

    @FXML
    RadioButton brRadio;

    @FXML
    RadioButton tcfRadio;

    @FXML
    RadioButton powerRadio;

    @FXML
    RadioButton clearRadio;

    ToggleGroup tg = new ToggleGroup();
    ToggleGroup tg2 = new ToggleGroup();

    public static TrackController con = new TrackController();
    Label[] blocks = new Label[con.get_Block_Amount()];
    Label[] open = new Label[con.get_Block_Amount()];
    Label[] faults = new Label[con.get_Block_Amount()];
    Label[] lights = new Label[con.get_Block_Amount()];
    Rotate rotate = new Rotate();
    boolean switchState = false;
    boolean brState = false;
    boolean tcfState = false;
    boolean powerState = false;
    Alert alert;

    @FXML
    void initialize () {
        alert = new Alert(Alert.AlertType.NONE);
        tabLabel.setText("PLC");
        tabLabel.setStyle("-fx-font-size: 20px");
        trackPane.setVisible(false);
        signalPane.setVisible(false);
        faultPane.setVisible(false);
        plcPane.setVisible(true);
        debugPane.setVisible(false);
        fileLabel.setText("Current File: " + con.get_File());
        xLabel.setText("NO RAILROAD CROSSING");
        load_Info();

        plcField.setText(con.get_Track_Line() + con.get_Controller_Num() + ".txt");

        defaultRadio.setToggleGroup(tg);
        otherRadio.setToggleGroup(tg);

        brRadio.setToggleGroup(tg2);
        tcfRadio.setToggleGroup(tg2);
        powerRadio.setToggleGroup(tg2);
        clearRadio.setToggleGroup(tg2);

        rotate.setAngle(130);
        rotate.setPivotX(100);

        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText("No PLC file in Track Controller");
    }

    public void load_Info() {

        for (int i = 0; i < con.get_Block_Amount(); i++) {
            Label block = new Label("BLOCK");
            Label blockOpen = new Label("BLOCK");
            Label fault = new Label("Block " + con.get_Block_ID(i) + ": " + con.get_Broken_Rail(i)  + ", " + con.get_Track_Fail(i) + ", " +con.get_Power_Fail(i));
            Label light = new Label(con.get_Light_State(i));

            if (con.get_Block_State(i)) {
                block.setText("Block " + con.get_Block_ID(i) + " OCCUPIED");
            } else {
                block.setText("Block " + con.get_Block_ID(i) + " CLEAR");
            }

            if (con.get_Block_Status(i)) {
                blockOpen.setText("Block " + con.get_Block_ID(i) + " OPEN");
            } else {
                blockOpen.setText("Block " + con.get_Block_ID(i) + " CLOSED FOR MAINTENANCE");
            }

            block.setStyle("-fx-font-size: 20px");
            blockOpen.setStyle("-fx-font-size: 20px");
            fault.setStyle("-fx-font-size: 20px");
            light.setStyle("-fx-font-size: 20px");

            blocks[i] = block;
            open[i] = blockOpen;
            faults[i] = fault;
            lights[i] = light;
        }

        if (!con.get_Switch_State()) {
            if (con.get_Switch_Beta() == 0) {
                switchLabel.setText("Block " + con.get_Switch_ID() + " is connected to YARD");
            } else {
                switchLabel.setText("Block " + con.get_Switch_ID() + " is connected to " + con.get_Switch_Beta());
            }
        } else {
            switchLabel.setText("Block " + con.get_Switch_ID() + " is connected to " + con.get_Switch_Gamma());
        }
        switchLabel.setStyle("-fx-font-size: 20px");

        root.getChildren().addAll(blocks);
        root2.getChildren().addAll(open);
        root3.getChildren().addAll(faults);
        root4.getChildren().addAll(lights);

        blockBox.setContent(root);
        blockBox2.setContent(root2);
        faultBox.setContent(root3);
        lightBox.setContent(root4);

        if (con.get_XBar_ID() > 0) {
            xLabel.setText("RAILROAD CROSSING AT " + con.get_XBar_ID());
        }

        xBox.getTransforms().setAll(rotate);
    }

    public void load_Data(MouseEvent mouseEvent) {
        for (int i = 0; i < con.get_Block_Amount(); i++) {

            if (con.get_Block_State(i)) {
                blocks[i].setText("Block " + con.get_Block_ID(i) + " OCCUPIED");
                blocks[i].setTextFill(Color.RED);
            } else {
                blocks[i].setText("Block " + con.get_Block_ID(i) + " CLEAR");
                blocks[i].setTextFill(Color.GREEN);
            }

            if (con.get_Block_Status(i)) {
                open[i].setText("Block " + con.get_Block_ID(i) + " OPEN");
            } else {
                open[i].setText("Block " + con.get_Block_ID(i) + " CLOSED FOR MAINTENANCE");
            }

            lights[i].setText("Block " + con.get_Block_ID(i) + ": " + con.get_Light_State(i));
            faults[i].setText("Block " + con.get_Block_ID(i) + ": " + con.get_Broken_Rail(i)  + ", " + con.get_Track_Fail(i) + ", " +con.get_Power_Fail(i));
        }

        if (!con.get_Switch_State()) {
            if (con.get_Switch_Beta() == 0) {
                switchLabel.setText("Block " + con.get_Switch_ID() + " is connected to YARD");
            } else {
                switchLabel.setText("Block " + con.get_Switch_ID() + " is connected to " + con.get_Switch_Beta());
            }
        } else {
            switchLabel.setText("Block " + con.get_Switch_ID() + " is connected to " + con.get_Switch_Gamma());
        }

        if (con.get_XBar_ID() > 0) {
            xLabel.setText("RAILROAD CROSSING AT " + con.get_XBar_ID());

            if (con.get_XBar_State()) {
                rotate.setAngle(0);
            } else {
                rotate.setAngle(130);
            }

            xBox.getTransforms().setAll(rotate);
        }
    }

    //PANES
    public void set_Track_View(ActionEvent actionEvent) {
        if (!con.get_File().equalsIgnoreCase("no file")) {
            tabLabel.setText("Track View");
            trackPane.setVisible(true);
            signalPane.setVisible(false);
            faultPane.setVisible(false);
            plcPane.setVisible(false);
            debugPane.setVisible(false);
        } else {
            alert.show();
        }
    }

    public void set_Signal_View(ActionEvent actionEvent) {
        if (!con.get_File().equalsIgnoreCase("no file")) {
            tabLabel.setText("Signal Control");
            trackPane.setVisible(false);
            signalPane.setVisible(true);
            faultPane.setVisible(false);
            plcPane.setVisible(false);
            debugPane.setVisible(false);
        } else {
            alert.show();
        }
    }

    public void set_Fault_View(ActionEvent actionEvent) {
        if (!con.get_File().equalsIgnoreCase("no file")) {
            tabLabel.setText("Fault History");
            trackPane.setVisible(false);
            signalPane.setVisible(false);
            faultPane.setVisible(true);
            plcPane.setVisible(false);
            debugPane.setVisible(false);
        } else {
            alert.show();
        }
    }

    public void set_PLC_View(ActionEvent actionEvent) {
        tabLabel.setText("PLC");
        trackPane.setVisible(false);
        signalPane.setVisible(false);
        faultPane.setVisible(false);
        plcPane.setVisible(true);
        debugPane.setVisible(false);
    }


    public void debug(ActionEvent actionEvent) throws FileNotFoundException {
        if (!con.get_File().equalsIgnoreCase("no file")) {
            trackPane.setVisible(false);
            signalPane.setVisible(false);
            faultPane.setVisible(false);
            plcPane.setVisible(false);
            debugPane.setVisible(true);
        } else {
            alert.show();
        }
    }
    //PANES

    public void upload_PLC(ActionEvent actionEvent) throws FileNotFoundException {
        String fileText = plcField.getText();
        if (fileText.equalsIgnoreCase("")) {
            System.out.println("PLC NOT SET");
            System.out.println("ENTER A VALID PATH");
        } else {
            con.set_PLC_Program(plcField.getText());
            System.out.println("PLC SET");
        }

        fileLabel.setText("Current File: " + con.get_File());
    }

    //DEBUG BUTTONS
    public void move_Train(ActionEvent actionEvent) throws FileNotFoundException, RemoteException, InterruptedException {
        String moveBlock = trainMoveField.getText();
        Network.serverObject.train_Moved(0, Integer.parseInt(moveBlock));
    }

    public void reset_Blocks(ActionEvent actionEvent) {
        con.reset_Blocks();
    }

    public void close_Block(ActionEvent actionEvent) throws RemoteException {
        String block = closeField.getText();
        Network.serverObject.close_Block(con.get_Track_Line(), Integer.parseInt(block));
        //con.set_Block_Closed(Integer.parseInt(block));
    }

    public void open_Block(ActionEvent actionEvent) throws RemoteException {
        String block = openField.getText();
        Network.serverObject.open_Block(con.get_Track_Line(), Integer.parseInt(block));
        //con.set_Block_Open(Integer.parseInt(block));
    }

    public void manual_Switch(ActionEvent actionEvent) throws RemoteException {
        String switchBlock = manualField.getText();
        Network.serverObject.set_Switch_Manual(con.get_Track_Line(), Integer.parseInt(switchBlock));
    }

    public void set_Default(ActionEvent actionEvent) {
        if (defaultRadio.isSelected()) {
            System.out.println("Switch will be on default");
            switchState = true;
        }
    }

    public void set_Other(ActionEvent actionEvent) {
        if (otherRadio.isSelected()) {
            System.out.println("Switch will be on other");
            switchState = false;
        }
    }

    public void set_BR(ActionEvent actionEvent) {
        if (brRadio.isSelected()) {
            System.out.println("Broken Rail");
            brState = true;
        }
    }

    public void set_TCF(ActionEvent actionEvent) {
        if (tcfRadio.isSelected()) {
            System.out.println("Track Circuit Failure");
            tcfState = true;
        }
    }

    public void set_PF(ActionEvent actionEvent) {
        if (powerRadio.isSelected()) {
            System.out.println("Power Failure");
            powerState = true;
        }
    }

    public void set_Clear(ActionEvent actionEvent) {
        if (clearRadio.isSelected()) {
            System.out.println("No Failures");
            brState = false;
            tcfState = false;
            powerState = false;
        }
    }

    public void set_Fail(ActionEvent actionEvent) throws RemoteException {
        String failBlock = faultField.getText();
        int lineIndex = 0 ;
        if (brRadio.isSelected()) {
            Network.serverObject.set_Broken_Rail(lineIndex, Integer.parseInt(failBlock), brState);
        }

        if (tcfRadio.isSelected()) {
            Network.serverObject.set_Track_Circuit_Failure(lineIndex, Integer.parseInt(failBlock), tcfState);
        }

        if (powerRadio.isSelected()) {
            Network.serverObject.set_Power_Fail(lineIndex, Integer.parseInt(failBlock), powerState);
        }

        if (clearRadio.isSelected()) {
            Network.serverObject.set_Broken_Rail(lineIndex, Integer.parseInt(failBlock), brState);
            Network.serverObject.set_Track_Circuit_Failure(lineIndex, Integer.parseInt(failBlock), tcfState);
            Network.serverObject.set_Power_Fail(lineIndex, Integer.parseInt(failBlock), powerState);
        }
    }

    //DEBUG BUTTONS
}
