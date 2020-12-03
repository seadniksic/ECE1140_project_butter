package Back_End;

import GUI.Main_GUI;

import java.rmi.RemoteException;
import java.sql.SQLOutput;
import java.util.Date;


public class Train_Controller {
    private int Kp;
    private int Ki;
    private int number_Of_Cars;
    private int ID;
    private int manual_Speed;
    private boolean manual_Mode;
    private boolean brakes_On;
    private boolean e_Brake_On;
    private int authority;
    private int commanded_Speed;
    private double current_Speed;
    private Date setpointUpdate;
    private Boolean loopStarted = false;
    private String announcements;
    private boolean advertisements;
    private boolean open_Doors_Left;
    private boolean open_Doors_Right;
    private boolean internal_Lights;
    private boolean external_Lights;
    // need to implement the temperature control




    public Train_Controller() {
        Kp = 0;
        Ki = 0;
        number_Of_Cars = 0;
        ID = 0;
        manual_Speed = 0;
        manual_Mode = false;
        brakes_On = false;
        e_Brake_On = false;
        authority = 0;
        commanded_Speed = 0;
        setpointUpdate = java.util.Calendar.getInstance().getTime();

    }

    public Train_Controller(int cars, int id_Num) {
        Kp = 0;
        Ki = 0;
        number_Of_Cars = cars;
        ID = id_Num;
        manual_Speed = 0;
        manual_Mode = false;
        brakes_On = false;
        e_Brake_On = false;
        authority = 0;
        commanded_Speed = 0;
        setpointUpdate = java.util.Calendar.getInstance().getTime();
    }

    public void set_Kp(int kp_Passed) {
        Kp = kp_Passed;
        System.out.println("Kp: " + Kp);
    }

    public int get_Kp() {
        return Kp;
    }

    public void set_Ki(int ki_Passed) {
        Ki = ki_Passed;
        System.out.println("Ki: " + Ki);
    }

    public int get_Ki() {
        return Ki;
    }

    public void set_Number_Of_Cars(int cars) {
        number_Of_Cars = cars;
        System.out.println("cars: " + number_Of_Cars);
    }

    public int get_Number_Of_Cars() {
        return number_Of_Cars;
    }

    public void set_ID(int id) {
        ID = id;
    }

    public int get_ID() {
        return ID;
    }

    public void set_Manual_Speed(int speed) {
        manual_Speed = speed;
        System.out.println("manual speed: " + manual_Speed);
    }

    public int get_Manual_Speed() {
        return manual_Speed;
    }

    public void set_Manual_Mode(boolean b){
        manual_Mode = b;
    }

    public boolean get_Manual_Mode(){
        return manual_Mode;
    }

    public boolean get_Brakes_On() {
        return brakes_On;
    }

    public void set_Brakes_On(boolean brakes) {
        brakes_On = brakes;
        // send to Sead
        try{
            // this is throwing an error
            Network.tm_Interface.set_Brake_Status(ID, brakes);
        } catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("brakes: " + brakes_On);
    }

    public boolean get_E_Brake_On() {
        return e_Brake_On;
    }

    public void set_E_Brake_On(boolean e_Brake) {
        e_Brake_On = e_Brake;
        // send to Sead
        try{
            Network.tm_Interface.set_Emergency_Brake_Status(ID, e_Brake);
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("ebrake: " + e_Brake_On);
    }

    public void set_Authority(int auth) {
        authority = auth;
        System.out.println("Authority has been set");
        setpointUpdate = java.util.Calendar.getInstance().getTime();
    }

    public int get_Authority() {
        return authority;
    }

    public void set_Announcements(String s){
        // Sead needs an announcements function in his interface
        try {
            Network.tm_Interface.set_Announcements(ID, s);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String get_Announcements(){
        return announcements;
    }

    public void set_Advertisements(boolean ads){
        try {
            Network.tm_Interface.set_Advertisements(ID, ads);
        } catch (Exception e){
            e.printStackTrace();
        }
        advertisements = ads;
    }

    public boolean get_Advertisements(){
        return advertisements;
    }

    //public void set_Left_Door_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    //public void set_Right_Door_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Open_Doors_Left(boolean doors) {
        // doors should not open if the train is moving
        if (current_Speed == 0) {
            try {
                Network.tm_Interface.set_Left_Door_Status(ID, doors); // opens doors VIA Train Model
            } catch (Exception e) {
                e.printStackTrace();
            }
            open_Doors_Left = doors; // sets state in the Train Controller module
        }
    }

    public void set_Open_Doors_Right(boolean doors) {
        // doors should not open if the train is moving
        if (current_Speed == 0) {
            try {
                Network.tm_Interface.set_Right_Door_Status(ID, doors); // opens doors VIA Train Model
            } catch (Exception e) {
                e.printStackTrace();
            }
            open_Doors_Right = doors; // sets state in the Train Controller module
        }
    }

    // need getters after splitting into right and left
    /*public boolean get_Open_Doors(){
        return open_Doors;
    }*/

    public void set_Internal_Lights(boolean intLight){
        try {
            Network.tm_Interface.set_Int_Lights(ID, intLight);
        } catch (Exception e){
            e.printStackTrace();
        }
        internal_Lights = intLight;
    }

    public boolean get_Internal_Lights(){
        return internal_Lights;
    }

    public void set_External_Lights(boolean extLight){
        try {
            Network.tm_Interface.set_Ext_Lights(ID, extLight);
        } catch (Exception e){
            e.printStackTrace();
        }
        external_Lights = extLight;
    }

    /*public void set_Commanded_Speed(int sp) throws RemoteException, InterruptedException {

        commanded_Speed = sp;
        setpointUpdate = java.util.Calendar.getInstance().getTime();
        //check the kp and ki values before starting the loop
        //launch train engineer ui when kp and ki are both 0, then return to the power command
        if(loopStarted == false){ calculate_Power_Command(); }

    }*/
    // modified set_Commanded_Speed
    public void set_Commanded_Speed(int sp) throws RemoteException, InterruptedException {
        commanded_Speed = sp;
        setpointUpdate = java.util.Calendar.getInstance().getTime();

        if (loopStarted == false){
            Power_Loop object = new Power_Loop();
            object.start();
        }
    }
    // Power_Loop for subthread
    class Power_Loop extends Thread {
        public void run() {
            try {
                // Displaying the thread that is running
                calculate_Power_Command();

            } catch (Exception e) {
                // Throwing an exception
                e.printStackTrace();
            }
        }

    }

    public int get_Commanded_Speed() {
        return commanded_Speed;
    }

    public void set_Current_Speed(double d){

        current_Speed = d;
    }

    public double get_Current_Speed(){
        return current_Speed;
    }

    public String get_Setpoint_Update(){
        return setpointUpdate.toString();
    }

    /*public double power_Iteration_2(double vSead) {
        double speed_Dub = commanded_Speed;
        double vError = (double) ((0.277778 * speed_Dub) - vSead); // converstion factor to calculate from km/hr to m/s
        double power = vError * Kp;
        return power;
    }*/

    // need a function that deals with the manual mode power. Also need a handler function that decides whether to call this one or that one
    public void calculate_Power_Command() throws RemoteException, InterruptedException {
        double previousError = 0;
        double integral = 0;
        double error = 0;
        double vCommand;
        double currV;
        int dt = 100;


        currV = Network.tm_Interface.get_Velocity(ID);
        //vCommand = (double) commanded_Speed; // what is was before modfication

        //check manual speed against commanded speed, verify it isnt over, then replace commanded speed with manual speed
        double output_Power = 0;

        while (true == true) {
            // handles for breaking
            while(brakes_On == true || e_Brake_On == true){
                currV = Network.tm_Interface.update_Speed(ID, 0);
                System.out.println("curr v: " + currV);
                // need to keep updating the current speed
                set_Current_Speed(currV);
                Thread.sleep(dt);
            }

            //Handling for whether we are in manual mode or automatic mode
            if(manual_Mode == true && manual_Speed >0 && manual_Speed <= commanded_Speed){
                vCommand = (double) manual_Speed;
            } else {
                vCommand = (double) commanded_Speed;
            }

            //breaks when the current velocity is greater than the commanded velocity
            if(current_Speed > vCommand + 0.3){
                while(current_Speed > vCommand){
                    set_Brakes_On(true);
                    currV = Network.tm_Interface.update_Speed(ID, 0);
                    set_Current_Speed(currV);
                    Thread.sleep(dt);
                }
                set_Brakes_On(false);
            }

            // loopStarted marks that there is an active loop running for this train controller
            loopStarted = true;
            error = vCommand - currV;

            System.out.println(ID + " currV: " + currV + "\n" + "error: " + error + "\n");

            if(output_Power >= 120000) {
                // integral term does not update if the output power is greater than the max as per Project Slides
                integral = integral;
                System.out.println("output power greater than max");
            } else{
                integral = integral + (error * (((double)dt) / 1000));
                System.out.println("testing: " + (((double)dt) / 1000));
                System.out.println("output power not greater than max");
            }
            System.out.println("integral var: " + integral);

            output_Power = (Kp * error) + (Ki * integral);
            System.out.println("power: " + output_Power);
            //limits the power to always being positive
            if (output_Power < 0) {
                output_Power = 0;
            }
            //limits the power output to 120 Kilowatts as per Datasheet
            else if (output_Power > 120000){
                System.out.println("output before limited: " + output_Power);
                output_Power = 120000;
            }
            System.out.println("power: " + output_Power);
            currV = Network.tm_Interface.update_Speed(ID, output_Power);
            set_Current_Speed(currV);
            previousError = error;
            Thread.sleep(dt);
            System.out.println("Dt is: " + dt);

        }
    }

    public void set_E_Brake_From_TM(boolean b) {
        e_Brake_On = b;
    }
}
