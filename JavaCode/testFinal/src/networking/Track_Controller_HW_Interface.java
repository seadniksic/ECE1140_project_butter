package networking;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Track_Controller_HW_Interface extends Remote {


    public void set_Block_State(int BlockNum, boolean occupancy) throws RemoteException;
    public void manual_Switch_set(boolean state) throws RemoteException;
    public void reprogramPLC(String PLC) throws RemoteException;
    public void set_Broken_Rail(int lineIndex, int BlockNum, boolean state) throws RemoteException;
    public void set_Track_Circuit_Failure(int lineIndex, int BlockNum, boolean state) throws RemoteException;
    public void set_Power_Fail(int lineIndex, int BlockNum, boolean state) throws RemoteException;


}
