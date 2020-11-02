package networking;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Train_Model_Interface extends Remote {

    public void create_Train(int cars) throws RemoteException; // (CTC -> track controller ->) track model -> train model
    public void send_Speed_Authority(int train_Num, int speed, int authority, double grade) throws RemoteException, InterruptedException; // (CTC -> track controller ->) track model -> train model
    public double update_Speed(int train_Num, double power) throws RemoteException; // train controller -> train model
    public void set_Int_Lights(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Ext_Lights(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Door_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void update_Temperature(int train_Num, int temp) throws RemoteException; // train controller -> train model
    public void set_Brake_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Emergency_Brake_Status(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public void set_Advertisements(int train_Num, boolean state) throws RemoteException; // train controller -> train model
    public boolean get_Int_Lights(int train_Num) throws RemoteException;
    public boolean get_Ext_Lights(int train_Num) throws RemoteException;
    public boolean get_Door_Status(int train_Num) throws RemoteException;
    public int get_Temperature(int train_Num) throws RemoteException;
    public boolean get_Brake_Status(int train_Num) throws RemoteException;
    public boolean get_Emergency_Brake_Status(int train_Num) throws RemoteException;
    public double get_Engine_Power(int train_Num) throws RemoteException;
    public double get_Velocity(int train_Num) throws RemoteException;

}
