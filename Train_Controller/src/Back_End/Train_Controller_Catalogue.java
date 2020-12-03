package Back_End;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import networking.Train_Controller_Interface;

import java.rmi.RemoteException;
import java.sql.SQLOutput;
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
        //Kp = new int[]{500000, 500000, 500000, 500000, 500000};
        //Ki = new int[]{100, 100, 100, 100, 100};
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

    public void set_Defaults(int kp_Passed, int ki_Passed, int cars, int trainEngInstance) throws RemoteException {
        // set the corresponding number of cars with the KP and KI values
        // arrays start @ 0

        Kp[cars-1] = kp_Passed;
        Ki[cars-1] = ki_Passed;
        System.out.println("This is the set default function, Kp: " + Kp[cars-1] + " Ki: " + Ki[cars-1] + " cars: " + cars);


        //set kp and ki for specific instance after default values are entered
        //get_Train_Controller(train_Cont_Count-1).set_Kp(get_Default_Kp(cars));
        //get_Train_Controller(train_Cont_Count-1).set_Ki(get_Default_Ki(cars));

        //this is a test to see if passing the parameter of train engineer index fixes this issue
        get_Train_Controller(trainEngInstance).set_Kp(get_Default_Kp(cars));
        get_Train_Controller(trainEngInstance).set_Ki(get_Default_Ki(cars));

        System.out.println("Train controller count: " + train_Cont_Count);
        System.out.println("Kp values: " + Kp[cars-1]);
        System.out.println("Ki values: " + Ki[cars-1]);

    }

    public int get_Default_Kp(int cars){
        return Kp[cars-1];
    }

    public int get_Default_Ki(int cars){

        return Ki[cars-1];
    }

    public void add_Train_Controller(int cars)throws RemoteException{

        train_Controller_List.add(new Train_Controller(cars, train_Cont_Count));

        //uses default values for Kp and Ki if they exist
        if(Kp[cars-1] != 0 && Ki[cars-1] != 0){
            get_Train_Controller(train_Cont_Count).set_Ki(get_Default_Ki(cars));
            get_Train_Controller(train_Cont_Count).set_Kp(get_Default_Kp(cars));
        }
        // This override/run later prevents the observable list from crashing
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

    public void set_Commanded_Speed_Authority(int index, double speed, int auth) throws RemoteException, InterruptedException {
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

    public void set_Failure_Status(int index, String failure){
        get_Train_Controller(index).set_Failure_Bool(true);
        get_Train_Controller(index).set_Failure_Mode(failure);
        get_Train_Controller(index).set_E_Brake_On(true);

        // create a text box that pops up and prints the error on your gui
    }

    public void remove_Failure_Status(int index) throws RemoteException {
        get_Train_Controller(index).set_Failure_Bool(false);
        get_Train_Controller(index).set_Failure_Mode("");
        get_Train_Controller(index).set_E_Brake_On(false);
        Network.tm_Interface.remove_Failure_Status(index);

        // remove the error string text form the gui
    }

    public void set_Temperature(int index, double temp) throws RemoteException, InterruptedException {
        get_Train_Controller(index).set_Desired_Temperature(temp);
        get_Train_Controller(index).set_Cabin_Temperature();
    }

    public void send_Beacon_Information(int index, String next_Stop, boolean door_Side){
        get_Train_Controller(index).set_Next_Stop(next_Stop);
        //TODO create function for handling whether the left or right doors will open when the train stops
    }

    public void send_Distance(int index, double new_Distance){
        get_Train_Controller(index).set_Position(new_Distance);
    }
}



//TODO
//double check that manual mode input is working properly (conversions)
// Make sure Zach can remove the Train Model errors from me and that that works properly
//Alerts for speed going over commanded speed when manual speed is set && Other Balsalmiq warnings // I BELIEVE THIS IS DONE
// Put error window on driver GUI (Vital and Nonvital) // Relatively high priority if possible. AlertBox maybe?
//Get position from Sead and Next stop From ???????????
// Update all labels on the GUI // LOWEST PRIORITY
//Announcements default true
