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

    public void set_Commanded_Speed(int sp) throws RemoteException, InterruptedException {

        commanded_Speed = sp;
        setpointUpdate = java.util.Calendar.getInstance().getTime();
        //check the kp and ki values before starting the loop
        //launch train engineer ui when kp and ki are both 0, then return to the power command
        if(loopStarted == false){ calculate_Power_Command(); }

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

    public double power_Iteration_2(double vSead) {
        double speed_Dub = commanded_Speed;
        double vError = (double) ((0.277778 * speed_Dub) - vSead); // converstion factor to calculate from km/hr to m/s
        double power = vError * Kp;
        return power;
    }

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

        while (true == true) {
            // handles for breaking
            while(brakes_On == true || e_Brake_On == true){
                currV = Network.tm_Interface.update_Speed(ID, 0);
                Thread.sleep(dt);
            }
            //Handling for whether we are in manual mode or automatic mode
            if(manual_Mode == true && manual_Speed >0 && manual_Speed <= commanded_Speed){
                vCommand = (double) manual_Speed;
            } else {
                vCommand = (double) commanded_Speed;
            }
            loopStarted = true;
            //vCommand = (double) commanded_Speed;
            error = vCommand - currV;
            //if (error >= 0) {
                System.out.println(ID + " currV: " + currV + "\n" + "error: " + error + "\n");
                integral = integral + (error * (dt/1000));
                double output_Power = (Kp * error) + (Ki * integral);
                if (output_Power < 0) {
                    output_Power = 0;
                }
                else if (output_Power > 10000){
                    output_Power = 10000;
                }
                System.out.println("power: " + output_Power);
                currV = Network.tm_Interface.update_Speed(ID, output_Power);
                set_Current_Speed(currV);
                previousError = error;
                Thread.sleep(dt);
                System.out.println("Dt is: " + dt);
            /*}
            else {
                currV = Network.tm_Interface.update_Speed(ID,0);
                System.out.println("currv in negative: " + currV);
            }*/

        }
    }

    public void set_E_Brake_From_TM(boolean b) {
        e_Brake_On = b;
    }
}
