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

    CheckBox is_Yard_CheckBox;
    CheckBox is_Switch_CheckBox;
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
                parent_Block.blockNumber = (block_Num_TF.getText() != "" ? Integer.parseInt(block_Num_TF.getText()) : parent_Block.blockNumber);
                parent_Block.section = (section_TF.getText() != "" ? section_TF.getText() : parent_Block.section);
                parent_Block.length = (length_TF.getText() != "" ? Double.parseDouble(length_TF.getText()) : parent_Block.length);
                parent_Block.grade = (grade_TF.getText() != "" ? Double.parseDouble(grade_TF.getText()) : parent_Block.grade);
                parent_Block.next_Block_Number = (set_Next_TF.getText() != "" ? Integer.parseInt(set_Next_TF.getText()) : parent_Block.next_Block_Number);
                if(parent_Block.is_Switch){
                    parent_Block.next_Block_Number_2 = (set_Next_2_TF.getText() != "" ? Integer.parseInt((set_Next_2_TF.getText())) : parent_Block.next_Block_Number_2);
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

        is_Yard_CheckBox = new CheckBox("Yard");
        is_Yard_CheckBox.setSelected(parent_Block.is_Yard);

        is_Switch_CheckBox = new CheckBox("Switch");
        is_Switch_CheckBox.setSelected(parent_Block.is_Switch);
        map_is_Switch_CheckBox(is_Switch_CheckBox);

        is_Station_CheckBox = new CheckBox("Station");
        is_Station_CheckBox.setSelected(parent_Block.is_Station);
        map_is_Station_CheckBox(is_Station_CheckBox);

        Button save_Button = new Button("Save and Close");
        map_Save_Button(save_Button);

        VBox this_VBox = new VBox();

        // Only add the settings for special blocks if they are selected as such
        HBox temp_Set_Next_2_HBox = new HBox();
        if(is_Switch_CheckBox.isSelected()){
            System.out.println("Reassignment");
            temp_Set_Next_2_HBox = return_Set_Next_2_HBox();
        }
        this_VBox.getChildren().addAll(description_label, return_section_HBox(), return_block_Num_HBox(), return_length_HBox(), return_grade_HBox(), return_Set_Previous_HBox(), temp_Set_Next_2_HBox, return_Set_Next_HBox(), is_Yard_CheckBox, is_Switch_CheckBox, is_Station_CheckBox, save_Button);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 600);
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
        Label set_Next_Label = new Label("Next Block: ");
        set_Next_TF = new TextField(String.valueOf(parent_Block.next_Block_Number));
        HBox set_Next_HBox = new HBox();
        set_Next_HBox.getChildren().addAll(set_Next_Label, set_Next_TF);
        set_Next_HBox.setAlignment(Pos.CENTER);

        return set_Next_HBox;
    }
    private HBox return_Set_Next_2_HBox(){
        //Label set_Next_2_Label = new Label("Next Block 2 (Switches Only): ");
        final TextArea text = new TextArea("Next Block 2 (Switches Only): ");
        text.setStyle("-fx-font-size: 12px;");
        text.setWrapText(true);
        text.setMaxHeight(12);
        text.setMaxWidth(175);
        text.setStyle("-fx-background-color: #F4F4F4");
        text.setEditable(false);
        set_Next_2_TF = new TextField(String.valueOf(parent_Block.next_Block_Number_2));
        HBox set_Next_2_HBox = new HBox();
        set_Next_2_HBox.getChildren().addAll(text, set_Next_2_TF);
        set_Next_2_HBox.setAlignment(Pos.CENTER);

        return set_Next_2_HBox;
    }
    private HBox return_Set_Previous_HBox(){
        Label set_Previous_Label = new Label("Previous Block: ");
        set_Previous_TF = new TextField(String.valueOf(parent_Block.previous_Block_Number));
        HBox set_Previous_HBox = new HBox();
        set_Previous_HBox.getChildren().addAll(set_Previous_Label, set_Previous_TF);
        set_Previous_HBox.setAlignment(Pos.CENTER);

        return set_Previous_HBox;
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
