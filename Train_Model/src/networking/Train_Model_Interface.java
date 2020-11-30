package networking;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Train_Model_Interface extends Remote {

    public void create_Train(int cars) throws RemoteException; // (CTC -> track controller ->) track model -> train model
    public void send_Speed_Authority(int train_Num, double speed, int authority, double grade) throws RemoteException, InterruptedException; // (CTC -> track controller ->) track model -> train model
    public void send_Beacon_Information (int train_Num, String next_Stop, boolean door_Side) throws RemoteException;
    public double update_Speed(int train_Num, double power) throws RemoteException; // train controller -> train model
    public void set_Int_Lights(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Ext_Lights(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Left_Door_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Right_Door_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Announcements(int train_Num, String announcements) throws RemoteException;
    public void update_Temperature(int train_Num, int temp) throws RemoteException; // train controller -> train model
    public void set_Brake_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Emergency_Brake_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Advertisements(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void add_Passengers(int train_Num, int passengers) throws RemoteException;
    public void add_Crew(int train_Num, int number) throws RemoteException;
    public boolean get_Int_Lights(int train_Num) throws RemoteException;
    public boolean get_Ext_Lights(int train_Num) throws RemoteException;
    public boolean get_Left_Door_Status(int train_Num) throws RemoteException;
    public boolean get_Right_Door_Status(int train_Num) throws RemoteException;
    public int get_Temperature(int train_Num) throws RemoteException;
    public boolean get_Brake_Status(int train_Num) throws RemoteException;
    public boolean get_Emergency_Brake_Status(int train_Num) throws RemoteException;
    public double get_Engine_Power(int train_Num) throws RemoteException;
    public double get_Velocity(int train_Num) throws RemoteException;
    public void remove_Train(int train_Num) throws RemoteException;
    public void update_Time(double time) throws RemoteException;
    public void remove_Failure_Status(int train_Num) throws RemoteException;
    public void update_Multiplier(int m) throws RemoteException;

}
