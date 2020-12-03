package MainPackage;

import com.fazecast.jSerialComm.SerialPort;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialController implements Initializable {

    public static SerialPort chosenPort;
    boolean serverconnected = false;
    boolean arduinoconnected = false;

    @FXML
    private ComboBox<String> portList;
    @FXML
    private Button connectButton;
    @FXML
    private Button serverButton;
    @FXML
    private Button refreshButton;

    private URL url;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Initialize the ComboBox of Port Names
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (int i = 0; i < portNames.length; i++) {
            String port = portNames[i].getSystemPortName();
            portList.getItems().add(port);
        }
        portList.getSelectionModel().selectFirst();

        //Initialize buttons
        //arduinoButton1.setDisable(true);
        //arduinoButton2.setDisable(true);
        Network.start_Server();

        serverconnected = !serverconnected;
        if (arduinoconnected && serverconnected) {
            //if (serverconnected) {
            FXMLLoader program = new FXMLLoader(getClass().getResource("Program_Screen.fxml"));
            Stage stage = (Stage) Main.stage.getScene().getWindow();
            Scene programScene = null;
            try {
                programScene = new Scene(program.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(programScene);
        }
    }

    @FXML
    void refreshCOMports(ActionEvent event) {
        SerialPort[] portNames = SerialPort.getCommPorts();
        portList.getItems().clear();
        for (int i = 0; i < portNames.length; i++) {
            String port = portNames[i].getSystemPortName();
            portList.getItems().add(port);
        }
        portList.getSelectionModel().selectFirst();
    }

    @FXML
    void connect_to_arduino(ActionEvent event) throws IOException {
        if(portList.getValue() != null) {

        chosenPort = SerialPort.getCommPort(portList.getValue().toString());
        chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

            if (connectButton.getText().equals("Connect")) {
                System.out.println("Attempting to connect to Arduino...");
                if (chosenPort.openPort()) {
                    System.out.println("Connected to Arduino");

                    Main.ArduinoIsConnected = true;
                    arduinoconnected = !arduinoconnected;

                    portList.setDisable(true);
                    connectButton.setText("Disconnect");

                    if (arduinoconnected && serverconnected) {
                        FXMLLoader program = new FXMLLoader(getClass().getResource("Program_Screen.fxml"));
                        Stage stage = (Stage) Main.stage.getScene().getWindow();
                        Scene programScene = new Scene(program.load());
                        stage.setScene(programScene);
                    }
                }
            } else {
                // disconnect from the serial port
                Main.ArduinoIsConnected = false;
                chosenPort.closePort();
                arduinoconnected = !arduinoconnected;
                portList.setDisable(false);
                connectButton.setText("Connect");
                System.out.println("Disconnected");
            }
        }
        else {
            System.out.println("Com Port Not Selected");


        }
    }

    @FXML
    void connect_to_server(ActionEvent event) throws IOException {

        //System.out.println("Connect to Server Button Pressed");


        Network.connect_To_Modules();


    }
}
