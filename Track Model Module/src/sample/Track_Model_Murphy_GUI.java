package sample;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import networking.Track_Model_Interface;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Track_Model_Murphy_GUI implements Track_Model_Interface {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Stage murphy_Stage;
    Label description_label;
    static boolean simulate_Was_Clicked = false;
    private Service<Void> backgroundThread;
    static Track_Model_Builder_Data this_TMBD;
    GridPane global_GridPane;
    Button seads_Simulate_Button;
    int working_Line_Index;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Track_Model_Murphy_GUI(){
        murphy_Stage = new Stage();
        murphy_Stage.setTitle("Murphy");
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------

    private Scene return_Start_Scene(){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        description_label = new Label("Welcome, Murphy!");

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 200);
    }
    private Scene return_New_Track_Scene(ArrayList<Button> param_line_Button_ArrayList){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        if(this_TMBD.this_File != null){
            description_label = new Label("Editing Track File: " + this_TMBD.this_Track.name);
        }

        ArrayList<Button> line_Button_ArrayList = param_line_Button_ArrayList;

        // Map any additional lines
        if(line_Button_ArrayList.size() > 1){
            for(int i = 0; i < line_Button_ArrayList.size() - 1; i++){
                map_Line_Button(line_Button_ArrayList.get(i), i);
            }
        }

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label);
        // Add buttons to VBox one by one
        for (Button button : line_Button_ArrayList) {
            this_VBox.getChildren().add(button);
        }
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 500);
    }
    private Scene return_Line_Scene(){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        if(this_TMBD.this_File != null){
            description_label = new Label("Viewing Line: " + (this_TMBD.this_Track.get_Line_At_Index(working_Line_Index).index + 1));
        }

        // Update ObservableList
        ObservableList<Button> temp = FXCollections.observableArrayList();

        // Update Gridpane
        global_GridPane = new GridPane();
        Block[][] line_Block_Arr = this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).block_Arr;

        for(int i = 0; i < line_Block_Arr.length; i++){
            for(int j = 0; j < line_Block_Arr[i].length; j++){
                global_GridPane.add(line_Block_Arr[i][j].this_Block_GUI.this_Button, i, j);
               // temp.add(line_Block_Arr[i][j].this_Block_GUI.this_Button);
            }
        }

        GridPane canvas_GP = global_GridPane;
        canvas_GP.setAlignment(Pos.CENTER);
        canvas_GP.setHgap(3);
        canvas_GP.setVgap(3);

        Button return_To_Track_Button = new Button("Go Back");
        map_Return_To_Track_Button(return_To_Track_Button);


        seads_Simulate_Button = new Button("Simulate");
        map_Simulate_Button();

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label, return_To_Track_Button, canvas_GP, seads_Simulate_Button);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 800, 800);
    }

    private void map_Line_Button(Button param_Button, int param_Line_Index){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                swap_To_Line_Scene(param_Line_Index);
                simulate_Was_Clicked = false;
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_Return_To_Track_Button(Button param_Button){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                write_Text_File_To_Track_Data(this_TMBD.this_File);
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_Simulate_Button(){

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                    if(simulate_Was_Clicked){
                        outer_Update_Occupancy(0, 20.0);
                    }else{
                        try {
                            spawn_Train_In_Yard(2, working_Line_Index, 0);
                            simulate_Was_Clicked = true;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Calling the sleep from the button");
                    backgroundThread = new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                protected Void call() throws Exception {
                                    // Actions
                                    Platform.runLater( ()->{
//                                        // Sleeping
//                                        try {
//                                            System.out.println("Running Platform.runLater for button sleep");
//                                            Thread.sleep(500);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
                                    });
                                    return null;
                                }
                            };
                        }
                    };

                    backgroundThread.restart();
            }
        };

        seads_Simulate_Button.setOnAction(event);
    }


    public void outer_Update_Occupancy(int param_Occupancy_Index, double param_Distance_Traveled_In_Tick){
        System.out.println();
        System.out.println("Running outer_Update_Occupancy");
        System.out.println("param_Line_Index: " + working_Line_Index + ", " + "param_Occ: " + param_Occupancy_Index + ", " +  "distance: " + param_Distance_Traveled_In_Tick + " || " + "simulate: " + simulate_Was_Clicked);

                // Sead - update_Occ
                backgroundThread = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            protected Void call() throws Exception {
                                // Actions
                                    Platform.runLater( ()->{
                                        System.out.println();
                                        System.out.println("Running the Platform.runLater for outer_Update_Occupancy - update_Occupancy");
                                        update_Occupancy(param_Occupancy_Index, param_Distance_Traveled_In_Tick);
                                    });
                                return null;
                            }
                        };
                    }
                };
                // Sead - GetUpdatedOccupancyService
                //final GetUpdatedOccupancyService service = new GetUpdatedOccupancyService();

                // Sead - backgroundThread.restart
                backgroundThread.restart();
    }

    private void configureMenuBar(MenuBar param_MenuBar){
        Menu file_Menu = new Menu("File");
        MenuItem open_Track_MI = new MenuItem(("Open Track"));

        open_Track_MI.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent actionEvent) {
                FileChooser this_fileChooser = new FileChooser();
                this_fileChooser.setTitle("Open Track");
                this_fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Documents", "*.txt"));
                File chosen_File = this_fileChooser.showOpenDialog(null);
                write_Text_File_To_Track_Data(chosen_File);
            }
        });

        file_Menu.getItems().addAll(open_Track_MI);
        param_MenuBar.getMenus().add(file_Menu);
    }

    private void write_Text_File_To_Track_Data(File param_File){
        // Start with a fresh data model
        this_TMBD = new Track_Model_Builder_Data(param_File, param_File.getName()); //TODO: See if this is actually right

        // Read information on file
        try{
            Scanner this_Scanner = new Scanner(param_File);
            int current_Line_index = -1;

            // First scan to write data
            while(this_Scanner.hasNextLine()){
                String next_Line = this_Scanner.nextLine();

                // Creating lines
                if(next_Line.substring(0, 3).equals("L: ")){
                    // Update lines
                    current_Line_index = Integer.parseInt(String.valueOf(next_Line.charAt(3)));
                    this_TMBD.this_Track.add_Line(new Line(current_Line_index));
                }

                // Setting canvases to lines
                if(next_Line.substring(0, 3).equals("C: ")){
                    // Update lines
                    // Iterate through line to get dimensions
                    String x_String = "", y_String = "";
                    Boolean collect_X = false, collect_Y = false;
                    for(int i = 0; i < next_Line.length(); i++){
                        x_String += (collect_X ? next_Line.substring(i, i+1) : "");
                        y_String += (collect_Y ? next_Line.substring(i, i+1) : "");
                        collect_X = (next_Line.substring(i, i+1).equals(" ")? true : collect_X);
                        collect_X = (next_Line.substring(i, i+1).equals("x")? false : collect_X);
                        x_String = (next_Line.substring(i, i+1).equals("x")? x_String.substring(0, x_String.length() - 1) : x_String);
                        collect_Y = (next_Line.substring(i, i+1).equals("x")? true : collect_Y);
                        collect_Y = (next_Line.substring(i, i+1).equals("\n")? false : collect_Y);

                    }
                    this_TMBD.this_Track.line_ArrayList.get(current_Line_index).set_Canvas(Integer.parseInt(x_String),Integer.parseInt(y_String));
                }

                // Updating canvases with blocks
                if(next_Line.substring(0, 3).equals("B: ")){
                    // Iterate through line to get properties of block
                    ArrayList<String> block_Line_Elements = new ArrayList<String>();
                    String build = "";
                    for(int i = 2; i < next_Line.length(); i++){
                        if(!next_Line.substring(i, i+1).equals(",")){
                            if(!next_Line.substring(i, i+1).equals(" ")){
                                build += next_Line.substring(i, i+1);
                            }
                        }else{
                            block_Line_Elements.add(build);
                            build = "";
                        }
                        if(i == next_Line.length() - 1){
                            block_Line_Elements.add(build);
                        }
                    }

                    // Extract the coordinates from the block
                    String x_Coord_S = "", y_Coord_S = "";
                    Boolean collect_X = true, collect_Y = false;
                    String coord_String = block_Line_Elements.get(0);
                    for(int i = 0; i < coord_String.length(); i++){
                        x_Coord_S += (collect_X ? coord_String.substring(i, i+1) : "");
                        y_Coord_S += (collect_Y ? coord_String.substring(i, i+1) : "");
                        collect_X = (coord_String.substring(i, i+1).equals("x")? false : collect_X);
                        x_Coord_S = (coord_String.substring(i, i+1).equals("x")? x_Coord_S.substring(0, x_Coord_S.length() - 1) : x_Coord_S);
                        collect_Y = (coord_String.substring(i, i+1).equals("x")? true : collect_Y);

                    }

                    // Assign elements from text file to data
                    String extracted_Section = block_Line_Elements.get(1);
                    int extracted_Block_Num = Integer.parseInt(block_Line_Elements.get(2));
                    double extracted_Block_Length = Double.parseDouble(block_Line_Elements.get(3));
                    double extracted_Block_Grade = Double.parseDouble(block_Line_Elements.get(4));
                    int extracted_Next_Block_Number = Integer.parseInt(block_Line_Elements.get(5));
                    int extracted_Next_Block_Number_2 = Integer.parseInt(block_Line_Elements.get(6));
                    int extracted_Previous_Block_Number = Integer.parseInt(block_Line_Elements.get(7));
                    Boolean extracted_is_Yard = Boolean.parseBoolean(block_Line_Elements.get(8));
                    Boolean extracted_is_Switch = Boolean.parseBoolean(block_Line_Elements.get(9));

                    Boolean extracted_is_Alpha = Boolean.parseBoolean(block_Line_Elements.get(10));
                    Boolean extracted_is_Beta = Boolean.parseBoolean(block_Line_Elements.get(11));
                    Boolean extracted_is_Gamma = Boolean.parseBoolean(block_Line_Elements.get(12));

                    Boolean extracted_is_Station = Boolean.parseBoolean(block_Line_Elements.get(13));
                    String extracted_Station_Name = block_Line_Elements.get(14);


                    for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(current_Line_index).block_Arr) {
                        for (Block block : blocks) {
                            if(block.x_Coord == Integer.parseInt(x_Coord_S) && block.y_Coord == Integer.parseInt(y_Coord_S)){
                                block.is_Builder = false;
                                block.section = extracted_Section;
                                block.blockNumber = extracted_Block_Num;
                                block.length = extracted_Block_Length;
                                block.grade = extracted_Block_Grade;
                                block.next_Block_Number = extracted_Next_Block_Number;
                                block.next_Block_Number_2 = extracted_Next_Block_Number_2;
                                block.previous_Block_Number = extracted_Previous_Block_Number;
                                block.is_Yard = extracted_is_Yard;
                                block.is_Switch = extracted_is_Switch;

                                block.is_Alpha = extracted_is_Alpha;
                                block.is_Beta = extracted_is_Beta;
                                block.is_Gamma = extracted_is_Gamma;

                                block.is_Station = extracted_is_Station;
                                block.station_Name = extracted_Station_Name;
                                if(block.blockNumber != -1){//TODO: May need to revise this
                                    block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Green"));
                                    if(block.is_Yard){
                                        block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Mustard"));
                                    }
                                    if(block.is_Switch){
                                        block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Blue"));
                                    }
                                    if(block.is_Station){
                                        block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Purple"));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Create buttons based off how many lines are now in the track
            ArrayList<Button> line_Button_ArrayList = new ArrayList<Button>();

            // Add the line buttons while mapping them every time
            for (Line line : this_TMBD.this_Track.line_ArrayList) {
                Button line_Button = new Button(line.name);
                line_Button_ArrayList.add(line_Button_ArrayList.size(), line_Button);
                map_Line_Button(line_Button, line.index);
                swap_To_New_Track_Scene(line_Button_ArrayList);
            }

        }catch (FileNotFoundException e){
            System.out.println("ERROR! File Not Found!");
        }


        // Create and update objects from information on file
        // Update GUI based on new objects in
    }

    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------

    public void swap_to_Start_Scene(){
        murphy_Stage.setScene(return_Start_Scene());
        murphy_Stage.show();
    }
    public void swap_To_New_Track_Scene(ArrayList<Button> param_Line_Button_ArrayList){
        if(this_TMBD.this_File != null){
            murphy_Stage.setScene(return_New_Track_Scene(param_Line_Button_ArrayList));
            murphy_Stage.show();
        }
    }
    public void swap_To_Line_Scene(int param_Line_Index){
        working_Line_Index = param_Line_Index;
        murphy_Stage.setScene(return_Line_Scene());
        murphy_Stage.show();
    }

    public void spawn_Train_In_Yard(int param_Cars, int param_Line_Index, int param_Block_Number) throws RemoteException {
        System.out.println();
        System.out.println("Running spawn_Train_In_Yard: " + "param_Cars: " + param_Cars + ", " + "param_Line_Index: " + param_Line_Index + ", " + "param_Block_Number: " + param_Block_Number);
        Line working_Line = this_TMBD.this_Track.line_ArrayList.get(param_Line_Index);
        // Find the block in the coordinate plane and turn its occupancy on
        for (Block[] blocks : working_Line.block_Arr) {
            for (Block block : blocks) {
                if(block.blockNumber == param_Block_Number && block.is_Yard){
                    System.out.println("Spawning train at block: " + block.blockNumber);


                    // Sead
                    if(Network.tm_Interface != null){
                        Network.tm_Interface.create_Train(2);
                    }

                    System.out.println("Block block blocks");

                        backgroundThread = new Service<Void>() {
                            @Override
                            protected Task<Void> createTask() {
                                return new Task<Void>() {
                                    protected Void call() throws Exception {
                                        // Actions
                                        Platform.runLater( ()->{
                                            System.out.println();
                                            System.out.println("Running the Platform.runLater for spawn_Train_In_Yard - set_Occupancy");
                                            block.set_Occupancy(true);
                                            //TODO: The arrays below need to be in tandem
                                            working_Line.occupancies.add(block.blockNumber);
                                            System.out.println("Running the Platform.runLater for spawn_Train_In_Yard - train_Moved");

                                            //call_Network_Train_Moved(block);

                                            working_Line.distances.add(0.0);
                                        });
                                        return null;
                                    }
                                };
                            }
                        };

                        backgroundThread.restart();
                }
            }
        }
        // Sead's alternative - swap
        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    protected Void call() throws Exception {
                        // Actions
                        Platform.runLater( ()->{
                            System.out.println();
                            System.out.println("Running the Platform.runLater for spawn_Train_In_Yard - swap_To_Line_Scene");
                            swap_To_Line_Scene(param_Line_Index);
                        });
                        return null;
                    }
                };
            }
        };
    }
    public void update_Occupancy(int param_Occupancy_Index, Double param_Distance_Traveled_In_Tick){
        // Print statements are concatenated into ...
        System.out.println("\nUpdating occupancy...");
        // For every row in the line
        for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).block_Arr) {
            // For every block in the current row
            for (Block block : blocks) {
                // Check if the block has an occupancy and is recorded in the occupancy tracker
                if(!this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.isEmpty()){
                    if(block.blockNumber == this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.get(param_Occupancy_Index) && block.isOccupied){
                        System.out.println(block.blockNumber + " is occupied");
                        // If so, add the distance to the matching distance array
                        double temp = this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances.get(param_Occupancy_Index);
                        temp += param_Distance_Traveled_In_Tick;
                        System.out.println("Added distance: " + temp);
                        System.out.println("Block.length: " + block.length);
                        // Compare distance to block length
                        if(temp >= block.length){
                            // Subtracting block length from distance and updating distances/occupancies
                            temp -= block.length;
                            this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances.set(param_Occupancy_Index, temp);

                            // Swap the next and previous blocks if the next block happens to be the previous block
                            if(!this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.isEmpty()){
                                if(block.next_Block_Number == this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.get(param_Occupancy_Index)){
                                    System.out.println("Swapping next: " + block.next_Block_Number + " and previous: " + block.previous_Block_Number);
                                    int dummy = 0;
                                    dummy = block.next_Block_Number;
                                    block.next_Block_Number = block.previous_Block_Number;
                                    block.previous_Block_Number = dummy;
                                }
                            }

                            // Perform different actions if the block is a switch
                            if(block.is_Switch){
                                System.out.println("This block: " + block.blockNumber + " is a switch");
                                System.out.println("Block number 1: " + block.next_Block_Number);
                                System.out.println("Block number 2: " + block.next_Block_Number_2);
                                System.out.println("Block is switched: " + block.is_Switched);
                                // For every row in the line
                                for (Block[] blocks1 : this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).block_Arr) {
                                    // For every block in the row
                                    for (Block block1 : blocks1) {
                                        // If block is stated as the next block of the block from before and the switch is set to false
                                        if(block.next_Block_Number == block1.blockNumber && !block.is_Switched){
                                            System.out.println("New occupancy at block for not switched: " + block1.blockNumber);
                                            System.out.println("Block number 2: " + block.next_Block_Number_2);
                                            System.out.println("Block is switched: " + block.is_Switched);

                                            //TODO: Must be set to false once all cars have left
                                            block.set_Occupancy(false);

                                            // Add the block from before as a past occupancy
                                            if(this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.isEmpty()){
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.add(param_Occupancy_Index, block.blockNumber);
                                            }else{
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.set(param_Occupancy_Index, block.blockNumber);
                                            }
                                            // Update the occupancy array with the new block
                                            block1.set_Occupancy(true);

                                            call_Network_Train_Moved(block1);

                                            this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.set(param_Occupancy_Index, block1.blockNumber);
                                            // Destroy the train if the new occupancy is a yard
                                            if(block1.is_Yard){
                                                System.out.println("Absorbing train at block 1: " + block1.blockNumber);
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.remove(param_Occupancy_Index);
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances.remove(param_Occupancy_Index);

                                                //TODO: Must be set to false once all cars have left
                                                block1.set_Occupancy(false);

                                                return;
                                            }
                                        }
                                        // If block is stated as the next block of the block from before and switch is set to true
                                        else if(block.next_Block_Number_2 == block1.blockNumber && block.is_Switched){
                                            System.out.println("New occupancy at block for switched: " + block1.blockNumber);

                                            //TODO: Must be set to false once all cars have left
                                            block.set_Occupancy(false);

                                            // Add the block from before as a past occupancy
                                            if(this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.isEmpty()){
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.add(param_Occupancy_Index, block.blockNumber);
                                            }else{
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.set(param_Occupancy_Index, block.blockNumber);
                                            }

                                            // Update the occupancy array with the new block
                                            block1.set_Occupancy(true);

                                           call_Network_Train_Moved(block1);

                                            this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.set(param_Occupancy_Index, block1.blockNumber);
                                            // Destroy the train if the new occupancy is a yard
                                            if(block1.is_Yard){
                                                System.out.println("Absorbing train at block 1: " + block1.blockNumber);
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.remove(param_Occupancy_Index);
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances.remove(param_Occupancy_Index);

                                                //TODO: Must be set to false once all cars have left
                                                block1.set_Occupancy(false);

                                                print_Update_Occupancy("Moved and block is switch and next block is yard");
                                                return;
                                            }
                                        }
                                    }
                                }
                                print_Update_Occupancy("Moved and block is switch");
                                return;
                                // Perform different actions if the block is not a switch
                            }else{
                                // For every row in the line
                                for (Block[] blocks1 : this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).block_Arr) {
                                    // For every block in the row
                                    for (Block block1 : blocks1) {
                                        // If the block is stated as the next block of the block from before
                                        if(block.next_Block_Number == block1.blockNumber){
                                            System.out.println("New occupancy at block: " + block1.blockNumber);


                                            //TODO: Must be set to false once all cars have left
                                            block.set_Occupancy(false);

                                            // Add the block from before to the list of past occupancies
                                            System.out.println("Is empty: " + this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.isEmpty());
                                            if(this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.isEmpty()){
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.add(block.blockNumber);
                                            }else{
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.set(param_Occupancy_Index, block.blockNumber);
                                            }
                                            // Update the occupancy array with the new block
                                            block1.set_Occupancy(true);

                                            call_Network_Train_Moved(block1);

                                            this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.set(param_Occupancy_Index, block1.blockNumber);
                                            // Destroy the train if the new occupancy is a yard
                                            if(block1.is_Yard){
                                                System.out.println("Absorbing train at block 1: " + block1.blockNumber);
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies.remove(param_Occupancy_Index);
                                                this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances.remove(param_Occupancy_Index);

                                                //TODO: Must be set to false once all cars have left
                                                block1.set_Occupancy(false);

                                                print_Update_Occupancy("Moved and next block is yard");
                                                return;
                                            }
                                        }
                                    }
                                }
                                print_Update_Occupancy("Moved and block is not switch");
                                return;
                            }
                        }
                        // If smaller, just add to the corresponding distance in the distance array
                        else{
                            this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances.set(param_Occupancy_Index, temp);
                        }
                    }
                }
            }
        }
        print_Update_Occupancy("Did not move");

        // Second call
        swap_To_Line_Scene(working_Line_Index);
    }
    public void set_Switch_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched){
        System.out.println("Running set_Switch_At_Block: " + "param_Line_Index: " + param_Line_Index + ", " + "param_Block_Number: " + param_Block_Number + ", " + "param_Is_Switched: " + param_Is_Switched);
        for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr) {
            for (Block block : blocks) {
                if(block.blockNumber == param_Block_Number){
                    block.is_Switched = param_Is_Switched;
                }
            }
        }
    }

    //TODO: Tony
    @Override
    public void set_Light_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched) {

    }

    @Override
    public void set_Crossbar_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched) {

    }

    void print_Update_Occupancy(String param_Where_Called){
        System.out.println("Called at: " + param_Where_Called);
        System.out.println("Current occupancies: ");
        for (Integer occupancy : this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).occupancies) {
            System.out.println(occupancy);
        }
        System.out.println("Current distances: ");
        for (Double distance : this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).distances) {
            System.out.println(distance);
        }
        System.out.println("Past occupancies: ");
        for (int i = 0; i < this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.size(); i++){
            System.out.println(this_TMBD.this_Track.line_ArrayList.get(working_Line_Index).past_Occupancies.get(i));
        }
    }

    public void send_Speed_Authority(int train_Num, double speed, int authority) throws RemoteException, InterruptedException {
        if(Network.tm_Interface != null){
            Network.tm_Interface.send_Speed_Authority(train_Num, speed, authority, 0.0);
            System.out.println("Sending speed and authority to train model");
        }
    }

    private void call_Network_Train_Moved(Block param_Block){
        //TODO: Hardcoded train num

        if(Network.tcs_Interface != null){
            try {
                Network.tcs_Interface.train_Moved(0, param_Block.blockNumber);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}