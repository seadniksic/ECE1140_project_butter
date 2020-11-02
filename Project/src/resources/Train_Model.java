package resources;

import networking.Network;
import java.lang.Math;
import java.io.IOException;
import java.rmi.RemoteException;

public class Train_Model {
    static int car_Mass = 1000; //kg
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

    public Train_Model(int cars, int new_id) {
        id = new_id;
        num_Cars = cars;
        velocity = 0;
    }

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
    }

    public void set_Emergency_Brake_Status(boolean state) {
        this.emergency_Brake_Status = state;
    }

    public void set_Advertisements(boolean state) {this.advertisements = state;}

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

    public double get_Velocity() {return this.velocity;}

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

        double current_Velocity = (double) velocity;
        double current_power = (double) power;

        if (current_Velocity == 0) {
            current_Velocity = .1;
        }

        double force = power / current_Velocity;

        //Force of gravity in meters
        double F_g = (num_Cars * car_Mass) * 9.81;
        double F_g_x = F_g * Math.sin(grade);
        force -= F_g_x;

        System.out.println("Power: "+ power);
        System.out.println("Force: "+ force);
        double acc = force / (num_Cars * car_Mass);

        velocity = velocity + (acc * (double) cycle_Time);
        System.out.println("Acceleration: " + acc);
        double time = (double) cycle_Time;

        System.out.println("Old distance: "+ distance);
        //Update position
        double new_Distance = velocity * time + distance;
        System.out.println("New Position: " + new_Distance);
        time = System.currentTimeMillis();
        System.out.println("Train " + id + " Power: " + power);
        System.out.println("Train " + id + " Velocity: " + velocity);
        return velocity;

        }

    public int update_Temperature(double power) {
        return this.temperature;
        //Write pid loop here
    }


    public void send_Speed_Authority(int speed, int authority, double grade_in) throws RemoteException, InterruptedException {
        Network.tc_Interface.set_Commanded_Speed_Authority(id, speed, authority);
        grade = grade_in;
    }

}