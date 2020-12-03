package sample;

public class Block {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------

    // Backend
    int x_Coord;
    int y_Coord;
    Block_GUI this_Block_GUI;
    Boolean is_Builder;

    // Core information
    int line_Index;
    String section;
    int blockNumber;
    double length;
    double grade;
    int next_Block_Number;
    int previous_Block_Number;
    Boolean isOccupied = false;

    // Yard only
    Boolean is_Yard;

    // Switch only
    Boolean is_Switch;
    int next_Block_Number_2;
    Boolean is_Switched;
    Boolean is_Alpha;
    Boolean is_Beta;
    Boolean is_Gamma;

    // Station only
    Boolean is_Station;
    String station_Name;
    String prev_Station_Name;
    String next_Station_Name;
    int ticket_Sales;

    // Failure Modes
    Boolean broken_Rail;
    Boolean track_Circuit_Failure;
    Boolean power_Failure;

    Boolean track_Heater;


    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Block(int param_X, int param_Y){
        // Backend
        this_Block_GUI = new Block_GUI(this);
        x_Coord = param_X;
        y_Coord = param_Y;
        is_Builder = true;

        // Core information
        section = "A";
        blockNumber = -1;
        length = 50.0;
        grade = 0.0;
        next_Block_Number = -1;
        previous_Block_Number = -1;

        // Yard only
        is_Yard = false;

        // Switch only
        is_Switch = false;
        next_Block_Number_2 = -1;
        is_Switched = false;
        is_Alpha = false;
        is_Beta = false;
        is_Gamma = false;


        // Station only
        is_Station = false;
        station_Name = "";
        prev_Station_Name = "";
        next_Station_Name = "";
        int ticket_Sales;

        // Failure Modes
        broken_Rail = false;
        track_Circuit_Failure = false;
        power_Failure = false;

        track_Heater = false;

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
            if(is_Station){
                this_Block_GUI.changeColor(Block_GUI.color_Map.get("Purple"));
            }
            if(is_Yard){
                this_Block_GUI.changeColor(Block_GUI.color_Map.get("Mustard"));
            }
            if(is_Switch){
                this_Block_GUI.changeColor(Block_GUI.color_Map.get("Blue"));
            }
        }

    }
}
