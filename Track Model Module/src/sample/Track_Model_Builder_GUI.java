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

public class Track_Model_Builder_GUI {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Stage builder_Stage;
    Label description_label;
    Track_Model_Builder_Data this_TMBD;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Track_Model_Builder_GUI(){
        builder_Stage = new Stage();
        builder_Stage.setTitle("Track Builder");
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------

    private Scene return_Start_Scene(){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        description_label = new Label("Welcome to the track builder!");

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
        // If this is the first time this scene is being called, add the add button (prevents duplicate add buttons)
        if(line_Button_ArrayList.size() == 0){
            Button add_Line_Button = new Button("Add Line");
            line_Button_ArrayList.add(add_Line_Button);
        }
        // Map the last button of the array (add) to summon new line buttons when clicked
        map_Add_Line_Button(line_Button_ArrayList.get(line_Button_ArrayList.size() - 1), line_Button_ArrayList);

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
            description_label = new Label("Editing Line: " + (this_TMBD.this_Track.get_Line_At_Index(param_Line_Index).index + 1));
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

        Button return_To_Track_Button = new Button("Save and Go Back");
        map_Return_To_Track_Button(return_To_Track_Button);

        Button set_Canvas_Button = new Button("Set Canvas");
        map_Canvas_Button(set_Canvas_Button, this_TMBD.this_Track.line_ArrayList.get(param_Line_Index), canvas_GP);


        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label, return_To_Track_Button, set_Canvas_Button, canvas_GP);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 500);
    }
    private Scene return_set_Canvas_Scene(int param_Line_Index, GridPane param_GridPane){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        description_label = new Label("Set the X and Y dimensions of your line canvas");

        Label x_Label = new Label("X:");
        TextField x_TextField = new TextField();
        Label y_Label = new Label("Y:");
        TextField y_TextField = new TextField();

        HBox this_Hbox = new HBox();
        this_Hbox.getChildren().addAll(x_Label, x_TextField, y_Label, y_TextField);
        this_Hbox.setAlignment(Pos.CENTER);
        this_Hbox.setSpacing(20);

        Button after_Set_Canvas_Button = new Button("Save and Go Back");
        map_After_Set_Canvas_Button(after_Set_Canvas_Button, param_Line_Index, x_TextField, y_TextField, param_GridPane);

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label, this_Hbox, after_Set_Canvas_Button);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 200);
    }
    private void map_Add_Line_Button(Button param_Button, ArrayList param_ArrayList){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Keeping track of the index of the line
                int lineIndex = param_ArrayList.size() - 1;
                this_TMBD.this_Track.add_Line(new Line(lineIndex));

                Button newest_Line_Button = new Button(this_TMBD.this_Track.line_ArrayList.get(this_TMBD.this_Track.line_ArrayList.size() - 1).name);
                param_ArrayList.add(param_ArrayList.size() - 1, newest_Line_Button);

                write_Track_Data_To_Text_File();
                write_Text_File_To_Track_Data(this_TMBD.this_File);
            }
        };
        param_Button.setOnAction(event);
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
    private void map_Canvas_Button(Button param_Button, Line param_Line, GridPane param_GridPane){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                swap_To_Set_Canvas_Scene(param_Line.index, param_GridPane);
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_After_Set_Canvas_Button(Button param_Button, int param_Line_Index, TextField param_X_TextField, TextField param_Y_TextField, GridPane param_GridPane){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //TODO: Error Checking for not numbers
                this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).set_Canvas(Integer.parseInt(param_X_TextField.getText()), Integer.parseInt(param_Y_TextField.getText()));

                write_Track_Data_To_Text_File();
                write_Text_File_To_Track_Data(this_TMBD.this_File);

                swap_To_Line_Scene(param_Line_Index, param_GridPane);
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_Return_To_Track_Button(Button param_Button){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                write_Track_Data_To_Text_File();
                write_Text_File_To_Track_Data(this_TMBD.this_File);
            }
        };
        param_Button.setOnAction(event);
    }

    private void configureMenuBar(MenuBar param_MenuBar){
        Menu file_Menu = new Menu("File");
        MenuItem new_Track_MI = new MenuItem("New Track");
        MenuItem open_Track_MI = new MenuItem(("Open Track"));
        MenuItem save_Track_MI = new MenuItem(("Save Track"));
        new_Track_MI.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) { //TODO: Override missing
                FileChooser this_fileChooser = new FileChooser();
                this_fileChooser.setTitle("New Track");
                this_fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", ".txt"));
                File this_file = this_fileChooser.showSaveDialog(builder_Stage);
                if(this_file != null){
                    saveTextToFile("Hello", this_file);
                    this_TMBD = new Track_Model_Builder_Data(this_file, this_file.getName());
                    // Move to next scene for new file
                    swap_To_New_Track_Scene(new ArrayList<Button>());
                }
            }
        });
        open_Track_MI.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent actionEvent) {
                FileChooser this_fileChooser = new FileChooser();
                this_fileChooser.setTitle("Open Track");
                this_fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Documents", "*.txt"));
                File chosen_File = this_fileChooser.showOpenDialog(null);
                write_Text_File_To_Track_Data(chosen_File);
            }
        });
        save_Track_MI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                write_Track_Data_To_Text_File();
            }
        });
        file_Menu.getItems().addAll(new_Track_MI, open_Track_MI, save_Track_MI);
        param_MenuBar.getMenus().add(file_Menu);
    }
    private void saveTextToFile(String param_String, File param_File){
        try{
            PrintWriter this_printWriter = new PrintWriter(param_File);
            this_printWriter.println(param_String);
            this_printWriter.close();
        }catch(IOException e){
            System.out.println("Darn");
        }
    }
    private void write_Track_Data_To_Text_File(){
        // If the track model data has been set
        if(this_TMBD != null){
            String output = "";

            // Writing track lines to string

            ArrayList<Line> line_Arr_List = this_TMBD.this_Track.line_ArrayList;

            for(int i = 0; i < line_Arr_List.size(); i++){
                output += "L: " + line_Arr_List.get(i).index + "\n";
                output += "C: " + line_Arr_List.get(i).num_Rows + "x" + line_Arr_List.get(i).num_Columns + "\n";

                // Include any modified blocks
                for (Block[] blocks : line_Arr_List.get(i).block_Arr){
                    for (Block block : blocks) {
                        if(block.blockNumber != -1){
                            output += "B: " + block.x_Coord + "x" + block.y_Coord + ", " + block.section + ", " + block.blockNumber + ", " + block.length + ", " + block.grade + ", " + block.next_Block_Number + ", " + block.next_Block_Number_2 + ", " + block.previous_Block_Number + ", " + block.is_Yard + ", " + block.is_Switch + ", " + block.is_Alpha + ", " + block.is_Beta + ", " + block.is_Gamma + ", " + block.is_Station + ", " + block.station_Name + ", " + block.prev_Station_Name + ", " + block.next_Station_Name + "\n";
                        }
                    }
                }
                output = output.substring(0, output.length() - 1); // Remove extra new line
                output += (i != this_TMBD.this_Track.line_ArrayList.size() - 1 ? "\n" : "");
            }

            // Write string to text file
            saveTextToFile(output, this_TMBD.this_File);
        }
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
                    current_Line_index = Integer.parseInt(String.valueOf(next_Line.charAt(3)));
                    this_TMBD.this_Track.add_Line(new Line(current_Line_index));
                }

                // Setting canvases to lines
                if(next_Line.substring(0, 3).equals("C: ")){
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
                    String extracted_Prev_Station_Name = block_Line_Elements.get(15);
                    String extracted_Next_Station_Name = block_Line_Elements.get(16);


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
                                block.is_Yard = extracted_is_Yard;
                                block.is_Switch = extracted_is_Switch;

                                block.is_Alpha = extracted_is_Alpha;
                                block.is_Beta = extracted_is_Beta;
                                block.is_Gamma = extracted_is_Gamma;

                                block.is_Station = extracted_is_Station;
                                block.station_Name = extracted_Station_Name;
                                block.prev_Station_Name = extracted_Prev_Station_Name;
                                block.next_Station_Name = extracted_Next_Station_Name;
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

            // Add the add line button first
            Button add_Line_Button = new Button("Add Line");
            line_Button_ArrayList.add(add_Line_Button);

            // Add the line buttons while mapping both the new line button and add line button every time
            for (Line line : this_TMBD.this_Track.line_ArrayList) {
                Button line_Button = new Button(line.name);
                line_Button_ArrayList.add(line_Button_ArrayList.size() - 1, line_Button);
                map_Line_Button(line_Button, line.index);
                map_Add_Line_Button(line_Button_ArrayList.get(line_Button_ArrayList.size() - 1), line_Button_ArrayList);
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
        builder_Stage.setScene(return_Start_Scene());
        builder_Stage.show();
    }

    public void swap_To_New_Track_Scene(ArrayList<Button> param_Line_Button_ArrayList){
        if(this_TMBD.this_File != null){
            builder_Stage.setScene(return_New_Track_Scene(param_Line_Button_ArrayList));
            builder_Stage.show();
        }
    }

    public void swap_To_Line_Scene(int param_Line_Index, GridPane param_GridPane){
        builder_Stage.setScene(return_Line_Scene(param_Line_Index, param_GridPane));
        builder_Stage.show();
    }

    public void swap_To_Set_Canvas_Scene(int param_Line_Index, GridPane param_GridPane){
        builder_Stage.setScene(return_set_Canvas_Scene(param_Line_Index, param_GridPane));
        builder_Stage.show();
    }

}

// SAFEKEEPING
//        StackPane this_StackPane = new StackPane();
//        this_StackPane.getChildren().addAll(this_MenuBar, this_VBox);
//        this_StackPane.setAlignment(this_MenuBar, Pos.TOP_CENTER);
//
//        if(description_label != null){
//                description_label.setText("Editing: " + param_File.getName());
//            }

//    String x_Coord_S = "", y_Coord_S = "";
//    Boolean collect_X = false, collect_Y = false;
//          x_Coord_S += (collect_X ? next_Line.substring(i, i+1) : "");
//        y_Coord_S += (collect_Y ? next_Line.substring(i, i+1) : "");
//        collect_X = (next_Line.substring(i, i+1).equals(" ")? true : collect_X);
//        collect_X = (next_Line.substring(i, i+1).equals("x")? false : collect_X);
//        x_Coord_S = (next_Line.substring(i, i+1).equals("x")? x_Coord_S.substring(0, x_Coord_S.length() - 1) : x_Coord_S);
//        collect_Y = (next_Line.substring(i, i+1).equals("x")? true : collect_Y);
//        collect_Y = (next_Line.substring(i, i+1).equals(",")? false : collect_Y);
//        if(next_Line.substring(i, i+1).equals(",")){
//        break;
//        }