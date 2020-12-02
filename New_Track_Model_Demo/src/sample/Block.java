package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class Block {

    // ---------------------------------------------------------------- Variables -------------------------------------------------------------------------
    // Integers, Doubles and Strings
    public static int totalBlocks = 0;
    public int blockNumber = -1;

    int[]coords;
    double grade = -1.0;
    double length = -1.0;

    public Line parentLine;

    // Variables dealing with appearance
    public FileInputStream inputStream;
    public Button button;
    public ImageView imageView;
    public Image image;

    // Object that causes changes to this block
    BlockMod thisBlockMod;

    // Hashmap for file paths to color images
    static public HashMap<String, String> colorMap;

    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Block(Line paramParentLine, int coordX, int coordY){
        // Constructor stuff
        coords = new int[]{coordX, coordY};

        parentLine = paramParentLine;
        setUpColorMap();

        try{
            button = new Button();
            changeColor(colorMap.get("Grey"));
        }
        catch (FileNotFoundException f){
            System.out.println("Dang");
        }

        // Assigning actions for when the block is clicked
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Declare a block
                try {
                    Block.this.declareBlock();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // If the block has been declared previously, open the window to make modifications
                if(thisBlockMod != null){
                    thisBlockMod.openModWindow();
                    System.out.println(Block.this.toString());
                }
                else{// Otherwise, set up a new mod object for the block
                    thisBlockMod = new BlockMod(Block.this);
                    System.out.println("New BlockMod: " + Block.this.blockNumber);
                }
            }
        };
        // Actually mapping the actions to when the button is clicked
        button.setOnAction(event);
    }

    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    private void setUpColorMap(){
        colorMap = new HashMap<String,String>();
        colorMap.put("Blue", "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Junk\\New_Track_Model_Demo\\Blue.png");
        colorMap.put("Green", "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Junk\\New_Track_Model_Demo\\Green.png");
        colorMap.put("Mustard", "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Junk\\New_Track_Model_Demo\\Mustard.png");
        colorMap.put("Purple", "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Junk\\New_Track_Model_Demo\\Purple.png");
        colorMap.put("Grey", "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Junk\\New_Track_Model_Demo\\Grey.png");
        colorMap.put("Red", "E:\\Fat Documents\\Pitt\\Junior Year\\1140 - Systems and Project Engineering\\Junk\\New_Track_Model_Demo\\Red.png");
    }



    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    @Override public String toString() {
        String outputString = "";

        outputString += "Block number: " + blockNumber;
        return outputString;
    }
    public void changeColor(String imageFilePath) throws FileNotFoundException {
        inputStream = new FileInputStream(imageFilePath);
        image = new Image(inputStream);
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setGraphic(imageView);
        button.setPadding(new Insets(0));
    }
    public void undeclareBlock() throws FileNotFoundException {
        if(this.blockNumber != -1){
            totalBlocks--;
            //TODO: Come up with a way to undo the numbers of all previous blocks
            changeColor(Block.colorMap.get("Grey"));
            button.setPadding(new Insets(0));
            System.out.println("Undeclaring block: " + this.blockNumber);
            this.blockNumber = -1;
            this.thisBlockMod = null;
            parentLine.outputLineFile();
        }
    }
    public void declareBlock() throws FileNotFoundException {
        if(this.blockNumber == -1){
            totalBlocks++;
            blockNumber = totalBlocks;
            changeColor(Block.colorMap.get("Green"));

            button.setPadding(new Insets(0));

            System.out.println("Declaring block: " + blockNumber);

            parentLine.outputLineFile();
        }
    }
}
