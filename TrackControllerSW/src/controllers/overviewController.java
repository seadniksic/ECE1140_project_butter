package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.Network;
import sample.PLCConfig;

import java.io.IOException;
import java.rmi.RemoteException;

public class overviewController extends trackControllerUI {

    Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    Label line_label;

    @FXML
    GridPane greenControllers;

    @FXML
    GridPane redControllers;

    @FXML
    GridPane otherControllers;

    @FXML
    void initialize () {
        line_label.setText("Green");
        greenControllers.setVisible(true);
        redControllers.setVisible(false);
        otherControllers.setVisible(false);
        alert.setHeaderText("No track data avaliable");
        alert.setContentText("Track controller has no data, please wait for initialization");
    }

    public void set_Line_Green(ActionEvent actionEvent) {
        line_label.setText("Green");
        greenControllers.setVisible(true);
        redControllers.setVisible(false);
        otherControllers.setVisible(false);
    }

    public void set_Line_Red(ActionEvent actionEvent) {
        line_label.setText("Red");
        greenControllers.setVisible(false);
        redControllers.setVisible(true);
        otherControllers.setVisible(false);
    }

    public void set_Line_Other(ActionEvent actionEvent) {
        line_label.setText("Other");
        greenControllers.setVisible(false);
        redControllers.setVisible(false);
        otherControllers.setVisible(true);
    }

    public void open_Controller1(ActionEvent actionEvent) throws IOException {
        Stage stage1 = new Stage();
        stage1.setTitle("GREEN Track Controller 1");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("green", 0);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            //System.out.println(Network.serverObject.get_Controller("green", 0);0).get_Track_Line());
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage1.setScene(scene);
            stage1.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller2(ActionEvent actionEvent) throws IOException {
        Stage stage2 = new Stage();
        stage2.setTitle("GREEN Track Controller 2");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("green", 1);
            PLCConfig.con = Network.serverObject.get_Controller("green", 1);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage2.setScene(scene);
            stage2.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller3(ActionEvent actionEvent) throws IOException {
        Stage stage3 = new Stage();
        stage3.setTitle("GREEN Track Controller 3");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("green", 2);
            PLCConfig.con = Network.serverObject.get_Controller("green", 1);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage3.setScene(scene);
            stage3.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller4(ActionEvent actionEvent) throws IOException {
        Stage stage4 = new Stage();
        stage4.setTitle("GREEN Track Controller 4");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("green", 3);
            PLCConfig.con = Network.serverObject.get_Controller("green", 3);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage4.setScene(scene);
            stage4.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller5(ActionEvent actionEvent) throws IOException {
        Stage stage5 = new Stage();
        stage5.setTitle("GREEN Track Controller 5");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("green", 4);
            PLCConfig.con = Network.serverObject.get_Controller("green", 4);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage5.setScene(scene);
            stage5.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller6(ActionEvent actionEvent) throws IOException {
        Stage stage6 = new Stage();
        stage6.setTitle("GREEN Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("green", 5);
            PLCConfig.con = Network.serverObject.get_Controller("green", 5);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage6.setScene(scene);
            stage6.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller7(ActionEvent actionEvent) throws IOException {
        Stage stage7 = new Stage();
        stage7.setTitle("RED Track Controller 1");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 0);
            PLCConfig.con = Network.serverObject.get_Controller("red", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage7.setScene(scene);
            stage7.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller8(ActionEvent actionEvent) throws IOException {
        Stage stage8 = new Stage();
        stage8.setTitle("RED Track Controller 2");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 1);
            PLCConfig.con = Network.serverObject.get_Controller("green", 7);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage8.setScene(scene);
            stage8.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller9(ActionEvent actionEvent) throws IOException {
        Stage stage9 = new Stage();
        stage9.setTitle("RED Track Controller 3");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 2);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage9.setScene(scene);
            stage9.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller10(ActionEvent actionEvent) throws IOException {
        Stage stage10 = new Stage();
        stage10.setTitle("RED Track Controller 4");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 3);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage10.setScene(scene);
            stage10.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller11(ActionEvent actionEvent) throws IOException {
        Stage stage11 = new Stage();
        stage11.setTitle("RED Track Controller 5");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 4);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage11.setScene(scene);
            stage11.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller12(ActionEvent actionEvent) throws IOException {
        Stage stage12 = new Stage();
        stage12.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 5);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage12.setScene(scene);
            stage12.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void openController13(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("red", 6);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller14(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("Blue Track Controller 1");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("blue", 0);
            PLCConfig.con = Network.serverObject.get_Controller("blue", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void open_Controller15(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("blue", 1);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }
    public void open_Controller16(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("blue", 2);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }
    public void open_Controller17(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("blue", 3);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }
    public void open_Controller18(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("blue", 4);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }
    public void open_Controller19(ActionEvent actionEvent) throws IOException {
        Stage stage13 = new Stage();
        stage13.setTitle("RED Track Controller 6");

        try {
            trackControllerUI.con = Network.serverObject.get_Controller("blue", 5);
            PLCConfig.con = Network.serverObject.get_Controller("green", 0);
            Parent root = FXMLLoader.load(getClass().getResource("../resources/trackController.fxml"));
            Scene scene = new Scene(root);
            stage13.setScene(scene);
            stage13.show();
        } catch (NullPointerException e) {
            System.out.println("No Data");
            alert.show();
        }
    }

    public void start_Server(ActionEvent actionEvent) throws RemoteException {
        Network.start_Server();
    }

    public void demo_Data(ActionEvent actionEvent) throws RemoteException {
//        String[] lines = new String[]{"Green"};
//        int[][]blocks = new int[][] {{1, 9, 12, 10, 13, 20, 29, 21, 30, 45, 150, 117, 46, 51, 52, 57, 58, 60, 62, 61, 63, 66, 67, 69, 76, 70, 77, 82, 101, 116, 85, 83, 86, 93, 100, 94}};
        String[] lines = new String[]{"Green", "Blue"};
        int[][]blocks = new int[][] {{1, 9, 12, 10, 13, 20, 29, 21, 30, 45, 150, 117, 46, 51, 52, 57, 58, 60, 62, 61, 63, 66, 67, 69, 76, 70, 77, 82, 101, 116, 85, 83, 86, 93, 100, 94}, {1, 5, 6, 10, 15, 11}};

        Network.serverObject.make_Controllers(lines, blocks);
    }

    public void run_Network1(ActionEvent actionEvent) throws RemoteException {
        Network.connect_To_CTC();
    }

    public void run_Network2(ActionEvent actionEvent) {
        Network.connect_To_Track_Model();
    }

}
