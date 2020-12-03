package resources;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import networking.Network;
import networking.Train_Model_Interface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Train_Model {
    static double car_Length = 32.2; // meters
    static int car_Mass = 40900; //kg
    static double static_friction_coefficient = 0.05;  // steel on steel (clean and dry conditions)
    static double kinetic_friction_coefficient = 0.01; // steel on steel (clean and dry conditions)
    static int average_Person_Mass = 70; //kg
    double static_friction; //N
    double kinetic_friction; //N
    static double brake_acc = 1.2;
    static double e_brake_acc = 2.7;
    public int id;
    private boolean int_Lights = false;
    private boolean ext_Lights = false;
    private boolean left_Door_Status = false;
    private boolean right_Door_Status = false;
    private double temperature = 20; // celsius
    private int wheel_Slippage;
    private int power_Consumption;
    private double engine_Power;
    private boolean brake_Status;
    private boolean emergency_Brake_Status = false;
    private double velocity;
    public int num_Cars;
    private double distance;
    private double time = 0;
    private boolean advertisements;
    private boolean announcements;
    private double grade;
    private int passengers = 0;
    private int crew = 3;
    private String failure = "None";
    private String next_Stop;
    private int mass;
    private double acc;
    private double force;
    private double total_Force;
    private double positive_Force;
    private double neg_Force;
    private double max_Force;
    private double e_Brake_Force = 140413;
    private double brake_Force = 61720;
    private double temp_Power = 0;
    private double temp_Time;
    private double energy;
    private double heater_Power;
    private double ac_Power;
    public HashMap<Double, String> failures = new HashMap<Double, String>();

    public Train_Model(int cars, int new_id) {
        this.id = new_id;
        this.num_Cars = cars;
        this.velocity = 0;
        crew = 3;
        update_Mass();
        static_friction = static_friction_coefficient * (((crew * average_Person_Mass) + (num_Cars * car_Mass)) * 9.81); // Mu * Normal Force (the 10 is to account for the hitches in between trains greatly reducing the static friciton
        kinetic_friction = kinetic_friction_coefficient * (((crew * average_Person_Mass) + (num_Cars * car_Mass)) * 9.81);  // Mu * Normal Force
    }

    public void add_Passengers(int number) {
        this.passengers += number;
        update_Mass();
        //Network.tc_Interface.send_Num_Passengers()
    }

    public void add_Crew(int number) {
        this.crew += number;
        update_Mass();
        //Network.tc_Interface.send_Num_Crew()
    }

    public void set_Grade(double number) { this.grade = number; }

    public void send_Beacon_Information (String next_Stop, boolean door_Side) throws RemoteException {  // 0 - left, 1 - right
        Network.tc_Interface.send_Beacon_Information(id, next_Stop, door_Side);
        int temp_passengers = passengers;
        this.next_Stop = next_Stop;

        class Disembark extends Thread {
            public void run() {
                while (velocity != 0) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                passengers -= new Random().nextInt(temp_passengers);
            }

        }
        Disembark disembark = new Disembark();
        disembark.start();

    }

    public void set_Announcements(boolean announcements) { this.announcements = announcements; }

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
    }

    public void set_Emergency_Brake_Status(boolean state) throws RemoteException, FileNotFoundException {
        this.emergency_Brake_Status = state;
        if (!state) {
            Platform.runLater( () -> {
                try {
                    GUI.e_brake_image.setImage(new Image(new FileInputStream(GUI.asset_Location + "lever_off.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } else if (state) {
            Platform.runLater( () -> {
                try {
                    GUI.e_brake_image.setImage(new Image(new FileInputStream(GUI.asset_Location + "lever_on.png")));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void __set_Emergency_Brake_Status(boolean state) throws RemoteException {
        this.emergency_Brake_Status = state;
        Network.tc_Interface.update_E_Brake(id, state);
    }

    public void set_Advertisements(boolean state) { this.advertisements = state;}

    public void set_Failure(String type) throws RemoteException {
        //Take appropriate action on failure
        this.failure = type;
        failures.put(Network.server_Object.get_Current_Time(), type);
        Network.tc_Interface.set_Failure_Status(id, type);
    }


    public String get_Failure() { return failure; }
    public double get_Grade() { return this.grade; }

    public int get_Num_Cars() { return this.num_Cars; }

    public int get_Passengers() { return this.passengers; }

    public int get_Crew() { return this.crew; }

    public boolean get_Int_Lights() {
        return this.int_Lights;
    }

    public boolean get_Ext_Lights() {
        return this.ext_Lights;
    }

    public void remove_Failure_Status() {
        failure = "None";
        Platform.runLater(() -> {
            GUI.destruction_Mode.setDisable(false);
            GUI.failure_Status_Container.setVisible(false);
        });

    }

    public ArrayList<Double> get_Force() {
        ArrayList<Double> temp = new ArrayList<Double>();
        temp.add(this.force);
        temp.add(this.positive_Force);
        temp.add(this.neg_Force);
        temp.add(this.max_Force);
        return temp;
    }

    public double get_Neg_Force() { return this.neg_Force; }

    public double get_Max_Force() { return this.max_Force; }

    public String get_Next_Stop() { return this.next_Stop; }

    public boolean get_Left_Door_Status() {
        return this.left_Door_Status;
    }

    public boolean get_Right_Door_Status() {
        return this.right_Door_Status;
    }

    public double get_Temperature() {
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

    public double update_Speed(double power) throws RemoteException {
        double cycle_Time;
        System.out.println("current tim");
        if (Network.server_Object.get_Current_Time() - time > 2 * Train_Model_Catalogue.multiplier) {
            time = Network.server_Object.get_Current_Time();
            System.out.println();
            System.out.println(Train_Model_Catalogue.multiplier);
            cycle_Time = .001 * Train_Model_Catalogue.multiplier;
        } else {
            cycle_Time = Network.server_Object.get_Current_Time() - time;
            System.out.println("Current Time: " + Network.server_Object.get_Current_Time());
            System.out.println("Time: " + time);
        }
        System.out.println("Cycle time: " + cycle_Time);

        this.engine_Power = power;

        System.out.println("----------------------------");
        double current_Velocity = (double) velocity; //meters / second
        double current_power = (double) this.engine_Power; //watts

        if (power == 0 && current_Velocity == 0) {
            force = 0;
        } else {
            force = power / current_Velocity; // Newtons
        }
        System.out.println("Initial Power: " + power);
        System.out.println("Initial Current Velocity: " + current_Velocity);
        System.out.println("Force Initial: " + force);
        max_Force = ((num_Cars * car_Mass) + (passengers + crew) * average_Person_Mass) * 2.7;

        force = Math.min(force, max_Force); //force limiter

        System.out.println("Force After mins/maxes: " + force);

        positive_Force = force;

        //Force of gravity in meters
        double F_g = (num_Cars * car_Mass) * 9.81;
        double F_g_x = F_g * Math.sin(this.grade * (2*3.14159)/360);

        if (current_Velocity < .01) {
            if (force != 0) {
                neg_Force = (F_g_x + static_friction);
                if (emergency_Brake_Status) {
                    force -= (F_g_x + static_friction + (e_Brake_Force)); //assuming each car contributes the same amount of braking force
                } else if (brake_Status) {
                    force -= (F_g_x + static_friction + (brake_Force));
                } else {
                    force -= (F_g_x + static_friction);
                }
            }
        } else if (current_Velocity >=.01) {
            neg_Force = (F_g_x + kinetic_friction);
            if (emergency_Brake_Status) {
                force -= (F_g_x + e_Brake_Force);
            } else if (brake_Status) {
                force -= (F_g_x + brake_Force);
            } else {
                force -= (F_g_x + kinetic_friction);
            }
        }

        System.out.println("Force After friction and grade: " + force);

        acc = force / mass;

        System.out.println("Acc: "+ acc);

        System.out.println("cycle time: " + cycle_Time);
        current_Velocity += (acc * cycle_Time);

//        System.out.println("F_g: " + F_g);
//        System.out.println("F_g_x: " + F_g_x);
//        System.out.println("Cycle Time: " + cycle_Time);
//        System.out.println("Power:" + power);
//        System.out.println("current_v");
//        System.out.println("Force: " + force);
//        System.out.println("acceleration: " + acc);

        //Update position
        double new_Distance = current_Velocity * cycle_Time + distance;

        time = Network.server_Object.get_Current_Time();
        Network.tm_Interface.outer_Update_Occupancy(id, new_Distance - distance);
        Network.tc_Interface.send_Distance(id, distance);
        distance = new_Distance;
        velocity = current_Velocity < 0 ? 0: current_Velocity;


        GUI.refresh_Table(id);

        return velocity;

        }

    public double update_Temperature(double heating_Power, double cooling_Power) throws InterruptedException, RemoteException {
        //Write pid loop here
        //Volume of car - 291 m^3  Density of dry air - 1.2041  Mass of air 351.4 kg
        //specific heat capacity of air - 1.006 kJ/kgC
//        Network.server_Object.get_Current_Time();

        double cycle_Time;
        if (Network.server_Object.get_Current_Time() - temp_Time > 2 * Train_Model_Catalogue.multiplier) {
            temp_Time = Network.server_Object.get_Current_Time();
            System.out.println();
            System.out.println(Train_Model_Catalogue.multiplier);
            cycle_Time = .001 * Train_Model_Catalogue.multiplier;
        } else {
            cycle_Time = Network.server_Object.get_Current_Time() - temp_Time;
            System.out.println("Current Time: " + Network.server_Object.get_Current_Time());
            System.out.println("Time: " + time);
        }

        if (cooling_Power == 0 && heating_Power != 0) {
            double temp_energy = heating_Power * (cycle_Time);
            double increase = temp_energy - energy;
            temperature += increase / 353500 - .001;
            System.out.println(temperature);
            heater_Power = heating_Power;
        } else if (cooling_Power != 0 && heating_Power == 0) {
            double temp_energy = cooling_Power * (cycle_Time);
            double increase = temp_energy - energy;
            temperature -= increase / 353500 - .001;
            System.out.println(temperature);
            ac_Power = cooling_Power;
        } else {
            temperature -= .001;
        }


        Thread.sleep(100);
        temp_Time = Network.server_Object.get_Current_Time();

        GUI.refresh_Table(id);

        return temperature * 9 / 5 + 32;
    }

    public void update_Mass() {
        this.mass = (crew + passengers) * average_Person_Mass + (num_Cars * car_Mass);
    }

    public void send_Speed_Authority(double speed, int authority, double grade_in) throws RemoteException, InterruptedException {
        System.out.println("-------------------");
        System.out.println("Speed: " + speed);
        System.out.println("Authority: " + authority);
        System.out.println("Grade: " + grade_in);
        System.out.println("-------------------");
        Network.tc_Interface.set_Commanded_Speed_Authority(id, speed, authority);
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 1000; i++) {
//                    System.out.println(i);
//                    try {
//                        Thread.sleep(500);
//                        Network.tm_Interface.outer_Update_Occupancy(1, id, i);
//                    } catch (RemoteException | InterruptedException remoteException) {
//                        remoteException.printStackTrace();
//                    }
//                }
//            }
//        });

        //thread.start();

        set_Grade(grade_in);
    }

}