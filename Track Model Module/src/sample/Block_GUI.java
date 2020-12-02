package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Block_GUI {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------

    Block parent_Block;

    // Variables dealing with appearance
    public Button this_Button;
    public ImageView this_Image_View;
    public Image this_Image;
    public FileInputStream this_FileInputStream;
    static public HashMap<String, String> color_Map;


    TextField block_Num_TF;
    TextField line_TF;
    TextField section_TF;
    TextField length_TF;
    TextField grade_TF;
    TextField set_Next_TF;
    TextField set_Previous_TF;
    TextField set_Next_2_TF;
    TextField station_Name_TF;

    Label error_Label;

    CheckBox is_Yard_CheckBox;

    CheckBox is_Switch_CheckBox;
    RadioButton is_Alpha_RB;
    RadioButton is_Beta_RB;
    RadioButton is_Gamma_RB;

    CheckBox is_Station_CheckBox;

    Stage block_Stage;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Block_GUI(Block param_Parent_Block){
        this_Button = new Button();
        map_This_Button(this_Button);
        parent_Block = param_Parent_Block;
        block_Stage = new Stage();
        block_Stage.setTitle("Block Editor");
        set_Up_Color_Map();
        changeColor(color_Map.get("Grey"));
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    private void set_Up_Color_Map(){
        color_Map = new HashMap<String,String>();
        String folder = "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Project Repository\\ECE1140_project_butter\\Track Model Module\\src\\sample\\";
        color_Map.put("Blue", folder + "Blue.png");
        color_Map.put("Green", folder + "Green.png");
        color_Map.put("Mustard", folder + "Mustard.png");
        color_Map.put("Purple", folder + "Purple.png");
        color_Map.put("Grey", folder + "Grey.png");
        color_Map.put("Red", folder + "Red.png");
    }
    public void changeColor(String param_Path) {
        try {
            this_FileInputStream = new FileInputStream(param_Path);
            this_Image = new Image(this_FileInputStream);
            this_Image_View = new ImageView(this_Image);
            this_Image_View.setFitWidth(20);
            this_Image_View.setFitHeight(20);
            this_Button.setGraphic(this_Image_View);
            this_Button.setPadding(new Insets(0));
        } catch (FileNotFoundException f) {
            System.out.println("ERROR! File not found");
        }

    }

    private void map_This_Button(Button param_Button){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                summon_Block_Editor(false, parent_Block.is_Builder);
                System.out.println(to_String());
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_Save_Button(Button param_Button) {
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                // Perform checks for proper input
                System.out.println("Parent: " + parent_Block.is_Switch + ", " + "Switch group: " + switch_RB_Group_isSelected());
                if(parent_Block.is_Switch && !switch_RB_Group_isSelected()){
                    // Change error label
                    error_Label.setText("ERROR: Please select an option for the switch.");
                }else{
                    parent_Block.blockNumber = (block_Num_TF.getText() != "" ? Integer.parseInt(block_Num_TF.getText()) : parent_Block.blockNumber);
                    parent_Block.section = (section_TF.getText() != "" ? section_TF.getText() : parent_Block.section);
                    parent_Block.length = (length_TF.getText() != "" ? Double.parseDouble(length_TF.getText()) : parent_Block.length);
                    parent_Block.grade = (grade_TF.getText() != "" ? Double.parseDouble(grade_TF.getText()) : parent_Block.grade);
                    parent_Block.next_Block_Number = (set_Next_TF.getText() != "" ? Integer.parseInt(set_Next_TF.getText()) : parent_Block.next_Block_Number);
                    if(parent_Block.is_Switch){
                        parent_Block.is_Alpha = (is_Alpha_RB.isSelected() != false ? true : is_Alpha_RB.isSelected());
                        parent_Block.is_Beta = (is_Beta_RB.isSelected() != false ? true : is_Beta_RB.isSelected());
                        parent_Block.is_Gamma = (is_Gamma_RB.isSelected() != false ? true : is_Gamma_RB.isSelected());

                    }
                    if(parent_Block.is_Station){
                        parent_Block.station_Name = (station_Name_TF.getText() != "" ? station_Name_TF.getText() : parent_Block.station_Name);
                    }
                    parent_Block.previous_Block_Number = (set_Previous_TF.getText() != "" ? Integer.parseInt(set_Previous_TF.getText()) : parent_Block.previous_Block_Number);
                    parent_Block.is_Yard = (is_Yard_CheckBox.isSelected() != false ? true : is_Yard_CheckBox.isSelected());
                    parent_Block.is_Switch = (is_Switch_CheckBox.isSelected() != false ? true : is_Switch_CheckBox.isSelected());


                    parent_Block.is_Station = (is_Station_CheckBox.isSelected() != false ? true : is_Station_CheckBox.isSelected());

                    //TODO: Only change color if the block number is not -1, (declared)
                    if(parent_Block.blockNumber != -1){
                        changeColor(color_Map.get("Green"));
                        if(parent_Block.is_Yard){
                            changeColor(color_Map.get("Mustard"));
                        }
                        if(parent_Block.is_Switch){
                            changeColor(color_Map.get("Blue"));
                        }
                        if(parent_Block.is_Station){
                            changeColor(color_Map.get("Purple"));
                        }
                    }
                    block_Stage.close();
                }
            }
        };
        param_Button.setOnAction(event);
    }
    private void map_is_Switch_CheckBox(CheckBox param_Checkbox){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                parent_Block.is_Switch = is_Switch_CheckBox.isSelected(); //TODO: Selecting the checkbox acts as a save, but should have no impact since the user will notice the blue + blknum
                summon_Block_Editor(true, parent_Block.is_Builder);
            }
        };
        param_Checkbox.setOnAction(event);
    }
    private void map_Is_Alpha_RB(RadioButton param_Radio_Button){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("alpha button: " + is_Alpha_RB.isSelected());
                parent_Block.is_Alpha = is_Alpha_RB.isSelected(); //TODO: Selecting the checkbox acts as a save, but should have no impact since the user will notice the blue + blknum
                parent_Block.is_Beta = false;
                parent_Block.is_Gamma = false;
                summon_Block_Editor(true, parent_Block.is_Builder);
            }
        };
        param_Radio_Button.setOnAction(event);
    }
    private void map_Is_Beta_RB(RadioButton param_Radio_Button){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                parent_Block.is_Beta = is_Beta_RB.isSelected(); //TODO: Selecting the checkbox acts as a save, but should have no impact since the user will notice the blue + blknum
                parent_Block.is_Gamma = false;
                parent_Block.is_Alpha = false;
                summon_Block_Editor(true, parent_Block.is_Builder);
            }
        };
        param_Radio_Button.setOnAction(event);
    }
    private void map_Is_Gamma_RB(RadioButton param_Radio_Button){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                parent_Block.is_Gamma = is_Gamma_RB.isSelected(); //TODO: Selecting the checkbox acts as a save, but should have no impact since the user will notice the blue + blknum
                parent_Block.is_Alpha = false;
                parent_Block.is_Beta = false;
                summon_Block_Editor(true, parent_Block.is_Builder);
            }
        };
        param_Radio_Button.setOnAction(event);
    }
    private void map_is_Station_CheckBox(CheckBox param_Checkbox){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                parent_Block.is_Station = is_Station_CheckBox.isSelected(); //TODO: Selecting the checkbox acts as a save, but should have no impact since the user will notice the blue + blknum
                summon_Block_Editor(true, parent_Block.is_Builder);
            }
        };
        param_Checkbox.setOnAction(event);
    }

    private Scene return_Builder_Block_Editor_Scene(){

        Label description_label = new Label("Welcome to the block editor!");

        // CHECKBOXES
        // Yard checkbox
        is_Yard_CheckBox = new CheckBox("Yard");
        is_Yard_CheckBox.setSelected(parent_Block.is_Yard);
        // Switch checkbox
        is_Switch_CheckBox = new CheckBox("Switch");
        is_Switch_CheckBox.setSelected(parent_Block.is_Switch);
        map_is_Switch_CheckBox(is_Switch_CheckBox);
        // Station checkbox
        is_Station_CheckBox = new CheckBox("Station");
        is_Station_CheckBox.setSelected(parent_Block.is_Station);
        map_is_Station_CheckBox(is_Station_CheckBox);

        // Holds checkbox row
        HBox checkBox_HBox = new HBox();
        checkBox_HBox.getChildren().addAll(is_Yard_CheckBox, is_Switch_CheckBox, is_Station_CheckBox);
        checkBox_HBox.setAlignment(Pos.TOP_CENTER);
        checkBox_HBox.setSpacing(20);
        // Save button
        Button save_Button = new Button("Save and Close");
        map_Save_Button(save_Button);
        VBox this_VBox = new VBox();

        // Only add the settings if they are selected as such
        HBox temp_Set_Next_2_HBox = new HBox();
        if(is_Alpha_RB != null){
            if(is_Switch_CheckBox.isSelected() && is_Alpha_RB.isSelected()){
                temp_Set_Next_2_HBox = return_Set_Next_2_HBox();
            }
        }

        // Station HBox
        HBox temp_Station_Name_HBox = new HBox();
        if(is_Station_CheckBox.isSelected()){
            temp_Station_Name_HBox = return_Station_name_HBox();
        }

        // Switch CheckBox semantics
        HBox temp_Switch_HBox = new HBox();
        if(is_Switch_CheckBox.isSelected()){
            // Radio button config
            is_Alpha_RB = new RadioButton("Alpha");
            System.out.println("Set selected(parentB...): " + parent_Block.is_Alpha);
            is_Alpha_RB.setSelected(parent_Block.is_Alpha);
            map_Is_Alpha_RB(is_Alpha_RB);

            System.out.println("Set selected(parentB..B): " + parent_Block.is_Beta);
            is_Beta_RB = new RadioButton("Beta");
            is_Beta_RB.setSelected(parent_Block.is_Beta);
            map_Is_Beta_RB(is_Beta_RB);

            System.out.println("Set selected(parentB..G): " + parent_Block.is_Gamma);
            is_Gamma_RB = new RadioButton("Gamma");
            is_Gamma_RB.setSelected(parent_Block.is_Gamma);
            map_Is_Gamma_RB(is_Gamma_RB);

            // ToggleGroup config
            final ToggleGroup switch_TG = new ToggleGroup();
            is_Alpha_RB.setToggleGroup(switch_TG);
            is_Beta_RB.setToggleGroup(switch_TG);
            is_Gamma_RB.setToggleGroup(switch_TG);

            // Temp switch HBox config
            temp_Switch_HBox.getChildren().addAll(is_Alpha_RB, is_Beta_RB, is_Gamma_RB);
            temp_Switch_HBox.setAlignment(Pos.TOP_CENTER);
            temp_Switch_HBox.setSpacing(20);
        }

        // Holds columns
        HBox columns_HBox = new HBox();

        // Column one
        VBox ints_VBox = new VBox();
        ints_VBox.getChildren().addAll(return_block_Num_HBox(), return_Set_Previous_HBox(), return_Set_Next_HBox(), temp_Set_Next_2_HBox);
        ints_VBox.setSpacing(20);
        ints_VBox.setAlignment(Pos.CENTER_RIGHT);

        // Column two
        VBox text_VBox = new VBox();
        text_VBox.getChildren().addAll(return_section_HBox(), temp_Station_Name_HBox);
        text_VBox.setSpacing(20);

        // Column three
        VBox double_VBox = new VBox();
        double_VBox.getChildren().addAll(return_grade_HBox(), return_length_HBox());
        double_VBox.setSpacing(20);

        // Adding columns to column box
        columns_HBox.getChildren().addAll(text_VBox, ints_VBox, double_VBox);
        columns_HBox.setSpacing(20);
        columns_HBox.setAlignment(Pos.CENTER);

        // Error label
        error_Label = new Label("");

        // Adding column box and additional elements to total vbox
        this_VBox.getChildren().addAll(description_label, checkBox_HBox, temp_Switch_HBox, columns_HBox, save_Button, error_Label);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);
        this_VBox.setAlignment(Pos.CENTER);

        return new Scene(this_VBox, 900, 400);
    }
    private Scene return_Murphy_Block_Editor_Scene(){
        Label description_label = new Label("Welcome to the block editor!");
        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(description_label);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 600);
    }
    private void summon_Block_Editor(Boolean param_Is_Summoned_Again, Boolean param_Is_Builder){
        if(param_Is_Summoned_Again){
            block_Stage.close();
        }
        if(param_Is_Builder){
            block_Stage.setScene(return_Builder_Block_Editor_Scene());
        }else{
            block_Stage.setScene(return_Murphy_Block_Editor_Scene());
        }
        block_Stage.show();
    }
    private HBox return_block_Num_HBox(){
        Label block_Num_Label = new Label("Block Number: ");
        block_Num_TF = new TextField(String.valueOf(parent_Block.blockNumber));
        HBox block_Num_HBox = new HBox();
        block_Num_HBox.getChildren().addAll(block_Num_Label, block_Num_TF);
        block_Num_HBox.setAlignment(Pos.CENTER);
        block_Num_HBox.setSpacing(10);

        return block_Num_HBox;
    }
    private HBox return_section_HBox(){
        Label section_Label = new Label("Section: ");
        section_TF = new TextField(parent_Block.section);
        HBox section_HBox = new HBox();
        section_HBox.getChildren().addAll(section_Label, section_TF);
        section_HBox.setAlignment(Pos.CENTER);

        return section_HBox;
    }
    private HBox return_length_HBox(){
        Label length_Label = new Label("Length (m): ");
        length_TF = new TextField(String.valueOf(parent_Block.length));
        HBox length_HBox = new HBox();
        length_HBox.getChildren().addAll(length_Label, length_TF);
        length_HBox.setAlignment(Pos.CENTER);

        return length_HBox;
    }
    private HBox return_grade_HBox(){
        Label grade_Label = new Label("Grade (%): ");
        grade_TF = new TextField(String.valueOf(parent_Block.grade));
        HBox grade_HBox = new HBox();
        grade_HBox.getChildren().addAll(grade_Label, grade_TF);
        grade_HBox.setAlignment(Pos.CENTER);

        return grade_HBox;
    }
    private HBox return_Set_Next_HBox(){
        Label set_Next_Label = new Label("Next Block:       ");
        set_Next_TF = new TextField(String.valueOf(parent_Block.next_Block_Number));
        HBox set_Next_HBox = new HBox();
        set_Next_HBox.getChildren().addAll(set_Next_Label, set_Next_TF);
        set_Next_HBox.setAlignment(Pos.CENTER);

        return set_Next_HBox;
    }
    private HBox return_Set_Next_2_HBox(){
        Label set_Next_2_Label = new Label("Next Block 2: ");
        set_Next_2_TF = new TextField(String.valueOf(parent_Block.next_Block_Number_2));
        HBox set_Next_2_HBox = new HBox();
        set_Next_2_HBox.getChildren().addAll(set_Next_2_Label, set_Next_2_TF);
        set_Next_2_HBox.setAlignment(Pos.CENTER);

        return set_Next_2_HBox;
    }
    private HBox return_Station_name_HBox(){
        Label station_Name_Label = new Label("Station Name: ");
        station_Name_TF = new TextField(String.valueOf(parent_Block.station_Name));
        HBox station_Name_HBox = new HBox();
        station_Name_HBox.getChildren().addAll(station_Name_Label, station_Name_TF);
        station_Name_HBox.setAlignment(Pos.CENTER);

        return station_Name_HBox;
    }
    private HBox return_Set_Previous_HBox(){
        Label set_Previous_Label = new Label("Previous Block: ");
        set_Previous_TF = new TextField(String.valueOf(parent_Block.previous_Block_Number));
        HBox set_Previous_HBox = new HBox();
        set_Previous_HBox.getChildren().addAll(set_Previous_Label, set_Previous_TF);
        set_Previous_HBox.setAlignment(Pos.CENTER);

        return set_Previous_HBox;
    }

    private Boolean switch_RB_Group_isSelected(){
        Boolean to_Return = false;

        if(is_Alpha_RB != null && is_Beta_RB != null && is_Gamma_RB != null){
            if(is_Alpha_RB.isSelected() || is_Beta_RB.isSelected() || is_Gamma_RB.isSelected()){
                to_Return = true;
            }
        }
        return to_Return;
    }

    private String to_String(){
        String output = "";
        output += "Block Properties:" + "\n";
        output += "Coordinates: " + parent_Block.x_Coord + "x" + parent_Block.y_Coord + "\n";
        output += "Section: " + parent_Block.section + "\n";
        output += "Number: " + parent_Block.blockNumber;
        return output;
    }
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
}
