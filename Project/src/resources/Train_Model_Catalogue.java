package resources;

import java.rmi.RemoteException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Network;
import networking.Train_Model_Interface;

public class Train_Model_Catalogue implements Train_Model_Interface {

    static int train_Total = 0;
    static ArrayList<Train_Model> trains = new ArrayList<Train_Model>();
    static ObservableList<String> name_List = FXCollections.observableArrayList();

    public static void create_Model(int cars) throws RemoteException {
        Train_Model new_Model = new Train_Model(cars, train_Total);
        trains.add(new_Model);
        name_List.add("Train " + (train_Total));
        create_Controller(cars);
        train_Total += 1;
    }

    public static void remove_Train(int index) {
        trains.remove(index);
    }

    public void set_Next_Stop(int train_Num, String next_Stop)  { trains.get(train_Num).set_Next_Stop(next_Stop); }

    public static void create_Controller(int cars) throws RemoteException {
        //Call Alex's create controller method
        Network.tc_Interface.add_Train_Controller(cars);
    }

    public void create_Train(int cars){
        Train_Model new_Model = new Train_Model(cars, train_Total);
        train_Total += 1;
        trains.add(new_Model);
    }

    public void send_Speed_Authority(int train_Num, int speed, int authority, double grade) throws RemoteException, InterruptedException {
        trains.get(train_Num).send_Speed_Authority(speed, authority, grade);
    }

    public static void test_Send_Speed_Authority(int train_Num, int speed, int authority, double grade) throws RemoteException, InterruptedException {
        trains.get(train_Num).send_Speed_Authority(speed, authority, grade);
    }

    //Meyers calls this function in a loop
    public double update_Speed(int train_Num, double power) {  //
        return trains.get(train_Num).update_Speed(power);
    }


    public void set_Int_Lights(int train_Num, boolean state) {
        trains.get(train_Num).set_Int_Lights(state);
    }

    public void set_Ext_Lights(int train_Num, boolean state) {
        trains.get(train_Num).set_Ext_Lights(state);
    }

    public void set_Door_Status(int train_Num, boolean state) {
        trains.get(train_Num).set_Door_Status(state);
    }

    public void update_Temperature(int train_Num, int temp) {
        trains.get(train_Num).update_Temperature(temp);
    }

    public void set_Brake_Status(int train_Num, boolean state) {
        trains.get(train_Num).set_Brake_Status(state);
    }

    public void set_Emergency_Brake_Status(int train_Num, boolean state) throws RemoteException {
        trains.get(train_Num).set_Emergency_Brake_Status(state);
    }

    public void add_Passengers(int train_Num, int passengers) throws RemoteException { trains.get(train_Num).add_Passengers(passengers);};

    public void send_Next_STop(int train_Num, String next_Stop) {  }

    public void set_Advertisements(int train_Num, boolean state) {
        trains.get(train_Num).set_Advertisements(state);
    }

    public boolean get_Int_Lights(int train_Num) {
        return trains.get(train_Num).get_Int_Lights();
    }

    public boolean get_Ext_Lights(int train_Num) {
        return trains.get(train_Num).get_Ext_Lights();
    }

    public boolean get_Door_Status(int train_Num) {
        return trains.get(train_Num).get_Door_Status();
    }

    public int get_Temperature(int train_Num) {
        return trains.get(train_Num).get_Temperature();
    }

    public boolean get_Brake_Status(int train_Num) {
        return trains.get(train_Num).get_Brake_Status();
    }

    public boolean get_Emergency_Brake_Status(int train_Num) {
        return trains.get(train_Num).get_Emergency_Brake_Status();
    }

    public double get_Engine_Power(int train_Num) { return trains.get(train_Num).get_Engine_Power(); }

    public double get_Velocity(int train_Num) { return trains.get(train_Num).get_Velocity(); }

    public boolean get_Advertisements(int train_Num) {
        return trains.get(train_Num).get_Advertisements();
    }



}
