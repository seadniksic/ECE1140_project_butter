package resources;

public class Block {
    private char section;
    private int number;
    private double length;
    private double grade;
    private int speedLimit;
    private String infrastructure;
    private String stationSide;
    private  double elevation;
    private  double cumulativeElevation;
    private boolean occupancy;//true is occupied & false is not occupied
    private boolean condition;//true is open & false is closed
    private boolean lights ;//true is green & false is red
    private boolean crossbar ;//true is down & false is up

    public Block(){
        section = ' ';
        number = 0;
        length = 0;
        grade = 0;
        speedLimit = 0;
        infrastructure = null;
        elevation = 0;
        cumulativeElevation = 0;
        occupancy = false;
        condition = true;
    }


    public Block(char sec, int num, double len, double gra, int speed, String inf, String sSide, double ele,double cumEle){
        section = sec;
        number = num;
        length = len;
        grade = gra;
        speedLimit = speed;
        infrastructure = inf;
        elevation = ele;
        cumulativeElevation = cumEle;
        stationSide = sSide;
        occupancy = false;
        condition = true;
        lights = true;
        crossbar = false;
    }


    public Character get_Section(){
        return section;
    }

    public Integer get_Number(){
        return number;
    }

    public Double get_Length(){
        return length;
    }

    public Double get_Grade() {
        return grade;
    }

    public Integer get_Speed_Limit() {
        return speedLimit;
    }

    public String get_Infrastructure(){
        return infrastructure;
    }

    public Double get_Elevation() {
        return elevation;
    }


    public Double get_Cumulative_Elevation() {
        return cumulativeElevation;
    }

    public boolean get_Occupancy(){return occupancy;}

    public void set_Occupancy(boolean b){occupancy = b;}

    public boolean get_Condition(){return condition;}

    public void set_Condition(boolean b){condition = b;}


    public String get_Block_ID(){
        return section + String.valueOf(number);
    }

    public String get_stationSide(){return stationSide;}

    public boolean get_Lights(){return lights;}

    public void set_Lights(boolean b){ lights = b;}

    public boolean get_Crossbar(){return crossbar;}

    public void set_Crossbar(boolean b){crossbar = b;}



}
