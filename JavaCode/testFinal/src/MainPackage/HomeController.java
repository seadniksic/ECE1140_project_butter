package MainPackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import static MainPackage.ProgramController.*;

public class HomeController extends Thread implements Initializable  {

    @FXML
    private Text NC_label;
    @FXML
    private Text NO_label;
    @FXML
    private ListView<String> listview_home;
    @FXML
    private Text in_start_text;
    @FXML
    private Text in_end_text;
    @FXML
    private Text NC_start_text;
    @FXML
    private Text NO_end_text;
    @FXML
    private Text NC_end_text;
    @FXML
    private Text NO_start_text;
    @FXML
    private Rectangle lightFrame;
    @FXML
    private Button switchButton;
    @FXML
    private CheckBox manualMode;
    @FXML
    private Text line_text;
    @FXML
    private Button reprogramButton;
    @FXML
    public ListView<String> occupancy_list;
    @FXML
    public ListView<String> fault_list;
    @FXML
    public ListView<String> light_list;
    @FXML
    private TextField occ_test;
    @FXML
    private TextField fal_test;
    @FXML
    private Button occ_test_button;
    @FXML
    private Button fal_test_button;
    @FXML
    private CheckBox occ_setting;
    @FXML
    private CheckBox fal_setting;
    @FXML
    private CheckBox manual_setting;

    public String[] shortString;
    public boolean flip = false;
    public boolean flip2 = false;
    boolean flip3 = true;
    public static int[] FaultInputArray;
    public static int[] FaultNCArray;
    public static int[] FaultNOArray;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        Main main1 = new Main();
        main1.setController(this);
        switchButton.setVisible(false);

        shortString = ProgramController.longString.split(";");
        FaultInputArray = new int[ProgramController.inputArray.length];
        FaultNCArray = new int[ProgramController.NCArray.length];
        FaultNOArray = new int[ProgramController.NOArray.length];

        for (int i = 0; i < shortString.length; i++) {
            listview_home.getItems().add(shortString[i]);
        }

        ProgramController.longString = "";

        for (int i = 0; i < ProgramController.inputArray.length;i++){
            occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - CLEAR");
            fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": No Failures");
            FaultInputArray[i] = 0;

            if (Main.LightInputArray[i]){ //If this block has lights
                if (Main.LightInputStatusArray[i]){ //If the block has lights and they're ON
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Green");
                }
                else{ //If the block has lights and they're OFF
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Red");
                }
            }
            else{ //If the block doesn't have lights
                light_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]));
            }

        }
        for (int i = 0; i < ProgramController.NCArray.length;i++){
            occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i])+ " - CLEAR");
            fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": No Failures");
            FaultNCArray[i] = 0;
            if (Main.LightNCArray[i]){ //If this block has lights
                if (Main.LightNCStatusArray[i]){ //If the block has lights and they're ON
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Green");
                }
                else{ //If the block has lights and they're OFF
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Red");
                }
            }
            else{ //If the block doesn't have lights
                light_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]));
            }


        }
        for (int i = 0; i < ProgramController.NOArray.length;i++){
            occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i])+ " - CLEAR");
            fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": No Failures");
            FaultNOArray[i] = 0;
            if (Main.LightNOArray[i]){ //If this block has lights
                if (Main.LightNOStatusArray[i]){ //If the block has lights and they're ON
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Green");
                }
                else{ //If the block has lights and they're OFF
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Red");
                }
            }
            else{ //If the block doesn't have lights
                light_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]));
            }


        }

        line_text.setText(lineColor);
        in_start_text.setText(ProgramController.INPUTstartBN);
        in_end_text.setText(ProgramController.INPUTendBN);
        NC_start_text.setText(ProgramController.NCstartBN);
        NC_end_text.setText(ProgramController.NCendBN);
        NO_start_text.setText(ProgramController.NOstartBN);
        NO_end_text.setText(ProgramController.NOendBN);

        UpdateGUISwitch();
    }
    @FXML
    public void checkOccUpdate(){
        if (Main.blockReceived != -1){
            UpdateOccupancy(Main.blockReceived, Main.occupancyReceived);
            Main.blockReceived = -1;
        }

        if(Main.faultBlockReceived != -1){
            UpdateFaults(Main.faultBlockReceived, 1, Main.faultStatusReceived);
            Main.faultBlockReceived = -1;
        }


/*
        if (!Main.blockReceivedList.isEmpty()){
            UpdateOccupancy(Main.blockReceivedList.get(Main.blockReceivedList.size() - 1), Main.occupancyReceivedList.get(Main.occupancyReceivedList.size() -1));
            Main.blockReceivedList.remove(Main.blockReceivedList.size() - 1);
            Main.occupancyReceivedList.remove(Main.occupancyReceivedList.size() - 1);
        }
        if (!Main.faultBlockReceivedList.isEmpty()){
            UpdateFaults(Main.faultBlockReceivedList.get(Main.faultBlockReceivedList.size() - 1), Main.faultTypeReceivedList.get(Main.faultTypeReceivedList.size() - 1), Main.faultStatusList.get(Main.faultStatusList.size() -1));
            Main.faultBlockReceivedList.remove(Main.faultBlockReceivedList.size() - 1);
            Main.faultTypeReceivedList.remove(Main.faultTypeReceivedList.size() - 1);
            Main.faultStatusList.remove(Main.faultStatusList.size() - 1);
        }
        */

    }
    @FXML
    void reprogram_press(ActionEvent event) throws IOException {
        FXMLLoader program = new FXMLLoader(getClass().getResource("Program_Screen.fxml"));
        Stage stage = (Stage) Main.stage.getScene().getWindow();
        Scene programScene = new Scene(program.load());
        stage.setScene(programScene);
    }

    @FXML
    void button1Press(ActionEvent event) throws RemoteException {

        Main main1 = new Main();
        main1.setController(this);

        flip2 = !flip2;
        main1.manual_Switch_set(flip2);

        UpdateGUISwitch();
        UpdateLights();
    }

    @FXML
    void testOccupancyPress(ActionEvent event) throws RemoteException {
        Main main1 = new Main();
        main1.setController(this);

        flip = !flip;
        main1.set_Block_State(5, flip);
        main1.set_Broken_Rail(1, 4, flip);

        UpdateGUISwitch();
    }

    @FXML
    public void UpdateGUISwitch() {
        if (!Main.switched) {
            NC_label.setVisible(true);
            NO_label.setVisible(false);
        }
        else{
            NC_label.setVisible(false);
            NO_label.setVisible(true);
        }
    }

    @FXML
    void moduleConnect(ActionEvent event) throws RemoteException {
        //Send the int of input start block to Tony
        //int InputStBlock = Integer.parseInt(ProgramController.INPUTstartBN);
        //System.out.println("I send to Tony: " + InputStBlock);
        //Network.tc_Interface.get_Block_Array(InputStBlock);
/*
        Main main1 = new Main();
        main1.setController(this);

        main1.set_Block_State(1, true);

        //main1.set_Block_State(1, false);
        main1.set_Block_State(2, true);

        //main1.set_Block_State(2, false);
        main1.set_Block_State(3, true);

        //main1.set_Block_State(3, false);
        main1.set_Block_State(4, true);

        //main1.set_Block_State(4, false);
        main1.set_Block_State(5, true);

        //main1.set_Block_State(5, false);
        main1.set_Block_State(6, true);
*/
    }

    @FXML
    public void UpdateOccupancy(int block_received, boolean state_received) {

        occupancy_list.getItems().clear();

        //First scan the input array to see if the block is contained within
        for (int i = 0; i < ProgramController.inputArray.length;i++){
            if (block_received == ProgramController.inputArray[i]){
                //If the block is contained, check if the current status of the block is occupied
                if (ProgramController.inputStatusArray[i]){ //If the block in question is occupied
                    if (!state_received){ //If the block is being commanded as not occupied
                        occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - CLEAR");
                        ProgramController.inputStatusArray[i] = false;
                    }
                    else if (state_received){
                        //error here, block is already occupied!

                        occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - OCCUPIED");
                    }
                }
                else if (!ProgramController.inputStatusArray[i]){ //If the block is not occupied
                    if (!state_received){ //If the block is being commanded as not occupied
                        occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - CLEAR");
                    }
                    else if (state_received){ //If the block is being commanded as occupied
                        occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - OCCUPIED");
                        ProgramController.inputStatusArray[i] = true;
                    }
                }
            }
            else {
                if (ProgramController.inputStatusArray[i]) { //If the block in question is occupied
                    occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - OCCUPIED");
                }
                else{
                    occupancy_list.getItems().add("Input - " + String.valueOf(ProgramController.inputArray[i]) + " - CLEAR");
                }
            }
        }
        for (int i = 0; i < ProgramController.NCArray.length;i++){
            if (block_received == ProgramController.NCArray[i]){
                if (ProgramController.NCStatusArray[i]){
                    if (!state_received){
                        occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i]) + " - CLEAR");
                        ProgramController.NCStatusArray[i] = false;
                    }
                    else if (state_received){
                        //error here, block is already occupied!
                        occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i]) + " - OCCUPIED");
                    }
                }
                else if (!ProgramController.NCStatusArray[i]){
                    if (!state_received){
                        occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i]) + " - CLEAR");
                    }
                    else if (state_received){
                        occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i]) + " - OCCUPIED");
                        ProgramController.NCStatusArray[i] = true;
                    }
                }
            }
            else {
                if (ProgramController.NCStatusArray[i]) { //If the block in question is occupied
                    occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i]) + " - OCCUPIED");
                }
                else{
                    occupancy_list.getItems().add("NC - " + String.valueOf(ProgramController.NCArray[i]) + " - CLEAR");
                }
            }
        }
        for (int i = 0; i < ProgramController.NOArray.length;i++){
            if (block_received == ProgramController.NOArray[i]){
                if (ProgramController.NOStatusArray[i]){
                    if (!state_received){
                        occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i]) + " - CLEAR");
                        ProgramController.NOStatusArray[i] = false;
                    }
                    else if (state_received){
                        //error here, block is already occupied!
                        occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i]) + " - OCCUPIED");

                    }
                }
                else if (!ProgramController.NOStatusArray[i]){
                    if (!state_received){
                        occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i]) + " - CLEAR");
                    }
                    else if (state_received){
                        occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i]) + " - OCCUPIED");
                        ProgramController.NOStatusArray[i] = true;
                    }
                }
            }
            else {
                if (ProgramController.NOStatusArray[i]) { //If the block in question is occupied
                    occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i]) + " - OCCUPIED");
                }
                else{
                    occupancy_list.getItems().add("NO - " + String.valueOf(ProgramController.NOArray[i]) + " - CLEAR");
                }
            }
        }
        UpdateGUISwitch();
        UpdateLights();
    }

    @FXML
    public void UpdateFaults(int block_received, int fault, boolean status){
        fault_list.getItems().clear();

        //First scan the input array to see if the block is contained within
        for (int i = 0; i < ProgramController.inputArray.length;i++){
            if (block_received == ProgramController.inputArray[i]){
                //If the block is contained, check what the status of the fault is
                //update the memory
                FaultInputArray[i] = fault;

                //If the fault is not present, clear it
                if (!status){
                    FaultInputArray[i] = 0;
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": No Failure");
                }
                else if (fault == 1) {
                    //A fault of 1 is a broken rail
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Broken Rail Failure");
                } else if (fault == 2) {
                    //A fault of 2 is a Track Circuit Failure
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Track Circuit Failure");
                } else if (fault == 3) {
                    //A fault of 2 is a Power Circuit Failure
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Power Circuit Failure");
                }
            }
            else {
                if (FaultInputArray[i] == 0){
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": No Failure");
                }
                else if (FaultInputArray[i] == 1){
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Broken Rail Failure");
                }
                else if (FaultInputArray[i] == 2) {
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Track Circuit Failure");
                }
                else if (FaultInputArray[i] == 3) {
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.inputArray[i]) + ": Power Circuit Failure");
                }
            }
        }

        for (int i = 0; i < ProgramController.NCArray.length;i++){
            if (block_received == ProgramController.NCArray[i]){
                //If the block is contained, check what the status of the fault is
                //update the memory
                FaultNCArray[i] = fault;

                //If the fault is not present, clear it
                if (!status){
                    FaultNCArray[i] = 0;
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": No Failure");
                }
                else if (fault == 1) {
                    //A fault of 1 is a broken rail
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Broken Rail Failure");
                } else if (fault == 2) {
                    //A fault of 2 is a Track Circuit Failure
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Track Circuit Failure");
                } else if (fault == 3) {
                    //A fault of 2 is a Power Circuit Failure
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Power Circuit Failure");
                }
            }
            else {
                if (FaultNCArray[i] == 0){
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": No Failure");
                }
                else if (FaultNCArray[i] == 1){
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Broken Rail Failure");
                }
                else if (FaultNCArray[i] == 2) {
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Track Circuit Failure");
                }
                else if (FaultNCArray[i] == 3) {
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Power Circuit Failure");
                }
            }
        }

        for (int i = 0; i < ProgramController.NOArray.length;i++){
            if (block_received == ProgramController.NOArray[i]){
                //If the block is contained, check what the status of the fault is
                //update the memory
                FaultNOArray[i] = fault;

                //If the fault is not present, clear it
                if (!status){
                    FaultNOArray[i] = 0;
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": No Failure");
                }
                else if (fault == 1) {
                    //A fault of 1 is a broken rail
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Broken Rail Failure");
                } else if (fault == 2) {
                    //A fault of 2 is a Track Circuit Failure
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Track Circuit Failure");
                } else if (fault == 3) {
                    //A fault of 2 is a Power Circuit Failure
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Power Circuit Failure");
                }
            }
            else {
                if (FaultNOArray[i] == 0){
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": No Failure");
                }
                else if (FaultNOArray[i] == 1){
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Broken Rail Failure");
                }
                else if (FaultNOArray[i] == 2) {
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Track Circuit Failure");
                }
                else if (FaultNOArray[i] == 3) {
                    fault_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Power Circuit Failure");
                }
            }
        }
        UpdateLights();
    }

    @FXML
    public void UpdateLights(){
        if (!Main.switched){
            //Make Input Start Block Lights Green
            //Make NC Start Block Lights Green
            //Make NO Start Block Lights Red
            for (int i = 0; i < inputArray.length; i++) {
                if (Main.LightInputArray[i]) {
                    Main.LightInputStatusArray[i] = true;
                }
            }
            for (int j = 0; j < NCArray.length; j++) {
                if (Main.LightNCArray[j]) {
                    Main.LightNCStatusArray[j] = true;
                }
            }
            for (int i = 0; i < NOArray.length; i++) {
                if (Main.LightNOArray[i]) {
                    Main.LightNOStatusArray[i] = false;
                }
            }

        }
        else{
            for (int i = 0; i < inputArray.length; i++) {
                if (Main.LightInputArray[i]) {
                    Main.LightInputStatusArray[i] = false;
                }
            }
            for (int i = 0; i < NCArray.length; i++) {
                if (Main.LightNCArray[i]) {
                    Main.LightNCStatusArray[i] = false;
                }
            }
            for (int i = 0; i < NOArray.length; i++) {
                if (Main.LightNOArray[i]) {
                    Main.LightNOStatusArray[i] = true;
                }
            }
        }

        //Next, I will check the faults. if there are any faults on that segment of track, all of Input Goes Red
        boolean inputFault = false;
        boolean NCFault = false;
        boolean NOFault = false;
        for (int i = 0; i < FaultInputArray.length; i++) {
            if (FaultInputArray[i] != 0) {
                inputFault = true;
                break;
            }
        }
        for (int i = 0; i < FaultNCArray.length; i++) {
            if (FaultNCArray[i] != 0) {
                NCFault = true;
                break;
            }
        }
        for (int j : FaultNOArray) {
            if (j != 0) {
                NOFault = true;
                break;
            }
        }

        //System.out.println(inputFault);
        //System.out.println(NCFault);
        //System.out.println(NOFault);

        //If there is a fault on both an input and another branch, shut everything down
        if ((inputFault && NCFault) || (inputFault && NOFault)){
            for (int i = 0; i < Main.LightInputArray.length; i++){
                if (Main.LightInputArray[i]){
                    Main.LightInputStatusArray[i] = false;
                }
            }
            for (int i = 0; i < Main.LightNCArray.length; i++){
                if (Main.LightNCArray[i]){
                    Main.LightNCStatusArray[i] = false;
                }
            }
            for (int i = 0; i < Main.LightNOArray.length; i++){
                if (Main.LightNOArray[i]){
                    Main.LightNOStatusArray[i] = false;
                }
            }

        //If there's a fault on just the input section, input section closes and NO switch remains red. All other sections stay what they were.
        } else if (inputFault) {
            for (int i = 0; i < Main.LightInputArray.length; i++){
                if (Main.LightInputArray[i]){
                    Main.LightInputStatusArray[i] = false;
                }
            }
            //if (ProgramController.inputIncreasing){

            //}
            Main.LightNOStatusArray[0] = false;
        }
        //If there are any faults on NC, all of NC goes Red. Input start block goes Red. NO Start depends on Switch position.
        else if (NCFault){
            for (int i = 0; i < Main.LightNCArray.length; i++){
                if (Main.LightNCArray[i]){
                    Main.LightNCStatusArray[i] = false;
                }
            }
            Main.LightInputStatusArray[0] = false;
        }
        //If there are any faults on NO, all of NO goes Red.
        else if (NOFault){
            for (int i = 0; i < Main.LightNOArray.length; i++){
                if (Main.LightNOArray[i]){
                    Main.LightNOStatusArray[i] = false;
                }
            }
        }



        //Finally, update the GUI
        light_list.getItems().clear();
        for (int i = 0; i < Main.LightInputArray.length;i++){
            if (Main.LightInputArray[i]){//If the block has lights, consider it
                if (Main.LightInputStatusArray[i]){
                    light_list.getItems().add("Block " + String.valueOf(inputArray[i]) + ": Green");
                }
                else{
                    light_list.getItems().add("Block " + String.valueOf(inputArray[i]) + ": Red");
                }
            }
            else{
                light_list.getItems().add("Block " + String.valueOf(inputArray[i]));
            }
        }

        for (int i = 0; i < NCArray.length;i++){
            if (Main.LightNCArray[i]){//If the block has lights, consider it
                if (Main.LightNCStatusArray[i]){
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Green");
                }
                else{
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]) + ": Red");
                }
            }
            else{
                light_list.getItems().add("Block " + String.valueOf(ProgramController.NCArray[i]));
            }
        }

        for (int i = 0; i < NOArray.length;i++){
            if (Main.LightNOArray[i]){//If the block has lights, consider it
                if (Main.LightNOStatusArray[i]){
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Green");
                }
                else{
                    light_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]) + ": Red");
                }
            }
            else{
                light_list.getItems().add("Block " + String.valueOf(ProgramController.NOArray[i]));
            }
        }



    }

    @FXML
    void toggle_occ(ActionEvent event) throws RemoteException {
        String blockID = occ_test.getText();
        Main main1 = new Main();
        main1.setController(this);
        main1.set_Block_State(Integer.parseInt(blockID), occ_setting.isSelected());
    }

    @FXML
    void toggle_fal(ActionEvent event) throws RemoteException {
        String blockID = fal_test.getText();
        Main main1 = new Main();
        main1.setController(this);
        main1.set_Power_Fail(1, Integer.parseInt(blockID), fal_setting.isSelected());
    }

    @FXML
    void button1Enable(MouseEvent event) {
        if (flip3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Entering Manual Mode");
            alert.show();
        }
        switchButton.setVisible(flip3);
        flip3 = !flip3;
    }

}



