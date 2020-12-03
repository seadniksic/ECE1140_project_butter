package MainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProgramController implements Initializable {

    public static String lineColor;
    public static String INPUTstartBN;
    public static String INPUTendBN;
    public static String NCstartBN;
    public static String NCendBN;
    public static String NOstartBN;
    public static String NOendBN;
    public static int[] inputArray;
    public static boolean[] inputStatusArray;
    public static int[] NCArray;
    public static boolean[] NCStatusArray;
    public static int[] NOArray;
    public static boolean[] NOStatusArray;
    public static String longString = "";
    public static boolean inputIncreasing = false;
    public static String attributeSelected;

    //public TextField start_block_input_box;
    //public TextField end_block_input_box;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        attribute_select_box.getItems().add("Station");
        attribute_select_box.getItems().add("Yard");
        attribute_select_box.getItems().add("Railway Crossing");
        attribute_select_box.getSelectionModel().selectFirst();
    }

    @FXML
    private Button program_Arduino_button;
    @FXML
    private TextField line_input_box;
    @FXML
    private TextField input_Start_box;
    @FXML
    private TextField input_End_box;
    @FXML
    private TextField NC_Start_box;
    @FXML
    private TextField NC_End_box;
    @FXML
    private TextField NO_Start_box;
    @FXML
    private TextField NO_End_box;
    @FXML
    private ChoiceBox<String> attribute_select_box;
    @FXML
    private TextField blockID_box;
    @FXML
    private ListView<String> listview;
    @FXML
    private Button listAdd;
    @FXML
    private Button listClear;
    @FXML
    private Button set_button;
    @FXML
    private ImageView green_check;
    @FXML
    private TextField PLCPaste;
    @FXML
    private Button program_Arduino_viaPLC_button;



    String message = "<0001";
    String oldString;

    @FXML
    void enable_PLCProgram(KeyEvent event) {
        program_Arduino_viaPLC_button.setDisable(false);
    }
    @FXML
    void program_Arduino_viaPLC(ActionEvent event) throws IOException {
        String message = PLCPaste.getText();

        char[] reply = Arduino.ArduinoMessenger(message);
        System.out.println("I received back: " + new String(reply));

        FXMLLoader program = new FXMLLoader(getClass().getResource("Home_Screen.fxml"));
        Stage stage = (Stage) Main.stage.getScene().getWindow();
        Scene programScene = new Scene(program.load());
        stage.setScene(programScene);
    }
    @FXML
    void set_press(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.ERROR);

        lineColor = line_input_box.getText();
        message = "<0001";
        String s1 = "";

        //Check that there isnt any digits or symbols in the line color
        for(int i=0; i < lineColor.length(); i++){
            char ch = lineColor.charAt(i);
            if(!Character.isAlphabetic(ch)){
                alert.setHeaderText("The line color must not contain numbers or symbols.");
                alert.show();
            }
        }

        if (lineColor.length() < 1 || lineColor.length() > 8) {
            //error message
            alert.setHeaderText("The line color must be between 1 and 8 characters");
            alert.show();
        }
        else if (lineColor.length() < 2){  //for 1 character line color
            s1 = lineColor + "0000000";
        }
        else if(lineColor.length() < 3){ //for 2 character line color
            s1 = lineColor +  "000000";
        }
        else if (lineColor.length() < 4){ //for 3 character line color
            s1 = lineColor +  "00000";
        }
        else if (lineColor.length() < 5){ // for 4 character line color
            s1 = lineColor +  "0000";
        }
        else if (lineColor.length() < 6){ // for 5 character line color
            s1 = lineColor +  "000";
        }
        else if (lineColor.length() < 7){ // for 6 character line color
            s1 = lineColor +  "00";
        }
        else if (lineColor.length() < 8){ // for 7 character line color
            s1 = lineColor +  "0";
        }
//      else if (lineColor.length() < 9){ // for 8 character line color
//          s1 = lineColor +  "";
//      }

        message = message + s1; //Add the line color to the message


        //INPUT start//
        INPUTstartBN = input_Start_box.getText();
        //Check that there IS ONY DIGITS IN input start number
        for(int i=0; i < INPUTstartBN.length(); i++){
            char ch = INPUTstartBN.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The Input Start Block Number must only contain digits.");
                alert.show();
            }
        }
        String s2 = "";
        if (INPUTstartBN.length() < 1 || INPUTstartBN.length() > 4){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else if (INPUTstartBN.length() < 2){  //for 1 digit block number
            s2 = "---" + INPUTstartBN;
        }
        else if(INPUTstartBN.length() < 3){ //for 2 digit block number
            s2 = "--" + INPUTstartBN;
        }
        else if (INPUTstartBN.length() < 4){ //for 3 digit block number
            s2 = "-" + INPUTstartBN;
        }
        else if (INPUTstartBN.length() < 5){ //for 4 digit block number
            s2 = INPUTstartBN;
        }
        if (s2.equals("---0")){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else{
            message = message +  s2;
        }

        INPUTendBN = input_End_box.getText();
        //Check that there IS ONY DIGITS
        for(int i=0; i < INPUTendBN.length(); i++){
            char ch = INPUTendBN.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The Input End Block Number must only contain digits.");
                alert.show();
            }
        }
        s2 = "";
        if (INPUTendBN.length() < 1 || INPUTendBN.length() > 4){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else if (INPUTendBN.length() < 2){  //for 1 digit block number
            s2 = "---" + INPUTendBN;
        }
        else if(INPUTendBN.length() < 3){ //for 2 digit block number
            s2 =  "--" + INPUTendBN;
        }
        else if (INPUTendBN.length() < 4){ //for 3 digit block number
            s2 =  "-" + INPUTendBN;
        }
        else if (INPUTendBN.length() < 5){ //for 4 digit block number
            s2 = INPUTendBN;
        }
        if (s2.equals("---0")){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else{
            message = message +  s2;
        }
        //INPUT end//

        //NC Start//
        NCstartBN = NC_Start_box.getText();
        for(int i=0; i < NCstartBN.length(); i++){
            char ch = NCstartBN.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The NC Start Block Number must only contain digits.");
                alert.show();
            }
        }
        s2 = "";
        if (NCstartBN.length() < 1 || NCstartBN.length() > 4){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else if (NCstartBN.length() < 2){  //for 1 digit block number
            s2 = "---" + NCstartBN;
        }
        else if(NCstartBN.length() < 3){ //for 2 digit block number
            s2 = "--" + NCstartBN;
        }
        else if (NCstartBN.length() < 4){ //for 3 digit block number
            s2 = "-" + NCstartBN;
        }
        else if (NCstartBN.length() < 5){ //for 4 digit block number
            s2 = NCstartBN;
        }
        if (s2.equals("---0")){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else{
            message = message +  s2;
        }

        NCendBN = NC_End_box.getText();
        for(int i=0; i < NCendBN.length(); i++){
            char ch = NCendBN.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The NC End Block Number must only contain digits.");
                alert.show();
            }
        }
        s2 = "";
        if (NCendBN.length() < 1 || NCendBN.length() > 4){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else if (NCendBN.length() < 2){  //for 1 digit block number
            s2 = "---" + NCendBN;
        }
        else if(NCendBN.length() < 3){ //for 2 digit block number
            s2 = "--" + NCendBN;
        }
        else if (NCendBN.length() < 4){ //for 3 digit block number
            s2 = "-" + NCendBN;
        }
        else if (NCendBN.length() < 5){ //for 4 digit block number
            s2 = NCendBN;
        }
        if (s2.equals("---0")){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else{
            message = message +  s2;
        }
        //NC END//

        // NO Start //
        NOstartBN = NO_Start_box.getText();
        for(int i=0; i < NOstartBN.length(); i++){
            char ch = NOstartBN.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The NO Start Block Number must only contain digits.");
                alert.show();
            }
        }
        s2 = "";
        if (NOstartBN.length() < 1 || NOstartBN.length() > 4){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else if (NOstartBN.length() < 2){  //for 1 digit block number
            s2 = "---" + NOstartBN;
        }
        else if(NOstartBN.length() < 3){ //for 2 digit block number
            s2 = "--" + NOstartBN;
        }
        else if (NOstartBN.length() < 4){ //for 3 digit block number
            s2 = "-" + NOstartBN;
        }
        else if (NOstartBN.length() < 5){ //for 4 digit block number
            s2 = NOstartBN;
        }
        if (s2.equals("---0")){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else{
            message = message +  s2;
        }

        NOendBN = NO_End_box.getText();
        for(int i=0; i < NOendBN.length(); i++){
            char ch = NOendBN.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The NO End Block Number must only contain digits.");
                alert.show();
            }
        }
        s2 = "";
        if (NOendBN.length() < 1 || NOendBN.length() > 4){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else if (NOendBN.length() < 2){  //for 1 digit block number
            s2 = "---"+ NOendBN;
        }
        else if(NOendBN.length() < 3){ //for 2 digit block number
            s2 = "--" + NOendBN;
        }
        else if (NOendBN.length() < 4){ //for 3 digit block number
            s2 = "-" + NOendBN;
        }
        else if (NOendBN.length() < 5){ //for 4 digit block number
            s2 = NOendBN;
        }
        if (s2.equals("---0")){
            alert.setHeaderText("The block number must be between 1 and 9999");
            alert.show();
        }
        else{
            message = message +  s2;
        }
        //NO END//

        //Now to compare that no block numbers are repeated
        // I start by creating arrays of the start and end blocks for each terminal

        //Create Input array
        int start = Integer.parseInt(INPUTstartBN);
        int end = Integer.parseInt(INPUTendBN);
        if (start == end){
            alert.setHeaderText("Start and end blocks can't be the same!");
            alert.show();
        }
        if (start < end){ //increasing input
            inputIncreasing = true;
            inputArray = new int[end - start + 1];
            inputStatusArray = new boolean[end - start + 1];
            Main.LightInputArray = new boolean[inputArray.length];
            Main.LightInputStatusArray = new boolean[inputArray.length];

            int length = end - start;
            for(int i=0; i<=length; i++){
                inputArray[i] = start + i;
                inputStatusArray[i] = false;
                if (i == 0){ //this if statement initializes the start blocks to have lights and make them green
                    Main.LightInputArray[i] = true;
                    Main.LightInputStatusArray[i] = true;
                }
            }
        }
        else{ //decreasing input
            inputArray = new int[start - end + 1];
            inputStatusArray = new boolean[start - end + 1];
            Main.LightInputArray = new boolean[inputArray.length];
            Main.LightInputStatusArray = new boolean[inputArray.length];

            int length = start - end;
            for(int i=0; i<=length; i++){
                inputArray[i] = end + i;
                inputStatusArray[i] = false;
                if (i == 0){
                    Main.LightInputArray[i] = true;
                    Main.LightInputStatusArray[i] = true;
                }
            }
        }


        //Create NC array
        start = Integer.parseInt(NCstartBN);
        end = Integer.parseInt(NCendBN);
        if (start == end){
            alert.setHeaderText("Start and end blocks can't be the same!");
            alert.show();
        }
        if (start < end){ //increasing input
            NCArray = new int[end - start + 1];
            NCStatusArray = new boolean[end - start + 1];
            Main.LightNCArray = new boolean[NCArray.length];
            Main.LightNCStatusArray = new boolean[NCArray.length];

            int length = end - start;
            for(int i=0; i<=length; i++){
                NCArray[i] = start + i;
                NCStatusArray[i] = false;
                if (i == 0){
                    Main.LightNCArray[i] = true;
                    Main.LightNCStatusArray[i] = true;
                }
            }
        }
        else{ //decreasing input
            NCArray = new int[start - end + 1];
            NCStatusArray = new boolean[start - end + 1];
            Main.LightNCArray = new boolean[NCArray.length];
            Main.LightNCStatusArray = new boolean[NCArray.length];


            int length = start - end;
            for(int i=0; i<=length; i++){
                NCArray[i] = end + i;
                NCStatusArray[i] = false;
                if (i == 0){
                    Main.LightNCArray[i] = true;
                    Main.LightNCStatusArray[i] = true;
                }
            }
        }
        //Create NO array
        start = Integer.parseInt(NOstartBN);
        end = Integer.parseInt(NOendBN);
        if (start == end){
            alert.setHeaderText("Start and end blocks can't be the same!");
            alert.show();
        }
        if (start < end){ //increasing input
            NOArray = new int[end - start + 1];
            NOStatusArray = new boolean[end - start + 1];
            Main.LightNOArray = new boolean[NOArray.length];
            Main.LightNOStatusArray = new boolean[NOArray.length];


            int length = end - start;
            for(int i=0; i<=length; i++){
                NOArray[i] = start + i;
                NOStatusArray[i] = false;
                if (i == 0){
                    Main.LightNOArray[i] = true;
                    Main.LightNOStatusArray[i] = false;
                }
            }
        }
        else{ //decreasing input
            NOArray = new int[start - end + 1];
            NOStatusArray = new boolean[start - end + 1];
            Main.LightNOArray = new boolean[NOArray.length];
            Main.LightNOStatusArray = new boolean[NOArray.length];

            int length = start - end;
            for(int i=0; i<=length; i++){
                NOArray[i] = end + i;
                NOStatusArray[i] = false;
                if (i == 0){
                    Main.LightNOArray[i] = true;
                    Main.LightNOStatusArray[i] = false;
                }
            }
        }

        //There's probably a more efficient way of doing this but I don't care.
        boolean ICoverlap = false;
        boolean IOoverlap = false;
        boolean COoverlap = false;
        //Compare each element of the input array to each element of the NC array
        for (int j = 0; j < NCArray.length; j++) {
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i] == NCArray[j]){
                    ICoverlap = true;
                }
            }
        }
        //Compare each element of the input array to each element of the NO array
        for (int j = 0; j < NOArray.length; j++) {
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i] == NOArray[j]){
                    IOoverlap = true;
                }
            }
        }
        //Compare each element of the NC array to each element of the NO array
        for (int j = 0; j < NOArray.length; j++) {
            for (int i = 0; i < NCArray.length; i++) {
                if (NCArray[i] == NOArray[j]){
                    COoverlap = true;
                }
            }
        }
        if (ICoverlap){
            alert.setHeaderText("Overlapping arrays! Check that the Input array does not overlap with the NC array!");
            alert.show();
        }
        if (IOoverlap){
            alert.setHeaderText("Overlapping arrays! Check that the Input array does not overlap with the NO array!");
            alert.show();
        }
        if (COoverlap){
            alert.setHeaderText("Overlapping arrays! Check that the NC array does not overlap with the NO array!");
            alert.show();
        }

        if (!alert.isShowing()) { //if there are alerts, cancel the reprogram

            green_check.setVisible(true);
            blockID_box.setDisable(false);
            attribute_select_box.setDisable(false);
            listAdd.setDisable(false);
            listClear.setDisable(false);
            program_Arduino_button.setDisable(false);
        }
    }
    @FXML
    void add_to_List(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.ERROR);

        //Read in the block number
        String string = blockID_box.getText();
        if (string.equals("")){
            alert.setHeaderText("Please enter a block number.");
            alert.show();
        }

        //Make sure it's a number
        for(int i=0; i < string.length(); i++){
            char ch = string.charAt(i);
            if(!Character.isDigit(ch)){
                alert.setHeaderText("The Block Number must only contain digits.");
                alert.show();
            }
        }

        if (string.equals(oldString)) {
            alert.setHeaderText("Block already contains an attribute.");
            alert.show();
        }

        int block = Integer.parseInt(string);
        boolean contained = false;

        for (int i = 0; i < inputArray.length; i++) {
            if (block == inputArray[i]) {
                contained = true;
            }
        }

        for (int i = 0; i < NCArray.length; i++) {
            if (block == NCArray[i]){
                contained = true;
            }
        }

        for (int i = 0; i < NOArray.length; i++) {
            if (block == NOArray[i]){
                contained = true;
            }
        }

        if (!contained){
            alert.setHeaderText("The Block number you specified is not contained a segment of track this controller controls.");
            alert.show();
        }

        if (!alert.isShowing()) { //if there aren't alerts, proceed. Else cancel.

            //First, add to the message the block attributes
            message = message + ";";
            String s3 = "";
            int length = String.valueOf(block).length();
            if (length < 2){  //for 1 digit block number
                message = message + "---" + String.valueOf(block);
            }
            else if(length < 3){ //for 2 digit block number
                message = message + "--" + String.valueOf(block) ;
            }
            else if (length < 4){ //for 3 digit block number
                message = message + "-" + String.valueOf(block);
            }
            else if (length < 5){ //for 4 digit block number
                message = message + String.valueOf(block);
            }
            message = message + ",";
            switch (attribute_select_box.getValue()) {
                case "Station" -> message = message + "1";
                case "Yard" -> message = message + "2";
                case "Railway Crossing" -> message = message + "3";
            }

            for (int i = 0; i < inputArray.length; i++) {
                if (block == inputArray[i]) {
                    Main.LightInputArray[i] = true;
                    Main.LightInputStatusArray[i] = true;
                }
            }

            for (int i = 0; i < NCArray.length; i++) {
                if (block == NCArray[i]){
                    Main.LightNCArray[i] = true;
                    Main.LightNCStatusArray[i] = true;
                }
            }

            for (int i = 0; i < NOArray.length; i++) {
                if (block == NOArray[i]){
                    Main.LightNOArray[i] = true;
                    Main.LightNOStatusArray[i] = true;
                }
            }
            listview.getItems().add(string + " - " + attribute_select_box.getValue());
            longString = longString + string + " - " + attribute_select_box.getValue() + ";";
            oldString = string;
        }
    }
    @FXML
    void clear_list(ActionEvent event) {
        listview.getItems().clear();
        longString = "";
        oldString = "";
    }
    @FXML
    void program_Arduino(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.ERROR);

        if (!alert.isShowing()) { //if there are alerts, cancel the reprogram
            message += ">";
            char[] reply = Arduino.ArduinoMessenger(message);
            System.out.println("I received back: " + new String(reply));

            if(reply.length == 0) {
                reply = Arduino.ArduinoMessenger(message);
                System.out.println("I received back: " + new String(reply));
            }

            //Network.tc_Interface.get_Block_Array(Integer.parseInt(INPUTstartBN));

            FXMLLoader program = new FXMLLoader(getClass().getResource("Home_Screen.fxml"));
            Stage stage = (Stage) Main.stage.getScene().getWindow();
            Scene programScene = new Scene(program.load());
            stage.setScene(programScene);
        }
    }


}
