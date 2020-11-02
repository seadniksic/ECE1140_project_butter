package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Line {
    // ---------------------------------------------------------------- Variables ---------------------------------------------------------------------------
    public int NUMROWS;
    public int NUMCOLS;

    public String name;
    public Block[][] XYBlockArray;
    public GridPane GUIGridpane;
    // ---------------------------------------------------- Constructors, Getters and Setters ---------------------------------------------------------------
    public Line(int paramNUMROWS, int paramNUMCOLS) throws FileNotFoundException {
        // Constructor stuff
        NUMROWS = paramNUMROWS;
        NUMCOLS = paramNUMCOLS;
        GUIGridpane = new GridPane();
        name = "Blue";
        XYBlockArray = new Block[NUMROWS][NUMCOLS];

        // Instantiating blocks and inserting their buttons to the gridpane
        for(int i = 0; i < NUMROWS; i++){
            for(int j = 0; j < NUMCOLS; j++){
                XYBlockArray[i][j] = new Block(Line.this, i, j);
                GUIGridpane.add(XYBlockArray[i][j].button, j, i);
            }
        }
        formatGUIGridPane();
    }
    // ------------------------------------------------------------- Private/Helper Functions ---------------------------------------------------------------
    private void formatGUIGridPane(){
        GUIGridpane.setMinSize(400, 200);
        GUIGridpane.setPadding(new Insets(1, 1, 1, 1));
        GUIGridpane.setVgap(1);
        GUIGridpane.setHgap(1);
        GUIGridpane.setAlignment(Pos.CENTER);
    }
    // ------------------------------------------------------------ Miscellaneous ---------------------------------------------------------------------------
    @Override public String toString() {
        String outputString = "Blocks: ";
        for (Block[] blocks : XYBlockArray) {
            for (Block block : blocks) {
                if(block != null){
                    outputString += block.blockNumber + " ";
                }
            }
        }
        return outputString;
    }
    public void outputLineFile(){
        // Creating the file
        try{
            File myObj = new File("importThisFile.txt");
            if(myObj.createNewFile()){
                System.out.println("File created: " + myObj.getName());
            }
            else{
                System.out.println("File already exists.");
            }
        }catch (IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        // Writing to the file
        try{
            FileWriter myWriter = new FileWriter("importThisFile.txt");

            // Find all blocks that have been declared and write them to the text file
            String toWrite = "";
            for (Block[] blocks : XYBlockArray) {
                for (Block block : blocks) {
                    if(block.blockNumber != -1){
                        toWrite += Line.this.name + ", (" + block.coords[1] + ":" + block.coords[0] + "), " + block.blockNumber + "\n";
                    }
                }
            }
            myWriter.write(toWrite);
            myWriter.close();
            System.out.println("Write successful");
        }catch (IOException e){
            System.out.println("An error occurred in write");
            e.printStackTrace();
        }
    }
    String fileToString(String textFileName) throws IOException {
        String outputString;
        BufferedReader br = new BufferedReader(new FileReader(textFileName));
        try{
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            outputString = everything;
        } finally {
            br.close();
        }
        return outputString;
    }
    ArrayList<ArrayList<String>> stringToLineWordsArrayList(String fileString){
        String currentLine = "";
        String word = "";
        ArrayList<ArrayList<String>> lineWordsArrayList = new ArrayList<ArrayList<String>>();

        for(int i = 0; i < fileString.length(); i++) {
            // Adds characters to current line until a new line character is found
            ArrayList<String> lineWords = new ArrayList<String>();
            if (fileString.charAt(i) != '\n') {
                currentLine += fileString.charAt(i);
            }
            else {
                for (int j = 0; j < currentLine.length(); j++) {
                    if (currentLine.charAt(j) != ',') {
                        if (currentLine.charAt(j) != ' ') {
                            word += currentLine.charAt(j);
                        }
                    }
                    else {// The character is a comma
                        lineWords.add(word);
                        word = "";
                    }
                }
                lineWordsArrayList.add(lineWords);
                currentLine = "";
            }
        }
        return lineWordsArrayList;
    }

    public void importLineFile(String fileName) throws IOException {
        ArrayList<ArrayList<String>> toPrintArrayList = stringToLineWordsArrayList(fileToString(fileName));

        // Loop to color the blocks and give them their block numbers from the text file
        for (ArrayList<String> strings : toPrintArrayList) {
            for (String string : strings) {
                if(string.charAt(0) == '('){
                    int fillCoords[] = new int[]{-1,-1};
                    for(int i = 0; i < string.length(); i++){
                        if(i == 1){
                            fillCoords[0] = Integer.parseInt(String.valueOf(string.charAt(i)));
                        }
                        if(i == 3){
                            fillCoords[1] = Integer.parseInt(String.valueOf(string.charAt(i)));
                        }
                        if(fillCoords[1] != -1){
                            XYBlockArray[fillCoords[1]][fillCoords[0]].declareBlock();
                            XYBlockArray[fillCoords[1]][fillCoords[0]].thisBlockMod = new BlockMod(XYBlockArray[fillCoords[1]][fillCoords[0]]);
                            System.out.println("Success!");
                        }
                    }
                    System.out.println(fillCoords[0] + ", " + fillCoords[1]);
                }
            }
        }
    }
}
