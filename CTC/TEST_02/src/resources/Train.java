package resources;


import java.rmi.RemoteException;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MINUTES;

import networking.Network;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Train {

    private String name;
    private Integer numberOfCars;
    private Double suggestSpeed;
    private Double avgSpeed;
    private Integer authority;
    private List<Integer> infrastructureBlockList = new ArrayList<>();
    private List<String> infrastructureList = new ArrayList<>();
    private List<LocalTime> timeList = new ArrayList<>();
    private int currentIndex;
    private LocalTime timeStarted;
    private Long ticketsPerHour;
    private Integer numberOfTickets;
    private String currentLine;
    private Character currentSection;
    private Integer currentBlock;
    private boolean sentCreateCommand;

    public Train(String n){
        name = n;
        numberOfCars = 1;
        suggestSpeed = (double) 0;
        avgSpeed =(double) 0;
        authority = 0;
        numberOfTickets = 0;
        currentIndex = 0;
        sentCreateCommand = false;
        currentBlock = 0;

        set_Number_Of_Cars();
    }

    private Train(String n, Integer numOfCars){
        name = n;
        numberOfCars = numOfCars;
        suggestSpeed = (double) 0;
        avgSpeed =(double) 0;
        authority = 0;
        numberOfTickets = 0;
        currentIndex = 0;

        set_Number_Of_Cars();
    }

    private Train(String n,Integer numOfCars, Double sugSpeed, Double aSpeed, Integer auth) {
        name = n;
        numberOfCars = numOfCars;
        suggestSpeed = sugSpeed;
        avgSpeed = aSpeed;
        authority = auth;
        numberOfTickets = 0;
        currentIndex = 0;

        set_Number_Of_Cars();
    }

    private Train(String n, Integer numOfCars, Double sugSpeed, Double aSpeed, Integer auth, String startingBlock){
        name = n;
        numberOfCars = numOfCars;
        suggestSpeed = sugSpeed;
        avgSpeed = aSpeed;
        authority = auth;
        numberOfTickets = 0;
        currentIndex = 0;

    }

    private Train(String n, Integer numOfCars, Double sugSpeed, Double aSpeed, Integer auth, List<String> infList,
                  List<LocalTime> tList){
        name = n;
        numberOfCars = numOfCars;
        suggestSpeed = sugSpeed;
        avgSpeed = aSpeed;
        authority = auth;
        ticketsPerHour = (long) 0.0;
        infrastructureList = infList;
        numberOfTickets = 0;
        currentIndex = 0;

    }


    public boolean get_Sent_Create_Command(){return sentCreateCommand;}

    public void set_Sent_Create_Command(boolean f){ sentCreateCommand = f;}

    public String get_Name() { return name; }

    public Integer get_Number_Of_Cars(){ return numberOfCars; }

    public Double get_Suggest_Speed() { return suggestSpeed; }

    public Double get_Suggest_Speed_GUI(){return suggestSpeed *.621371;}

    public Double get_Avg_Speed() { return avgSpeed; }

    public Integer get_Authority(){ return authority; }

    public LocalTime get_Arrival_Time_Of_Next_INFR(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime second = LocalTime.parse("00:00",formatter);
        for(int i = 0; i < infrastructureList.size(); i ++){

            if(infrastructureList.get(i).contains(get_Next_Infrastructure())){
                second = timeList.get(i);
            }else if( second.compareTo(LocalTime.parse("00:00",formatter)) != 0){
                break;
            }
        }

        return second;
    }

    public long get_Time_Between(String start, String end){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        long timeInMinutes = 0;
        LocalTime first = LocalTime.parse("00:00",formatter);
        LocalTime second = LocalTime.parse("00:00",formatter);
        System.out.println("WHY IS THIS START? ->> " + start);
        System.out.println("WHY IS THIS END? ->> " + end);
       // System.out.println(infrastructureList);
       // System.out.println(timeList);
        for(int i = currentIndex; i < infrastructureList.size(); i ++){
            if(infrastructureList.get(i).contains(start)){
                first = timeList.get(i);
            }else if(infrastructureList.get(i).contains(end)){
                second = timeList.get(i);
            }else if( second.compareTo(LocalTime.parse("00:00",formatter)) != 0){
                break;
            }
        }
        timeInMinutes = MINUTES.between(first, second);

        return timeInMinutes;
    }

    public int get_Current_Infrastructure_Block(){ return infrastructureBlockList.get(currentIndex);}

    public int get_Next_Infrastructure_Block() {
        return  infrastructureBlockList.get(currentIndex + 1);
    }

    public List<Integer> get_Infrastructure_Block_List(){ return infrastructureBlockList; }

    public String get_Current_Infrastructure() { return infrastructureList.get(currentIndex);}

    public String get_Next_Infrastructure() {
        return  infrastructureList.get(currentIndex + 1);
    }

    public List<String> get_Infrastructure_List(){ return infrastructureList;}

    public List<LocalTime> get_Time_List() { return timeList;}

    public long get_Tickets_Per_Hour() {
        long ticketsPerHour = 0;

        long simTime = 1;
        if(Network.server_Object != null);
        simTime =Network.server_Object.get_Sim_Time();

        //convert seconds to hours
        simTime *= 0.000277778;
        if(simTime > 0)
        ticketsPerHour = numberOfTickets / simTime;
        return ticketsPerHour;
    }

    public String get_Current_Line(){return currentLine;}

    public Character get_Current_Section() { return currentSection;}

    public Integer get_Current_Block() {return currentBlock;}

    public Integer get_Current_Index() {return currentIndex;}

    public void set_Name(String n) { name = n ; }

    public void set_Number_Of_Cars(){
        Random numOfCar = new Random();
        numberOfCars = numOfCar.nextInt(4) + 1;
    }

    public void set_Suggest_Speed(Double sugSpeed) { suggestSpeed = sugSpeed;}

    public void set_Authority(Integer auth){
        System.out.println("Authority Setting To : " + auth);
        authority = auth; }

    public void set_Tickets_Per_Hour(Long tickets) { ticketsPerHour = tickets; }

    public void set_Current_Line(String line){ currentLine = line; }

    public void set_Current_Section(Character sec){ currentSection = sec;}

    public void set_Current_Block(Integer b){ currentBlock = b;}


    public void set_Infrastructure_Block_List(List<Integer> block){
        infrastructureBlockList = block;
    }

    public void set_Infrastructure_List(List<String> inf){ infrastructureList = inf; }

    public void set_Time_List(List<LocalTime> times){ timeList = times; }

    public void add_Block(int b){
        infrastructureBlockList.add(b);}

    public void add_Infrastructure(String s){
        infrastructureList.add(s);
    }

    public void add_Time(LocalTime t){
        timeList.add(t);
    }

    public void add_Ticket(){
        numberOfTickets ++;
    }

    public void sort_Lists(){
        int n = timeList.size();
        for (int i = 0; i < n; i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < n; j++) {
                if (timeList.get(j).compareTo(timeList.get(pos)) < 0) {
                    pos = j;
                }
            }
            // Swap min (smallest num) to current position on array
            LocalTime minTime = timeList.get(pos);
            String minInfrastructure = infrastructureList.get(pos);
            int minBlock = infrastructureBlockList.get(pos);

            timeList.set(pos, timeList.get(i));
            infrastructureList.set(pos,infrastructureList.get(i));
            infrastructureBlockList.set(pos, infrastructureBlockList.get(i));

            timeList.set(i, minTime);
            infrastructureList.set(i,minInfrastructure);
            infrastructureBlockList.set(i,minBlock);
        }



    }

    public void add_Ticket(Integer add){
        numberOfTickets += add;
    }


    public void train_Moved(){
        System.out.println("Authority--");
        authority--;
    }


    public void arrived() throws RemoteException, InterruptedException {
        if(Network.tcsw_Interface != null)
        Network.tcsw_Interface.send_Speed_Authority(Integer.valueOf(name.substring(2)),suggestSpeed,0);

        System.out.println("TRAIN ARRIVED AT :" + get_Next_Infrastructure());
        currentIndex ++;
    }

    public void set_Current_Index(int i){ currentIndex = i;
        System.out.println("INF: " + get_Current_Infrastructure() + " -> " + get_Next_Infrastructure());}

    public void clear_Infrastructure_List(){
        infrastructureList.clear();
    }

    public void clear_Time_List(){
        timeList.clear();
    }

}
