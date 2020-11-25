package sample;

public class Block {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------

    // Backend
    int x_Coord;
    int y_Coord;
    Block_GUI this_Block_GUI;
    Boolean is_Builder;

    // Core information
    String section;
    int blockNumber;
    double length;
    double grade;
    int next_Block_Number;
    int previous_Block_Number;
    Boolean isOccupied = false;

    // Yard only
    Boolean isYard;

    // Switch only
    Boolean isSwitch;
    int next_Block_Number_2;
    Boolean is_Switched;


    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Block(int param_X, int param_Y){
        // Backend
        this_Block_GUI = new Block_GUI(this);
        x_Coord = param_X;
        y_Coord = param_Y;
        is_Builder = true;

        // Core information
        section = "!";
        blockNumber = -1;
        length = -1.0;
        grade = -1.0;
        next_Block_Number = -1;
        previous_Block_Number = -1;

        // Yard only
        isYard = false;

        // Switch only
        isSwitch = false;
        next_Block_Number_2 = -1;
        is_Switched = false;
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    public void set_Next(int param_Next_Block_Number){
        next_Block_Number = param_Next_Block_Number;
    }

    // Commands from Train Model

    // Commands from Track Controller
    public void set_Occupancy(Boolean param_Is_Occupied){
        isOccupied = param_Is_Occupied;
        if(isOccupied){
            this_Block_GUI.changeColor(Block_GUI.color_Map.get("Red"));
        }else{
            this_Block_GUI.changeColor(Block_GUI.color_Map.get("Green"));
            if(isYard){
                this_Block_GUI.changeColor(Block_GUI.color_Map.get("Mustard"));
            }
            if(isSwitch){
                this_Block_GUI.changeColor(Block_GUI.color_Map.get("Blue"));
            }
        }

    }
}
