package resources;


import javafx.application.Platform;
import javafx.concurrent.Task;
import networking.*;
import CTC_GUI.*;
import java.io.*;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.lang.Thread.sleep;

public class CTC_Back implements CTC_Interface {

    private String schedulePath ;
    static private File scheduleFile;
    private String trackPath ;
    static private File trackFile;
    static private List<Line> lineList = new ArrayList<>();
    static private List<Train> trainList = new ArrayList<>();
    static private boolean automatic = false; // should be set to false because gui starts in manual mode
    static private long simTime = 0;



//TODO check schedule viability ie can it move that length within the time needed give speed limits applied

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

    public void toggle_Automatic() throws RemoteException {
        automatic = !automatic;
    }

    public boolean get_Automatic(){ return automatic;}

    public long get_Sim_Time(){return simTime;}

    public void create_Train(Train t){
        trainList.add(t);
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
        System.out.println("CALC AUTH HIT:");

        //first find the index of the line the train is on.
        int lineIndex = -1;
        for(int i  = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(trainList.get(trainIndex).get_Current_Line())){
                lineIndex = i;
                break;
            }
        }

        //set start and end blocks
        int startBlock = trainList.get(trainIndex).get_Current_Infrastructure_Block()+1;
        int stopBlock = trainList.get(trainIndex).get_Next_Infrastructure_Block();


        //check track condition
        boolean condition = lineList.get(lineIndex).condition(startBlock,stopBlock);

        //check track occupancy
        boolean occupancy = lineList.get(lineIndex).occupancy(startBlock,stopBlock);

        //get authority
        int numberOfBlocks = lineList.get(lineIndex).get_Number_Of_Blocks_Between(startBlock,stopBlock);


        if(trainList.get(trainIndex).get_Authority() == 0 && condition && !occupancy){
            if(trainList.get(trainIndex).get_Current_Infrastructure().toLowerCase().contains("switch from yard")) {
                trainList.get(trainIndex).set_Authority(numberOfBlocks + 2);
            }else{
                trainList.get(trainIndex).set_Authority(numberOfBlocks);
           }
        }
    }

    public void calculate_Suggested_Speed(Integer trainIndex){
        System.out.println("SUG SPEED HIT:");
        Double distance = determine_Distance(trainIndex);
        System.out.println("Distance: " + distance);

        long scheduletime = trainList.get(trainIndex).get_Time_Between(trainList.get(trainIndex).get_Current_Infrastructure(),
                trainList.get(trainIndex).get_Next_Infrastructure());

        long time = scheduletime;

        System.out.println("TIME: " + time);

        double r = (distance * 6.0) / (time * 100.0);

        //TODO check against speed limits
        trainList.get(trainIndex).set_Suggest_Speed((double)r);
    }

    public void dispatch(Integer trainIndex) throws RemoteException, InterruptedException {
        System.out.println("hit dispatch automatic function ");
       // System.out.println(trainIndex);

        if(!trainList.get(trainIndex).get_Sent_Create_Command() && Network.tcsw_Interface != null){

            Network.tcsw_Interface.create_Train(
                    trainList.get(trainIndex).get_Number_Of_Cars(),
                    trainList.get(trainIndex).get_Current_Line(),
                    trainList.get(trainIndex).get_Current_Block());

            trainList.get(trainIndex).set_Sent_Create_Command(true);
        }
        calculate_Suggested_Speed(trainIndex);
        calculate_Authority(trainIndex);
        System.out.println("suggested speed :" + trainList.get(trainIndex).get_Suggest_Speed());
        System.out.println("authority send: " + trainList.get(trainIndex).get_Authority());

        if( Network.tcsw_Interface != null)
        Network.tcsw_Interface.send_Speed_Authority(trainIndex, trainList.get(trainIndex).get_Suggest_Speed(),
          trainList.get(trainIndex).get_Authority());
    }

    public void dispatch(Integer trainIndex, String nextStop, LocalTime arrivalTime) throws RemoteException, InterruptedException {
        System.out.println("hit dispatch manual function");

        if(!trainList.get(trainIndex).get_Sent_Create_Command() && Network.tcsw_Interface != null){
            Network.tcsw_Interface.create_Train(
                    trainList.get(trainIndex).get_Number_Of_Cars(),
                    trainList.get(trainIndex).get_Current_Line(),
                    trainList.get(trainIndex).get_Current_Block());

            System.out.println(trainList.get(trainIndex).get_Current_Block());
            trainList.get(trainIndex).set_Sent_Create_Command(true);
        }

        Double distance = determine_Distance(trainIndex);
        System.out.println("Distance: " + distance);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime first = LocalTime.parse("00:00",formatter);

        long time = MINUTES.between(first, arrivalTime);;

        System.out.println("TIME: " + time);

        double r = (distance * 6.0) / (time * 100.0);


        trainList.get(trainIndex).set_Suggest_Speed((double)r);
        calculate_Authority(trainIndex);

        System.out.println("suggested speed :" + trainList.get(trainIndex).get_Suggest_Speed());
        System.out.println("authority send: " + trainList.get(trainIndex).get_Authority());

        if(Network.tcsw_Interface != null)
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

        //find how many trains there are on the schedule and add them to list
        //account for trains being created already
        int numberOfTrains = linesList.get(0).length() - 2;

        for(int j = 0; j < numberOfTrains; j++){

            Train t = new Train("TR" + j);
            create_Train(t);
            //System.out.println("Created TR" + j);
        }

        //add times to train
        int lIndex =-1 ;
        for(String s: linesList){
            String[] h = s.split(",");

            ///////FOR ADDING TRAINS/////////////////////
            if(h.length >3){
                System.out.println(h[0] + h[1]+h[2]);
                int end = 3 + trainList.size();
                for(int i = 3; i < end; i++) {

                    String timeString = "0";//used for adjusting time to parse into LOCALTIME
                    if(i< h.length)
                    if (h[i].contains(":")) {
                        trainList.get(i - 3).set_Current_Line(h[0]);
                        //System.out.println(h[0]);

                        //trainList.get(i - 3).set_Current_Block(0);
                        trainList.get(i-3).add_Block(Integer.parseInt(h[1]));
                        trainList.get(i-3).add_Infrastructure(h[2]);
                       // System.out.println(h[2]);
                        trainList.get(i-3).add_Time(LocalTime.parse(timeString.concat(h[i])));
                      //  System.out.println(h[i]);
                    }
                }
            }
        }

        for(int i = 0; i < trainList.size(); i++){
            trainList.get(i).sort_Lists();
        }

       // System.out.println(trainList.get(0).get_Infrastructure_List());
       // System.out.println(trainList.get(0).get_Time_List());

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
            lineList.get(0).create_Graph();

    }

    private double determine_Distance(Integer trainIndex){
        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now

        for(int i  = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(trainList.get(trainIndex).get_Current_Line())){
                lineIndex = i;
            }
        }

        System.out.println("Determin_Distance HIT ");
       // System.out.println("lineIndex" + lineIndex);
        return lineList.get(lineIndex).get_Distance_Between(trainList.get(trainIndex).get_Current_Infrastructure_Block(),
            trainList.get(trainIndex).get_Next_Infrastructure_Block());


    }

    public boolean test_Switch(String line, int block) throws RemoteException {
        boolean working = false;
        if(Network.tcsw_Interface != null) {
            working = Network.tcsw_Interface.set_Switch_Manual(line, block);
        }
        return working;
    }

    public LocalTime get_SimTime_As_LocalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        long currentSimTime = simTime;

        long hours = TimeUnit.SECONDS.toHours(simTime);
        currentSimTime -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(simTime);
        currentSimTime -= TimeUnit.MINUTES.toSeconds(minutes);

        String HH;
        if(hours <10) {
            HH = "0" + String.valueOf(hours);
        }else{
            HH = String.valueOf(hours);
        }

        String mm;
        if(minutes <10) {
            mm = "0" + String.valueOf(minutes);
        }else{
            mm = String.valueOf(minutes);
        }

        String ss;
        if(currentSimTime <10) {
            ss = "0" + String.valueOf(currentSimTime);
        }else{
            ss = String.valueOf(currentSimTime);
        }

        return LocalTime.parse(HH + ":" + mm + ":" + ss, formatter);

    }

    public void close_Line(String line) throws RemoteException{
        //close entire track so each block is closed
        int lineIndex = -1;
        boolean open = true;
        //First check if path is open
        //assume it is open for now

        for(int i  = 0; i < lineList.size(); i++){
            if(line.toLowerCase().equals( lineList.get(i).get_Line().toLowerCase())){
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
            if(line.toLowerCase().equals( lineList.get(i).get_Line().toLowerCase())){
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
            if(line.toLowerCase().equals( lineList.get(i).get_Line().toLowerCase())){
                lineList.get(i).open_Block(block);
            }
        }
    }

    public void train_Moved(int trainNum, int block) throws RemoteException, InterruptedException
    {
        System.out.println("==============================================================================");
        System.out.println("TRAIN " + trainNum + " Moved to Block: " + block);
        //have train be on block
        trainList.get(trainNum).set_Current_Block(block);

        int lineIndex = 0;
        for(int i = 0; i < lineList.size(); i++){
            if(lineList.get(i).get_Line().equals(trainList.get(trainNum).get_Current_Line())){
                lineIndex = i;
            }
        }

        //TODO this needs to changed based of actual occupancy ie length of train and block: each car length is 32 meters so do some math

        if(block > 1) {
            lineList.get(lineIndex).toggle_Block_Occupancy(block - 2);
            //this leaves 2 block spacer for trains
        }

        //occupy next block
        lineList.get(lineIndex).toggle_Block_Occupancy(block);

        //set start and end blocks
        int startBlock = block;
        int stopBlock = trainList.get(trainNum).get_Next_Infrastructure_Block();


        //check track condition
        boolean condition = lineList.get(lineIndex).condition(startBlock,stopBlock);

        //check track occupancy
        boolean occupancy = lineList.get(lineIndex).occupancy(startBlock,stopBlock);

        if(condition && !occupancy) {
            trainList.get(trainNum).train_Moved();
        }else{
            trainList.get(trainNum).set_Authority(0);
            //trainList.get(trainNum).set_Current_Index();
        }


        if(trainList.get(trainNum).get_Authority() == 0 && automatic ){
            trainList.get(trainNum).arrived();

            Task task = new Task<Void>() {
                @Override public Void call() throws InterruptedException, RemoteException {
                    long hold = simTime + 20; //~10 seconds wait
                    while(simTime < hold ){
                        Thread.sleep(1);
                    }//do nothing
                    dispatch(trainNum);
                    return null;
                }
            };
            new Thread(task).start();
        }else if(trainList.get(trainNum).get_Authority() == 0 && !automatic ){
            trainList.get(trainNum).arrived();
            return;
        }else if(trainList.get(trainNum).get_Authority() > 0){
            dispatch(trainNum);
        }
    }

    public void add_Ticket_CTC(int trainNum) throws RemoteException{
        System.out.println("Ticket added to: " +trainNum);
        trainList.get(trainNum).add_Ticket();
    }

    public void add_Tickets(int trainNum, int numTickets) throws RemoteException{
        trainList.get(trainNum).add_Ticket(numTickets);
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


        new Thread(new Runnable() {
            @Override public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Main.update_GUI_Time();
                    }
                });
            }
        }).start();



        if((minutes % 1.0) == 0.0 && automatic) {

            for (Train tr : trainList) {
                LocalTime ti = tr.get_Time_List().get(0);//this should only see first time for time list
                    if (ti.toSecondOfDay() == finalTime) {

                        int finalTrainIndex = trainIndex;
                          //  System.out.println("Hit min");


                        Task task = new Task<Void>() {
                            @Override public Void call() {
                                try {
                                   // Network.tcsw_Interface.create_Train(tr.get_Number_Of_Cars(),tr.get_Current_Line(),tr.get_Current_Block());
                                    dispatch(finalTrainIndex);
                                } catch (RemoteException | InterruptedException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        };
                        new Thread(task).start();

                    }

                trainIndex++;
            }
        }







    }

    public void change_Lights(String line, int block, boolean state){
        for(int i = 0 ; i < lineList.size(); i++) {
            if(line.toLowerCase().equals( lineList.get(i).get_Line().toLowerCase())){
                lineList.get(i).set_Lights(block,state);
            }
        }
    }

    public void change_CrossBar(String line, int block, boolean state){
        for(int i = 0 ; i < lineList.size(); i++) {
            if(line.toLowerCase().equals( lineList.get(i).get_Line().toLowerCase())){
                System.out.println("BLOCK: " + block + "State: " + state);
                lineList.get(i).set_Crossbar(block,state);
            }
        }

    }


    //TODO somehow know if a train isn't moving-> exit automatic mode-> resend speed and authority
}





