package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.Network;
import sample.OverallSoftware;
import sample.PLCConfig;
import sample.TrackControllerCatalogue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

import static sample.TrackControllerCatalogue.send_Block_State;

public class overviewController extends trackControllerUI {

    @FXML
    Label line_label;

    @FXML
    GridPane greenControllers;

    @FXML
    GridPane redControllers;

    @FXML
    void initialize () {
        line_label.setText("Green");
        greenControllers.setVisible(true);
        redControllers.setVisible(false);
    }

    public void set_Line_Green(ActionEvent actionEvent) {
        line_label.setText("Green");
        greenControllers.setVisible(true);
        redControllers.setVisible(false);
    }

    public void set_Line_Red(ActionEvent actionEvent) {
        line_label.setText("Red");
        greenControllers.setVisible(false);
        redControllers.setVisible(true);
    }

    public void open_Controller1(ActionEvent actionEvent) throws IOException {
        Stage stage1 = new Stage();
        stage1.setTitle("GREEN Track Controller 1");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(0);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage1.setScene(scene);
            stage1.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller2(ActionEvent actionEvent) throws IOException {
        Stage stage2 = new Stage();
        stage2.setTitle("GREEN Track Controller 2");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(1);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(1);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage2.setScene(scene);
            stage2.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller3(ActionEvent actionEvent) throws IOException {
        Stage stage3 = new Stage();
        stage3.setTitle("GREEN Track Controller 3");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(2);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(2);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage3.setScene(scene);
            stage3.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller4(ActionEvent actionEvent) throws IOException {
        Stage stage4 = new Stage();
        stage4.setTitle("GREEN Track Controller 4");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(3);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(3);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage4.setScene(scene);
            stage4.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller5(ActionEvent actionEvent) throws IOException {
        Stage stage5 = new Stage();
        stage5.setTitle("GREEN Track Controller 5");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(4);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(4);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage5.setScene(scene);
            stage5.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller6(ActionEvent actionEvent) throws IOException {
        Stage stage6 = new Stage();
        stage6.setTitle("GREEN Track Controller 6");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(5);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(5);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage6.setScene(scene);
            stage6.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller7(ActionEvent actionEvent) throws IOException {
        Stage stage7 = new Stage();
        stage7.setTitle("RED Track Controller 1");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(0);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage7.setScene(scene);
            stage7.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller8(ActionEvent actionEvent) throws IOException {
        Stage stage8 = new Stage();
        stage8.setTitle("RED Track Controller 2");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(1);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage8.setScene(scene);
            stage8.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller9(ActionEvent actionEvent) throws IOException {
        Stage stage9 = new Stage();
        stage9.setTitle("RED Track Controller 3");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(2);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage9.setScene(scene);
            stage9.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller10(ActionEvent actionEvent) throws IOException {
        Stage stage10 = new Stage();
        stage10.setTitle("RED Track Controller 4");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(3);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage10.setScene(scene);
            stage10.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller11(ActionEvent actionEvent) throws IOException {
        Stage stage11 = new Stage();
        stage11.setTitle("RED Track Controller 5");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(4);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage11.setScene(scene);
            stage11.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void open_Controller12(ActionEvent actionEvent) throws IOException {
        Stage stage12 = new Stage();
        stage12.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(5);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage12.setScene(scene);
            stage12.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void openController13(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = TrackControllerCatalogue.get_Controller(6);
            PLCConfig.con = TrackControllerCatalogue.get_Controller(0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
        }
    }

    public void run_Network(ActionEvent actionEvent) throws RemoteException {
        //Network.start_Server();
        Network.connect_To_Modules();
    }

    public void start_Server(ActionEvent actionEvent) throws RemoteException {
        Network.start_Server();
    }

    public void demo_Data(ActionEvent actionEvent) {
        String[] lines = new String[]{"green"};
        int[][]blocks = new int[][] {{1, 9, 12, 10, 13, 20, 29, 21, 30, 45, 150, 117, 46, 51, 52, 57, 58, 60, 62, 61, 63, 66, 67, 69, 76, 70, 77, 82, 101, 116, 85, 83, 86, 93, 100, 94}};
        OverallSoftware.tc.make_Controllers(lines, blocks);
    }
}
