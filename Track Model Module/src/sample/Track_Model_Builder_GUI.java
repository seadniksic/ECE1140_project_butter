package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        return new Scene(this_VBox, 500, 200);
    }
    private Scene return_Line_Scene(int param_Line_Index){
        MenuBar start_MenuBar = new MenuBar();
        configureMenuBar(start_MenuBar);

        if(this_TMBD.this_File != null){
            description_label = new Label("Editing Line: " + (this_TMBD.this_Track.get_Line_At_Index(param_Line_Index).index + 1));
        }

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(start_MenuBar, description_label);
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
                this_TMBD.this_Track.add_Line(new Line(lineIndex)); //TODO: Might need to keep track of indices here

                Button newest_Line_Button = new Button(this_TMBD.this_Track.line_ArrayList.get(this_TMBD.this_Track.line_ArrayList.size() - 1).name);
                param_ArrayList.add(param_ArrayList.size() - 1,newest_Line_Button);
                swap_To_New_Track_Scene(param_ArrayList);
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_Line_Button(Button param_Button, int param_Line_Index){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                swap_To_Line_Scene(param_Line_Index);
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
            for(int i = 0; i < this_TMBD.this_Track.line_ArrayList.size(); i++){
                output += "L: " + this_TMBD.this_Track.line_ArrayList.get(i).index;
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

            // First scan to write data
            while(this_Scanner.hasNextLine()){
                String next_Line = this_Scanner.nextLine();
                if(next_Line.substring(0, 3).equals("L: ")){
                    // Update lines
                    System.out.println(next_Line);
                    this_TMBD.this_Track.add_Line(new Line(Integer.parseInt(String.valueOf(next_Line.charAt(3)))));
                    // TODO: Iterate though sub-text under line until next line is met

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

    //*******************************************************************************************************************************************************
    // Opens the first scene of the stage                                                                                                                   *
    //*******************************************************************************************************************************************************
    public void swap_to_Start_Scene(){
        builder_Stage.setScene(return_Start_Scene());
        builder_Stage.show();
    }
    //*******************************************************************************************************************************************************
    // Changes the scene when a new track is created                                                                                                        *
    //*******************************************************************************************************************************************************
    public void swap_To_New_Track_Scene(ArrayList<Button> param_Line_Button_ArrayList){
        if(this_TMBD.this_File != null){
            builder_Stage.setScene(return_New_Track_Scene(param_Line_Button_ArrayList));
            builder_Stage.show();
        }
    }
    //*******************************************************************************************************************************************************
    // Changes the scene when a line is selected                                                                                                            *
    //*******************************************************************************************************************************************************
    public void swap_To_Line_Scene(int param_Line_Index){
        builder_Stage.setScene(return_Line_Scene(param_Line_Index));
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
