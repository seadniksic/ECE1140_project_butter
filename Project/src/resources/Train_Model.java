package resources;

import networking.Network;
import java.lang.Math;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;

public class Train_Model {
    static double car_Length = 32.2; // meters
    static int car_Mass = 40900; //kg
    static double static_friction_coefficient = 0.65;  // steel on steel (clean and dry conditions)
    static double kinetic_friction_coefficient = 0.42; // steel on steel (clean and dry conditions)
    static double average_Person_Mass = 70; //kg
    double static_friction; //N
    double kinetic_friction; //N
    static double brake_acc = 1.2;
    static double e_brake_acc = 2.7;
    public int id;
    private boolean int_Lights;
    private boolean ext_Lights;
    private boolean left_Door_Status;
    private boolean right_Door_Status;
    private int temperature;
    private int wheel_Slippage;
    private int power_Consumption;
    private double engine_Power;
    private boolean brake_Status;
    private boolean emergency_Brake_Status;
    private double velocity;
    public int num_Cars;
    private double distance;
    private long time;
    private boolean advertisements;
    private String announcements;
    private double grade;
    private int passengers = 0;
    private int crew = 3;
    private String failure = "None";

    public Train_Model(int cars, int new_id) {
        this.id = new_id;
        this.num_Cars = cars;
        this.velocity = 0;
        crew = 3;
        static_friction = static_friction_coefficient * (((crew * average_Person_Mass) + (num_Cars * car_Mass)) / 10 * 9.81); // Mu * Normal Force (the 10 is to account for the hitches in between trains greatly reducing the static friciton
        kinetic_friction = kinetic_friction_coefficient * (((crew * average_Person_Mass) + (num_Cars * car_Mass)) / 10 * 9.81);  // Mu * Normal Force
        set_Int_Lights(true);
    }

    public void add_Passengers(int number) { this.passengers += number; }

    public void add_Crew(int number) { this.crew += number; }

    public void set_Grade(double number) { this.grade = number; }

    public void send_Beacon_Information (String next_Stop, boolean door_Side) {  // 0 - left, 1 - right
//        Network.tc_Interface.send_Beacon_Information(next_Stop, door_Side);
//        Network.tc_Interface.send_Num_Passengers()
    }

    public void set_Announcements(String announcements) { this.announcements = announcements; }

    public void set_Int_Lights(boolean state) {
        this.int_Lights = state;
    }

    public void set_Ext_Lights(boolean state) {
        this.ext_Lights = state;
    }

    public void set_Left_Door_Status(boolean state) {
        this.left_Door_Status = state;
    }

    public void set_Right_Door_Status(boolean state) {
        this.right_Door_Status = state;
    }

    public void set_Brake_Status(boolean state) {
        this.brake_Status = state;
        if (GUI.current_index == id) {
            GUI.brake_Prop.setValue(brake_Status);
        }
    }

    public void set_Emergency_Brake_Status(boolean state) throws RemoteException {
        this.emergency_Brake_Status = state;
        Network.tc_Interface.update_E_Brake(id, true);
    }

    public void set_Advertisements(boolean state) { this.advertisements = state;}

    public void set_Failure(String type) throws RemoteException {
        //Take appropriate action on failure
        this.failure = type;

        if (failure.equals("Signal")) {
            set_Emergency_Brake_Status(true);
        } else if (failure.equals("Engine")) {
            set_Emergency_Brake_Status(true);
        }
    }

    public String get_Failure() { return failure; }
    public double get_Grade() { return this.grade; }

    public int get_Num_Cars() { return this.num_Cars; }

    public int get_Passengers() { return this.passengers; }

    public boolean get_Int_Lights() {
        return this.int_Lights;
    }

    public boolean get_Ext_Lights() {
        return this.ext_Lights;
    }

    public boolean get_Left_Door_Status() {
        return this.left_Door_Status;
    }

    public boolean get_Right_Door_Status() {
        return this.right_Door_Status;
    }

    public int get_Temperature() {
        return this.temperature;
    }

    public boolean get_Brake_Status() {
        return brake_Status;
    }

    public boolean get_Emergency_Brake_Status() {
        return this.emergency_Brake_Status;
    }

    public double get_Engine_Power() { return this.engine_Power;}

    public double get_Velocity() {
        System.out.println("Velocity: " + this.velocity);
        return this.velocity;
    }

    public boolean get_Advertisements() {return this.advertisements;}

    public double update_Speed(double power) {
        double cycle_Time;
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            cycle_Time = 1;
        } else {
            cycle_Time = (double) (System.currentTimeMillis() - time);
        }
        cycle_Time /= 1000;

        System.out.println("----------------------------");
        System.out.println("Cycle Time: " + cycle_Time);

        double current_Velocity = (double) velocity; //meters / second
        double current_power = (double) power; //watts

        double force = power / current_Velocity; // Newtons

        force = force > 74207 ? 74207 : force; //force limiter
        System.out.println("Force: "+ force);
        System.out.println("Static Friction: " + static_friction);
        System.out.println("Kinetic Friction: " + kinetic_friction);
        //Force of gravity in meters
        double F_g = (num_Cars * car_Mass) * 9.81;
        double F_g_x = F_g * Math.sin(this.grade * (2*3.14159)/360);

        if (current_Velocity < .01) {
            force -= (F_g_x + static_friction);
        } else {
            force -= (F_g_x + kinetic_friction);
        }

        System.out.println("Power: "+ power);
        //System.out.println("Force: "+ force);
        double acc = (force / (num_Cars * car_Mass));

        current_Velocity += (acc * (double) cycle_Time);

        System.out.println("Acceleration: " + acc);

        System.out.println("Old distance: "+ distance);
        //Update position
        double new_Distance = current_Velocity * (double) cycle_Time + distance;
        System.out.println("New Position: " + new_Distance);
        time = System.currentTimeMillis();
        //Network.tm_interface.send_Distance(new_Distance - distance);
        distance = new_Distance;
        velocity = current_Velocity < 0 ? 0: current_Velocity;

        System.out.println("Train " + id + " Power: " + power);
        System.out.println("Train " + id + " Velocity: " + velocity);


        DecimalFormat df = new DecimalFormat("0.00");
        if (GUI.current_index == id) {
//            GUI.speed_Prop.setValue(df.format(velocity));
//            GUI.power_Prop.setValue(df.format(power));
            System.out.println("--------------Refresh Call-----------------");
            GUI.refresh_Table(id);
        }

        return velocity;

        }

    public int update_Temperature(double power) {
        //Write pid loop here
        return temperature;
    }


    public void send_Speed_Authority(int speed, int authority, double grade_in) throws RemoteException, InterruptedException {
        Network.tc_Interface.set_Commanded_Speed_Authority(id, speed, authority);
        set_Grade(grade_in);
    }

}