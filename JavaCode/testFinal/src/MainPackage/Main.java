package MainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.Track_Controller_HW_Interface;

import java.io.IOException;
import java.rmi.RemoteException;

public class Main<d> extends Application implements Track_Controller_HW_Interface {

    public static boolean switched = false;
    public static boolean ArduinoIsConnected = false;
    public static Stage stage;
    //Light array is if the block has lights or not
    public static boolean[] LightNOArray;
    public static boolean[] LightNCArray;
    public static boolean[] LightInputArray;
    //Light status array is if the block's lights are enabled or disabled 1 = Green , 0 = Red
    public static boolean[] LightNOStatusArray;
    public static boolean[] LightNCStatusArray;
    public static boolean[] LightInputStatusArray;

    public static int blockReceived = -1;
    public static boolean occupancyReceived = false;
    public static int faultBlockReceived = -1;
    public static boolean faultStatusReceived = false;

    //ArrayList<Integer> blockReceivedList;
    //ArrayList<Boolean> occupancyReceivedList;

    private HomeController controller  = new HomeController();
    public void setController(HomeController controller) {
        this.controller = controller ;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            //Parent
            Parent initial = FXMLLoader.load(getClass().getResource("Initial_Screen.fxml"));
            Scene initialScene = new Scene(initial);

            stage = new Stage();
            stage.setTitle("Track Controller Hardware");
            stage.setScene(initialScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public void set_Block_State(int BlockNum, boolean occupancy) throws RemoteException {
        //For recieving occupancy

        //Then send the arduino a message
        String message = "<0010";
        //String s2 = "";
        String block = Integer.toString(BlockNum);

        if (block.length() < 2) {  //for 1 digit block number
            message = message + "---" + block;
        } else if (block.length() < 3) { //for 2 digit block number
            message = message + "--" + block;
        } else if (block.length() < 4) { //for 3 digit block number
            message = message + "-" + block;
        } else if (block.length() < 5) { //for 4 digit block number
            message = message + block;
        }

        if (occupancy){
            message = message + 1;
        }
        else {
            message = message + 0;
        }
        message += ">";
        //boolean StateReply = false;
        char[] reply = new char[1];
        try {
            reply = Arduino.ArduinoMessenger(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I received back: " + new String(reply));
        System.out.println(reply[0]);
        if (reply[0] == '1'){
            //StateReply = true;
            Main.switched = true;
        }
        else if (reply[0] == '0'){
            //StateReply = false;
            Main.switched = false;
        }
        //Send the data to HomeController class
        //myThread myThread = new myThread();
        //myThread.start();


        //blockReceivedList.add(BlockNum);
        //occupancyReceivedList.add(occupancy);
        blockReceived = BlockNum;
        occupancyReceived = occupancy;
        controller.UpdateOccupancy(BlockNum, occupancy);
    }
    public void manual_Switch_set(boolean state) throws RemoteException {
        String message = "<0011";
        if (state){
            message = message + 1;
        }
        else {
            message = message + 0;
        }
        message += ">";
        char[] reply = new char[0];
        try {
            reply = Arduino.ArduinoMessenger(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I received back: " + new String(reply));

        Main.switched = !Main.switched;

    }
    public void reprogramPLC(String PLC) throws RemoteException {

        char[] reply = new char[0];
        try {
            reply = Arduino.ArduinoMessenger(PLC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I received back: " + new String(reply));
    }
    public void set_Broken_Rail(int lineIndex, int BlockNum, boolean state) throws RemoteException{

        String message = "<0100";
        String block = Integer.toString(BlockNum);
        if (block.length() < 2) {  //for 1 digit block number
            message = message + "---" + block;
        } else if (block.length() < 3) { //for 2 digit block number
            message = message + "--" + block;
        } else if (block.length() < 4) { //for 3 digit block number
            message = message + "-" + block;
        } else if (block.length() < 5) { //for 4 digit block number
            message = message + block;
        }
        message += 1;
        message += state ? 1 : 0;
        message += ">";
        char[] reply = new char[1];
        try {
            reply = Arduino.ArduinoMessenger(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I received back: " + new String(reply));
        System.out.println(reply[0]);
        if (reply[0] == '1'){
            //StateReply = true;
            //Main.switched = true;
        }
        else if (reply[0] == '0'){
            //StateReply = false;
            //Main.switched = false;
        }

        //Send the data to HomeController class
        controller.UpdateFaults(BlockNum, 1, state);
    }
    public void set_Track_Circuit_Failure(int lineIndex, int BlockNum, boolean state) throws RemoteException{
        String message = "<0100";
        String block = Integer.toString(BlockNum);
        if (block.length() < 2) {  //for 1 digit block number
            message = message + "---" + block;
        } else if (block.length() < 3) { //for 2 digit block number
            message = message + "--" + block;
        } else if (block.length() < 4) { //for 3 digit block number
            message = message + "-" + block;
        } else if (block.length() < 5) { //for 4 digit block number
            message = message + block;
        }
        message += 2;
        message += state ? 1 : 0;
        message += ">";
        char[] reply = new char[1];
        try {
            reply = Arduino.ArduinoMessenger(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I received back: " + new String(reply));
        System.out.println(reply[0]);
        if (reply[0] == '1'){
            //StateReply = true;
            //Main.switched = true;
        }
        else if (reply[0] == '0'){
            //StateReply = false;
            //Main.switched = false;
        }
        //Send the data to HomeController class
        controller.UpdateFaults(BlockNum, 2, state);
    }
    public void set_Power_Fail(int lineIndex, int BlockNum, boolean state) throws RemoteException{
        String message = "<0100";
        String block = Integer.toString(BlockNum);
        if (block.length() < 2) {  //for 1 digit block number
            message = message + "---" + block;
        } else if (block.length() < 3) { //for 2 digit block number
            message = message + "--" + block;
        } else if (block.length() < 4) { //for 3 digit block number
            message = message + "-" + block;
        } else if (block.length() < 5) { //for 4 digit block number
            message = message + block;
        }
        message += 3;
        message += state ? 1 : 0;
        message += ">";
        char[] reply = new char[1];
        try {
            reply = Arduino.ArduinoMessenger(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I received back: " + new String(reply));
        System.out.println(reply[0]);
        if (reply[0] == '1'){
            //StateReply = true;
            //Main.switched = true;
        }
        else if (reply[0] == '0'){
            //StateReply = false;
            //Main.switched = false;
        }
        //Send the data to HomeController class
        controller.UpdateFaults(BlockNum, 3, state);

    }

}







