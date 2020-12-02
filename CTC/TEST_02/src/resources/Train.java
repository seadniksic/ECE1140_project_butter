package resources;


import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Train {

    private String name;
    private Integer numberOfCars;
    private Double suggestSpeed;
    private Double avgSpeed;
    private Integer authority;
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
        currentBlock = 62;

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

    public Double get_Avg_Speed() { return avgSpeed; }

    public Integer get_Authority(){ return authority; }


    public long get_Time_Between(String start, String end){
        long timeInMinutes = 0;
        LocalTime first = LocalTime.parse("00:00");
        LocalTime second = LocalTime.parse("00:00");
        for(int i = 0; i < infrastructureList.size(); i ++){
            if(infrastructureList.get(i).equals(start)){
               // System.out.println("HIT FIRST STRING");
                first = timeList.get(i);
            }else if(infrastructureList.get(i).equals(end)){
                second = timeList.get(i);
            }
        }
        timeInMinutes = MINUTES.between(first,second);

        return timeInMinutes - 1 ;//MINUS ONE ACCOUNTS FOR SPENDING 1 MINUTE AT EACH STATION
    }

    public String get_Current_Infrastructure() { return infrastructureList.get(currentIndex);}

    public String get_Next_Infrastructure() {
        return  infrastructureList.get(currentIndex + 1);
    }

    public List<String> get_Infrastructure_List(){ return infrastructureList;}

    public List<LocalTime> get_Time_List() { return timeList;}

    public long get_Tickets_Per_Hour() {
        long ticketsPerHour = 0;
        ticketsPerHour = numberOfTickets / 1;
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

    public void set_Suggest_Speed(Double sugSpeed) { suggestSpeed = sugSpeed; }



    public void set_Authority(Integer auth){ authority = auth; }

    public void set_Tickets_Per_Hour(Long tickets) { ticketsPerHour = tickets; }

    public void set_Current_Line(String line){ currentLine = line; }

    public void set_Current_Section(Character sec){ currentSection = sec;}

    public void set_Current_Block(Integer b){ currentBlock = b;}

    // public void setCurrentPosition(String currentPos){ currentBlock = currentPos; }


    public void set_Infrastructure_List(List<String> inf){ infrastructureList = inf; }

    public void set_Time_List(List<LocalTime> times){ timeList = times; }

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
            timeList.set(pos, timeList.get(i));
            infrastructureList.set(pos,infrastructureList.get(i));
            timeList.set(i, minTime);
            infrastructureList.set(i,minInfrastructure);
        }



    }

    public void add_Ticket(Integer add){
        numberOfTickets += add;
    }



    //TODO make sure arrived() is getting called. adjust train.arrived for index out of range error
    public void arrived(){
        currentIndex ++;
    }


    public void clear_Infrastructure_List(){
        infrastructureList.clear();
    }

    public void clear_Time_List(){
        timeList.clear();
    }

}
