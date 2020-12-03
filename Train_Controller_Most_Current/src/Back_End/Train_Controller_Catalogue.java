package Back_End;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import networking.Train_Controller_Interface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Train_Controller_Catalogue implements Train_Controller_Interface {
    private static int[] Kp;
    private static int[] Ki;
    private static List<Train_Controller> train_Controller_List = new ArrayList<>();
    public static ObservableList<String> tc_Name_List = FXCollections.observableArrayList();
    private boolean use_Defaults;
    static int train_Cont_Count = 0;
    public double simulationTime;

    public Train_Controller_Catalogue() {
        Kp = new int[5];
        Ki = new int[5];
        use_Defaults = false;
      // try {
      //     add_Train_Controller(3);
      // } catch (Exception e) {
      //     System.out.println("Train Controllers not created");
      // }
      // try {
      //     add_Train_Controller(4);
      //     add_Train_Controller(3);
      // } catch (Exception e) {
      //     System.out.println("Second batch of train controllers not created");
      // }
    }

    public void set_Defaults(int kp_Passed, int ki_Passed, int cars) throws RemoteException {
        // set the corresponding number of cars with the KP and KI values
        // arrays start @ 0


        Kp[cars-1] = kp_Passed;
        Ki[cars-1] = ki_Passed;
        System.out.println("This is the set default function, Kp: " + Kp[cars-1] + " Ki: " + Ki[cars-1] + " cars: " + cars);

    }

    public int get_Default_Kp(int cars){
        return Kp[cars-1];
    }

    public int get_Default_Ki(int cars){

        return Ki[cars-1];
    }

    public void add_Train_Controller(int cars)throws RemoteException{

        train_Controller_List.add(new Train_Controller(cars, train_Cont_Count));
        // This override/run later prevents the ovbservable list from crashing
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tc_Name_List.add("Train Controller " + (train_Cont_Count-1));
            }
        });
        train_Cont_Count += 1;

    }

    public void remove_Train_Controller(int index){

        // need handling for which instance to delete out of the Array List

    }

    public Train_Controller get_Train_Controller(int i){
        return train_Controller_List.get(i);
    }

    public int get_Train_Controller_List_Size(){
        return train_Controller_List.size();
    }

    public List get_Train_Controller_List(){
        return train_Controller_List;
    }

    public void set_Commanded_Speed_Authority(int index, int speed, int auth) throws RemoteException, InterruptedException {
        System.out.println("Index: " + index + "  Speed:  " + speed + "  Authority:  " + auth);
        get_Train_Controller(index).set_Authority(auth);
        get_Train_Controller(index).set_Commanded_Speed(speed);
    }

    public void update_Time(double time){
        simulationTime = time;
        System.out.println("simulation Time: " + simulationTime);
    }

    public void update_E_Brake(int index, boolean b){
        get_Train_Controller(index).set_E_Brake_From_TM(b);
    }
}

// fix the gui flow - kp/ki first - notification window about setting kp and ki - warning about going into manual mode ------all the warnings from basalmiq - current speed
// negate position and next stop for right now
// make the power loop not start if kp and ki are 0
//fix default settings selection for kp and ki
