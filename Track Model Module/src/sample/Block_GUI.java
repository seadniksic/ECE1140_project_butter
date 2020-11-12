package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    TextField section_TF;

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
                summon_Block_Editor();
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

                if(parent_Block.blockNumber != -1){
                    changeColor(color_Map.get("Red"));
                }
                block_Stage.close();
            }
        };
        param_Button.setOnAction(event);
    }
    private Scene return_Block_Editor_Scene(){

        Label description_label = new Label("Welcome to the block editor!");

        Button save_Button = new Button("Save and Close");
        map_Save_Button(save_Button);

        VBox this_VBox = new VBox();
        this_VBox.getChildren().addAll(description_label, return_section_HBox(), return_block_Num_HBox(), save_Button);
        this_VBox.setAlignment(Pos.TOP_CENTER);
        this_VBox.setSpacing(20);

        return new Scene(this_VBox, 500, 200);
    }
    private void summon_Block_Editor(){
        block_Stage.setScene(return_Block_Editor_Scene());
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
