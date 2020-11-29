package resources;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import networking.*;

import java.io.*;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.*;

public class CTC_Back implements CTC_Interface {

    private String schedulePath ;
    static private File scheduleFile;
    private String trackPath ;
    static private File trackFile;
    static private List<Line> lineList = new ArrayList<>();
    static private List<Train> trainList = new ArrayList<>();
    static private boolean automatic = false;
    static private long simTime = 0;




    public CTC_Back(){
        trainList.clear();
        lineList.clear();
    }

    public CTC_Back(List<Line> lines, List<Train> trains){
        lineList = lines;
        trainList = trains;
    }

    public String get_Schedule_Path(){ return schedulePath;}

    public void set_Schedule_Path(String sP){schedulePath = sP;}

    public String get_Track_Path(){ return trackPath;}

    public void set_Track_Path(String tP){trackPath = tP;}

    public List<Line> get_Line_List() {
        return lineList;
    }

    public List<Train> get_Train_List() {
        return trainList;
    }

    public List<String> get_Line_Name_List() {
        List<String> names = new ArrayList<>();
        for(Line t : lineList){
            names.add(t.get_Line());
        }
        return names;
    }

    public List<String> get_Train_Name_List() {
        List<String> names = new ArrayList<>();
        for(Train t: trainList){
            names.add(t.get_Name());
        }

        return names;
    }

    private List<String> get_Infrastructure_Of(Line t){


        return t.get_Infrastructure_List();
    }

    public void toggle_Automatic() {
        automatic = !automatic;
    }

    public boolean get_Automatic(){ return automatic;}

    public long get_Sim_Time(){return simTime;}

   // public void create_Track(Line trac){
      //  lineList.add(trac);
   // }

    public void create_Train(Train t){
        trainList.add(t);

        //System.out.println("CREATED TRAIN: " + t.get_Name());
    }

    public void create_Line(Line l){
        lineList.add(l);
    }

    public void set_Train_Schedule(String train, List<String> infrastructures, List<LocalTime> times ){

    }

    public void clear_Train_List(){
        trainList.clear();
    }

    public void clear_Line_List(){
        lineList.clear();
    }

    public void calculate_Authority(Integer trainIndex){
        //Integer blocks_between = lineList.get().get_Number_Of_Blocks_Between(trainList.get(trainIndex).
        // getCurrentInfrastructure(), trainList.get(trainIndex).getNextInfrastructure());
        //TODO GET NUM OF BLOCKS BETWEEN NEEDS TO REALIZE SWITCH
        //
        //TODO have see 4 blocks ahead of it. if clear set to 4 if less set lower
        System.out.println("CALC AUTH HIT:");
        Integer blocks_between = 10;

        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now


        for(int i  = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(trainList.get(trainIndex).get_Current_Line())){
                lineIndex = i;
            }
        }
        System.out.println("Line Index: " + lineIndex);

        System.out.println("Current INFR: "+ trainList.get(trainIndex).get_Current_Infrastructure());
        System.out.println("Next INFR: " + trainList.get(trainIndex).get_Next_Infrastructure());

        blocks_between =
         lineList.get(lineIndex).get_Number_Of_Blocks_Between(trainList.get(trainIndex).get_Current_Infrastructure(),
                trainList.get(trainIndex).get_Next_Infrastructure());


        System.out.println("AUTHORITY: " + blocks_between);
        trainList.get(trainIndex).set_Authority(blocks_between);
        //Integer blocks_between = 11;

    }

    public void calculate_Suggested_Speed(Integer trainIndex){

        System.out.println("SUG SPEED HIT:");
        Double distance = determine_Distance(trainIndex);
        System.out.println("Distance: " + distance);
        long time = trainList.get(trainIndex).get_Time_Between(trainList.get(trainIndex).get_Current_Infrastructure(),
                trainList.get(trainIndex).get_Next_Infrastructure());//this is in minutes
        double r = (distance * 6.0)/(time*100.0);//this is in Km/Hr
        System.out.println("time: " + time);
        System.out.println("r: " + r);

        //test
        trainList.get(trainIndex).set_Suggest_Speed((double)r);
    }

    public void dispatch(Integer trainIndex) throws RemoteException {
        System.out.println("hit dispatch automatic function");
        System.out.println(trainIndex);

        if(!trainList.get(trainIndex).get_Sent_Create_Command()){
            Network.tcsw_Interface.create_Train(
                    trainList.get(trainIndex).get_Number_Of_Cars(),
                    trainList.get(trainIndex).get_Current_Line(),
                    trainList.get(trainIndex).get_Current_Block());
            trainList.get(trainIndex).set_Sent_Create_Command(true);
        }
       // System.out.println(trainList.get(trainIndex).get_Number_Of_Cars());
        //determine_Path(trainIndex);

        calculate_Suggested_Speed(trainIndex);
        calculate_Authority(trainIndex);
        Network.tcsw_Interface.send_Speed_Authority(trainIndex, trainList.get(trainIndex).get_Suggest_Speed(),
          trainList.get(trainIndex).get_Authority());
    }

    public void dispatch(Integer trainIndex, String nextStop, LocalTime arrivalTime) throws RemoteException{
        System.out.println("hit dispatch manual function");

        if(!trainList.get(trainIndex).get_Sent_Create_Command()){
            Network.tcsw_Interface.create_Train(
                    trainList.get(trainIndex).get_Number_Of_Cars(),
                    trainList.get(trainIndex).get_Current_Line(),
                    trainList.get(trainIndex).get_Current_Block());

            System.out.println(trainList.get(trainIndex).get_Current_Block());
            trainList.get(trainIndex).set_Sent_Create_Command(true);
        }
       // String holdCurrentInfr = trainList.get(trainIndex).get_Current_Infrastructure();
        Integer holdCurrentIndex = trainList.get(trainIndex).get_Current_Index();
        List<String> infrListHold = trainList.get(trainIndex).get_Infrastructure_List();
        List<LocalTime> timeListHold = trainList.get(trainIndex).get_Time_List();

        System.out.println(infrListHold);
        System.out.println(timeListHold);

       // trainList.get(trainIndex).clear_Infrastructure_List();
       // trainList.get(trainIndex).clear_Time_List();

        System.out.println(trainList.get(trainIndex).get_Time_List());
        System.out.println(trainList.get(trainIndex).get_Infrastructure_List());

        System.out.println(holdCurrentIndex);
        System.out.println(infrListHold.size());
        System.out.println(timeListHold.size());
        infrListHold.subList(holdCurrentIndex+1,infrListHold.size()).clear();
        timeListHold.subList(holdCurrentIndex+1,timeListHold.size()).clear();

        trainList.get(trainIndex).set_Infrastructure_List(infrListHold);
        trainList.get(trainIndex).set_Time_List(timeListHold);

        trainList.get(trainIndex).add_Infrastructure(nextStop);
        trainList.get(trainIndex).add_Time(arrivalTime);

        System.out.println(trainList.get(trainIndex).get_Time_List());
        System.out.println(trainList.get(trainIndex).get_Infrastructure_List());

        calculate_Suggested_Speed(trainIndex);
        calculate_Authority(trainIndex);

        Network.tcsw_Interface.send_Speed_Authority(trainIndex, trainList.get(trainIndex).get_Suggest_Speed(),
               trainList.get(trainIndex).get_Authority());

    }

    public void import_Train_Schedule() throws FileNotFoundException{
        scheduleFile = new File(schedulePath);
        Scanner sc = new Scanner(scheduleFile);
        List<String> linesList = new ArrayList<>();
        sc.useDelimiter("\\r\\n");
        do {
            linesList.add(sc.next());
        }while(sc.hasNext());
        //System.out.println(linesList.get(0).substring(73,124));
        //System.out.println(linesList.get(0).length()); -1
        //find how many trains there are on the schedule and add them to list
        int numberOfTrains = 0 ;
        if(linesList.get(0).length() > 74){
            numberOfTrains = (linesList.get(0).length() - 74)/ 5;
        }

        //account for trains being created already
        numberOfTrains += trainList.size();

        for(int j = 0 + trainList.size(); j < numberOfTrains; j++){

            Train t = new Train("TR" + j);
            create_Train(t);
        }

        int lIndex =-1 ;
        for(String s: linesList){
            String[] h = s.split(",");

            ///////FOR ADDING TRAINS/////////////////////
            if(h.length > 30){
                //index 31 for first train
                int end = 31 + trainList.size();
                for(int i = 31; i < end; i++) {
                    String timeString = "0";//used for adjusting time to parse into LOCALTIME
                    if (h[i].contains(":")){

                        trainList.get(i-31).set_Current_Line(h[0]);
                        //System.out.println(h[0]);
                        trainList.get(i-31).set_Current_Block(Integer.parseInt(h[2]));
                        //
                        trainList.get(i-31).add_Infrastructure(h[6]);
                       // System.out.println(h[6]);
                        trainList.get(i-31).add_Time(LocalTime.parse(timeString.concat(h[i])));
                       // System.out.println(h[i]);
                    }
                }
            }

        }


    }

    public void import_Track_File() throws FileNotFoundException {
        trackFile = new File(trackPath);
        Scanner sc = new Scanner(trackFile);
        List<String> linesList = new ArrayList<>();

        sc.useDelimiter("\\r\\n");

        do {

            linesList.add(sc.next());

        }while(sc.hasNext());

        String currentLine;
        String previousLine = "";
        Line l = null;
        int lIndex =-1 ;
        for(String s: linesList){
            String[] h = s.split(",");

            currentLine = h[0];
            if(!currentLine.equals(previousLine)){
                lIndex++;
                l = new Line(h[0]);
                create_Line(l);
            }


            lineList.get(lIndex).add_Block(h[1].toCharArray()[0],Integer.parseInt(h[2]),Double.parseDouble(h[3]),
                    Double.parseDouble(h[4]),Integer.parseInt(h[5]),h[6],h[7],Double.parseDouble(h[8]),
                    Double.parseDouble(h[9]));



            previousLine = currentLine;

        }


    }

    private static int count_Occurences(String someString, char searchedChar, int index) {
        if (index >= someString.length()) {
            return 0;
        }

        int count = someString.charAt(index) == searchedChar ? 1 : 0;
        return count + count_Occurences(
                someString, searchedChar, index + 1);
    }

    public double determine_Distance(Integer trainIndex){
        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now

        for(int i  = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(trainList.get(trainIndex).get_Current_Line())){
                lineIndex = i;
            }
        }

        System.out.println("Determin_Distance HIT");
        System.out.println("lineIndex" + lineIndex);
        return lineList.get(lineIndex).get_Distance_Between(trainList.get(trainIndex).get_Current_Infrastructure(),
            trainList.get(trainIndex).get_Next_Infrastructure());


    }

    public int determine_Path_Distance(String start_Infrastructure, String stop_Infrastructure){
        int lineIndex = -1;

        //find current line
        for(int i = 0; i < lineList.size(); i++){
            for(int j = 0; j < lineList.get(i).get_Infrastructure_List().size(); j++) {
                if(lineList.get(i).get_Infrastructure_List().get(j).equals( start_Infrastructure)){
                    lineIndex = i;
                }
            }
        }
        if(lineIndex ==  -1){
            System.out.println("ERROR: DETERMINE DISTANCE LINEINDEX= 1-");
            return 0;
        }
        //set start block

        int distance = 0;

        return distance;

    }

    public boolean test_Switch(String line, int block) throws RemoteException {
        boolean working = false;
        if(Network.tcsw_Interface != null) {
            working = Network.tcsw_Interface.set_Switch_Manual(line, block);
        }
        return working;
    }

    public void close_Line(String line) throws RemoteException{
        //close entire track so each block is closed
        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now

        for(int i  = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(line)){
                lineIndex = i;
            }
        }
        lineList.get(lineIndex).close_Line();

    }

    public void close_Section(String line, char section) throws RemoteException{
        //close all blocks in section
    }

    public void close_Blocks(String line, char section, int startBlock, int endBlock) throws RemoteException{
        //close all blocks from start to stop
    }

    public void close_Block(String line, int block) throws RemoteException{
        //System.out.println("CLOSE BLOCK: " + line + ", " + block);
        for(int i = 0 ; i < lineList.size(); i++) {
            if(line == lineList.get(i).get_Line()){
                lineList.get(i).close_Block(block);
            }
        }
    }

    public void open_Line(String line) throws RemoteException{
        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now

        for(int i  = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(line)){
                lineIndex = i;
            }
        }
        lineList.get(lineIndex).open_Line();

    }

    public void open_Section(String line, char section) throws RemoteException{
        //open all blocks in section
    }

    public void open_Blocks(String line, char section, int startBlock, int endBlock) throws RemoteException{

    }//open all blocks from start to stop

    public void open_Block(String line, int block) throws RemoteException{
        //System.out.println("OPEN BLOCK: " + line + ", " + block);
        for(int i = 0 ; i < lineList.size(); i++) {
            if(line == lineList.get(i).get_Line()){
                lineList.get(i).open_Block(block);
            }
        }
    }


    //TODO make sure this calls calculate authority and sends authority, also updates block occupancy
    public void train_Moved(int trainNum, int block) throws RemoteException{
        System.out.println("TRAIN " + trainNum + " Moved to Block: " + block);

//        trainList.get(trainNum).set_Current_Block(block);
//        trainList.get(trainNum).moved_Block();
//
//        int lineIndex = 0;
//        for(int i = 0; i < lineList.size(); i++){
//            if(lineList.get(i).get_Line().equals(trainList.get(trainNum)
//                    .get_Current_Line())){
//                lineIndex = i;
//            }
//        }
//        if(block != 1) {
//            lineList.get(lineIndex).toggle_Block_Occupancy(block - 1);
//        }
//        lineList.get(lineIndex).toggle_Block_Occupancy(block);


    }


    public void add_Ticket_CTC(int trainNum) throws RemoteException{
        System.out.println("Ticket added to: " +trainNum);
        trainList.get(trainNum).add_Ticket();
    }

    public void update_Time(double time) throws RemoteException{
        //System.out.println(time);
       // System.out.printf("%.2f\n",time);

        time = (Math.round(time * 100.0) / 100.0);
        simTime = (long) time;

        double finalTime = time;
        double minutes = 0;
        minutes = finalTime /60;
        int trainIndex = 0;
        if((minutes % 1.0) == 0.0 && automatic) {



            for (Train tr : trainList) {
                for (LocalTime ti : tr.get_Time_List()) {
                    if (ti.toSecondOfDay() == finalTime) {

                        int finalTrainIndex = trainIndex;
                          //  System.out.println("Hit min");


                        Task task = new Task<Void>() {
                            @Override public Void call() {
                                try {
                                    dispatch(finalTrainIndex);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        };
                        new Thread(task).start();

                    }
                }
                trainIndex++;
            }
        }







    }



    public class Dispatch_Caller {


    }

}





