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
    private boolean door_Status;
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
    private double grade;
    private int passengers = 0;
    private int crew = 0;
    private String failure = "None";
    private String next_Stop;

    public Train_Model(int cars, int new_id) {
        this.id = new_id;
        this.num_Cars = cars;
        this.velocity = 0;
        crew = 3;
        static_friction = static_friction_coefficient * (crew * average_Person_Mass * num_Cars * car_Mass * 9.81); // Mu * Normal Force
        kinetic_friction = kinetic_friction_coefficient * (crew * average_Person_Mass * num_Cars * car_Mass * 9.81);  // Mu * Normal Force
        set_Int_Lights(true);
    }

    public void add_Passengers(int number) { this.passengers += number; }

    public void set_Grade(double number) { this.grade = number; }

    public void set_Next_Stop(String next_Stop) { this.next_Stop = next_Stop; }

    public void set_Int_Lights(boolean state) {
        this.int_Lights = state;
    }

    public void set_Ext_Lights(boolean state) {
        this.ext_Lights = state;
    }

    public void set_Door_Status(boolean state) {
        this.door_Status = state;
    }

    public void set_Brake_Status(boolean state) {
        this.brake_Status = state;
        if (GUI.current_index == id) {
            GUI.brake_Prop.setValue(brake_Status);
            GUI.main_Table.refresh();
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

    public boolean get_Door_Status() {
        return this.door_Status;
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

        force = force > 10000 ? 10000 : force; //force limiter

        //Force of gravity in meters
        double F_g = (num_Cars * car_Mass) * 9.81;
        double F_g_x = F_g * Math.sin(this.grade * (2*3.14159)/360);
        force -= (F_g_x);

        System.out.println("Power: "+ power);
        System.out.println("Force: "+ force);
        double acc;
            acc = (force / (num_Cars * car_Mass));

        if ( acc < 0 && velocity <= 0) {
            current_Velocity = 0;
            acc = 0;
        } else {
            current_Velocity += (acc * (double) cycle_Time);
        }

        System.out.println("Acceleration: " + acc);

        System.out.println("Old distance: "+ distance);
        //Update position
        double new_Distance = current_Velocity * (double) cycle_Time + distance;
        System.out.println("New Position: " + new_Distance);
        time = System.currentTimeMillis();
        //Network.tm_interface.send_Distance(new_Distance - distance);
        distance = new_Distance;
        velocity = current_Velocity;
        System.out.println("Train " + id + " Power: " + power);
        System.out.println("Train " + id + " Velocity: " + velocity);


        DecimalFormat df = new DecimalFormat("0.00");
        if (GUI.current_index == id) {
            GUI.speed_Prop.setValue(df.format(velocity));
            GUI.power_Prop.setValue(df.format(power));
            GUI.main_Table.refresh();
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