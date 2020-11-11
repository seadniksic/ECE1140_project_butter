package sample;

import java.io.File;

public class Track_Model_Builder_Data {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    File this_File;
    Track this_Track;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    public Track_Model_Builder_Data(File param_This_File, String param_File_Name){
        this_File = param_This_File;
        this_Track = new Track(param_File_Name);
    }

}
