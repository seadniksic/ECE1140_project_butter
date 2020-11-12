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
            description_label = new Label("Editing Line: " + (this_TMBD.this_Track.get_Line_At_Index(param_Line_Index).index + 1));
        }

        System.out.println(param_GridPane.getColumnCount());

        // Update Gridpane
        param_GridPane = new GridPane();
        Block[][] line_Block_Arr = this_TMBD.this_Track.line_ArrayList.get(param_Line_Index).block_Arr;
//        System.out.println("Length: " + line_Block_Arr.length);
//        System.out.println("Width: " + line_Block_Arr[0].length);
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

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label, return_To_Track_Button, canvas_GP);
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
    private void saveTextToFile(String param_String, File param_File){
        try{
            PrintWriter this_printWriter = new PrintWriter(param_File);
            this_printWriter.println(param_String);
            this_printWriter.close();
        }catch(IOException e){
            System.out.println("Darn");
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
                    // Update lines
                    System.out.println(next_Line);
                    current_Line_index = Integer.parseInt(String.valueOf(next_Line.charAt(3)));
                    this_TMBD.this_Track.add_Line(new Line(current_Line_index));
                }

                // Setting canvases to lines
                if(next_Line.substring(0, 3).equals("C: ")){
                    // Update lines
                    System.out.println(next_Line);
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
                        System.out.println("Substring atm: " + next_Line.substring(i, i+1));
                        if(!next_Line.substring(i, i+1).equals(",")){
                            if(!next_Line.substring(i, i+1).equals(" ")){
                                build += next_Line.substring(i, i+1);
                                System.out.println("Build: " + build);
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

                        System.out.println("x_Coord_S: " + x_Coord_S);
                        System.out.println("y_Coord_S: " + y_Coord_S);
                    }

                    // Assign elements from text file to data
                    String extracted_Section = block_Line_Elements.get(1);
                    int extracted_Block_Num = Integer.parseInt(block_Line_Elements.get(2));
                    double extracted_Block_Length = Double.parseDouble(block_Line_Elements.get(3));
                    double extracted_Block_Grade = Double.parseDouble(block_Line_Elements.get(4));
                    int extracted_Next_Block_Number = Integer.parseInt(block_Line_Elements.get(5));
                    int extracted_Previous_Block_Number = Integer.parseInt(block_Line_Elements.get(6));
                    Boolean extracted_is_Yard = Boolean.parseBoolean(block_Line_Elements.get(7));

                    for (Block[] blocks : this_TMBD.this_Track.line_ArrayList.get(current_Line_index).block_Arr) {
                        for (Block block : blocks) {
                            if(block.x_Coord == Integer.parseInt(x_Coord_S) && block.y_Coord == Integer.parseInt(y_Coord_S)){
                                block.section = extracted_Section;
                                block.blockNumber = extracted_Block_Num;
                                block.length = extracted_Block_Length;
                                block.grade = extracted_Block_Grade;
                                block.next_Block_Number = extracted_Next_Block_Number;
                                block.previous_Block_Number = extracted_Previous_Block_Number;
                                block.isYard = extracted_is_Yard;
                                if(block.blockNumber != -1){//TODO: May need to revise this
                                    block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Green"));
                                    if(block.isYard){
                                        block.this_Block_GUI.changeColor(Block_GUI.color_Map.get("Mustard"));
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

    //*******************************************************************************************************************************************************
    // Opens the first scene of the stage                                                                                                                   *
    //*******************************************************************************************************************************************************
    public void swap_to_Start_Scene(){
        murphy_Stage.setScene(return_Start_Scene());
        murphy_Stage.show();
    }
    //*******************************************************************************************************************************************************
    // Changes the scene when a new track is created                                                                                                        *
    //*******************************************************************************************************************************************************
    public void swap_To_New_Track_Scene(ArrayList<Button> param_Line_Button_ArrayList){
        if(this_TMBD.this_File != null){
            murphy_Stage.setScene(return_New_Track_Scene(param_Line_Button_ArrayList));
            murphy_Stage.show();
        }
    }
    //*******************************************************************************************************************************************************
    // Changes the scene when a line is selected                                                                                                            *
    //*******************************************************************************************************************************************************
    public void swap_To_Line_Scene(int param_Line_Index, GridPane param_GridPane){
        murphy_Stage.setScene(return_Line_Scene(param_Line_Index, param_GridPane));
        murphy_Stage.show();
    }


}