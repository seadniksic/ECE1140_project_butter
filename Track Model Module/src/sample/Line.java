package sample;

import java.util.ArrayList;

public class Line {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    String name;
    int index;
    int num_Rows;
    int num_Columns;
    ArrayList<Integer> occupancies;
    ArrayList<Double> distances;
    Block[][] block_Arr;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Line(int param_Index){
        block_Arr = new Block[0][0]; //TODO: Might need to change
        index = param_Index;
        name = "Line " + (index + 1);
        occupancies = new ArrayList<Integer>();
        distances = new ArrayList<Double>();
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    //*******************************************************************************************************************************************************
    // The block array is instantiated, given the x and y size by the user                                                                                  *
    //*******************************************************************************************************************************************************
    void set_Canvas(int x_Width, int y_Height){
        // Determine initial size of array
        block_Arr = new Block[x_Width][y_Height];

        // Fill array with empty blocks
        for(int i = 0; i < x_Width; i++){
            for(int j = 0; j < y_Height; j++){
                block_Arr[i][j] = new Block(i, j);
            }
        }

        // Update rows and cols
        num_Rows = x_Width;
        num_Columns = y_Height;
    }
}
