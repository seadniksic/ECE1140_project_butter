package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Track_Model_Murphy_GUI {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Stage murphy_Stage;
    Label description_label;
    Track_Model_Builder_Data this_TMBD;
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
    private Scene return_Line_Scene(int param_Line_Index, GridPane param_GridPane){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        if(this_TMBD.this_File != null){
            description_label = new Label("Viewing Line: " + (this_TMBD.this_Track.get_Line_At_Index(param_Line_Index).index + 1));
        }

        // Update Gridpane
        param_GridPane = new GridPane();
        Block[][] line_Block_Arr = this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr;

        for(int i = 0; i < line_Block_Arr.length; i++){
            for(int j = 0; j < line_Block_Arr[i].length; j++){
                param_GridPane.add(line_Block_Arr[i][j].this_Block_GUI.this_Button, i, j);
            }
        }

        GridPane canvas_GP = param_GridPane;
        canvas_GP.setAlignment(Pos.CENTER);
        canvas_GP.setHgap(3);
        canvas_GP.setVgap(3);

        Button return_To_Track_Button = new Button("Go Back");
        map_Return_To_Track_Button(return_To_Track_Button);

        Button simulate_Button = new Button("Simulate");
        map_Simulate_Button(simulate_Button, canvas_GP, param_Line_Index);

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label, return_To_Track_Button, canvas_GP, simulate_Button);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 500);
    }
    private void map_Line_Button(Button param_Button, int param_Line_Index){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                swap_To_Line_Scene(param_Line_Index, new GridPane());
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
    private void map_Simulate_Button(Button param_Button, GridPane param_GridPane, int param_Line_Index){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // The simulation will run here when clicked
                System.out.println("Spawning Train...");
                spawn_Train_In_Yard(param_Line_Index,1);
                swap_To_Line_Scene(param_Line_Index, param_GridPane);

                set_Switch_At_Block(param_Line_Index, 2, false);
                update_Occupancy(param_Line_Index, 0, 50.0);
                update_Occupancy(param_Line_Index, 0, 50.0);
                //update_Occupancy(param_Line_Index, 0, 50.0);

                swap_To_Line_Scene(param_Line_Index, param_GridPane);
            }
        };
        param_Button.setOnAction(event);
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

                    for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(current_Line_index).block_Arr) {
                        for (Block block : blocks) {
                            if(block.x_Coord == Integer.parseInt(x_Coord_S) && block.y_Coord == Integer.parseInt(y_Coord_S)){
                                block.section = extracted_Section;
                                block.blockNumber = extracted_Block_Num;
                                block.length = extracted_Block_Length;
                                block.grade = extracted_Block_Grade;
                                block.next_Block_Number = extracted_Next_Block_Number;
                                block.next_Block_Number_2 = extracted_Next_Block_Number_2;
                                block.previous_Block_Number = extracted_Previous_Block_Number;
                                block.isYard = extracted_is_Yard;
                                block.isSwitch = extracted_is_Switch;
                                if(block.blockNumber != -1){//TODO: May need to revise this
                                    block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Green"));
                                    if(block.isYard){
                                        block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Mustard"));
                                    }
                                    if(block.isSwitch){
                                        block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Blue"));
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
    public void swap_To_Line_Scene(int param_Line_Index, GridPane param_GridPane){
        murphy_Stage.setScene(return_Line_Scene(param_Line_Index, param_GridPane));
        murphy_Stage.show();
    }
    public void spawn_Train_In_Yard(int param_Line_Index, int param_Block_Number){
        Line working_Line = this_TMBD.this_Track.line_ArrayList.get(param_Line_Index);
        // Find the block in the coordinate plane and turn its occupancy on
        for (Block[] blocks : working_Line.block_Arr) {
            for (Block block : blocks) {
                if(block.blockNumber == param_Block_Number && block.isYard){
                    System.out.println("Spawning train at block: " + block.blockNumber);
                    block.set_Occupancy(true);
                    //TODO: The arrays below need to be in tandem
                    working_Line.occupancies.add(block.blockNumber);
                    working_Line.distances.add(0.0);
                }
            }
        }
    }
    public void update_Occupancy(int param_Line_Index, int param_Occupancy_Index, Double param_Distance_Traveled_In_Tick){
        Boolean full_Break = false;

        System.out.println("\nUpdating occupancy...");
        System.out.println("Current occupancies: ");
        for (Integer occupancy : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies) {
            System.out.println(occupancy);
        }
        System.out.println("Current distances: ");
        for (Double distance : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances) {
            System.out.println(distance);
        }
        System.out.println("Past occupancies: ");
        for (Integer past_occupancy : this_TMBD.this_Track.line_ArrayList.get(param_Occupancy_Index).past_Occupancies) {
            System.out.println(past_occupancy);
        }
        // Search for block occupancy
        for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr) {
            for (Block block : blocks) {
                if(block.blockNumber == this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies.get(param_Occupancy_Index) && block.isOccupied){
                    System.out.println("This block is occupied: " + block.blockNumber);
                    // Add distance
                    double temp = this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.get(param_Occupancy_Index);
                    temp += param_Distance_Traveled_In_Tick;
                    //System.out.println("Added distance: " + temp);
                    //System.out.println("Block.length: " + block.length);
                    // Compare distance to block length
                    if(temp >= block.length){
                        // Subtracting block length from distance and updating distances/occupancies
                        temp -= block.length;
                        this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.set(param_Occupancy_Index, temp);
                        this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies.set(param_Occupancy_Index, block.next_Block_Number);


                        // Swap the next and previous blocks if the next block happens to be the previous block
                        if(!this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.isEmpty()){
                            if(block.next_Block_Number == this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.get(param_Occupancy_Index)){
                                int dummy = 0;
                                dummy = block.next_Block_Number;
                                block.next_Block_Number = block.previous_Block_Number;
                                block.previous_Block_Number = dummy;
                            }
                        }


                        if(block.isSwitch){
                            System.out.println("This block: " + block.blockNumber + " is a switch");
                            System.out.println("Block number 1: " + block.next_Block_Number);
                            System.out.println("Block number 2: " + block.next_Block_Number_2);
                            System.out.println("Block is switched: " + block.is_Switched);
                            for (Block[] blocks1 : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr) {
                                for (Block block1 : blocks1) {
                                    if(block.next_Block_Number == block1.blockNumber && !block.is_Switched){
                                        System.out.println("New occupancy at block: " + block1.blockNumber);
                                        System.out.println("Block number 2: " + block.next_Block_Number_2);
                                        System.out.println("Block is switched: " + block.is_Switched);
                                        block.set_Occupancy(false);
                                        if(this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.isEmpty()){
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.add(param_Occupancy_Index, block.blockNumber);
                                        }else{
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.set(param_Occupancy_Index, block.blockNumber);
                                        }

                                        System.out.println("New past occupancies: ");
                                        for (Integer past_occupancy : this_TMBD.this_Track.line_ArrayList.get(param_Occupancy_Index).past_Occupancies) {
                                            System.out.println(past_occupancy);
                                        }

                                        block1.set_Occupancy(true);
                                        // Destroy the train if the new occupancy is a yard
                                        if(block1.isYard){
                                            System.out.println("Absorbing train at block 1: " + block1.blockNumber);
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies.remove(param_Occupancy_Index);
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.remove(param_Occupancy_Index);
                                            block1.set_Occupancy(false);
                                            return;
                                        }
                                    }
                                    else if(block.next_Block_Number_2 == block1.blockNumber && block.is_Switched){
                                        System.out.println("New occupancy at block for switched: " + block1.blockNumber);
                                        block.set_Occupancy(false);
                                        if(this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.isEmpty()){
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.add(param_Occupancy_Index, block.blockNumber);
                                        }else{
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.set(param_Occupancy_Index, block.blockNumber);
                                        }

                                        System.out.println("New past occupancies: ");
                                        for (Integer past_occupancy : this_TMBD.this_Track.line_ArrayList.get(param_Occupancy_Index).past_Occupancies) {
                                            System.out.println(past_occupancy);
                                        }

                                        block1.set_Occupancy(true);
                                        // Destroy the train if the new occupancy is a yard
                                        if(block1.isYard){
                                            System.out.println("Absorbing train at block 1: " + block1.blockNumber);
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies.remove(param_Occupancy_Index);
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.remove(param_Occupancy_Index);
                                            block1.set_Occupancy(false);
                                            return;
                                        }
                                    }
                                }
                            }
                            return;

                        }else{
                            for (Block[] blocks1 : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr) {
                                for (Block block1 : blocks1) {
                                    if(block.next_Block_Number == block1.blockNumber){
                                        System.out.println("New occupancy at block: " + block1.blockNumber);
                                        block.set_Occupancy(false);
                                        if(this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.isEmpty()){
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.add(param_Occupancy_Index, block.blockNumber);
                                        }else{
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).past_Occupancies.set(param_Occupancy_Index, block.blockNumber);
                                        }

                                        System.out.println("New past occupancies: ");
                                        for (Integer past_occupancy : this_TMBD.this_Track.line_ArrayList.get(param_Occupancy_Index).past_Occupancies) {
                                            System.out.println(past_occupancy);
                                        }

                                        block1.set_Occupancy(true);
                                        // Destroy the train if the new occupancy is a yard
                                        if(block1.isYard){
                                            System.out.println("Absorbing train at block 1: " + block1.blockNumber);
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies.remove(param_Occupancy_Index);
                                            this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.remove(param_Occupancy_Index);
                                            block1.set_Occupancy(false);
                                            return;
                                        }
                                    }

                                }
                            }
                            return;
                        }

                    }
                    // If smaller, just add and replace
                    else{
                        this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.set(param_Occupancy_Index, temp);
                    }
                }
            }
        }

//        // Check for occupancies in yards (that absorbed) and delete them
//        for (Integer occupancy : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies) {
//            for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr) {
//                for (Block block : blocks) {
//                    if(block.blockNumber)
//                }
//            }
//        }
    }
    public void set_Switch_At_Block(int param_Line_Index, int param_Block_Number, Boolean param_Is_Switched){
        for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr) {
            for (Block block : blocks) {
                if(block.blockNumber == param_Block_Number){
                    block.is_Switched = param_Is_Switched;
                }
            }
        }
    }


}

// Safekeeping
//this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).occupancies.remove(param_Occupancy_Index);
//this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).distances.remove(param_Occupancy_Index);