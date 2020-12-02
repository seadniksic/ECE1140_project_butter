package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.FileNotFoundException;



public class BlockMod {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public Block parentBlock;
    public Stage modStage;

    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public BlockMod(Block paramParentBlock) {
        parentBlock = paramParentBlock;
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------


    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    public void openModWindow() {
        // Adding a button
        Label label = new Label("Reset block below");

        Button greyButton = new Button("Grey");
        configureButtonToColor(greyButton, "Grey");

        TextField textField = new TextField();

        Button submitButton = new Button("Submit");
        configureSubmitButton(submitButton, textField);

        VBox layout = new VBox();
        layout.getChildren().addAll(label, greyButton, textField, submitButton);
        layout.setAlignment(Pos.CENTER);

        // Adding the stack to the scene
        Scene modScene = new Scene(layout, 500, 200);
        modStage = new Stage();
        modStage.setTitle("Modifications for " + parentBlock.blockNumber);
        modStage.setScene(modScene);
        modStage.show();
    }

    private void configureButtonToColor(Button paramButton, String color){
    // Assigning actions for when the block is clicked
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                parentBlock.undeclareBlock();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    };
    paramButton.setOnAction(event);
    }
    private void configureSubmitButton(Button paramButton, TextField paramTextField){
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                if(paramTextField.getText().length() > 0){
                    parentBlock.blockNumber = Integer.parseInt(paramTextField.getText());
                }
                parentBlock.parentLine.outputLineFile();
                modStage.close();
            }
        };
        paramButton.setOnAction(event);
    }

}
