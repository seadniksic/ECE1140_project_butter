package Back_End;

import GUI.Controllers.driverController;
import GUI.Main_GUI;
import com.sun.tools.javac.Main;
import GUI.*;

import java.rmi.RemoteException;
import java.sql.SQLOutput;
import java.util.Date;


public class Train_Controller {
    private int Kp;
    private int Ki;
    private int number_Of_Cars;
    private int ID;
    private double manual_Speed;
    private boolean manual_Mode;
    private boolean brakes_On;
    private boolean e_Brake_On;
    private int authority;
    private double commanded_Speed;
    private double current_Speed;
    private Date setpointUpdate;
    private boolean loopStarted = false;
    private boolean announcements;
    private boolean advertisements;
    private boolean open_Doors_Left;
    private boolean open_Doors_Right;
    private boolean internal_Lights;
    private boolean external_Lights;
    private boolean failure_Bool;
    private String failure_String;
    private double current_Temperature;
    private double desired_Temperature;
    private boolean temp_Loop_Started = false;
    private String next_Stop;
    private double position;




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
        current_Speed = 0;
        setpointUpdate = java.util.Calendar.getInstance().getTime();
        current_Temperature = 68;
        next_Stop = "Next Stop";

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
        current_Speed = 0;
        setpointUpdate = java.util.Calendar.getInstance().getTime();
        current_Temperature = 68;
        next_Stop = "Next Stop";
    }

    public void set_Kp(int kp_Passed) {
        Kp = kp_Passed;
        //System.out.println("Kp: " + Kp);
    }

    public int get_Kp() {
        return Kp;
    }

    public void set_Ki(int ki_Passed) {
        Ki = ki_Passed;
        //System.out.println("Ki: " + Ki);
    }

    public int get_Ki() {
        return Ki;
    }

    public void set_Number_Of_Cars(int cars) {
        number_Of_Cars = cars;
        //System.out.println("cars: " + number_Of_Cars);
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
        manual_Speed = (double)speed / 2.237; // handles conversion from mph to m/s
        //System.out.println("manual speed: " + manual_Speed);
    }

    public double get_Manual_Speed() {
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


        //System.out.println("brakes: " + brakes_On);
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

        //System.out.println("ebrake: " + e_Brake_On);
    }

    public void set_Authority(int auth) {
        authority = auth;
        //System.out.println("Authority has been set");
        setpointUpdate = java.util.Calendar.getInstance().getTime();
    }

    public int get_Authority() {
        return authority;
    }

    public void set_Announcements(boolean b){
        // Sead needs an announcements function in his interface
        try {
            Network.tm_Interface.set_Announcements(ID, b);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean get_Announcements(){
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

    public boolean get_Open_Doors_Left(){
        return open_Doors_Left;
    }

    public boolean get_Open_Doors_Right(){
        return open_Doors_Right;
    }

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

    // modified set_Commanded_Speed
    public void set_Commanded_Speed(double sp) throws RemoteException, InterruptedException {
        commanded_Speed = sp/3.6; //converts from km/hr that the CTC sends to m/s which the power loop uses. Converted to mph on the front end
        setpointUpdate = java.util.Calendar.getInstance().getTime();

        if (loopStarted == false){
            Power_Loop object = new Power_Loop();
            object.start();
        }
    }

    public void set_Desired_Temperature(double des_Temp) {
        desired_Temperature = des_Temp;
        if(temp_Loop_Started != true) {
            Temp_Loop temp_loop = new Temp_Loop();
            temp_loop.start();
            temp_Loop_Started = true;
        }

    }

    public double get_Desired_Temperature(){
        return desired_Temperature;
    }

    public void set_Current_Temperature(double temp){
        current_Temperature = temp;
    }

    public double get_Current_Temperature(){
        return current_Temperature;
    }

    public void set_Failure_Bool(boolean b){
        failure_Bool = b;
    }

    public boolean get_Failure_Bool(){
        return failure_Bool;
    }

    public void set_Failure_Mode(String failure) {
        failure_String = failure;
    }

    public String get_Failure_Mode(){
        return failure_String;
    }

    public void set_Next_Stop(String s){
        next_Stop = s;
        System.out.println("when set next stop: " + next_Stop);
    }

    public String get_Next_Stop(){
        return next_Stop;
    }

    public void set_Position(double d){
        position = d;
    }

    public double get_Position(){
        return position;
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

    class Temp_Loop extends Thread{
        public void run(){
            try{
                set_Cabin_Temperature();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public double get_Commanded_Speed() {
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

    public double velocity_Check(double v1, double v2, double v3) throws RemoteException {
        //returns the value of 2/3 results
        if(v1 == v2){
            //System.out.println("V1 == v2");
            return v2;
        } else if(v2 == v3){
            //System.out.println("V2 == v3");
            return v2;
        } else if(v1 == v3){
            //System.out.println("V1 == V3");
            return v1;
        }else{
            //System.out.println("Made it to else statement of Velocity");
            //if none of these are true, poll again and re-call this function
            v1 = Network.tm_Interface.get_Velocity(ID);
            v2 = Network.tm_Interface.get_Velocity(ID);
            //TODO
            v3 = 0;
            return velocity_Check(v1, v2, v3);
        }
    }


    // need a function that deals with the manual mode power. Also need a handler function that decides whether to call this one or that one
    // TODO safety critical architecture
    public void calculate_Power_Command() throws RemoteException, InterruptedException {
        double previousError = 0;
        double integral = 0;
        double error = 0;
        double vCommand;
        double currV, software_Velocity, hardware_Velocity, software_V_2;
        int dt = 20;

        //putting loop started at the top to try to fix the error we are getting with sending 2 speend and authority in a row
        //loopStarted = true;

        software_Velocity = Network.tm_Interface.get_Velocity(ID);
        //System.out.println("Beginning software v: " + software_Velocity);
        //hardware simulation from actual train
        hardware_Velocity = Network.tm_Interface.get_Velocity(ID);
        //System.out.println("Beginning hardware v: " + hardware_Velocity);
        //TODO
        software_V_2 = 0;
        currV = velocity_Check(software_Velocity, hardware_Velocity, software_V_2);
        //System.out.println("Beginning returned v: " + currV);

        //check manual speed against commanded speed, verify it isnt over, then replace commanded speed with manual speed
        double output_Power = 0;

        while (true == true) {
            Date temp_Setpoint = setpointUpdate;
            if(authority < 1){
                set_Brakes_On(true);
            }else{
                set_Brakes_On(false);
            }
            // handles for breaking
            while((brakes_On == true || e_Brake_On == true || failure_Bool == true) && temp_Setpoint == setpointUpdate){
                software_Velocity = Network.tm_Interface.update_Speed(ID, 0);
                //System.out.println("software v in braking: " +software_Velocity);
                //simulates getting the velocity from the hardware on the actual train
                hardware_Velocity = Network.tm_Interface.update_Speed(ID,0);
                //System.out.println("hardware v in braking: " + hardware_Velocity);
                //TODO calculate position on your own
                software_V_2 = 0;
                currV = velocity_Check(software_Velocity, hardware_Velocity, software_V_2);
                //System.out.println("Returned v in braking: " + currV);
                // need to keep updating the current speed
                set_Current_Speed(currV);
                //Thread.sleep(dt);
            }

            //Handling for whether we are in manual mode or automatic mode
            if(manual_Mode == true && manual_Speed >0 && manual_Speed <= commanded_Speed){
                vCommand = (double) manual_Speed;
            } else {
                vCommand = (double) commanded_Speed;
            }

            //breaks when the current velocity is greater than the commanded velocity
            if(current_Speed > vCommand + 0.3){
                while(current_Speed > vCommand ){
                    set_Brakes_On(true);
                    //currV = Network.tm_Interface.update_Speed(ID, 20000);
                    software_Velocity = Network.tm_Interface.update_Speed(ID, 20000);
                    //System.out.println("software v in going too fast: " + software_Velocity);
                    //simulates getting the velocity from the hardware on the actual train
                    hardware_Velocity = Network.tm_Interface.update_Speed(ID,20000);
                    //System.out.println("Hardware v in going too fast: " + hardware_Velocity);
                    //TODO calculate position on your own
                    software_V_2 = 0;
                    currV = velocity_Check(software_Velocity, hardware_Velocity, software_V_2);
                    //System.out.println("Returned v in going too fast: "+ currV);
                    set_Current_Speed(currV);
                    //Thread.sleep(dt);
                }
                set_Brakes_On(false);
            }

            // loopStarted marks that there is an active loop running for this train controller
            loopStarted = true;
            error = vCommand - currV;

            //System.out.println(ID + " currV: " + currV + "\n" + "error: " + error + "\n");

            if(output_Power >= 120000) {
                // integral term does not update if the output power is greater than the max as per Project Slides
                integral = integral;
                //System.out.println("output power greater than max");
            } else{
                integral = integral + (error * (((double)dt) / 1000));
                //System.out.println("testing: " + (((double)dt) / 1000));
                //System.out.println("output power not greater than max");
            }
            //System.out.println("integral var: " + integral);

            output_Power = (Kp * error) + (Ki * integral);
            //System.out.println("power: " + output_Power);
            //limits the power to always being positive
            if (output_Power < 0) {
                output_Power = 0;
            }
            //limits the power output to 120 Kilowatts as per Datasheet
            else if (output_Power > 120000){
                //System.out.println("output before limited: " + output_Power);
                output_Power = 120000;
            }
            //System.out.println("power: " + output_Power);
            software_Velocity = Network.tm_Interface.update_Speed(ID, output_Power);
            //System.out.println("Software v in normal: "+ software_Velocity);
            //simulates getting the velocity from the hardware on the actual train
            hardware_Velocity = Network.tm_Interface.update_Speed(ID,output_Power);
            //System.out.println("Hardware v in normal: " + hardware_Velocity);
            //TODO calculate position on your own
            software_V_2 = 0;
            currV = velocity_Check(software_Velocity, hardware_Velocity, software_V_2);
            //System.out.println("Returned v in normal: " + currV);
            //currV = Network.tm_Interface.update_Speed(ID, output_Power);
            set_Current_Speed(currV);
            previousError = error;
            //Thread.sleep(dt);
            //System.out.println("Dt is: " + dt);

        }
    }

    public void set_E_Brake_From_TM(boolean b) {
        e_Brake_On = b;
    }


    public void set_Cabin_Temperature() throws InterruptedException, RemoteException {
        //default kp and ki for temperature
        double temp_Kp = 10;
        double temp_Ki = 1;

        //sets the cabin temperature VIA a PID loop
        while (true == true) {
            //loop for heating
            while (desired_Temperature > current_Temperature) {
                double temp_Temp = desired_Temperature - current_Temperature;
                double integral = 0;
                int dt = 1000;

                integral = integral + (temp_Temp * ((double) dt));
                double heating_Power_Out = (temp_Kp * temp_Temp) + (temp_Ki * integral);
                current_Temperature = Network.tm_Interface.update_Temperature(ID, heating_Power_Out, 0);
                Thread.sleep(dt);

            }
            //loop for cooling
            while (desired_Temperature < current_Temperature) {
                double temp_Temp = current_Temperature - desired_Temperature;
                double integral = 0;
                int dt = 1000;

                integral = integral + (temp_Temp * ((double) dt));
                double cooling_Power_Out = (temp_Kp * temp_Temp) + (temp_Ki * integral);
                current_Temperature = Network.tm_Interface.update_Temperature(ID, 0, cooling_Power_Out);
                Thread.sleep(dt);

            }

        }
    }
}
