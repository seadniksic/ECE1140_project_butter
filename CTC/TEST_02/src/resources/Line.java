package resources;

import resources.Block;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Line {

    private String line;//name of track
    //private String trains[];
    private Boolean status;
    private List<Block> blocksList = new ArrayList<>();
    private List<String> infrastructure = new ArrayList<>();
    private Graph routeGraph;
    ArrayList<ArrayList<Integer>> adj;
    int v;

    public Line(){
        line = "";
        status = true;
        blocksList.clear();
        infrastructure.clear();
    }

    public Line(String l){
        line = l;
        status = true;
        blocksList.clear();
        infrastructure.clear();
    }

    public Line(String l, boolean s){
        line = l;
        status = s;
        blocksList.clear();
        infrastructure.clear();
    }

    public void add_Block(Character section,Integer number, Double length, Double grade, Integer speedLimit,
                          String infrastructure, String stationSide, Double elevation, Double cumulativeElevation){
        Block b = new Block(section, number, length,  grade, speedLimit, infrastructure, stationSide, elevation, cumulativeElevation);
        blocksList.add(b);
    }

    public void set_Line(String l){
        line= l;
    }

    public String get_Line(){
        return line;
    }


    public void toggle_Block_Occupancy(int index){

        if(blocksList.get(index).get_Occupancy() == true){
            blocksList.get(index).set_Occupancy(false);
        }else{
            blocksList.get(index).set_Occupancy(true);
        }

    }


    //TODO front end should be calling this
    public boolean get_Block_Occupancy(Integer blockNum){
        return blocksList.get(blockNum-1).get_Occupancy();
    }

    public String get_Block_Infrastructure(Integer blockNum){
        return blocksList.get(blockNum-1).get_Infrastructure();
    }

    public List<String> get_Infrastructure_List(){
        infrastructure.clear();
        for(Block bl : blocksList){
            if(!bl.get_Infrastructure().equals("")){
                infrastructure.add(bl.get_Infrastructure());
            }
        }
        return infrastructure;
    }

    public List<Character> get_Section_List(){
        List<Character> blocksSec = new ArrayList<>();
        if(blocksList.size() > 0) {
            Character currentChar = blocksList.get(0).get_Section();

            blocksSec.add(currentChar);

            for (Block b : blocksList) {
                if (currentChar != b.get_Section()) {
                    currentChar = b.get_Section();
                    blocksSec.add(currentChar);
                }
            }
        }
        return blocksSec;
    }

    public List<Integer> get_Blocks_In_Section_List(Integer currSec){
        List<Integer> blocksInSec = new ArrayList<>();
        for(Block b: blocksList){
            if((b.get_Section() - 1)  == (currSec + 64)){//use subtraction to force integer
                blocksInSec.add(b.get_Number());
            }
        }
        return blocksInSec;
    }

    public List<Integer> get_Blocks_Numbers_List(){
        List<Integer> blocksnum = new ArrayList<>();

        for(int i = 0; i < blocksList.size(); i++){
            blocksnum.add(blocksList.get(i).get_Number());
        }
        return blocksnum;
    }

    public List<Integer> get_Blocks_Are_Switch_List(){
        List<Integer> switches = new ArrayList<>();

        for(int i = 0; i < blocksList.size(); i++){
            if(blocksList.get(i).get_Infrastructure().contains("SWITCH") || blocksList.get(i).get_Infrastructure().contains("Switch")){
                switches.add(i+1);
            }

        }

        return switches;
    }

    public Double get_Distance_Between(String start, String stop){
        double distance =0;
        int startBlock = 0,stopBlock=1;
        //find block after start infrastructure and block of stop
        for(Block b: blocksList){
            if(b.get_Infrastructure().toLowerCase().contains(start.toLowerCase())){
                startBlock = b.get_Number() + 1;
            }
            if(b.get_Infrastructure().toLowerCase().contains((stop.toLowerCase()))){
                stopBlock = b.get_Number();
                System.out.println("STOPBLOCK = " + stopBlock);
                break;
            }

        }
        LinkedList<Integer> path = routeGraph.get_Path(adj,startBlock,stopBlock,151);

        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
            distance+= blocksList.get(i).get_Length();
        }

        return distance;
    }

    public Integer get_Number_Of_Blocks_Between(String start, String stop){
        Integer numOfBlocks =0;
        int startBlock = 0,stopBlock=1;
        //find block after start infrastructure and block of stop
        for(Block b: blocksList){
            if(b.get_Infrastructure().toLowerCase().contains(start.toLowerCase())){
                startBlock = b.get_Number() + 1;
            }
            if(b.get_Infrastructure().toLowerCase().contains((stop.toLowerCase()))){
                stopBlock = b.get_Number();
                System.out.println("STOPBLOCK = " + stopBlock);
                break;
            }

        }
        LinkedList<Integer> path = routeGraph.get_Path(adj,startBlock,stopBlock,151);
        return path.size();
    }

    public List<String> get_Block_Information_List(Integer b){
        List<String> information = new ArrayList<>();
        information.add(blocksList.get(b).get_Length().toString());
        information.add(blocksList.get(b).get_Grade().toString());
        information.add(blocksList.get(b).get_Speed_Limit().toString());
        information.add(blocksList.get(b).get_Infrastructure());
        information.add(blocksList.get(b).get_Elevation().toString());
        information.add(blocksList.get(b).get_Cumulative_Elevation().toString());
        if(blocksList.get(b).get_Occupancy()){
            information.add("Occupied");
        }else{
            information.add("Empty");
        }
        if(blocksList.get(b).get_Condition()){
            information.add("Open");
        }else{
            information.add("Closed");
        }

        return information;
    }

    private Double get_Distance_Between_Blocks(Integer start, Integer end){
        Double distance = 0.0;
        while(start != end) {
            distance += blocksList.get(start).get_Length();

            start++;
        }



        return distance;
    }


    public void close_Line(){
        for(Block b: blocksList){
               b.set_Condition(false);
        }
    }

    public void open_Line(){
        for(Block b: blocksList){
            b.set_Condition(true);
        }
    }

    public void close_Block(int b){
        blocksList.get(b-1).set_Condition(false);
    }

    public void open_Block(int b){

        blocksList.get(b-1).set_Condition(true);
    }

    public void get_Path(int startBlock, int stopBlock){
        routeGraph.get_Path(adj,startBlock,stopBlock,151);
    }

    public void create_Graph(){

         v = 151;
        // Adjacency list for storing which vertices are connected
         adj = new ArrayList<ArrayList<Integer>>(v);
        for (int i = 0; i < 151; i++) {
            adj.add(new ArrayList<Integer>());
        }

        for(int i = 0; i < v - 2; i ++){
            Block cb = blocksList.get(i);
            Block nb = blocksList.get(i+1);

            String cbInfra = cb.get_Infrastructure();
            cbInfra = cbInfra.replaceAll("\\s+","");
            int switchTo = -1;
            if(cbInfra.toLowerCase().contains("switch")){
                switchTo =  Integer.valueOf(cbInfra.substring(cbInfra.indexOf("SWITCH")+6));

                routeGraph.addEdge(adj,cb.get_Number(),switchTo);
            }
            if((line.toLowerCase().contains("g")) && (cb.get_Number() != 100) && (switchTo !=  nb.get_Number())) {
                routeGraph.addEdge(adj, cb.get_Number(), nb.get_Number());
            }
        }

        routeGraph.addEdge(adj,150,29);



    }

}
