package CTC_GUI;

import javafx.application.Application;
import javafx.concurrent.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import networking.*;
import resources.*;




public class Main extends Application {

    public static TextField simulationTime = new TextField("00");


    public static void update_GUI_Time(){
        if(Network.server_Object != null)
        simulationTime.setText(Network.server_Object.get_SimTime_As_LocalTime().toString());

    }

    public void fill_Train_ChoiceBox(ChoiceBox cB){
        if( Network.server_Object != null ){
            cB.getItems().clear();
            if(!Network.server_Object.get_Train_Name_List().isEmpty()) {
                cB.getItems().addAll(Network.server_Object.get_Train_Name_List());
            }
        }
    }

    public void fill_Track_ChoiceBox(ChoiceBox cB){
        if(Network.server_Object != null && !Network.server_Object.get_Line_Name_List().isEmpty()  ){
            cB.getItems().clear();
            cB.getItems().addAll(Network.server_Object.get_Line_Name_List());
        }
    }

    public void fill_Track_Section_ChoiceBox(ChoiceBox child, ChoiceBox parent){
        if( Network.server_Object != null && !Network.server_Object.get_Line_Name_List().isEmpty() ){
            child.getItems().clear();
            int parentIndex = parent.getSelectionModel().getSelectedIndex();
            if(parentIndex > -1)
            child.getItems().addAll(Network.server_Object.get_Line_List().get(parentIndex).get_Section_List());
        }
    }

    public void fill_Track_Block_ChoiceBox(ChoiceBox child, ChoiceBox parent, ChoiceBox superParent){
        if(Network.server_Object != null && !Network.server_Object.get_Line_Name_List().isEmpty() ){
            child.getItems().clear();
            int parentIndex = parent.getSelectionModel().getSelectedIndex();
            int superParentIndex = superParent.getSelectionModel().getSelectedIndex();
            if(parentIndex > -1 && superParentIndex > -1)
                child.getItems().addAll(Network.server_Object.get_Line_List().get(superParentIndex).get_Blocks_In_Section_List(parentIndex));
        }
    }

    public void set_Text_Settings(TextField tF){
        tF.setEditable(false);
        tF.setStyle("-fx-background-color: grey;");
    }

    public void show_Sorry_Automatic_Mode(){
        Alert noServerObjectAlert = new Alert(Alert.AlertType.INFORMATION);
        noServerObjectAlert.setHeaderText("CURRENTLY IN AUTOMATIC MODE");
        noServerObjectAlert.setContentText("For safety reasons, you may not edit the schedule while in automatic mode.");
        noServerObjectAlert.showAndWait();
    }


    public void show_No_ServerObject(){
        Alert noServerObjectAlert = new Alert(Alert.AlertType.ERROR);
        noServerObjectAlert.setHeaderText("NO SERVER OBJECT");
        noServerObjectAlert.setContentText("Please start server before trying this.\n Go to Network>Start Server.");
        noServerObjectAlert.showAndWait();
    }

    public void show_No_TCS_Connection(){
        Alert noServerObjectAlert = new Alert(Alert.AlertType.ERROR);
        noServerObjectAlert.setHeaderText("NO TRACK CONTROLLER CONNECTION");
        noServerObjectAlert.setContentText("Please connect to Track Controller Module before trying this.\n Go to Network>Connect to Track Controller.");
        noServerObjectAlert.showAndWait();
    }

    public void show_No_Sim_Connection(){
        Alert noServerObjectAlert = new Alert(Alert.AlertType.ERROR);
        noServerObjectAlert.setHeaderText("NO SIMULATION CONNECTION");
        noServerObjectAlert.setContentText("Please connect to Track Controller Module before trying this.\n Go to Network>Connect to Simulation Time.");
        noServerObjectAlert.showAndWait();
    }
    
    VBox getBottomTray(HBox Top, HBox Bottom){
        VBox root = new VBox(Top,Bottom);
        return root;
    }

    HBox getRightTray(VBox Right, VBox Left){
        HBox root = new HBox(Right,Left);
        return root;
    }

    HBox getTopTray(MenuBar Right, MenuBar Left){
        HBox.setHgrow(Right,Priority.ALWAYS);
        HBox root = new HBox(Right,Left);

        return root;
    }

    @Override
    public void start(Stage CTCStage) throws Exception{

        CTCStage.setTitle("CTC Office");


//************************************TRAIN INFO BOX CODE START*******************************************************//
        VBox rootTrainInfoBox = new VBox();
        Label trainInfoBoxLabel = new Label("TRAIN INFO:");
        trainInfoBoxLabel.setFont(Font.font("Arial",18));
        Label trainIDLabelInfo = new Label("Train ID:");
        ChoiceBox trainIDInfo = new ChoiceBox();

        trainIDInfo.setOnShowing(e->{
            fill_Train_ChoiceBox(trainIDInfo);
        });

        Label suggestSpeedLabel = new Label("Suggested Speed(MPH):");
        TextField suggestSpeed = new TextField();
        set_Text_Settings(suggestSpeed);

        Label avgSpeedLabel = new Label("Avg Speed(MPH):");
        TextField avgSpeed = new TextField();
        set_Text_Settings(avgSpeed);

        Label authorityLabelInfo = new Label("Authority:");
        TextField authorityInfo = new TextField();
        set_Text_Settings(authorityInfo);

        Label destinationLabel = new Label("Next Stop:");
        TextField destination = new TextField();
        set_Text_Settings(destination);

        Label lineLabel = new Label("Current Line:");
        TextField line = new TextField();
        set_Text_Settings(line);


        Label blockLabel = new Label("Current Block:");
        TextField block = new TextField();
        set_Text_Settings(block);

        Label ticketsLabel = new Label("Tickets per hour:");
        TextField tickets = new TextField();
        set_Text_Settings(tickets);

        Label numberCarsLabel = new Label("Number Of Cars:");
        TextField numberCars = new TextField();
        set_Text_Settings(numberCars);


        trainIDInfo.setOnAction(e->{
            if(trainIDInfo.getSelectionModel().getSelectedIndex() != -1){
                int selectedTrainIndexInfo = trainIDInfo.getSelectionModel().getSelectedIndex();
                suggestSpeed.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Suggest_Speed().toString());
                avgSpeed.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Avg_Speed().toString());
                authorityInfo.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Authority().toString());
                destination.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Next_Infrastructure());
                tickets.setText(String.valueOf(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Tickets_Per_Hour()));
                numberCars.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Number_Of_Cars().toString());
                block.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Current_Block().toString());
                line.setText(Network.server_Object.get_Train_List().get(selectedTrainIndexInfo).get_Current_Line());
            }
        });

        rootTrainInfoBox.setPadding(new Insets(10,10,5,5));
        rootTrainInfoBox.setSpacing(5);

        rootTrainInfoBox.getChildren().addAll(trainInfoBoxLabel,trainIDLabelInfo,trainIDInfo,suggestSpeedLabel,
                suggestSpeed,avgSpeedLabel, avgSpeed,authorityLabelInfo,authorityInfo,destinationLabel,destination,
                lineLabel,line, blockLabel,block,ticketsLabel,tickets, numberCarsLabel,
                numberCars);

//************************************TRAIN INFO BOX CODE END*********************************************************//



//************************************TRAIN SET BOX CODE START********************************************************//

        //TODO make this handle new train. down to 300


        HBox rootTrainSetBox = new HBox();
        rootTrainSetBox.setPadding(new Insets(10,10,5,5));
        rootTrainSetBox.setSpacing(5);


        Label trainSetBoxLabel = new Label("TRAIN SETTINGS:");
        trainSetBoxLabel.setFont(Font.font("Arial",18));
        Label trainIDLabelSet = new Label("Train ID:");

        ChoiceBox trainIDSet = new ChoiceBox();

        trainIDSet.setOnShowing(e->{
            if(Network.server_Object != null) {
                fill_Train_ChoiceBox(trainIDSet);
                trainIDSet.getItems().addAll("NEW TRAIN TR" + Network.server_Object.get_Train_List().size());
            }else{
                show_No_ServerObject();
            }
        });


        //THIS NEEDS TO ONLY POP UP WHEN NEW TRAIN SELECTED
        Label lineIDLabelSet = new Label("Line ID:");
        ChoiceBox lineIDSet = new ChoiceBox();
        lineIDSet.setOnShowing(e->{
            if(Network.server_Object != null){
                //TODO fill choice lines
            }else{
                show_No_ServerObject();
            }


        });


        Label stopLabel = new Label("Next Stop:");
        ChoiceBox stops = new ChoiceBox();

        //TODO this updates wrong when dispatching after arriving. because i'm reseting the array lists when manual dispatching.
        stops.setOnShowing(e->{
            if(trainIDSet.getSelectionModel().getSelectedIndex() != -1) {
                stops.getItems().clear();

               //wrong -> stops.getItems().addAll(Network.server_Object.get_Train_List().get(trainIDSet.getSelectionModel().getSelectedIndex()).get_c());
                //stops.getItems().addAll(Network.server_Object.)
            }
        });

        Label timeLabel = new Label("Arrival Time:");
        TextField timeArrival = new TextField("HH:mm");
        timeArrival.setMaxWidth(100);
        Button setButton = new Button("DISPATCH");// set button needs to calculate speed and authority based on current block and next block



        rootTrainSetBox.getChildren().addAll(trainSetBoxLabel,trainIDLabelSet,trainIDSet,stopLabel,stops,timeLabel,timeArrival, setButton);

        //TODO THINGS NEEDED FOR NEW TRAIN: Current line, inline block-infrastructure-time lists
        //idea all trains have same infrastructure and block lists so get from an existing and copy over
        setButton.setOnAction(e-> {
               if (trainIDSet.getSelectionModel().getSelectedIndex() != -1) {
                   if (Network.server_Object != null) {
                       if (!Network.server_Object.get_Automatic()) {

                           System.out.println("TRAIN BEING DISPATCHED " + Network.server_Object.get_Train_List().get(trainIDSet.getSelectionModel().getSelectedIndex()).get_Name());
                           int trainNumberFromName = trainIDSet.getSelectionModel().getSelectedIndex();
                           //System.out.println("Train Number: " + trainNumberFromName);
                           Network.server_Object.get_Train_List().get(trainNumberFromName).add_Time(LocalTime.parse(timeArrival.getText()));
                           try {
                               Network.server_Object.dispatch(trainNumberFromName, stops.getSelectionModel().getSelectedItem().toString(), LocalTime.parse(timeArrival.getText()));
                           } catch (RemoteException | InterruptedException remoteException) {
                               remoteException.printStackTrace();
                           }
                       } else {
                          show_Sorry_Automatic_Mode();
                       }
                   } else {
                       show_No_ServerObject();
                   }
               }
        });


//************************************TRAIN SET BOX CODE END**********************************************************//




//************************************TRACK INFO BOX CODE START*******************************************************//
        VBox rootTrackInfoBox = new VBox();
        Label trackInfoBoxLabel = new Label("TRACK INFO:");
        trackInfoBoxLabel.setFont(Font.font("Arial",18));

        Label trackLineLabelInfo = new Label("Track Line:");
        ChoiceBox trackLineInfo = new ChoiceBox();
        trackLineInfo.setOnShowing(e->{
            fill_Track_ChoiceBox(trackLineInfo);
        });

        Label trackSectionLabelInfo = new Label("Track Section:");
        ChoiceBox trackSectionInfo = new ChoiceBox();
        trackSectionInfo.setOnShowing(e->{
            fill_Track_Section_ChoiceBox(trackSectionInfo,trackLineInfo);
        });

        Label trackBlockLabelInfo = new Label("Track Block:");
        ChoiceBox trackBlockInfo = new ChoiceBox();
        trackBlockInfo.setOnShowing(e->{
            fill_Track_Block_ChoiceBox(trackBlockInfo,trackSectionInfo,trackLineInfo);
        });

        Button displayTrackInfo = new Button("SHOW INFO");

        Label blockLengthLabel = new Label("Block Length (M):");
        TextField blockLength = new TextField();
        set_Text_Settings(blockLength);

        Label blockGradeLabel = new Label("Block Grade %:");
        TextField blockGrade = new TextField();
        set_Text_Settings(blockGrade);

        Label speedLimitLabel = new Label("Speed Limit (MPH):");
        TextField speedLimit = new TextField();
        set_Text_Settings(speedLimit);

        Label infrastructureLabel = new Label("Infrastructure:");
        TextField infrastructure = new TextField();
        set_Text_Settings(infrastructure);

        Label stationSideLabel = new Label("Station Side:");
        TextField stationSide = new TextField();
        set_Text_Settings(stationSide);


        Label elevationLabel = new Label("Elevation (FT):");
        TextField elevation = new TextField();
        set_Text_Settings(elevation);

        Label cumulativeElevationLabel = new Label("Cumulative Elevation (FT):");
        TextField cumulativeElevation = new TextField();
        set_Text_Settings(cumulativeElevation);

        Label blockOccupancyLabel = new Label("Block Occupancy:");
        TextField blockOccupancy = new TextField();
        set_Text_Settings(blockOccupancy);

        Label blockConditionLabel = new Label("Block Condition:");
        TextField blockCondition = new TextField();
        set_Text_Settings(blockCondition);

        Label lightsLabel = new Label("Lights");
        TextField lights = new TextField();
        set_Text_Settings(lights);

        Label crossBarLabel = new Label("Cross Bar");
        TextField crossBar = new TextField();
        set_Text_Settings(crossBar);


        rootTrackInfoBox.setPadding(new Insets(5,10,2,10));
        rootTrackInfoBox.setSpacing(2);
        rootTrackInfoBox.getChildren().addAll(trackInfoBoxLabel,trackLineLabelInfo,trackLineInfo,trackSectionLabelInfo,
                trackSectionInfo, trackBlockLabelInfo,trackBlockInfo,displayTrackInfo,blockLengthLabel,blockLength,
                blockGradeLabel,blockGrade, speedLimitLabel,speedLimit,infrastructureLabel,infrastructure,stationSideLabel,stationSide,
                elevationLabel,elevation,cumulativeElevationLabel,cumulativeElevation,blockOccupancyLabel,
                blockOccupancy,blockConditionLabel,blockCondition,lightsLabel,lights,crossBarLabel,crossBar);

        displayTrackInfo.setOnAction(e->{
            blockLength.setText("N/A");
            blockGrade.setText("N/A");
            speedLimit.setText("N/A");
            infrastructure.setText("N/A");
            elevation.setText("N/A");
            cumulativeElevation.setText("N/A");
            blockOccupancy.setText("N/A");
            blockCondition.setText("N/A");
            if(trackLineInfo.getSelectionModel().getSelectedIndex() > -1 && trackBlockInfo.getSelectionModel().getSelectedIndex() > -1 && trackSectionInfo.getSelectionModel().getSelectedIndex() >-1) {
                Line selectedLine = Network.server_Object.get_Line_List().get(trackLineInfo.getSelectionModel().getSelectedIndex());
                List<String> selectedBlock = selectedLine.get_Block_Information_List(Integer.parseInt(trackBlockInfo.getSelectionModel().getSelectedItem().toString()));
                blockLength.setText(selectedBlock.get(0));
                blockGrade.setText(selectedBlock.get(1));
                speedLimit.setText(selectedBlock.get(2));
                infrastructure.setText(selectedBlock.get(3));
                elevation.setText(selectedBlock.get(4));
                cumulativeElevation.setText(selectedBlock.get(5));
                blockOccupancy.setText(selectedBlock.get(6));
                blockCondition.setText(selectedBlock.get(7));
                lights.setText(selectedBlock.get(8));
                crossBar.setText(selectedBlock.get(9));
                stationSide.setText(selectedBlock.get(10));

            }else{
                Alert noServerObjectAlert = new Alert(Alert.AlertType.WARNING);
                noServerObjectAlert.setHeaderText("NO SELECTION");
                noServerObjectAlert.setContentText("Please make selection.\nCheck these possibilities:\n1) Make sure the server is up by: Network>Start Server\n2) Make sure the Track is imported by: Track>Import Track\n3) Make selection in each choice box\n Note: You must choose a line, a section, and a block.");
                noServerObjectAlert.showAndWait();
            }
        });

//************************************TRACK INFO BOX CODE END*********************************************************//


//************************************TRACK SET BOX CODE START********************************************************//
        HBox rootTrackSetBox = new HBox();
        Label trackSetBoxLabel = new Label("TRACK SETTINGS:");
        trackSetBoxLabel.setFont(Font.font("Arial",18));

        Label trackLineLabelSet = new Label("Track Line:");
        ChoiceBox trackLineSet = new ChoiceBox();

        trackLineSet.setOnShowing(e->{
            fill_Track_ChoiceBox(trackLineSet);
        });

        Label trackSectionLabelSet = new Label("Track Section:");
        ChoiceBox trackSectionSet = new ChoiceBox();

        trackSectionSet.setOnShowing(e->{
            fill_Track_Section_ChoiceBox(trackSectionSet,trackLineSet);
        });

        Label trackBlockLabelStartSet = new Label("Start Block: ");
        ChoiceBox trackBlockStartSet = new ChoiceBox();

        trackBlockStartSet.setOnShowing(e->{
            fill_Track_Block_ChoiceBox(trackBlockStartSet,trackSectionSet,trackLineSet);
        });

        Label trackBlockLabelEndSet = new Label("End Block: ");
        ChoiceBox trackBlockEndSet = new ChoiceBox();

        trackBlockEndSet.setOnShowing(e->{
            fill_Track_Block_ChoiceBox(trackBlockEndSet,trackSectionSet,trackLineSet);
        });

        Button openTrackButton = new Button("OPEN");
        Button closeTrackButton = new Button("CLOSE");

        rootTrackSetBox.setSpacing(10);
        rootTrackSetBox.setPadding(new Insets(10,10,10,10));
        rootTrackSetBox.getChildren().addAll(trackSetBoxLabel,trackLineLabelSet,trackLineSet,trackSectionLabelSet,
                trackSectionSet,trackBlockLabelStartSet,trackBlockStartSet,trackBlockLabelEndSet,trackBlockEndSet,
                openTrackButton,closeTrackButton);


        openTrackButton.setOnAction(e->{
            if(Network.server_Object != null) {
                if (!Network.server_Object.get_Automatic()) {
                    if (trackBlockEndSet.getSelectionModel().getSelectedIndex() != -1 && trackBlockStartSet.getSelectionModel().getSelectedIndex() != -1) {

                            if (Network.tcsw_Interface != null) {
                                int startBlock = Integer.parseInt(trackBlockStartSet.getSelectionModel().getSelectedItem().toString());
                                int endBlock = Integer.parseInt(trackBlockEndSet.getSelectionModel().getSelectedItem().toString());

                                for (int i = startBlock; i < endBlock + 1; i++) {
                                    String trackLine = trackLineSet.getSelectionModel().getSelectedItem().toString();
                                    try {
                                        Network.server_Object.open_Block(trackLine, i);
                                    } catch (RemoteException remoteException) {
                                        remoteException.printStackTrace();
                                    }

                                    try {

                                        Network.tcsw_Interface.open_Block(trackLine, i);

                                    } catch (RemoteException | FileNotFoundException remoteException) {
                                        remoteException.printStackTrace();
                                    }


                                }
                            } else {
                                show_No_TCS_Connection();
                            }

                    }
                } else {
                    show_Sorry_Automatic_Mode();
                }
            }else{
                show_No_ServerObject();
            }
        });

        closeTrackButton.setOnAction(e->{
            if(!Network.server_Object.get_Automatic()) {
                if(trackBlockEndSet.getSelectionModel().getSelectedIndex() != -1 && trackBlockStartSet.getSelectionModel().getSelectedIndex() != -1) {

                    if(Network.tcsw_Interface != null){
                        int startBlock = Integer.parseInt(trackBlockStartSet.getSelectionModel().getSelectedItem().toString());
                        int endBlock = Integer.parseInt(trackBlockEndSet.getSelectionModel().getSelectedItem().toString());

                        String trackLine = trackLineSet.getSelectionModel().getSelectedItem().toString();

                        for (int i = startBlock; i < endBlock + 1; i++) {
                            try {
                                Network.server_Object.close_Block(trackLine, i);
                            } catch (RemoteException remoteException) {
                                remoteException.printStackTrace();
                            }

                            try {
                                Network.tcsw_Interface.close_Block(trackLine, i);
                            } catch (RemoteException remoteException) {
                                remoteException.printStackTrace();
                            }


                        }
                    }else{
                        show_No_TCS_Connection();
                    }
                }
            }else{
                show_Sorry_Automatic_Mode();
            }
        });

//************************************TRACK SET BOX CODE END**********************************************************//



//************************************MENU BAR CODE START*************************************************************//

        MenuBar rootMenuBarLeft = new MenuBar();

        Menu file = new Menu("_File");
        MenuItem testWindow = new MenuItem("_Overrider");
        //MenuItem trainNew = new MenuItem("_Train New");
        MenuItem closeStageButton = new MenuItem("_Close");
        file.getItems().addAll(testWindow,closeStageButton);


        Menu scheduleMenu = new Menu("_Schedule");
        MenuItem scheduleEdit = new MenuItem("_Edit Schedule");
        MenuItem scheduleView = new MenuItem("_View Schedule");
       // MenuItem trainNew = new MenuItem("_Add Train");
        MenuItem scheduleImport = new MenuItem("_Import Schedule");
        scheduleMenu.getItems().addAll(scheduleImport);


        Menu trackMenu = new Menu("_Track");
        MenuItem trackImport = new MenuItem("_Import Track");
        MenuItem switchTest = new MenuItem("_Test Switch");
        trackMenu.getItems().addAll(trackImport);


        Menu networkMenu = new Menu("_Network");
        MenuItem serverNetworkMenu = new MenuItem("_Start Server");
        MenuItem moduleTrackControllerNetworkMenu = new MenuItem("Connect to Track _Controller");
        MenuItem moduleSimNetworkMenu = new MenuItem("Connect to Simulation _Time");


        networkMenu.getItems().addAll(serverNetworkMenu, moduleTrackControllerNetworkMenu,moduleSimNetworkMenu);


        Menu timeMenu = new Menu("T_ime");
        MenuItem simStart       = new MenuItem("_Start Simulation Time");
        MenuItem simSetMult1 = new MenuItem("Multiplier Set to 1");
        MenuItem simSetMult10 = new MenuItem("Multiplier Set to 10");
        MenuItem simSetMult20 = new MenuItem( "Multiplier Set to 20");
        MenuItem simSetMult50 = new MenuItem( "Multiplier Set to 50");
        MenuItem simPause = new MenuItem("_Pause Simulation Time");
        MenuItem simResume = new MenuItem("_Resume Simulation Time");
        MenuItem simReset = new MenuItem("Reset Simulation Time");
        timeMenu.getItems().addAll( simStart, simSetMult1, simSetMult10, simSetMult20, simSetMult50, simPause, simResume,simReset);


        rootMenuBarLeft.setMaxHeight(5);
        rootMenuBarLeft.getMenus().addAll(file,scheduleMenu, trackMenu,networkMenu,timeMenu);



        MenuBar rootMenuBarRight = new MenuBar();

        Menu serverUpLabel = new Menu("Server Down");
        serverUpLabel.setStyle("-fx-background-color: red;");

        Menu clientTCSUpLabel = new Menu( "Track Controller: Not Connected");
        clientTCSUpLabel.setStyle("-fx-background-color: red;");

        Menu clientSimUpLabel = new Menu( "Simulation Time: Not Connected");
        clientSimUpLabel.setStyle("-fx-background-color: red;");


        Menu currentModeLabel = new Menu( "Manual Mode");
        currentModeLabel.setStyle("-fx-background-color: pink");

        rootMenuBarRight.getMenus().addAll(serverUpLabel,clientTCSUpLabel,clientSimUpLabel,currentModeLabel);



//************************************MENU BAR CODE STOP**************************************************************//
        testWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage test = new Stage();
                test.setTitle("OVERRIDER");
                test.show();
                Button createTrainOverride = new Button("Create Train");
                Button sendSpeedAuthority = new Button("Send Speed and Authority");

                TextField carsField = new TextField("Enter # of Cars");
                TextField lineField = new TextField("Enter Line for train");
                TextField blockField = new TextField("Enter Block for train to start on");

                TextField trainIdField = new TextField("Enter Train ID '#'");
                TextField speedField = new TextField("Enter Speed");
                TextField authorityField = new TextField("Enter Authority");
                VBox rootV = new VBox();
                rootV.setSpacing(10);

                rootV.getChildren().addAll(carsField,lineField,blockField,trainIdField,speedField,authorityField,
                        createTrainOverride, sendSpeedAuthority);

                Scene scene1 = new Scene(rootV, 700, 450);
                test.setScene(scene1);
                sendSpeedAuthority.setOnAction(e->{
                    int trainNum =Integer.parseInt( trainIdField.getText());
                    double speed = Double.parseDouble(speedField.getText());
                    int authority = Integer.parseInt(authorityField.getText());

                    try {
                        Network.tcsw_Interface.send_Speed_Authority(trainNum,speed,authority);
                    } catch (RemoteException | InterruptedException remoteException) {
                        remoteException.printStackTrace();
                    }
                });
                createTrainOverride.setOnAction(e->{
                    int cars =Integer.parseInt( carsField.getText());
                    String line =lineField.getText();
                    int block = Integer.parseInt(blockField.getText());

                    try {
                        Network.tcsw_Interface.create_Train(cars, line, block);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                });

            }
        });

        serverNetworkMenu.setOnAction(e->{
            try {
                Network.start_Server();
                serverNetworkMenu.setDisable(true);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            if(Network.server_Object != null){
                serverUpLabel.setStyle("-fx-background-color: green;");
                serverUpLabel.setText("Server Up");
            }else{
                Alert noServerObjectAlert = new Alert(Alert.AlertType.ERROR);
                noServerObjectAlert.setHeaderText("NO SERVER OBJECT");
                noServerObjectAlert.setContentText("Please call 7249961300");
                noServerObjectAlert.showAndWait();
            }

        });

        moduleSimNetworkMenu.setOnAction(e->{

            Alert noServerObjectAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noServerObjectAlert.setHeaderText("PROGRAM COULD CRASH: SIMULATION TIME MUST BE ONLINE");
            noServerObjectAlert.setContentText("ARE YOU SURE?");
            Optional<ButtonType> result = noServerObjectAlert.showAndWait();

            if(result.get() == ButtonType.OK){
                Network.connect_To_Simulation();
                if(Network.Simulation_Interface != null){
                    clientSimUpLabel.setStyle("-fx-background-color: green;");
                    clientSimUpLabel.setText("Simulation Time: Connected");
                }else{
                   show_No_Sim_Connection();
                }
            }



        });

        simStart.setOnAction(e->{

            if(Network.Simulation_Interface != null) {
                try {
                    Network.Simulation_Interface.start_Simulation_Time(1);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
            /*

            Task task = new Task<Void>() {
                @Override public Void call() throws InterruptedException {

                    for(double i = 0; i <4000; i++) {

                        try {

                            Network.server_Object.update_Time(i);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Thread.sleep(500);
                    }
                    return null;
                }
            };
            new Thread(task).start(); */

        });

        simSetMult1.setOnAction(e->{
            if(Network.Simulation_Interface != null){
                try {
                    Network.Simulation_Interface.update_Multiplier(1);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
        });

        simSetMult10.setOnAction(e->{
            if(Network.Simulation_Interface != null){

                try {
                    Network.Simulation_Interface.update_Multiplier(10);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }


            }else{
                show_No_Sim_Connection();
            }

        });

        simSetMult20.setOnAction(e->{
            if(Network.Simulation_Interface != null){
                try {
                    Network.Simulation_Interface.update_Multiplier(20);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
        });

        simSetMult50.setOnAction(e->{
            if(Network.Simulation_Interface != null){
                try {
                    Network.Simulation_Interface.update_Multiplier(50);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
        });


        simPause.setOnAction(e->{
            if(Network.Simulation_Interface != null){
                try {
                    Network.Simulation_Interface.pause_Simulation();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
        });

        simResume.setOnAction(e->{
            if(Network.Simulation_Interface != null){
                try {
                    Network.Simulation_Interface.resume_Simulation();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
        });

        simReset.setOnAction(e->{
            if(Network.Simulation_Interface != null){
                try {
                    Network.Simulation_Interface.reset_Simulation();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }else{
                show_No_Sim_Connection();
            }
        });

        moduleTrackControllerNetworkMenu.setOnAction((e->{
            Alert noServerObjectAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noServerObjectAlert.setHeaderText("PROGRAM COULD CRASH: TRACK CONTROLLER MUST BE ONLINE");
            noServerObjectAlert.setContentText("ARE YOU SURE?");
            Optional<ButtonType> result = noServerObjectAlert.showAndWait();

            if(result.get() == ButtonType.OK){
                Network.connect_To_Module();
                if(Network.tcsw_Interface != null){
                    clientTCSUpLabel.setStyle("-fx-background-color: green;");
                    clientTCSUpLabel.setText("Track Controller: Connected");
                }else{
                    show_No_TCS_Connection();
                }
            }



        }));

        closeStageButton.setOnAction(e->{
            CTCStage.close();
        });

        scheduleImport.setOnAction(e->{
            if(Network.server_Object != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Schedule File");
                fileChooser.setInitialDirectory(new File("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\"));
                File selectedFile = fileChooser.showOpenDialog(CTCStage);

                Network.server_Object.set_Schedule_Path(selectedFile.getAbsolutePath());

                //if (selectedFile != null) CTCStage.display(selectedFile);

                //Network.server_Object.clear_Train_List();
                try {
                    Network.server_Object.import_Train_Schedule();
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                scheduleMenu.getItems().addAll(scheduleEdit,scheduleView);//,trainNew);


            }else{
                show_No_ServerObject();
            }
        });

        trackImport.setOnAction(e->{
            if(Network.server_Object != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Track File");
                fileChooser.setInitialDirectory(new File("C:\\Users\\Zachary\\Documents\\GitHub\\ECE1140_project_butter\\CTC\\TEST_02\\src\\resources\\"));
                // fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"))
                File selectedFile = fileChooser.showOpenDialog(CTCStage);

                Network.server_Object.set_Track_Path(selectedFile.getAbsolutePath());

                // Network.server_Object.clear_Line_List();
                try {
                    Network.server_Object.import_Track_File();
                    trackMenu.getItems().addAll(switchTest);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

            }else{
                show_No_ServerObject();
            }



        });

        switchTest.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent actionEvent) {
                if(Network.server_Object != null) {

                        if(Network.server_Object.get_Line_List().size() > 0){
                            Stage switchTest = new Stage();

                            Label switchTestResult = new Label ("SWITCH RESULT HERE");
                            switchTestResult.setStyle("-fx-background-color: purple;");



                            switchTest.setTitle("Switch Tester");



                            Label trackLineLabelSwitchTest = new Label("Track Line:");
                            ChoiceBox trackLineSwitchTest = new ChoiceBox();
                            trackLineSwitchTest.setOnShowing(e->{
                                fill_Track_ChoiceBox(trackLineSwitchTest);
                            });


                            Label trackBlockLabelSwitchTest = new Label("Track Blocks That Have Switch:");
                            ChoiceBox trackBlockSwitchTest = new ChoiceBox();
                            trackBlockSwitchTest.setOnShowing(e->{
                                //
                                switchTestResult.setText("SWITCH RESULT HERE");
                                switchTestResult.setStyle("-fx-background-color: purple;");

                                if(Network.tcsw_Interface != null){
                                    //trackBlockSwitchTest.getItems().clear();
                                    if(trackLineSwitchTest.getSelectionModel().getSelectedIndex() != -1) {

                                        trackBlockSwitchTest.getItems().addAll(Network.server_Object.get_Line_List().get(trackLineSwitchTest.getSelectionModel().getSelectedIndex()).get_Blocks_Are_Switch_List());
                                    }
                                }else{
                                    show_No_TCS_Connection();
                                }

                            });



                            Button testSwitchTest = new Button("Test Switch");
                            testSwitchTest.setOnAction(e->{
                                int blockNumSwitchTest = Integer.valueOf((Integer) trackBlockSwitchTest.getSelectionModel().getSelectedItem());
                                System.out.println(blockNumSwitchTest);
                                System.out.println(trackLineSwitchTest.getSelectionModel().getSelectedItem().toString());
                                if(trackLineSwitchTest.getSelectionModel().getSelectedIndex() != -1
                                        && blockNumSwitchTest!= -1){
                                    try {
                                        if(Network.server_Object.test_Switch(
                                                trackLineSwitchTest.getSelectionModel().getSelectedItem().toString(),
                                                blockNumSwitchTest)){

                                            switchTestResult.setText("SWITCH SUCCESSFUL");
                                            switchTestResult.setStyle("-fx-background-color: green;");


                                        }else{
                                            switchTestResult.setText("SWITCH FAILED");
                                            switchTestResult.setStyle("-fx-background-color: red;");

                                        }
                                    } catch (RemoteException remoteException) {
                                        remoteException.printStackTrace();
                                    }


                                }

                            });



                            VBox rootSwitchTest = new VBox(trackLineLabelSwitchTest,trackLineSwitchTest,trackBlockLabelSwitchTest,trackBlockSwitchTest,testSwitchTest,switchTestResult);
                            rootSwitchTest.setPadding(new Insets(20,20,20,20));
                            rootSwitchTest.setSpacing(15);

                            Scene sceneSwitchTest = new Scene(rootSwitchTest, 250, 230);

                            switchTest.setScene(sceneSwitchTest);
                            switchTest.show();
                        }


                }else{
                    show_No_ServerObject();
                }

            }
        });


//////////////////////////////////SCHEDULE VIEW//////////////////////////////////////////




        scheduleView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage scheduleStageView = new Stage();
                scheduleStageView.setTitle("Schedule Viewer");

                HBox rootScheduleView = new HBox();

                VBox verticalLeft = new VBox();
                VBox verticalCenter = new VBox();
                VBox verticalRight = new VBox();

                Label trainIDLabelScheduleView = new Label("Train ID:");
                ChoiceBox trainIDScheduleView = new ChoiceBox();
                trainIDScheduleView.setOnShowing(e->{
                    fill_Train_ChoiceBox(trainIDScheduleView);
                });

                Label infrastructureLabelScheduleView = new Label("Infrastructure List:");
                TextArea infrastructureScheduleView = new TextArea();
                infrastructureScheduleView.setEditable(false);
                infrastructureScheduleView.setStyle("-fx-background-color: transparent;");
                infrastructureScheduleView.setMaxWidth(300);
                infrastructureScheduleView.setMinHeight(1000);

                Label  timeLabelScheduleView = new Label("Time List:");
                TextArea timeScheduleView = new TextArea();
                timeScheduleView.setEditable(false);
                timeScheduleView.setStyle("-fx-background-color: transparent;");
                timeScheduleView.setMaxWidth(100);
                timeScheduleView.setMinHeight(1000);

                verticalCenter.getChildren().addAll(infrastructureLabelScheduleView, infrastructureScheduleView);
                verticalLeft.getChildren().addAll(trainIDLabelScheduleView, trainIDScheduleView);
                verticalRight.getChildren().addAll(timeLabelScheduleView,timeScheduleView);



                trainIDScheduleView.setOnAction(e->{
                    if(trainIDScheduleView.getSelectionModel().getSelectedIndex() != -1) {
                        Integer trainSelectedIndex = trainIDScheduleView.getSelectionModel().getSelectedIndex();
                        //System.out.println(trainSelectedIndex);
                        Train trainSelected = Network.server_Object.get_Train_List().get(trainSelectedIndex);
                        // System.out.println(trainSelected.getInfrastructureList());
                        Integer size = trainSelected.get_Infrastructure_List().size();
                        for (int i = 0; i < size; i++) {
                            if (i > 0) {
                                infrastructureScheduleView.setText(infrastructureScheduleView.getText() + "\n" + trainSelected.get_Infrastructure_List().get(i));
                                timeScheduleView.setText(timeScheduleView.getText() + "\n" + trainSelected.get_Time_List().get(i));
                            } else {
                                infrastructureScheduleView.setText(trainSelected.get_Infrastructure_List().get(i));
                                timeScheduleView.setText(trainSelected.get_Time_List().get(i).toString());
                            }
                        }
                    }
                });

                rootScheduleView.getChildren().addAll(verticalLeft,verticalCenter,verticalRight);
                Scene scene1 = new Scene(rootScheduleView, 600, 700);
                scheduleStageView.setScene(scene1);
                scheduleStageView.show();
            }

        });

        //**************************************SCHEDULE EDIT WINDOW*******************************************

        scheduleEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!Network.server_Object.get_Automatic()) {
                    Stage scheduleStageEdit = new Stage();
                    scheduleStageEdit.setTitle("Schedule Editor");

                    HBox rootScheduleEdit = new HBox();

                    VBox verticalLeft = new VBox();
                    verticalLeft.setSpacing(15);
                    VBox verticalCenter = new VBox();
                    VBox verticalRight = new VBox();

                    Label trainIDLabelScheduleEdit = new Label("Train ID:");
                    ChoiceBox trainIDScheduleEdit = new ChoiceBox();
                    trainIDScheduleEdit.setOnShowing(e -> {
                        fill_Train_ChoiceBox(trainIDScheduleEdit);
                    });

                    Label infrastructureLabelScheduleEdit = new Label("Infrastructure List:");
                    TextArea infrastructureScheduleEdit = new TextArea();
                    infrastructureScheduleEdit.setEditable(false);
                    infrastructureScheduleEdit.setStyle("-fx-background-color: transparent;");
                    infrastructureScheduleEdit.setMaxWidth(300);
                    infrastructureScheduleEdit.setMinHeight(1000);


                    Label timeLabelScheduleEdit = new Label("Time List:");
                    TextArea timeScheduleEdit = new TextArea();

                    timeScheduleEdit.setStyle("-fx-background-color: transparent;");
                    timeScheduleEdit.setMaxWidth(100);
                    timeScheduleEdit.setMinHeight(1000);

                    Button applyScheduleEdit = new Button("APPLY CHANGES");
                    Label saveScheduleEdit = new Label("Unsaved");
                    Label saveScheduleEditNote = new Label("Note: These changes do not write into schedule.txt");
                    saveScheduleEditNote.setWrapText(true);

                    verticalCenter.getChildren().addAll(infrastructureLabelScheduleEdit, infrastructureScheduleEdit);
                    verticalLeft.getChildren().addAll(trainIDLabelScheduleEdit, trainIDScheduleEdit, applyScheduleEdit, saveScheduleEdit, saveScheduleEditNote);
                    verticalRight.getChildren().addAll(timeLabelScheduleEdit, timeScheduleEdit);


                    trainIDScheduleEdit.setOnAction(e -> {
                        if (trainIDScheduleEdit.getSelectionModel().getSelectedIndex() != -1) {
                            Integer trainSelectedIndex = trainIDScheduleEdit.getSelectionModel().getSelectedIndex();
                            //System.out.println(trainSelectedIndex);
                            Train trainSelected = Network.server_Object.get_Train_List().get(trainSelectedIndex);
                            // System.out.println(trainSelected.getInfrastructureList());
                            Integer size = trainSelected.get_Infrastructure_List().size();
                            for (int i = 0; i < size; i++) {
                                if (i > 0) {
                                    infrastructureScheduleEdit.setText(infrastructureScheduleEdit.getText() + "\n" +
                                            trainSelected.get_Infrastructure_List().get(i));
                                    timeScheduleEdit.setText(timeScheduleEdit.getText() + "\n" +
                                            trainSelected.get_Time_List().get(i));
                                } else {
                                    infrastructureScheduleEdit.setText(trainSelected.get_Infrastructure_List().get(i));
                                    timeScheduleEdit.setText(trainSelected.get_Time_List().get(i).toString());
                                }
                            }
                        }
                        saveScheduleEdit.setText("Unsaved");
                    });

                    timeScheduleEdit.setOnMouseClicked(e -> {
                        saveScheduleEdit.setText("Unsaved");
                    });

                    applyScheduleEdit.setOnAction(e -> {

                        if (trainIDScheduleEdit.getSelectionModel().getSelectedIndex() != -1) {
                            Integer trainSelectedIndex = trainIDScheduleEdit.getSelectionModel().getSelectedIndex();


                            saveScheduleEdit.setText("Saving...");
                            String timeStringTemp = timeScheduleEdit.getText();
                            String[] timeStringArray = timeStringTemp.split("\n");
                            Network.server_Object.get_Train_List().get(trainSelectedIndex).clear_Time_List();
                            for (String s : timeStringArray) {
                                Network.server_Object.get_Train_List().get(trainSelectedIndex).add_Time(LocalTime.parse(s));

                            }
                            //System.out.println(Network.server_Object.get_Train_List().get(trainSelectedIndex).get_Time_List());
                            saveScheduleEdit.setText("Saved");
                        }

                    });

                    rootScheduleEdit.getChildren().addAll(verticalLeft, verticalCenter, verticalRight);
                    Scene scene1 = new Scene(rootScheduleEdit, 600, 700);
                    scheduleStageEdit.setScene(scene1);
                    scheduleStageEdit.show();

                    }else{
                        show_Sorry_Automatic_Mode();
                    }


                }
        });





        //****************************************TRAIN CREATOR WINDOW*****************************************
        /*
        trainNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                int HOLDIT = (Network.server_Object.get_Train_List().size());
                Train t_new = new Train("TR" + HOLDIT);

                Stage trainStage = new Stage();

                trainStage.setTitle("Train Creator");

                VBox rootTrainInfoBoxNew = new VBox();
                Label trainInfoBoxLabelNew = new Label("TRAIN CREATOR:");
                trainInfoBoxLabelNew.setFont(Font.font("Arial",18));

                Label trainIDLabelInfoNew = new Label("Train ID:");
                TextField trainIDNew = new TextField();
                trainIDNew.setText(t_new.get_Name());
                set_Text_Settings(trainIDNew);



                Button setTrainScheduleNew = new Button("Create Schedule");

                Button setTrainNew = new Button("Dispatch Train");
                //setTrainNew.setDisable(true);
                setTrainNew.setDisable(false);
                setTrainNew.setStyle("-fx-background-color: grey;");

                rootTrainInfoBoxNew.setPadding(new Insets(10,10,10,10));
                rootTrainInfoBoxNew.setSpacing(10);
                rootTrainInfoBoxNew.getChildren().addAll(trainInfoBoxLabelNew,trainIDLabelInfoNew,trainIDNew,
                        setTrainScheduleNew,setTrainNew);

                Scene scene1 = new Scene(rootTrainInfoBoxNew, 250, 360);

                trainStage.setMinWidth(270);
                trainStage.setMinHeight(395);
                trainStage.setScene(scene1);
                trainStage.show();

                setTrainScheduleNew.setOnAction(new EventHandler<ActionEvent>(){
                    //open scheduler make it forefront

                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Stage scheduleStage = new Stage();

                        scheduleStage.setTitle("SCHEDULER");

                        HBox rootScheduler = new HBox();

                        VBox verticalLeft = new VBox();

                        VBox verticalRight = new VBox();

                        Label trainIDLabelScheduler = new Label("Train ID:");
                        ChoiceBox trainIDScheduler = new ChoiceBox();
                        trainIDScheduler.getItems().addAll(t_new.get_Name());


                        Label trackIDLabelScheduler = new Label("Track ID:");
                        ChoiceBox trackIDScheduler = new ChoiceBox();

                        trackIDScheduler.setOnShowing(e->{
                            fill_Track_ChoiceBox(trackIDScheduler);
                        });

                        Button applyScheduler = new Button("Apply");

                        List<TextField> timesScheduler = new ArrayList<>();
                        for(int i = 0; i < 30; i++){
                            TextField txt = new TextField();
                            txt.setVisible(false);
                            txt.setPromptText("Time: \"00::00\"");
                            timesScheduler.add(txt);
                        }

                        TextArea infrastructureScheduler = new TextArea();
                        infrastructureScheduler.setEditable(false);
                        infrastructureScheduler.setStyle("-fx-background-color: transparent;");
                        infrastructureScheduler.setMaxWidth(200);

                        verticalLeft.getChildren().addAll(trackIDLabelScheduler, trackIDScheduler, infrastructureScheduler);
                        verticalRight.getChildren().addAll(trainIDLabelScheduler, trainIDScheduler);
                        verticalRight.getChildren().addAll(timesScheduler);

                        /*
                        trackIDScheduler.setOnAction(e->{

                            Integer trackSelectedIndex = trackIDScheduler.getSelectionModel().getSelectedIndex();
                            Line lineSelected = Network.server_Object.get_Line_List().get(trackSelectedIndex);
                            Integer size = Network.server_Object.get_Infrastructure_Of(lineSelected).size();
                            for(int i = 0; i < size; i++){
                                timesScheduler.get(i).setVisible(true);
                                if(i > 0)
                                    infrastructureScheduler.setText(infrastructureScheduler.getText()+"\n"+Network.server_Object.get_Infrastructure_Of(Network.server_Object.get_Line_List().get(trackSelectedIndex)).get(i));
                                else
                                    infrastructureScheduler.setText(Network.server_Object.get_Infrastructure_Of(Network.server_Object.get_Line_List().get(trackSelectedIndex)).get(i));
                            }

                        });



                        //THIS STUFF ONLY WORKS FOR ONE TRACK NEEDS TO HAVE STUFF LIKE ADD INFRASTRUCTURE OR ADD TIMES
                        //AND CREATE IT SO YOU CAN APPLY THEN SELECT A DIFFERENT LINE SET TIMES FOR THE SAME TRAIN
                        //ETC
                        applyScheduler.setOnAction(e->{
                            Integer trackSelectedIndex = trackIDScheduler.getSelectionModel().getSelectedIndex();

                            Line lineSelected = Network.server_Object.get_Line_List().get(trackSelectedIndex);

                            Integer size = Network.server_Object.get_Infrastructure_Of(lineSelected).size();

                            List<LocalTime> times = new ArrayList<>();

                            for(int j = 0; j < size; j++){
                                LocalTime t ;
                                if(timesScheduler.get(j).getText() != ""){
                                    t = LocalTime.parse(timesScheduler.get(j).getText());
                                    times.add(t);
                                }
                            }

                            t_new.set_Time_List(times);
                            t_new.set_Infrastructure_List(lineSelected.get_Infrastructure_List());

                            // Thread.sleep(3000);
                            infrastructureScheduler.clear();
                            for(int i = 0; i <size; i++){
                                timesScheduler.get(i).clear();
                                timesScheduler.get(i).setVisible(false);
                            }
                        });

                        rootScheduler.getChildren().addAll(verticalLeft,verticalRight,applyScheduler);
                        Scene scene3 = new Scene(rootScheduler, 600, 700);
                        scheduleStage.initOwner(trainStage);
                        scheduleStage.initModality(Modality.APPLICATION_MODAL);
                        scheduleStage.setScene(scene3);

                        scheduleStage.show();

                        scheduleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent windowEvent) {
                                setTrainNew.setDisable(false);
                                setTrainNew.setStyle(setTrainScheduleNew.getStyle());
                            }
                        });

                    }

                     setTrainNew.setStyle(setTrainScheduleNew.getStyle());

                });




                setTrainNew.setOnAction(e->{

                    Network.server_Object.create_Train(t_new);

                    Network.server_Object.get_Train_List().get(HOLDIT).set_Current_Block(62);
                    Network.server_Object.get_Train_List().get(HOLDIT).set_Current_Line("Green");
                    Network.server_Object.get_Train_List().get(HOLDIT).add_Infrastructure("SWITCH FROM YARD");
                    Network.server_Object.get_Train_List().get(HOLDIT).add_Infrastructure("STATION; GLENBURY");
                    Network.server_Object.get_Train_List().get(HOLDIT).add_Infrastructure("STATION; DORMONT");


                    LocalTime starttimeTemp = LocalTime.parse("00:00");
                    Network.server_Object.get_Train_List().get(HOLDIT).add_Time(starttimeTemp);

                  /*
                    try {
                        Network.server_Object.dispatch(Network.server_Object.get_Train_List().size()-1);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }


                    trainStage.close();
                });
            }
        });
        */




        Button modeToggle = new Button("Switch Mode");
        modeToggle.setStyle("-fx-background-color: pink;");
        modeToggle.setMinHeight(80);
        modeToggle.setMinWidth(300);

        modeToggle.setOnAction(e->{
            if(Network.server_Object != null) {
                try {
                    Network.server_Object.toggle_Automatic();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                if (Network.server_Object.get_Automatic()) {

                    modeToggle.setStyle("-fx-background-color: yellow;");
                    currentModeLabel.setStyle("-fx-background-color: yellow");
                    currentModeLabel.setText("Current Mode: Automatic");

                } else {

                    modeToggle.setStyle("-fx-background-color: pink;");
                    currentModeLabel.setStyle("-fx-background-color: pink");
                    currentModeLabel.setText("Current Mode: Manual");
                }
            }else{
                show_No_ServerObject();
            }
        });

        Label simulationTimeLabel = new Label("SIM TIME (s): ");
        simulationTime.setEditable(false);




        VBox bottomRightButton = new VBox();
        bottomRightButton.getChildren().add(modeToggle);
        BorderPane rootBorderPane = new BorderPane();

        HBox bottomRoot = new HBox();
        bottomRoot.getChildren().addAll(getBottomTray(rootTrainSetBox,rootTrackSetBox),bottomRightButton,
                simulationTimeLabel,simulationTime);
        bottomRoot.setSpacing(10);

        rootBorderPane.setTop( getTopTray(rootMenuBarLeft , rootMenuBarRight) );

        rootBorderPane.setBottom(bottomRoot);
        rootBorderPane.setRight(getRightTray(rootTrainInfoBox,rootTrackInfoBox));

        Scene scene1 = new Scene(rootBorderPane, 1410, 900);

        CTCStage.setScene(scene1);
        CTCStage.setMinWidth(1410);
        CTCStage.setMinHeight(850);

        CTCStage.show();

        Alert WELCOME = new Alert(Alert.AlertType.INFORMATION);
        WELCOME.setWidth(600);
        WELCOME.setHeaderText("WELCOME TO THE CENTRAL TRACK CONTROLLER (CTC) MODULE");
        WELCOME.setContentText("Important note: Most functions require you to start the CTC server. (Network>StartServer)\n\n" +
                "- The Schedule Menu will allow you to import, view, edit, and add a train to the schedule.\n\n" +
                "- The Network Menu will allow you to start your server or connect to other modules' servers.\n\n" +
                "- The Track Menu will allow you to import the track model.\n\n" +
                "- The Information box are located to the right. There you can view information about the track, trains, time, and network issues.\n\n" +
                "- The Manual Controls are located at the bottom. There you can manually dispatch a train and manually open or close blocks of track\n\n" +
                "- The mode can be changed by pressing the big colorful button at the bottom right."
        );
        WELCOME.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }

}
