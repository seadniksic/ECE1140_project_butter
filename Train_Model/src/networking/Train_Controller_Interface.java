package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface Train_Controller_Interface extends Remote{
    public void set_Defaults(int kp_Passed, int ki_Passed, int cars, int trainEngInd) throws RemoteException;

    public int get_Default_Kp(int cars) throws RemoteException;

    public int get_Default_Ki(int cars) throws RemoteException;

    public void add_Train_Controller(int cars) throws RemoteException;

    public void remove_Train_Controller(int index) throws RemoteException;

    //public Train_Controller get_Train_Controller(int i) throws RemoteException;

    //public int get_Train_Controller_List_Size() throws RemoteException;

    //public List get_Train_Controller_List() throws RemoteException;

    public void set_Commanded_Speed_Authority(int index, double speed, int auth) throws RemoteException, InterruptedException;

    public void update_Time(double time) throws RemoteException;

    public void update_E_Brake(int index, boolean b) throws RemoteException;
    public void set_Failure_Status(int index, String failure) throws RemoteException;
}